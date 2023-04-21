package cz.fi.muni.xkremser.editor.shared.rpc.action;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;
import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;
import cz.fi.muni.xkremser.editor.shared.rpc.OpenIDItem;

/**
 * The Class GetRecentlyModified.
 */
@GenDispatch(isSecure = false)
@SuppressWarnings("unused")
public class PutUserIdentity extends UnsecuredActionImpl<PutUserIdentityResult> {

    /** The identity. */
    @In(1)
    private OpenIDItem identity;

    /** The user id. */
    @In(2)
    private String userId;

    /** The id. */
    @Out(1)
    private String id;

    /** The found. */
    @Out(2)
    private boolean found;
}
