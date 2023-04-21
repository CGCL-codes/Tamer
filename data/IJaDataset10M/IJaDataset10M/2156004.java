package eu.roelbouwman.personWeb.forms;

import static eu.roelbouwman.config.PropertyLoader.properties;
import static eu.roelbouwman.personWeb.Application.getApp;
import nextapp.echo2.app.ResourceImageReference;
import nextapp.echo2.app.event.ActionEvent;
import eu.roelbouwman.housestyle.forms.DefaultLanguageSelector;
import eu.roelbouwman.menu.Controller;

public class LanguageSelector extends DefaultLanguageSelector {

    Controller controller = getApp().getController();

    public LanguageSelector() {
        setFlag();
        setLanguages(properties.getProperty("LANGUAGES"));
    }

    @Override
    public void actionListenerSelectLanguages(ActionEvent e) {
        if (!selLanguage.getSelectedItem().equals("")) {
            getApp().setLanguage(selLanguage.getSelectedItem().toString());
        }
    }

    @Override
    public void setFlag() {
        lblLanguageFlag.setIcon(new ResourceImageReference("eu/roelbouwman/resources/images/" + getApp().getLanguage() + ".gif"));
    }
}
