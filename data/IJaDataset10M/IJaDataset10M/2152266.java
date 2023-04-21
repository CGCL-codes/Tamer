package net.sf.osadm.linedata.domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * The unique <code>LineDataContainer</code> checks that the added
 * {@link TableData} instances contain unique data for a collection of fields.
 * <p>
 * It functions as a decorator of a {@link TableDataContainer}.
 * </p>
 *
 * @author T. Verhagen 
 * <em>Design Pattern: GOF Decorator: Concrete Decorator.</em> 
 */
public class UniqueLineDataContainer extends TableDataContainerDecorator {

    /** The <code>List</code> instance containing the field name, 
	 *  for which the values must be unique. */
    private List<String> uniqueFieldList;

    /** This <code>Map</code> instance contains for each field name 
	 *  of the uniqueFieldList and <code>Set</code> instance. The Set
	 *  is used to check if the newly added LineData instance is indeed
	 *  unique. 
	 */
    private Map<String, Set<String>> uniqueDataPerFieldMap;

    /**
	 * Constructs the unique {@link TableDataContainer} decorator.
	 * 
	 * @param  container  The container, to which the calls are forwarded.
	 * @param  uniqueFieldList  The field names, for which the data should be unique  
	 *                          over all added <code>LineData</code> instances.
	 * @throws  IllegalArgumentException  Is thrown when the container is <code>null</code> 
	 *                                    or when the uniqueFieldList is <code>null</code> or empty. 
	 */
    public UniqueLineDataContainer(TableDataContainer container, List<String> uniqueFieldList) {
        super(container);
        if (uniqueFieldList == null || uniqueFieldList.size() == 0) {
            throw new IllegalArgumentException("The uniqueFieldList should be a list" + " containing at least one field name.");
        }
        this.uniqueFieldList = uniqueFieldList;
        uniqueDataPerFieldMap = new HashMap<String, Set<String>>();
        for (Iterator<String> iter = uniqueFieldList.iterator(); iter.hasNext(); ) {
            String fieldName = iter.next();
            uniqueDataPerFieldMap.put(fieldName, new TreeSet<String>());
        }
    }

    /**
	 * Checks if the <code>LineData</code> instance contains unique data for all 
	 * required fields, before added it to the container.
	 * 
	 * @param  lineData  The <code>LineData</code> instance to add.
	 * @throws  IllegalArgumentException  Is thrown when one or multiple 
	 *          unique data fields do violate the rule.
	 */
    @SuppressWarnings("unchecked")
    public void add(TableData lineData) {
        StringBuffer strBuf = new StringBuffer();
        for (Iterator iter = uniqueFieldList.iterator(); iter.hasNext(); ) {
            String fieldName = (String) iter.next();
            String fieldData = lineData.get(fieldName);
            if (fieldData != null) {
                Set<String> uniqueDataSet = (Set<String>) uniqueDataPerFieldMap.get(fieldName);
                if (uniqueDataSet.contains(fieldData)) {
                    if (strBuf.length() > 0) {
                        strBuf.append(", ");
                    }
                    strBuf.append("( ").append(fieldName).append(": ").append(fieldData).append(" )");
                } else {
                    uniqueDataSet.add(fieldData);
                }
            }
        }
        if (strBuf.length() > 0) {
            throw new IllegalArgumentException("The following fields contained data which violates " + " the unique data rule for that field [ " + strBuf.toString() + " ].");
        }
        container.add(lineData);
    }
}
