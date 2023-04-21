package ch.iserver.ace.application.action;

import ch.iserver.ace.application.DocumentManager;
import ch.iserver.ace.application.ItemSelectionChangeEvent;
import ch.iserver.ace.application.LocaleMessageSource;
import ch.iserver.ace.application.DocumentViewController;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import javax.swing.KeyStroke;

public class EditCopyAction extends DocumentItemSelectionChangeAction {

    private DocumentManager documentManager;

    public EditCopyAction(LocaleMessageSource messageSource, DocumentManager documentManager, DocumentViewController viewController) {
        super(messageSource.getMessage("mEditCopy"), messageSource.getIcon("iMenuEditCopy"), viewController);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        this.documentManager = documentManager;
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("EditCopyAction");
    }

    public void itemSelectionChanged(ItemSelectionChangeEvent e) {
    }
}
