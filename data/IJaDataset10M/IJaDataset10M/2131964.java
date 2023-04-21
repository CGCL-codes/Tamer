package edu.psu.its.lionshare.gui.library;

import edu.psu.its.lionshare.share.DataObjectGroup;
import edu.psu.its.lionshare.share.ShareManager;
import edu.psu.its.lionshare.share.DataObject;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * This the mediator for the entire library panel in the gui.  It will manage
 * the VirtualDirectoryMediator as well as the LibraryTableMediator.
 *
 * @author Lorin Metzger   
 * LionShareP2P
 *
 */
public class LibraryMediator {

    private static final Log LOG = LogFactory.getLog(LibraryMediator.class);

    /**
   * A single instance of this class.
   */
    private static LibraryMediator instance = null;

    /**
   * The main display component panel.
   */
    private JComponent MAIN_COMPONENT = null;

    private LibrarySharingMediator sharing_mediator = null;

    private LibraryViewMediator view_mediator = null;

    private DataObjectGroup selected_group = null;

    /**
   * A private constructor to ensure no instances of this class are created
   * externally.
   */
    private LibraryMediator() {
        sharing_mediator = new LibrarySharingMediator();
        sharing_mediator.addSelectionListener(new SelectionListener());
        view_mediator = new LibraryViewMediator();
    }

    /**
   *
   * Returns the single static instance of this class.
   * 
   * @return LibraryMediator - a single instance.
   *
   */
    public static LibraryMediator instance() {
        if (instance == null) {
            instance = new LibraryMediator();
        }
        return instance;
    }

    public DataObjectGroup getSelectedGroup() {
        return selected_group;
    }

    public LibrarySharingMediator getShareMediator() {
        return sharing_mediator;
    }

    public LibraryViewMediator getViewMediator() {
        return view_mediator;
    }

    /**
   *
   * Returns the main display component for the library.
   *
   * @return JComponent - the library gui component.
   *
   */
    public JComponent getComponent() {
        if (MAIN_COMPONENT == null) {
            MAIN_COMPONENT = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sharing_mediator.getComponent(), view_mediator.getComponent());
            ((JSplitPane) MAIN_COMPONENT).setDividerLocation(150);
        }
        return MAIN_COMPONENT;
    }

    public void refreshIfIncomplete() {
        LOG.debug("Refresh incomplete");
    }

    public void setAnnotateEnabled(boolean enable) {
        LOG.debug("Annotate enable");
    }

    public void clearLibrary() {
        LOG.debug("clear library");
    }

    public void updateSharedFile(java.io.File file) {
        LOG.debug("Update file");
    }

    public void addSharedFile(com.limegroup.gnutella.FileDesc file, java.io.File parent) {
        LOG.debug("add shared file");
    }

    public void addSharedDirectory(java.io.File file, java.io.File parent) {
        LOG.debug("add shared directory");
    }

    public void refresh() {
        LOG.debug("Refreshing");
    }

    public void launch() {
        LOG.debug("Launching");
    }

    public void deleteLibraryFile() {
        LOG.debug("Delete library file");
    }

    public void addSharedLibraryFolder() {
        LOG.debug("Adding shared library folder");
    }

    public void addNewLibraryFolder() {
        LOG.debug("Add new library folder");
    }

    public void unshareLibraryFolder() {
        LOG.debug("unshare library folder");
    }

    public void renameLibraryFolder() {
        LOG.debug("rename library folder");
    }

    public void cancelEditing() {
        LOG.debug("cancel editing");
    }

    private class SelectionListener implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent event) {
            TreePath newtp = event.getNewLeadSelectionPath();
            TreePath oldp = event.getOldLeadSelectionPath();
            if (newtp != null) {
                if (newtp.getLastPathComponent() instanceof DataObjectGroup) {
                    selected_group = (DataObjectGroup) newtp.getLastPathComponent();
                } else selected_group = null;
            } else {
                selected_group = null;
            }
            view_mediator.getSelectedView().setDataObjectGroup(selected_group);
            if (selected_group != null) view_mediator.setEnabledImageView(selected_group.isLocal());
            view_mediator.setStatusMessage(selected_group);
        }
    }
}
