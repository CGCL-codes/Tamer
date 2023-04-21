package syn3d.data;

import java.io.Serializable;
import simtools.data.DataInfo;
import simtools.data.DataSource;
import simtools.data.DataSourceListener;
import simtools.data.DataSourcePool;
import simtools.data.EndNotificationListener;
import simtools.data.UnsupportedOperation;

/**
 * Class description ...
 * 
 * @author Claude CAZENAVE
 *
 */
public abstract class SceneGraphData implements DataSourceListener, EndNotificationListener, Serializable {

    static final long serialVersionUID = 7036052967290949148L;

    protected static class SourceHolder {

        public DataSource ds;

        public long index;

        public SourceHolder(DataSource ds) {
            this.ds = ds;
            try {
                index = ds.getLastIndex();
            } catch (UnsupportedOperation e) {
                try {
                    index = ds.computeLastIndex();
                } catch (UnsupportedOperation e1) {
                    index = -1;
                }
            }
        }
    }

    protected transient SourceHolder[] sources;

    /** @see setDelegateListener */
    protected transient EndNotificationListener delegateListener;

    protected boolean editable;

    /**
	 * Create a SceneGraphData linked with a SceneGraphObject
	 */
    public SceneGraphData() {
        editable = false;
        sources = null;
        delegateListener = this;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean flag) {
        editable = flag;
    }

    /**
     * @return Returns the delegateListener.
     */
    public EndNotificationListener getDelegateListener() {
        return delegateListener;
    }

    /**
     * Setting a delegate end notification listener is quite important to avoid duplicate events.
     * If the same object listens to multiple SceneGraphData, it will be notified only once.
     * The data themselves are of course listeners for the data source they manage.
     * They are also the default end notificationlistener when there is no delegation
     *  
     * @param delegateListener The delegateListener to set.
     */
    public void setDelegateListener(EndNotificationListener delegateListener) {
        if (this.delegateListener == delegateListener) return;
        if (sources != null) {
            for (int i = 0; i < sources.length; ++i) if ((sources[i] != null) && (sources[i].ds != null)) sources[i].ds.removeEndNotificationListener(this.delegateListener);
        }
        this.delegateListener = delegateListener;
        if (sources != null) {
            for (int i = 0; i < sources.length; ++i) if ((sources[i] != null) && (sources[i].ds != null)) sources[i].ds.addEndNotificationListener(delegateListener);
        }
    }

    public void removeSceneGraphData() {
        if (sources != null) {
            for (int i = 0; i < sources.length; ++i) if ((sources[i] != null) && (sources[i].ds != null)) {
                sources[i].ds.removeEndNotificationListener(this.delegateListener);
                sources[i].ds.removeListener(this);
                sources[i] = null;
            }
        }
    }

    /**
	 * Get number of fields for this data
	 */
    public int length() {
        if (sources == null) {
            return 0;
        }
        return sources.length;
    }

    /**
	 * Get data source
	 * @param index = field number 0..length-1
	 */
    public DataSource getDataSource(int index) {
        if (sources[index] == null) return null;
        return sources[index].ds;
    }

    /**
	 * Set data source
	 * @param index = field number 0..length-1
	 * @param d new value for the DataSource
	 */
    public void setDataSource(int index, DataSource d) {
        if (d == null) {
            if (sources[index] != null) {
                sources[index].ds.removeEndNotificationListener(delegateListener);
                sources[index].ds.removeListener(this);
                sources[index] = null;
            }
        } else {
            if (sources[index] == null) sources[index] = new SourceHolder(d); else {
                sources[index].ds.removeEndNotificationListener(delegateListener);
                sources[index].ds.removeListener(this);
                sources[index].ds = d;
            }
            sources[index].ds.addEndNotificationListener(delegateListener);
            sources[index].ds.addListener(this);
        }
    }

    /**
	 * Initialize data with the node current values
	 */
    public abstract void init();

    /**
	 * Apply data to the node
	 */
    public abstract void apply();

