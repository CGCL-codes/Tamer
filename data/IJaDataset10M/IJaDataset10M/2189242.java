package org.torweg.pulse.configuration;

import java.io.File;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import org.torweg.pulse.util.xml.transform.XSLHandle;

/**
 * configuration for an {@code XSLHandle}.
 * 
 * @author Thomas Weber
 * @version $Revision: 1387 $
 */
@XmlRootElement(name = "xsl")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlAccessorType(XmlAccessType.FIELD)
public class XSLHandleConfiguration extends Configuration {

    /**
	 * serialVersionUID.
	 */
    private static final long serialVersionUID = -838152594911552900L;

    /**
	 * the file to create the {@code XSLHandle} from.
	 */
    @XmlAttribute(name = "file", required = true)
    private String file;

    /**
	 * default constructor.
	 */
    protected XSLHandleConfiguration() {
        super();
    }

    /**
	 * creates a new {@code XSLHandleConfiguration} for the given file.
	 * 
	 * @param f
	 *            the file
	 */
    public XSLHandleConfiguration(final String f) {
        super();
        this.file = f;
    }

    /**
	 * Returns the file.
	 * 
	 * @return Returns the file.
	 */
    public final String getFile() {
        return this.file;
    }

    /**
	 * returns the {@code XSLHandle} created from the given file.
	 * 
	 * @return the {@code XSLHandle}
	 */
    public final XSLHandle getXSLHandle() {
        return PoorMansCache.getXSLHandle(new File(this.file));
    }
}
