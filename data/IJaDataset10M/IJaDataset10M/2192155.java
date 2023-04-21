package prajna.semantic.endeca;

import java.util.*;
import com.endeca.navigation.*;

/**
 * This class represents the overall structure of the data for a particular
 * Endeca instance. It provides a number of utility methods to access
 * information from Endeca.
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 */
public class DataProfile {

    private HttpENEConnection eneConn;

    private Map<?, ?> keyProps = new HashMap<String, String>();

    private Map<String, Long> dimIdMap = new HashMap<String, Long>();

    private Map<String, Number> fieldMin = new HashMap<String, Number>();

    private Map<String, Number> fieldMax = new HashMap<String, Number>();

    /**
     * Create the data profile for a particular Endeca instance, using the
     * provided connection
     * 
     * @param conn the Endeca connection
     */
    public DataProfile(HttpENEConnection conn) {
        eneConn = conn;
        loadFieldData();
    }

    /**
     * Create the data profile for a particular Endeca instance
     * 
     * @param host the Endeca host
     * @param port the Endeca port
     */
    public DataProfile(String host, int port) {
        eneConn = new HttpENEConnection(host, port);
        loadFieldData();
    }

    /**
     * Create the data profile for a particular Endeca instance
     * 
     * @param host the Endeca host
     * @param port the Endeca port
     */
    public DataProfile(String host, String port) {
        eneConn = new HttpENEConnection(host, port);
        loadFieldData();
    }

    /**
     * <P>
     * Convert a sequence of Field-Value pairs to an Endeca query. Each string
     * in the sequence should be one of the following forms:
     * </P>
     * <ul>
     * <li>FIELD=VALUE is converted to a specific value query on the field,
     * either a dimension or property.</li>
     * <li>FIELD=VALUE* or *VALUE or *VALUE* is concerted to a search on the
     * field for the value.</li>
     * <li>FIELD>VALUE or FIELD<VALUE is a range search. FIELD must be sortable
     * </li>
     * <li>FIELD(LAT,LON)<RANGE is a Geographic range search. Lat and Lon must
     * be decimal degrees, and range must be in Kilometers</li>
     * 
     * @param queryTerms Collection of query terms
     * @return An endeca-formatted query
     */
    @SuppressWarnings("unchecked")
    public String convertQuery(Collection<String> queryTerms) {
        String queryStr = null;
        DimValIdList dimensions = new DimValIdList();
        ERecSearchList searches = new ERecSearchList();
        HashMap<String, RangeFilter> filters = new HashMap<String, RangeFilter>();
        if (queryTerms != null && !queryTerms.isEmpty()) {
            for (String query : queryTerms) {
                int eqInx = query.indexOf('=');
                int ltInx = query.indexOf('<');
                int gtInx = query.indexOf('>');
                int plInx = query.indexOf('(');
                if (eqInx != -1) {
                    String field = query.substring(0, eqInx);
                    String fieldType = getFieldType(field);
                    if (query.indexOf('*') != -1 || fieldType == null || !fieldType.equals("Dimension")) {
                        String value = query.substring(eqInx + 1).trim();
                        searches.add(new ERecSearch(field, value));
                    } else {
                        String value = query.substring(eqInx + 1).trim();
                        long dimId = findDimId(field, value);
                        if (dimId > 0) {
                            dimensions.addDimValueId(dimId);
                        }
                    }
                } else if (plInx != -1) {
                    String field = query.substring(0, plInx);
                    int prInx = query.indexOf(')');
                    String latlon = query.substring(plInx + 1, prInx - 1).trim();
                    String range = query.substring(ltInx + 1).trim();
                    RangeFilter filter = new RangeFilter(field + "|GCLT " + latlon + " " + range);
                    filters.put(field, filter);
                } else if (ltInx != -1) {
                    String field = query.substring(0, ltInx);
                    String value = query.substring(ltInx + 1).trim();
                    RangeFilter filter = filters.get(field);
                    if (filter != null) {
                        String filterStr = filter.toString();
                        filterStr = filterStr.replace("GTEQ", "BTWN") + " " + value;
                        filter = new RangeFilter(filterStr);
                        filters.put(field, filter);
                    } else {
                        filter = new RangeFilter(field + "|LTEQ " + value);
                        filters.put(field, filter);
                    }
                } else if (gtInx != -1) {
                    String field = query.substring(0, gtInx);
                    String value = query.substring(gtInx + 1).trim();
                    RangeFilter filter = filters.get(field);
                    if (filter != null) {
                        String filterStr = filter.toString();
                        filterStr = filterStr.replace("LTEQ", "BTWN " + value);
                        filter = new RangeFilter(filterStr);
                        filters.put(field, filter);
                    } else {
                        filter = new RangeFilter(field + "|GTEQ " + value);
                        filters.put(field, filter);
                    }
                }
            }
        }
        try {
            UrlENEQuery query = new UrlENEQuery("N=0", "UTF-8");
            if (!dimensions.isEmpty()) {
                query.setNavDescriptors(dimensions);
            }
            if (!searches.isEmpty()) {
                query.setNavERecSearches(searches);
            }
            if (!filters.isEmpty()) {
                RangeFilterList list = new RangeFilterList();
                list.addAll(filters.values());
                query.setNavRangeFilters(list);
            }
            queryStr = query.toString();
        } catch (ENEQueryException exc) {
            throw new RuntimeException("Cannot access Endeca engine", exc);
        }
        return queryStr;
    }

