package net.sf.jabs.task.shell;

import java.io.File;
import net.sf.jabs.data.Fields;
import net.sf.jabs.task.TaskStartupException;
import net.sf.jabs.task.cmd.ExecTaskRunner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ShellTaskRunner extends ExecTaskRunner {

    private static Log _log = LogFactory.getLog(ShellTaskRunner.class);

    File _script;

    protected void startup() throws TaskStartupException {
        if (_log.isDebugEnabled()) _log.info("Starting up shell task");
        try {
            _script = createScriptFile(_loader.getProperty(getString("script"), true));
            String shellCmd = getString("shell");
            if (shellCmd == null) {
                shellCmd = _script.getAbsolutePath();
            } else {
                shellCmd += (" " + _script.getAbsolutePath());
            }
            _task.setString(Fields.ProjectTask.COMMAND, shellCmd);
        } catch (Exception e) {
            throw new TaskStartupException(e.getMessage());
        }
    }

    protected void shutdown() {
        if (_log.isDebugEnabled()) _log.info("Shutting down shell task");
        if (_script != null && _script.exists()) {
            _log.info("Deleting temp file " + _script.getAbsolutePath());
            _script.delete();
        }
    }

    protected File createScriptFile(String scriptBody) throws Exception {
        String workingDir = getDirectory();
        String tempSuffix = null;
        String osName = System.getProperty("os.name");
        if (osName != null && osName.toLowerCase().indexOf("windows") > -1) {
            tempSuffix = ".bat";
        }
        File tmpFile = File.createTempFile("shell", tempSuffix, new File(workingDir));
        FileUtils.writeStringToFile(tmpFile, scriptBody.replaceAll("\r\n", "\n"));
        return tmpFile;
    }
}
