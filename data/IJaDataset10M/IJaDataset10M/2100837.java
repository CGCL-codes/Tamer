package dmeduc.reference.securityrole;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.IDomainModel;

/**
 * SecurityRole specific entity.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-11-15
 */
public class SecurityRole extends GenSecurityRole {

    private static final long serialVersionUID = 1176319603402L;

    private static Log log = LogFactory.getLog(SecurityRole.class);

    /**
	 * Constructs securityRole within the domain model.
	 * 
	 * @param model
	 *            model
	 */
    public SecurityRole(IDomainModel model) {
        super(model);
    }
}
