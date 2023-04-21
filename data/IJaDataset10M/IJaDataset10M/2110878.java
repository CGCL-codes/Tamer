package de.forsthaus.webui.orderposition.model;

import java.io.Serializable;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import de.forsthaus.backend.model.Orderposition;

/**
 * Item renderer for listitems in the listbox.
 * 
 * @author bbruhns
 * @author sgerth
 * 
 */
public class OrderpositionListModelItemRenderer implements ListitemRenderer, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(OrderpositionListModelItemRenderer.class);

    @Override
    public void render(Listitem item, Object data) throws Exception {
        final Orderposition orderposition = (Orderposition) data;
        Listcell lc = new Listcell(String.valueOf(orderposition.getId()));
        lc.setParent(item);
        lc = new Listcell(orderposition.getArticle().getArtKurzbezeichnung());
        lc.setParent(item);
        lc = new Listcell(orderposition.getAupMenge().toString());
        lc.setStyle("text-align: right");
        lc.setParent(item);
        lc = new Listcell(orderposition.getAupEinzelwert().toString());
        lc.setStyle("text-align: right");
        lc.setParent(item);
        lc = new Listcell(orderposition.getAupGesamtwert().toString());
        lc.setStyle("text-align: right");
        lc.setParent(item);
        item.setValue(data);
        ComponentsCtrl.applyForward(item, "onDoubleClick=onDoubleClickedOrderPositionItem");
    }
}
