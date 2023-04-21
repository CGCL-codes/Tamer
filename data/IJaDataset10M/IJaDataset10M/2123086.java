package ch.iserver.ace.application.action;

import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import ch.iserver.ace.application.DocumentItem;
import ch.iserver.ace.application.DocumentManager;
import ch.iserver.ace.application.DocumentViewController;
import ch.iserver.ace.application.ItemSelectionChangeEvent;
import ch.iserver.ace.application.LocaleMessageSource;

public class TogglePublishConcealDocumentAction extends DocumentItemSelectionChangeAction {

    private DocumentManager documentManager;

    private ImageIcon iconPublish, iconConceal;

    private String toolTipPublish, toolTipConceal;

    private boolean documentPublished = false;

    public TogglePublishConcealDocumentAction(LocaleMessageSource messageSource, DocumentManager documentManager, DocumentViewController viewController) {
        super(messageSource.getMessage("mNetPublish"), messageSource.getIcon("iMenuNetPublish"), viewController);
        putValue(SHORT_DESCRIPTION, messageSource.getMessage("mNetPublishTT"));
        this.documentManager = documentManager;
        toolTipPublish = messageSource.getMessage("mNetPublishTT");
        iconPublish = messageSource.getIcon("iMenuNetPublish");
        toolTipConceal = messageSource.getMessage("mNetConcealTT");
        iconConceal = messageSource.getIcon("iMenuNetConceal");
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        if (documentPublished) {
            documentManager.concealDocument();
        } else {
            documentManager.publishDocument();
        }
    }

    public void itemSelectionChanged(ItemSelectionChangeEvent e) {
        if (e.getItem() == null) {
            setEnabled(false);
        } else {
            DocumentItem item = (DocumentItem) e.getItem();
            if (item.getType() == DocumentItem.LOCAL) {
                documentPublished = false;
                putValue(SMALL_ICON, iconPublish);
                putValue(SHORT_DESCRIPTION, toolTipPublish);
                setEnabled(true);
            } else if (item.getType() == DocumentItem.PUBLISHED) {
                documentPublished = true;
                putValue(SMALL_ICON, iconConceal);
                putValue(SHORT_DESCRIPTION, toolTipConceal);
                setEnabled(true);
            } else {
                setEnabled(false);
            }
        }
    }
}
