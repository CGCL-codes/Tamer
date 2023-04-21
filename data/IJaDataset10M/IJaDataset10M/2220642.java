package net.infordata.ifw2.web.menu;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.infordata.ifw2.web.DialogResultEnum;
import net.infordata.ifw2.web.bnds.ActionInfo;
import net.infordata.ifw2.web.bnds.IBndsFlow;
import net.infordata.ifw2.web.bnds.IFieldAction;
import net.infordata.ifw2.web.bnds.IFormAction;
import net.infordata.ifw2.web.ctrl.FlowContext;
import net.infordata.ifw2.web.ctrl.IFlowState;
import net.infordata.ifw2.web.mpart.UploadedFile;

/**
 * @author valentino.proietti
 */
public abstract class APopupMenuAction implements IFieldAction, IFormAction {

    private static final long serialVersionUID = 1L;

    public APopupMenuAction() {
    }

    @Override
    public IFlowState execute(IBndsFlow flow, HttpServletRequest request, String formName, ActionInfo action, Map<String, UploadedFile> files) {
        execute(flow, request, formName, null, action);
        return null;
    }

    @Override
    public void execute(IBndsFlow flow, HttpServletRequest request, String formName, String fieldName, ActionInfo action) {
        final PopupMenuFlow mFlow = createPopupMenuFlow();
        fillPopup(mFlow, action.getParameter());
        MenuStripe stripe = mFlow.getMenuStripe();
        stripe.onOpening();
        if (stripe.getItems().isEmpty()) return;
        if (action.getClientParameter("clientX") != null) {
            int clientX = Integer.valueOf(action.getClientParameter("clientX")[0]);
            int clientY = Integer.valueOf(action.getClientParameter("clientY")[0]);
            FlowContext.get().popup(mFlow, mFlow.getPopupStyles(), clientX, clientY, null);
        } else {
            FlowContext.get().popup(mFlow, mFlow.getPopupStyles(), fieldName, null);
        }
    }

    /**
   * @return a {@link PopupMenuFlow} subclass. 
   */
    protected PopupMenuFlow createPopupMenuFlow() {
        return new DefaultPopupFlow();
    }

    /**
   * Define to register actions and strokes.<br>
   * If an action execution must end with a closed popup, then
   * return (in the action) {@link DialogResultEnum#CONTINUE}. 
   * @param menuFlow
   * @param parameter - the parameter received by the action.
   */
    protected abstract void fillPopup(PopupMenuFlow menuFlow, String parameter);

    @Override
    public String getLabel() {
        return Messages.getString("APopupMenuAction.0");
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
