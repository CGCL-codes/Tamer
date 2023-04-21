package druid.util.gui.guardians;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Method;
import org.dlib.gui.TComboBox;
import druid.core.DataTracker;

public class TComboBoxGuardian extends TComboBox implements ItemListener {

    private String method;

    private boolean intKey;

    private Object sett;

    private static Class aSetArgsString[] = { String.class };

    private static Class aSetArgsInt[] = { int.class };

    public TComboBoxGuardian(String method) {
        this(method, false);
    }

    public TComboBoxGuardian(String method, boolean intKey) {
        this.method = method;
        this.intKey = intKey;
        addItemListener(this);
    }

    public void refresh(Object s) {
        sett = null;
        if (intKey) setSelectedKey(getValueInt(s)); else setSelectedKey(getValueString(s));
        sett = s;
    }

    public void removeAllItems() {
        sett = null;
        super.removeAllItems();
    }

    public void itemStateChanged(ItemEvent e) {
        if ((sett != null) && (e.getStateChange() == ItemEvent.SELECTED)) {
            if (intKey) setValueInt(sett, getSelectedIntKey()); else setValueString(sett, getSelectedKey());
            DataTracker.setDataChanged();
        }
    }

    private String getValueString(Object s) {
        try {
            Method m = s.getClass().getMethod("get" + method, (Class[]) null);
            Object v = m.invoke(s, (Object[]) null);
            return (String) v;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int getValueInt(Object s) {
        try {
            Method m = s.getClass().getMethod("get" + method, (Class[]) null);
            Object v = m.invoke(s, (Object[]) null);
            return ((Integer) v).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void setValueString(Object s, String value) {
        Object aObj[] = { value };
        try {
            Method m = s.getClass().getMethod("set" + method, aSetArgsString);
            m.invoke(s, aObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setValueInt(Object s, int value) {
        Object aObj[] = { new Integer(value) };
        try {
            Method m = s.getClass().getMethod("set" + method, aSetArgsInt);
            m.invoke(s, aObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
