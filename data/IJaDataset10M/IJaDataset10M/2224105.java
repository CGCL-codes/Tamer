package com.maiereni.web.sample.processor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.maiereni.web.sample.vo.CellValue;

/**
 * The in-memory status of the table
 * 
 * @author Petre Maierean
 *
 */
public class CellsMap implements Serializable {

    public static final long serialVersionUID = 1L;

    private static final String CELL_NAME_PREFIX = "cell";

    private transient Map<String, String> cells = new HashMap<String, String>();

    public Map getCells() {
        return cells;
    }

    public void initialize(int numberOfCells) {
        if (cells == null) cells = new HashMap<String, String>();
        cells.clear();
        for (int i = 0; i < numberOfCells; i++) {
            cells.put(getCellName(i), "value of " + i);
        }
    }

    public void setCells(Map<String, String> cells) {
        this.cells = cells;
    }

    public Collection<CellValue> getCellValues() {
        int size = getSize();
        Collection<CellValue> ret = new ArrayList<CellValue>();
        for (int i = 0; i < size; i++) {
            String cellId = getCellName(i);
            CellValue value = new CellValue();
            value.setId(cellId);
            value.setValue((String) cells.get(cellId));
            ret.add(value);
        }
        return ret;
    }

    public CellValue getValueOfCell(String cellId) {
        String val = (String) cells.get(cellId);
        if (val == null) return null;
        CellValue value = new CellValue();
        value.setId(cellId);
        value.setValue(val);
        return value;
    }

    public String getCellName(int ix) {
        return CellsMap.CELL_NAME_PREFIX + ix;
    }

    public void addValueOfCell(String cellId, String value) {
        cells.remove(cellId);
        if (value != null) cells.put(cellId, value);
    }

    public int getSize() {
        if (cells == null || cells.keySet() == null) initialize(9);
        return cells.keySet().size();
    }
}
