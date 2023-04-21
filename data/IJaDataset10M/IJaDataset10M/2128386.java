package edu.whitman.halfway.jigs.gui.desque;

import edu.whitman.halfway.jigs.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;
import org.apache.log4j.Logger;

public class SingleImagePanel extends JLabel implements ListSelectionListener {

    private static Logger log = Logger.getLogger(SingleImagePanel.class.getName());

    private AlbumModel albumModel;

    private ListSelectionModel selectionModel;

    private ImageCostFunction costFunction;

    private LoaderThread thread = null;

    public SingleImagePanel(AlbumModel albumModel, ListSelectionModel selectionModel) {
        this(albumModel, selectionModel, new DefaultImageCostFunction());
    }

    public SingleImagePanel(AlbumModel albumModel, ListSelectionModel selectionModel, ImageCostFunction costFunction) {
        this.albumModel = albumModel;
        this.selectionModel = selectionModel;
        this.costFunction = costFunction;
        setHorizontalAlignment(JLabel.CENTER);
        selectionModel.addListSelectionListener(this);
    }

    public void valueChanged(ListSelectionEvent evt) {
        if (!evt.getValueIsAdjusting()) {
            int index = selectionModel.getAnchorSelectionIndex();
            AlbumObject ao = null;
            if (index >= 0) {
                ao = albumModel.getAlbumObject(index);
            }
            if (ao == null || !(ao instanceof Picture)) {
                setIcon(null);
            } else {
                if (thread != null) thread.kill();
                thread = new LoaderThread((Picture) ao);
                thread.start();
            }
        }
    }

    class LoaderThread extends Thread {

        private Picture picture;

        public LoaderThread(Picture pic) {
            this.picture = pic;
        }

        public void kill() {
        }

        public void run() {
            Dimension maxSize = getSize();
            Dimension minSize = new Dimension((int) maxSize.getWidth() - 50, (int) maxSize.getHeight() - 50);
            BufferedImage img = PictureUtil.getBufferedImage(picture, maxSize, minSize, costFunction, false, true);
            if (img != null) {
                setIcon(new ImageIcon(img));
            } else {
                setIcon(null);
            }
        }
    }
}
