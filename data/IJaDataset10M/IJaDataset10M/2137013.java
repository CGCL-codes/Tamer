package de.mindcrimeilab.xsanalyzer.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.jfree.data.KeyedValues;
import org.jfree.data.Values;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.PieDataset;
import org.springframework.binding.value.ValueModel;

/**
 * @author Michael Engelhardt<me@mindcrime-ilab.de>
 * @author $Author: agony $
 * @version $Revision: 165 $
 * 
 */
public class PieDatasetValueModel<T> extends AbstractDataset implements Dataset, PieDataset, KeyedValues, Values {

    private final Map<Comparable<T>, ValueModel> dataSet = new HashMap<Comparable<T>, ValueModel>();

    private final List<Comparable<T>> index = new LinkedList<Comparable<T>>();

    public void setValue(Comparable<T> key, ValueModel value) {
        dataSet.put(key, value);
        index.add(key);
        value.addValueChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                fireDatasetChanged();
            }
        });
    }

    @Override
    public int getIndex(Comparable key) {
        return index.indexOf(key);
    }

    @Override
    public Comparable getKey(int idx) {
        return index.get(idx);
    }

    @Override
    public List getKeys() {
        return Collections.unmodifiableList(index);
    }

    @Override
    public Number getValue(Comparable key) {
        return (Number) dataSet.get(key).getValue();
    }

    @Override
    public int getItemCount() {
        return index.size();
    }

    @Override
    public Number getValue(int idx) {
        return (Number) dataSet.get(index.get(idx)).getValue();
    }
}
