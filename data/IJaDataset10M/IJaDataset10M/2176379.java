package sharerap.gui.popups;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import net.miginfocom.swing.MigLayout;
import sharerap.com.data.PrintInfo;

/**
 * A dialog for selecting information used for configuring the printer for a print like filament type.
 */
public class PrintInfoPopup {

    private JButton printB, cancelB;

    private JComboBox filamentTypeCB;

    private JTextField rpmTF;

    private JTextField projectTF;

    private String projectName;

    private String[] filamentTypes;

    public Object componentArray[];

    /**
     * Creates a dialog for selecting the printer to print the project.
     * @param projectName the name of the project to print.
     **/
    public PrintInfoPopup(String projectName) {
        this.projectName = projectName;
        setup();
        projectTF.setText(projectName);
    }

    /**
     * Returns the print information.
     **/
    public PrintInfo getPrintInfo() {
        int rpm;
        try {
            rpm = Integer.parseInt(rpmTF.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Must be an integer", "Invalid RPM", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        PrintInfo pi = new PrintInfo(projectName, (String) filamentTypeCB.getSelectedItem(), rpm);
        return pi;
    }

    /**
     * Sets up the gui components.
     **/
    protected void setup() {
        filamentTypes = new String[7];
        filamentTypes[0] = "ABS (clear)";
        filamentTypes[1] = "ABS (colored)";
        filamentTypes[2] = "HDPE";
        filamentTypes[3] = "LDPE";
        filamentTypes[4] = "PLA";
        filamentTypes[5] = "PP";
        filamentTypes[6] = "uPVC";
        JLabel projectL = new JLabel("Filename");
        projectTF = new JTextField(10);
        JLabel filamentTypeL = new JLabel("Select Filament Type");
        filamentTypeCB = new JComboBox(filamentTypes);
        JLabel rpmL = new JLabel("Set Feed RPMs");
        rpmTF = new JTextField(5);
        printB = new JButton("Print");
        cancelB = new JButton("Cancel");
        componentArray = new Object[6];
        componentArray[0] = projectL;
        componentArray[1] = projectTF;
        componentArray[2] = filamentTypeL;
        componentArray[3] = filamentTypeCB;
        componentArray[4] = rpmL;
        componentArray[5] = rpmTF;
    }

    /**
     * Opens up the print info dialog and blocks until the user selects a choice.
     * @return the print information and null if the user selects cancel.
     **/
    public static PrintInfo getPrintInfo(String projectName) {
        PrintInfoPopup pip = new PrintInfoPopup(projectName);
        int res = JOptionPane.showConfirmDialog(null, pip.componentArray, "Print", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.CANCEL_OPTION || res == JOptionPane.CLOSED_OPTION) {
            return null;
        }
        return pip.getPrintInfo();
    }
}
