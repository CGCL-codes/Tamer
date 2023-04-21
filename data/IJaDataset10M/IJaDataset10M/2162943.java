package gui.test;

import gui.dutsView.HumanRoomPanel;
import gui.xmlClasses.IOParam;
import gui.xmlClasses.Text;
import javax.swing.JFrame;
import domain.core.ResourceFacade;

/**
 * @author Mathieu Vibert for FARS Design
 * @version 1.0
 */
public class HumanRoomPanelTest {

    /**
	 * The main function used to test the HumanRoomPanel
	 * 
	 * @param args The arguments of the main
	 */
    public static void main(String[] args) {
        JFrame testFrame;
        Text labels;
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            labels = (Text) IOParam.deserialisationXML("xml/Text.xml");
            testFrame = new JFrame();
            testFrame.setTitle("Test of HumanRoomPanel");
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.getContentPane().add(new HumanRoomPanel(labels, ResourceFacade.getInstance()));
            testFrame.pack();
            testFrame.setLocationRelativeTo(null);
            testFrame.setVisible(true);
        } catch (Exception ex) {
            System.err.println("Failure while reading the labels configuration file");
            ex.printStackTrace();
        }
    }
}
