package org.tripcom.security.policies.impl;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.tripcom.integration.entry.TripleEntry;

/**
 * A <tt>PolicyDAO</tt> stub that maintains policies and certificates in
 * memory.
 * <p>
 * This class implements a transient repository of policies and certificates,
 * which are kept in main memory data structures. The repository is empty after
 * construction.
 * </p>
 * 
 * @author Francesco Corcoglioniti &lt;francesco.corcoglioniti@cefriel.it&gt;
 */
public class PolicyDAOStub implements PolicyDAO {

    /** A map associating a space URL with its policy, where defined. */
    private Map<String, Set<TripleEntry>> policies;

    /** A map associating a DN with corresponding certificates. */
    private Map<String, Collection<X509Certificate>> certificates;

    /**
	 * Create a new local policy DAO. The initial sets of policies and
	 * certificates will be empty.
	 */
    public PolicyDAOStub() {
        this.policies = Collections.synchronizedMap(new HashMap<String, Set<TripleEntry>>());
        this.certificates = Collections.synchronizedMap(new HashMap<String, Collection<X509Certificate>>());
    }

    /**
	 * {@inheritDoc}
	 */
    public Collection<X509Certificate> loadCertificates(String authorityDN) {
        if (authorityDN == null) {
            throw new NullPointerException();
        }
        Collection<X509Certificate> result = certificates.get(authorityDN);
        if (result == null) {
            result = new ArrayList<X509Certificate>();
        }
        return result;
    }

    /**
	 * {@inheritDoc}
	 */
    public Set<TripleEntry> loadPolicy(String spaceURL) {
        if (spaceURL == null) {
            throw new NullPointerException();
        }
        Set<TripleEntry> result = policies.get(spaceURL);
        if (result == null) {
            result = new HashSet<TripleEntry>();
        }
        return result;
    }

    /**
	 * {@inheritDoc}
	 */
    public void storeCertificates(String authorityDN, Collection<X509Certificate> certificates) {
        if (authorityDN == null) {
            throw new NullPointerException();
        }
        if ((certificates == null) || certificates.isEmpty()) {
            this.certificates.remove(authorityDN);
        } else {
            this.certificates.put(authorityDN, new ArrayList<X509Certificate>(certificates));
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public void storePolicy(String spaceURL, Set<TripleEntry> policyGraph) {
        if (spaceURL == null) {
            throw new NullPointerException();
        }
        if ((policyGraph == null) || policyGraph.isEmpty()) {
            this.policies.remove(spaceURL);
        } else {
            this.policies.put(spaceURL, new HashSet<TripleEntry>(policyGraph));
        }
    }
}
