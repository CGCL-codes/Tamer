package org.commentator.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.TransferHandler;

/**
 *
 * @author Trilarion
 */
public class FileDropOnListHandler extends TransferHandler {

    private static final long serialVersionUID = 1L;

    private static Logger LOG = Logger.getLogger(FileDropOnListHandler.class.getName());

    private FileListModel listModel;

    private Collection<String> validExt;

    /**
     * 
     * @param listModel
     * @param validExt
     */
    public FileDropOnListHandler(FileListModel listModel, Collection<String> validExt) {
        this.listModel = listModel;
        this.validExt = validExt;
    }

    /**
     *
     * @param support
     * @return
     */
    @Override
    public boolean canImport(TransferSupport support) {
        if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            return false;
        }
        return true;
    }

    /**
     * 
     * @param support
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }
        if (!support.isDrop()) {
            return false;
        }
        Transferable t = support.getTransferable();
        List<File> fileList = null;
        try {
            fileList = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
        } catch (UnsupportedFlavorException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        List<File> copyList = new LinkedList<File>();
        for (File file : fileList) {
            if (file.isFile()) {
                if (validExt != null) {
                    if (validExt.contains(Main.getExtension(file))) {
                        copyList.add(file);
                    }
                } else {
                    copyList.add(file);
                }
            }
        }
        if (copyList.size() > 0) {
            listModel.addFiles(copyList);
        }
        return true;
    }
}
