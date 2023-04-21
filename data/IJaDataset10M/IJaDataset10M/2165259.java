package de.iritgo.aktera.servers;

import de.iritgo.aktera.model.ModelException;
import de.iritgo.aktera.model.ModelRequest;
import de.iritgo.aktera.model.ModelResponse;

/**
 * @author root
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class KeelServerThread extends Thread {

    private ModelRequest myRequest = null;

    private ModelResponse myResponse = null;

    private ModelException myException = null;

    public void setRequest(ModelRequest req) {
        myRequest = req;
    }

    public void run() {
        try {
            myResponse = myRequest.execute();
        } catch (ModelException me) {
            myException = me;
        }
    }

    public ModelResponse getResponse() throws ModelException {
        if (myException != null) {
            throw myException;
        }
        return myResponse;
    }
}
