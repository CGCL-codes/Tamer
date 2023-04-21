package cz.fi.muni.xkremser.editor.shared.rpc.action;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;
import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;
import cz.fi.muni.xkremser.editor.shared.rpc.RoleItem;

/**
 * The Class GetRecentlyModified.
 */
@GenDispatch(isSecure = false)
@SuppressWarnings("unused")
public class PutUserRole extends UnsecuredActionImpl<PutUserRoleResult> {

    /** The role. */
    @In(1)
    private RoleItem role;

    /** The user id. */
    @In(2)
    private String userId;

    /** The id. */
    @Out(1)
    private String id;

    /** The found. */
    @Out(2)
    private boolean found;

    /** The description. */
    @Out(3)
    private String description;
}
