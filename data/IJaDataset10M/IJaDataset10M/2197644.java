package net.jxta.protocol;

import net.jxta.credential.Credential;
import net.jxta.document.Advertisement;
import net.jxta.document.ExtendableAdvertisement;

/**
 * A container for signed Advertisements
 */
public abstract class SignedAdvertisement extends ExtendableAdvertisement {

    protected Credential signer = null;

    protected Advertisement adv = null;

    /**
     * Returns the identifying type of this Advertisement.
     *
     * @return String the type of advertisement
     */
    public static String getAdvertisementType() {
        return "jxta:SA";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getBaseAdvType() {
        return getAdvertisementType();
    }

    /**
     * Gets the Advertisement.
     *
     * @return a signed advertisement
     */
    public Advertisement getAdvertisement() {
        return adv;
    }

    /**
     * Sets the Advertisement to be signed.
     *
     * @param adv set the advertisment
     */
    public void setAdvertisement(Advertisement adv) {
        this.adv = adv;
    }

    /**
     * Returns the credential which signed the advertisement.
     *
     * @return the credential which signed the advertisement.
     */
    public Credential getSigner() {
        return signer;
    }

    /**
     * Sets the Credential which will sign the advertisement.
     *
     * @param cred the credential
     */
    public void setSigner(Credential cred) {
        signer = cred;
    }
}
