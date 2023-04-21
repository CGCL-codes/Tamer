package be.vds.jtbdive.client.swing.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXImageView;
import be.vds.jtbdive.client.util.ResourceManager;
import be.vds.jtbdive.client.view.panels.document.Viewer;

public class ImageViewer extends Viewer {

    private JXImageView viewer;

    public ImageViewer() {
        init();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        this.add(createViewerPanel(), BorderLayout.CENTER);
        this.add(createButtonsPanel(), BorderLayout.NORTH);
    }

    private Component createButtonsPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEADING));
        p.add(createZoomInButton());
        p.add(createZoomOutButton());
        p.add(createRotateClockwiseButton());
        p.add(createRotateCounterClockwiseButton());
        p.add(createFitWidth());
        return p;
    }

    private Component createFitWidth() {
        JButton b = new JButton(new AbstractAction() {

            public void actionPerformed(ActionEvent actionEvent) {
                fitWidth();
            }
        });
        b.setText(null);
        b.setIcon(ResourceManager.getInstance().getImageIcon("fitwidth_16.png"));
        return b;
    }

    private Component createZoomInButton() {
        JButton b = new JButton(new AbstractAction() {

            public void actionPerformed(ActionEvent actionEvent) {
                viewer.setScale(viewer.getScale() * 2);
            }
        });
        b.setText(null);
        b.setIcon(ResourceManager.getInstance().getImageIcon("zoomin_16.png"));
        return b;
    }

    private Component createZoomOutButton() {
        JButton b = new JButton(new AbstractAction() {

            public void actionPerformed(ActionEvent actionEvent) {
                viewer.setScale(viewer.getScale() * 0.5);
            }
        });
        b.setText(null);
        b.setIcon(ResourceManager.getInstance().getImageIcon("zoomout_16.png"));
        return b;
    }

    private Component createRotateClockwiseButton() {
        JButton b = new JButton(new AbstractAction() {

            public void actionPerformed(ActionEvent actionEvent) {
                double scale = viewer.getScale();
                Image img = viewer.getImage();
                BufferedImage src = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                BufferedImage dst = new BufferedImage(img.getHeight(null), img.getWidth(null), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D) src.getGraphics();
                g.drawImage(img, 0, 0, null);
                g.dispose();
                AffineTransform trans = AffineTransform.getRotateInstance(-Math.PI / 2, 0, 0);
                trans.translate(-src.getWidth(), 0);
                BufferedImageOp op = new AffineTransformOp(trans, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                Rectangle2D rect = op.getBounds2D(src);
                op.filter(src, dst);
                viewer.setImage(dst);
                viewer.setScale(scale);
            }
        });
        b.setText(null);
        b.setIcon(ResourceManager.getInstance().getImageIcon("rotate_clockwise_16.png"));
        return b;
    }

    private Component createRotateCounterClockwiseButton() {
        JButton b = new JButton(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                double scale = viewer.getScale();
                Image img = viewer.getImage();
                BufferedImage src = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                BufferedImage dst = new BufferedImage(img.getHeight(null), img.getWidth(null), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D) src.getGraphics();
                g.drawImage(img, 0, 0, null);
                g.dispose();
                AffineTransform trans = AffineTransform.getRotateInstance(Math.PI / 2, 0, 0);
                trans.translate(0, -src.getHeight());
                BufferedImageOp op = new AffineTransformOp(trans, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                Rectangle2D rect = op.getBounds2D(src);
                op.filter(src, dst);
                viewer.setImage(dst);
                viewer.setScale(scale);
            }
        });
        b.setText(null);
        b.setIcon(ResourceManager.getInstance().getImageIcon("rotate_counter_clockwise_16.png"));
        return b;
    }

    private void fitWidthIfTooLarge() {
        if (viewer.getImage() != null) {
            double viewerW = (double) this.getParent().getWidth();
            double imageW = (double) viewer.getImage().getWidth(null);
            if (imageW > viewerW) {
                double scale = viewerW / imageW;
                if (scale > 0) viewer.setScale(scale);
            }
        }
    }

    private void fitWidth() {
        if (viewer.getImage() != null) {
            double scale = (double) ImageViewer.this.getWidth() / (double) viewer.getImage().getWidth(null);
            viewer.setScale(scale);
        }
    }

    private Component createViewerPanel() {
        viewer = new JXImageView();
        return viewer;
    }

    public void setImage(byte[] content) {
        Image image = null;
        try {
            image = ImageIO.read(new ByteArrayInputStream(content));
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewer.setImage(image);
    }

    @Override
    public void doAfterInstall() {
        fitWidthIfTooLarge();
    }
}
