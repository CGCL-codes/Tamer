package org.zkoss.zul;

import org.zkoss.lang.Objects;
import org.zkoss.zul.event.ChartDataEvent;
import org.zkoss.zul.event.ChartDataListener;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A Category data model implementation of {@link CategoryModel}.
 * A Category model is an N series of (category, value) data objects.
 *
 * @author henrichen
 * @see CategoryModel
 * @see Chart
 */
public class SimpleCategoryModel extends AbstractChartModel implements CategoryModel {

    private static final long serialVersionUID = 20091008183445L;

    private Map _seriesMap = new HashMap(13);

    private List _seriesList = new ArrayList(13);

    private Map _categoryMap = new HashMap(13);

    private List _categoryList = new ArrayList(13);

    private Map _valueMap = new LinkedHashMap(79);

    public Comparable getSeries(int index) {
        return (Comparable) _seriesList.get(index);
    }

    public Comparable getCategory(int index) {
        return (Comparable) _categoryList.get(index);
    }

    public Collection getSeries() {
        return _seriesList;
    }

    public Collection getCategories() {
        return _categoryList;
    }

    public Collection getKeys() {
        return _valueMap.keySet();
    }

    public Number getValue(Comparable series, Comparable category) {
        List key = new ArrayList(2);
        key.add(series);
        key.add(category);
        Number num = (Number) _valueMap.get(key);
        return num;
    }

    public void setValue(Comparable series, Comparable category, Number value) {
        List key = new ArrayList(2);
        key.add(series);
        key.add(category);
        if (!_valueMap.containsKey(key)) {
            if (!_categoryMap.containsKey(category)) {
                _categoryMap.put(category, new Integer(1));
                _categoryList.add(category);
            } else {
                Integer count = (Integer) _categoryMap.get(category);
                _categoryMap.put(category, new Integer(count.intValue() + 1));
            }
            if (!_seriesMap.containsKey(series)) {
                _seriesMap.put(series, new Integer(1));
                _seriesList.add(series);
            } else {
                Integer count = (Integer) _seriesMap.get(series);
                _seriesMap.put(series, new Integer(count.intValue() + 1));
            }
        } else {
            Number ovalue = (Number) _valueMap.get(key);
            if (Objects.equals(ovalue, value)) {
                return;
            }
        }
        _valueMap.put(key, value);
        fireEvent(ChartDataEvent.CHANGED, series, category);
    }

    public void removeValue(Comparable series, Comparable category) {
        List key = new ArrayList(2);
        key.add(series);
        key.add(category);
        if (!_valueMap.containsKey(key)) {
            return;
        }
        _valueMap.remove(key);
        int ccount = ((Integer) _categoryMap.get(category)).intValue();
        if (ccount > 1) {
            _categoryMap.put(category, new Integer(ccount - 1));
        } else {
            _categoryMap.remove(category);
            _categoryList.remove(category);
        }
        int scount = ((Integer) _seriesMap.get(series)).intValue();
        if (scount > 1) {
            _seriesMap.put(series, new Integer(scount - 1));
        } else {
            _seriesMap.remove(series);
            _seriesList.remove(series);
        }
        fireEvent(ChartDataEvent.REMOVED, series, category);
    }

    public void clear() {
        _seriesMap.clear();
        _seriesList.clear();
        _categoryMap.clear();
        _categoryList.clear();
        _valueMap.clear();
        fireEvent(ChartDataEvent.REMOVED, null, null);
    }
}
