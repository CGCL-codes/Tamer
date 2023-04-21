package uk.gov.dti.og.fox.command;

import uk.gov.dti.og.fox.command.AbstractCommand;
import uk.gov.dti.og.fox.ex.ExInternal;
import uk.gov.dti.og.fox.dom.DOM;
import uk.gov.dti.og.fox.dom.DOMList;
import uk.gov.dti.og.fox.XThread;
import uk.gov.dti.og.fox.ex.ExActionFailed;
import uk.gov.dti.og.fox.ex.ExModule;
import uk.gov.dti.og.fox.ContextUElem;
import uk.gov.dti.og.fox.Mod;
import uk.gov.dti.og.fox.ContextUCon;

/** 
* Simple command that removes DOM Nodes
*
* @author Jason Brown
*/
public class RemoveCommand extends AbstractCommand {

    private String mMatch;

    /**
  * Contructs the command from the XML element specified.
  *
  * @param commandElement the element from which the command will
  *        be constructed.
  */
    public RemoveCommand(Mod pMod, DOM commandElement) throws ExInternal, ExModule {
        super(commandElement);
        parseCommand(pMod, commandElement);
    }

    /**
  * Parses the command structure. Relies on the XML Schema to
  * ensure the command adheres to the required format.
  *
  * @param commandElement the element from which the command will
  *        be constructed.
  */
    public void parseCommand(Mod pMod, DOM commandElement) throws ExInternal, ExModule {
        if (!commandElement.hasAttr("match")) {
            throw new ExModule("<remove> command with no match clause", commandElement);
        }
        mMatch = commandElement.getAttr("match");
        if (mMatch.equals("")) {
            throw new ExModule("<remove> command with match=\"\" clause", commandElement);
        }
    }

    /**
  * Runs the command with the specified user thread and session.
  *
  * @param userThread the user thread context of the command
  * @return userSession the user's session context
  */
    public void run(XThread pXThread, ContextUElem pContextUElem, ContextUCon pContextUCon) throws ExInternal, ExActionFailed {
        DOMList lRemoveTarget = pContextUElem.extendedXPathUL(mMatch, ContextUElem.ATTACH);
        if (lRemoveTarget.treesContain(pContextUElem.getUElem(ContextUElem.ATTACH))) {
            throw new ExActionFailed("BADPATH", "<remove match=\"+mMatch+\"> command attempting to remove current attach point", pContextUElem.getUElem(ContextUElem.ATTACH));
        }
        lRemoveTarget.removeFromDOMTree();
    }

    public boolean isCallTransition() {
        return false;
    }
}
