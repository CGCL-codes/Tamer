package com.orientechnologies.tools.oexplorer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;
import javax.swing.JFrame;

public class DragHandler extends JFrame {

    public DragHandler() {
        try {
            DropTarget dropTarget = new DropTarget();
            dropTarget.setComponent(this);
            dropTarget.addDropTargetListener(new DropHandlerAdapter());
        } catch (Exception e) {
        }
    }

    private class DropHandlerAdapter extends DropTargetAdapter {

        public void drop(DropTargetDropEvent dtde) {
            Transferable t = dtde.getTransferable();
            try {
                DataFlavor[] dataFlavors = t.getTransferDataFlavors();
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                for (int i = 0; i < dataFlavors.length; i++) {
                    if (dataFlavors[i].getRepresentationClass().equals(Class.forName("java.util.List"))) {
                        List fileList = (List) t.getTransferData(dataFlavors[i]);
                        File fileDropped = (File) fileList.get(0);
                        String fileName = fileDropped.getAbsolutePath().toUpperCase();
                        if (fileName.endsWith(".XML")) Application.getInstance().getMainFrame().getViewDatabase().importDatabase(fileDropped.getAbsolutePath()); else if (fileName.endsWith(".OAR")) Application.getInstance().getMainFrame().getViewDatabase().restoreDatabase(fileDropped.getAbsolutePath()); else if (fileName.endsWith(".ODL")) Application.getInstance().getMainFrame().getViewDatabase().createDatabase(null, null, fileDropped.getAbsolutePath());
                    }
                }
                dtde.dropComplete(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
