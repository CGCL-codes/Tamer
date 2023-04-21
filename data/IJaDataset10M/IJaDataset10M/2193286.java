package proj.zoie.impl.indexing;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import proj.zoie.api.DataConsumer.DataEvent;

public class MemoryStreamDataProvider<V> extends StreamDataProvider<V> {

    private List<DataEvent<V>> _list;

    private int _count;

    private boolean _stop;

    private static final Logger log = Logger.getLogger(MemoryStreamDataProvider.class);

    public MemoryStreamDataProvider() {
        super();
        _list = new LinkedList<DataEvent<V>>();
        _count = 0;
        _stop = false;
    }

    @Override
    public void reset() {
        synchronized (this) {
            _list.clear();
            this.notifyAll();
        }
    }

    public void flush() {
        synchronized (this) {
            while (!_list.isEmpty() && !_stop) {
                this.notifyAll();
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    log.warn(e.getMessage());
                }
            }
        }
    }

    public void addEvents(List<DataEvent<V>> list) {
        if (list != null && !list.isEmpty()) {
            Iterator<DataEvent<V>> iter = list.iterator();
            synchronized (this) {
                while (iter.hasNext()) {
                    DataEvent<V> obj = iter.next();
                    _count++;
                    _list.add(obj);
                }
                this.notifyAll();
            }
        }
    }

    public void addEvent(DataEvent<V> event) {
        if (event != null) {
            synchronized (this) {
                _count++;
                _list.add(event);
                this.notifyAll();
            }
        }
    }

    @Override
    public DataEvent<V> next() {
        DataEvent<V> obj = null;
        synchronized (this) {
            while (_list.isEmpty() && !_stop) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    log.warn(e.getMessage());
                }
            }
            if (!_list.isEmpty()) {
                obj = _list.remove(0);
                this.notifyAll();
            }
        }
        return obj;
    }

    public int getCount() {
        synchronized (this) {
            return _count;
        }
    }

    @Override
    public void stop() {
        try {
            synchronized (this) {
                _stop = true;
                this.notifyAll();
            }
        } finally {
            super.stop();
        }
    }
}
