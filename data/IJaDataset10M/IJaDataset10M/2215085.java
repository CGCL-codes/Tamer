package org.ala.dao;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.KeyRange;
import org.apache.cassandra.thrift.KeySlice;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.scale7.cassandra.pelops.Bytes;
import org.scale7.cassandra.pelops.Pelops;
import org.scale7.cassandra.pelops.Selector;

/**
 * An implementation of a scanner for Cassandra. 
 * 
 * TODO extend this to include support for retrieving multiple
 * columns in a scan.
 * 
 * - 2011-11-22: Changed to use Pelops instead of using thrift directly
 * - 2011=11-12: Added the ability to retrieve the record values that were requested
 *
 * @author Dave Martin (David.Martin@csiro.au)
 */
public class CassandraScanner implements Scanner {

    private String keySpace;

    private int pageSize = 100;

    private List<KeySlice> keySlices;

    private int countInSlice = 0;

    private Cassandra.Client clientConnection;

    private SlicePredicate slicePredicate;

    private ColumnParent columnParent;

    private String pool;

    private String columnFamily;

    private Selector selector;

    private Map<Bytes, List<Column>> rowMap;

    private List<Bytes> rowList;

    private HashMap<String, String> currentValues = new HashMap<String, String>();

    ObjectMapper mapper = new ObjectMapper();

    public CassandraScanner(String pool, String keySpace, String columnFamily, String startKey, String... column) throws Exception {
        this.pool = pool;
        this.keySpace = keySpace;
        this.selector = Pelops.createSelector(pool);
        this.slicePredicate = (column != null && column.length > 0) ? Selector.newColumnsPredicate(column) : Selector.newColumnsPredicateAll(false);
        this.columnFamily = columnFamily;
        if (startKey == null) startKey = "";
        KeyRange keyRange = Selector.newKeyRange(startKey, "", pageSize + 1);
        rowMap = selector.getColumnsFromRows(columnFamily, keyRange, slicePredicate, ConsistencyLevel.ONE);
        rowList = new ArrayList<Bytes>(rowMap.keySet());
        mapper.getDeserializationConfig().set(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private void initCurrentValues(List<Column> columns) throws Exception {
        currentValues.clear();
        for (Column column : columns) {
            String name = new String(column.getName(), "UTF-8");
            String value = new String(column.getValue(), "UTF-8");
            currentValues.put(name, value);
        }
    }

    public Map<String, String> getCurrentValues() throws Exception {
        return currentValues;
    }

    public Comparable getValue(String column, Class theClass) throws Exception {
        if (currentValues.containsKey(column)) {
            if (theClass == String.class) return currentValues.get(column);
            return (Comparable) mapper.readValue(currentValues.get(column), theClass);
        }
        return null;
    }

    public List<Comparable> getListValue(String column, Class theClass) throws Exception {
        if (currentValues.containsKey(column)) {
            return mapper.readValue(currentValues.get(column), TypeFactory.collectionType(ArrayList.class, theClass));
        }
        return new ArrayList<Comparable>();
    }

    /**
	 * @see org.ala.dao.Scanner#getNextGuid()
	 */
    @Override
    public byte[] getNextGuid() throws Exception {
        if (rowList.size() > countInSlice) {
            byte[] guid = rowList.get(countInSlice).toByteArray();
            initCurrentValues(rowMap.get(rowList.get(countInSlice)));
            countInSlice++;
            return guid;
        } else if (!rowList.isEmpty()) {
            Bytes lastBytes = rowList.get(rowList.size() - 1);
            String lastKey = lastBytes.toUTF8();
            KeyRange keyRange = Selector.newKeyRange(lastKey, "", pageSize + 1);
            rowMap = selector.getColumnsFromRows(columnFamily, keyRange, slicePredicate, ConsistencyLevel.ONE);
            rowList = new ArrayList<Bytes>(rowMap.keySet());
            rowList.remove(lastBytes);
            countInSlice = 0;
            if (rowList.isEmpty()) {
                return null;
            } else {
                byte[] guid = rowList.get(countInSlice).toByteArray();
                initCurrentValues(rowMap.get(rowList.get(countInSlice)));
                countInSlice++;
                return guid;
            }
        } else {
            return null;
        }
    }

    /**
	 * @return the pageSize
	 */
    public int getPageSize() {
        return pageSize;
    }

    /**
	 * @param pageSize the pageSize to set
	 */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
