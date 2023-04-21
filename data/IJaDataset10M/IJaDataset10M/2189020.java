package proto.weblink;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.IDomain;
import org.modelibra.DomainModel;
import proto.weblink.url.Urls;

/**
 * WebLink generated model. This class should not be changed manually. 
 * Use a subclass to add specific code.
 * 
 * @author Dzenan Ridjanovic
 * @version 2011-09-20
 */
public abstract class GenWebLink extends DomainModel {

    private static final long serialVersionUID = 1316545278713L;

    private static Log log = LogFactory.getLog(GenWebLink.class);

    private Urls urls;

    /**
	 * Constructs a domain model within the domain.
	 * 
	 * @param domain
	 *            domain
	 */
    public GenWebLink(IDomain domain) {
        super(domain);
        urls = new Urls(this);
    }

    /**
	 * Gets Url entities.
	 * 
	 * @return Url entities
	 */
    public Urls getUrls() {
        return urls;
    }
}
