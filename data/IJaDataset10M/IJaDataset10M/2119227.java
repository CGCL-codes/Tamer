package savenews.app.gui.panel;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import savenews.backend.util.I18NResources;

/**
 * About window panel
 * @author Eduardo Ferreira
 */
public class AboutWindowPanel extends JPanel {

    /** SVUID */
    private static final long serialVersionUID = 1L;

    private JDialog parentDialog;

    /** Main window layout manager */
    private final LayoutManager layout = new GridBagLayout();

    private final JLabel aboutLabel;

    /** Panel containing buttons */
    private final JPanel buttonsField;

    private final JButton okButton;

    /**
     * Default constructor
     * @param parentDialog Parent dialog
     */
    public AboutWindowPanel(JDialog parentDialog) {
        this.parentDialog = parentDialog;
        aboutLabel = new JLabel(I18NResources.getInstance().getAboutMessage());
        buttonsField = new JPanel();
        okButton = new JButton(I18NResources.getInstance().get(I18NResources.DIALOG_BUTTON_OK));
        aboutLabel.setName("aboutLabel");
        buttonsField.setName("buttonsField");
        okButton.setName("okButton");
        configureComponents();
        assembleLayout();
    }

    /**
     *  Configures all components
     */
    private void configureComponents() {
        configureLabels();
        configureButtons();
    }

    private void configureButtons() {
        BoxLayout buttonLayout = new BoxLayout(buttonsField, BoxLayout.X_AXIS);
        buttonsField.setLayout(buttonLayout);
        Font plainButtonFont = okButton.getFont().deriveFont(Font.PLAIN);
        okButton.setFont(plainButtonFont);
        buttonsField.add(Box.createGlue());
        buttonsField.add(okButton);
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                parentDialog.dispose();
            }
        });
    }

    private void configureLabels() {
        Font plainLabelFont = aboutLabel.getFont().deriveFont(Font.PLAIN);
        aboutLabel.setFont(plainLabelFont);
    }

    /**
     * Assembles the application layout
     */
    private void assembleLayout() {
        setLayout(layout);
        GridBagConstraints c = getGridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        add(aboutLabel, c);
        c = getGridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 3;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.EAST;
        add(buttonsField, c);
    }

    private GridBagConstraints getGridBagConstraints() {
        GridBagConstraints c = new GridBagConstraints();
        int insetsize = 2;
        c.insets = new Insets(insetsize, insetsize, insetsize, insetsize);
        return c;
    }
}
