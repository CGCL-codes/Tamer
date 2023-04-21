package fr.itris.glips.extension.jwidget.trends.edition.general;

import org.w3c.dom.*;
import fr.itris.glips.rtdaeditor.jwidget.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * the class used to configure the the history
 * @author ITRIS, Jordi SUC
 */
public class InterpolationPanel extends JPanel {

    /**
	 * the jwidget edition
	 */
    private JWidgetEdition jwidgetEdition;

    /**
	 * the currently edited element
	 */
    private Element currentElement;

    /**
	 * the check box
	 */
    private JCheckBox checkBox;

    /**
	 * the action listener
	 */
    private ActionListener checkBoxActionListener;

    /**
	 * the constructor of the class
	 * @param jwidgetEdition the jwidget edition
	 */
    public InterpolationPanel(JWidgetEdition jwidgetEdition) {
        this.jwidgetEdition = jwidgetEdition;
        build();
    }

    /**
	 * initializes the panel
	 * @param element an element
	 */
    public void initialize(Element element) {
        this.currentElement = element;
        checkBox.removeActionListener(checkBoxActionListener);
        try {
            checkBox.setSelected(Boolean.parseBoolean(jwidgetEdition.getProperty(element, jwidgetEdition.getPropertiesList().get(21))));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        checkBox.addActionListener(checkBoxActionListener);
    }

    /**
	 * building the panel
	 */
    protected void build() {
        String interpolationLabel = "", interpolationWithPeriodLabel = "";
        try {
            interpolationLabel = jwidgetEdition.getBundle().getString("interpolation");
            interpolationWithPeriodLabel = jwidgetEdition.getBundle().getString("interpolationWithPeriod");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        checkBox = new JCheckBox(interpolationWithPeriodLabel);
        checkBox.setHorizontalAlignment(SwingConstants.LEFT);
        checkBoxActionListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jwidgetEdition.setProperty(currentElement, jwidgetEdition.getPropertiesList().get(21), Boolean.toString(checkBox.isSelected()), true);
            }
        };
        TitledBorder border = new TitledBorder(interpolationLabel);
        setBorder(border);
        GridBagLayout gridBag1 = new GridBagLayout();
        setLayout(gridBag1);
        GridBagConstraints c1 = new GridBagConstraints();
        c1.insets = new Insets(1, 1, 1, 1);
        c1.fill = GridBagConstraints.NONE;
        c1.gridwidth = 1;
        c1.anchor = GridBagConstraints.WEST;
        c1.weightx = 50;
        gridBag1.setConstraints(checkBox, c1);
        add(checkBox);
    }
}
