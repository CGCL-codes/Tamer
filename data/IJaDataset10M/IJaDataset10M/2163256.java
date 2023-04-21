package ca.ucalgary.cpsc.ebe.fitClipse.tests.fit;

import ca.ucalgary.cpsc.ebe.fitClipse.connector.FitNesse;
import ca.ucalgary.cpsc.ebe.fitClipse.connector.ServerConfiguration;
import fitlibrary.DoFixture;

public class RenameTest extends DoFixture {

    /** The sc. */
    private static ServerConfiguration sc = null;

    /** The fitnesse. */
    private static FitNesse fitNesse = null;

    /**
	 * Connect to server.
	 */
    public void connectToServer() {
        if (fitNesse == null) {
            ServerConfiguration.clear();
            sc = ServerConfiguration.getInstance();
            sc.setHost("localhost");
            sc.setWebPort("8090");
            sc.setWebPath("FitClipse.ProjectS");
            sc.setProjectNameSpace("FitClipseProject");
            fitNesse = new FitNesse(sc);
        }
    }

    public boolean checkAcceptanceTestExists(String exists) {
        connectToServer();
        return this.fitNesse.doesPageExist(exists);
    }

    public boolean checkThatFixtureExistis(String fixture) {
        return false;
    }

    public boolean renameTo(String rename, String to) {
        return false;
    }
}
