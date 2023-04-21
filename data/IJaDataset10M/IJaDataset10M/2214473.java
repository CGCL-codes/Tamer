package net.da.client.component;

import net.da.client.common.component.ComponentDef;
import net.da.client.common.component.ComponentDispatcher;
import net.da.client.common.component.ComponentDispatcherImpl;
import net.da.client.common.component.ComponentImpl;
import net.da.client.common.ui.widget.NavigationPanel;
import net.da.client.common.ui.widget.NestNavigationPanel;
import net.da.client.constants.DAConstants;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * TODO:
 * 
 * @author Alexander E. Berezin (ABE) / 2M business applications a|s
 * @version $Revision: 1.0 $ $Date: 26 ��� 2007 13:45:36 $
 */
public abstract class AbstractTestComponent extends ComponentImpl {

    private Panel mainPanel;

    public void onHide() {
    }

    public void onShow() {
        buildLayout();
    }

    protected void buildLayout() {
        String[] compNames = new String[] { DAConstants.COMPONENTS_NAMES.MAIN_COMPONENT, DAConstants.COMPONENTS_NAMES.TEST_COMPONENT_0, DAConstants.COMPONENTS_NAMES.TEST_COMPONENT_1, DAConstants.COMPONENTS_NAMES.TEST_COMPONENT_2 };
        String compName = getComponentDef().getName();
        String id0 = HTMLPanel.createUniqueId();
        String id1 = HTMLPanel.createUniqueId();
        HTMLPanel html = new HTMLPanel("<div> Id:" + compName + " </div>" + "<br><div> Info:" + compName + " </div>" + "<br><span id='" + id0 + "'></span>" + "<br><br><span id='" + id1 + "'></span>");
        NavigationPanel np = new NavigationPanel(this, compNames);
        NestNavigationPanel nnp = new NestNavigationPanel(this, compNames);
        html.add(np, id0);
        html.add(nnp, id1);
        Panel root = getComponentRoot();
        root.clear();
        root.add(html);
    }

    protected void buildButtons() {
        String[] componets = new String[] { DAConstants.COMPONENTS_NAMES.MAIN_COMPONENT, DAConstants.COMPONENTS_NAMES.TEST_COMPONENT_0, DAConstants.COMPONENTS_NAMES.TEST_COMPONENT_1, DAConstants.COMPONENTS_NAMES.TEST_COMPONENT_2 };
        ComponentDef def = getComponentDef();
        final ComponentDispatcher componentDispatcher = ComponentDispatcherImpl.getInstance();
        HorizontalPanel hp = new HorizontalPanel();
        mainPanel.add(hp);
        for (int i = 0; i < componets.length; i++) {
            final String compName = componets[i];
            Button button = new Button();
            button.setEnabled(false);
            button.setText(compName);
            if (!def.getName().equals(compName)) {
                button.setEnabled(true);
                button.addClickListener(new ClickListener() {

                    public void onClick(Widget arg0) {
                        componentDispatcher.jumpTo(compName);
                    }
                });
            }
            hp.add(button);
        }
    }
}
