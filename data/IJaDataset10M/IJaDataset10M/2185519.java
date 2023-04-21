package fr.itris.glips.extension.jwidget.trends.edition.display;

import org.w3c.dom.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/**
 * the class used to configure the cursor on the trend box
 * @author ITRIS, Jordi SUC
 */
public class CursorPanel extends JPanel {

    /**
	 * the jwidget edition
	 */
    private JWidgetEdition jwidgetEdition;

    /**
	 * the currently edited element
	 */
    private Element currentElement;

    /**
	 * the display check box
	 */
    private JCheckBox displayHorodateCheckBox;

    /**
	 * the action listener
	 */
    private ActionListener checkBoxActionListener;

    /**
	 * the text field used for specifying the horodate format
	 */
    private JTextField horodateFormatTextField;

    /**
	 * the listener to the textField
	 */
    private CaretListener caretListener;

    /**
	 * the jlabels whose state should be handled
	 */
    private JLabel horodateFormatLbl;

    /**
	 * the constructor of the class
	 * @param jwidgetEdition the jwidget edition
	 */
    public CursorPanel(JWidgetEdition jwidgetEdition) {
        this.jwidgetEdition = jwidgetEdition;
        build();
    }

    /**
	 * initializes the panel
	 * @param element an element
	 */
    public void initialize(Element element) {
        this.currentElement = element;
        displayHorodateCheckBox.removeActionListener(checkBoxActionListener);
        try {
            displayHorodateCheckBox.setSelected(Boolean.parseBoolean(jwidgetEdition.getProperty(element, jwidgetEdition.getPropertiesList().get(4))));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        displayHorodateCheckBox.addActionListener(checkBoxActionListener);
        horodateFormatTextField.removeCaretListener(caretListener);
        horodateFormatTextField.setText(jwidgetEdition.getProperty(element, jwidgetEdition.getPropertiesList().get(5)));
        horodateFormatTextField.addCaretListener(caretListener);
        handleHorodateCheckBoxState();
    }

    /**
	 * building the panel
	 */
    protected void build() {
        String cursorLabel = "", displayCursorHorodatesLabel = "", cursorHorodatesFormatLabel = "";
        try {
            cursorLabel = jwidgetEdition.getBundle().getString("cursor");
            displayCursorHorodatesLabel = jwidgetEdition.getBundle().getString("displayCursorHorodates");
            cursorHorodatesFormatLabel = jwidgetEdition.getBundle().getString("cursorHorodatesFormat");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        displayHorodateCheckBox = new JCheckBox(displayCursorHorodatesLabel);
        checkBoxActionListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jwidgetEdition.setProperty(currentElement, jwidgetEdition.getPropertiesList().get(4), Boolean.toString(displayHorodateCheckBox.isSelected()), true);
                handleHorodateCheckBoxState();
            }
        };
        horodateFormatTextField = new JTextField();
        horodateFormatLbl = new JLabel(cursorHorodatesFormatLabel + " : ");
        horodateFormatLbl.setHorizontalAlignment(JLabel.RIGHT);
        caretListener = new CaretListener() {

            public void caretUpdate(CaretEvent evt) {
                jwidgetEdition.setProperty(currentElement, jwidgetEdition.getPropertiesList().get(5), horodateFormatTextField.getText(), true);
            }
        };
        TitledBorder border = new TitledBorder(cursorLabel);
        setBorder(border);
        GridBagLayout gridBag1 = new GridBagLayout();
        setLayout(gridBag1);
        GridBagConstraints c1 = new GridBagConstraints();
        c1.insets = new Insets(1, 1, 1, 1);
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridwidth = GridBagConstraints.REMAINDER;
        c1.anchor = GridBagConstraints.WEST;
        gridBag1.setConstraints(displayHorodateCheckBox, c1);
        add(displayHorodateCheckBox);
        c1.gridwidth = 1;
        c1.anchor = GridBagConstraints.EAST;
        gridBag1.setConstraints(horodateFormatLbl, c1);
        add(horodateFormatLbl);
        c1.gridwidth = GridBagConstraints.REMAINDER;
        c1.anchor = GridBagConstraints.WEST;
        c1.weightx = 2;
        gridBag1.setConstraints(horodateFormatTextField, c1);
        add(horodateFormatTextField);
    }

    /**
	 * handles the state of the horodate check box, ie, 
	 * enabling or disabling the format textfield according to its state
	 */
    protected void handleHorodateCheckBoxState() {
        horodateFormatLbl.setEnabled(displayHorodateCheckBox.isSelected());
        horodateFormatTextField.setEnabled(displayHorodateCheckBox.isSelected());
    }

    /**
	 * disposes the component
	 */
    public void dispose() {
        displayHorodateCheckBox.removeActionListener(checkBoxActionListener);
        horodateFormatTextField.removeCaretListener(caretListener);
    }
}
