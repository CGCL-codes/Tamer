package org.adempiere.webui.editor;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.PAttributebox;
import org.adempiere.webui.event.ContextMenuEvent;
import org.adempiere.webui.event.ContextMenuListener;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.window.WPAttributeDialog;
import org.compiere.framework.Lookup;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAttributeSet;
import org.compiere.model.MProduct;
import org.compiere.util.CLogger;
import org.adempiere.webui.EnvWeb;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

/**
 * 
 * @author Low Heng Sin
 *
 */
public class WPAttributeEditor extends WEditor implements ContextMenuListener {

    private static final String[] LISTENER_EVENTS = { Events.ON_CLICK, Events.ON_CHANGE };

    private static final CLogger log = CLogger.getCLogger(WPAttributeEditor.class);

    private int m_WindowNo;

    private Lookup m_mPAttribute;

    private int m_C_BPartner_ID;

    private WEditorPopupMenu popupMenu;

    private Object m_value;

    private GridTab m_GridTab;

    /**	No Instance Key					*/
    private static Integer NO_INSTANCE = new Integer(0);

    public WPAttributeEditor(GridTab gridTab, GridField gridField) {
        super(new PAttributebox(), gridField);
        m_GridTab = gridTab;
        initComponents();
    }

    private void initComponents() {
        getComponent().setButtonImage("images/PAttribute10.png");
        getComponent().addEventListener(Events.ON_CLICK, this);
        m_WindowNo = gridField.getWindowNo();
        m_mPAttribute = gridField.getLookup();
        m_C_BPartner_ID = EnvWeb.getCtx().getContextAsInt(m_WindowNo, "C_BPartner_ID");
        popupMenu = new WEditorPopupMenu(true, false, false);
        getComponent().getTextbox().setContext(popupMenu.getId());
    }

    @Override
    public WEditorPopupMenu getPopupMenu() {
        return popupMenu;
    }

    @Override
    public PAttributebox getComponent() {
        return (PAttributebox) component;
    }

    @Override
    public void setValue(Object value) {
        if (value == null || NO_INSTANCE.equals(value)) {
            getComponent().setText("");
            m_value = value;
            return;
        }
        if (value.equals(m_value)) return;
        log.fine("Value=" + value);
        m_value = value;
        getComponent().setText(m_mPAttribute.getDisplay(value));
    }

    @Override
    public Object getValue() {
        return m_value;
    }

    @Override
    public String getDisplay() {
        return getComponent().getText();
    }

    public void onEvent(Event event) {
        if (Events.ON_CHANGE.equals(event.getName())) {
            ValueChangeEvent changeEvent = new ValueChangeEvent(this, this.getColumnName(), getComponent().getText(), getComponent().getText());
            fireValueChange(changeEvent);
        } else if (Events.ON_CLICK.equals(event.getName())) {
            cmd_dialog();
        }
    }

    /**
	 *  Start dialog
	 */
    private void cmd_dialog() {
        Integer oldValue = (Integer) getValue();
        int oldValueInt = oldValue == null ? 0 : oldValue.intValue();
        int M_AttributeSetInstance_ID = oldValueInt;
        int M_Product_ID = EnvWeb.getCtx().getContextAsInt(m_WindowNo, "M_Product_ID");
        int M_ProductBOM_ID = EnvWeb.getCtx().getContextAsInt(m_WindowNo, "M_ProductBOM_ID");
        log.config("M_Product_ID=" + M_Product_ID + "/" + M_ProductBOM_ID + ",M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID + ", AD_Column_ID=" + gridField.getAD_Column_ID());
        boolean productWindow = (gridField.getAD_Column_ID() == 8418);
        boolean exclude = true;
        if (M_Product_ID != 0) {
            MProduct product = MProduct.get(EnvWeb.getCtx(), M_Product_ID);
            int M_AttributeSet_ID = product.getM_AttributeSet_ID();
            if (M_AttributeSet_ID != 0) {
                MAttributeSet mas = MAttributeSet.get(EnvWeb.getCtx(), M_AttributeSet_ID);
                exclude = mas.excludeEntry(gridField.getAD_Column_ID(), EnvWeb.getCtx().isSOTrx(m_WindowNo));
            }
        }
        boolean changed = false;
        if (M_ProductBOM_ID != 0) M_Product_ID = M_ProductBOM_ID;
        if (!productWindow && (M_Product_ID == 0 || exclude)) {
            changed = true;
            getComponent().setText(null);
            M_AttributeSetInstance_ID = 0;
        } else {
            WPAttributeDialog vad = new WPAttributeDialog(M_AttributeSetInstance_ID, M_Product_ID, m_C_BPartner_ID, productWindow, gridField.getAD_Column_ID(), m_WindowNo);
            if (vad.isChanged()) {
                getComponent().setText(vad.getM_AttributeSetInstanceName());
                M_AttributeSetInstance_ID = vad.getM_AttributeSetInstance_ID();
                if (m_GridTab != null && !productWindow && vad.getM_Locator_ID() > 0) m_GridTab.setValue("M_Locator_ID", vad.getM_Locator_ID());
                changed = true;
            }
        }
        if (changed) {
            log.finest("Changed M_AttributeSetInstance_ID=" + M_AttributeSetInstance_ID);
            m_value = new Object();
            if (M_AttributeSetInstance_ID == 0) setValue(null); else setValue(new Integer(M_AttributeSetInstance_ID));
            ValueChangeEvent vce = new ValueChangeEvent(this, gridField.getColumnName(), new Object(), getValue());
            fireValueChange(vce);
            if (M_AttributeSetInstance_ID == oldValueInt && m_GridTab != null && gridField != null) {
                m_GridTab.processFieldChange(gridField);
            }
        }
    }

    public String[] getEvents() {
        return LISTENER_EVENTS;
    }

    public void onMenu(ContextMenuEvent evt) {
        if (WEditorPopupMenu.ZOOM_EVENT.equals(evt.getContextEvent())) {
            actionZoom();
        }
    }

    public void actionZoom() {
        AEnv.actionZoom(m_mPAttribute, getValue());
    }

    @Override
    public boolean isReadWrite() {
        return !getComponent().getTextbox().isReadonly();
    }

    @Override
    public void setReadWrite(boolean readWrite) {
        getComponent().setEnabled(readWrite);
    }
}
