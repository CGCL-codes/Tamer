package be.fedict.eid.applet.service.signer.facets;

/**
 * Interface for the signature policy service.
 * 
 * @author Frank Cornelis
 * 
 */
public interface SignaturePolicyService {

    /**
	 * Gives back the signature policy identifier URI.
	 * 
	 * @return
	 */
    String getSignaturePolicyIdentifier();

    /**
	 * Gives back the short description of the signature policy or
	 * <code>null</code> if a description is not available.
	 * 
	 * @return the description, or <code>null</code>.
	 */
    String getSignaturePolicyDescription();

    /**
	 * Gives back the download URL where the signature policy document can be
	 * found. Can be <code>null</code> in case such a download location does not
	 * exist.
	 * 
	 * @return the download URL, or <code>null</code>.
	 */
    String getSignaturePolicyDownloadUrl();

    /**
	 * Gives back the signature policy document.
	 * 
	 * @return the bytes of the signature policy document.
	 */
    byte[] getSignaturePolicyDocument();
}
