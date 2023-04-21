package fr.itris.glips.extension.jwidget.trends.edition.display;

import org.w3c.dom.*;
import fr.itris.glips.library.widgets.DashChooserWidget;
import fr.itris.glips.library.widgets.TimeChooserWidget;
import fr.itris.glips.rtdaeditor.jwidget.*;
import fr.itris.glips.svgeditor.widgets.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * the class used to configure the cursor on the trend box
 * @author ITRIS, Jordi SUC
 */
public class VerticalGridLinesDivisionsPanel extends JPanel {

    /**
	 * the jwidget edition
	 */
    private JWidgetEdition jwidgetEdition;

    /**
	 * the currently edited element
	 */
    private Element currentElement;

    /**
	 * the automatic period radio button
	 */
    private JRadioButton automaticPeriodRadioButton;

    /**
	 * the manual period radio button
	 */
    private JRadioButton manualPeriodRadioButton;

    /**
	 * the listener to the radio buttons
	 */
    private ActionListener periodRadioButtonsListener;

    /**
	 * the time chooser widget
	 */
    private TimeChooserWidget durationTimeChooser;

    /**
	 * the listener to the actions on the duration time chooser
	 */
    private ActionListener durationTimeChooserListener;

    /**
	 * the color chooser
	 */
    private ColorChooserWidget colorChooser;

    /**
	 * the listener to the color chooser
	 */
    protected ActionListener colorChooserListener;

    /**
	 * the line style chooser
	 */
    private DashChooserWidget lineStyleChooser;

    /**
	 * the listener to the actions on the line style chooser
	 */
    private ActionListener lineStyleChooserListener;

    /**
	 * the jlabels
	 */
    private JLabel durationTimeChooserLbl, backgroundColorLbl, lineStyleChooserLbl;

    /**
	 * the constructor of the class
	 * @param jwidgetEdition the jwidget edition
	 */
    public VerticalGridLinesDivisionsPanel(JWidgetEdition jwidgetEdition) {
        this.jwidgetEdition = jwidgetEdition;
        build();
    }

    /**
	 * initializes the panel
	 * @param element an element
	 */
    public void initialize(Element element) {
        this.currentElement = element;
        automaticPeriodRadioButton.removeActionListener(periodRadioButtonsListener);
        manualPeriodRadioButton.removeActionListener(periodRadioButtonsListener);
        boolean handleAutomatically = true;
        try {
            handleAutomatically = Boolean.parseBoolean(jwidgetEdition.getProperty(element, jwidgetEdition.getPropertiesList().get(13)));
        } catch (Exception ex) {
        }
        if (handleAutomatically) {
            automaticPeriodRadioButton.setSelected(true);
        } else {
            manualPeriodRadioButton.setSelected(true);
        }
        automaticPeriodRadioButton.addActionListener(periodRadioButtonsListener);
        manualPeriodRadioButton.addActionListener(periodRadioButtonsListener);
        durationTimeChooser.removeListener(durationTimeChooserListener);
        durationTimeChooser.init(jwidgetEdition.getProperty(element, jwidgetEdition.getPropertiesList().get(14)));
        durationTimeChooser.addListener(durationTimeChooserListener);
        colorChooser.removeListener(colorChooserListener);
        colorChooser.init(jwidgetEdition.getProperty(element, jwidgetEdition.getPropertiesList().get(15)));
        colorChooser.addListener(colorChooserListener);
        lineStyleChooser.removeListener(lineStyleChooserListener);
        lineStyleChooser.init(jwidgetEdition.getProperty(element, jwidgetEdition.getPropertiesList().get(16)));
        lineStyleChooser.addListener(lineStyleChooserListener);
        handleButtonsState();
    }

