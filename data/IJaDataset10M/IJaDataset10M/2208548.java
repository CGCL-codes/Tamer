package twoadw.reference.securityrole;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.IDomainModel;

/**
 * SecurityRole specific entities.
 * 
 * @author TeamFcp
 * @version 2009-03-16
 */
public class SecurityRoles extends GenSecurityRoles {

    private static final long serialVersionUID = 1236722534896L;

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
