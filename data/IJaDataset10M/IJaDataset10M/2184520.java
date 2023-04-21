package org.adapit.wctoolkit.uml.ext.sequence;

import java.io.Serializable;
import org.adapit.wctoolkit.uml.ext.core.IElement;
import org.adapit.wctoolkit.uml.ext.core.ElementImpl;
import org.adapit.wctoolkit.uml.ext.core.NamedElement;

@SuppressWarnings({ "serial" })
public class ReturnMessage extends ElementImpl implements IElement, Serializable {

    /**
	 * 
	 */
    public ReturnMessage() {
        super();
    }

    /**
	 * @param parent
	 */
    public ReturnMessage(NamedElement parent) {
        super(parent);
    }

    protected Method caller;

    protected Method receiver;

    /**
	 * @return Returns the caller.
	 */
    public Method getCaller() {
        return caller;
    }

    /**
	 * @param caller The caller to set.
	 */
    public void setCaller(Method caller) {
        this.caller = caller;
    }

    /**
	 * @return Returns the receiver.
	 */
    public Method getReceiver() {
        return receiver;
    }

    /**
	 * @param receiver The receiver to set.
	 */
    public void setReceiver(Method receiver) {
        this.receiver = receiver;
    }
}
