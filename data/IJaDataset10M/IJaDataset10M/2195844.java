package Mypackage.Runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.util.text.StringUtil;

/**
 * This code bases on the intellij-batch plugin by wibotwi.
 *
 * @author wibotwi, jansorg
 */
public class PerlCommandLineState extends CommandLineState {

    private final PerlRunConfiguration runConfiguration;

    public PerlCommandLineState(PerlRunConfiguration runConfiguration, ExecutionEnvironment env) {
        super(env);
        this.runConfiguration = runConfiguration;
    }

    @Override
    protected OSProcessHandler startProcess() throws ExecutionException {
        GeneralCommandLine commandLine = generateCommandLine();
        OSProcessHandler osProcessHandler = new OSProcessHandler(commandLine.createProcess(), commandLine.getCommandLineString());
        osProcessHandler.putUserData(OSProcessHandler.SILENTLY_DESTROY_ON_CLOSE, Boolean.TRUE);
        ProcessTerminatedListener.attach(osProcessHandler, getRunConfiguration().getProject());
        return osProcessHandler;
    }

    protected GeneralCommandLine generateCommandLine() {
        GeneralCommandLine commandLine = new GeneralCommandLine();
        if (!StringUtil.isEmptyOrSpaces(getRunConfiguration().getInterpreterPath())) commandLine.setExePath(getRunConfiguration().getInterpreterPath());
        commandLine.getParametersList().addParametersString(getRunConfiguration().getInterpreterOptions());
        if (!StringUtil.isEmptyOrSpaces(getRunConfiguration().getScriptName())) {
            commandLine.addParameter(getRunConfiguration().getScriptName());
        }
        commandLine.getParametersList().addParametersString(getRunConfiguration().getScriptParameters());
        if (!StringUtil.isEmptyOrSpaces(getRunConfiguration().getWorkingDirectory())) {
            commandLine.setWorkDirectory(getRunConfiguration().getWorkingDirectory());
        }
        commandLine.setEnvParams(getRunConfiguration().getEnvs());
        commandLine.setPassParentEnvs(getRunConfiguration().isPassParentEnvs());
        return commandLine;
    }

    protected PerlRunConfiguration getRunConfiguration() {
        return runConfiguration;
    }
}
