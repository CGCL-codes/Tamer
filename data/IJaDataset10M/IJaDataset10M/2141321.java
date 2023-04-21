package org.proteomecommons.MSExpedite.app;

import java.awt.Component;
import java.awt.dnd.DragSourceDropEvent;
import org.proteomecommons.MSExpedite.Graph.IMouseUser;
import org.proteomecommons.MSExpedite.Graph.IPropertyChangedListener;

/**
 *
 * @author takis
 */
public class MSEToolbar extends SessionDnDToolbar implements IPropertyChangedListener {

    public MSEToolbar(ISessionHandler h, IComponentContainer cc, AppAttrHandler appHandler) {
        super(h, cc, appHandler);
        try {
            initComponent();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initComponent() throws Exception {
        String s[] = appHandler.getAppAttr().getToolbar().getToolbarButtons();
        for (int i = 0; i < s.length; i++) {
            Class c = Class.forName(s[i]);
            Component obj = (Component) c.newInstance();
            if (obj instanceof ISessionButton) {
                ISessionButton btn = (ISessionButton) obj;
                if (sessionHandler != null) btn.set(sessionHandler);
            }
            if (obj instanceof IComponentContainerButton) {
                IComponentContainerButton btn = (IComponentContainerButton) obj;
                if (componentContainer != null) btn.set(componentContainer);
            }
            Class ci[] = c.getInterfaces();
            for (int j = 0; j < ci.length; j++) {
                if (ci[j] != IMouseUser.class) continue;
            }
            add(obj);
        }
    }

    public void propertyChanged(Object src, String propName, int mouseButton, Object oldValue, Object newValue) {
        Component[] c = this.getComponents();
        for (int i = 0; i < c.length; i++) {
            if (c[i] instanceof IPropertyChangedListener) {
                IPropertyChangedListener pcl = (IPropertyChangedListener) c[i];
                pcl.propertyChanged(src, propName, mouseButton, oldValue, newValue);
            }
        }
    }
}
