package sisi.depositi;

import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;

public class RenderListaDepositi extends GenericForwardComposer implements ListitemRenderer {

    private static final long serialVersionUID = 1L;

    public RenderListaDepositi() {
    }

    public void render(org.zkoss.zul.Listitem listItem, Object data) throws Exception {
        Depositi dep = (Depositi) data;
        new Listcell(dep.getCoddep()).setParent(listItem);
        new Listcell(dep.getDpdes()).setParent(listItem);
        listItem.setAttribute("rigaDepositi", data);
    }
}
