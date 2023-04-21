package com.softberries.jasperberry.elements;

/**
 * Responsible for holding information
 * of the columns used by the column header section (title)
 * and details section (property) of the report
 * 
 * @author Softberries Krzysztof Grajek
 */
public class JBColumnBean {

    /**
     * Associated java bean property.
     */
    private String property;

    /**
     * Table column title shown in the report
     */
    private String title;

    /**
     * Preffered width of the column.
     */
    private int preferredWidth;

    /**
     * Construct ColumnBean object initializing at the
     * same time all properties
     * @param p name of the assciated bean property
     * @param t column title
     * @param width preferred width of the table column
     */
    public JBColumnBean(String p, String t, int width) {
        this.property = p;
        this.title = t;
        this.preferredWidth = width;
    }

    public JBColumnBean() {
    }

    public int getPreferredWidth() {
        return preferredWidth;
    }

    public void setPreferredWidth(int preferredWidth) {
        this.preferredWidth = preferredWidth;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.property;
    }
}
