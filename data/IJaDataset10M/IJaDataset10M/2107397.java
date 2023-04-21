package net.sf.amemailchecker.gui.messageviewer.extension.point;

import net.sf.amemailchecker.app.extension.MailActionContext;
import net.sf.amemailchecker.app.extension.MailActionContextChangeListener;
import net.sf.amemailchecker.app.notification.NotificationCache;
import net.sf.amemailchecker.mail.model.Letter;
import javax.swing.*;
import java.util.List;

class MailContextChangeListener implements MailActionContextChangeListener {

    @Override
    public void contextChanged(MailActionContext context, AbstractButton[] components) {
        switch(context.getFolder().getType()) {
            case REMOTE:
                changeRemoteViewState(context, components);
                break;
            case LOCAL:
                changeLocalViewState(context, components);
                break;
        }
    }

    protected void changeLocalViewState(MailActionContext context, AbstractButton[] components) {
        components[0].setEnabled(false);
    }

    protected void changeRemoteViewState(MailActionContext context, AbstractButton[] components) {
        List<Letter> letterList = NotificationCache.getInstance().get(context.getAccount(), context.getFolder());
        boolean letters = letterList != null && letterList.size() > 0;
        for (AbstractButton button : components) {
            button.setEnabled(letters);
        }
    }
}
