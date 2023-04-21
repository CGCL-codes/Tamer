package com.matthewtavares.jipt.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.io.FileNotFoundException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.matthewtavares.jipt.util.ImageCreator;

/**
 * 
 * @author Matt Tavares mattTavares@gmail.com
 * 
 * A simple GUI for displaying, opening, and operating on images.
 *
 */
public class ImageProcessingGui extends JFrame {

    /**
	 * Random generated SVU
	 */
    private static final long serialVersionUID = -3519446749458084166L;

    /** The path of the default image to display in the GUI */
    private static final String DEFAULT_PICTURE_PATH = "images/jellyFish.jpg";

    /** The display name on the GUI */
    private static final String APPLICATION_NAME = "Image Processing Toolkit";

    /** The panel which displays the image */
    private JPanel imagePanel = new JPanel();

    /** The displayed image */
    private Image displayedImage = null;

    /**
	 * Constructor, which sets up the Image Processing viewer GUI
	 */
    public ImageProcessingGui() {
        setTitle(APPLICATION_NAME);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(300, 100);
        setMinimumSize(new Dimension(350, 100));
        Image image = null;
        try {
            image = ImageCreator.createImage(DEFAULT_PICTURE_PATH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (image != null) {
            imagePanel.add(new JLabel(new ImageIcon(image)));
            displayedImage = image;
        }
        setJMenuBar(new ImageProcessingMenuBar(this));
        add(imagePanel);
        pack();
    }

    /**
	 * Returns a reference to the image panel
	 * 
	 * @return the image JPanel for this GUI
	 */
    public JPanel getImagePanel() {
        return imagePanel;
    }

    /**
	 * Returns the Image object of the currently displayed image
	 * 
	 * @return
	 */
    public Image getDisplayedImage() {
        return displayedImage;
    }

    public void setDisplayedImage(Image displayedImage) {
        if (displayedImage != null) {
            this.displayedImage = displayedImage;
            imagePanel.removeAll();
            imagePanel.add(new JLabel(new ImageIcon(displayedImage)));
            pack();
        }
    }
}