    /**
     * Find the dimension ID associated with the specified dimension value in
     * the specified dimension. Note that this does not work for dimension
     * values which are below the first level in hierarchical dimensions.
     * 
     * @param dimName the dimension name
     * @param dimValue the dimension value
     * @return The dimension ID
     */
    public long findDimId(String dimName, String dimValue) {
        long dimId = dimIdMap.get(dimName);
        long dimValId = -1;
        try {
            UrlENEQuery query = new UrlENEQuery("N=0&Ne=" + dimId, "UTF-8");
            ENEQueryResults results = eneConn.query(query);
            Navigation nav = results.getNavigation();
            DimensionList dimList = nav.getCompleteDimensions();
            Dimension dim = dimList.getDimension(dimName);
            DimValList valList = dim.getRefinements();
            for (int i = 0; i < valList.size(); i++) {
                DimVal dimVal = valList.getDimValue(i);
                if (dimVal.getName().equals(dimValue)) {
                    dimValId = dimVal.getId();
                }
            }
        } catch (Exception exc) {
            throw new RuntimeException("Cannot access Endeca engine", exc);
        }
        return dimValId;
    }

    /**
     * Find the maximum numeric value for a field
     * 
     * @param field the field name
     * @return the maximum numeric value available
     */
    public Number findFieldMax(String field) {
        Number max = null;
        try {
            UrlENEQuery query = new UrlENEQuery("N=0&Ns=" + field + "|1", "UTF-8");
            query.setNavNumERecs(1);
            ENEQueryResults results = eneConn.query(query);
            ERec rec = (ERec) results.getNavigation().getERecs().get(0);
            PropertyMap propMap = rec.getProperties();
            Collection<?> vals = propMap.getValues(field);
            String type = getFieldType(field);
            if (vals != null) {
                for (Object obj : vals) {
                    String val = obj.toString();
                    Number test = null;
                    if (type.equals("Int")) {
                        test = new Integer(val);
                    } else if (type.equals("Double")) {
                        test = new Double(val);
                    } else if (type.equals("Datetime")) {
                        test = new Long(val);
                    }
                    if (test != null) {
                        if (max == null || test.doubleValue() > max.doubleValue()) {
                            max = test;
                        }
                    } else {
                        throw new IllegalArgumentException("Mismatch format " + obj + " is not a number in field " + field);
                    }
                }
            }
        } catch (ENEQueryException exc) {
            throw new RuntimeException("Cannot query Endeca engine", exc);
        }
        fieldMax.put(field, max);
        return max;
    }

    /**
     * Find the minimum numeric value for a field
     * 
     * @param field the field name
     * @return the minimum numeric value available
     */
    public Number findFieldMin(String field) {
        Number min = null;
        try {
            UrlENEQuery query = new UrlENEQuery("N=0&Ns=" + field + "|0", "UTF-8");
            query.setNavNumERecs(1);
            ENEQueryResults results = eneConn.query(query);
            ERec rec = (ERec) results.getNavigation().getERecs().get(0);
            PropertyMap propMap = rec.getProperties();
            Collection<?> vals = propMap.getValues(field);
            String type = getFieldType(field);
            if (vals != null) {
                for (Object obj : vals) {
                    String val = obj.toString();
                    Number test = null;
                    if (type.equals("Int")) {
                        test = new Integer(val);
                    } else if (type.equals("Double")) {
                        test = new Double(val);
                    } else if (type.equals("Datetime")) {
                        test = new Long(val);
                    }
                    if (test != null) {
                        if (min == null || test.doubleValue() < min.doubleValue()) {
                            min = test;
                        }
                    } else {
                        System.err.println("Mismatch format " + obj + " is not a number in field " + field);
                    }
                }
            }
        } catch (ENEQueryException exc) {
            throw new RuntimeException("Cannot query Endeca engine", exc);
        }
        fieldMin.put(field, min);
        return min;
    }