    /**
	 * building the panel
	 */
    protected void build() {
        String gridVerticalLinesDivisionsLabel = "", gridVerticalLinesDivisionsAutomaticPeriodLabel = "", gridVerticalLinesDivisionsDurationLabel = "", gridVerticalLinesDivisionsColorLabel = "", gridVerticalLinesDivisionsStyleLabel = "";
        try {
            gridVerticalLinesDivisionsLabel = jwidgetEdition.getBundle().getString("gridVerticalLinesDivisions");
            gridVerticalLinesDivisionsAutomaticPeriodLabel = jwidgetEdition.getBundle().getString("gridVerticalLinesDivisionsAutomaticPeriod");
            gridVerticalLinesDivisionsDurationLabel = jwidgetEdition.getBundle().getString("gridVerticalLinesDivisionsDuration");
            gridVerticalLinesDivisionsColorLabel = jwidgetEdition.getBundle().getString("gridVerticalLinesDivisionsColor");
            gridVerticalLinesDivisionsStyleLabel = jwidgetEdition.getBundle().getString("gridVerticalLinesDivisionsDash");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        durationTimeChooserLbl = new JLabel(gridVerticalLinesDivisionsDurationLabel + " : ");
        durationTimeChooserLbl.setHorizontalAlignment(JLabel.RIGHT);
        automaticPeriodRadioButton = new JRadioButton(gridVerticalLinesDivisionsAutomaticPeriodLabel);
        manualPeriodRadioButton = new JRadioButton("");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(automaticPeriodRadioButton);
        buttonGroup.add(manualPeriodRadioButton);
        periodRadioButtonsListener = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                jwidgetEdition.setProperty(currentElement, jwidgetEdition.getPropertiesList().get(13), Boolean.toString(automaticPeriodRadioButton.isSelected()), false);
                handleButtonsState();
            }
        };
        durationTimeChooser = new TimeChooserWidget();
        durationTimeChooserListener = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                jwidgetEdition.setProperty(currentElement, jwidgetEdition.getPropertiesList().get(14), durationTimeChooser.getValue(), false);
            }
        };
        backgroundColorLbl = new JLabel(gridVerticalLinesDivisionsColorLabel + " : ");
        backgroundColorLbl.setHorizontalAlignment(JLabel.RIGHT);
        colorChooser = new ColorChooserWidget();
        colorChooserListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jwidgetEdition.setProperty(currentElement, jwidgetEdition.getPropertiesList().get(15), colorChooser.getValue(), true);
            }
        };
        lineStyleChooserLbl = new JLabel(gridVerticalLinesDivisionsStyleLabel + " : ");
        lineStyleChooserLbl.setHorizontalAlignment(JLabel.RIGHT);
        lineStyleChooser = new DashChooserWidget();
        lineStyleChooserListener = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                jwidgetEdition.setProperty(currentElement, jwidgetEdition.getPropertiesList().get(16), lineStyleChooser.getValue(), false);
            }
        };
        TitledBorder border = new TitledBorder(gridVerticalLinesDivisionsLabel);
        setBorder(border);
        GridBagLayout gridBag1 = new GridBagLayout();
        setLayout(gridBag1);
        GridBagConstraints c1 = new GridBagConstraints();
        c1.insets = new Insets(1, 1, 1, 1);
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridx = 0;
        c1.gridy = 0;
        c1.gridwidth = 1;
        c1.anchor = GridBagConstraints.EAST;
        gridBag1.setConstraints(durationTimeChooserLbl, c1);
        add(durationTimeChooserLbl);
        c1.gridx = 1;
        c1.gridy = 0;
        c1.gridwidth = 2;
        c1.anchor = GridBagConstraints.WEST;
        gridBag1.setConstraints(automaticPeriodRadioButton, c1);
        add(automaticPeriodRadioButton);
        c1.gridx = 1;
        c1.gridy = 1;
        c1.gridwidth = 1;
        c1.anchor = GridBagConstraints.WEST;
        gridBag1.setConstraints(manualPeriodRadioButton, c1);
        add(manualPeriodRadioButton);
        c1.gridx = 2;
        c1.gridy = 1;
        c1.gridwidth = 1;
        c1.anchor = GridBagConstraints.WEST;
        gridBag1.setConstraints(durationTimeChooser, c1);
        add(durationTimeChooser);
        c1.gridx = 0;
        c1.gridy = 2;
        c1.gridwidth = 1;
        c1.anchor = GridBagConstraints.EAST;
        gridBag1.setConstraints(backgroundColorLbl, c1);
        add(backgroundColorLbl);
        c1.gridx = 1;
        c1.gridy = 2;
        c1.gridwidth = 2;
        c1.anchor = GridBagConstraints.EAST;
        gridBag1.setConstraints(colorChooser, c1);
        add(colorChooser);
        c1.gridx = 0;
        c1.gridy = 3;
        c1.gridwidth = 1;
        c1.anchor = GridBagConstraints.EAST;
        gridBag1.setConstraints(lineStyleChooserLbl, c1);
        add(lineStyleChooserLbl);
        c1.gridx = 1;
        c1.gridy = 3;
        c1.gridwidth = 2;
        c1.anchor = GridBagConstraints.EAST;
        gridBag1.setConstraints(lineStyleChooser, c1);
        add(lineStyleChooser);
    }

    @Override
    public void setEnabled(boolean enable) {
        super.setEnabled(enable);
        automaticPeriodRadioButton.setEnabled(enable);
        manualPeriodRadioButton.setEnabled(enable);
        colorChooser.setEnabled(enable);
        lineStyleChooser.setEnabled(enable);
        durationTimeChooserLbl.setEnabled(enable);
        backgroundColorLbl.setEnabled(enable);
        lineStyleChooserLbl.setEnabled(enable);
        if (enable) {
            handleButtonsState();
        } else {
            durationTimeChooser.setEnabled(false);
        }
    }

    /**
	 * modifies the state of the manual period widgets according to 
	 * the state of the radio buttons 
	 */
    protected void handleButtonsState() {
        durationTimeChooser.setEnabled(!automaticPeriodRadioButton.isSelected());
    }
}