    public void DataSourceIndexRangeChanged(DataSource ds, long startIndex, long lastIndex) {
        if (sources == null) return;
        for (int i = 0; i < sources.length; ++i) if ((sources[i] != null) && (ds.equals(sources[i].ds))) sources[i].index = lastIndex;
    }

    public void DataSourceReplaced(DataSource oldData, DataSource newData) {
        if (sources == null) return;
        for (int i = 0; i < sources.length; ++i) if (sources[i] != null && sources[i].ds == oldData) {
            sources[i].ds = newData;
            sources[i].ds.addEndNotificationListener(delegateListener);
            sources[i].ds.addListener(this);
        }
    }

    public void DataSourceInfoChanged(DataSource ds, DataInfo newInfo) {
    }

    public void DataSourceOrderChanged(DataSource ds, int newOrder) {
    }

    public void DataSourceValueChanged(DataSource ds, long minIndex, long maxIndex) {
    }

    public void DataSourceValueRangeChanged(DataSource ds) {
    }

    public void notificationEnd(Object referer) {
    }

    public Object clone() throws CloneNotSupportedException {
        SceneGraphData res = (SceneGraphData) super.clone();
        if (delegateListener == this) res.delegateListener = res;
        if (sources != null) {
            res.sources = new SourceHolder[sources.length];
            System.arraycopy(sources, 0, res.sources, 0, sources.length);
            for (int i = 0; i < sources.length; ++i) if ((sources[i] != null) && (sources[i].ds != null)) {
                sources[i].ds.addEndNotificationListener(res.delegateListener);
                sources[i].ds.addListener(res);
            }
        } else {
            res.sources = null;
        }
        return res;
    }

    /**
     * Inverse operation from clone()
     * Update this data from the content of another one
     */
    public void updateFrom(SceneGraphData dataCopy) {
        editable = dataCopy.editable;
        if (sources != null) {
            for (int i = 0; i < sources.length; ++i) {
                if ((sources[i] != null) && (sources[i].ds != null)) {
                    sources[i].ds.removeListener(this);
                    sources[i].ds.removeEndNotificationListener(delegateListener);
                }
            }
        }
        if (dataCopy.sources != null) {
            sources = new SourceHolder[dataCopy.sources.length];
            System.arraycopy(dataCopy.sources, 0, sources, 0, sources.length);
        } else {
            sources = null;
        }
        if (dataCopy.delegateListener == dataCopy) delegateListener = this; else delegateListener = dataCopy.delegateListener;
        if (sources != null) {
            for (int i = 0; i < sources.length; ++i) {
                if ((sources[i] != null) && (sources[i].ds != null)) {
                    sources[i].ds.addListener(this);
                    sources[i].ds.addEndNotificationListener(delegateListener);
                }
            }
        }
    }

    /**
     * Cleanup anything that could possibly help the garbage collector.
     * Ref counted listeners may be a good start...
     */
    public void dispose() {
        if (sources != null) {
            for (int i = 0; i < sources.length; ++i) {
                if ((sources[i] != null) && (sources[i].ds != null)) {
                    sources[i].ds.removeListener(this);
                    sources[i].ds.removeEndNotificationListener(delegateListener);
                }
                sources[i] = null;
            }
        }
        sources = null;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.defaultWriteObject();
        if (sources == null) out.writeBoolean(false); else {
            out.writeBoolean(true);
            out.writeInt(sources.length);
            for (int i = 0; i < sources.length; i++) if (sources[i] != null) {
                out.writeBoolean(true);
                DataSourcePool.global.writeDataSource(out, sources[i].ds);
            } else out.writeBoolean(false);
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws java.lang.ClassNotFoundException, java.io.IOException {
        in.defaultReadObject();
        delegateListener = this;
        if (in.readBoolean()) {
            int size = in.readInt();
            sources = new SourceHolder[size];
            for (int i = 0; i < sources.length; ++i) {
                if (in.readBoolean()) {
                    sources[i] = new SourceHolder(DataSourcePool.global.readDataSource(in));
                    sources[i].ds.addEndNotificationListener(delegateListener);
                    sources[i].ds.addListener(this);
                }
            }
        } else sources = null;
    }
}
