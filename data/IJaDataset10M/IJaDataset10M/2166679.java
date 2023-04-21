package data;

import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import security.PbStreamCipher;

/**
 * this is the common interface for all data sources. it provides methods to
 * access and modify flash vocabulary data
 * 
 * @author executor
 */
public abstract class DataSource {

    /**
	 * stores a string representation of the source type
	 */
    protected String sourcetype = "unknown";

    /**
	 * the security parameter used for this data source
	 */
    protected PbStreamCipher secparam;

    /**
	 * @return the string representation of the source type
	 */
    public String getSourceType() {
        return sourcetype;
    }

    /**
	 * sets the security parameter for this data source
	 * 
	 * @param pbsc
	 *            the password based stream cipher that should be used for this
	 *            data source
	 */
    public void setSecurityParameter(PbStreamCipher pbsc) {
        secparam = pbsc;
    }

    /**
	 * @return true if data sources of this type can be created, false if only
	 *         existing ones can be used
	 */
    public boolean isCreatable() {
        return false;
    }

    /**
	 * creates a new data source of this type (can only be successful if
	 * isCreatable is true)
	 * 
	 * @param parent
	 *            the parent shell used to display dialogs
	 * @return true if a new data source has been created, false otherwise
	 * @throws DataSourceException
	 */
    public boolean create(Shell parent) throws DataSourceException {
        return false;
    }

    /**
	 * creates a data source by attaching itself to an already existing source
	 * type (file, db, ...)
	 * 
	 * @param parent
	 *            the parent shell used to display dialogs
	 * @return true if the data source has been initialized successfully, false
	 *         otherwise
	 * @throws DataSourceException
	 */
    public abstract boolean initialize(Shell parent) throws DataSourceException;

    /**
	 * creates a data source by reading the exact configuration from a xml
	 * configuration. this configuration had to be created by the
	 * generateParameterXml method to ensure correctness of the loading process
	 * 
	 * @param node
	 *            xml node of the configuration
	 * @throws DataSourceException
	 *             if the node has not the expected format
	 */
    public abstract void initialize(Element node) throws DataSourceException;

    /**
	 * generates a xml node representing this data source's configuration
	 * 
	 * @param doc
	 *            parent document of the created xml node
	 * @return the generated xml node
	 * @throws DataSourceException
	 */
    public abstract Element generateParameterXml(Document doc) throws DataSourceException;

    /**
	 * generates the xml skeleton common to all data source configurations
	 * 
	 * @param doc
	 *            parent document of the created xml node
	 * @return the generated skeleton
	 */
    protected Element generateParameterSkeleton(Document doc) {
        Element skel = doc.createElement("source");
        skel.setAttribute("type", this.getClass().getName());
        Element security = doc.createElement("secparam");
        security.setAttribute("password", secparam.getPassword());
        skel.appendChild(security);
        return skel;
    }

    /**
	 * saves the data source and frees used resources
	 */
    public abstract void close();
}
