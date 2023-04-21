package org.hironico.dbtool2.visualdb;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;
import org.hironico.gui.image.TransferableImage;

/**
 * Permet d'exporter l'image de la scene VusalDB dans le clipboard afin de
 * pouvoir le récupérer dans un document Word par exemple.
 * @author hironico
 * @since 2.1.0
 */
public class ExportSceneImageToClipboardAction extends ExportSceneImageAction {

    public ExportSceneImageToClipboardAction(DBGraphScene graphScene) {
        super(graphScene);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        BufferedImage img = getSceneImage();
        TransferableImage transferable = new TransferableImage(img);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(transferable, null);
        JOptionPane.showMessageDialog(graphScene.getView(), "Image copied into the clipboard.", "Yeah...", JOptionPane.INFORMATION_MESSAGE);
    }
}
