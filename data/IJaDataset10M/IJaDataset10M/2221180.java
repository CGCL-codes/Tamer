package es.aeat.eett.rubik.menu;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import com.tonbeller.jpivot.olap.model.OlapModel;
import es.aeat.eett.rubik.core.ViewPartFactory;
import es.aeat.eett.workbench.core.CorePlugin;

public class HtmlViewFactory implements ViewPartFactory {

    HtmlViewFactory() {
        super();
    }

    public JComponent getView(OlapModel olapModel) throws Exception {
        HtmlPane htmlPane = new HtmlView().createHltmPane();
        JScrollPane scroll = new JScrollPane(htmlPane);
        scroll.putClientProperty(CorePlugin.KEY_PROPERTY_PLUGIN_CONFIGURABLE, htmlPane);
        HtmlConfiUI confiUI = new HtmlConfiUI(htmlPane);
        scroll.putClientProperty(CorePlugin.KEY_PROPERTY_PLUGIN_CONFIGURABLE_UI, confiUI);
        return scroll;
    }
}
