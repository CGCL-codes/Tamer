package uk.gov.dti.og.fox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import uk.gov.dti.og.fox.XThread.CallStackTransformation;
import uk.gov.dti.og.fox.command.AbstractCommand;
import uk.gov.dti.og.fox.command.Command;
import uk.gov.dti.og.fox.command.CommandFactory;
import uk.gov.dti.og.fox.dom.DOM;
import uk.gov.dti.og.fox.dom.DOMList;
import uk.gov.dti.og.fox.ex.ExActionFailed;
import uk.gov.dti.og.fox.ex.ExDoSyntax;
import uk.gov.dti.og.fox.ex.ExInternal;
import uk.gov.dti.og.fox.ex.ExModule;
import uk.gov.dti.og.fox.xhtml.DisplayItemAttrs;

public class XDo extends AbstractCommand {

    Command[] mCommandArray;

    HashMap f_namespace_2_ht_attrs = new HashMap();

    public static final String G_SERVER_SIDE_ACTION = "SERVER";

    public static final String G_CLIENT_SIDE_ACTION = "CLIENT";

    private static final String M_ACTION_TYPE_ATTRIBUTE = "type";

    private static final HashMap M_ATTR_VAL_TO_TYPE_MAP = new HashMap();

    static {
        M_ATTR_VAL_TO_TYPE_MAP.put("server-side", G_SERVER_SIDE_ACTION);
        M_ATTR_VAL_TO_TYPE_MAP.put("client-side", G_CLIENT_SIDE_ACTION);
    }

    private final String mActionName;

    private final State mState = null;

    public final boolean mApplyFlag;

    private String mActionType;

    private boolean mValidated = false;

    public XDo(Mod pMod, DOM edo, String pActionName, boolean pApplyFlag, HashMap attributes) throws ExInternal, ExDoSyntax {
        super(edo);
        mApplyFlag = pApplyFlag;
        HashMap foxmap = (HashMap) attributes.get("fox");
        if (foxmap != null) {
            attributesMap = foxmap;
        }
        buildCmdList(pMod, edo);
        f_namespace_2_ht_attrs = attributes;
        mActionName = pActionName;
        resolveActionType(attributes);
    }

    public XDo(Mod pMod, DOM edo) throws ExInternal, ExDoSyntax {
        super(edo);
        buildCmdList(pMod, edo);
        mActionName = "(Unnamed Command List)";
        mApplyFlag = false;
        mActionType = G_SERVER_SIDE_ACTION;
    }

    public XDo(DOM pEvalCmd, Command[] pCommandArray) throws ExInternal, ExDoSyntax {
        super(pEvalCmd);
        mCommandArray = pCommandArray;
        mActionName = "fm:eval";
        mApplyFlag = false;
        mActionType = G_SERVER_SIDE_ACTION;
    }

    private void buildCmdList(Mod pMod, DOM edo) throws ExInternal, ExDoSyntax {
        DOMList cmd_list = edo.getChildElements();
        int size = cmd_list.getLength();
        mCommandArray = new Command[size];
        for (int i = 0; i < size; i++) {
            DOM ecmd = cmd_list.item(i);
            Command cmd = CommandFactory.getInstance().getCommand(pMod, ecmd);
            mCommandArray[i] = cmd;
        }
    }

    /**
   * Set the action type based upon the type attribute existance.
   * 
   * @param pAttributes - The hash of namespaces to attributes
   * @throws ExInternal - thrown if unknown action type
   */
    private void resolveActionType(HashMap pAttributes) throws ExInternal {
        HashMap lNonNsAttrs = (HashMap) pAttributes.get("");
        String lActionType = (String) lNonNsAttrs.get(M_ACTION_TYPE_ATTRIBUTE);
        if (lActionType == null || lActionType.equals("")) {
            mActionType = G_SERVER_SIDE_ACTION;
        } else {
            mActionType = (String) M_ATTR_VAL_TO_TYPE_MAP.get(lActionType);
        }
        if (mActionType == null) {
            throw new ExInternal("Invlaid action type (" + lActionType + ") specified on action " + mActionName + ".");
        }
    }

    public HashMap getAttributes() {
        return f_namespace_2_ht_attrs;
    }

