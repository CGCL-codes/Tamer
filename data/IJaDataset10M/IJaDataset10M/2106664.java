package net.myfigurecollection.android.data;

import org.jdom.Element;
import android.database.AbstractCursor;

/**
 * An abstract cursor to use an xml file like a database
 * 
 * @author Climbatize
 *
 */
public class XMLCursor extends AbstractCursor {

    private String[] columns;

    private Element dataArray;

    public XMLCursor(Element data, String[] cols) {
        dataArray = data;
        columns = cols;
    }

    @Override
    public String[] getColumnNames() {
        return columns;
    }

    @Override
    public int getCount() {
        return dataArray.getChildren().size();
    }

    @Override
    public double getDouble(int column) {
        return Double.parseDouble(((Element) dataArray.getChildren().get(mPos)).getChildText(getColumnName(column)));
    }

    @Override
    public float getFloat(int column) {
        return Float.parseFloat(((Element) dataArray.getChildren().get(mPos)).getChildText(getColumnName(column)));
    }

    @Override
    public int getInt(int column) {
        return Integer.parseInt(((Element) dataArray.getChildren().get(mPos)).getChildText(getColumnName(column)));
    }

    @Override
    public long getLong(int column) {
        return Long.parseLong(((Element) dataArray.getChildren().get(mPos)).getChildText(getColumnName(column)));
    }

    @Override
    public short getShort(int column) {
        return Short.parseShort(((Element) dataArray.getChildren().get(mPos)).getChildText(getColumnName(column)));
    }

    @Override
    public String getString(int column) {
        return ((Element) dataArray.getChildren().get(mPos)).getChildText(getColumnName(column));
    }

    @Override
    public boolean isNull(int column) {
        return ((Element) dataArray.getChildren().get(mPos)).getChildText(getColumnName(column)) == null;
    }
}
