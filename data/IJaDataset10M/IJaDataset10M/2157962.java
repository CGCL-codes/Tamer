package fr.ign.cogit.geoxygene.util.loader.gui;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.log4j.Logger;

public class GUIShapefileChoice extends JFrame {

    private static final long serialVersionUID = 1L;

    static Logger logger = Logger.getLogger(GUIShapefileChoice.class.getName());

    /**
   * Dernier dossier ouvert
   */
    private String directoryPath;

    private boolean multiSelectionEnabled = true;

    /**
   * Constructeurs de sélecteur de shapefiles
   * @param MultiSelectionEnabled
   */
    public GUIShapefileChoice(boolean MultiSelectionEnabled) {
        this.multiSelectionEnabled = MultiSelectionEnabled;
        this.directoryPath = null;
    }

    public GUIShapefileChoice(boolean MultiSelectionEnabled, String latestFolder) {
        this.multiSelectionEnabled = MultiSelectionEnabled;
        this.directoryPath = latestFolder;
    }

    /**
   * Ouvre une fenêtre de sélection et renvoie un tableau contenant les fichiers
   * sélectionnés. Ne permet que la sélection de fichiers.
   * @return un tableau contenant les fichiers sélectionnés. null si rien n'a
   *         été sélectionné
   */
    public File[] getSelectedFiles() {
        return this.getSelectedFilesOrDirectories(true);
    }

    /**
   * Ouvre une fenêtre de sélection et renvoie un tableau contenant les
   * répertoires sélectionnés. Ne permet que la sélection de répertoires.
   * @return un tableau contenant les répertoires sélectionnés. null si rien n'a
   *         été sélectionné
   */
    public File[] getSelectedDirectories() {
        return this.getSelectedFilesOrDirectories(false);
    }

    /**
   * Ouvre une fenêtre de sélection et renvoie un tableau contenant les fichiers
   * ou répertoires sélectionnés.
   * @param selectFiles si vrai, ne permet que la sélection de fichiers, si faux
   *          que la sélection de répertoires.
   * @return un tableau contenant les fichiers ou répertoires sélectionnés. null
   *         si rien n'a été sélectionné
   */
    public File[] getSelectedFilesOrDirectories(boolean selectFiles) {
        JFileChooser chooser = new JFileChooser(this.directoryPath);
        if (selectFiles) {
            FileNameExtensionFilter filter = new FileNameExtensionFilter("ESRI Shapefiles", "shp");
            chooser.setFileFilter(filter);
        } else {
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
        chooser.setMultiSelectionEnabled(this.multiSelectionEnabled);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles();
            if (GUIShapefileChoice.logger.isTraceEnabled()) {
                for (File file : files) {
                    GUIShapefileChoice.logger.trace("You chose this " + (selectFiles ? "file :" : " directory ") + file.getName());
                }
            }
            return files;
        }
        return null;
    }
}
