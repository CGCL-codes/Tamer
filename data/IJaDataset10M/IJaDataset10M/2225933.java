package be.fedict.eid.applet.service.spi;

import java.io.Serializable;

/**
 * Identity Request.
 * 
 * @author Frank Cornelis
 * 
 */
public class IdentityRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private final boolean includeIdentity;

    private final boolean includeAddress;

    private final boolean includePhoto;

    private final boolean includeCertificates;

    private final boolean removeCard;

    /**
	 * Main constructor.
	 * 
	 * @param includeIdentity
	 *            Marks whether the eID operation should also deliver the eID
	 *            identity data.
	 * @param includeAddress
	 *            Marks whether the eID operation should also deliver the eID
	 *            address data.
	 * @param includePhoto
	 *            Marks whether the eID operation should also deliver the eID
	 *            photo.
	 * @param includeCertificates
	 *            Marks whether the eID operation should also deliver the eID
	 *            certificates.
	 * @param removeCard
	 *            Marks whether the eID card should be removed after eID
	 *            operation.
	 */
    public IdentityRequest(boolean includeIdentity, boolean includeAddress, boolean includePhoto, boolean includeCertificates, boolean removeCard) {
        super();
        this.includeIdentity = includeIdentity;
        this.includeAddress = includeAddress;
        this.includePhoto = includePhoto;
        this.includeCertificates = includeCertificates;
        this.removeCard = removeCard;
    }

    /**
	 * Main constructor.
	 * 
	 * @param includeIdentity
	 *            Marks whether the eID operation should also deliver the eID
	 *            identity data.
	 * @param includeAddress
	 *            Marks whether the eID operation should also deliver the eID
	 *            address data.
	 * @param includePhoto
	 *            Marks whether the eID operation should also deliver the eID
	 *            photo.
	 * @param includeCertificates
	 *            Marks whether the eID operation should also deliver the eID
	 *            certificates.
	 */
    public IdentityRequest(boolean includeIdentity, boolean includeAddress, boolean includePhoto, boolean includeCertificates) {
        this(includeIdentity, includeAddress, includePhoto, includeCertificates, false);
    }

    public boolean includeIdentity() {
        return this.includeIdentity;
    }

    public boolean includeAddress() {
        return this.includeAddress;
    }

    public boolean includePhoto() {
        return this.includePhoto;
    }

    public boolean includeCertificates() {
        return this.includeCertificates;
    }

    public boolean removeCard() {
        return this.removeCard;
    }
}
