package jsynoptic.plugins.jfreechart;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.jfree.data.AbstractDataset;
import org.jfree.data.PieDataset;
import simtools.data.DataException;
import simtools.data.DataInfo;
import simtools.data.DataSource;
import simtools.data.DataSourceCollection;
import simtools.data.DataSourceListener;
import simtools.data.DataSourcePool;
import simtools.data.EndNotificationListener;
import simtools.data.UnsupportedOperation;
import simtools.util.NumberStringComparator;

/**
 * A JFreeChart Dataset backed-up by data sources, for pie charts
 * 
 */
public class SourcePieDataset extends AbstractDataset implements DataSourceListener, EndNotificationListener, PieDataset {

    static final long serialVersionUID = -8753520808697463640L;

    protected transient boolean dirty = false;

    public class SourceHolder implements Comparable {

        public DataSource source;

        public long index;

        /** Name override. Null => use default*/
        public String name;

        public SourceHolder(DataSource ds) {
            source = ds;
            try {
                index = ds.getLastIndex();
            } catch (UnsupportedOperation e) {
                try {
                    index = ds.computeLastIndex();
                } catch (UnsupportedOperation e1) {
                    index = 0;
                }
            }
        }

        public SourceHolder(DataSource ds, long idx) {
            this(ds, idx, null);
        }

        public SourceHolder(DataSource ds, long idx, String name) {
            source = ds;
            index = idx;
            this.name = name;
        }

        public int compareTo(Object o) {
            SourceHolder sh = (SourceHolder) o;
            return SourcePieDataset.this.sources.indexOf(this) - SourcePieDataset.this.sources.indexOf(o);
        }

        public String toString() {
            if (name != null) return name;
            String label = DataInfo.getLabel(source);
            if (label != null) return label;
            return super.toString();
        }
    }

    protected transient Vector sources = new Vector();

    protected boolean notifySourceChange = false;

    protected DataInfo info = null;

    public SourcePieDataset() {
        notifySourceChange = true;
    }

    public SourcePieDataset(DataSourceCollection dsc) {
        setDataSourceCollection(dsc);
        notifySourceChange = true;
    }

    public void setDataSourceCollection(DataSourceCollection dsc) {
        boolean notify = notifySourceChange;
        notifySourceChange = false;
        clear();
        info = dsc.getInformation();
        for (int i = 0; i < dsc.size(); ++i) {
            addSource((DataSource) dsc.get(i));
        }
        notifySourceChange = notify;
        if (notifySourceChange) fireDatasetChanged();
    }

    public void addDataSourceCollection(DataSourceCollection dsc) {
        boolean notify = notifySourceChange;
        notifySourceChange = false;
        for (int i = 0; i < dsc.size(); ++i) {
            addSource((DataSource) dsc.get(i));
        }
        notifySourceChange = notify;
        if (notifySourceChange) fireDatasetChanged();
    }

    public DataSource getSource(int i) {
        return ((SourceHolder) sources.get(i)).source;
    }

    public void addSource(DataSource ds) {
        if (ds == null) return;
        sources.add(new SourceHolder(ds));
        ds.addListener(this);
        ds.addEndNotificationListener(this);
        if (notifySourceChange) fireDatasetChanged();
    }

    public void addSource(DataSource ds, long index) {
        if (ds == null) return;
        sources.add(new SourceHolder(ds, index));
        ds.addListener(this);
        ds.addEndNotificationListener(this);
        if (notifySourceChange) fireDatasetChanged();
    }

    public void removeSource(DataSource ds) {
        if (ds == null) return;
        ds.removeListener(this);
        ds.removeEndNotificationListener(this);
        for (Iterator it = sources.iterator(); it.hasNext(); ) {
            SourceHolder sh = (SourceHolder) it.next();
            if ((sh.source != null) && sh.source.equals(ds)) {
                sh.source.removeListener(this);
                sh.source.removeEndNotificationListener(this);
                it.remove();
            }
        }
        if (notifySourceChange) fireDatasetChanged();
    }

    public void removeSource(int i) {
        SourceHolder sh = (SourceHolder) sources.get(i);
        if (sh.source != null) {
            sh.source.removeListener(this);
            sh.source.removeEndNotificationListener(this);
        }
        sources.remove(i);
        if (notifySourceChange) fireDatasetChanged();
    }

    public void clear() {
        for (Iterator it = sources.iterator(); it.hasNext(); ) {
            SourceHolder sh = (SourceHolder) it.next();
            if (sh.source != null) {
                sh.source.removeListener(this);
                sh.source.removeEndNotificationListener(this);
            }
            it.remove();
        }
        info = null;
        if (notifySourceChange) fireDatasetChanged();
    }

    /** Changes the name of the source entry at index i, overrides the default.
	 * You can revert to the default name by setting the override to null
	 * @param series the series for which to set a name override
	 * @param name the new name
	 */
    public void setName(int series, String name) {
        SourceHolder sh = (SourceHolder) sources.get(series);
        sh.name = name;
    }

