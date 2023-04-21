package cz.fi.muni.xkremser.editor.shared.rpc.action;

import java.util.Date;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;
import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

/**
 * The Class GetClientConfig.
 */
@GenDispatch(isSecure = false)
@SuppressWarnings("unused")
public class GetDescription extends UnsecuredActionImpl<GetDescriptionResult> {

    /** The config. */
    @In(1)
    private String uuid;

    /** The description. */
    @Out(1)
    private String description;

    /** The user description. */
    @Out(2)
    private String userDescription;

    /** The modified. */
    @Out(3)
    private Date modified;
}
