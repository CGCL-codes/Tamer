package org.eclipse.plugin.worldwind.actions;

/**
 * Interface defining the application's command IDs.
 * Key bindings can be defined for specific commands.
 * To associate an action with a command, use IAction.setActionDefinitionId(commandId).
 *
 * @see org.eclipse.jface.action.IAction#setActionDefinitionId(String)
 */
public interface ICommandIds {

    public static final String CMD_OPEN_FILE = "org.eclipse.plugin.worldwind.OpenFile";

    public static final String CMD_WEATHER_WIZARD = "org.eclipse.plugin.worldwind.WeatherWizard";

    public static final String CMD_OPEN_WEB_BROWSER = "org.eclipse.plugin.worldwind.Open.WebBrowser";
}
