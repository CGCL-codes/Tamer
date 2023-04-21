package net.woodstock.rockapi.anttasks.mysql.tasks;

import java.util.List;
import net.woodstock.rockapi.anttasks.common.AntCommand;
import net.woodstock.rockapi.anttasks.mysql.common.MysqlBaseTask;
import org.apache.tools.ant.BuildException;

public class CommandTask extends MysqlBaseTask {

    private static final long serialVersionUID = 1L;

    public static final String[] listeners = { "command" };

    private String command;

    public CommandTask() {
        super();
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public void execute() throws BuildException {
        if ((this.command == null) || (this.command.trim().equals(""))) {
            throw new BuildException("command not defined!!!");
        }
        List<String> cmd = this.getDefaultParams();
        cmd.add("--execute=" + this.command);
        AntCommand.execute(this.getProject(), cmd);
    }
}
