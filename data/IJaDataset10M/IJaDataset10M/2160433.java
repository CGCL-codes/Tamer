package de.psychomatic.mp3db.gui.menu.actions;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import de.psychomatic.messagesystem.MessageEvent;
import de.psychomatic.messagesystem.MessageEventTypeEnum;
import de.psychomatic.messagesystem.MessageSenderIf;
import de.psychomatic.mp3db.core.dblayer.utils.DB;
import de.psychomatic.mp3db.gui.Main;
import de.psychomatic.mp3db.gui.utils.AppDB;
import de.psychomatic.mp3db.gui.utils.GuiStrings;

/**
 * Action for clearing internal database
 * @author Kykal
 */
public class InternalDBClearAction extends MenuAction {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(InternalDBClearAction.class);

    /**
     * Creates the action
     * @param parent Parentframe
     * @param messenger Messenger
     */
    public InternalDBClearAction(final Main parent, final MessageSenderIf messenger) {
        super(parent, messenger);
        putValue(Action.NAME, GuiStrings.getInstance().getString("menu.options.internal.clear"));
    }

    /**
     * Clears the internal database
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (DB.getInstance().isInit()) {
            final int button = JOptionPane.showConfirmDialog(_parent, GuiStrings.getInstance().getString("label.cleardb"), GuiStrings.getInstance().getString("title.cleardb"), JOptionPane.YES_NO_OPTION);
            if (button == JOptionPane.YES_OPTION) {
                _messenger.fireMessageEvent(new MessageEvent(this, "StopThreads", MessageEventTypeEnum.EMPTY));
                _messenger.fireMessageEvent(new MessageEvent(this, "TableData", MessageEventTypeEnum.REMOVE));
                _messenger.fireMessageEvent(new MessageEvent(this, "AlbumTableData", MessageEventTypeEnum.REMOVE));
                _messenger.fireMessageEvent(new MessageEvent(this, "CdTreeData", MessageEventTypeEnum.REMOVE));
                try {
                    final AppDB db = (AppDB) DB.getInstance();
                    db.clearDatabase();
                    db.shutdown();
                    db.init(_parent.getConfig().getDbConfiguration(), _parent.getConfig().getConfigDir());
                } catch (final Exception e1) {
                    LOG.error("actionPerformed(ActionEvent)", e1);
                }
                _messenger.fireMessageEvent(new MessageEvent(this, "TableData", MessageEventTypeEnum.CHANGED));
                _messenger.fireMessageEvent(new MessageEvent(this, "AlbumTableData", MessageEventTypeEnum.CHANGED));
                _messenger.fireMessageEvent(new MessageEvent(this, "CdTreeData", MessageEventTypeEnum.CHANGED));
            }
        }
    }

    /**
     * Status if action is enabled
     * @return true, if program uses internal database
     */
    @Override
    public boolean isEnabled() {
        return _parent.getConfig().isInternalDB();
    }
}
