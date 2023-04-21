package cz.fi.muni.xkremser.editor.shared.rpc.action;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;
import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

/**
 * @author Jiri Kremser
 * @version $Id$
 */
@GenDispatch(isSecure = false)
@SuppressWarnings("unused")
public class UnlockDigitalObject extends UnsecuredActionImpl<UnlockDigitalObjectResult> {

    /** The uuid of the digital object */
    @In(1)
    private String uuid;

    /** Whether the locking was successful */
    @Out(1)
    private boolean successful;
}
