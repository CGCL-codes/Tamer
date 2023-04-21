package cn.shining365.webclips.clipper.extractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.apache.log4j.Logger;
import cn.shining365.webclips.clipper.type.ComplexType;
import cn.shining365.webclips.clipper.type.SimpleType;
import cn.shining365.webclips.clipper.type.Type;
import cn.shining365.webclips.sheet.Sheet;
import cn.shining365.webclips.sheet.Sheet.Row;

class SheetAdjuster {

    private static final Logger logger = Logger.getLogger(SheetAdjuster.class);

    /**对sheet进行结构调整（合并被网页结构拆分开的表）。*/
    Sheet adjust(Sheet sheet, Type baseType) {
        if (baseType instanceof SimpleType) {
            return sheet;
        }
        dfsAdjust(sheet, (ComplexType) baseType, new Stack<Integer>());
        if (sheet.getRow().size() == 1 && sheet.getRow().get(0).getCell().size() == 1 && sheet.getRow().get(0).getCell().get(0).getSheet() != null) {
            return sheet.getRow().get(0).getCell().get(0).getSheet();
        }
        return sheet;
    }

    /**
	 * 调整类型为complexType的表格。每次调用本方法，则调整complexType对应的嵌套表类型。
	 * 
	 * @param sheet 本表格的数据
	 * @param complexType 本表格的类型，而且必须已经过清理。本方法返回时，该参数变
	 * 为调整后的类型。
	 * @param columnPath 相对于顶级表格，本表格所处的位置。如果为空，表示顶级表格；
	 * columnPath顶层为顶级表格的列序号；次层为次级表格的列序号；以此类推。
	 * */
    private void dfsAdjust(Sheet sheet, final ComplexType complexType, Stack<Integer> columnPath) {
        for (int i = 0; i < complexType.types.size(); i++) {
            Type type = complexType.types.get(i);
            if (type instanceof SimpleType) {
                continue;
            }
            ComplexType ct = (ComplexType) type;
            columnPath.push(i);
            dfsAdjust(sheet, ct, columnPath);
            complexType.types.set(i, ct);
            if (complexType.repetitions.get(i) != 1 && complexType.repetitions.get(i) != 0 && ct.repetitions.size() == 1 && ct.repetitions.get(0) != 1 && ct.repetitions.get(0) != 0) {
                if (complexType.repetitions.get(0) >= 2 && ct.repetitions.get(0) >= 2) {
                    complexType.repetitions.set(i, complexType.repetitions.get(0) * ct.repetitions.get(0));
                } else {
                    complexType.repetitions.set(i, -2);
                }
                complexType.types.set(i, ct.types.get(0));
                merge(sheet, new ArrayList<Integer>(columnPath));
            }
            columnPath.pop();
        }
        if (complexType.types.size() < 2) {
            return;
        }
        Map<Integer, List<Integer>> repetition2indexes = groupByRepetition(complexType.repetitions);
        for (List<Integer> indexes : repetition2indexes.values()) {
            if (indexes.size() < 2) {
                continue;
            }
            ComplexType joinedType;
            Type type = complexType.types.get(indexes.get(0));
            if (type instanceof ComplexType) {
                joinedType = (ComplexType) type;
            } else {
                joinedType = new ComplexType();
                joinedType.types.add(type);
                joinedType.repetitions.add(1);
            }
            for (int i = indexes.size() - 1; i >= 1; i--) {
                Type t = complexType.types.get(indexes.get(i));
                if (t instanceof SimpleType) {
                    joinedType.types.add(1, t);
                    joinedType.repetitions.add(1, 1);
                } else {
                    ComplexType ct = (ComplexType) t;
                    for (int l = ct.types.size() - 1; l >= 0; l--) {
                        joinedType.types.add(1, ct.types.get(l));
                        joinedType.repetitions.add(1, ct.repetitions.get(l));
                    }
                }
                complexType.types.remove(indexes.get(i).intValue());
                complexType.repetitions.remove(indexes.get(i).intValue());
            }
            complexType.types.set(indexes.get(0), joinedType);
            join(sheet, new ArrayList<Integer>(columnPath), indexes, joinedType.getName());
        }
    }

    private Map<Integer, List<Integer>> groupByRepetition(List<Integer> repetitions) {
        Map<Integer, List<Integer>> repetition2indexes = new HashMap<Integer, List<Integer>>();
        for (int i = 0; i < repetitions.size(); i++) {
            int repetition = repetitions.get(i);
            if (repetition <= 1) {
                continue;
            }
            List<Integer> indexes = repetition2indexes.get(repetition);
            if (indexes == null) {
                indexes = new ArrayList<Integer>();
                repetition2indexes.put(repetition, indexes);
            }
            indexes.add(i);
        }
        return repetition2indexes;
    }

