package tcdc.vgetty.actions;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import tcdc.vgetty.servlets.Constants;
import tcdc.vgetty.beans.Factory;
import tcdc.vgetty.beans.Messages;
import tcdc.vgetty.beans.VGettyException;

/**
 *  Handles actions for the DeleteMessages.vm page.
 *
 * @author     Neil Clayton
 * @created    June 6, 2001
 */
public class DeleteMessages extends VGettyAction {

    /**
	 *  Deletes the message files stored in the session attribute "toBeRemoved".
	 */
    public void doOk(RunData data, Context ctx) throws Exception {
        Messages msgs = Factory.getMessages();
        Vector removeThisLog = (Vector) data.getSession().getAttribute(Constants.toBeRemoved);
        for (java.util.Iterator iter = removeThisLog.iterator(); iter.hasNext(); ) {
            java.lang.String fileName = (java.lang.String) iter.next();
            try {
                msgs.removeFile(fileName);
            } catch (java.lang.Exception e) {
                throw new VGettyException("Error removing file: " + fileName, e);
            }
        }
        data.setMessage(removeThisLog.size() + " message(s) were removed");
        data.setScreenTemplate("Index.vm");
    }

    /**
	 *  Cancels the delete action, by just redirecting the user to the main page.
	 */
    public void doCancel(RunData data, Context ctx) throws Exception {
        data.setMessage("Messages were not deleted");
        data.setScreenTemplate("Index.vm");
    }
}
