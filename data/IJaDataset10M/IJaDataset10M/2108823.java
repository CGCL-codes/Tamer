package clavicom.core.keygroup.mouse;

import java.awt.Color;
import javax.swing.event.EventListenerList;
import clavicom.core.listener.onClicMouseClickListener;
import clavicom.tools.TMouseKeyClickEnum;

public class CMouseKeyClick extends CMouseKey {

    TMouseKeyClickEnum click;

    protected EventListenerList listenerList;

    public CMouseKeyClick(TMouseKeyClickEnum myClick, String caption, Color myColorNormal, Color myColorClicked, Color myColorEntered, boolean holdable) {
        super(caption, myColorNormal, myColorClicked, myColorEntered, holdable);
        click = myClick;
        listenerList = new EventListenerList();
    }

    public void addOnClicMouseClickListener(onClicMouseClickListener l) {
        this.listenerList.add(onClicMouseClickListener.class, l);
    }

    public void removeOnClicMouseClickListener(onClicMouseClickListener l) {
        this.listenerList.remove(onClicMouseClickListener.class, l);
    }

    protected void fireOnClicMouseClick() {
        onClicMouseClickListener[] listeners = (onClicMouseClickListener[]) listenerList.getListeners(onClicMouseClickListener.class);
        for (int i = listeners.length - 1; i >= 0; i--) {
            listeners[i].onClicMouseClick(this);
        }
    }

    public TMouseKeyClickEnum GetClick() {
        return click;
    }

    public void Click() {
        fireOnClicMouseClick();
    }
}
