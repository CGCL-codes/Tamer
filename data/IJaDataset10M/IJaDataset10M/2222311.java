package wesodi.logic.session;

import java.io.Serializable;
import javax.ejb.Remote;
import wesodi.entities.transi.ServerRequest;
import wesodi.entities.transi.ServerResponse;

/**
 * The Interface UserSessionRemote.
 * 
 * @author Sarah Haselbauer
 * @date 05.03.2009
 */
@Remote
public interface UserSessionRemote extends Serializable {

    public static final long serialVersionUID = -2052487647100781548L;

    public static final String ERROR_DATABASE_RECORD_MISSING = "err_" + serialVersionUID + ".003";

    /**
	 * Error Codecs Convention:
	 * err_[component_serial_version_uid].[unique_error_number]
	 */
    public static final String ERROR_LDAP_AUTHENTICATION_FAILED = "err_" + serialVersionUID + ".001";

    public static final String ERROR_LOGIN_ID_OR_PW_MISSING = "err_" + serialVersionUID + ".002";

    public static final String ERROR_UNEXPECTED = "err_" + serialVersionUID + ".000";

    public ServerResponse login(ServerRequest request);

    public ServerResponse logout();

    /**
	 * Handles a request from UI.
	 * 
	 * @param request
	 *            the {@link ServerRequest} with a parameter list and containing
	 *            the handler (simple class name) and the concrete method to
	 *            invoke
	 * @return {@link ServerResponse} with the result or some errors
	 */
    public ServerResponse handle(ServerRequest request);
}