    /**
   * Validates that all commands are valid.
   *
   * @param module the module where the component resides
   * @throws ExInternal if the component syntax is invalid.
   */
    public void validate(Mod module, DOM commandElement) throws ExInternal {
        if (mValidated) {
            return;
        }
        int size = mCommandArray.length;
        for (int i = 0; i < size; i++) {
            mCommandArray[i].validate(module);
            if (i != size - 1 && mCommandArray[i].isCallTransition()) {
                throw new ExInternal("A Call Stack Transtion command (one of fm:call-module, fm:exit-module, fm:state-pop) is followed by an unreachable command.\n" + "CST Command: " + mCommandArray[i].toString() + "\n" + "Unreachable Command: " + mCommandArray[i + 1].toString() + "\n" + "Path: " + mActionName + "\n");
            }
        }
        mValidated = true;
    }

    public void run(XThread pXThread, ContextUElem pContextUElem, ContextUCon pContextUCon) throws ExInternal, ExActionFailed, CallStackTransformation {
        pXThread.trackPush("do_block").setAttribute("actionname", mActionName);
        try {
            int size = mCommandArray.length;
            for (int i = 0; i < size; i++) {
                pXThread.trackPush("FoxCommand").addChildContent(mCommandArray[i]);
                try {
                    mCommandArray[i].run(pXThread, pContextUElem, pContextUCon);
                } catch (CallStackTransformation lCallStackTransformation) {
                    if (size != i + 1) {
                        throw new ExInternal("Command not reachable after Call Stack Transformation: " + (mCommandArray[i + 1].toString()));
                    }
                    throw lCallStackTransformation;
                } catch (ExInternal e) {
                    throw new ExInternal(mCommandArray[i].toString(), e);
                } finally {
                    pXThread.trackPop();
                }
            }
        } finally {
            pXThread.trackPop();
        }
    }

    public final DisplayItemAttrs getDisplayItemAttrs(DOM pMenuoutCommand, State pState, List pModeList) throws ExInternal {
        List lKeyList = new ArrayList();
        lKeyList.add(this);
        lKeyList.add(pMenuoutCommand.getAttr("cmd.id"));
        lKeyList.addAll(pModeList);
        lKeyList.add(pState);
        DisplayItemAttrs lDisplayItemAttrs = (DisplayItemAttrs) DisplayItemAttrs.gCachedDispItemAttrsMap.get(lKeyList);
        if (lDisplayItemAttrs == null) {
            lDisplayItemAttrs = new DisplayItemAttrs(pState, this, pMenuoutCommand, (ArrayList) pModeList);
            DisplayItemAttrs.gCachedDispItemAttrsMap.put(lKeyList, lDisplayItemAttrs);
            DisplayItemAttrs.gDIAMissCounter.inc();
        } else {
            DisplayItemAttrs.gDIAHitCounter.inc();
        }
        return lDisplayItemAttrs;
    }

    public final String getName() {
        return mActionName;
    }

    public final String getType() {
        return mActionType;
    }

    public static boolean addAutoAction(String actionName, XDo xdo, SortedMap autoActionInit, SortedMap autoActionFinal, SortedMap autoStateInit, SortedMap autoStateFinal, SortedMap autoCallbackInit, SortedMap autoCallbackFinal) throws ExModule {
        if (!actionName.startsWith("auto-")) {
            return false;
        }
        if (actionName.startsWith("action-init", 5)) {
            autoActionInit.put(actionName, xdo);
        } else if (actionName.startsWith("action-final", 5)) {
            autoActionFinal.put(actionName, xdo);
        } else if (actionName.startsWith("state-init", 5)) {
            autoStateInit.put(actionName, xdo);
        } else if (actionName.startsWith("state-final", 5)) {
            autoStateFinal.put(actionName, xdo);
        } else if (actionName.startsWith("callback-init", 5)) {
            autoCallbackInit.put(actionName, xdo);
        } else if (actionName.startsWith("callback-final", 5)) {
            autoCallbackFinal.put(actionName, xdo);
        } else {
            throw new ExModule("Invlaid auto- action: " + actionName + " Needs to start with [auto-action-init,auto-action-final,auto-state-init,auto-state-final]");
        }
        return true;
    }

    public boolean isCallTransition() {
        for (int i = 0; i < mCommandArray.length; i++) {
            if (mCommandArray[i].isCallTransition()) {
                return true;
            }
        }
        return false;
    }
}
