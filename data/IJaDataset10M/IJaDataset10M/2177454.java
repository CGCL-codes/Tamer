package org.personalsmartspace.spm.identity.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.spm.identity.api.platform.Identity;
import org.personalsmartspace.spm.identity.api.platform.IdentityMgmtException;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;

/**
 * @author <a href="mailto:nliampotis@users.sourceforge.net">Nicolas
 *         Liampotis</a> (ICCS)
 * @since 0.4.0
 */
public class FileIdentityWallet {

    /** The name of the file where the identities are stored. */
    private static final String DATA_FILENAME = "identity.data";

    /** The PSS logging facility. */
    private final PSSLog log = new PSSLog(this);

    public FileIdentityWallet() throws IdentityMgmtException {
        this.log.info("Connecting to ID wallet");
        try {
            this.doRetrieve();
        } catch (IdentityMgmtException idme) {
            this.log.warn("ID wallet is missing");
            this.init();
        }
    }

    /**
     * Returns an unmodifiable view of all identities contained in the ID
     * wallet.
     * 
     * @return an unmodifiable view of all identities contained in the ID
     *         wallet.
     * @throws IdentityMgmtException
     */
    public Set<Identity> getAll() throws IdentityMgmtException {
        if (this.log.isDebugEnabled()) this.log.debug("Retrieving all identities from ID wallet");
        return Collections.unmodifiableSet(new HashSet<Identity>(this.doRetrieve().values()));
    }

    /**
     * Returns an unmodifiable view of all DPIs contained in the ID wallet.
     * 
     * @return an unmodifiable view of all DPIs contained in the ID wallet.
     * @throws IdentityMgmtException
     */
    public Set<IDigitalPersonalIdentifier> getAllIdentifiers() throws IdentityMgmtException {
        if (this.log.isDebugEnabled()) this.log.debug("Retrieving all DPIs from ID wallet");
        return Collections.unmodifiableSet(this.doRetrieve().keySet());
    }

    /**
     * Retrieves the identity associated with the specified DPI from the ID
     * wallet. The method returns <code>null</code> if no match is found.
     * 
     * @param dpi
     *            the DPI to match.
     * @return the identity associated with the specified DPI or
     *         <code>null</code> if no match is found.
     * @throws IdentityMgmtException
     */
    public Identity get(final IDigitalPersonalIdentifier dpi) throws IdentityMgmtException {
        if (this.log.isDebugEnabled()) this.log.debug("Retrieving identity for DPI " + dpi + " from the ID wallet");
        if (dpi == null) throw new NullPointerException("dpi can't be null");
        final Map<IDigitalPersonalIdentifier, Identity> identities = this.doRetrieve();
        return identities.get(dpi);
    }

    /**
     * Retrieves the identity associated with the specified DPI annotation from
     * the ID wallet. The method returns <code>null</code> if no match is found.
     * 
     * @param annotation
     *            the DPI annotation to match.
     * @return the identity associated with the specified DPI or
     *         <code>null</code> if no match is found.
     * @throws IdentityMgmtException if there is a problem accessing the
     */
    public Identity get(final String annotation) throws IdentityMgmtException {
        if (this.log.isDebugEnabled()) this.log.debug("Retrieving identity for DPI annotation " + annotation + " from the ID wallet");
        if (annotation == null) throw new NullPointerException("annotation can't be null");
        final Map<IDigitalPersonalIdentifier, Identity> identities = this.doRetrieve();
        for (final Identity identity : identities.values()) if (annotation.equals(identity.getID().getAnnotation())) return identity;
        return null;
    }

    public void store(final Identity identity) throws IdentityMgmtException {
        if (this.log.isDebugEnabled()) this.log.debug("Storing identity " + identity + " to the ID wallet");
        if (identity == null) throw new NullPointerException("identity can't be null");
        final Map<IDigitalPersonalIdentifier, Identity> identities = this.doRetrieve();
        identities.put(identity.getID(), identity);
        this.doStore(identities);
    }

    /**
     * Removes the identity associated with the specified DPI from the ID
     * wallet. The method returns <code>true</code> if the ID wallet contained
     * an identity with the specified DPI.
     * 
     * @param dpi
     *            the DPI to match.
     * @return <code>true</code> if the ID wallet contained an identity with the
     *         specified DPI; <code>false</code> otherwise.
     * @throws IdentityMgmtException
     */
    public boolean remove(final IDigitalPersonalIdentifier dpi) throws IdentityMgmtException {
        if (this.log.isDebugEnabled()) this.log.debug("Removing identity for DPI " + dpi + " from the ID wallet");
        if (dpi == null) throw new NullPointerException("dpi can't be null");
        final Map<IDigitalPersonalIdentifier, Identity> identities = this.doRetrieve();
        if (identities.remove(dpi) != null) {
            this.doStore(identities);
            return true;
        }
        return false;
    }

    public boolean contains(final IDigitalPersonalIdentifier dpi) throws IdentityMgmtException {
        if (this.log.isDebugEnabled()) this.log.debug("Checking if " + dpi + " is contained in the ID wallet");
        if (dpi == null) throw new NullPointerException("dpi can't be null");
        final Map<IDigitalPersonalIdentifier, Identity> identities = this.doRetrieve();
        return identities.containsKey(dpi);
    }

    public int getSize() throws IdentityMgmtException {
        if (this.log.isDebugEnabled()) this.log.debug("Fetching ID wallet size");
        final Map<IDigitalPersonalIdentifier, Identity> identities = this.doRetrieve();
        return identities.size();
    }

    public void clear() throws IdentityMgmtException {
        if (this.log.isDebugEnabled()) this.log.debug("Clearing ID wallet");
        this.doStore(new LinkedHashMap<IDigitalPersonalIdentifier, Identity>());
    }

    private void init() throws IdentityMgmtException {
        this.log.info("Initialising FILE ID wallet");
        this.doStore(new LinkedHashMap<IDigitalPersonalIdentifier, Identity>());
    }

    @SuppressWarnings("unchecked")
    private synchronized Map<IDigitalPersonalIdentifier, Identity> doRetrieve() throws IdentityMgmtException {
        Map<IDigitalPersonalIdentifier, Identity> identities = null;
        try {
            InputStream file = new FileInputStream(DATA_FILENAME);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            try {
                identities = (Map<IDigitalPersonalIdentifier, Identity>) input.readObject();
            } finally {
                input.close();
            }
        } catch (Exception e) {
            throw new IdentityMgmtException("Could not read identities from file: " + e.getMessage(), e);
        }
        return identities;
    }

    private synchronized void doStore(final Map<IDigitalPersonalIdentifier, Identity> identities) throws IdentityMgmtException {
        try {
            OutputStream file = new FileOutputStream(DATA_FILENAME);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            try {
                output.writeObject(identities);
            } finally {
                output.close();
            }
        } catch (Exception e) {
            throw new IdentityMgmtException("Could not write identities to file: " + e.getMessage(), e);
        }
    }
}
