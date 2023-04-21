package org.formaria.aria.data;

/**
 * <p>Adapts an DataModel to provide access to child nodes as a list in a way that
 * is more convenient for use with UI components and so that some state
 * information can be maintained.</p>
 * <p>Copyright: Copyright (c) Formaria Ltd., 1998-2003<br>
 * License:      see license.txt
 * @version $Revision: 2.3 $
 */
public class ListModelAdapter implements ModelAdapter {

    protected DataModel model;

    protected int columnIdx = -1;

    protected String columnName;

    /**
   * Constructs a new adapter for the specified model node.
   * @param src The model for which this adapter is working
   */
    public ListModelAdapter(DataModel src) {
        model = src;
    }

    /**
   * Constructs a new adapter.
   */
    public ListModelAdapter() {
    }

    /**
   * Set the index of the column to use when getting values from this adapter
   * @param colIdx the (zero based) column index.
   */
    public void setKeyColumn(int colIdx) {
        columnIdx = colIdx;
    }

    /**
   * Set the name of the column to use when getting values from this adapter
   * @param colName the column name.
   */
    public void setKeyColumn(String colName) {
        columnName = colName;
    }

    /**
   * Get the number of children belong to the model node that this object adapts
   * @return the number of children
   */
    public int getNumChildren() {
        return model.getNumChildren();
    }

    /**
   * Gets the individual list item value
   * @param i The index of the listitem
   * @return The value of the listitem
   */
    public Object get(int i) {
        DataModel m = model.get(i);
        if (columnIdx >= 0) return m.get(columnIdx); else if (columnName != null) return ((DataModel) m.get(columnName)).get(); else return m.get();
    }

    /**
   * Set the value of the listitem
   * @param o The new value
   */
    public void set(Object o) {
        model.set(o);
    }

    /**
   * Gets the value of the selected item from the list.
   * @return the selected list item/node
   */
    public Object getSelected() {
        return model.get();
    }

    /**
   * Set the adapter source
   * @param src the model
   */
    public void setModel(DataModel src) {
        model = src;
    }

    /**
   * Get the model being used by this adapter
   * @return The model being used by this adapter
   */
    public DataModel getModel() {
        return model;
    }

    /**
   * Gets the name of the model node
   * @return the name
   */
    public String getTagName() {
        return model.getAttribValueAsString(BaseModel.ID_ATTRIBUTE);
    }

    /**
   * Locate a key value in the underlying data source
   * @param key the key to locate
   * @param columnIdx the index of the key column
   * @return the row/record index taht contains the first instance of the key,
   * or -1 if the key is not found
   */
    public int find(String key, int columnIdx) {
        int numChildren = model.getNumChildren();
        for (int i = 0; i < numChildren; i++) {
            String s = model.get(i).get(columnIdx).toString();
            if (s.equals(key)) return i;
        }
        return -1;
    }
}
