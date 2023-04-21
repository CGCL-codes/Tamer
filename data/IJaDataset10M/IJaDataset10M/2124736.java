package net.seagis.coverage.web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import net.seagis.catalog.CatalogException;
import org.opengis.util.CodeList;

/**
 * Reports a failure in {@link WebService}.
 *
 * @version $Id: WebServiceException.java 448 2008-03-11 12:39:36Z desruisseaux $
 * @author Guihlem Legal
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "WebServiceException", namespace = "http://wms.geomatys.fr/")
public abstract class WebServiceException extends CatalogException {

    /**
     * Creates an exception with no cause and no details message.
     */
    public WebServiceException() {
        super();
    }

    /**
     * Creates an exception with the specified details message.
     *
     * @param message The detail message.
     */
    public WebServiceException(final String message) {
        super(message);
    }

    /**
     * Creates an exception with the specified cause and no details message.
     *
     * @param cause The cause for this exception.
     */
    public WebServiceException(Exception cause) {
        super(cause);
    }

    /**
     * Creates an exception with the specified details message and cause.
     *
     * @param message The detail message.
     * @param cause The cause for this exception.
     */
    public WebServiceException(String message, Exception cause) {
        super(message, cause);
    }

    public abstract CodeList getExceptionCode();

    public abstract String getVersion();
}
