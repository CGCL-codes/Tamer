package org.zkoss.bind.converter.sys;

import java.util.Iterator;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;

/**
 * Convert tabbox selected tab and vice versa.
 *
 * @author Dennis
 * @since 6.0.0
 */
public class TabboxSelectedTabConverter implements Converter, java.io.Serializable {

    private static final long serialVersionUID = 200808190445L;

    @Override
    public Object coerceToUi(Object val, Component component, BindContext ctx) {
        if (val != null) {
            for (Iterator<Component> it = ((Tabbox) component).getTabs().getChildren().iterator(); it.hasNext(); ) {
                final Component child = it.next();
                if (child instanceof Tab) {
                    if (val.equals(((Tab) child).getLabel())) {
                        return child;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Object coerceToBean(Object val, Component component, BindContext ctx) {
        return val != null ? ((Tab) val).getLabel() : null;
    }
}
