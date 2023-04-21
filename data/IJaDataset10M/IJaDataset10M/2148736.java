package com.vitria.test.commandline.application;

import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.vitria.test.commandline.CommandParser;
import com.vitria.test.commandline.CommandResult;
import com.vitria.test.common.CommandLineLogger;

public abstract class CommandLineBase {

    private static final Log log_ = LogFactory.getLog(CommandLineBase.class);

    private List<String> commandLineArguments;

    public CommandLineBase(List<String> commandLineArguments) {
        super();
        this.setCommandLineArguments(commandLineArguments);
    }

    public void run() {
        CommandParser cmdParser = createCommandParser();
        try {
            CommandResult cmdResult = cmdParser.parse(getCommandLineArguments());
            if (cmdResult.isHelpCommand()) {
                log(cmdParser.getCommandHelp());
            } else {
                work(cmdResult.getCommandOptions(), cmdResult.getExtraCommandLines());
            }
        } catch (Exception e) {
            log_.error(cmdParser.getCommandHelp(), e);
        }
    }

    private void setCommandLineArguments(List<String> commandLineArguments) {
        this.commandLineArguments = commandLineArguments;
    }

    public List<String> getCommandLineArguments() {
        return commandLineArguments;
    }

    public void log(Object msg) {
        CommandLineLogger.print(msg);
    }

    /**
     * template method
     * 
     * @param cmdOpts: parsed command line options
     * @param extraCmds: extra command line arguments which are not command options 
     *      
     * @throws Exception
     *      any exception thrown during working
     * 
     */
    protected abstract void work(Map<String, Object> cmdOpts, List<String> extraCmds) throws Exception;

    /**
     * template method
     * @return a command parser which can be used to parse command line arguments
     */
    protected abstract CommandParser createCommandParser();
}
