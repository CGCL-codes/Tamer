package net.dataforte.canyon.echo3.demo;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.app.TaskQueueHandle;
import nextapp.echo.app.text.TextComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleOutputInterceptor extends FilterOutputStream {

    static final Logger log = LoggerFactory.getLogger(ConsoleOutputInterceptor.class);

    TextComponent component;

    ByteArrayOutputStream os;

    TaskQueueHandle taskQueue;

    ApplicationInstance app;

    public ConsoleOutputInterceptor(TextComponent component, ApplicationInstance app, TaskQueueHandle taskQueue) {
        super(null);
        this.os = new ByteArrayOutputStream(1024);
        this.component = component;
        this.app = app;
        this.taskQueue = taskQueue;
    }

    @Override
    public void flush() throws IOException {
        app.enqueueTask(taskQueue, new TextComponentUpdateTask(false));
    }

    @Override
    public void close() throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Closing output...");
        }
        app.enqueueTask(taskQueue, new TextComponentUpdateTask(true));
    }

    /**
	 * Intercepts output - most common case of byte[]
	 */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        os.write(b, off, len);
    }

    /**
	 * Intercepts output - single characters
	 */
    @Override
    public void write(int b) throws IOException {
        os.write(b);
    }

    private class TextComponentUpdateTask implements Runnable {

        boolean complete;

        public TextComponentUpdateTask(boolean complete) {
            this.complete = complete;
        }

        @Override
        public void run() {
            component.setText(os.toString());
            if (complete) {
                if (log.isDebugEnabled()) {
                    log.debug("Output complete, removing task queue");
                }
            }
        }
    }
}
