package com.nullfish.app.jfd2.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.impl.local.LocalFile;

/**
 * @author shunji
 *
 */
public class TransferableVFileList implements Transferable {

    private VFile[] files;

    private Object from;

    private boolean local = true;

    private boolean cut = false;

    public static final DataFlavor LOCAL_VFILELIST_FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + "; class=com.nullfish.app.jfd2.dnd.TransferableVFileList", "List of VFiles for jFD2");

    private static final DataFlavor[] nomalFlavors = { DataFlavor.stringFlavor };

    private static final DataFlavor[] localFlavors = { LOCAL_VFILELIST_FLAVOR, DataFlavor.javaFileListFlavor, DataFlavor.stringFlavor };

    public TransferableVFileList(VFile[] files, Object from, boolean cut) {
        this.files = files;
        this.from = from;
        this.cut = cut;
        for (int i = 0; i < files.length; i++) {
            if (!(files[i] instanceof LocalFile)) {
                local = false;
                break;
            }
        }
    }

    public DataFlavor[] getTransferDataFlavors() {
        if (local) {
            return localFlavors;
        } else {
            return nomalFlavors;
        }
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        if (flavor.equals(DataFlavor.stringFlavor)) {
            return true;
        }
        if (local && (flavor.equals(DataFlavor.javaFileListFlavor) || flavor.equals(LOCAL_VFILELIST_FLAVOR))) {
            return true;
        }
        return false;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor.equals(DataFlavor.stringFlavor)) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < files.length; i++) {
                buffer.append(files[i].getAbsolutePath());
                if (i != files.length - 1) {
                    buffer.append('\n');
                }
            }
            return buffer.toString();
        }
        if (local && flavor.equals(DataFlavor.javaFileListFlavor)) {
            List rtn = new ArrayList();
            for (int i = 0; i < files.length; i++) {
                rtn.add(((LocalFile) files[i]).getFile());
            }
            return rtn;
        }
        if (local && flavor.equals(LOCAL_VFILELIST_FLAVOR)) {
            return this;
        }
        return null;
    }

    public Object getFrom() {
        return from;
    }

    public VFile[] getFiles() {
        return files;
    }

    public boolean isCut() {
        return cut;
    }
}
