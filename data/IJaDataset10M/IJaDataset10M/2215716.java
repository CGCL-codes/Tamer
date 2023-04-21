package org.atricore.idbus.kernel.main.mediation.claim;

import org.atricore.idbus.kernel.main.mediation.Channel;
import java.io.Serializable;

/**
 *
 * @author <a href="mailto:gbrigand@josso.org">Gianluca Brigandi</a>
 * @version $Rev: 1278 $ $Date: 2009-06-14 03:14:41 -0300 (Sun, 14 Jun 2009) $
 */
public interface ClaimsResponse extends Serializable {

    String getId();

    String getRelayState();

    Channel getIssuer();

    String getInResponseTo();

    ClaimSet getClaimSet();
}
