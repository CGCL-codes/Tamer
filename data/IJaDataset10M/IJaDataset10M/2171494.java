package es.caib.zkib.component;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.ext.client.Checkable;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.impl.LabelImageElement;
import es.caib.zkib.binder.SingletonBinder;
import es.caib.zkib.events.XPathEvent;
import es.caib.zkib.events.XPathSubscriber;

public class DataCheckbox extends Checkbox implements XPathSubscriber {

    /**
	 * 
	 */
    private static final long serialVersionUID = -26350721236864797L;

    SingletonBinder binder = new SingletonBinder(this);

    private boolean duringOnUpdate = false;

    public void setPage(Page page) {
        super.setPage(page);
        binder.setPage(page);
    }

    public void setParent(Component parent) {
        super.setParent(parent);
        binder.setParent(parent);
    }

    public DataCheckbox() {
        super();
    }

    public void setBind(String bind) {
        binder.setDataPath(bind);
        if (bind != null) {
            refreshValue();
        }
    }

    public String getBind() {
        return binder.getDataPath();
    }

    private void refreshValue() {
        Object newVal = binder.getValue();
        boolean selected;
        if (newVal != null) {
            if (newVal instanceof Boolean) selected = ((Boolean) newVal).booleanValue(); else if (newVal instanceof String) selected = !newVal.equals("false"); else if (newVal instanceof Integer) selected = ((Integer) newVal).intValue() != 0; else if (newVal instanceof Long) selected = ((Long) newVal).intValue() != 0; else selected = true;
        } else selected = false;
        try {
            duringOnUpdate = true;
            setChecked(selected);
        } finally {
            duringOnUpdate = false;
        }
        if (!binder.isVoid() && !binder.isValid()) super.setDisabled(true); else super.setDisabled(effectiveDisabled);
    }

    public void onUpdate(XPathEvent event) {
        refreshValue();
    }

    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if (!duringOnUpdate) {
            binder.setValue(new Boolean(checked));
        }
    }

    private boolean effectiveDisabled = false;

    public void setDisabled(boolean disabled) {
        effectiveDisabled = disabled;
        super.setDisabled(disabled);
    }

    public Object clone() {
        DataCheckbox clone = (DataCheckbox) super.clone();
        clone.binder = new SingletonBinder(clone);
        clone.binder.setDataPath(binder.getDataPath());
        return clone;
    }

    protected Object newExtraCtrl() {
        return new ExtraCtrl();
    }

    /** A utility class to implement {@link #getExtraCtrl}.
	 * It is used only by component developers.
	 */
    protected class ExtraCtrl extends Checkbox.ExtraCtrl {

        public void setCheckedByClient(boolean checked) {
            super.setCheckedByClient(checked);
            if (!duringOnUpdate) {
                binder.setValue(new Boolean(checked));
            }
        }
    }
}
