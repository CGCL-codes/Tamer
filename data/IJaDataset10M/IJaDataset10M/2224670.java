package com.lowagie.rups;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import com.lowagie.rups.controller.RupsController;

/**
 * iText RUPS is a tool that combines SUN's PDF Renderer (to view PDF
 * documents), iText's PdfReader (to inspect the internal structure of a
 * PDF file), and iText's PdfStamper to manipulate a PDF file.
 */
public class Rups {

    /** The version String; this String may only be changed by Bruno Lowagie. */
    public static final String VERSION = "RUPS 0.0.1 (by lowagie.com)";

    /**
	 * Main method of this PdfFrame class.
	 * @param	args	no arguments needed
	 */
    public static void main(String[] args) {
        startApplication();
    }

    /**
     * Initializes the main components of the Rups application.
     */
    public static void startApplication() {
        JFrame frame = new JFrame();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) (screen.getWidth() * .90), (int) (screen.getHeight() * .90));
        frame.setLocation((int) (screen.getWidth() * .05), (int) (screen.getHeight() * .05));
        frame.setResizable(true);
        frame.setTitle("iText RUPS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        RupsController controller = new RupsController(frame.getSize());
        frame.setJMenuBar(controller.getMenuBar());
        frame.getContentPane().add(controller.getMasterComponent(), java.awt.BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /** Serial Version UID. */
    private static final long serialVersionUID = 4386633640535735848L;
}
