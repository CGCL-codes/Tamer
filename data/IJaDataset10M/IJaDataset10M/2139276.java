package seevolution.gui.listeners;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.vecmath.*;
import seevolution.*;
import seevolution.gui.*;
import seevolution.gui.preferences.*;

/**
 * Listens to the "Preferences" button in the toolbar, and displays a dialog that allows the user to
 * set all the preferences that affect the graphical representation of the chromosomes and the general
 * settings related to the GUI.<br>
 * The dialog has several tabs that cover the different types of preferences. Each of the tabs is a separate
 * class that extends a JPanel, and is it's own listener for any of the components it contains. They all have
 * a reference to an object of this class to notify any changes and enable the "Apply" button.<br>
 * This class is its own listener for the "Apply", "Accept", and "Cancel" buttons.
 * @author Andres Esteban Marcos
 * @version 1.0
 */
public class PreferencesListener extends AbstractAction {

    private SeevolutionView view;

    private Preferences prefs;

    private JButton applyButton, okButton, cancelButton;

    private JFrame prefsFrame;

    private LanguageStrings ls;

    private String lastLanguage;

    private AppearanceTab appearanceTab;

    private AnimationTab animationTab;

    private AnimationTimesTab animationTimesTab;

    private MiscTab miscTab;

    private boolean createNewUniverse = false;

    /**
	 * The only constructor
	 * @param view A reference to the SeevolutionView object, necessary to perform the action
	 */
    public PreferencesListener(SeevolutionView view) {
        this.view = view;
        ls = view.getLanguageStrings();
        prefs = view.getPreferences();
        applyButton = null;
        okButton = null;
        cancelButton = null;
        prefsFrame = buildFrame();
        prefsFrame.setVisible(false);
    }

    /**
	 * Applies the changes made to the preferences. Each of the tabs manages its own preferences.
	 */
    private void apply() {
        appearanceTab.savePreferences(prefs);
        animationTab.savePreferences(prefs);
        animationTimesTab.savePreferences(prefs);
        miscTab.updatePreferences();
        if (createNewUniverse) {
            createNewUniverse = false;
            view.createNewUniverse();
        } else {
            Genome genome = view.getGenome();
            int numberOfColors = prefs.getInt("numberOfColors");
            Color3f colors[] = null;
            if (numberOfColors > 0) {
                colors = new Color3f[numberOfColors];
                for (int i = 0; i < numberOfColors; i++) colors[i] = new Color3f(new Color(prefs.getInt("color" + i)));
            }
            genome.setColoring(prefs.getInt("coloring"), colors);
            genome.setLighting(prefs.getBool("lighting"));
            genome.setWireframe(prefs.getBool("wireframe"));
            genome.setTextures(prefs.getBool("textures"));
        }
    }

    /** 
	 * Constructs the frame and all its elements
	 * @return The frame
	 */
    private JFrame buildFrame() {
        lastLanguage = ls.getString(ls.LANGUAGE_NAME);
        JFrame prefsFrame = new JFrame(ls.getString(LanguageStrings.PREFS_TITLE));
        prefsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        prefsFrame.setAlwaysOnTop(true);
        JPanel sidePanel = new JPanel(new GridLayout(1, 5, 10, 10));
        applyButton = new JButton(ls.getString(LanguageStrings.APPLY));
        okButton = new JButton(ls.getString(LanguageStrings.OK));
        cancelButton = new JButton(ls.getString(LanguageStrings.CANCEL));
        applyButton.addActionListener(this);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        sidePanel.add(new JPanel());
        sidePanel.add(applyButton);
        sidePanel.add(okButton);
        sidePanel.add(cancelButton);
        sidePanel.add(new JPanel());
        sidePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
        prefsFrame.add(sidePanel, BorderLayout.SOUTH);
        JTabbedPane mainPanel = new JTabbedPane();
        appearanceTab = new AppearanceTab(this);
        mainPanel.addTab(ls.getString(ls.APPEARANCE_TAB), appearanceTab);
        animationTab = new AnimationTab(this);
        mainPanel.addTab(ls.getString(ls.ANIMATION_TAB), animationTab);
        animationTimesTab = new AnimationTimesTab(this);
        mainPanel.addTab(ls.getString(ls.ANIMATION_TIMES_TAB), animationTimesTab);
        miscTab = new MiscTab(this);
        mainPanel.addTab(ls.getString(ls.MISC), miscTab);
        prefsFrame.add(mainPanel, BorderLayout.CENTER);
        prefsFrame.setLocationRelativeTo(view.getFrame());
        prefsFrame.setSize(450, 400);
        prefsFrame.setResizable(false);
        applyButton.setEnabled(false);
        return prefsFrame;
    }

    /**
 	 * Required by the ActionListener interface, fired when the preferences button is pressed,
 	 * or when any of the "Apply","OK" or "Cancel" is pressed.<br>
 	 * When the "Preferences" button is pressed, this method takes care of building the dialog,
 	 * and creating and adding all the tabs.
	 * @param ae The event
	 */
    public void actionPerformed(ActionEvent ae) {
        boolean create = false;
        ls = view.getLanguageStrings();
        try {
            JButton source = (JButton) ae.getSource();
            if (source == applyButton) {
                applyButton.setEnabled(false);
                apply();
            } else if (source == okButton) {
                apply();
                prefsFrame.setVisible(false);
            } else if (source == cancelButton) {
                prefsFrame.setVisible(false);
            } else {
                create = true;
            }
            if (!create) return;
        } catch (ClassCastException e) {
        }
        if (!lastLanguage.equals(ls.getString(ls.LANGUAGE_NAME))) {
            prefsFrame.dispose();
            prefsFrame = buildFrame();
        }
        appearanceTab.loadPreferences(prefs);
        animationTab.loadPreferences(prefs);
        animationTimesTab.loadPreferences(prefs);
        miscTab.loadPreferences(prefs);
        prefsFrame.setVisible(true);
    }

    /**
	 * Changes the state of the Apply button to enabled or disabled
	 * @param b The button is enabled when true
	 */
    public void enableApply() {
        applyButton.setEnabled(true);
    }

    /**
	 * Returns a LanguageStrings object with the strings of the language currently in use. Used to create the
	 * tabs with the proper language.
	 * @return The LanguageStrings object for the current language
	 */
    public LanguageStrings getLanguageStrings() {
        return ls;
    }

    /**
	 * Returns a reference to the SeevolutionView object, used by some of the tabs.
	 * @return The SeevolutionView object
	 */
    public SeevolutionView getView() {
        return view;
    }

    /**
	 * Sets the necessity to create a new genome due to some change in the preferences that cannot be
	 * changed dinamically.<br>
	 * It would probably work better if the value was returned by the method "updatePreferences" in each tab, 
	 * and it will be changed soon.
	 * @param b When true, sets the internal value to true. When false, leaves the value unchanged.
	 */
    public void setCreateNewUniverse(boolean b) {
        createNewUniverse = createNewUniverse || b;
    }

    /**
	 * Display an error message
	 * @param messageNumber A constant that identifies the error in LanguageStrings.
	 */
    public void showError(int error) {
        view.showError(error);
    }
}
