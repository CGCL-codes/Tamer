package es.caib.zkib.component;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ext.client.Checkable;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Radio;
import es.caib.zkib.component.DataCheckbox.ExtraCtrl;

public class DataRadio extends Radio {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7855528364349317912L;

    public DataRadio() {
        super();
    }

    public DataRadio(String label) {
        super(label);
    }

    public DataRadio(String label, String image) {
        super(label, image);
    }

    public void setChecked(boolean checked) {
        super.setChecked(checked);
        notifyParent();
    }

    private void notifyParent() {
        final Component group = getParent();
        if (group != null && group instanceof DataRadiogroup) {
            ((DataRadiogroup) group).updateBinder();
        }
    }

    protected Object newExtraCtrl() {
        return new ExtraCtrl();
    }

    /** A utility class to implement {@link #getExtraCtrl}.
	 * It is used only by component developers.
	 */
    protected class ExtraCtrl extends Radio.ExtraCtrl {

        public void setCheckedByClient(boolean checked) {
            super.setCheckedByClient(checked);
            notifyParent();
        }
    }
}
