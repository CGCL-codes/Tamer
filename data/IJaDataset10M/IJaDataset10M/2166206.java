package eu.roelbouwman.housestyle;

import java.util.ResourceBundle;
import nextapp.echo2.app.SplitPane;
import org.apache.log4j.Logger;
import eu.roelbouwman.housestyle.forms.AbstractMain;
import eu.roelbouwman.housestyle.forms.DefaultMenuBar;
import eu.roelbouwman.housestyle.utils.LogUtil;

/**
 * Extend to AbstractMain if you want to set up the portal housestyle.
 * The default extends AbstractMainApplication and creates a screen with the
 * application housestyle.
 */
public class MainForm extends AbstractMain {

    ResourceBundle resourceBundle;

    Logger logger = LogUtil.getLogger(MainForm.class);

    public MainForm() {
        super();
        initComponents();
    }

    private void initComponents() {
        resourceBundle = ResourceBundle.getBundle("eu.roelbouwman.resources.localization.housestyle_" + Application.getApp().getLanguage());
        menuLeft.removeAll();
        menuLeft.add(new LeftMenu());
        menuTop.removeAll();
        menuTop.add(new LanguageSelector());
        MessageDialog md = new MessageDialog();
        md.setControlConfiguration(md.CONTROLS_YES_NO);
        add(md);
    }
}
