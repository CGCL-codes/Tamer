package net.fdukedom.epicurus.client.plugin.core;

import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.JPanel;
import net.fdukedom.epicurus.client.ClientSession;
import net.fdukedom.epicurus.client.plugin.ClientPluginInterface;
import net.fdukedom.epicurus.client.plugin.PageNotFoundException;
import net.fdukedom.epicurus.client.plugin.core.panels.LoginPanel;
import net.fdukedom.epicurus.client.plugin.core.panels.ProfilePanel;
import net.fdukedom.epicurus.client.ui.GUIDispatcher;
import net.fdukedom.epicurus.resource.ResourceDispatcher;

/**
 * Implementation of core plugin for client
 * 
 * @author Alexander Kirakozov
 * 
 */
public class CoreClientPlugin implements ClientPluginInterface {

    /**
	 * Plugin name
	 */
    public static final String PLUGINNAME = "core";

    public JPanel createPage(String pageLocation, Map<String, Object> parameters) throws PageNotFoundException {
        ResourceBundle bundle = ResourceDispatcher.getInstance().getBundle("bundles.core.Extensions");
        if (pageLocation.equals(bundle.getString("core.profile.actionLink"))) {
            return new ProfilePanel();
        } else if (pageLocation.equals(bundle.getString("core.login.actionLink"))) {
            return new LoginPanel();
        } else if (pageLocation.equals(bundle.getString("core.logout.actionLink"))) {
            ClientSession.getInstance().closeSession();
            return GUIDispatcher.getInstance().createPage(GUIDispatcher.INDEX_PAGE_ID);
        } else {
            throw new PageNotFoundException("Page unical name: " + pageLocation);
        }
    }
}
