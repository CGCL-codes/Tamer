package org.itsnat.impl.comp.button;

import org.itsnat.impl.comp.ItsNatHTMLFormCompValueBasedImpl;
import org.itsnat.impl.comp.button.ItsNatButtonInternal;
import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public class ItsNatHTMLFormCompButtonSharedImpl {

    protected ItsNatHTMLFormCompValueBasedImpl comp;

    /**
     * Creates a new instance of ItsNatHTMLFormCompButtonSharedImpl
     */
    public ItsNatHTMLFormCompButtonSharedImpl(ItsNatHTMLFormCompValueBasedImpl comp) {
        this.comp = comp;
    }

    public boolean handleEvent(Event evt) {
        boolean res;
        ItsNatButtonInternal compButton = (ItsNatButtonInternal) comp;
        comp.setServerUpdatingFromClient(true);
        try {
            res = compButton.getItsNatButtonShared().handleEvent(evt);
        } finally {
            comp.setServerUpdatingFromClient(false);
        }
        return res;
    }
}
