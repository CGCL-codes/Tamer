package travel.reference.securityrole;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.IDomainModel;

/**
 * SecurityRole specific entities.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-12-04
 */
public class SecurityRoles extends GenSecurityRoles {

    private static final long serialVersionUID = 1176319603403L;

    private static Log log = LogFactory.getLog(SecurityRoles.class);

    /**
	 * Constructs securityRoles within the domain model.
	 * 
	 * @param model
	 *            model
	 */
    public SecurityRoles(IDomainModel model) {
        super(model);
    }
}
