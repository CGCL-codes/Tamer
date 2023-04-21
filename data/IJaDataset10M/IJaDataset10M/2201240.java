package mipt.data.choice.event;

import mipt.common.NotifiableListenerList;
import mipt.data.Data;
import mipt.data.choice.DataChooser;
import mipt.data.choice.DataManager;
import mipt.data.event.DataEvent;

public class DefaultDataNotificator implements DataActor, DataSelector {

    protected NotifiableListenerList listenerList = new NotifiableListenerList();

    protected DataManager manager;

    private int actClickCount = 2;

    /**
 * 
 */
    public DefaultDataNotificator(DataManager manager) {
        this.manager = manager;
    }

    public final void addActListener(DataActListener listener) {
        listenerList.add(DataActListener.class, listener);
    }

    public final void addSelectListener(DataSelectListener listener) {
        listenerList.add(DataSelectListener.class, listener);
    }

    public void fireActRequired() {
        fireActRequired(getChooser().getChosenData());
    }

    public void fireActRequired(Data data) {
        listenerList.notifyListeners(DataActListener.class, "actRequired", createEvent(data), DataEvent.class);
    }

    public void fireSelected() {
        fireSelected(getChooser().getChosenData());
    }

    public final void fireSelected(Data data) {
        listenerList.notifyListeners(DataSelectListener.class, "selected", createEvent(data), DataEvent.class);
    }

    protected DataEvent createEvent(Data data) {
        return new DataEvent(manager, data);
    }

    private DataChooser getChooser() {
        return manager.getDataChooser();
    }

    /**
 * 
 * @return mipt.gui.NotifiableListenerList
 */
    public final NotifiableListenerList getListenerList() {
        return listenerList;
    }

    public final void removeActListeners() {
        listenerList.remove(DataActListener.class);
    }

    public final void removeSelectListeners() {
        listenerList.remove(DataSelectListener.class);
    }

    public final void selectData(Data dataToSelect) {
        getChooser().setChosenData(dataToSelect);
        fireSelected(dataToSelect);
    }

    /**
 * 
 * @return int
 */
    public final int getActClickCount() {
        return actClickCount;
    }

    /**
 * 
 * @param newActClickCount int
 */
    public void setActClickCount(int newActClickCount) {
        actClickCount = newActClickCount;
    }
}
