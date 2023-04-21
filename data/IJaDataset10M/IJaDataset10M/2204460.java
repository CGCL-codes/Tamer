package jaco.swing;

import jaco.image.ImageCache;
import jaco.image.ImageUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * {@link ImageEditorComponent2}
 * 
 * @version 1.5.0, October 21, 2011
 * @author Cristian Sulea ( http://cristiansulea.entrust.ro )
 */
public class ImageEditorComponent2 extends JPanel {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** the cache for {@link #read(File, boolean)} method */
    private static final ImageCache CACHE_CANVAS_IMAGE = new ImageCache();

    private BufferedImage originalImage;

    private BufferedImage image;

    private JComponent canvas;

    private AdvancedScrollPane canvasScrollPane;

    private BufferedImage canvasImage;

    private boolean isBestFit = true;

    private JButton bestFitButton;

    private JButton realSizeButton;

    private int zoom = 100;

    private JSlider zoomSlider;

    private ChangeListener zoomSliderChangeListener;

    private JButton zoomInButton;

    private JButton zoomOutButton;

    private JLabel zoomLabel;

    private Thread thread = null;

    private final Object threadLock = new Object();

    private boolean isUpdated;

    public ImageEditorComponent2(Icon bestFitIcon, Icon realSizeIcon, Icon zoomOutIcon, Icon zoomInIcon) {
        canvas = new JComponent() {

            /** serialVersionUID */
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                if (canvasImage == null) {
                    return;
                }
                int x = (getWidth() - canvasImage.getWidth()) / 2;
                int y = (getHeight() - canvasImage.getHeight()) / 2;
                g.drawImage(canvasImage, x, y, null);
            }
        };
        canvasScrollPane = new AdvancedScrollPane(canvas);
        canvasScrollPane.setBorder(BorderFactory.createEmptyBorder());
        canvasScrollPane.getViewport().addMouseWheelListener(new MouseWheelListener() {

            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() > 0) {
                    zoomIn();
                } else {
                    zoomOut();
                }
            }
        });
        bestFitButton = new JButton(bestFitIcon);
        bestFitButton.setFocusable(false);
        bestFitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setBestFit();
            }
        });
        realSizeButton = new JButton(realSizeIcon);
        realSizeButton.setFocusable(false);
        realSizeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setZoom(100);
            }
        });
        zoomSlider = new JSlider(1, 20, 10);
        zoomSlider.setFocusable(false);
        zoomSlider.setSnapToTicks(true);
        zoomSlider.setMinorTickSpacing(1);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setPreferredSize(new Dimension(190, zoomSlider.getPreferredSize().height));
        zoomSlider.addChangeListener(zoomSliderChangeListener = new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                setZoom(zoomSlider.getValue() * 10);
            }
        });
        zoomInButton = new JButton(zoomInIcon);
        zoomInButton.setFocusable(false);
        zoomInButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                zoomIn();
            }
        });
        zoomOutButton = new JButton(zoomOutIcon);
        zoomOutButton.setFocusable(false);
        zoomOutButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                zoomOut();
            }
        });
        zoomLabel = new JLabel("666%");
        zoomLabel.setPreferredSize(zoomLabel.getPreferredSize());
        setOpaque(false);
        canvasScrollPane.setOpaque(false);
        canvasScrollPane.getViewport().setOpaque(false);
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.add(bestFitButton);
        toolbar.add(realSizeButton);
        toolbar.add(new JLabel(" "));
        toolbar.add(new JSeparator(JSeparator.VERTICAL));
        toolbar.add(new JLabel(" "));
        toolbar.add(zoomInButton);
        toolbar.add(zoomSlider);
        toolbar.add(zoomOutButton);
        toolbar.add(new JLabel(" "));
        toolbar.add(zoomLabel);
        JPanel toolbarContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        toolbarContainer.add(toolbar);
        setLayout(new BorderLayout(5, 5));
        add(canvasScrollPane, BorderLayout.CENTER);
        add(toolbarContainer, BorderLayout.SOUTH);
        addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                updateCanvas(false);
            }
        });
    }

    public void setImage(BufferedImage image) {
        this.originalImage = image;
        this.image = image;
        updateImage();
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setBestFit() {
        this.isBestFit = true;
        updateImage();
    }

    public boolean isBestFit() {
        return isBestFit;
    }

    public void zoom(int zoom) {
        setZoom(zoom);
    }

    public void zoomIn() {
        setZoom((getZoom() - 1) / 10 * 10);
    }

    public void zoomOut() {
        setZoom((getZoom() + 10) / 10 * 10);
    }

    public void setZoom(int zoom) {
        if (zoom == this.zoom) {
            return;
        }
        if (zoom <= 0 || zoom > 200) {
            return;
        }
        this.zoom = zoom;
        this.isBestFit = false;
        updateCanvas(true);
        updateZoomControls(false);
    }

    private void updateZoomControls(boolean removeListeners) {
        if (removeListeners) {
            zoomSlider.removeChangeListener(zoomSliderChangeListener);
        }
        zoomSlider.setValue(zoom / 10);
        zoomLabel.setText(zoom + "%");
        if (removeListeners) {
            zoomSlider.addChangeListener(zoomSliderChangeListener);
        }
    }

    public int getZoom() {
        return zoom;
    }

    public final void updateImage() {
        this.isUpdated = false;
        if (thread == null) {
            synchronized (threadLock) {
                if (thread == null) {
                    thread = new Thread(new Runnable() {

                        public void run() {
                            while (!isUpdated) {
                                isUpdated = true;
                                image = updateImage(originalImage);
                                updateCanvas(false);
                            }
                            synchronized (threadLock) {
                                thread = null;
                            }
                        }
                    });
                    thread.setPriority(Thread.MIN_PRIORITY);
                    thread.start();
                }
            }
        }
    }

    private void updateCanvas(boolean isZoom) {
        if (image == null) {
            canvasImage = null;
            canvas.setPreferredSize(new Dimension(0, 0));
        } else {
            BufferedImage canvasImageOld = canvasImage;
            if (isBestFit) {
                int width = canvasScrollPane.getWidth();
                int height = canvasScrollPane.getHeight();
                if (width > 0 && height > 0) {
                    canvasImage = CACHE_CANVAS_IMAGE.getImage(image, width, height);
                    if (canvasImage == null) {
                        canvasImage = ImageUtils.resize(image, width, height);
                    }
                    CACHE_CANVAS_IMAGE.addImage(canvasImage, image, width, height);
                    zoom = canvasImage.getWidth() * 100 / image.getWidth();
                    updateZoomControls(true);
                } else {
                    canvasImage = image;
                }
            } else if (zoom == 100) {
                canvasImage = image;
            } else {
                canvasImage = CACHE_CANVAS_IMAGE.getImage(image, zoom);
                if (canvasImage == null) {
                    canvasImage = ImageUtils.zoom(image, zoom);
                }
                CACHE_CANVAS_IMAGE.addImage(canvasImage, image, zoom);
            }
            canvas.setPreferredSize(new Dimension(canvasImage.getWidth(), canvasImage.getHeight()));
            if (canvasImageOld != null) {
                Rectangle visibleRect = canvas.getVisibleRect();
                double ratio = ((double) canvasImage.getWidth()) / ((double) canvasImageOld.getWidth());
                visibleRect.x = (int) (visibleRect.getX() * ratio);
                visibleRect.y = (int) (visibleRect.getY() * ratio);
                canvas.scrollRectToVisible(visibleRect);
            }
        }
        canvas.getParent().doLayout();
        canvasScrollPane.repaint();
        canvasScrollPane.setDragToScroll(canvasScrollPane.getHorizontalScrollBar().isVisible() || canvasScrollPane.getVerticalScrollBar().isVisible());
        if (!isZoom) {
            updateCanvas(canvas, canvasScrollPane, canvasImage);
        }
    }

    protected BufferedImage updateImage(BufferedImage originalImage) {
        if (originalImage == null) {
            return null;
        }
        BufferedImage image = ImageUtils.create(originalImage.getWidth(), originalImage.getHeight(), false);
        Graphics2D g = image.createGraphics();
        g.drawImage(originalImage, 0, 0, null);
        g.dispose();
        return image;
    }

    protected void updateCanvas(JComponent canvas, AdvancedScrollPane canvasScrollPane, BufferedImage canvasImage) {
    }

    public JButton getBestFitButton() {
        return bestFitButton;
    }

    public JButton getRealSizeButton() {
        return realSizeButton;
    }

    public JSlider getZoomSlider() {
        return zoomSlider;
    }

    public JButton getZoomInButton() {
        return zoomInButton;
    }

    public JButton getZoomOutButton() {
        return zoomOutButton;
    }
}
