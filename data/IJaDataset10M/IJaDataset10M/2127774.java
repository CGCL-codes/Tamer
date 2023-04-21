package com.j2biz.blogunity.installer.tasks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import com.j2biz.blogunity.BlogunityManager;
import com.j2biz.blogunity.installer.ExecutionStatus;
import com.j2biz.blogunity.installer.InstallationContext;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 * 
 */
public class Log4jPropertiesTask extends AbstractInstallationTask {

    private VelocityEngine engine;

    public Log4jPropertiesTask(VelocityEngine engine) {
        super("Creating log4j.properties...");
        this.engine = engine;
    }

    public ExecutionStatus execute(InstallationContext context) {
        try {
            Context vars = context.getConfigurationTemplatesVariables();
            File log4jPropertiesFile = new File(BlogunityManager.getRealPath("/WEB-INF/classes/log4j.properties"));
            boolean result = log4jPropertiesFile.createNewFile();
            if (!result) throw new IOException("Unable to create new file: " + log4jPropertiesFile.getAbsolutePath());
            addDetail("Loading velocity template 'log4j.properties'.");
            org.apache.velocity.Template template = engine.getTemplate("log4j.properties");
            FileWriter writer = new FileWriter(log4jPropertiesFile);
            addDetail("Merging template 'log4j.properties'.");
            template.merge(vars, writer);
            if (context.isUseFileLogger()) {
                String logFile = (String) vars.get("logFile");
                addDetail("Create '" + logFile + "' file,    if not already exists..");
                File f = new File(logFile);
                if (!f.exists()) {
                    result = f.createNewFile();
                    if (!result) throw new IOException("Unable to create new file: " + f.getAbsolutePath());
                } else {
                    if (!f.isFile() || !f.canRead() || !f.canWrite()) {
                        throw new IOException("Unable to validate log-file: " + f.getAbsolutePath());
                    }
                }
            }
            writer.flush();
            writer.close();
            return ok();
        } catch (Throwable t) {
            addDetail(ExceptionUtils.getStackTrace(t));
            return failed();
        }
    }
}
