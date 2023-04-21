package net.sf.table4gwt.client.model.impl;

import java.io.Serializable;
import net.sf.table4gwt.client.model.EditableRectangularModel;

/**
 * This model supports editing data. Data objects should implement {@link Serializable}.
 * 
 * @author Vitaliy Semochkin aka Yilativs <a href="mailto:vitaliy.se@gmail.com">
 * 
 */
public class SimpleRectangularDataModel implements EditableRectangularModel, Serializable {

    private static final long serialVersionUID = -4738359848316086234L;

    private Serializable[][] data;

    protected void setData(Serializable[][] data) {
        this.data = data;
    }

    SimpleRectangularDataModel() {
    }

    public SimpleRectangularDataModel(Serializable[][] data) {
        this.data = data;
    }

    public Object get(int row, int col) {
        return data[row][col];
    }

    public int getColumnCount() {
        return data.length > 0 ? data[0].length : 0;
    }

    public int getRowCount() {
        return data.length;
    }

    protected Object[][] getData() {
        return data;
    }

    public void set(Object obj, int row, int col) {
        if (obj instanceof Serializable) {
            data[row][col] = (Serializable) obj;
        } else {
            throw new IllegalArgumentException(obj + " must be serialzable");
        }
    }
}
