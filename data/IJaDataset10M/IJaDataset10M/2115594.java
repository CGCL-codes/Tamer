package frost.fileTransfer.common;

import frost.*;
import frost.fileTransfer.*;
import frost.identities.*;
import frost.util.*;
import frost.util.model.*;

public class FileListFileDetailsItem extends ModelItem implements CopyToClipboardItem {

    private FrostFileListFileObjectOwner fileOwner;

    private String displayComment = null;

    private String displayKeywords = null;

    private String displayLastReceived;

    private String displayLastUploaded;

    private Identity ownerIdentity = null;

    public FileListFileDetailsItem(FrostFileListFileObjectOwner o) {
        fileOwner = o;
    }

    public String getDisplayComment() {
        if (displayComment == null) {
            if (fileOwner.getComment() == null) {
                displayComment = "";
            } else {
                displayComment = fileOwner.getComment();
            }
        }
        return displayComment;
    }

    public String getDisplayKeywords() {
        if (displayKeywords == null) {
            if (fileOwner.getKeywords() == null) {
                displayKeywords = "";
            } else {
                displayKeywords = fileOwner.getKeywords();
            }
        }
        return displayKeywords;
    }

    public String getDisplayLastReceived() {
        if (displayLastReceived == null) {
            if (fileOwner.getLastReceived() == 0) {
                displayLastReceived = "";
            } else {
                displayLastReceived = DateFun.getExtendedDateFromMillis(fileOwner.getLastReceived());
            }
        }
        return displayLastReceived;
    }

    public String getDisplayLastUploaded() {
        if (displayLastUploaded == null) {
            if (fileOwner.getLastUploaded() == 0) {
                displayLastUploaded = "";
            } else {
                displayLastUploaded = DateFun.getExtendedDateFromMillis(fileOwner.getLastUploaded());
            }
        }
        return displayLastUploaded;
    }

    public Identity getOwnerIdentity() {
        if (ownerIdentity == null) {
            ownerIdentity = Core.getIdentities().getIdentity(fileOwner.getOwner());
        }
        return ownerIdentity;
    }

    public String getKey() {
        return fileOwner.getKey();
    }

    public String getFilename() {
        return getFileOwner().getName();
    }

    public long getFileSize() {
        return -1;
    }

    public FrostFileListFileObjectOwner getFileOwner() {
        return fileOwner;
    }
}
