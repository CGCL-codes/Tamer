package net.finnigin.process;

import java.util.List;

public class BufferedProcess extends GobbledProcess {

    private boolean redirect;

    public BufferedProcess(List<String> command, boolean redirect) {
        super(command);
        this.redirect = redirect;
        inputStreamGobbler = new BufferedGobbler();
        errorStreamGobbler = new BufferedGobbler();
    }

    @Override
    protected void beforeProcessStart() throws Exception {
        processBuilder.redirectErrorStream(redirect);
    }

    public String getInput() {
        return inputStreamGobbler.toString();
    }

    public String getError() {
        return errorStreamGobbler.toString();
    }

    public boolean getRedirect() {
        return redirect;
    }
}
