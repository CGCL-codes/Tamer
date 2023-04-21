package ch.ethz.dcg.spamato.stats.server.process.handler.collaborative.earlgrey.client;

public class ClientConfig {

    private static ClientConfig theInstance = null;

    protected ClientConfig() {
        theInstance = this;
    }

    public static synchronized ClientConfig getInstance() {
        return theInstance;
    }

    public String getServerAddress() {
        return "spamato.net";
    }

    public int getServerPort() {
        return 10014;
    }

    public String getUsername() {
        return "COLLABORATIVE@spamato.net";
    }

    public int getMinNrOfVoters() {
        return 2;
    }

    public float getMinQuotaOfVotersSimple() {
        return 0.51f;
    }

    public int getTopXVoters2Use() {
        return 20;
    }

    public float getMinQuotaOfVotersControversial() {
        return 0.51f;
    }
}
