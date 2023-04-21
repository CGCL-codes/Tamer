package backend.command;

import java.util.Iterator;

public class HelpCommand extends AbstractCommand {

    public HelpCommand(CommandType commandType) {
        super(commandType);
    }

    @Override
    public String fire(Context context, CommandInput input) throws CommandExecutionException, CommandParameterException {
        CommandManager cm = CommandManager.getInstance(getCommandType());
        String response = null;
        if (input.getParam() == null) {
            Iterator<String> itr = cm.getCommands().keySet().iterator();
            StringBuffer sb = new StringBuffer();
            while (itr.hasNext()) {
                String label = (String) itr.next();
                sb.append("Command: " + label + "\n");
                Command c = cm.getCommands().get(label);
                sb.append("Usage: " + c.help() + "\n\n");
            }
            response = sb.toString();
        } else {
            input.setName(input.getParam());
            input.setParam(Command.HELP);
            try {
                response = cm.executeCommand(context, input);
            } catch (CommandNotFoundException e) {
                throw new CommandParameterException("Command not found: " + input.getName(), e);
            }
        }
        return response;
    }

    @Override
    public String help() {
        return "@help";
    }

    @Override
    public String namePattern() {
        return "help";
    }
}
