package org.happy.commons.patterns.observer.event;

import java.awt.event.ActionEvent;
import org.happy.commons.patterns.version.Version_1x0;

/**
 * you can use this Action Listener to fire a after you have done some thing, for
 * example you added an data to the array and you want notify registered
 * Listeners
 * 
 * @author Andreas Hollmann, Wjatscheslaw Stoljarski, Eugen Lofing
 * 
 * @param <T>
 *            data data, for example added data to array
 */
public class ActionEventAfter_1x0<T> extends ActionEvent implements Version_1x0<Float> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7243921516095753912L;

    private T data = null;

    public ActionEventAfter_1x0(Object surce, int id, String command, T data) {
        super(surce, id, command);
        this.data = data;
    }

    public ActionEventAfter_1x0(Object surce, int id, String command, int modifiers) {
        super(surce, id, command, modifiers);
    }

    public ActionEventAfter_1x0(Object surce, int id, String command, long when, int modifiers) {
        super(surce, id, command, when, modifiers);
    }

    public ActionEventAfter_1x0(Object surce, int id, String command) {
        super(surce, id, command);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Float getVersion() {
        return 1.0F;
    }

    @Override
    public String toString() {
        return "ActionEventAfter_1x0 [data=" + data + ", actionCommand=" + this.getActionCommand() + ", id=" + id + ", source=" + source + "]";
    }
}
