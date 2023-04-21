package net.sf.javadc.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.Timer;
import net.sf.javadc.config.ConstantSettings;
import net.sf.javadc.gui.model.RowTableModel;
import net.sf.javadc.gui.model.SortableTable;
import net.sf.javadc.gui.model.SortableTableListener;
import net.sf.javadc.gui.util.ByteCellRenderer;
import net.sf.javadc.gui.util.UserCellRenderer;
import net.sf.javadc.interfaces.IDownloadManager;
import net.sf.javadc.interfaces.IHub;
import net.sf.javadc.interfaces.ISettings;
import net.sf.javadc.listeners.HubListener;
import net.sf.javadc.listeners.HubListenerBase;
import net.sf.javadc.net.DownloadRequest;
import net.sf.javadc.net.SearchResult;
import net.sf.javadc.net.hub.HubUser;
import net.sf.javadc.util.FileUtils;
import spin.Spin;

/**
 * <CODE>UserListComponent</CODE> provides a view on the users of a specific <CODE>Hub</CODE> or all connected
 * <CODE>Hubs</CODE>
 * 
 * @author Jesper Nordenberg
 * @version $Revision: 1.22 $ $Date: 2005/10/02 11:42:28 $
 */
public class UserListComponent extends BasePanel implements SortableTableListener {

    private final class MyHubListener extends HubListenerBase {

        private final RowTableModel model;

        private ArrayList<HubUser> added = new ArrayList<HubUser>();

        private ArrayList<HubUser> removed = new ArrayList<HubUser>();

        private boolean updated = false;

        private MyHubListener(RowTableModel model) {
            super();
            this.model = model;
        }

        public void addUpdateTimer() {
            updated = true;
            ActionListener taskPerformer = new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    update();
                    updated = false;
                }
            };
            Timer timer = new Timer(ConstantSettings.MANAGERCOMPONENT_UPDATEINTERVAL, taskPerformer);
            timer.setRepeats(false);
            timer.start();
        }

        @Override
        public void userAdded(IHub hub, HubUser ui) {
            added.add(ui);
            if (!updated) {
                addUpdateTimer();
            }
        }

        @Override
        public void userRemoved(IHub hub, HubUser ui) {
            removed.add(ui);
            if (!updated) {
                addUpdateTimer();
            }
        }

        private void update() {
            HubUser[] ad = added.toArray(new HubUser[added.size()]);
            added.clear();
            HubUser[] re = removed.toArray(new HubUser[removed.size()]);
            removed.clear();
            for (HubUser element : ad) {
                model.addRow(element, getUserColumns(element));
            }
            for (HubUser element : re) {
                model.deleteRow(element);
            }
        }
    }

    /**
     * 
     */
    private static final long serialVersionUID = 2650931664823334860L;

    private final IHub hub;

    private HubComponent hubComponent;

    private final RowTableModel model = new RowTableModel(new String[] { "Nick", "Size", "Speed", "Description" });

    private final SortableTable list = new SortableTable(new int[] { 90, 70, 70, -1 }, this, model, "users");

    private final ISettings settings;

    private final IDownloadManager downloadManager;

    /**
     * Create a new UserListComponent instance
     * 
     * @param _hub
     * @param _settings
     * @param _downloadManager
     */
    public UserListComponent(IHub _hub, HubComponent _hubComponent, ISettings _settings, IDownloadManager _downloadManager) {
        super(new BorderLayout());
        if (_hub == null) {
            throw new NullPointerException("hub was null");
        }
        if (_settings == null) {
            throw new NullPointerException("settings was null");
        }
        if (_downloadManager == null) {
            throw new NullPointerException("downloadManager was null");
        }
        hub = _hub;
        hubComponent = _hubComponent;
        settings = _settings;
        downloadManager = _downloadManager;
        initialize();
    }

    public final void cellSelected(int row, int column, int[] selectedColumn) {
        HubUser user = (HubUser) model.getRow(row);
        String filename = "MyList.DcLst";
        SearchResult sr = new SearchResult(hub, user.getNick(), filename, settings, 1);
        String localname = settings.getDownloadDir() + "MyList." + user.getNick() + ".DcLst";
        DownloadRequest dr = new DownloadRequest(sr, localname, settings);
        downloadManager.requestDownload(dr);
    }

    public final void showPopupClicked(final int row, final int column, MouseEvent e, int[] selectedRows) {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem sendMessage = new JMenuItem("Send Message");
        sendMessage.setIcon(FileUtils.loadIcon("images/16/mail_generic.png"));
        JMenuItem browse = new JMenuItem("Browse Files");
        browse.setIcon(FileUtils.loadIcon("images/16/list.png"));
        sendMessage.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e1) {
                HubUser user = (HubUser) model.getRow(row);
                hubComponent.addUserMessageTab(user.getNick());
            }
        });
        browse.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e1) {
                int[] els = {};
                cellSelected(row, column, els);
            }
        });
        popup.add(sendMessage);
        popup.add(browse);
        popup.show(e.getComponent(), e.getX(), e.getY());
    }

    /**
     * Get the column contents for the given HubUser
     * 
     * @param user HubUser instance for which the column contents are to be retrieved
     * @return
     */
    private final Object[] getUserColumns(HubUser user) {
        return new Object[] { user.getNick(), new Long(user.getSharedSize()), user.getSpeed(), user.getDescription() };
    }

    /**
     * 
     */
    private void initialize() {
        hub.addListener((HubListener) Spin.over(new MyHubListener(model)));
        list.getTable().setDefaultRenderer(Long.class, new ByteCellRenderer());
        ImageIcon[] buffer = getUserIcons();
        List opList = null;
        try {
            opList = hub.getOpList();
        } catch (Exception e) {
        }
        list.getTable().getColumn("Nick").setCellRenderer(new UserCellRenderer(buffer, opList));
        list.getTable().setRowHeight(settings.getAdvancedSettings().getIconSize());
        add(list, BorderLayout.CENTER);
        setMinimumSize(new Dimension(200, 0));
    }
}