    /**
     * Return the set of all dimension values, aka refinements, for a
     * particular dimension.
     * 
     * @param dimName The dimension name
     * @return The set of all available dimension values.
     */
    public Set<String> getAllDimValues(String dimName) {
        HashSet<String> valueSet = new HashSet<String>();
        try {
            long dimId = dimIdMap.get(dimName);
            KeyProperties prop = (KeyProperties) keyProps.get(dimName);
            PropertyMap propVals = prop.getProperties();
            String precStr = (String) propVals.get("Endeca.PrecedenceRule");
            long precId = Long.parseLong(precStr);
            if (!dimIdMap.values().contains(precId)) {
                precId = 0;
            }
            Set<DimVal> vals = getDimVals(dimId, dimId, -1, precId);
            for (DimVal val : vals) {
                valueSet.add(val.getName());
            }
        } catch (NullPointerException exc) {
        } catch (Exception exc) {
            exc.printStackTrace();
            throw new RuntimeException("Cannot access Endeca engine", exc);
        }
        return valueSet;
    }

    /**
     * Get the dimensionId for a particular field. If the field is not a
     * dimension, this method returns -1. Otherwise the dimensionId is
     * returned.
     * 
     * @param fieldName the name of the dimension
     * @return the dimension ID, or -1 if the field is not a dimension in this
     *         Endeca instance.
     */
    public long getDimensionId(String fieldName) {
        Long dimId = dimIdMap.get(fieldName);
        return (dimId == null) ? -1 : dimId;
    }

    /**
     * Get the set of dimension values for a particular dimension.
     * 
     * @param dimId the dimension ID
     * @param expandId The id to expand the dimension. Corresponds to the
     *            Endeca Ne= query
     * @param dimValId dimension value ID. Corresponds to the Endeca N= query
     * @param precId alternate dimension value ID, used when the dimension is
     *            hierarchical
     * @return the set of dimension values for this dimension
     */
    private Set<DimVal> getDimVals(long dimId, long expandId, long dimValId, long precId) {
        HashSet<DimVal> vals = new HashSet<DimVal>();
        try {
            UrlENEQuery query = null;
            if (dimValId > -1) {
                query = new UrlENEQuery("N=" + dimValId + "&Ne=" + expandId, "UTF-8");
            } else {
                query = new UrlENEQuery("N=" + precId + "&Ne=" + expandId, "UTF-8");
            }
            query.setNavNumERecs(0);
            ENEQueryResults results = eneConn.query(query);
            Navigation nav = results.getNavigation();
            DimensionList dimList = nav.getCompleteDimensions();
            Dimension dim = dimList.getDimension(dimId);
            if (dim != null) {
                DimValList valList = dim.getRefinements();
                for (int i = 0; i < valList.size(); i++) {
                    DimVal dimVal = valList.getDimValue(i);
                    if (dimVal.isNavigable()) {
                        vals.add(dimVal);
                        if (!dimVal.isLeaf()) {
                            vals.addAll(getDimVals(dimId, dimId, dimVal.getId(), precId));
                        }
                    } else {
                        vals.addAll(getDimVals(dimId, dimVal.getId(), -1, precId));
                    }
                }
            }
        } catch (ENEQueryException exc) {
            exc.printStackTrace();
        }
        return vals;
    }

    /**
     * Return the earliest date value for a particular date field.
     * 
     * @param field the name of the field
     * @return the earliest date from the field in the available Endeca records
     * @throws IllegalArgumentException if the field is not a Datetime field
     */
    public Date getEarliestDate(String field) {
        Long early = (Long) fieldMin.get(field);
        if (early == null) {
            String prop = getFieldType(field);
            if (prop == null || !prop.equals("Datetime")) {
                throw new IllegalArgumentException("Field " + field + " is not a Datetime field");
            }
            early = (Long) findFieldMin(field);
        }
        return new Date(early);
    }

