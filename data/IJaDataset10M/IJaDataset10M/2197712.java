package fitlibrary.table;

import java.util.Stack;
import fit.Parse;
import fit.exception.FitParseException;
import fitlibrary.tableOnParse.CellOnParse;
import fitlibrary.tableOnParse.RowOnParse;
import fitlibrary.tableOnParse.TableOnParse;
import fitlibrary.tableOnParse.TablesOnParse;

public class TableFactory {

    private static Stack<Boolean> stack = new Stack<Boolean>();

    private static boolean RUN_WITH_LIST_BASED = true;

    private static boolean CREATE_LIST_BASED = RUN_WITH_LIST_BASED;

    public static Tables tables() {
        if (CREATE_LIST_BASED) return new TablesOnList();
        return new TablesOnParse();
    }

    public static Tables tables(Table table) {
        if (CREATE_LIST_BASED) return new TablesOnList(table);
        return new TablesOnParse(table);
    }

    public static Tables tables(Tables tables) {
        if (CREATE_LIST_BASED) return new TablesOnList(tables);
        return new TablesOnParse(tables);
    }

    public static Tables tables(String html) throws FitParseException {
        Tables tables = new TablesOnParse(new Parse(html));
        if (RUN_WITH_LIST_BASED) {
            useOnLists(true);
            Tables convert = TableConversion.convert(tables);
            pop();
            return convert;
        }
        return tables;
    }

    public static Tables tables(Parse parse) {
        TablesOnParse tablesOnParse = new TablesOnParse(parse);
        if (CREATE_LIST_BASED) return tablesOnParse.deepCopy();
        return tablesOnParse;
    }

    public static Table table() {
        if (CREATE_LIST_BASED) {
            TableOnList tableOnList = new TableOnList();
            tableOnList.setTagLine("border=\"1\" cellspacing=\"0\"");
            return tableOnList;
        }
        return new TableOnParse();
    }

    public static Table table(Row... rows) {
        if (CREATE_LIST_BASED) return new TableOnList(rows);
        return new TableOnParse(rows);
    }

    public static Table table(Parse parse) {
        if (CREATE_LIST_BASED) throw new RuntimeException("Unable to");
        return new TableOnParse(parse);
    }

    public static Row row() {
        if (CREATE_LIST_BASED) return new RowOnList();
        return new RowOnParse();
    }

    public static Row row(String... cellTexts) {
        Row row = row();
        for (String cellText : cellTexts) row.add(cell(cellText));
        return row;
    }

    public static Row row(Cell... cells) {
        Row row = row();
        for (Cell cell : cells) row.add(cell);
        return row;
    }

    public static Cell cell(String cellText) {
        if (CREATE_LIST_BASED) return new CellOnList(cellText);
        return new CellOnParse(cellText);
    }

    public static Cell cell(Cell cell) {
        if (CREATE_LIST_BASED) return new CellOnList(cell);
        return new CellOnParse(cell);
    }

    public static Cell cell(Tables innerTables) {
        if (CREATE_LIST_BASED) return new CellOnList(innerTables);
        return new CellOnParse(innerTables);
    }

    public static void useOnLists(boolean useLists) {
        stack.push(CREATE_LIST_BASED);
        CREATE_LIST_BASED = useLists;
    }

    public static void pop() {
        CREATE_LIST_BASED = stack.pop();
    }
}
