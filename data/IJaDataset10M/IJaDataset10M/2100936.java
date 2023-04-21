package de.annee.mosaix.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import de.annee.mosaix.model.Model;

class JSourceImage extends JComponent implements DropTargetListener {

    private BufferedImage raster;

    public JSourceImage() {
        super();
        try {
            raster = ImageIO.read(getClass().getResource("res/raster.png"));
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
        setPreferredSize(new Dimension(200, 200));
        setOpaque(true);
        setBorder(BorderFactory.createLoweredBevelBorder());
        DropTarget dropTarget = new DropTarget(this, this);
        dropTarget.setActive(true);
        setToolTipText("<html>Zieh ein Bild auf diesen Bereich<br>um es als <b>Zielbild</b> zu verwenden.</html>");
    }

    public void paint(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setPaint(new TexturePaint(raster, new Rectangle(20, 20)));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        if (Model.getInstance().getSourceImage().getImage() != null) {
            double scale;
            if (Model.getInstance().getSourceImage().getImageWidth().intValue() > Model.getInstance().getSourceImage().getImageHeight().intValue()) {
                scale = (double) getWidth() / Model.getInstance().getSourceImage().getImageWidth().intValue();
            } else {
                scale = (double) getHeight() / Model.getInstance().getSourceImage().getImageHeight().intValue();
            }
            g2d.drawImage(Model.getInstance().getSourceImage().getImage(), new AffineTransformOp(new AffineTransform(scale, 0, 0, scale, 0, 0), AffineTransformOp.TYPE_NEAREST_NEIGHBOR), 0, 0);
        }
        super.paintBorder(graphics);
    }

    public void dragEnter(DropTargetDragEvent event) {
        if (event.getTransferable().isDataFlavorSupported(DataFlavor.javaFileListFlavor)) event.acceptDrag(DnDConstants.ACTION_LINK); else event.acceptDrag(DnDConstants.ACTION_NONE);
    }

    public void dragExit(DropTargetEvent event) {
    }

    public void dragOver(DropTargetDragEvent event) {
        if (event.getTransferable().isDataFlavorSupported(DataFlavor.javaFileListFlavor)) event.acceptDrag(DnDConstants.ACTION_LINK); else event.acceptDrag(DnDConstants.ACTION_NONE);
    }

    protected void openImage(File file) throws IOException {
        Model.getInstance().getSourceImage().setImage(ImageIO.read(file));
        repaint();
    }

    public void drop(DropTargetDropEvent event) {
        event.acceptDrop(DnDConstants.ACTION_LINK);
        boolean success = false;
        try {
            List list = (List) event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
            if (list.size() == 1) {
                File imageFile = (File) list.get(0);
                openImage(imageFile);
                success = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        event.dropComplete(success);
    }

    public void dropActionChanged(DropTargetDragEvent event) {
    }
}
