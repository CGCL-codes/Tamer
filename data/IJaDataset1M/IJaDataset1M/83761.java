package foamcutapp;

import edu.mit.aero.foamcut.FCEC;
import edu.mit.aero.foamcut.FCECException;
import edu.mit.aero.foamcut.Prefs;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;

/**
 *
 * @author mschafer
 */
public class ConnectTask extends Task {

    private Application application;

    ConnectTask(Application app) {
        super(app);
        application = app;
    }

    @Override
    protected Object doInBackground() throws Exception {
        String port = Prefs.fcecPort.get();
        if (port.contentEquals("Simulator") || port.isEmpty()) {
            message("noPortMessage");
            return null;
        } else {
            try {
                message("startMessage", port);
                FCEC.INSTANCE.connect(port);
                message("connectedMessage", port);
                return port;
            } catch (FCECException fex) {
                return fex;
            }
        }
    }

    private void testThrow() throws FCECException {
        throw (new FCECException("test"));
    }
}
