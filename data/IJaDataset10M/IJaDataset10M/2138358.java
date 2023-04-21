package org.skunk.dav.client.gui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URLDecoder;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.skunk.dav.client.DAVConstants;
import org.skunk.dav.client.DAVFile;
import org.skunk.dav.client.gui.DAVFileChooser;
import org.skunk.dav.client.gui.DAVTreeNode;
import org.skunk.dav.client.gui.Explorer;
import org.skunk.dav.client.gui.ResourceManager;
import org.skunk.dav.client.gui.ServerData;

public class CopyAction extends AbstractAction {

    final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    private Explorer ex;

    private DAVTreeNode node;

    private DAVFile file;

    private String destinationURL;

    public CopyAction(Explorer ex, DAVTreeNode node, DAVFile file, String destinationURL) {
        this.ex = ex;
        this.node = node;
        this.file = file;
        this.destinationURL = destinationURL;
    }

    public CopyAction(Explorer ex, DAVFile file) {
        this(ex, null, file, null);
    }

    public CopyAction(Explorer ex) {
        this(ex, null, null, null);
    }

    public void actionPerformed(ActionEvent ae) {
        DAVTreeNode localNode;
        DAVFile localFile = (file != null) ? file : ex.getSelectedTableFile();
        if (localFile == null) {
            DAVFileChooser.ChoiceStruct choice = ex.chooseFile(null, getDialogTitle(), false, null);
            if (choice == null) {
                log.trace("user aborted copy");
                return;
            }
            DAVTreeNode leafNode = choice.getSelectedLeaf();
            if (leafNode == null) {
                log.trace("user aborted copy");
                return;
            }
            localFile = leafNode.getDAVFile();
            if (localFile == null) {
                log.trace("selected node has null file: {0}", leafNode);
                return;
            }
            localNode = ex.getNodeForFile(localFile);
        } else {
            localNode = (node != null) ? node : ex.getSelectedNode();
            assert localNode != null : "node is not null";
        }
        String localDestinationURL = (destinationURL != null) ? destinationURL : obtainDestination(URLDecoder.decode(localFile.getFileName()), localNode);
        if (localDestinationURL != null) execute(ex, localNode, localFile, localDestinationURL);
    }

    protected void execute(Explorer ex, DAVTreeNode node, DAVFile file, String destinationURL) {
        ex.copy(node, file, destinationURL);
    }

    protected String getDialogTitle() {
        return ResourceManager.getMessage(ResourceManager.COPY_DIALOG_TITLE);
    }

    protected Explorer getExplorer() {
        return ex;
    }

    protected String obtainDestination(String filename, DAVTreeNode node) {
        String title = getDialogTitle();
        DAVFileChooser.ChoiceStruct choice = ex.chooseFile(node, title, true, filename);
        if (choice == null) return null;
        DAVTreeNode parentNode = choice.getParentNode();
        String newName = choice.getFilename();
        if (parentNode == null || newName == null) return null;
        String destination = new StringBuffer().append(parentNode.getDAVFile().getFullNameWithProtocol()).append(newName).toString();
        log.trace("returning from obtainDestination(): {0}", destination);
        return destination;
    }
}
