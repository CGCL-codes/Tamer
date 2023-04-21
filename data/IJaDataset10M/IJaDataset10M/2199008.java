package org.tripcom.api.execution;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Set;
import java.net.URI;
import org.apache.log4j.Logger;
import org.openrdf.model.Statement;
import org.tripcom.api.auxiliary.Pair;
import org.tripcom.integration.exception.*;

/**
 * This is a implementation of the "string" API This means that the parameters
 * and returns will be serialized into strings (or integers). Furthermore every
 * alongside data (as security info) has to be provided in function calls, not
 * in instantiation of the class
 * 
 * @author Jan-Ole Christian
 * 
 */
public class StringCoreAPIImplementation {

    private static Logger log = Logger.getLogger(StringCoreAPIImplementation.class);

    public String securityCertificate;

    public String securityCookie;

    public String[] securityAssertions;

    public StringCoreAPIImplementation(String certificate, String cookie, String[] assertions) {
        securityCertificate = certificate;
        securityCookie = cookie;
        securityAssertions = assertions;
    }

    public String[] out(String rdfxmlStatement, String spaceURI, boolean synchrone, String signature) throws TSAPIException {
        return this.out(rdfxmlStatement, spaceURI, null, synchrone, signature);
    }

    public String[] out(String rdfxmlStatement, String spaceURI, URI transactionID, boolean synchrone, String signature) throws TSAPIException {
        String newCookie;
        CoreAPIImplementation coreAPIImplementation = new CoreAPIImplementation(securityCertificate, securityCookie, securityAssertions);
        try {
            Set<Statement> statementSet = RDFWrapper.getStatementsFromString(rdfxmlStatement);
            Statement statement = null;
            if (statementSet.size() == 1) {
                Iterator<Statement> it = statementSet.iterator();
                statement = it.next();
            } else {
                log.error("StringCoreAPIImplementation: more ore less than one triple provided, statementSet has wrong size");
                throw new java.lang.RuntimeException("StringCoreAPIImplementation: smore ore less than one triple provided, statementSet has wrong size");
            }
            java.net.URI space = new java.net.URI(spaceURI);
            if (log.isInfoEnabled()) log.info("String out: parsed statement: " + statement.toString());
            newCookie = coreAPIImplementation.out(statement, space, transactionID, synchrone, signature);
        } catch (Exception e) {
            log.error("StringCoreAPIImplementation: " + e.toString());
            throw new java.lang.RuntimeException("StringCoreAPIImplementation: " + e.toString(), e);
        }
        return new String[] { "OK, order received", newCookie };
    }

    public String[] rdWithSpace(String query, String spaceURI, int timeout, boolean recursive, URI transactionID, String signature) throws TSAPIException {
        String newCookie;
        CoreAPIImplementation coreAPIImplementation = new CoreAPIImplementation(securityCertificate, securityCookie, securityAssertions);
        java.net.URI space;
        try {
            space = new java.net.URI(spaceURI);
            Pair<String, Set<Statement>> statementSet = coreAPIImplementation.rd_with_cookie(query, space, timeout, recursive, transactionID, signature);
            String rdfxmlString = RDFWrapper.getRDFXMLStringFromStatements(statementSet.second);
            newCookie = statementSet.first;
            return new String[] { rdfxmlString, newCookie };
        } catch (URISyntaxException e) {
            log.error("StringCoreAPIImplementation: " + e.toString());
            return null;
        }
    }

    public String[] rdWithSpace(String query, String spaceURI, int timeout, URI transactionID, String signature) throws TSAPIException {
        return rdWithSpace(query, spaceURI, timeout, false, transactionID, signature);
    }

    public String[] rdWithSpace(String query, String spaceURI, int timeout, String signature) throws TSAPIException {
        return rdWithSpace(query, spaceURI, timeout, false, null, signature);
    }

    public String[] rdWOSpace(String query, int timeout, String signature) throws TSAPIException {
        return this.rdWOSpace(query, timeout, null, signature);
    }

    public String[] rdWOSpace(String query, int timeout, URI transactionID, String signature) throws TSAPIException {
        String newCookie;
        CoreAPIImplementation coreAPIImplementation = new CoreAPIImplementation(securityCertificate, securityCookie, securityAssertions);
        Pair<String, Set<Statement>> statementSet = coreAPIImplementation.rd_with_cookie(query, timeout, true, transactionID, signature);
        String rdfxmlString = RDFWrapper.getRDFXMLStringFromStatements(statementSet.second);
        newCookie = statementSet.first;
        return new String[] { rdfxmlString, newCookie };
    }

    public String[] rdWOSpaceWithKnownKernels(String query, int timeout, Set<URI> kernels, URI transactionID, String signature) throws TSAPIException {
        String newCookie;
        CoreAPIImplementation coreAPIImplementation = new CoreAPIImplementation(securityCertificate, securityCookie, securityAssertions);
        Pair<String, Set<Statement>> statementSet = coreAPIImplementation.rd_with_cookie_with_known_kernels(query, timeout, kernels, transactionID, signature);
        String rdfxmlString = RDFWrapper.getRDFXMLStringFromStatements(statementSet.second);
        newCookie = statementSet.first;
        return new String[] { rdfxmlString, newCookie };
    }
}
