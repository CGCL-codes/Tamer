package ar.com.AmberSoft.iEvenTask.client.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ar.com.AmberSoft.iEvenTask.shared.ParamsConst;

@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class PagingLoadResult implements com.extjs.gxt.ui.client.data.PagingLoadResult, Map, Serializable {

    private Map map = new HashMap();

    public PagingLoadResult() {
    }

    ;

    @Override
    public List getData() {
        return (List) map.get(ParamsConst.DATA);
    }

    @Override
    public int getOffset() {
        return getIntValue(map.get(ParamsConst.OFFSET));
    }

    @Override
    public int getTotalLength() {
        return getIntValue(map.get(ParamsConst.TOTAL_COUNT));
    }

    @Override
    public void setOffset(int offset) {
        map.put(ParamsConst.OFFSET, new Integer(offset));
    }

    @Override
    public void setTotalLength(int totalLength) {
        map.put(ParamsConst.TOTAL_COUNT, new Integer(totalLength));
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean containsKey(Object arg0) {
        return map.containsKey(arg0);
    }

    @Override
    public boolean containsValue(Object arg0) {
        return map.containsValue(arg0);
    }

    @Override
    public Set entrySet() {
        return map.entrySet();
    }

    @Override
    public Object get(Object arg0) {
        return map.get(arg0);
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public Set keySet() {
        return map.keySet();
    }

    @Override
    public Object put(Object arg0, Object arg1) {
        return map.put(arg0, arg1);
    }

    @Override
    public void putAll(Map arg0) {
        map.putAll(arg0);
    }

    @Override
    public Object remove(Object arg0) {
        return map.remove(arg0);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Collection values() {
        return map.values();
    }

    private int getIntValue(Object value) {
        if (value != null) {
            if (value instanceof Number) {
                Number valueNumber = (Number) value;
                return valueNumber.intValue();
            }
        }
        return -1;
    }
}
