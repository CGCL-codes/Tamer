package it.unical.inf.wsportal.client.view;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import it.unical.inf.wsportal.client.WSPortal;
import it.unical.inf.wsportal.client.util.ComponentID;

/**
 *
 * @author Simone Spaccarotella {spa.simone@gmail.com}, Carmine Dodaro {carminedodaro@gmail.com}
 */
public class OutputTabItem extends TabItem {

    private PortletContainer portal;

    public OutputTabItem() {
        super("Output");
        setID(ComponentID.OUTPUT_TAB_ITEM);
        setLayout(new FitLayout());
        portal = WSPortal.getOutputPortletContainer();
        add(portal);
    }

    /**
     * Sets the id and the item id.
     *
     * @param id a string which represent the component univocally.
     */
    private void setID(String id) {
        setId(id);
        setItemId(id);
    }
}
