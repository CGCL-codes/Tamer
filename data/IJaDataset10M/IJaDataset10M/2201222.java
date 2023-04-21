package com.matrixbi.adansi.ocore.client.olap.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * @author mcastillejo
 *
 */
@SuppressWarnings("serial")
public class DataRow extends BaseModelData implements Serializable {

    List<DataCell> cells = new ArrayList<DataCell>();

    public DataRow() {
        super();
    }

    public void addCell(DataCell cell) {
        cells.add(cell);
    }

    @Override
    public <X> X get(String property) {
        try {
            String[] parts = property.split("\\@");
            for (DataCell c : cells) {
                if (c.getName().equalsIgnoreCase(parts[0])) {
                    return c.get(parts[1]);
                }
            }
        } catch (Exception e) {
        }
        return super.get(property);
    }
}
