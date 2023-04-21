package org.charvolant.tmsnet.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.charvolant.tmsnet.command.RenameFile;
import org.charvolant.tmsnet.model.FileEntry;
import org.charvolant.tmsnet.model.FileInfo;

/**
 * A transaction to rename a file.
 * <p>
 * The file is renamed. But we keep the original
 * name to allow reversal.
 * <p>
 * If the file contains multiple files (info and nav files)
 * then the renaming is according to the file pattern.
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
@XmlRootElement()
@XmlAccessorType(XmlAccessType.NONE)
public class RenameFileTransaction extends Transaction<Transactable> {

    /** The skip trigger */
    public static final String SKIP = "skip";

    /** The renamed file TODO Xml-ise this */
    private FileEntry file;

    /** The new name */
    @XmlElement
    private String newName;

    /** The old name */
    @XmlElement
    private String oldName;

    /** The file being renamed */
    private FileInfo renamer;

    /** The new name */
    private String name;

    /**
   * Construct an empty rename file transaction
   *
   */
    public RenameFileTransaction() {
        super();
    }

    /**
   * Construct a transaction for a specific file and name
   * 
   * @param file The file
   * @param newName The new name
   */
    public RenameFileTransaction(FileEntry file, String newName) {
        super();
        this.file = file;
        this.oldName = this.file.getBaseName();
        this.newName = newName;
    }

    /**
   * Get the file.
   *
   * @return the file
   */
    public FileEntry getFile() {
        return this.file;
    }

    /**
   * Perform a renaming.
   * 
   * @param renamer The file to rename
   */
    private void performRename(FileInfo renamer, String from, String to) {
        this.renamer = renamer;
        if (this.renamer == null) this.event(this.SKIP); else {
            this.name = this.renamer.getName();
            if (this.name.startsWith(from)) this.name = to + this.name.substring(from.length());
            if (this.renamer.getName().equals(this.name)) this.event(this.SKIP); else this.queue(new RenameFile(this.file.getPath(this.renamer), this.name));
        }
    }

    /**
   * Start the ball rolling by renaming the file to be deleted.
   *
   * @see org.charvolant.tmsnet.client.Transaction#onExecute()
   */
    @Override
    protected void onExecute() {
        super.onExecute();
        this.performRename(this.file.getFile(), this.oldName, this.newName);
    }

    /**
   * If there is an .inf file then rename that, too.
   */
    protected void onExecuteInfo() {
        this.performRename(this.file.getInfo(), this.oldName, this.newName);
    }

    /**
   * If there is an .nav file then rename that, too.
   */
    protected void onExecuteNav() {
        this.performRename(this.file.getNavigation(), this.oldName, this.newName);
    }

    /**
   * Respond to a renaming success by renaming the underlying file.
   */
    protected void onRename() {
        if (this.renamer != null) this.renamer.setName(this.name);
    }

    /**
   * Rollback by re-naming the file back to the original name.
   *
   * @see org.charvolant.tmsnet.client.Transaction#onRollback()
   */
    @Override
    protected void onRollingBack() {
        super.onRollingBack();
        this.performRename(this.file.getFile(), this.newName, this.oldName);
    }

    /**
   * If there is an .inf file then rename that, too.
   */
    protected void onRollbackInfo() {
        this.performRename(this.file.getInfo(), this.newName, this.oldName);
    }

    /**
   * If there is an .nav file then rename that, too.
   */
    protected void onRollbackNav() {
        this.performRename(this.file.getNavigation(), this.newName, this.oldName);
    }
}
