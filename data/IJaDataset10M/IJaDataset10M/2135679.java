package org.columba.mail.gui.composer.action;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import org.columba.core.gui.action.AbstractSelectableAction;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.SecurityItem;
import org.columba.mail.gui.composer.ComposerController;
import org.columba.mail.gui.composer.ComposerModel;
import org.columba.mail.resourceloader.MailImageLoader;
import org.columba.mail.util.MailResourceLoader;

/**
 * @author frd
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class EncryptMessageAction extends AbstractSelectableAction {

    /** JDK 1.4+ logging framework logger, used for logging. */
    private static final Logger LOG = Logger.getLogger("org.columba.mail.gui.composer.action");

    public EncryptMessageAction(ComposerController composerController) {
        super(composerController, MailResourceLoader.getString("menu", "composer", "menu_message_encrypt"));
        putValue(SHORT_DESCRIPTION, MailResourceLoader.getString("menu", "composer", "menu_message_encrypt").replaceAll("&", ""));
        putValue(SMALL_ICON, MailImageLoader.getSmallIcon("lock.png"));
        ComposerModel model = composerController.getModel();
        AccountItem account = model.getAccountItem();
        SecurityItem pgp = account.getPGPItem();
        setState(pgp.getBooleanWithDefault("always_encrypt", false));
    }

    public void actionPerformed(ActionEvent evt) {
        LOG.info("start encryption...");
        ComposerModel model = (ComposerModel) ((ComposerController) getFrameMediator()).getModel();
        model.setEncryptMessage(getState());
    }
}
