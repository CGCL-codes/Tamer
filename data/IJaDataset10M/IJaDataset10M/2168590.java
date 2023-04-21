package net.sourceforge.volunteer.action;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sourceforge.volunteer.FocusManager;
import net.sourceforge.volunteer.Main;
import net.sourceforge.volunteer.module.event.EventsModule;
import net.sourceforge.volunteer.module.place.PlacesModule;
import net.sourceforge.volunteer.module.volunteer.VolunteerModule;
import net.sourceforge.volunteer.ui.ConfigureCard;

/**
 * {description}
 * 
 * @author Vasiliy Gagin
 * 
 * @version $Revision$
 */
public class ConfigureManager {

    private final ModuleHandler[] moduleHandlers;

    private final FocusManager focusManager;

    /**
     * ConfigureManager
     */
    public ConfigureManager() {
        focusManager = Main.focusManager;
        moduleHandlers = new ModuleHandler[] { new ModuleHandler("Places", Main.createBean(PlacesModule.class)), new ModuleHandler("Events", Main.createBean(EventsModule.class)), new ModuleHandler("Volunteers", Main.createBean(VolunteerModule.class)) };
    }

    /**
     * @return new card
     */
    public ConfigureCard createCard() {
        final ConfigureCard configureCard = new ConfigureCard(this);
        configureCard.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                int selectedIndex = configureCard.getSelectedIndex();
                focusManager.setComponentWithFocus(moduleHandlers[selectedIndex].module);
            }
        });
        for (ModuleHandler moduleHandler : moduleHandlers) {
            configureCard.addTab(moduleHandler.name, moduleHandler.module.getPanel());
        }
        return configureCard;
    }

    private static class ModuleHandler {

        final String name;

        final BaseConfigurationModule module;

        /**
         * ModuleHandler
         * 
         * @param name
         * @param module
         */
        public ModuleHandler(String name, BaseConfigurationModule module) {
            this.name = name;
            this.module = module;
        }
    }
}
