package fr.soleil.bensikin.containers.sub.dialogs.options;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import fr.soleil.bensikin.options.sub.SaveOptions;
import fr.soleil.bensikin.tools.Messages;

/**
 * The save options tab of OptionsDialog, used to set the save on shudown/load
 * on startup cycle of the application.
 * 
 * @author CLAISSE
 */
public class OptionsSaveOptionsTab extends JPanel {

    private static final long serialVersionUID = -9212162249698224136L;

    private static OptionsSaveOptionsTab instance = null;

    private JRadioButton yesButton;

    private JRadioButton noButton;

    private JLabel label;

    private ButtonGroup buttonGroup;

    private JPanel myPanel;

    /**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @return The instance
	 */
    public static OptionsSaveOptionsTab getInstance() {
        if (instance == null) {
            instance = new OptionsSaveOptionsTab();
        }
        return instance;
    }

    /**
	 * Builds the tab.
	 */
    private OptionsSaveOptionsTab() {
        this.initComponents();
        this.initLayout();
        this.addComponents();
    }

    /**
	 * Inits the tab's layout.
	 */
    private void initLayout() {
        setLayout(new GridLayout());
    }

    /**
	 * Adds the initialized components to the tab.
	 */
    private void addComponents() {
        myPanel = new JPanel(new GridBagLayout());
        Insets gapInsets = new Insets(0, 0, 40, 0);
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints yesConstraints = new GridBagConstraints();
        yesConstraints.fill = GridBagConstraints.HORIZONTAL;
        yesConstraints.gridx = 0;
        yesConstraints.gridy = 0;
        yesConstraints.weightx = 1;
        yesConstraints.weighty = 0;
        yesConstraints.insets = gapInsets;
        buttonPanel.add(yesButton, yesConstraints);
        GridBagConstraints noConstraints = new GridBagConstraints();
        noConstraints.fill = GridBagConstraints.HORIZONTAL;
        noConstraints.gridx = 0;
        noConstraints.gridy = 1;
        noConstraints.weightx = 1;
        noConstraints.weighty = 0;
        buttonPanel.add(noButton, noConstraints);
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        labelConstraints.weightx = 0.3;
        labelConstraints.weighty = 0;
        labelConstraints.insets = new Insets(10, 0, 0, 40);
        labelConstraints.anchor = GridBagConstraints.EAST;
        myPanel.add(label, labelConstraints);
        GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
        buttonPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonPanelConstraints.gridx = 1;
        buttonPanelConstraints.gridy = 0;
        buttonPanelConstraints.weightx = 0.7;
        buttonPanelConstraints.weighty = 0;
        buttonPanelConstraints.insets = new Insets(10, 0, 0, 0);
        buttonPanelConstraints.anchor = GridBagConstraints.WEST;
        myPanel.add(buttonPanel, buttonPanelConstraints);
        GridBagConstraints glueConstraints = new GridBagConstraints();
        glueConstraints.fill = GridBagConstraints.BOTH;
        glueConstraints.gridx = 0;
        glueConstraints.gridy = 1;
        glueConstraints.weightx = 1;
        glueConstraints.weighty = 1;
        glueConstraints.gridwidth = GridBagConstraints.REMAINDER;
        glueConstraints.gridheight = GridBagConstraints.REMAINDER;
        myPanel.add(Box.createGlue(), glueConstraints);
        this.add(myPanel);
    }

    /**
	 * Inits the tab's components.
	 */
    private void initComponents() {
        buttonGroup = new ButtonGroup();
        String msgSaveHistoryOnShutdown = Messages.getMessage("DIALOGS_OPTIONS_SAVE_SAVEONSHUTDOWN");
        String msgYes = Messages.getMessage("DIALOGS_OPTIONS_SAVE_SAVEONSHUTDOWN_YES");
        String msgNo = Messages.getMessage("DIALOGS_OPTIONS_SAVE_SAVEONSHUTDOWN_NO");
        label = new JLabel(msgSaveHistoryOnShutdown, JLabel.RIGHT);
        yesButton = new JRadioButton(msgYes, true);
        noButton = new JRadioButton(msgNo, true);
        yesButton.setActionCommand(String.valueOf(SaveOptions.HISTORY_YES));
        noButton.setActionCommand(String.valueOf(SaveOptions.HISTORY_NO));
        buttonGroup.add(yesButton);
        buttonGroup.add(noButton);
    }

    /**
	 * @return The buttonGroup attribute, containing the Save Yes and Save No
	 *         JRadioButtons
	 */
    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    /**
	 * Selects a save JRadioButton, depending on the hasSave parameter value
	 * 
	 * @param hasSave
	 *            Has to be either HISTORY_YES or HISTORY_NO, otherwise a
	 *            IllegalArgumentException is thrown
	 * @throws IllegalArgumentException
	 */
    public void selectHasSaveButton(int hasSave) throws IllegalArgumentException {
        switch(hasSave) {
            case SaveOptions.HISTORY_YES:
                yesButton.setSelected(true);
                break;
            case SaveOptions.HISTORY_NO:
                noButton.setSelected(true);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
