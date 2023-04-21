package gui.test;

import gui.dutsView.NewAcaYearWizard;
import gui.xmlClasses.IOParam;
import gui.xmlClasses.Text;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import domain.core.ResourceFacade;
import domain.core.WizardFacade;
import domain.exception.DataGetException;

public class WizardTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Text texte = null;
        try {
            texte = (Text) IOParam.deserialisationXML("xml/Text.xml");
        } catch (Exception ex) {
        }
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        NewAcaYearWizard testFrame;
        try {
            testFrame = new NewAcaYearWizard(new WizardFacade(), ResourceFacade.getInstance(), texte);
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.setLocationRelativeTo(null);
            testFrame.setVisible(true);
        } catch (DataGetException e) {
            JOptionPane.showMessageDialog(null, "Echec de la r�cup�ration de la nouvelle ann�e universitaire", "Erreur de r�cup�ration de donn�es", JOptionPane.WARNING_MESSAGE, null);
        }
    }
}
