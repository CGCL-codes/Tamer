package prajna.viz.prefuse;

import prefuse.data.Table;
import prefuse.data.Tuple;

/**
 * Simple instance of a TupleLoader which loads Integer data elements into the
 * Tuple. The integer is stored in the designated label field.
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 */
public class IntegerTupleLoader extends TupleLoader<Integer> {

    /**
     * Determine whether the given data and tuple represent the same data
     * 
     * @param data the data element
     * @param tuple the tuple
     * @return true if they represent the same data, false otherwise.
     */
    @Override
    protected boolean dataMatches(Integer data, Tuple tuple) {
        int val = tuple.getInt(getLabelField());
        return (val == data);
    }

    /**
     * Find the original data element which matches the provided tuple. A
     * collection of original data elements is provided, and the original data
     * element should be contained within that collection. If not, null is
     * returned
     * 
     * @param tuple the Tuple to match
     * @return the original data element
     */
    @Override
    public Integer getData(Tuple tuple) {
        String label = tuple.getString(getLabelField());
        int labelVal = Integer.parseInt(label);
        return labelVal;
    }

    /**
     * Load data from the data element into the tuple. The schema of the
     * specified table is updated to make sure the tuple fields are able to be
     * loaded into the table.
     * 
     * @param data the data element
     * @param tuple the tuple to load
     */
    @Override
    public void loadData(Integer data, Tuple tuple) {
        Table table = getTable();
        if (table.getColumnType(getLabelField()) == null) {
            table.addColumn(getLabelField(), String.class);
        }
        tuple.setString(getLabelField(), data.toString());
    }
}
