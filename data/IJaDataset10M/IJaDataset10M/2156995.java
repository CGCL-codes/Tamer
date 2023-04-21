package meraner81.jets.processing.parser.mapparser.model;

import java.util.ArrayList;
import java.util.List;

public class Box {

    private List<Row> rows = new ArrayList<Row>();

    public void add(Row row) {
        rows.add(row);
    }

    public Row getRowAt(int index) {
        return rows.get(index);
    }

    public int getRowCount() {
        return rows.size();
    }
}