    /**
     * Get the list of fields from Endeca. The set of fields includes all
     * Dimension and Property field names. If a type is specified, only those
     * fields that match the type are returned. If type is null, all fields are
     * returned
     * 
     * @param type The type, or null. Valid types are Dimension, Int, String.
     *            Double, Geocode, Datetime.
     * @return The set of fields matching the type, or all fields if type is
     *         null
     */
    public Set<String> getFields(String type) {
        HashSet<String> fieldSet = new HashSet<String>();
        for (Object key : keyProps.keySet()) {
            String keyName = (String) key;
            if (type != null) {
                KeyProperties prop = (KeyProperties) keyProps.get(key);
                PropertyMap propVals = prop.getProperties();
                String endType = (String) propVals.get("Endeca.Type");
                if (type.equals(endType)) {
                    fieldSet.add(keyName);
                }
            } else {
                fieldSet.add(keyName);
            }
        }
        return fieldSet;
    }

    /**
     * Get the field type for a given field. Valid values are Dimension, Int,
     * Double, String, Geocode, Datetime. If the field does not exist, null is
     * returned
     * 
     * @param field the Endeca field, which may be either a Dimension or
     *            Property
     * @return the field type
     */
    public String getFieldType(String field) {
        String type = null;
        KeyProperties prop = (KeyProperties) keyProps.get(field);
        if (prop != null) {
            PropertyMap propVals = prop.getProperties();
            type = (String) propVals.get("Endeca.Type");
        }
        return type;
    }

    /**
     * Get the key properties. This map contains information for each Endeca
     * data property and dimension, such as the property value type or whether
     * the dimension is multi-selectable.
     * 
     * @return A map of properties of the data
     */
    public Map<?, ?> getKeyProperties() {
        return keyProps;
    }

    /**
     * Return the latest date value for a particular date field.
     * 
     * @param field the name of the field
     * @return the latest date from the field in the available Endeca records
     * @throws IllegalArgumentException if the field is not a Datetime field
     */
    public Date getLatestDate(String field) {
        Long late = (Long) fieldMax.get(field);
        if (late == null) {
            String prop = getFieldType(field);
            if (prop == null || !prop.equals("Datetime")) {
                throw new IllegalArgumentException("Field " + field + " is not a Datetime field");
            }
            late = (Long) findFieldMax(field);
        }
        return new Date(late);
    }

    /**
     * Return the number of records for a particular query.
     * 
     * @param queryString An Endeca query String
     * @return the total number of records returned for the query
     */
    public synchronized int getRecordCount(String queryString) {
        int count = 0;
        try {
            UrlENEQuery query = new UrlENEQuery(queryString, "UTF-8");
            query.setNavNumERecs(0);
            ENEQueryResults results = eneConn.query(query);
            if (results.containsNavigation()) {
                Navigation nav = results.getNavigation();
                count = (int) nav.getTotalNumERecs();
            }
        } catch (ENEQueryException exc) {
            exc.printStackTrace();
        }
        return count;
    }

    /**
     * Load the field data for this Endeca instance
     */
    private void loadFieldData() {
        if (keyProps.isEmpty()) {
            try {
                UrlENEQuery query = new UrlENEQuery("", "UTF-8");
                query.setN("0");
                query.setNk("all");
                ENEQueryResults results = eneConn.query(query);
                Navigation nav = results.getNavigation();
                keyProps = nav.getKeyProperties();
                for (Object key : keyProps.keySet()) {
                    String keyName = (String) key;
                    KeyProperties prop = (KeyProperties) keyProps.get(key);
                    PropertyMap propVals = prop.getProperties();
                    String type = (String) propVals.get("Endeca.Type");
                    if (type == null) {
                        System.err.println("Null Type for " + keyName);
                    } else if (type.equals("Dimension") && propVals.get("Endeca.PrecedenceRule") != null) {
                        String dimIdStr = propVals.get("Endeca.DimensionId").toString();
                        long dimId = Long.parseLong(dimIdStr);
                        dimIdMap.put(keyName, dimId);
                    }
                }
            } catch (ENEQueryException exc) {
                throw new RuntimeException("Cannot access Endeca engine", exc);
            }
        }
    }
}
