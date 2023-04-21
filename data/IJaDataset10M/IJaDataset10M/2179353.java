package eu.roelbouwman.housestylePeugeot.forms;

import org.karora.cooee.app.Alignment;
import org.karora.cooee.app.Extent;
import org.karora.cooee.app.Insets;
import org.karora.cooee.app.Label;
import org.karora.cooee.app.ResourceImageReference;
import org.karora.cooee.app.Row;
import org.karora.cooee.app.SelectField;
import org.karora.cooee.app.event.ActionEvent;
import org.karora.cooee.app.event.ActionListener;
import org.karora.cooee.app.list.DefaultListModel;

public abstract class DefaultLanguageSelector extends Row {

    public SelectField selLanguage;

    public Label lblLanguageFlag;

    public String languages;

    public DefaultLanguageSelector() {
        initComponents();
    }

    /**
     * Creates initial list.
     */
    private void createSelectModel() {
        languages = ",en";
        selLanguage.setModel(new DefaultListModel(languages.split(",")));
        selLanguage.setSelectedIndex(0);
        selLanguage.setActionCommand("onchange");
        selLanguage.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionListenerSelectLanguages(e);
            }
        });
    }

    public void setLanguages(String langs) {
        languages = langs;
        selLanguage.setModel(new DefaultListModel(languages.split(",")));
    }

    /**
     * lblLanguageFlag.setIcon(new ResourceImageReference("eu/roelbouwman/resources/images/en.gif"));
     *
     */
    public abstract void setFlag();

    /**
     * selectLanguages ActionEventListener.
     * 
     * if(!selLanguage.getSelectedItem().equals("")){}
     * Application.getApp().setLanguage(selLanguage.getSelectedItem().toString());
     */
    public abstract void actionListenerSelectLanguages(ActionEvent e);

    private void initComponents() {
        setInsets(new Insets(new Extent(10, Extent.PX), new Extent(5, Extent.PX), new Extent(0, Extent.PX), new Extent(0, Extent.PX)));
        setAlignment(new Alignment(Alignment.RIGHT, Alignment.DEFAULT));
        lblLanguageFlag = new Label();
        lblLanguageFlag.setIcon(new ResourceImageReference("eu/roelbouwman/resources/images/en.gif"));
        add(lblLanguageFlag);
        selLanguage = new SelectField();
        createSelectModel();
        add(selLanguage);
    }
}
