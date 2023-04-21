package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;

public class OpenVCEditDialogNewAction extends AbstractAction {

    private static final long serialVersionUID = -7467328126525836858L;

    private static OpenVCEditDialogNewAction instance = null;

    public static OpenVCEditDialogNewAction getInstance(final String name) {
        if (instance == null) {
            instance = new OpenVCEditDialogNewAction(name);
        }
        return instance;
    }

    public static OpenVCEditDialogNewAction getInstance() {
        return instance;
    }

    /**
     * @param name
     */
    private OpenVCEditDialogNewAction(final String name) {
        super.putValue(Action.NAME, name);
        super.putValue(Action.SHORT_DESCRIPTION, name);
    }

    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
        WaitingDialog.openInstance();
        try {
            ViewConfigurationBeanManager.getInstance().newConfiguration();
        } catch (final Throwable t) {
            t.printStackTrace();
        } finally {
            WaitingDialog.closeInstance();
        }
    }
}
