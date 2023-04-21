package hoplugins.conv;

import java.io.File;
import javax.swing.JOptionPane;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Thorsten Dietz
 *
 */
public class HAM extends HrfMaker {

    protected HAM() {
        type = "HAM";
    }

    protected void start(File[] xmls, File targetDir) {
        Document doc = null;
        try {
            for (int i = 0; i < xmls.length; i++) {
                clearArrays();
                addBasics();
                doc = getDocument(xmls[i]);
                analyze(doc.getDocumentElement().getChildNodes());
                writeHrf(xmls[i].getName().substring(3, 11), targetDir);
            }
            JOptionPane.showMessageDialog(null, RSC.getProperty("finished"));
        } catch (Exception e1) {
            handleException(e1, RSC.PROP_DEFAULT_ERROR_MESSAGE);
        }
    }

    private void analyze(NodeList nodelist) {
        for (int i = 0; i < nodelist.getLength(); i++) {
            if (nodelist.item(i) instanceof Element) {
                Element element = (Element) nodelist.item(i);
                if (element.getTagName().equals("TeamDetails")) {
                    analyzeTeamDetails(nodelist.item(i).getChildNodes());
                }
                if (element.getTagName().equals("Players")) {
                    initPlayersArray(nodelist.item(i).getChildNodes());
                }
                if (element.getTagName().equals("Economy")) {
                    analyzeEconomy(nodelist.item(i).getChildNodes());
                }
                if (element.getTagName().equals("Club")) {
                    analyzeClub(nodelist.item(i).getChildNodes());
                }
                if (element.getTagName().equals("Training")) {
                    analyzeTraining(nodelist.item(i).getChildNodes());
                }
                if (element.getTagName().equals("Arena")) {
                    analyzeArena(nodelist.item(i).getChildNodes());
                }
                if (element.getTagName().equals("WorldDetails")) {
                    analyzeWorldDetail(nodelist.item(i).getChildNodes());
                }
            }
        }
    }
}
