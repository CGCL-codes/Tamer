package de.xirp.ui.widgets.dialogs.preferences.renderer;

import java.util.List;
import java.util.Observable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import de.xirp.settings.IValue;
import de.xirp.settings.Option;
import de.xirp.ui.util.SWTUtil;
import de.xirp.ui.widgets.custom.XCombo;
import de.xirp.ui.widgets.custom.XLabel;

/**
 * Renders options in combo box style.<br>
 * Synchronization over observers with the current data is supported.<br>
 * 
 * @author Rabea Gransberger
 */
public class ComboRenderer extends AbstractOptionRenderer {

    /**
	 * The combo box
	 */
    private XCombo cc;

    /**
	 * For each value a new item is added to the combo box.<br>
	 * Internationalization is used if the value provides a key.
	 * (non-Javadoc)
	 * 
	 * @see de.xirp.ui.widgets.dialogs.preferences.renderer.IOptionRenderer#render(Composite,
	 *      Option)
	 */
    public void render(Composite parent, Option option) {
        XLabel l = new XLabel(parent, SWT.NONE, option.getI18n());
        l.setTextForLocaleKey(option.getNameKey(), option.getNameKeyArgs());
        SWTUtil.setGridData(l, true, false, SWT.LEFT, SWT.CENTER, 1, 1);
        cc = new XCombo(parent, SWT.BORDER | SWT.READ_ONLY, option.getI18n());
        SWTUtil.setGridData(cc, true, false, SWT.FILL, SWT.CENTER, 1, 1);
        IValue selected = null;
        for (IValue value : option.getValues()) {
            if (value.getKey() != null) {
                cc.addForLocaleKey(value.getKey(), value.getKeyArgs());
            } else {
                cc.add(value.getDisplayValue());
            }
            if (value.isCurrentlySelected() || selected == null) {
                selected = value;
            }
            value.addObserverToValue(this);
        }
        cc.setData(option.getValues());
        if (selected != null) {
            cc.select(cc.indexOf(selected.getDisplayValue()));
            cc.addModifyListener(new ModifyListener() {

                @SuppressWarnings("unchecked")
                public void modifyText(@SuppressWarnings("unused") ModifyEvent e) {
                    String t = cc.getText();
                    List<IValue> values = (List<IValue>) cc.getData();
                    for (IValue value : values) {
                        if (t.equalsIgnoreCase(value.getDisplayValue())) {
                            value.setSelected(true, true);
                        } else {
                            value.setSelected(false, true);
                        }
                    }
                }
            });
        }
    }

    /**
	 * If the observable is a {@link IValue value} and this value is
	 * selected the combo box is updated so that the value is selected
	 * in the combo box.
	 * 
	 * @param observable
	 *            the observable
	 * @param obj
	 *            (unused)
	 * @see java.util.Observer#update(java.util.Observable,
	 *      java.lang.Object)
	 */
    public void update(Observable observable, @SuppressWarnings("unused") Object obj) {
        if (observable instanceof IValue) {
            IValue val = (IValue) observable;
            String key = val.getDisplayValue();
            boolean isSelected = val.isCurrentlySelected();
            if (isSelected) {
                cc.select(cc.indexOf(key));
            }
        }
    }
}
