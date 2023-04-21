package net.sourceforge.dsnk.gui.action;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import net.sourceforge.dsnk.gui.GradientPanel;
import net.sourceforge.dsnk.gui.UOGumpViewer;
import net.sourceforge.dsnk.model.AbstractGump;

/**
 * Implements the export gump action.
 * 
 * @author Jochen Kraushaar
 */
public class ExportGumpAction extends BasicAction {

    private static final long serialVersionUID = 4107228687979277355L;

    private final Logger logger;

    /**
	 * Constructor
	 * 
	 * @param text
	 *            displayed action text
	 * @param icon
	 *            displayed icon
	 * @param desc
	 *            tooltip text
	 * @param mnemonic
	 *            keyboard shortcut
	 * @param dataProvider
	 *            the provider of gump data
	 */
    public ExportGumpAction(String text, ImageIcon icon, String desc, Integer mnemonic, IGumpDataProvider dataProvider) {
        super(text, icon, desc, mnemonic, dataProvider);
        logger = Logger.getLogger(UOGumpViewer.LOGGER_NAME);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".png") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return ("*.png");
            }
        });
        Container cont = parentWindow.getContentPane();
        if (cont instanceof GradientPanel) {
            GradientPanel contentPane = (GradientPanel) cont;
            contentPane.setBlurred(true);
            contentPane.repaint();
        }
        int returnVal = fileChooser.showSaveDialog(parentWindow);
        if (cont instanceof GradientPanel) {
            GradientPanel contentPane = (GradientPanel) cont;
            contentPane.setBlurred(false);
            contentPane.repaint();
        }
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (!path.endsWith(".png")) {
                path = path.concat(".png");
            }
            File outFile = new File(path);
            AbstractGump gump = previewCanvas.getGumpAt(0);
            if (gump != null) {
                BufferedImage img = (BufferedImage) gump.getImage();
                try {
                    ImageIO.write(img, "png", outFile);
                } catch (IOException e1) {
                    String msg = UOGumpViewer.getResourceBundle().getString("UOGumpViewer.Message.CouldNotSaveGump");
                    String title = UOGumpViewer.getResourceBundle().getString("UOGumpViewer.MessageTitle.CouldNotSaveGump");
                    logger.log(Level.SEVERE, msg, e1);
                    JOptionPane.showMessageDialog(parentWindow, msg, title, JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
