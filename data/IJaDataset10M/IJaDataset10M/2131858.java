package net.future118.smallbusiness.rcp;

/**
 * Interface defining the application's command IDs.
 * Key bindings can be defined for specific commands.
 * To associate an action with a command, use IAction.setActionDefinitionId(commandId).
 *
 * @see org.eclipse.jface.action.IAction#setActionDefinitionId(String)
 */
public interface ICommandIds {

    public static final String CMD_OPEN = "net.future118.smallbusiness.rcp.open";

    public static final String CMD_OPEN_MESSAGE = "net.future118.smallbusiness.rcp.openMessage";

    public static final String CMD_OPEN_WELCOME_PERSPECTIVE = "net.future118.smallbusiness.rcp.openWelcomePerspective";

    public static final String CMD_OPEN_DOCUMENTS_PERSPECTIVE = "net.future118.smallbusiness.rcp.openDocumentsPerspective";

    public static final String CMD_OPEN_CONTACTS_PERSPECTIVE = "net.future118.smallbusiness.rcp.openContactsPerspective";
}
