package jonkoshare.gui;

import java.util.List;

/**
 * List model providing type safety.
 * 
 * @author methke01
 *
 * @param <T>
 */
public class DefaultListModel<T> extends javax.swing.DefaultListModel {

    public DefaultListModel(List<? extends T> list) {
        this();
        for (T ele : list) {
            add(ele);
        }
    }

    public DefaultListModel(T[] arr) {
        this();
        for (T ele : arr) {
            add(ele);
        }
    }

    /** Creates a new instance of DefaultListModel */
    public DefaultListModel() {
        super();
    }

    public void add(T ele) {
        super.add(getSize(), ele);
    }

    public T getElementAt(int idx) {
        @SuppressWarnings("unchecked") T element = (T) elementAt(idx);
        return element;
    }

    public T getSelectedElement() {
        return (T) getSelectedElement();
    }
}
