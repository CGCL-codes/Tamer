package net.infordata.ifw2.web;

import java.io.Serializable;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.infordata.ifw2.web.bnds.ActionInfo;
import net.infordata.ifw2.web.bnds.IBndsFlow;
import net.infordata.ifw2.web.bnds.IFormAction;
import net.infordata.ifw2.web.ctrl.IFlowEndState;
import net.infordata.ifw2.web.ctrl.IFlowState;
import net.infordata.ifw2.web.mpart.UploadedFile;

/**
 * An action which ends a flow with the provided result. 
 * @author valentino.proietti
 */
public abstract class AResultAction implements IFormAction, Serializable {

    private static final long serialVersionUID = 1L;

    private final IFlowEndState ivResult;

    public AResultAction(IFlowEndState res) {
        ivResult = res;
    }

    public IFlowEndState getResult() {
        return ivResult;
    }

    @Override
    public abstract String getLabel();

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
   * @return true if the action can be executed, in this case it ends up
   *    in a flow state switch.
   */
    public boolean onBeforeExecute(ActionInfo action) {
        return true;
    }

    @Override
    public final IFlowState execute(IBndsFlow flow, HttpServletRequest request, String formName, ActionInfo action, Map<String, UploadedFile> files) {
        return onBeforeExecute(action) ? ivResult : null;
    }
}
