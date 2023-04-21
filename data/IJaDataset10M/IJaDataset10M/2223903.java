package org.hardtokenmgmt.core.ejbca.wrappers;

import java.security.cert.Certificate;
import java.util.Collection;
import org.ejbca.core.model.log.Admin;
import org.ejbca.core.model.ra.ExtendedInformation;

/**
 * 
 * Publisher related Wrapper methods to use with multiple versions of EJBCA
 * 
 * @author Philip Vendil 23 Jan 2010
 *
 * @version $Id$
 */
public interface IPublisher extends IEJBCAWrapper {

    boolean storeCertificate(Admin admin, Collection<Integer> publisherids, Certificate incert, String username, String password, String cafp, int status, int type, long revocationDate, int revocationReason, ExtendedInformation extendedinformation);
}
