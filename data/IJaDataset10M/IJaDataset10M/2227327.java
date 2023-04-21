package es.caib.zkib.binder.list;

import java.util.Iterator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import es.caib.zkib.binder.BindContext;
import es.caib.zkib.binder.SingletonBinder;
import es.caib.zkib.component.DataCombobox;
import es.caib.zkib.component.DataListbox;
import es.caib.zkib.component.MasterListItem;

public class DataListItemRenderer implements ListitemRenderer {

    DataListbox _listbox;

    DataCombobox _combobox;

    public DataListItemRenderer(DataListbox listbox) {
        _listbox = listbox;
    }

    public void render(Listitem item, Object data) throws Exception {
        try {
            String xPath;
            int i = item.getIndex();
            ListModel model = null;
            MasterListItem master = null;
            model = _listbox.getModel();
            master = _listbox.getMasterListItem();
            ;
            if (model != null && model instanceof ModelProxy) xPath = ((ModelProxy) model).getBind(i); else xPath = "[" + (i + 1) + "]";
            SingletonBinder binder = new SingletonBinder(item);
            binder.setDataPath(xPath);
            item.setAttribute(BindContext.BINDCTX_ATTRIBUTE, binder, Component.COMPONENT_SCOPE);
            if (master.getBind() != null) item.setValue(binder.getJXPathContext().getValue(master.getBind()));
            while (item.getChildren().size() > 0) {
                Component c = (Component) item.getChildren().get(0);
                c.setParent(null);
            }
            Iterator it1 = master.getChildren().iterator();
            while (it1.hasNext()) {
                Component c1 = (Component) it1.next();
                Component clone = (Component) c1.clone();
                clone.setParent(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Component duplicateComponent(Component master, Component parent) {
        Component replica = (Component) master.clone();
        replica.setParent(parent);
        applyData(replica, master);
        return replica;
    }

    private void applyData(Component component, Component master) {
        for (Iterator it = master.getChildren().iterator(); it.hasNext(); ) {
            duplicateComponent((Component) it.next(), component);
        }
    }
}
