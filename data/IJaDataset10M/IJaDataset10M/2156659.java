package dataManager;

import java.util.Observable;
import org.dom4j.XPath;
import org.dom4j.Node;

/**
 * @author David
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FilterAttributes extends Observable {

    private XPath query;

    private static FilterAttributes aInstance;

    private XMLDataSource xMLDataSource;

    public void filterAttributes(Node currentObject) {
    }

    public void setFilter(String filter) {
    }

    public FilterAttributes getInstance() {
        return null;
    }
}
