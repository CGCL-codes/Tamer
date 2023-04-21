package gui.linkFinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;

public class GetLinksEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    protected List<String> links = new ArrayList<String>();

    /**
	 * 
	 * @param arg0
	 */
    @SuppressWarnings("unchecked")
    public GetLinksEvent(Object arg0) {
        super(arg0);
        if (arg0 instanceof Collection) {
            Collection collection = ((Collection) arg0);
            this.links = new ArrayList<String>(collection);
        }
    }

    public List<String> getLinks() {
        return this.links;
    }
}
