package net.infordata.ifw2.web.ctrl;

import java.io.Serializable;

/**
 * If implemented by an IFlow it is ensured that no other call-backs can be used it.  
 *  
 * @author valentino.proietti
 */
public interface IDialogCallback extends Serializable {

    /**
   * 
   * @param flow - the flow in which the modal dialog has been started
   * @param dialog - the modal dialog
   * @param res - the ending state of the dialog flow
   * @return the flow state to switch to on dialog end, null if none.
   */
    public IFlowState endDialog(IFlow flow, IFlowAsDialog dialog, IFlowEndState res);
}
