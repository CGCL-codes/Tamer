package gui.test;

import gui.dutsForms.RoomForm;
import gui.xmlClasses.IOParam;
import gui.xmlClasses.Text;
import java.awt.Color;
import javax.swing.JFrame;
import domain.core.ResourceFacade;

/**
 * @author Mathieu Vibert for FARS Design
 * @version 1.0
 */
public class RoomFormTest {

    /**
	 * The main function used to test the RoomForm
	 * 
	 * @param args The arguments of the main
	 */
    public static void main(String[] args) {
        JFrame testFrame;
        Text labels;
        RoomForm form;
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            labels = (Text) IOParam.deserialisationXML("xml/Text.xml");
            testFrame = new JFrame();
            testFrame.setTitle("Test of RoomForm");
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            form = new RoomForm(labels, ResourceFacade.getInstance());
            form.setBackGroundColor(new Color(254, 251, 231));
            testFrame.getContentPane().add(form);
            testFrame.pack();
            testFrame.setLocationRelativeTo(null);
            testFrame.setVisible(true);
        } catch (Exception ex) {
            System.err.println("Failure while reading the labels configuration file");
        }
    }
}
