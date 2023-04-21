package net.sf.rpe.example.activity.java;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sf.rpe.core.activity.Activity;
import net.sf.rpe.core.context.Context;

public class PrintMessage implements Activity {

    private static final Log logger = LogFactory.getLog(PrintMessage.class);

    private String message;

    @Override
    public void execute(Context context) {
        if (logger.isInfoEnabled()) {
            logger.info("Message: " + this.message);
        }
    }
}
