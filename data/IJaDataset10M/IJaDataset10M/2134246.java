package uk.gov.dti.og.fox.command;

import uk.gov.dti.og.fox.XThread;
import uk.gov.dti.og.fox.Mod;
import uk.gov.dti.og.fox.ContextUElem;
import uk.gov.dti.og.fox.ContextUCon;
import uk.gov.dti.og.fox.dom.DOM;
import uk.gov.dti.og.fox.State;
import uk.gov.dti.og.fox.XDo;
import uk.gov.dti.og.fox.XThread.CallStackTransformation;
import uk.gov.dti.og.fox.ex.ExInternal;
import uk.gov.dti.og.fox.ex.ExActionFailed;
import java.util.SortedMap;
import java.util.Iterator;
import uk.gov.dti.og.fox.track.Track;

/** 
* Implementation of a FOX command that performs push, pop and
* replace state manipulation.
*
* @author Gary Watson
*/
public final class StateCommand extends Track implements Command {

    public final String mStateName;

    public final String mAttach;

    public final boolean mReplaceAll;

    public final String mOperationIntern;

    private final Mod mMod;

    private State mState;

    /**
   * Contructs a StateCommand command from the XML element specified.
   *
   * @param commandElement the element from which the command will
   *        be constructed.
   */
    public StateCommand(Mod pMod, DOM pCommandDOM) throws ExInternal {
        String lName = pCommandDOM.getLocalName();
        if (lName.equals("state-push") || lName.equals("state-replace") || lName.equals("state-pop") || lName.equals("state-strict-pop")) {
            mOperationIntern = lName.substring(6).intern();
        } else {
            mOperationIntern = pCommandDOM.getAttrOrNull("action").intern();
        }
        mStateName = pCommandDOM.getAttrOrNull("name");
        mAttach = pCommandDOM.getAttrOrNull("attach");
        mMod = pMod;
        mReplaceAll = Boolean.valueOf(pCommandDOM.getAttrOrNull("all")).booleanValue();
    }

    /**
   * Runs the command with the specified user thread and session.
   *
   * @param userThread the user thread context of the command
   * @return userSession the user's session context
   */
    public void run(XThread pXThread, ContextUElem pContextUElem, ContextUCon pContextUCon) throws ExInternal, ExActionFailed, CallStackTransformation {
        pXThread.runStateFinalActions(pContextUElem, pContextUCon);
        if (mOperationIntern == "replace") {
            pXThread.stateReplace(this, pContextUElem);
        } else if (mOperationIntern == "push") {
            pXThread.statePush(this, pContextUElem);
        } else if (mOperationIntern == "pop" || mOperationIntern == "strict-pop") {
            pXThread.statePop(this, pContextUElem);
        } else {
            throw new ExInternal("fm:state error");
        }
        pXThread.runStateInitActions(pContextUElem, pContextUCon);
    }

    /**
   * Called to allow the component to validate it's syntax and structure.
   *
   * <p>Although XML Schema provides for the basic validation of the command,
   * there is some validation (cross references with the Module/App) that
   * can only be done by the component itself.
   *
   * @param module the module where the component resides
   * @param commandElement the XML element that comprises the command
   * @throws ExInternal if the component syntax is invalid.
   */
    public void validate(Mod module) throws ExInternal {
        if (mOperationIntern != "replace" && mOperationIntern != "push" && mOperationIntern != "pop" && mOperationIntern != "strict-pop") {
            throw new ExInternal("A fm:state within module " + module.getName() + " has no push, pop or replace");
        }
        if (mOperationIntern == "push" || mOperationIntern == "replace") {
            getState();
        }
    }

    /**
   * Gets the named state from the module.
   *
   * @throws ExInternal if the state specified does not exist
   */
    public State getState() {
        if (mState == null) {
            mState = mMod.getState(mStateName);
        }
        return mState;
    }

    public boolean isCallTransition() {
        return mOperationIntern == "pop";
    }
}
