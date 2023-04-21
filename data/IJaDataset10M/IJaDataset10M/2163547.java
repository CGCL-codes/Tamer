package org.personalsmartspace.cm.broker.api.platform.identity;

import org.personalsmartspace.cm.model.api.pss3p.ICtxIdentifier;
import org.personalsmartspace.spm.access.api.platform.IAccessControl;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;

/**
 * {@link IAccessControl} interface containing extra methods for use
 * in test setup.
 * 
 * Will be redundant when proper IAccessControl implementation is complete.
 * 
 * @author <a href="mailto:phdn@users.sourceforge.net">phdn</a>
 */
public interface ICtxAccessControl extends IAccessControl {

    /**
	 * Adds read permission for a dpi to a context identifier.
	 * 
	 * @param ctxIdentifier The private context identifier.
	 * @param dpi The associated requester digital personal identifier. 
	 */
    void addReadPermission(ICtxIdentifier ctxIdentifier, IDigitalPersonalIdentifier dpi);
}
