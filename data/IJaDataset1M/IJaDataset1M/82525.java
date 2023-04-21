package org.photovault.swingui;

import java.awt.dnd.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import org.photovault.imginfo.*;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.folder.*;

/**
   Implements the dropping logic for PhotoInfoTree. Modified from JavaWorld tip,
   @see http://www.javaworld.com/javaworld/javatips/jw-javatip114.html
*/
class PhotoTreeDropTargetListener implements DropTargetListener {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PhotoTreeDropTargetListener.class.getName());

    private TreePath lastPath = null;

    private Rectangle2D cueRect = new Rectangle2D.Float();

    private Rectangle2D ghostImgRect = new Rectangle2D.Float();

    private Color cueRectColor;

    private Point _ptLast = new Point();

    private Timer hoverTimer;

    private int _nLeftRight = 0;

    private int _nShift = 0;

    private final JTree tree;

    /**
       Data flavor for an array of PhotoInfo objects. This is used when transferring
       photos inside the same virtual machine
    */
    DataFlavor photoInfoFlavor = null;

    public PhotoTreeDropTargetListener(JTree t) {
        this.tree = t;
        cueRectColor = new Color(SystemColor.controlShadow.getRed(), SystemColor.controlShadow.getGreen(), SystemColor.controlShadow.getBlue(), 64);
        try {
            photoInfoFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + PhotoInfo[].class.getName() + "\"");
        } catch (Exception e) {
        }
        hoverTimer = new Timer(1000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (isRootPath(lastPath)) return;
                if (tree.isExpanded(lastPath)) tree.collapsePath(lastPath); else tree.expandPath(lastPath);
            }
        });
        hoverTimer.setRepeats(false);
    }

    public void dragEnter(DropTargetDragEvent e) {
        if (!isDragAcceptable(e)) e.rejectDrag(); else e.acceptDrag(e.getDropAction());
    }

    public void dragExit(DropTargetEvent e) {
        if (!DragSource.isDragImageSupported()) {
            tree.repaint(ghostImgRect.getBounds());
        }
    }

    /**
     * This is where the ghost image is drawn
     */
    public void dragOver(DropTargetDragEvent e) {
        Point pt = e.getLocation();
        if (pt.equals(_ptLast)) return;
        int nDeltaLeftRight = pt.x - _ptLast.x;
        if ((_nLeftRight > 0 && nDeltaLeftRight < 0) || (_nLeftRight < 0 && nDeltaLeftRight > 0)) _nLeftRight = 0;
        _nLeftRight += nDeltaLeftRight;
        _ptLast = pt;
        Graphics2D g2 = (Graphics2D) tree.getGraphics();
        if (!DragSource.isDragImageSupported()) {
            tree.paintImmediately(ghostImgRect.getBounds());
        } else tree.paintImmediately(cueRect.getBounds());
        TreePath path = tree.getClosestPathForLocation(pt.x, pt.y);
        if (!(path == lastPath)) {
            _nLeftRight = 0;
            lastPath = path;
            hoverTimer.restart();
        }
        Rectangle raPath = tree.getPathBounds(path);
        cueRect.setRect(raPath);
        g2.setColor(cueRectColor);
        g2.fill(cueRect);
        if (_nLeftRight > 20) {
            _nShift = +1;
        } else if (_nLeftRight < -20) {
            _nShift = -1;
        } else _nShift = 0;
        ghostImgRect = ghostImgRect.createUnion(cueRect);
    }

    public void dropActionChanged(DropTargetDragEvent e) {
        if (!isDragAcceptable(e)) e.rejectDrag(); else e.acceptDrag(e.getDropAction());
    }

    public void drop(DropTargetDropEvent e) {
        hoverTimer.stop();
        if (!isDropAcceptable(e)) {
            e.rejectDrop();
            return;
        }
        e.acceptDrop(e.getDropAction());
        Transferable transferable = e.getTransferable();
        DataFlavor[] flavors = transferable.getTransferDataFlavors();
        for (int i = 0; i < flavors.length; i++) {
            DataFlavor flavor = flavors[i];
            if (flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType)) {
                try {
                    Point pt = e.getLocation();
                    TreePath pathTarget = tree.getClosestPathForLocation(pt.x, pt.y);
                    PhotoFolder folder = (PhotoFolder) pathTarget.getLastPathComponent();
                    PhotoCollectionTransferHandler.setLastImportTarget(folder);
                    PhotoInfo[] photos = (PhotoInfo[]) transferable.getTransferData(photoInfoFlavor);
                    for (int n = 0; n < photos.length; n++) {
                        folder.addPhoto(photos[n]);
                    }
                    break;
                } catch (UnsupportedFlavorException ufe) {
                    log.warn(ufe);
                    e.dropComplete(false);
                    return;
                } catch (IOException ioe) {
                    log.warn(ioe);
                    e.dropComplete(false);
                    return;
                }
            }
        }
        e.dropComplete(true);
    }

    public boolean isDragAcceptable(DropTargetDragEvent e) {
        return true;
    }

    public boolean isDropAcceptable(DropTargetDropEvent e) {
        return true;
    }

    private boolean isRootPath(TreePath path) {
        return tree.isRootVisible() && tree.getRowForPath(path) == 0;
    }
}
