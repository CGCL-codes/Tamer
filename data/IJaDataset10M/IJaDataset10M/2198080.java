package org.armedbear.j.mail;

import org.armedbear.j.Frame;
import org.armedbear.j.ToolBar;
import static org.armedbear.j.ToolBarIcon.*;

public final class MailboxModeToolBar extends ToolBar {

    public MailboxModeToolBar(Frame frame) {
        super(frame);
        addButton("New", ICON_NEW, "newBuffer");
        addButton("Open", ICON_OPEN, "openFile");
        addButton("Save", ICON_SAVE, "saveFile", false);
        addButton("Close", ICON_CLOSE, "killBuffer");
        addSeparator();
        addButton("Get New Mail", ICON_MAIL_RECEIVE, "mailboxGetNewMessages");
        addSeparator();
        addButton("Delete", ICON_DELETE, "mailboxDelete");
        addButton("Expunge", ICON_REFRESH, "mailboxExpunge");
        addSeparator();
        addButton("Compose", ICON_MAIL_COMPOSE, "compose");
        addSeparator();
        addButton("Home", ICON_HOME, "dirHomeDir");
        maybeAddInboxButton();
        addSeparator();
        addButton("Stop", ICON_STOP, "mailboxStop");
        addSeparator();
        addButton("Exit", ICON_EXIT, "quit");
    }
}
