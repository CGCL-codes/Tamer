package org.charvolant.tmsnet.ui;

import org.charvolant.tmsnet.client.TMSNetClient;
import org.charvolant.tmsnet.model.Directory;
import org.charvolant.tmsnet.util.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * Display recordings as a directory tree.
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
public class RecordingTreePanel extends AbstractDirectoryTreePanel<RecordingTable> {

    /** The directory to start looking through recordings */
    private Directory root;

    /**
   * Construct a directory tree panel.
   *
   * @param client The client
   * @param resources The resource manager
   * @param parent The parent widget
   * @param directory The root directory to start from
   * 
   * @throws Exception if unable to build the tree
   */
    public RecordingTreePanel(TMSNetClient client, ResourceManager resources, Composite parent, Directory directory) throws Exception {
        super(client, resources, parent);
        this.root = directory;
    }

    /**
   * {@inheritDoc}
   *
   * @return The supplied root directory
   *
   * @see org.charvolant.tmsnet.ui.AbstractDirectoryTreePanel#getRoot()
   */
    @Override
    Directory getRoot() {
        return this.root;
    }

    /**
   * {@inheritDoc}
   *
   * @return The {@link RecordingTable}
   * 
   * @throws Exception
   *
   * @see org.charvolant.tmsnet.ui.AbstractTreeTablePanel#buildTable()
   */
    @Override
    protected RecordingTable buildTable() throws Exception {
        return new RecordingTable(this.client, this.resources, this, SWT.NONE);
    }
}
