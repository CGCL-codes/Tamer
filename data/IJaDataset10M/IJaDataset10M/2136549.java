package net.dataforte.canyon.cooee.demo;

import java.io.PrintStream;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import net.dataforte.canyon.spi.cooee.CooeeApplication;
import org.karora.cooee.app.ApplicationInstance;
import org.karora.cooee.app.Button;
import org.karora.cooee.app.TaskQueueHandle;
import org.karora.cooee.app.text.TextComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptRunner extends Thread {

    static final Logger log = LoggerFactory.getLogger(ScriptRunner.class);

    ScriptEngine engine;

    String script;

    CooeeApplication app;

    TextComponent text;

    TaskQueueHandle taskQueue;

    Button button;

    public ScriptRunner() {
        app = (CooeeApplication) ApplicationInstance.getActive();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager(contextClassLoader);
        engine = scriptEngineManager.getEngineByMimeType("text/javascript");
    }

    @Override
    public void run() {
        PrintStream out = new PrintStream(new ConsoleOutputInterceptor(text, app, taskQueue), true);
        app.getBinding().put("out", out);
        try {
            if (log.isDebugEnabled()) {
                log.debug("Executing script...");
            }
            engine.eval(script);
            if (log.isDebugEnabled()) {
                log.debug("Done script");
            }
        } catch (Throwable t) {
            out.println(t.getMessage());
            t.printStackTrace(out);
            log.error("Script error", t);
        }
        out.close();
        if (log.isDebugEnabled()) {
            log.debug("Closed output");
        }
        app.getBinding().put("out", null);
        if (log.isDebugEnabled()) {
            log.debug("Queue enable button");
        }
        app.enqueueTask(taskQueue, new Runnable() {

            @Override
            public void run() {
                if (log.isDebugEnabled()) {
                    log.debug("Enable button");
                }
                button.setEnabled(true);
                app.removeTaskQueue(taskQueue);
                if (log.isDebugEnabled()) {
                    log.debug("Removed task queue");
                }
            }
        });
    }

    public void runScript(String script, TextComponent text, Button button) {
        this.script = script.replace("\\r", "");
        this.text = text;
        this.button = button;
        text.setText("");
        taskQueue = app.createTaskQueue();
        if (log.isDebugEnabled()) {
            log.debug("Created TaskQueue");
        }
        button.setEnabled(false);
        this.start();
    }
}
