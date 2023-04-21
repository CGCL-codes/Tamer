package ntorrent.settings.model;

import java.lang.annotation.*;
import java.awt.Component;

/**
 * @author Kim Eik
 *
 */
public interface SettingsExtension {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserSetting {

        String label() default "";

        String oneOf() default "";
    }

    /**
	 * returns a string that gives a human understandable name of this plugin.
	 * This string will be shown in the settings menu.
	 * @return String
	 */
    public String getSettingsDisplayName();

    /**
	 * Fetch the user interface for user customizable values.
	 * if this returns null, then SettingsElement will autogenerate a display
	 * based on reflection data
	 * @return Component
	 */
    public Component getSettingsDisplay();

    /**
	 * this method is called when the save button has been pressed.
	 * Signals that the plugin should serialize the model connected to
	 * the display.
	 */
    public void saveActionPerformedOnSettings() throws Exception;
}
