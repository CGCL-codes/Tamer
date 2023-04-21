package fitService;

import fitService.fitnesse.FitnesseServerCommunicator;
import fitService.fitnesse.FitnesseWikiTranslator;
import fitService.mase.MaseServerCommunicator;
import fitService.mase.MaseWikiTranslator;

public class FitClipseStub {

    public static enum ServerType {

        SERVER_MASE, SERVER_FITNESSE
    }

    ;

    private ServerType fitServerType;

    private ServerCommunicator fitConnection;

    private Translator fitTranslator;

    public FitClipseStub() {
    }

    public FitClipseStub(ServerType serverType) {
        fitServerType = serverType;
        if (fitServerType == ServerType.SERVER_FITNESSE) {
            fitConnection = new FitnesseServerCommunicator();
            fitTranslator = new FitnesseWikiTranslator();
        }
        if (fitServerType == ServerType.SERVER_MASE) {
            fitConnection = new MaseServerCommunicator();
            fitTranslator = new MaseWikiTranslator();
        }
    }

    public ServerType getFitServerType() {
        return fitServerType;
    }

    public void setFitServerType(ServerType fitServerType) {
        this.fitServerType = fitServerType;
    }

    public ServerCommunicator getFitConnection() {
        return fitConnection;
    }

    public void setFitConnection(ServerCommunicator fitConnection) {
        this.fitConnection = fitConnection;
    }

    public Translator getFitTranslator() {
        return fitTranslator;
    }

    public void setFitTranslator(Translator fitTranslator) {
        this.fitTranslator = fitTranslator;
    }
}
