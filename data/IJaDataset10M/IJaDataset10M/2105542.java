package DE.FhG.IGD.semoa.security;

import DE.FhG.IGD.semoa.server.Environment;
import DE.FhG.IGD.semoa.server.AgentFilter;
import DE.FhG.IGD.semoa.service.*;
import DE.FhG.IGD.semoa.server.*;
import DE.FhG.IGD.util.*;
import java.security.cert.*;
import java.security.*;
import java.util.*;
import java.io.*;

/**
 * This filter encrypts folders in the agent's structure
 * that is assigned to a valid access group. It uses the
 * {@link AgentEncryptor AgentEncryptor} for this purpose.
 *
 * @author Volker Roth
 * @version $Id: EncryptFilter.java 462 2001-08-21 18:21:00Z vroth $
 */
public class EncryptFilter extends AbstractFilter implements AgentFilter.Out {

    /**
     * Creates an instance of this filter.
     */
    public EncryptFilter() {
    }

    /**
     * This method does the encrypting. It is called by the
     * {@link OutGate outgate}. which takes care of wrapping
     * the agent before it is sent on its way.
     *
     * @return <code>OK</code> if the agent context passed
     *   the filter alright, and <code>REJECT</code> if the
     *   agent could not be encrypted.
     * @param ctx The agent context of the agent that is to be
     *   filtered.
     */
    public ErrorCode filter(AgentContext ctx) {
        X509Certificate cert;
        AgentEncryptor ae;
        PrivateKey key;
        KeyMaster km;
        Resource resource;
        String what;
        long time;
        try {
            time = System.currentTimeMillis();
            resource = (Resource) ctx.get(FieldType.RESOURCE);
            if (resource == null) {
                throw new NullPointerException("resource");
            }
            what = WhatIs.stringValue("KEYMASTER");
            km = (KeyMaster) getEnvironment().lookup(what);
            cert = (X509Certificate) km.getCertificate(KeyMaster.CRYPT_KEY);
            key = km.getPrivateKey(KeyMaster.CRYPT_KEY);
            ae = new AgentEncryptor(resource);
            if (ae.isPlain()) {
                return ErrorCode.OK;
            }
            ae.init();
            ae.validateRecipient(key, cert);
            ae.encrypt();
            updateStatistics(System.currentTimeMillis() - time);
        } catch (Exception e) {
            AgentCard card;
            String name;
            card = (AgentCard) ctx.get(FieldType.CARD);
            name = (card == null) ? null : card.toString();
            logMessage(null, e, name);
            return ErrorCode.REJECT;
        }
        return ErrorCode.OK;
    }

    public String info() {
        return "This filter encrypts outgoing agents.";
    }

    public String author() {
        return People.VROTH;
    }

    public String revision() {
        return "$Revision: 462 $/$Date: 2001-08-21 14:21:00 -0400 (Tue, 21 Aug 2001) $";
    }
}
