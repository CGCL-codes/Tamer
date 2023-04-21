package com.ek.mitapp.ui.wizard;

import java.util.Map;
import javax.swing.JComponent;
import org.netbeans.spi.wizard.WizardController;
import org.netbeans.spi.wizard.WizardPanelProvider;
import com.ek.mitapp.AppSettings;
import com.ek.mitapp.ui.wizard.panels.DefaultIntroPanel;
import com.ek.mitapp.ui.wizard.panels.JExcelPanel;

/**
 * TODO: Class description.
 * <br>
 * Id: $Id: $

 * @author dirwin
 */
public class MitListInitialSteps extends WizardPanelProvider {

    /**
     * Introduction description.
     */
    private static final String INTRO = "intro";

    /**
     * Define introduction text.
     */
    private static final String introText = "<html>This wizard will help guide you through the process of entering mitigation type<br>" + "information into the mitigation prioritization Excel spreadsheet.</html>";

    /**
     * Reference to the application settings object.
     */
    private final AppSettings appSettings;

    /**
     * References to the known panels.
     */
    private JExcelPanel introPanel;

    /**
     * @param appSettings
     */
    public MitListInitialSteps(AppSettings appSettings) {
        super("City of Houston :: Mitigation List Entry wizard ", new String[] { INTRO }, new String[] { "Introduction" });
        this.appSettings = appSettings;
    }

    /**
     * @see org.netbeans.spi.wizard.WizardPanelProvider#createPanel(org.netbeans.spi.wizard.WizardController, java.lang.String, java.util.Map)
     */
    @Override
    protected JComponent createPanel(final WizardController controller, final String id, final Map data) {
        switch(indexOfStep(id)) {
            case 0:
                if (introPanel == null) introPanel = new DefaultIntroPanel(controller, data, introText);
                return introPanel.createPanel();
            default:
                throw new IllegalArgumentException(id);
        }
    }
}
