package org.vfsutils.shell.events;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.vfsutils.shell.Arguments;
import org.vfsutils.shell.Engine;

public class RecordingEventListener extends AbstractEngineEventListener {

    protected static Log log = LogFactory.getLog(RecordingEventListener.class);

    protected FileObject outputFile;

    protected Engine engine;

    protected String encoding = "UTF-8";

    private OutputStream outStream = null;

    public RecordingEventListener(FileObject outputFile, Engine engine) {
        this.engine = engine;
        this.outputFile = outputFile;
    }

    protected void init() throws IOException {
        this.outStream = outputFile.getContent().getOutputStream();
    }

    protected void close() throws IOException {
        if (this.outStream != null) {
            this.outStream.flush();
            this.outStream.close();
            this.outStream = null;
        }
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void commandStarted(Arguments args) {
        if (this.outStream != null) {
            String message = this.engine.getCommandParser().toString(args.getAllTokens()) + "\n";
            try {
                this.outStream.write(message.getBytes(this.encoding));
                this.outStream.flush();
            } catch (IOException e) {
                log.error("Error writing command " + message, e);
            }
        }
    }

    public void engineStarted() {
        try {
            init();
        } catch (IOException e) {
            log.error("Error initializing", e);
        }
    }

    public void engineStopping() {
        try {
            close();
        } catch (IOException e) {
            log.error("Error closing", e);
        }
    }

    protected void finalize() throws Throwable {
        close();
    }
}
