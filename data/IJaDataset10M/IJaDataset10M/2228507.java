package gridrm.onto.messaging;

/**
   * Base class for all action failures.
* Protege name: ActionFailure
* @author ontology bean generator
* @version 2009/01/30, 15:01:03
*/
public class ActionFailure extends ActionResult {

    /**
   * Message describing type of failure.
* Protege name: failureMsg
   */
    private String failureMsg;

    public void setFailureMsg(String value) {
        this.failureMsg = value;
    }

    public String getFailureMsg() {
        return this.failureMsg;
    }
}
