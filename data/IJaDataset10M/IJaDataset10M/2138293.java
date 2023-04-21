package fitnesse.server;

import fit.Counts;
import fit.FitServer;

class FitServerRunner extends Runner {

    public FitServerRunner(Parameters fitServerParameters) {
        this.parameters = fitServerParameters;
    }

    protected Counts run(String[] params) throws Exception {
        FitServer fitServer = new FitServer();
        fitServer.run(params);
        return fitServer.getCounts();
    }
}
