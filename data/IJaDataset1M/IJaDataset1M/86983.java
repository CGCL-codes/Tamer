package edutex.latex;

import edutex.Executor;
import edutex.config.EdutexConfig;

public class PdfViewerExecutor extends Executor {

    private String[] params;

    public PdfViewerExecutor(EdutexConfig config) {
        super(config);
    }

    @Override
    protected void init() {
        params = new String[2];
        params[0] = config.getPdfCommand();
        params[1] = this.shortFileName + ".pdf";
    }

    public final void view() throws Exception {
        this.executeInReader(this.workingDirectory, params);
    }
}
