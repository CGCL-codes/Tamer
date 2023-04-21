package mipt.crec.muse.data;

import mipt.data.Data;
import mipt.data.event.DataModelAdapter;
import mipt.data.event.DataModelEvent;
import mipt.util.log.Log;

/**
 * A collaborator of RemoteDataLoader.
 * initListening() waits to be notified by IO about receiving data.
 * No next calls to initReceiving() works until waiting is complete, so this must always receive some event
 *  (with eventType=GET_DATA).
 * @author Alexey Evdokimov
 */
public class RemoteDataReceiver extends DataModelAdapter {

    private DataIO parent;

    protected Data data;

    protected Object moduleKey;

    /**
	 * @param parent
	 */
    public RemoteDataReceiver(DataIO parent) {
        this.parent = parent;
    }

    public final DataIO getParent() {
        return this.parent;
    }

    /**
	 * Returns the event data obtained.
	 */
    public final Data getData() {
        return data;
    }

    /**
	 * 
	 * @param request
	 */
    public synchronized void initReceiving(Data request) {
        data = null;
        parent.addDataModelListener(this);
        moduleKey = request.get(DataIO.MODULE);
        send(request);
        try {
            wait();
        } catch (InterruptedException e) {
            Log.getLog().log(data == null ? Log.ERROR : Log.INFO, "Data loading was interrupted", e);
        }
    }

    protected void send(Data request) {
        parent.send(request);
    }

    /**
	 * @see mipt.data.event.DataModelAdapter#dataChanged(mipt.data.event.DataModelEvent)
	 */
    public synchronized void dataChanged(DataModelEvent e) {
        Data data = e.getData();
        if (data.getInt(DataIO.EVENT_TYPE) != DataProtocol.GET_DATA) return;
        if (moduleKey != null && !moduleKey.equals(data.get(DataIO.MODULE))) return;
        this.data = data;
        parent.removeDataModelListener(this);
        notify();
    }

    /**
	 * Stops threads started and remove listeners added.
	 */
    public void close() {
        parent.removeDataModelListener(this);
    }
}
