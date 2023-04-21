package org.hip.vif.menu;

import java.util.Collection;
import java.util.Locale;
import java.util.Vector;
import org.eclipse.core.runtime.IConfigurationElement;
import org.hip.vif.ApplicationConstants;
import org.hip.vif.interfaces.IMessages;
import org.hip.vif.registry.AbstractConfiguration;
import org.osgi.framework.Bundle;

/**
 * The taskSet configuration element of the plug-in extending the <code>org.hip.vifapp.subApps</code> extension point.
 *
 * @author Luthiger
 * Created: 01.12.2007
 */
public class TaskSetConfiguration extends AbstractConfiguration {

    public static final String ATTRIBUTE_SET_ID = "setId";

    private static final String NL = System.getProperty("line.separator");

    private static final String TMPL_LI = "<li class=\"part\">%s</li>";

    private Collection<MenuTaskConfiguration> tasks;

    private String setId;

    private boolean isAdmin;

    private IMessages messages;

    /**
	 * @param inConfiguration IConfigurationElemente
	 * @param inIsAdmin boolean <code>true</code> id this element is part of the application's administration part
	 * @param inMessages This bundle's messages class.
	 * @param inBundle Bundle The bundle providing this task.
	 */
    public TaskSetConfiguration(IConfigurationElement inConfiguration, boolean inIsAdmin, IMessages inMessages, Bundle inBundle) {
        super(inConfiguration.getNamespaceIdentifier());
        isAdmin = inIsAdmin;
        messages = inMessages;
        setId = inConfiguration.getAttribute(ATTRIBUTE_SET_ID);
        tasks = new Vector<MenuTaskConfiguration>();
        for (IConfigurationElement lChild : inConfiguration.getChildren()) {
            String lName = lChild.getName();
            if (ApplicationConstants.PARTLET_MENU_TASK_ID.equals(lName)) {
                tasks.add(new MenuTaskConfiguration(lChild, isAdmin, messages, inBundle));
            } else if (ApplicationConstants.PARTLET_TASK_BACK_ID.equals(lName)) {
                tasks.add(new TaskBackConfiguration(lChild, isAdmin, messages, inBundle));
            }
        }
    }

    /**
	 * @return String The taskSet's ID.
	 */
    public String getSetId() {
        return setId;
    }

    /**
	 * Renders this taskSet to a html menu entry using the specified locale.
	 * 
	 * @param inLocale Locale
	 * @param inState ActorGroupState parameter object passing the actual state concerning the actor and group. 
	 * @return StringBuffer html
	 */
    public StringBuffer render(Locale inLocale, ActorGroupState inState) {
        StringBuffer outMenu = new StringBuffer();
        for (MenuTaskConfiguration lTask : tasks) {
            StringBuffer lEntry = lTask.render(inLocale, inState);
            if (lEntry.length() != 0) {
                outMenu.append(String.format(TMPL_LI, lEntry)).append(NL);
            }
        }
        return outMenu;
    }

    /**
	 * Release all resources reserved by this task set instance. 
	 */
    public void dispose() {
        for (MenuTaskConfiguration lTask : tasks) {
            lTask.dispose();
        }
        tasks.clear();
        messages = null;
    }
}
