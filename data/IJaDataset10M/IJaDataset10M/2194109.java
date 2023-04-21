package cz.fi.muni.xkremser.editor.shared.rpc.action;

import java.util.List;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;
import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

/**
 * The Class GetRecentlyModified.
 */
@GenDispatch(isSecure = false)
@SuppressWarnings("unused")
public class RemoveDigitalObject extends UnsecuredActionImpl<RemoveDigitalObjectResult> {

    /** The uuid of the object is going to be removed */
    @In(1)
    private String uuid;

    /** The list of uuid of digital object which has not to be removed */
    @In(2)
    private List<String> uuidNotToRemove;

    /**
     * The message about the failure, <code>errorMessage == null</code> when
     * there hasn't been any failure
     */
    @Out(1)
    private String errorMessage;

    /** The list of uuid which has been removed */
    @Out(2)
    private List<String> removed;
}
