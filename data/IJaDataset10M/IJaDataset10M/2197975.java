package org.adempiere.webui.editor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import org.adempiere.webui.EnvWeb;
import org.adempiere.webui.UtilWeb;
import org.adempiere.webui.ValuePreference;
import org.adempiere.webui.component.NumberBox;
import org.adempiere.webui.event.ContextMenuEvent;
import org.adempiere.webui.event.ValueChangeEvent;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.model.GridField;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

/**
 *
 * @author  <a href="mailto:agramdass@gmail.com">Ashley G Ramdass</a>
 * @date    Mar 11, 2007
 * @version $Revision: 0.10 $
 * 
 * @author Low Heng Sin
 */
public class WNumberEditor extends WEditor {

    public static final String[] LISTENER_EVENTS = { Events.ON_CHANGE };

    public static final int MAX_DISPLAY_LENGTH = 20;

    private BigDecimal oldValue;

    private boolean mandatory = false;

    private int displayType;

    public WNumberEditor() {
        this("Number", false, false, true, DisplayTypeConstants.Number, "");
    }

    /**
     * 
     * @param gridField
     */
    public WNumberEditor(GridField gridField) {
        super(new NumberBox(gridField.getDisplayType() == DisplayTypeConstants.Integer), gridField);
        this.displayType = gridField.getDisplayType();
        init();
    }

    /**
     * 
     * @param gridField
     * @param integral
     */
    public WNumberEditor(GridField gridField, boolean integral) {
        super(new NumberBox(integral), gridField);
        this.displayType = integral ? DisplayTypeConstants.Integer : DisplayTypeConstants.Number;
        init();
    }

    /**
     * 
     * @param columnName
     * @param mandatory
     * @param readonly
     * @param updateable
     * @param displayType
     * @param title
     */
    public WNumberEditor(String columnName, boolean mandatory, boolean readonly, boolean updateable, int displayType, String title) {
        super(new NumberBox(displayType == DisplayTypeConstants.Integer), columnName, title, null, mandatory, readonly, updateable);
        this.displayType = displayType;
        init();
    }

    private void init() {
        if (gridField != null) {
            getComponent().setTooltiptext(gridField.getDescription());
        }
        if (!DisplayType.isNumeric(displayType)) displayType = DisplayTypeConstants.Number;
        DecimalFormat format = DisplayType.getNumberFormat(displayType, Env.getLanguage(EnvWeb.getCtx()));
        getComponent().getDecimalbox().setFormat(format.toPattern());
    }

    /**
	 * Event handler
	 * @param event
	 */
    public void onEvent(Event event) {
        if (Events.ON_CHANGE.equalsIgnoreCase(event.getName())) {
            BigDecimal newValue = getComponent().getValue();
            ValueChangeEvent changeEvent = new ValueChangeEvent(this, this.getColumnName(), oldValue, newValue);
            super.fireValueChange(changeEvent);
            oldValue = newValue;
        }
    }

    @Override
    public NumberBox getComponent() {
        return (NumberBox) component;
    }

    @Override
    public boolean isReadWrite() {
        return getComponent().isEnabled();
    }

    @Override
    public void setReadWrite(boolean readWrite) {
        getComponent().setEnabled(readWrite);
    }

    @Override
    public String getDisplay() {
        return getComponent().getText();
    }

    @Override
    public Object getValue() {
        return getComponent().getValue();
    }

    @Override
    public boolean isMandatory() {
        return mandatory;
    }

    @Override
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    @Override
    public void setValue(Object value) {
        if (value == null) oldValue = null; else if (value instanceof BigDecimal) oldValue = (BigDecimal) value; else if (value instanceof Number) oldValue = new BigDecimal(((Number) value).doubleValue()); else oldValue = new BigDecimal(value.toString());
        getComponent().setValue(oldValue);
    }

    @Override
    public String[] getEvents() {
        return LISTENER_EVENTS;
    }

    /**
     * Handle context menu events 
     * @param evt
     */
    public void onMenu(ContextMenuEvent evt) {
        if (WEditorPopupMenu.PREFERENCE_EVENT.equals(evt.getContextEvent()) && gridField != null) {
            if (UtilWeb.getDefaultRole().isShowPreference()) ValuePreference.start(this.getGridField(), getValue());
            return;
        }
    }
}
