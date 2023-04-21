package com.tabuto.jsimlife.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import com.tabuto.jsimlife.Configuration;
import com.tabuto.jsimlife.views.JSLPreferencesView;

/**
 * Class extends AbstractAction to perform the following Action:
 * Save the current preferences into an XML file
 * 
 * @author tabuto83
 *
 * @see AbstractAction
 * @see JSLPreferencesView
 * @see Configuration
 */
public class SavePreferencesAction extends AbstractAction {

    JSLPreferencesView preferencesView;

    Configuration preferences;

    /**
	 * Instantiate new SavePreferencesAction
	 * 
	 * @param preferencesview JSLPreferencesView 
	 */
    public SavePreferencesAction(JSLPreferencesView preferencesview) {
        super();
        preferencesView = preferencesview;
        preferences = preferencesView.getPreferences();
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Override
    public void actionPerformed(ActionEvent e) {
        preferences.setPlayfieldDimension(Integer.parseInt(preferencesView.widthField.getText()), Integer.parseInt(preferencesView.heightField.getText()));
        preferences.setBackgroundColor(preferencesView.colorButton.getBackground());
        preferences.setMaxZlifes(Integer.parseInt(preferencesView.MaxZlifesField.getText()));
        preferences.setMaxZretador(Integer.parseInt(preferencesView.MaxZretadorsField.getText()));
        preferences.setMaxSeeds(Integer.parseInt(preferencesView.MaxSeedsField.getText()));
        preferences.setPath(preferencesView.pathField.getText());
        preferences.setLocale(preferences.LocaleParseString(preferencesView.localeComboBox.getSelectedItem().toString()));
        preferences.save();
        JOptionPane.showMessageDialog(preferencesView, preferencesView.resource.getString("prf_restartMsg"), preferencesView.resource.getString("prf_applyChanges"), JOptionPane.PLAIN_MESSAGE);
        preferencesView.dispose();
    }
}
