package org.columba.mail.folder.zippedmh;

import org.columba.mail.config.FolderItem;
import org.columba.mail.config.IFolderItem;
import org.columba.mail.folder.AbstractLocalFolder;
import org.columba.mail.folder.IDataStorage;

public class ZippedMHFolder extends AbstractLocalFolder {

    private ZippedMHDataStorage dataStorage;

    public ZippedMHFolder(FolderItem item, String path) {
        super(item, path);
    }

    public ZippedMHFolder(String name, String type, String path) {
        super(name, type, path);
        IFolderItem item = getConfiguration();
        item.setString("property", "accessrights", "user");
        item.setString("property", "subfolder", "true");
    }

    public IDataStorage getDataStorageInstance() {
        if (dataStorage == null) {
            dataStorage = new ZippedMHDataStorage(this.getDirectoryFile());
        }
        return dataStorage;
    }
}
