package org.argouml.pattern.cognitive.critics;

import java.util.Collections;
import java.util.List;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.application.api.InitSubsystem;
import org.argouml.cognitive.Agency;
import org.argouml.cognitive.Critic;
import org.argouml.model.Model;

/**
 * Initialise the Pattern related Critics.
 *
 * @author Michiel
 */
public class InitPatternCritics implements InitSubsystem {

    private static Critic crConsiderSingleton = new CrConsiderSingleton();

    private static Critic crSingletonViolatedMSA = new CrSingletonViolatedMissingStaticAttr();

    private static Critic crSingletonViolatedOPC = new CrSingletonViolatedOnlyPrivateConstructors();

    public List<GUISettingsTabInterface> getProjectSettingsTabs() {
        return Collections.emptyList();
    }

    public List<GUISettingsTabInterface> getSettingsTabs() {
        return Collections.emptyList();
    }

    public void init() {
        Object classCls = Model.getMetaTypes().getUMLClass();
        Agency.register(crConsiderSingleton, classCls);
        Agency.register(crSingletonViolatedMSA, classCls);
        Agency.register(crSingletonViolatedOPC, classCls);
    }

    public List<AbstractArgoJPanel> getDetailsTabs() {
        return Collections.emptyList();
    }
}
