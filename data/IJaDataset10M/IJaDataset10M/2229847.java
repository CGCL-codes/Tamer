package hoplugins.conv;

import org.w3c.dom.Document;
import java.io.File;
import javax.swing.JOptionPane;

/**
 * DOCUMENT ME!
 *
 * @author Thorsten Dietz
 */
public class HTCoach extends HrfMaker {

    /**
     * Creates a new HTCoach object.
     */
    protected HTCoach() {
        type = "HTCF";
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param selectedFiles TODO Missing Method Parameter Documentation
     * @param targetDir TODO Missing Method Parameter Documentation
     */
    protected void start(File[] selectedFiles, File targetDir) {
        try {
            String[] filter = null;
            File[][] xmls = null;
            filter = getName(selectedFiles);
            xmls = getFiles(filter, selectedFiles);
            for (int i = 0; i < xmls.length; i++) {
                if ((xmls[i][0] == null) || (xmls[i][1] == null) || (xmls[i][2] == null) || (xmls[i][3] == null) || (xmls[i][4] == null) || (xmls[i][5] == null) || (xmls[i][6] == null)) {
                    handleException(null, RSC.PROP_FILE_NOT_FOUND + " :" + filter + RSC.HTFOREVER_EXTENSION[i]);
                    return;
                }
            }
            if (targetDir != null) {
                Document doc = null;
                for (int i = 0; i < xmls.length; i++) {
                    clearArrays();
                    addBasics();
                    doc = getDocument(xmls[i][1]);
                    analyzeClub(doc.getDocumentElement().getChildNodes());
                    doc = getDocument(xmls[i][0]);
                    analyzeArena(doc.getDocumentElement().getChildNodes());
                    doc = getDocument(xmls[i][5]);
                    analyzeTraining(doc.getDocumentElement().getChildNodes());
                    doc = getDocument(xmls[i][2]);
                    analyzeEconomy(doc.getDocumentElement().getChildNodes());
                    doc = getDocument(xmls[i][4]);
                    analyzeTeamDetails(doc.getDocumentElement().getChildNodes());
                    doc = getDocument(xmls[i][3]);
                    initPlayersArray(doc.getDocumentElement().getChildNodes());
                    doc = getDocument(xmls[i][6]);
                    analyzeWorldDetail(doc.getDocumentElement().getChildNodes());
                    writeHrf(filter[i], targetDir);
                }
            }
            JOptionPane.showMessageDialog(null, RSC.getProperty("finished"));
        } catch (Exception e1) {
            handleException(e1, RSC.PROP_DEFAULT_ERROR_MESSAGE);
        }
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param f TODO Missing Method Parameter Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    private static String[] getName(File[] f) {
        String[] names = new String[f.length];
        int index = f[0].getName().indexOf(".");
        for (int i = 0; i < names.length; i++) {
            names[i] = f[i].getName().substring(f[i].getName().indexOf("_") + 1, index);
        }
        return names;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param filter TODO Missing Method Parameter Documentation
     * @param selectedFiles TODO Missing Method Parameter Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    private File[][] getFiles(String[] filter, File[] selectedFiles) {
        File[][] xmls = new File[selectedFiles.length][7];
        File dir = selectedFiles[0].getParentFile().getParentFile();
        for (int i = 0; i < selectedFiles.length; i++) {
            xmls[i][0] = new File(dir.getPath() + File.separator + RSC.HTCOACH_EXTENSION[0] + File.separator + RSC.HTCOACH_EXTENSION[0] + "_" + filter[i] + ".xml");
            xmls[i][1] = new File(dir.getPath() + File.separator + RSC.HTCOACH_EXTENSION[1] + File.separator + RSC.HTCOACH_EXTENSION[1] + "_" + filter[i] + ".xml");
            xmls[i][2] = new File(dir.getPath() + File.separator + RSC.HTCOACH_EXTENSION[2] + File.separator + RSC.HTCOACH_EXTENSION[2] + "_" + filter[i] + ".xml");
            xmls[i][3] = new File(dir.getPath() + File.separator + RSC.HTCOACH_EXTENSION[3] + File.separator + RSC.HTCOACH_EXTENSION[3] + "_" + filter[i] + ".xml");
            xmls[i][4] = new File(dir.getPath() + File.separator + RSC.HTCOACH_EXTENSION[4] + File.separator + RSC.HTCOACH_EXTENSION[4] + "_" + filter[i] + ".xml");
            xmls[i][5] = new File(dir.getPath() + File.separator + RSC.HTCOACH_EXTENSION[5] + File.separator + RSC.HTCOACH_EXTENSION[5] + "_" + filter[i] + ".xml");
            xmls[i][6] = new File(dir.getPath() + File.separator + RSC.HTCOACH_EXTENSION[6] + File.separator + RSC.HTCOACH_EXTENSION[6] + "_" + filter[i] + ".xml");
        }
        return xmls;
    }
}
