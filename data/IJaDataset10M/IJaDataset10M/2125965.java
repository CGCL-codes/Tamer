package de.miethxml.toolkit.container;

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Action;
import de.miethxml.toolkit.component.AbstractServiceable;
import de.miethxml.toolkit.ui.MenuBarManager;
import de.miethxml.toolkit.ui.ToolBarManager;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth </a>
 *
 */
public class DefaultActionContainer extends AbstractServiceable implements Configurable, LogEnabled, Initializable, Parameterizable, Disposable, Startable {

    protected ArrayList actions = new ArrayList();

    private Logger log;

    private Parameters params;

    /**
     *
     */
    public DefaultActionContainer() {
        super();
    }

    public void enableLogging(Logger log) {
        this.log = log;
    }

    public void initialize() {
        try {
            ToolBarManager toolbar = (ToolBarManager) manager.lookup(ToolBarManager.ROLE);
            MenuBarManager menubar = (MenuBarManager) manager.lookup(MenuBarManager.ROLE);
            Iterator i = actions.iterator();
            while (i.hasNext()) {
                ActionComponent comp = (ActionComponent) i.next();
                Action action = comp.getAction();
                ContainerUtil.enableLogging(action, this.log);
                applicationLifecycle(action);
                ContainerUtil.service(action, this.manager);
                ContainerUtil.parameterize(action, params);
                ContainerUtil.initialize(action);
                if (comp.isOnToolbar()) {
                    toolbar.addAction(action, ToolBarManager.BEFORE_LAST);
                }
                if (comp.hasMenuRole()) {
                    menubar.setMenuItemAction(comp.getMenuRole(), action);
                }
            }
        } catch (Exception e) {
            log.error("Initialize Actions", e);
        }
    }

    public void configure(Configuration conf) throws ConfigurationException {
        Configuration[] configs = conf.getChild("actions").getChildren();
        for (int i = 0; i < configs.length; i++) {
            try {
                Action action = (Action) Class.forName(configs[i].getAttribute("class")).newInstance();
                ActionComponent comp = new ActionComponent(action);
                if (configs[i].getAttribute("menu-role") != null) {
                    comp.setMenuRole(configs[i].getAttribute("menu-role"));
                }
                if (configs[i].getAttribute("toolbar") != null) {
                    comp.setOnToolbar(configs[i].getAttributeAsBoolean("toolbar"));
                }
                actions.add(comp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void parameterize(Parameters arg0) throws ParameterException {
        this.params = arg0;
    }

    public void dispose() {
        Iterator i = actions.iterator();
        while (i.hasNext()) {
            ActionComponent comp = (ActionComponent) i.next();
            ContainerUtil.dispose(comp.getAction());
        }
    }

    public void start() throws Exception {
        Iterator i = actions.iterator();
        while (i.hasNext()) {
            ActionComponent comp = (ActionComponent) i.next();
            ContainerUtil.start(comp.getAction());
        }
    }

    public void stop() throws Exception {
        Iterator i = actions.iterator();
        while (i.hasNext()) {
            ActionComponent comp = (ActionComponent) i.next();
            ContainerUtil.stop(comp.getAction());
        }
    }

    protected void applicationLifecycle(Object obj) {
    }

    public class ActionComponent {

        private String menuRole = "";

        private Action action;

        private boolean onToolbar = false;

        public ActionComponent(Action action) {
            this.action = action;
        }

        public boolean hasMenuRole() {
            if ((menuRole != null) && (menuRole.length() > 0)) {
                return true;
            }
            return false;
        }

        public String getMenuRole() {
            return menuRole;
        }

        public void setMenuRole(String menuRole) {
            this.menuRole = menuRole;
        }

        public Action getAction() {
            return action;
        }

        public boolean isOnToolbar() {
            return onToolbar;
        }

        public void setOnToolbar(boolean onToolbar) {
            this.onToolbar = onToolbar;
        }
    }
}
