package org.nwolf.carwars.core;

import java.awt.Frame;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class BlockEditor extends Frame {

    private static final long serialVersionUID = 1L;

    private Image image;

    /**
	 * This is the default constructor
	 */
    public BlockEditor() {
        super();
        this.image = Toolkit.getDefaultToolkit().getImage("data/blocks/3/visible.png");
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(image, 0);
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Chargement des images : " + tracker.checkAll());
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setMinimumSize(new Dimension(100, 100));
        this.setSize(200, 200);
        this.setResizable(false);
        this.setTitle("CarWars Block Editor");
        this.add(new BlockViewer(this.image), BorderLayout.CENTER);
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        BlockEditor be = new BlockEditor();
        be.setVisible(true);
    }
}
