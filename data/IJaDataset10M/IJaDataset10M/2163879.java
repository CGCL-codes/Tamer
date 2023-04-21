package com.smartwish.batch.polling;

import java.io.File;
import groovy.lang.Binding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sadun.util.polling.BasePollManager;
import org.sadun.util.polling.FileMovedEvent;
import com.smartwish.batch.common.job.Manager;
import com.smartwish.batch.scripting.ScriptRunner;

public class ScriptedPollManager extends BasePollManager {

    private static Log log = LogFactory.getLog(ScriptedPollManager.class);

    private String script;

    private Manager manager;

    private ScriptRunner scriptRunner;

    public void setScript(String script) {
        this.script = script;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public void setScriptRunner(ScriptRunner scriptRunner) {
        this.scriptRunner = scriptRunner;
    }

    public void fileMoved(FileMovedEvent evt) {
        String filePath = evt.getPath().getAbsolutePath();
        log.debug("ScriptedPollManager - new input file was moved to : " + filePath);
        Binding binding = new Binding();
        binding.setVariable("evt", evt);
        binding.setVariable("manager", manager);
        scriptRunner.setScriptName(script);
        scriptRunner.setBinding(binding);
        try {
            scriptRunner.run();
        } catch (Exception e) {
            log.error("Error executing script : " + script, e);
        } finally {
            File receivedFile = new File(filePath);
            if (receivedFile.exists()) {
                if (!receivedFile.delete()) log.warn("Could not delete file '" + filePath + "'");
            }
        }
    }
}
