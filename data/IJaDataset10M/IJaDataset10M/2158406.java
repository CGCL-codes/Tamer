package kr.ac.ssu.imc.durubi.report.designer.components.crosstab;

import java.util.Vector;

public class DRCTColumnHeaderTable {

    private Vector columnHeaderFields;

    private Vector columnHeaderFunctions;

    DRCTLabel[][] labelArray;

    private int rowCount;

    private int columnCount;

    public DRCTColumnHeaderTable(Vector fields, Vector functions) {
        this.columnHeaderFields = fields;
        this.columnHeaderFunctions = functions;
    }

    public DRCTColumnHeaderTable(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        labelArray = new DRCTLabel[rowCount][columnCount];
    }

    void assingCells(int startRowIndex, int startColumnIndex, DRCrossTabTable mainTable) {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                mainTable.setCellAt(startRowIndex + i, startColumnIndex + j, labelArray[i][j]);
            }
        }
    }

    void initLabelArray(int valueHeader, boolean existValueTitle, int valueFieldCount) {
        int rowCount = getFieldCount();
        if (existValueTitle && valueHeader == DROCrossTab.VALUE_HORIZONTAL) rowCount++;
        int columnCount = 0;
        int baseCount = 1;
        if (valueHeader == DROCrossTab.VALUE_HORIZONTAL) columnCount = baseCount = valueFieldCount;
        if (getFieldCount() > 0) {
            String field = columnHeaderFields.elementAt(getFieldCount() - 1).toString();
            int functionCount = getFunctionList(field).size();
            columnCount = baseCount * (functionCount + 1);
        }
        for (int i = getFieldCount() - 2; i >= 0; i--) {
            String field = columnHeaderFields.elementAt(i).toString();
            int functionCount = getFunctionList(field).size();
            columnCount += (baseCount * functionCount);
        }
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        labelArray = new DRCTLabel[rowCount][columnCount];
    }

    void createCells(int cellWidth, int cellHeight) {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                labelArray[i][j] = new DRCTLabel(new DRCTLabelModel());
                labelArray[i][j].getModel().nWidth = cellWidth;
                labelArray[i][j].getModel().nHeight = cellHeight;
                labelArray[i][j].oModel.setGroupInTheTable(DRCTLabelModel.COLUMN_HEADER);
                labelArray[i][j].oModel.setLabelType();
            }
        }
    }

    void mergeCells(int valueHeader, boolean existValueTitle, Vector valueFields) {
        if (existValueTitle && valueHeader == DROCrossTab.VALUE_HORIZONTAL) {
            for (int i = 0; i < columnCount; ) {
                for (int j = 0; j < valueFields.size(); j++) {
                    labelArray[rowCount - 1][i].oModel.sText = valueFields.elementAt(j).toString();
                    labelArray[rowCount - 1][i].oModel.roleInTheGroup = DRCTLabelModel.TITLE;
                    labelArray[rowCount - 1][i].oModel.sQueryField = valueFields.elementAt(j).toString();
                    i++;
                }
            }
        }
        int baseCount = 1;
        if (valueHeader == DROCrossTab.VALUE_HORIZONTAL && valueFields != null && valueFields.size() > 1) baseCount = valueFields.size();
        int columnIndex = 0;
        int fieldCount = columnHeaderFields.size();
        String fieldName = columnHeaderFields.elementAt(fieldCount - 1).toString();
        for (int j = 0; j < baseCount; j++) {
            labelArray[fieldCount - 1][j].oModel.sText = fieldName;
            labelArray[fieldCount - 1][j].oModel.roleInTheGroup = DRCTLabelModel.VALUE;
            labelArray[fieldCount - 1][j].oModel.sQueryField = fieldName;
            if (j == 0) labelArray[fieldCount - 1][j].oModel.bDrawable = true; else {
                labelArray[fieldCount - 1][j].oModel.bDrawable = false;
                labelArray[fieldCount - 1][j].mergeLabel = labelArray[fieldCount - 1][0];
                labelArray[fieldCount - 1][0].oModel.nWidth += labelArray[fieldCount - 1][j].oModel.nWidth;
            }
            columnIndex++;
        }
        Vector functions = getFunctionList(fieldName);
        for (int i = 0; i < functions.size(); i++) {
            String function = functions.elementAt(i).toString();
            int startIndex = columnIndex;
            for (int j = startIndex; j < startIndex + baseCount; j++) {
                labelArray[fieldCount - 1][j].oModel.sText = function;
                labelArray[fieldCount - 1][j].oModel.roleInTheGroup = DRCTLabelModel.SUMMARY;
                if (function.equals("�հ�")) labelArray[fieldCount - 1][j].oModel.functionType = DRCTLabelModel.SUM_FOPT; else if (function.equals("���")) labelArray[fieldCount - 1][j].oModel.functionType = DRCTLabelModel.AVG_FOPT; else if (function.equals("�ִ밪")) labelArray[fieldCount - 1][j].oModel.functionType = DRCTLabelModel.MAX_FOPT; else if (function.equals("�ּҰ�")) labelArray[fieldCount - 1][j].oModel.functionType = DRCTLabelModel.MIN_FOPT; else if (function.equals("�Ǽ�(����)")) labelArray[fieldCount - 1][j].oModel.functionType = DRCTLabelModel.CNT_FOPT;
                if (j == startIndex) labelArray[fieldCount - 1][j].oModel.bDrawable = true; else {
                    labelArray[fieldCount - 1][j].oModel.bDrawable = false;
                    labelArray[fieldCount - 1][j].mergeLabel = labelArray[fieldCount - 1][startIndex];
                    labelArray[fieldCount - 1][startIndex].oModel.nWidth += labelArray[fieldCount - 1][j].oModel.nWidth;
                }
                columnIndex++;
            }
        }
        int fieldSize = columnIndex;
        for (int i = fieldCount - 2; i >= 0; i--) {
            columnIndex = 0;
            for (int j = 0; j < fieldSize; j++) {
                fieldName = columnHeaderFields.elementAt(i).toString();
                labelArray[i][j].oModel.sText = fieldName;
                labelArray[i][j].oModel.roleInTheGroup = DRCTLabelModel.VALUE;
                labelArray[i][j].oModel.sQueryField = fieldName;
                if (j == 0) labelArray[i][j].oModel.bDrawable = true; else {
                    labelArray[i][j].oModel.bDrawable = false;
                    labelArray[i][j].mergeLabel = labelArray[i][0];
                    labelArray[i][0].oModel.nWidth += labelArray[i][j].oModel.nWidth;
                }
                columnIndex++;
            }
            functions = getFunctionList(fieldName);
            for (int k = 0; k < functions.size(); k++) {
                String function = functions.elementAt(k).toString();
                int startIndex = columnIndex;
                for (int j = startIndex; j < startIndex + baseCount; j++) {
                    for (int p = i; p < fieldCount; p++) {
                        labelArray[p][j].oModel.sText = function;
                        labelArray[p][j].oModel.roleInTheGroup = DRCTLabelModel.SUMMARY;
                        if (function.equals("�հ�")) labelArray[p][j].oModel.functionType = DRCTLabelModel.SUM_FOPT; else if (function.equals("���")) labelArray[p][j].oModel.functionType = DRCTLabelModel.AVG_FOPT; else if (function.equals("�ִ밪")) labelArray[p][j].oModel.functionType = DRCTLabelModel.MAX_FOPT; else if (function.equals("�ּҰ�")) labelArray[p][j].oModel.functionType = DRCTLabelModel.MIN_FOPT; else if (function.equals("�Ǽ�(����)")) labelArray[p][j].oModel.functionType = DRCTLabelModel.CNT_FOPT;
                        if (j == startIndex && p == i) labelArray[p][j].oModel.bDrawable = true;
                        if (j > startIndex && p == i) {
                            labelArray[p][j].oModel.bDrawable = false;
                            labelArray[p][j].mergeLabel = labelArray[i][startIndex];
                            labelArray[i][startIndex].oModel.nWidth += labelArray[p][j].oModel.nWidth;
                        }
                        if (p > i && j == startIndex) {
                            labelArray[p][j].oModel.bDrawable = false;
                            labelArray[p][j].mergeLabel = labelArray[i][startIndex];
                            labelArray[i][startIndex].oModel.nHeight += labelArray[p][j].oModel.nHeight;
                        }
                    }
                    columnIndex++;
                }
            }
            fieldSize = columnIndex;
        }
    }

    int getFieldCount() {
        return columnHeaderFields.size();
    }

    Vector getFunctionList(String field) {
        Vector tReturn = new Vector();
        for (int i = 0; i < columnHeaderFunctions.size(); i++) {
            Vector v = (Vector) columnHeaderFunctions.elementAt(i);
            if (v.elementAt(0).equals(field)) {
                tReturn = (Vector) v.elementAt(1);
                break;
            }
        }
        return tReturn;
    }

    int getRowCount() {
        return rowCount;
    }

    int getColumnCount() {
        return columnCount;
    }
}
