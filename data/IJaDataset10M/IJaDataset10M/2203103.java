package newtonERP.module.generalEntity;

import newtonERP.module.AbstractEntity;
import newtonERP.viewers.viewables.ForwardViewable;

/**
 * @author Guillaume Lacasse
 */
public class ForwardEntity extends AbstractEntity implements ForwardViewable {

    private String forwardingUrl;

    /**
	 * @param forwardingUrl the url to go to
	 */
    public ForwardEntity(String forwardingUrl) {
        this.forwardingUrl = forwardingUrl;
    }

    @Override
    public String getForwardingUrl() {
        return forwardingUrl;
    }
}
