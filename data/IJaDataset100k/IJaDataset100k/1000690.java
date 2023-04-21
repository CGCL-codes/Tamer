package frost.storage.perst.filelist;

import org.garret.perst.*;
import frost.fileTransfer.*;

public class PerstFileListIndexEntry extends Persistent {

    private IPersistentList<FrostFileListFileObjectOwner> fileOwnersWithText;

    public PerstFileListIndexEntry() {
    }

    public PerstFileListIndexEntry(final Storage storage) {
        fileOwnersWithText = storage.createScalableList();
    }

    public IPersistentList<FrostFileListFileObjectOwner> getFileOwnersWithText() {
        return fileOwnersWithText;
    }

    public void addFileOwnerWithText(final FrostFileListFileObjectOwner pmo) {
        fileOwnersWithText.add(pmo);
    }

    public void removeFileOwnerWithText(final FrostFileListFileObjectOwner pmo) {
        fileOwnersWithText.remove(pmo);
    }

    @Override
    public void deallocate() {
        if (fileOwnersWithText != null) {
            fileOwnersWithText.deallocate();
            fileOwnersWithText = null;
        }
        super.deallocate();
    }
}
