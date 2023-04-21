package net.sourceforge.obexftpfrontend.gui;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.SwingWorker;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import net.sourceforge.obexftpfrontend.model.ConfigurationHolder;
import net.sourceforge.obexftpfrontend.model.OBEXElement;
import net.sourceforge.obexftpfrontend.processor.OBEXResponseParser;
import net.sourceforge.obexftpfrontend.command.CommandExecutionEvent;
import net.sourceforge.obexftpfrontend.command.CommandExecutionListener;
import net.sourceforge.obexftpfrontend.command.CommandQueue;
import net.sourceforge.obexftpfrontend.command.ListFilesCommand;
import org.apache.log4j.Logger;
import static net.sourceforge.obexftpfrontend.command.CommandExecutionEvent.ExecutionStatus.*;

/**
 * This listener is responsible to manipulate the file system tree dynamically, renderizing
 * the device's file system when it's nodes are expanded.
 * @author Daniel F. Martins
 */
public class FetchFilesFromFolderListener implements TreeExpansionListener, CommandExecutionListener {

    private static final Logger log = Logger.getLogger(FetchFilesFromFolderListener.class);

    private Set<OBEXElement> fetchedFolders;

    private ConfigurationHolder configHolder;

    private FileSystemTreeHolder treeHolder;

    private OBEXResponseParser parser;

    private CommandQueue queue;

    /**
     * Create a new instance of FetchFilesFromFolderListener.
     * @param configHolder Component that manages the application configuration.
     * @param treeHolder Component that holds the file system tree component.
     * @param parser OBEXResponseParser object.
     * @param queue CommandQueue object.
     */
    public FetchFilesFromFolderListener(ConfigurationHolder configHolder, FileSystemTreeHolder treeHolder, OBEXResponseParser parser, CommandQueue queue) {
        this.configHolder = configHolder;
        this.treeHolder = treeHolder;
        this.parser = parser;
        this.queue = queue;
        fetchedFolders = new LinkedHashSet<OBEXElement>();
    }

    public void treeExpanded(final TreeExpansionEvent event) {
        OBEXElement folder = (OBEXElement) event.getPath().getLastPathComponent();
        log.debug("Node expanded");
        if (!fetchedFolders.contains(folder)) {
            try {
                queue.put(new ListFilesCommand(parser, configHolder, folder));
            } catch (Exception exc) {
                log.warn("The listener cannot be executed successfully due to some error in it's underlying commands", exc);
            }
        }
    }

    public void treeCollapsed(TreeExpansionEvent event) {
        log.debug("Node collapsed");
    }

    public void commandAdded(CommandExecutionEvent event) {
        log.debug("commandAdded() called");
    }

    public void commandRemoved(CommandExecutionEvent event) {
        log.debug("commandRemoved() called");
    }

    public void commandExecuted(final CommandExecutionEvent event) {
        log.debug("commandExecuted() called");
        if (!(event.getCommand() instanceof ListFilesCommand)) {
            return;
        }
        final ListFilesCommand command = (ListFilesCommand) event.getCommand();
        final OBEXElement selectedFolder = command.getFolder();
        if (event.getStatus() == ERROR) {
            treeHolder.collapseNode(selectedFolder);
        }
        if (event.getStatus() == SUCCESS) {
            new SwingWorker() {

                public Object doInBackground() {
                    log.debug("Refreshing the node " + selectedFolder.getName());
                    OBEXElement element = (OBEXElement) event.getResult();
                    treeHolder.clearFetchedInfo(selectedFolder);
                    log.debug("Adding the retrieved nodes to the tree");
                    for (OBEXElement child : element.getChildren()) {
                        selectedFolder.add(child);
                    }
                    fetchedFolders.add(selectedFolder);
                    treeHolder.showNode(selectedFolder);
                    return null;
                }
            }.execute();
        }
    }

    /**
     * Get the list of folders that was already fetched.
     * @return List of folders that was already fetched.
     */
    public Set<OBEXElement> getFetchedFolders() {
        return fetchedFolders;
    }
}
