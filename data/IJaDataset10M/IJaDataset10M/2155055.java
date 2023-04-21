package javax.xml.registry.infomodel;

import javax.xml.registry.JAXRException;

/**
 * Defines common behavior expected of any class that validates URIs.
 * 
 * 
 * @author Farrukh S. Najmi
 */
public interface URIValidator {

    /**
	 * Sets whether to do URI validation for this object. Default is true.
	 * 
	 * <p>
	 * <DL>
	 * <DT><B>Capability Level: 0 </B>
	 * </DL>
	 * 
	 * @param validate
	 *            <code>true</code> implies JAXR provider must perform
	 *            validation of URIs when they are set; <code>false</code>
	 *            implies validation is turned off
	 * @throws JAXRException
	 *             If the JAXR provider encounters an internal error
	 * 
	 */
    public void setValidateURI(boolean validate) throws JAXRException;

    /**
	 * Gets whether to do URI validation for this object.
	 * <p>
	 * <DL>
	 * <DT><B>Capability Level: 0 </B>
	 * </DL>
	 * 
	 * @return <code>true</code> implies JAXR provider must perform validation
	 *         of URIs when they are set; <code>false</code> implies validation
	 *         is turned off
	 * @throws JAXRException
	 *             If the JAXR provider encounters an internal error
	 * 
	 */
    public boolean getValidateURI() throws JAXRException;
}
