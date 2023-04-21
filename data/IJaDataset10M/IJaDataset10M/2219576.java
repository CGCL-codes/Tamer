package com.matthewtavares.jipt.util;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Various methods for creating and displaying images
 * 
 * @author Matt Tavares
 * 
 */
public class ImageCreator {

    /**
	 * Hide the constructor
	 */
    private ImageCreator() {
    }

    ;

    /**
	 * Creates an Image object from a file system image
	 * 
	 * @param filePath
	 *            The path of the file to turn into an Image
	 * 
	 * @return An Image object of the file passed in
	 */
    public static Image createImage(String filePath) throws FileNotFoundException {
        Image image = Toolkit.getDefaultToolkit().getImage(filePath);
        MediaTracker tracker = new MediaTracker(new Container());
        tracker.addImage(image, 1);
        try {
            tracker.waitForID(1, 10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        if ((tracker.statusAll(false) & MediaTracker.ERRORED & MediaTracker.ABORTED) != 0) {
            System.out.println("Load errored or aborted");
            throw new FileNotFoundException("Could not find: " + filePath);
        }
        return image;
    }

    /**
	 * Displays an image in a new window
	 * 
	 * @param image The image to display
	 * @param frameTitle The title for the new window
	 * @param xPos The x position on the screen to place the window
	 * @param yPos The y position on the screen to place the window
	 */
    public static void displayImage(Image image, String frameTitle, int xPos, int yPos) {
        JFrame imageFrame = new JFrame(frameTitle);
        imageFrame.setMinimumSize(new Dimension(450, 100));
        JPanel imagePanel = new JPanel();
        imagePanel.add(new JLabel(new ImageIcon(image)));
        imageFrame.add(imagePanel);
        imageFrame.setLocation(yPos, xPos);
        imageFrame.pack();
        imageFrame.setVisible(true);
    }
}
