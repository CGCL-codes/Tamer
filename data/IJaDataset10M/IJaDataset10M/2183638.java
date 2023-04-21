package org.charvolant.tmsnet.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.charvolant.properties.BeanDescription;
import org.charvolant.tmsnet.TMSClientPreferences;
import org.charvolant.tmsnet.client.TMSNetClient;
import org.charvolant.tmsnet.i18n.TMSUIBundle;
import org.charvolant.tmsnet.model.AbstractFile;
import org.charvolant.tmsnet.model.Directory;
import org.charvolant.tmsnet.model.FileEntry;
import org.charvolant.tmsnet.util.ActionMenu;
import org.charvolant.tmsnet.util.ActionResources;
import org.charvolant.tmsnet.util.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TableItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A table that displays the current directory
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
public class DirectoryTable extends InformationTable<FileEntry, Directory> {

    /** The logger for this class */
    private static final Logger logger = LoggerFactory.getLogger(DirectoryTable.class);

    /** The name of the resources file */
    private static final String ACTION_RESOURCES = "DirectoryTableMenu.xml";

    /** The name of the context menu */
    private static final String CONTEXT_MENU = "context";

    /** The directory to display */
    private Directory directory;

    /**
   * Construct a directory table.
   *
   * @param client The client
   * @param resources The resource manager
   * @param parent The parent widget
   * @param style Style flags
   * 
   * @throws Exception
   */
    public DirectoryTable(TMSNetClient client, ResourceManager resources, Composite parent, int style) throws Exception {
        super(FileEntry.class, client, true, resources, parent, style);
        this.directory = null;
    }

    /**
   * Build the context menu action.
   *
   * @return The context menu action.
   * 
   * @see org.charvolant.tmsnet.ui.AbstractTreePanel#buildContextAction()
   */
    @Override
    protected ActionMenu buildContextAction() {
        ActionResources res = this.resources.getActionResources(this, this.ACTION_RESOURCES);
        return (ActionMenu) res.getChild(this.CONTEXT_MENU);
    }

    /**
   * Get the list of items in the directory.
   * <p>
   * Only return file items.
   *
   * @return
   * @see org.charvolant.tmsnet.ui.InformationTable#getItems()
   */
    @Override
    public List<FileEntry> getItems() {
        List<FileEntry> items;
        if (this.directory == null) return null;
        items = new ArrayList<FileEntry>(this.directory.getContents().size());
        for (AbstractFile<?> item : this.directory.getContents()) {
            if (item instanceof FileEntry) items.add((FileEntry) item);
        }
        return items;
    }

    /**
   * {@inheritDoc}
   * 
   * @see org.charvolant.tmsnet.ui.InformationTable#setItems(org.charvolant.tmsnet.AbstractModel)
   */
    @Override
    public void setItems(Directory items) {
        this.directory = items;
        this.postUpdate();
    }

    /**
   * Update a table item to reflect the type of file entry that we have
   *
   * @param item The table item
   * @param data The file entry this refers to
   * 
   * @see org.charvolant.tmsnet.ui.InformationTable#updateItem(org.eclipse.swt.widgets.TableItem, org.charvolant.tmsnet.AbstractModel)
   */
    @Override
    protected void updateItem(TableItem item, FileEntry data) {
        super.updateItem(item, data);
        if (data.isDeleted()) {
            item.setBackground(this.getDisplay().getSystemColor(SWT.COLOR_GRAY));
            item.setForeground(this.getDisplay().getSystemColor(SWT.COLOR_RED));
        } else if (data.isRecording()) {
            item.setBackground(null);
            item.setForeground(this.resources.getHighlightColor(SWT.COLOR_LIST_FOREGROUND));
        } else {
            item.setBackground(null);
            item.setForeground(null);
        }
    }

    /**
   * Get the properties for the file entries. 
   *
   * @return
   * @see org.charvolant.tmsnet.ui.AbstractInformationPanel#getProperties()
   */
    @Override
    protected String[] getProperties() {
        return new String[] { "displayName", "size", "modified" };
    }

    /**
   * {@inheritDoc}
   *
   * @return An empty array
   * 
   * @see org.charvolant.tmsnet.ui.ClientInformationPanel#getStateProperties()
   */
    @Override
    protected String[] getStateProperties() {
        return new String[0];
    }

    /**
   * Is the currently selected recording playable?
   * 
   * @return True if we have a video-playable file.
   * 
   */
    public boolean isFilePlayable() {
        return this.model != null && this.model.isVideoPlayable();
    }

    /**
   * Play the currently selected recording.
   */
    public void doPlay() {
        if (this.model == null) return;
        this.getClient().playRecording(this.model);
    }

    /**
   * Is the currently selected recording deletable?
   * 
   * @return True if we have a file that is not currently deleted
   * 
   */
    public boolean isFileDeletable() {
        return this.model != null && (!this.model.isDeleted() || !this.getClient().hasDeleteUndo(this.model));
    }

    /**
   * Play the currently selected recording.
   */
    public void doDelete() {
        if (this.model == null) return;
        this.getClient().deleteFile(this.model);
    }

    /**
   * Is the currently selected recording renamable?
   * 
   * @return True if we have a file that is not currently deleted
   * 
   */
    public boolean isFileRenamable() {
        return this.model != null && !this.model.isDeleted();
    }

    /**
   * Rename the currently selected recording.
   */
    public void doRename() {
        String name;
        if (this.model == null) return;
        try {
            name = FilenamePanel.rename(this.getShell(), this.resources, this.model);
            if (name == null) return;
            this.getClient().renameFile(this.model, name);
        } catch (Exception ex) {
            this.logger.error("Unable to open rename dialog", ex);
            return;
        }
    }

    /**
   * Is the currently selected file savable?
   * 
   * @return True if we have a file that is not currently deleted
   * 
   */
    public boolean isFileSavable() {
        return this.model != null && !this.model.isDeleted();
    }

    /**
   * Save the currently selected recording.
   */
    public void doSave() {
        TMSClientPreferences prefs = this.getClient().getPreferences();
        BeanDescription bd;
        File save;
        try {
            bd = new BeanDescription(TMSClientPreferences.class);
            save = bd.getProperty("fileSave").getWithDefault(prefs);
            this.getClient().saveFile(this.model, new File(save, this.model.getName()));
        } catch (Exception ex) {
            this.getClient().reportError(ex);
        }
    }

    /**
   * Save the currently selected recording under a new name.
   */
    public void doSaveAs() {
        TMSClientPreferences prefs = this.getClient().getPreferences();
        BeanDescription bd;
        File saveDir;
        File save, newSaveDir;
        FileDialog dialog = new FileDialog(this.getShell(), SWT.SAVE);
        String name;
        try {
            bd = new BeanDescription(TMSClientPreferences.class);
            saveDir = bd.getProperty("fileSave").getWithDefault(prefs);
            dialog.setText(this.resources.getString(TMSUIBundle.TITLE_SAVEAS));
            dialog.setFilterPath(saveDir.getAbsolutePath());
            dialog.setFileName(this.model.getName());
            name = dialog.open();
            if (name == null) return;
            save = new File(name);
            newSaveDir = save.getParentFile();
            if ((save.exists() && !save.canWrite()) || !newSaveDir.exists() || !newSaveDir.canWrite()) return;
            this.getClient().saveFile(this.model, save);
            if (!saveDir.equals(newSaveDir)) {
                prefs.setFileSave(newSaveDir);
                prefs.save();
            }
        } catch (Exception ex) {
            this.getClient().reportError(ex);
        }
    }
}
