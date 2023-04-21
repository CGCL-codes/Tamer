package org.personalsmartspace.spm.trust.impl.model;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * This class represents trusted services. A <code>TrustedService</code> object
 * is referenced by the service type, while the associated {@link TrustValue}
 * object expresses the trustworthiness of this service. Each trusted service is
 * associated with a set of {@link ServiceTransaction} objects.
 * 
 * @see org.personalsmartspace.spm.trust.impl.model.TrustedEntity
 * @see org.personalsmartspace.spm.trust.impl.model.TrustedPss
 * @author <a href="mailto:nliampotis@users.sourceforge.net">Nicolas
 *         Liampotis</a> (ICCS)
 * @since 0.1.0
 */
public final class TrustedService extends TrustedEntity<String, TrustValue> {

    private static final long serialVersionUID = 6501256912753639265L;

    private final TrustedPss providerPss;

    private final Set<ServiceTransaction> transactions;

    /**
     * Creates a <code>TrustedService</code> object for the specified service
     * type. The DPI of the provider PSS and the initial trust value are also
     * supplied.
     * 
     * @param providerPss
     *            the PSS providing this service.
     * @param serviceType
     *            the type of this trusted service.
     * @param trust
     *            the initial trust value.
     */
    TrustedService(final TrustedPss providerPss, final String serviceType, TrustValue trust) {
        super(serviceType, trust);
        this.providerPss = providerPss;
        this.transactions = new CopyOnWriteArraySet<ServiceTransaction>();
    }

    /**
     * Returns the PSS providing this service
     * 
     * @return the PSS providing this service
     * @see TrustedPss
     */
    public TrustedPss getProviderPss() {
        return this.providerPss;
    }

    /**
     * Returns a set containing all <code>SericeTransaction</code> objects
     * associated with this service. Note that the method returns an
     * <i>unmodifiable</i> view of the service transactions. To modify the
     * contents of the <code>SericeTransaction</code> set use the
     * <code>addTransaction</code> method. If there are no transactions for this
     * service, an <i>empty</i> set is returned.
     * 
     * @return an unmodifiable set containing all
     *         <code>ServiceTransaction</code> objects associated with this
     *         service.
     * @since 0.3.1
     */
    public Set<ServiceTransaction> getAllTransactions() {
        return Collections.unmodifiableSet(this.transactions);
    }

    /**
     * Adds a transaction with this service.
     * 
     * @param transaction
     *            the service transaction to add.
     * @since 0.3.1
     */
    public void addTransaction(final ServiceTransaction transaction) {
        this.transactions.add(transaction);
    }

    /**
     * Returns the number of transactions with this service.
     * 
     * @return the number of transactions with this service.
     * @since 0.3.1
     */
    public int getTransactionSize() {
        return this.transactions.size();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.providerPss == null) ? 0 : this.providerPss.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (this.getClass() != obj.getClass()) return false;
        TrustedService that = (TrustedService) obj;
        if (this.providerPss == null) {
            if (that.providerPss != null) return false;
        } else if (!this.providerPss.equals(that.providerPss)) return false;
        return true;
    }
}
