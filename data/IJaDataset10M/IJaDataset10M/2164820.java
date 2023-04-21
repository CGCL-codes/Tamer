package ggc.cgm.gui.config;

import ggc.cgm.manager.CGMManager;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JDialog;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.graphics.dialogs.selector.SelectorAbstractDialog;
import com.atech.utils.ATDataAccessAbstract;

/**
 * Selector component for Meters (Simple Configuration)...
 * @author arozman
 *
 */
public class CGMDeviceSelectorDialog extends SelectorAbstractDialog {

    /**
     * 
     */
    private static final long serialVersionUID = -7451287714720285113L;

    public CGMDeviceSelectorDialog(JDialog parent, ATDataAccessAbstract da) {
        super(parent, da.getI18nControlInstance(), 0, null, true);
        this.showDialog();
    }

    public void initSelectorValuesForType() {
        setSelectorName(ic.getMessage("SELECTOR_METER"));
        setAllowedActions(SelectorAbstractDialog.SELECTOR_ACTION_CANCEL_AND_SELECT);
        this.setColumnSortingEnabled(false);
        this.setHelpEnabled(false);
    }

    public void getFullData() {
        this.full = new ArrayList<SelectableInterface>();
        this.full.addAll((Collection<? extends SelectableInterface>) CGMManager.getInstance().getSupportedDevices());
    }

    public void setHelpContext() {
    }

    @Override
    public void checkAndExecuteActionEdit() {
    }

    @Override
    public void checkAndExecuteActionNew() {
    }

    @Override
    public void checkAndExecuteActionSelect() {
        if (table != null) {
            if (table.getSelectedRow() == -1) return;
            this.selected_object = list.get(table.getSelectedRow());
            this.dispose();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
    }
}
