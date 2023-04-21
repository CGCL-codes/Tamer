package org.neodatis.odb.core.query.values;

import java.util.Iterator;
import java.util.Map;
import org.neodatis.odb.ODBRuntimeException;
import org.neodatis.odb.OID;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Values;
import org.neodatis.odb.core.NeoDatisError;
import org.neodatis.odb.core.layers.layer2.instance.InstanceBuilder;
import org.neodatis.odb.core.layers.layer2.meta.AttributeValuesMap;
import org.neodatis.odb.core.layers.layer2.meta.ClassInfo;
import org.neodatis.odb.core.layers.layer2.meta.NonNativeObjectInfo;
import org.neodatis.odb.core.query.IMatchingObjectAction;
import org.neodatis.odb.core.query.IValuesQuery;
import org.neodatis.odb.core.query.IndexTool;
import org.neodatis.odb.core.query.MatchResult;
import org.neodatis.odb.core.query.list.values.InMemoryBTreeCollectionForValues;
import org.neodatis.odb.core.query.list.values.SimpleListForValues;
import org.neodatis.odb.core.session.SessionEngine;
import org.neodatis.tool.wrappers.OdbComparable;
import org.neodatis.tool.wrappers.map.OdbHashMap;

public class GroupByValuesQueryResultAction implements IMatchingObjectAction {

    private IValuesQuery query;

    private long nbObjects;

    /** When executing a group by result, results are temporary stored in a hash map and at the end transfered to a Values objects
	 * In this case, the key of the map is the group by composed key, the value is a ValuesQueryResultAction
	 */
    private Map<OdbComparable, ValuesQueryResultAction> groupByResult;

    private Values result;

    private boolean queryHasOrderBy;

    /** An object to build instances */
    protected InstanceBuilder instanceBuilder;

    protected ClassInfo classInfo;

    private int returnArraySize;

    private String[] groupByFieldList;

    public GroupByValuesQueryResultAction(IValuesQuery query, SessionEngine storageEngine, InstanceBuilder instanceBuilder) {
        super();
        this.query = query;
        this.queryHasOrderBy = query.hasOrderBy();
        this.instanceBuilder = instanceBuilder;
        this.returnArraySize = query.getObjectActions().size();
        this.groupByFieldList = query.getGroupByFieldList();
        this.groupByResult = new OdbHashMap<OdbComparable, ValuesQueryResultAction>();
    }

    public void add(MatchResult matchResult, OdbComparable orderByKey) {
        ValuesMatchResult valuesMatchResult = (ValuesMatchResult) matchResult;
        AttributeValuesMap attributeValuesMap = valuesMatchResult.valuesMap;
        OdbComparable groupByKey = IndexTool.buildIndexKey("GroupBy", attributeValuesMap, groupByFieldList);
        ValuesQueryResultAction result = groupByResult.get(groupByKey);
        if (result == null) {
            result = new ValuesQueryResultAction(query, null, instanceBuilder);
            result.start();
            groupByResult.put(groupByKey, result);
        }
        result.add(matchResult, orderByKey);
    }

    public void start() {
    }

    public void end() {
        if (query != null && query.hasOrderBy()) {
            result = new InMemoryBTreeCollectionForValues((int) nbObjects, query.getOrderByType());
        } else {
            result = new SimpleListForValues((int) nbObjects);
        }
        Iterator iterator = groupByResult.keySet().iterator();
        ValuesQueryResultAction vqra = null;
        OdbComparable key = null;
        while (iterator.hasNext()) {
            key = (OdbComparable) iterator.next();
            vqra = (ValuesQueryResultAction) groupByResult.get(key);
            vqra.end();
            merge(key, vqra.getValues());
        }
    }

    private void merge(OdbComparable key, Values values) {
        while (values.hasNext()) {
            if (queryHasOrderBy) {
                result.addWithKey(key, values.nextValues());
            } else {
                result.add(values.nextValues());
            }
        }
    }

    public <T> Objects<T> getObjects() {
        return (Objects<T>) result;
    }

    public void addNnoi(OID oid, NonNativeObjectInfo object, OdbComparable orderByKey) {
        throw new ODBRuntimeException(NeoDatisError.NOT_YET_IMPLEMENTED);
    }

    public void addObject(OID oid, Object object, OdbComparable orderByKey) {
        throw new ODBRuntimeException(NeoDatisError.NOT_YET_IMPLEMENTED);
    }
}