    /**
	 * 对大表内行数相同的平行小表进行联合，即把如下形式的表：
	 * <table border="1" bordercolor="blue"><tr>
	 * <td><table border="1" bordercolor="orange">
	 * <tr><td>1</td></tr>
	 * <tr><td>3</td></tr>
	 * </table></td>
	 * <td><table border="1" bordercolor="orange">
	 * <tr><td>2</td></tr>
	 * <tr><td>4</td></tr>
	 * </table></td>
	 * </tr></table>
	 * 变换为：
	 * <table border="1" bordercolor="blue">
	 * <tr><td>1</td><td>2</td></tr>
	 * <tr><td>3</td><td>4</td></tr>
	 * </table>
	 * */
    private void join(Sheet baseSheet, List<Integer> columnPath, final List<Integer> joinIndexes, final String joinedTypeName) {
        List<Sheet> sheets = new ArrayList<Sheet>();
        sheets.add(baseSheet);
        operateSheet(sheets, columnPath, new SheetOperator() {

            @Override
            public void operate(Sheet sheet) {
                if (sheet == null) {
                    return;
                }
                for (Row row : sheet.getRow()) {
                    List<Sheet> ss = new ArrayList<Sheet>();
                    for (int j = 0; j < joinIndexes.size(); j++) {
                        ss.add(row.getCell().get(joinIndexes.get(j)).getSheet());
                    }
                    joinSheet(ss);
                    for (int j = joinIndexes.size() - 1; j >= 1; j--) {
                        row.getCell().remove(joinIndexes.get(j).intValue());
                    }
                    row.getCell().get(joinIndexes.get(0)).setName(joinedTypeName);
                }
            }
        });
    }

    /**
	 * 对大表内的同类型小表进行合并，即把如下形式的表：
	 * <table border="1" bordercolor="blue">
	 * <tr><td><table border="1" bordercolor="orange">
	 * <tr><td>1</td></tr>
	 * <tr><td>2</td></tr>
	 * </table></td></tr>
	 * <tr><td><table border="1" bordercolor="orange">
	 * <tr><td>3</td></tr>
	 * <tr><td>4</td></tr>
	 * </table></td></tr>
	 * </table>
	 * 变换为：
	 * <table border="1" bordercolor="blue">
	 * <tr><td>1</td></tr>
	 * <tr><td>2</td></tr>
	 * <tr><td>3</td></tr>
	 * <tr><td>4</td></tr>
	 * </table>
	 * @param baseSheet 顶级表对应的数据
	 * @param columnPath 大表的位置
	 * */
    private void merge(Sheet baseSheet, List<Integer> columnPath) {
        List<Sheet> sheets = new ArrayList<Sheet>();
        sheets.add(baseSheet);
        operateSheet(sheets, columnPath, new SheetOperator() {

            @Override
            public void operate(Sheet sheet) {
                if (sheet == null) {
                    return;
                }
                if (sheet.getRow().get(0).getCell().size() != 1) {
                    throw new RuntimeException("single column expected");
                }
                List<Row> rows = new ArrayList<Row>();
                for (Row row : sheet.getRow()) {
                    for (Row subrow : row.getCell().get(0).getSheet().getRow()) {
                        rows.add(subrow);
                    }
                }
                sheet.setRow(rows);
            }
        });
    }

    /**将众多子表合并到第一子表。如果子表的行数不同，则直接丢弃未完整合并的行，
	 * 而没有采用细致的方法实现对齐与空缺填补。*/
    private void joinSheet(List<Sheet> sheets) {
        if (sheets.contains(null)) {
            if (new HashSet<Sheet>(sheets).size() != 1) {
                throw new RuntimeException("DEBUG: null sheet " + "should be all or never");
            }
            return;
        }
        List<Row> firstSheetRows = sheets.get(0).getRow();
        int j;
        EACH_ROW: for (j = 0; j < firstSheetRows.size(); j++) {
            for (int k = 1; k < sheets.size(); k++) {
                if (j >= sheets.get(k).getRow().size()) {
                    break EACH_ROW;
                }
                firstSheetRows.get(j).getCell().addAll(sheets.get(k).getRow().get(j).getCell());
            }
        }
        if (j == firstSheetRows.size()) {
            return;
        }
        logger.warn("discard rows: " + (firstSheetRows.size() - j));
        for (int k = firstSheetRows.size() - 1; k >= j; k--) {
            firstSheetRows.remove(k);
        }
    }

    /**
	 * 把operator所指定的操作作用到sheets中由columnPath所指定的所有小表中。
	 * @param sheets 有子表要被操作的表
	 * @param columnPath 以sheets为基准，子表所处的列自上而下的路径（由一系列由
	 * 基准表通向要操作的子表的各列序号表示）
	 * @param operator 作用于子表的操作
	 * */
    private static void operateSheet(List<Sheet> sheets, List<Integer> columnPath, SheetOperator operator) {
        if (columnPath.size() == 0) {
            for (Sheet sheet : sheets) {
                operator.operate(sheet);
            }
            return;
        }
        List<Sheet> newList = new ArrayList<Sheet>();
        int columnIndex = columnPath.get(0);
        columnPath.remove(0);
        for (Sheet sheet : sheets) {
            if (sheet.getRow().size() == 0) {
                continue;
            }
            for (Row row : sheet.getRow()) {
                newList.add(row.getCell().get(columnIndex).getSheet());
            }
        }
        operateSheet(newList, new ArrayList<Integer>(columnPath), operator);
    }

    private static interface SheetOperator {

        public void operate(Sheet sheet);
    }
}
