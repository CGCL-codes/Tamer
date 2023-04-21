package org.imagearchive.lsm.toolbox.gui;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.ImageWindow;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import javax.swing.SwingUtilities;
import org.imagearchive.lsm.reader.info.LSMFileInfo;
import org.imagearchive.lsm.toolbox.MasterModel;
import org.imagearchive.lsm.toolbox.Reader;

public class CPDragAndDrop implements DropTargetListener {

    protected static ImageJ ij = null;

    private static boolean enableDND = true;

    protected DataFlavor dFlavor;

    private MasterModel masterModel;

    private ControlPanelFrame cp;

    public CPDragAndDrop(MasterModel masterModel, ControlPanelFrame cp) {
        String vers = System.getProperty("java.version");
        if (vers.compareTo("1.3.1") < 0) return;
        this.masterModel = masterModel;
        this.cp = cp;
        DropTarget dropTarget = new DropTarget(cp, this);
    }

    public void drop(DropTargetDropEvent dtde) {
        dtde.acceptDrop(DnDConstants.ACTION_COPY);
        try {
            Transferable t = dtde.getTransferable();
            if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                Object data = t.getTransferData(DataFlavor.javaFileListFlavor);
                Iterator iterator = ((List) data).iterator();
                while (iterator.hasNext()) {
                    final File file = (File) iterator.next();
                    final Reader reader = new Reader(masterModel);
                    SwingUtilities.invokeLater(new Runnable() {

                        ImageWindow iwc = null;

                        public void run() {
                            try {
                                IJ.showStatus("Loading image");
                                ImagePlus imp = reader.open(file.getAbsolutePath(), true);
                                IJ.showStatus("Image loaded");
                                if (imp == null) return;
                                imp.setPosition(1, 1, 1);
                                imp.show();
                            } catch (OutOfMemoryError e) {
                                IJ.outOfMemory("Could not load lsm image.");
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            dtde.dropComplete(false);
            return;
        }
        dtde.dropComplete(true);
    }

    public void dragEnter(DropTargetDragEvent dtde) {
        dtde.acceptDrag(DnDConstants.ACTION_COPY);
    }

    public void dragOver(DropTargetDragEvent e) {
    }

    public void dragExit(DropTargetEvent e) {
    }

    public void dropActionChanged(DropTargetDragEvent e) {
    }
}
