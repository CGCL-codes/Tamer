package us.wthr.jdem846.ui.scripting;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;
import us.wthr.jdem846.JDem846Properties;
import us.wthr.jdem846.i18n.I18N;
import us.wthr.jdem846.ui.ComponentButtonBar;
import us.wthr.jdem846.ui.base.ComboBox;
import us.wthr.jdem846.ui.base.Label;
import us.wthr.jdem846.ui.optionModels.ScriptLanguageListModel;

@SuppressWarnings("serial")
public class ScriptEditorButtonBar extends ComponentButtonBar {

    public enum ScriptEditButtons {

        LANGUAGE
    }

    ;

    private ComboBox cmbLanguage;

    private ScriptLanguageListModel languageModel;

    private List<ScriptEditorButtonClickedListener> scriptEditorButtonClickedListeners = new LinkedList<ScriptEditorButtonClickedListener>();

    public ScriptEditorButtonBar(Component owner) {
        super(owner);
        languageModel = new ScriptLanguageListModel();
        Label lblLanguage = new Label(I18N.get("us.wthr.jdem846.ui.scriptEditorPane.language.label") + ":");
        cmbLanguage = new ComboBox(languageModel);
        cmbLanguage.setToolTipText(I18N.get("us.wthr.jdem846.ui.scriptEditorPane.language.tooltip"));
        boolean displayText = JDem846Properties.getBooleanProperty("us.wthr.jdem846.ui.outputImageButtonBar.displayText");
        lblLanguage.setVisible(displayText);
        cmbLanguage.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    fireButtonClickedListeners(ScriptEditButtons.LANGUAGE);
                }
            }
        });
        add(lblLanguage);
        add(cmbLanguage);
        addSeparator();
    }

    public void addScriptEditorButtonClickedListener(ScriptEditorButtonClickedListener listener) {
        scriptEditorButtonClickedListeners.add(listener);
    }

    public boolean removeScriptEditorButtonClickedListener(ScriptEditorButtonClickedListener listener) {
        return scriptEditorButtonClickedListeners.remove(listener);
    }

    protected void fireButtonClickedListeners(ScriptEditButtons button) {
        for (ScriptEditorButtonClickedListener listener : scriptEditorButtonClickedListeners) {
            listener.onButtonClicked(button);
        }
    }

    public interface ScriptEditorButtonClickedListener {

        public void onButtonClicked(ScriptEditButtons button);
    }
}
