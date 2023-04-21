package com.sebulli.fakturama.actions;

import static com.sebulli.fakturama.Translate._;
import org.eclipse.jface.action.Action;
import com.sebulli.fakturama.backup.BackupManager;

/**
 * This action opens the calculator in a view.
 * 
 * @author Gerd Bartelt
 */
public class UpdateAction extends Action {

    public static final String ACTIONTEXT = _("Check for Updates");

    /**
	 * Constructor
	 */
    public UpdateAction() {
        super(ACTIONTEXT);
        setToolTipText(_("Check for Updates"));
        setId(ICommandIds.CMD_P2_UPDATE);
        setActionDefinitionId(ICommandIds.CMD_P2_UPDATE);
    }

    /**
	 * Run the action
	 * 
	 * Check for new updates
	 */
    @Override
    public void run() {
        BackupManager.createBackup();
    }
}
