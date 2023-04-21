package com.goodcodeisbeautiful.archtea.io.data.filter;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.goodcodeisbeautiful.archtea.io.data.DataContainerAdapter;
import com.goodcodeisbeautiful.archtea.io.data.DataContainerReaderType;
import com.goodcodeisbeautiful.archtea.io.data.DefaultDataContainer;

public class FilterDataContainer extends DefaultDataContainer {

    private final Map<String, ArrayList<DataFilter>> _filtersMap = new HashMap<String, ArrayList<DataFilter>>();

    private Map<String, DataFilter> _lastDataFilterMap = null;

    private String _defaultType = null;

    public FilterDataContainer() {
        super();
    }

    public FilterDataContainer(DataContainerAdapter adapter) {
        super(adapter);
    }

    @Override
    public Reader getReader(DataContainerReaderType type) throws IOException {
        if (_lastDataFilterMap == null) connectDataFilters();
        if (_lastDataFilterMap.containsKey(type.toString())) return _lastDataFilterMap.get(type.toString()).getReader();
        if (_lastDataFilterMap.containsKey(DataContainerReaderType.DEFAULT.toString())) return _lastDataFilterMap.get(DataContainerReaderType.DEFAULT.toString()).getReader();
        return _lastDataFilterMap.get(_defaultType).getReader();
    }

    @Override
    public String getCharset() {
        try {
            if (_lastDataFilterMap == null) connectDataFilters();
            DataFilter filter = _lastDataFilterMap.containsKey(DataContainerReaderType.DEFAULT.toString()) ? _lastDataFilterMap.get(DataContainerReaderType.DEFAULT.toString()) : _lastDataFilterMap.get(_defaultType);
            String charset = filter.getCharset();
            if (charset != null) return charset;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.getCharset();
    }

    public void add(String filterType, DataFilter filter) throws IOException {
        if (_defaultType == null) _defaultType = filterType;
        ArrayList<DataFilter> filters = _filtersMap.get(filterType);
        if (filters == null) {
            if (!filter.acceptBinaryStream()) {
                throw new IOException("First filter has to be accept binary input.");
            }
            filters = new ArrayList<DataFilter>();
            _filtersMap.put(filterType, filters);
        } else {
            DataFilter lastFilter = filters.get(filters.size() - 1);
            if (lastFilter.returnBinaryStream() != filter.acceptBinaryStream()) throw new IOException("Filter cannot connect.");
        }
        filters.add(filter);
    }

    public Object clone() {
        return super.clone();
    }

    @Override
    public void setDataContainerAdapter(DataContainerAdapter adapter) {
        super.setDataContainerAdapter(adapter);
        _lastDataFilterMap = null;
        _defaultType = null;
    }

    private void connectDataFilters() throws IOException {
        _lastDataFilterMap = new HashMap<String, DataFilter>();
        for (Map.Entry<String, ArrayList<DataFilter>> entryFilters : _filtersMap.entrySet()) {
            ArrayList<DataFilter> filters = entryFilters.getValue();
            Iterator<DataFilter> it = filters.iterator();
            DataFilter prevFilter = null;
            while (it.hasNext()) {
                DataFilter filter = it.next();
                if (prevFilter == null) {
                    filter.setSource(new DataFilterAdapter(getDataContainerAdapter()));
                } else {
                    filter.setSource(prevFilter);
                }
                prevFilter = filter;
            }
            if (prevFilter.returnBinaryStream()) throw new IOException("The last filter has to be return Text stream.");
            _lastDataFilterMap.put(entryFilters.getKey(), prevFilter);
        }
    }
}
