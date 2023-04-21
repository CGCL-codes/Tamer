package org.mitre.mrald.control;

import java.util.HashMap;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.ParseXML;
import org.mitre.mrald.util.Snoop;

/**
 *  This class provides an entry into the Mrald architecture. <br>
 *  Use: Instantiate a new MraldEntry class, using a pre-filled MsgObject, and
 *  call the runMrald() or buildQuery() methods. Alternately, instantiate a new
 *  MraldEntry class with the bare constructor, set a user name and add name
 *  value pairs using the provided methods, then call runMrald().
 *
 *@author     jchoyt
 *@created    May 23, 2002
 */
public class MraldEntry {

    MsgObject msg;

    /**
     *  Constructor for the MraldEntry object. Sets the MsgObject to the passed
     *  parameter.
     *
     *@param  passed_msg  Description of Parameter
     */
    public MraldEntry(MsgObject passed_msg) {
        msg = passed_msg;
    }

    /**
     *  Constructor for the MraldEntry object. Sets the MsgObject to a new
     *  MsgObject. If you use this constructor, you must pass in the name/value
     *  pairs before you use runMrald().
     */
    public MraldEntry() {
        msg = new MsgObject();
    }

    /**
     *  Sets the user attribute of the MraldEntry object. Set this so the
     *  username in the log files will not be null.
     *
     *@param  username  The new user value
     */
    public void setUser(String username) {
        msg.setUserId(username);
    }

    /**
     *  Adds a feature to the NvPair attribute of the MraldEntry object
     *
     *@param  name   The feature to be added to the NvPair attribute
     *@param  value  The feature to be added to the NvPair attribute
     */
    public void addNvPair(String name, String value) {
        msg.setValue(name, value);
    }

    /**
     *  Runs an MRALD session with the current MsgObject.
     *
     *@exception  WorkflowStepException  Description of Exception
     */
    public void runMrald() throws WorkflowStepException {
        try {
            Snoop.logParameters(msg);
            ParseXML mraldParser = new ParseXML(Config.getProperty("XMLFILE"));
            String workflow = msg.getValue("workflow")[0];
            if (workflow == null) {
                workflow = "Building SQL";
            }
            MiscUtils.logWorkFlow(msg.getUserUrl(), workflow);
            mraldParser.setWfPath(workflow);
            HashMap list = mraldParser.ProcessXML();
            WfController controller = new WfController(msg);
            controller.setWfObjects(list);
            controller.processWorkFlow();
        } catch (java.io.IOException e) {
            throw new WorkflowStepException(e.getMessage());
        } catch (MraldException e) {
            throw new WorkflowStepException(e.getMessage());
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            throw new WorkflowStepException(e.getMessage());
        } catch (org.mitre.mrald.util.MraldParseException e) {
            throw new WorkflowStepException(e.getMessage());
        }
    }

    /**
     *  Description of the Method
     *
     *@return                            Description of the Returned Value
     *@exception  WorkflowStepException  Description of Exception
     */
    public String buildQuery() throws WorkflowStepException {
        msg.setValue("workflow", "BuildQuery");
        runMrald();
        return msg.getQuery()[0];
    }
}
