package net.infordata.ifw2.web.bnds;

/**
 * This action is used to trigger script execution on the client side. 
 * @author valentino.proietti
 */
public interface IClientSideAction extends IAction {

    /**
   * The script receives the following variables when invoked in the browser:
   * <pre>
   * stroke - this is the trigger of the action, can be a MouseStroke or a KeyStroke
   * form   - the form binded to the action
   * subFormName - the subFormName (for nested forms support)
   * target - the target of the event
   * field  - if the target of the event is a field
   * </pre>
   */
    public String getScript(String parameter);
}
