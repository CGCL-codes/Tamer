package tw.idv.cut7man.cuttle.util;

import tw.idv.cut7man.cuttle.core.ConfigureDigester;
import tw.idv.cut7man.cuttle.helper.ActionHelper;
import tw.idv.cut7man.cuttle.vo.XMLAction;

public class ActionUtil {

    private ActionUtil() {
    }

    public static XMLAction getCurrentAction(ActionHelper helper) throws Exception {
        XMLAction action = (XMLAction) helper.getProperties("CUTTLE_ACTION");
        if (action == null) {
            action = ConfigureDigester.getActionFromMap(helper.getActionStr());
            XMLAction newAction = new XMLAction();
            newAction.setAjaxExtend(action.getAjaxExtend());
            newAction.setDescription(action.getDescription());
            newAction.setExtend(action.getExtend());
            newAction.setForward(action.getForward());
            newAction.setFunctionGroup(action.getFunctionGroup());
            newAction.setFunctionName(action.getFunctionName());
            newAction.setName(action.getName());
            newAction.setNonAuth(action.getNonAuth());
            newAction.setOutput(action.getOutput());
            newAction.setResponsewrap(action.getResponsewrap());
            helper.addProperties("CUTTLE_ACTION", newAction);
        }
        return action;
    }

    public static void changeOutput(ActionHelper helper, String output) throws Exception {
        XMLAction action = getCurrentAction(helper);
        action.setOutput(output);
        helper.addProperties("CUTTLE_ACTION", action);
    }

    public static void setScript(ActionHelper helper, String script) throws Exception {
        XMLAction action = getCurrentAction(helper);
        action.setScript(script);
        helper.addProperties("CUTTLE_ACTION", action);
    }
}