    public String getName(int series) {
        SourceHolder sh = (SourceHolder) sources.get(series);
        return sh.toString();
    }

    public void setName(String name) {
        if (info == null) info = new DataInfo(name); else info.label = name;
    }

    public String getName() {
        if (info == null) return "";
        return info.label;
    }

    public Comparable getKey(int index) {
        return (Comparable) sources.get(index);
    }

    public int getIndex(Comparable key) {
        return sources.indexOf(key);
    }

    public List getKeys() {
        return sources;
    }

    public Number getValue(Comparable key) {
        SourceHolder sh = (SourceHolder) key;
        Object ret = null;
        try {
            ret = sh.source.getValue(sh.index);
        } catch (DataException e) {
            try {
                sh.index = sh.source.getLastIndex();
                ret = sh.source.getValue(sh.index);
            } catch (DataException e2) {
                return null;
            }
            return null;
        }
        if (ret == null) return null;
        if (ret instanceof Double) {
            double d = ((Number) ret).doubleValue();
            if (Double.isNaN(d)) return null;
            if (Double.isInfinite(d)) return null;
            return (Double) ret;
        }
        if (ret instanceof Float) {
            float f = ((Number) ret).floatValue();
            if (Float.isNaN(f)) return null;
            if (Float.isInfinite(f)) return null;
            return (Float) ret;
        }
        if (ret instanceof Number) return (Number) ret;
        if (ret instanceof String) return NumberStringComparator.stringToNumber((String) ret);
        return null;
    }

    public int getItemCount() {
        return sources.size();
    }

    public Number getValue(int item) {
        return getValue((Comparable) sources.get(item));
    }

    public void DataSourceValueChanged(DataSource ds, long minIndex, long maxIndex) {
        for (Iterator it = sources.iterator(); it.hasNext(); ) {
            SourceHolder sh = (SourceHolder) it.next();
            if (ds.equals(sh.source)) {
                if ((sh.index >= minIndex) && (sh.index <= maxIndex)) dirty = true;
            }
        }
    }

    public void DataSourceIndexRangeChanged(DataSource ds, long startIndex, long lastIndex) {
        for (Iterator it = sources.iterator(); it.hasNext(); ) {
            SourceHolder sh = (SourceHolder) it.next();
            if (ds.equals(sh.source)) {
                sh.index = lastIndex;
                dirty = true;
            }
        }
    }

    public void DataSourceInfoChanged(DataSource ds, DataInfo newInfo) {
        dirty = true;
    }

    public void DataSourceValueRangeChanged(DataSource ds) {
    }

    public void DataSourceOrderChanged(DataSource ds, int newOrder) {
    }

    public void DataSourceReplaced(DataSource oldData, DataSource newData) {
        for (int i = 0; i < sources.size(); ++i) {
            SourceHolder sh = (SourceHolder) sources.get(i);
            if (sh.source == oldData) {
                sh.source = newData;
                if (newData != null) {
                    sh.source.addListener(this);
                    sh.source.addEndNotificationListener(this);
                    try {
                        sh.index = newData.getLastIndex();
                    } catch (UnsupportedOperation e) {
                        try {
                            sh.index = newData.computeLastIndex();
                        } catch (UnsupportedOperation e1) {
                            sh.index = 0;
                        }
                    }
                }
                oldData.removeListener(this);
                oldData.removeEndNotificationListener(this);
                dirty = true;
            }
        }
    }

    public void notificationEnd(Object referer) {
        if (dirty) {
            fireDatasetChanged();
            dirty = false;
        }
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.defaultWriteObject();
        int n = sources.size();
        out.writeInt(n);
        for (int i = 0; i < n; ++i) {
            SourceHolder sh = (SourceHolder) sources.get(i);
            DataSourcePool.global.writeDataSource(out, sh.source);
            out.writeLong(sh.index);
            out.writeObject(sh.name);
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws java.lang.ClassNotFoundException, java.io.IOException {
        in.defaultReadObject();
        int n = in.readInt();
        sources = new Vector();
        for (int i = 0; i < n; ++i) {
            SourceHolder sh;
            sources.add(sh = new SourceHolder(DataSourcePool.global.readDataSource(in), in.readLong()));
            sh.name = (String) in.readObject();
            if (sh.source != null) {
                sh.source.addListener(this);
                sh.source.addEndNotificationListener(this);
            }
        }
    }

    public Object clone() throws CloneNotSupportedException {
        SourcePieDataset c = (SourcePieDataset) super.clone();
        c.sources = new Vector();
        c.info = DataInfo.clone(info);
        for (Iterator it = sources.iterator(); it.hasNext(); ) {
            SourceHolder sh = (SourceHolder) it.next();
            c.sources.add(c.new SourceHolder(sh.source, sh.index, sh.name));
            if (sh.source != null) {
                sh.source.addListener(c);
                sh.source.addEndNotificationListener(c);
            }
        }
        return c;
    }

    /**
	  * @return
	  */
    public SourcePieDataset cloneSet() {
        try {
            return (SourcePieDataset) clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
