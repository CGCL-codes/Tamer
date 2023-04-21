package org.slasoi.businessManager.businessEngine.poc;

import java.util.List;
import org.slasoi.businessManager.businessEngine.poc.impl.NegotiationSession;

public interface IManualNegotiation {

    /**
     * Return the Current active manual Sessions;
     * @param type
     * @return
     */
    public List<NegotiationSession> getCurrentNegotiations(String type);

    /**
     * Return the required NegotiationSesion
     * @param id
     * @return
     */
    public NegotiationSession getNegotiation(String id);
}
