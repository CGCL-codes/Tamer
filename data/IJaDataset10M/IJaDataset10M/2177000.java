package cn.vlabs.duckling.vwb.ui.command;

import java.util.HashMap;

/**
 * @date Mar 2, 2010
 * @author xiejj@cnic.cn
 */
public class CommandResolver {

    private static final Command[] ALL_COMMANDS = new Command[] { DPageCommand.VIEW, DPageCommand.EDIT, DPageCommand.DIFF, DPageCommand.SHARE, DPageCommand.INFO, DPageCommand.PREVIEW, DPageCommand.CONFLICT, VWBCommand.NONE, VWBCommand.ATTACH, VWBCommand.ERROR, VWBCommand.EDIT_PREFERENCE, VWBCommand.FIND, VWBCommand.CREATE_RESOURCE, VWBCommand.EDIT_PROFILE, VWBCommand.LOGIN, VWBCommand.ADMIN, PortalCommand.VIEW, PortalCommand.CONFIG };

    private static final HashMap<String, Command> m_cmdMap;

    static {
        m_cmdMap = new HashMap<String, Command>();
        Command[] allcommands = ALL_COMMANDS;
        for (Command command : allcommands) {
            m_cmdMap.put(command.getAction(), command);
        }
    }

    public static Command findCommand(String action) {
        return m_cmdMap.get(action);
    }
}
