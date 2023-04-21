package khall.ui;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JFrame;
import khall.image.ColorPixel;
import khall.image.JavaMosaic;
import khall.image.JpegImageUtility;

/**
 * @author keith
 * 
 * Creat a mosaic from clip art images based on a larger image
 */
public class MosaicApp extends JFrame {

    /**
     * default constructor to create ui
     *  
     */
    public MosaicApp() {
        super();
        outputImage = System.getProperty("user.dir") + File.separator;
        java.awt.GridBagConstraints gridBagConstraints;
        jScrollPane = new javax.swing.JScrollPane();
        getContentPane().setLayout(new java.awt.GridBagLayout());
        setTitle("flickrMosaic");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        jScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setViewportBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jScrollPane.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                MouseButtonReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 4.0;
        gridBagConstraints.weighty = 4.0;
        getContentPane().add(jScrollPane, gridBagConstraints);
        jpegFileImagePanel = new khall.ui.ImagePanel();
        jpegFileImagePanel.setFont(new java.awt.Font("Dialog", 0, 11));
        jpegFileImagePanel.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MouseButtonClicked(evt);
            }
        });
        jpegFileImagePanel.setName("jpegFileImagePanel");
        jpegFileImagePanel.addComponentListener(new java.awt.event.ComponentListener() {

            public void componentMoved(java.awt.event.ComponentEvent evt) {
                ComponentMoved(evt);
            }

            public void componentHidden(java.awt.event.ComponentEvent evt) {
                ComponentHidden(evt);
            }

            public void componentResized(java.awt.event.ComponentEvent evt) {
                ComponentResized(evt);
            }

            public void componentShown(java.awt.event.ComponentEvent evt) {
                ComponentShown(evt);
            }
        });
        jpegFileImagePanel.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MouseButtonClicked(evt);
            }
        });
        jScrollPane.setViewportView(jpegFileImagePanel);
        try {
            jClipXLabel = (javax.swing.JLabel) UiUtil.createWidget(this.getClass().getClassLoader(), "javax.swing.JLabel", this, 0, 7);
            jClipXLabel.setName("jClipXLabel");
            jClipXLabel.setText("Clip Art Width");
            jClipX = (javax.swing.JTextField) UiUtil.createWidget(this.getClass().getClassLoader(), "javax.swing.JTextField", this, 1, 7);
            jClipX.setName("jClipX");
            jClipX.setText("100");
            jClipYLabel = (javax.swing.JLabel) UiUtil.createWidget(this.getClass().getClassLoader(), "javax.swing.JLabel", this, 2, 7);
            jClipYLabel.setName("jClipYLabel");
            jClipYLabel.setText("Clip Art Height");
            jClipY = (javax.swing.JTextField) UiUtil.createWidget(this.getClass().getClassLoader(), "javax.swing.JTextField", this, 3, 7);
            jClipY.setName("jClipY");
            jClipY.setText("100");
            jSubSampleLabel = (javax.swing.JLabel) UiUtil.createWidget(this.getClass().getClassLoader(), "javax.swing.JLabel", this, 0, 8);
            jSubSampleLabel.setName("jSubSampleLabel");
            jSubSampleLabel.setText("Subsampling");
            jSubSample = (javax.swing.JComboBox) UiUtil.createWidget(this.getClass().getClassLoader(), "javax.swing.JComboBox", this, 1, 8);
            jSubSample.setName("jIgnoreColor");
            jSubSample.addItem("false");
            jSubSample.addItem("true");
            jIgnoreDupLabel = (javax.swing.JLabel) UiUtil.createWidget(this.getClass().getClassLoader(), "javax.swing.JLabel", this, 2, 8);
            jIgnoreDupLabel.setName("jIgnoreDupLabel");
            jIgnoreDupLabel.setText("Ignore Duplicates");
            jIgnoreDup = (javax.swing.JComboBox) UiUtil.createWidget(this.getClass().getClassLoader(), "javax.swing.JComboBox", this, 3, 8);
            jIgnoreDup.setName("jIgnoreDup");
            jIgnoreDup.addItem("false");
            jIgnoreDup.addItem("true");
            jIgnoreColorLabel = (javax.swing.JLabel) UiUtil.createWidget(this.getClass().getClassLoader(), "javax.swing.JLabel", this, 0, 9);
            jIgnoreColorLabel.setName("jIgnoreColorLabel");
            jIgnoreColorLabel.setText("Ignore Color");
            jIgnoreColorValue = (javax.swing.JTextField) UiUtil.createWidget(this.getClass().getClassLoader(), "javax.swing.JTextField", this, 1, 9);
            jIgnoreColorValue.setName("jIgnoreColorValue");
            jIgnoreColorValue.setText("");
            jIgnoreColorValue.setBackground(new Color(colorPixel.getR(), colorPixel.getG(), colorPixel.getB()));
            jIgnoreColor = (javax.swing.JComboBox) UiUtil.createWidget(this.getClass().getClassLoader(), "javax.swing.JComboBox", this, 2, 9);
            jIgnoreColor.setName("jIgnoreColor");
            jIgnoreColor.addItem("false");
            jIgnoreColor.addItem("true");
            jClipDirButton = (javax.swing.JButton) UiUtil.createWidget(this.getClass().getClassLoader(), "javax.swing.JButton", this, 0, 10);
            jClipDirButton.setName("jClipDirButton");
            jClipDirButton.setText("Select Clip Dir");
            jClipDirButton.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    MouseButtonClicked(evt);
                }
            });
            jSourceImageButton = (javax.swing.JButton) UiUtil.createWidget(this.getClass().getClassLoader(), "javax.swing.JButton", this, 1, 10);
            jSourceImageButton.setName("jSourceImageButton");
            jSourceImageButton.setText("Select Source Image");
            jSourceImageButton.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    MouseButtonClicked(evt);
                }
            });
            jGetFlickr = (javax.swing.JButton) UiUtil.createWidget(this.getClass().getClassLoader(), "javax.swing.JButton", this, 2, 10);
            jGetFlickr.setName("jGetFlickr");
            jGetFlickr.setText("Get flickr Thumbs");
            jGetFlickr.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    MouseButtonClicked(evt);
                }
            });
            jRunMosaic = (javax.swing.JButton) UiUtil.createWidget(this.getClass().getClassLoader(), "javax.swing.JButton", this, 3, 10);
            jRunMosaic.setName("jRunMosaic");
            jRunMosaic.setText("Create Mosaic");
            jRunMosaic.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    MouseButtonClicked(evt);
                }
            });
            jSaveImage = (javax.swing.JButton) UiUtil.createWidget(this.getClass().getClassLoader(), "javax.swing.JButton", this, 0, 11);
            jSaveImage.setName("jSaveImage");
            jSaveImage.setText("Save Image");
            jSaveImage.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    MouseButtonClicked(evt);
                }
            });
            pack();
            this.setIconImage(JpegImageUtility.loadImage(System.getProperty("user.dir") + File.separator + "m.jpg"));
            java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            setSize(new java.awt.Dimension(647, 504));
            setLocation((screenSize.width - 647) / 2, (screenSize.height - 504) / 2);
        } catch (Exception e) {
        }
    }

    /**
     * select click event to process
     * 
     * @param evt
     */
    private void MouseButtonClicked(java.awt.event.MouseEvent evt) {
        String evtName = evt.getComponent().getName();
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
            if (evtName.equals("jRunMosaic")) {
                runMosaic();
            } else if (evtName.equals("jClipDirButton")) {
                findClipDir();
            } else if (evtName.equals("jSourceImageButton")) {
                sourceName();
            } else if (evtName.equals("jOutputImageButton")) {
                saveName();
            } else if (evtName.equals("jSaveImage")) {
                saveImage();
            } else if (evtName.equals("jGetFlickr")) {
                runFlickr();
            } else if (evtName.equals("jpegFileImagePanel")) {
                getPixel(evt.getX(), evt.getY());
            }
        }
    }

    /**
     * select event to process
     * 
     * @param evt
     */
    private void MouseButtonReleased(java.awt.event.MouseEvent evt) {
        String evtName = evt.getComponent().getName();
        if (evtName != null && evtName.equals("jpegFileImagePanel")) {
            jpegFileImagePanel.repaint();
        }
    }

    private void ComponentMoved(java.awt.event.ComponentEvent evt) {
    }

    private void ComponentHidden(java.awt.event.ComponentEvent evt) {
    }

    private void ComponentResized(java.awt.event.ComponentEvent evt) {
        if (evt.getComponent().getName().equals("jpegFileImagePanel")) {
            if (jm.getMainImage() != null) {
                Image scaledImage = resizeImage(jm.getMainImage());
                jpegFileImagePanel.setImage(scaledImage);
            } else if (sourceBufferedImage != null) {
                Image scaledImage = resizeImage(sourceBufferedImage);
                jpegFileImagePanel.setImage(scaledImage);
            }
        }
    }

    private void ComponentShown(java.awt.event.ComponentEvent evt) {
    }

    /**
     * run the action that takes image and converts to mosaic using images from
     * the clip directory
     */
    private void runMosaic() {
        try {
            try {
                clipx = Integer.parseInt(jClipX.getText());
            } catch (Exception e) {
            }
            try {
                clipy = Integer.parseInt(jClipY.getText());
            } catch (Exception e) {
            }
            if (sourceImage == null || sourceImage.equals("")) {
                return;
            }
            jm.setTilewidth(clipx);
            jm.setTileheight(clipy);
            if (clipDir == null || clipDir.equals("")) {
                jm.createMosaic(sourceImage);
            } else {
                boolean ignoreDup = new Boolean((String) jIgnoreDup.getSelectedItem()).booleanValue();
                boolean subSample = new Boolean((String) jSubSample.getSelectedItem()).booleanValue();
                if (colorPixel != null && new Boolean((String) jIgnoreColor.getSelectedItem()).booleanValue()) {
                    jm.createMosaic2(clipDir, sourceImage, colorPixel, ignoreDup, newClip, subSample);
                } else {
                    jm.createMosaic2(clipDir, sourceImage, ignoreDup, newClip, subSample);
                }
            }
            newClip = false;
            BufferedImage bi = jm.getMainImage();
            int scaledWidth = 0;
            int scaledHeight = 0;
            double proportion = 1;
            Image scaledImage = JpegImageUtility.resizeImage(bi, jpegFileImagePanel.getWidth(), jpegFileImagePanel.getHeight());
            if (scaledImage == null) {
                jpegFileImagePanel.setImage(bi);
            } else {
                jpegFileImagePanel.setImage(scaledImage);
            }
        } catch (Exception e) {
            System.err.println("error in running mosaic " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * select directory of source images
     *  
     */
    private void findClipDir() {
        FileChooser dc = new FileChooser(new javax.swing.JFrame(), true, System.getProperty("user.dir") + File.separator + "*");
        dc.setVisible(true);
        clipDir = dc.getFilePath();
        newClip = true;
    }

    /**
     * select the name of the save image
     *  
     */
    private void saveName() {
        FileChooser dc = new FileChooser(new javax.swing.JFrame(), true, "save", "jpg", "JPEG files", System.getProperty("user.dir") + File.separator + "*");
        dc.setVisible(true);
        outputImage = dc.getFilePath();
    }

    /**
     * save the image
     *  
     */
    private void saveImage() {
        saveName();
        if (outputImage == null || outputImage.equals("")) {
            return;
        }
        try {
            if (jm.getMainImage() != null) {
                if (!outputImage.endsWith(".jpg") && !outputImage.endsWith(".jpeg")) {
                    outputImage = outputImage + ".jpg";
                }
                JpegImageUtility.saveImage(outputImage, jm.getMainImage());
            }
        } catch (Exception e) {
            System.err.println("save error " + e.getMessage());
        }
    }

    /**
     * resize the buffered image to fit the frame
     * 
     * @param bi
     * @return
     */
    private Image resizeImage(BufferedImage bi) {
        Image scaledImage;
        if ((bi.getWidth() <= bi.getHeight())) {
            scaledImage = bi.getScaledInstance(-1, jpegFileImagePanel.getHeight(), BufferedImage.SCALE_SMOOTH);
        } else {
            scaledImage = bi.getScaledInstance(jpegFileImagePanel.getWidth(), -1, BufferedImage.SCALE_SMOOTH);
        }
        return scaledImage;
    }

    /**
     * down load images from flickr
     *  
     */
    private final void runFlickr() {
        if (flickrPanel == null) {
            flickrPanel = new FlickrPanel();
        }
        flickrPanel.setClipDir(clipDir);
        flickrPanel.setVisible(true);
    }

    /**
     * select the name of the source image
     *  
     */
    private void sourceName() {
        FileChooser dc = new FileChooser(new javax.swing.JFrame(), true, "open", "jpg", "JPEG files", System.getProperty("user.dir"));
        dc.setVisible(true);
        sourceImage = dc.getFilePath();
        try {
            sourceBufferedImage = JpegImageUtility.loadImage(sourceImage);
            int scaledWidth = 0;
            int scaledHeight = 0;
            double proportion = 1;
            Image scaledImage = JpegImageUtility.resizeImage(sourceBufferedImage, jpegFileImagePanel.getWidth(), jpegFileImagePanel.getHeight());
            if (scaledImage == null) {
                jpegFileImagePanel.setImage(sourceBufferedImage);
            } else {
                jpegFileImagePanel.setImage(scaledImage);
            }
        } catch (Exception e) {
        }
    }

    private void getPixel(int x, int y) {
        colorPixel = jpegFileImagePanel.getColorPixel(x, y);
        if (colorPixel != null) {
            jIgnoreColorValue.setBackground(new Color(colorPixel.getR(), colorPixel.getG(), colorPixel.getB()));
        }
    }

    /**
     * Exit the Application
     *  
     */
    private void exitForm(java.awt.event.WindowEvent evt) {
        System.exit(0);
    }

    /**
     * main entry point
     * 
     * @param args
     */
    public static void main(String[] args) {
        MosaicApp window = new MosaicApp();
        window.setVisible(true);
    }

    private JavaMosaic jm = new JavaMosaic();

    private FlickrPanel flickrPanel = new FlickrPanel();

    private javax.swing.JLabel jClipXLabel;

    private javax.swing.JLabel jClipYLabel;

    private javax.swing.JLabel jTolLabel;

    private javax.swing.JTextField jClipX;

    private javax.swing.JTextField jClipY;

    private javax.swing.JTextField jTol;

    private javax.swing.JLabel jIgnoreDupLabel;

    private javax.swing.JComboBox jIgnoreDup;

    private javax.swing.JLabel jSubSampleLabel;

    private javax.swing.JComboBox jSubSample;

    private khall.ui.ImagePanel jpegFileImagePanel;

    private javax.swing.JButton jClipDirButton;

    private javax.swing.JButton jSourceImageButton;

    private javax.swing.JButton jRunMosaic;

    private javax.swing.JButton jSaveImage;

    private javax.swing.JButton jGetFlickr;

    private javax.swing.JScrollPane jScrollPane;

    private String clipDir;

    private String sourceImage;

    private String outputImage;

    private int clipx = 100;

    private int clipy = 100;

    private int tolerance = 30;

    private BufferedImage sourceBufferedImage = null;

    private javax.swing.JLabel jIgnoreColorLabel;

    private javax.swing.JComboBox jIgnoreColor;

    private javax.swing.JTextField jIgnoreColorValue;

    private ColorPixel colorPixel = new ColorPixel();

    private boolean newClip = true;
}
