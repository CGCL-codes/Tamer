package ch.ethz.dcg.spamato.update;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import ch.ethz.dcg.plugin.*;
import ch.ethz.dcg.spamato.factory.common.main.SpamatoFactory;
import ch.ethz.dcg.spamato.webconfig.*;

/**
 * @author keno Updated after removing publishing/peerato support (unfortunately).
 * @author michelle Updated for publishing/peerato support.
 * @autho remo Original version
 */
public class UpdatePageHandler extends AbstractPageHandler {

    private final SpamatoFactory spamatoFactory;

    private PluginContainer container;

    private Collection<String> updateServers;

    private Vector<PluginInfo> updates = new Vector<PluginInfo>();

    private Vector<String> unreachableServers = new Vector<String>();

    public UpdatePageHandler(SpamatoFactory spamatoFactory, PluginContext pluginContext) {
        super(pluginContext);
        this.spamatoFactory = spamatoFactory;
        this.container = spamatoFactory.getContainer();
        this.updateServers = container.getUpdateServers();
    }

    public String getDescription(String page) {
        if ("update".equals(page)) {
            return "Use this page to install new plugins or to update existing ones.";
        } else {
            return super.getDescription(page);
        }
    }

    public void renderPage(String page, Hashtable<String, String> parameters, HtmlWriter writer) {
        if ("update".equals(page)) {
            this.renderInstallAndUpdate(page, parameters, writer);
        } else {
            this.renderMain(page, parameters, writer);
        }
    }

    public void renderMain(String page, Hashtable<String, String> parameters, HtmlWriter writer) {
        writer.startPanel("Overview");
        writer.writeLnIndent("<p>");
        writer.writeLnIndent("This page provides an overview of all installed plugins.");
        writer.writeLnIndent("You can delete plugins by clicking on the appropriate icon if the plugin is not used by any other plugin.");
        writer.writeLnIndent("Open the 'Install &amp; Update' page to install new or update existing plugins.");
        writer.writeLnIndent("</p>");
        writer.writeLnIndent("<p>");
        writer.addButton("Click here", "?action=refreshPlugins");
        writer.writeLnIndent(" to refresh the plugins now (this loads updates from the local file system if available).");
        writer.writeLnIndent("</p>");
        writer.endPanel();
        writer.startPanel("Installed Plugins");
        List<PluginHandler> handlers = getHandlers();
        Vector<String> brokenPlugins = new Vector<String>();
        boolean rowType = true;
        writer.writeLnIndent("<table cellpadding='4' cellspacing='0'>");
        for (PluginHandler handler : handlers) {
            PluginDescriptor descriptor = handler.getDescriptor();
            if (descriptor != null) {
                Collection<String> dependentKeys = container.getDependentPlugins(handler.getKey(), false);
                boolean deleteable = dependentKeys.size() == 0;
                List<String> dependentNames = new ArrayList<String>();
                for (String key : dependentKeys) {
                    PluginHandler dependentHandler = container.getHandler(key);
                    dependentNames.add(0, String.format("%1$s (%2$s)", dependentHandler.getName(), dependentHandler.getKey()));
                }
                Collections.sort(dependentNames);
                writer.writeLnIndent(String.format("<tr style='background-color:%1s;'>", rowType ? "#FFF4E4" : "#FFFFFF"));
                rowType = !rowType;
                writer.writeLnIndent("<td valign='top' nowrap>");
                writer.writeLnIndent(String.format("<p style='margin-top:0px; margin-bottom:4px;'><b>%1$s</b></p>", handler.getName()));
                writer.writeLnIndent(String.format("(%1$s)", handler.getKey()));
                writer.addBr();
                if (deleteable) {
                    writer.addImageLink("Remove " + handler.getName(), "?dialog=remove&key=" + handler.getKey(), "/images/config_remove.png", "/images/config_remove_hover.png", 10, true);
                    writer.writeLnIndent("Remove this plugin");
                }
                writer.writeLnIndent("</td>");
                writer.writeLnIndent("<td valign='top'>");
                writer.writeLnIndent(String.format("<b>Description: </b>%1$s", descriptor.getDescription()));
                writer.addBr();
                writer.writeLnIndent(String.format("<b>Version: </b>%1$s", handler.getDescriptor().getVersion()));
                writer.addBr();
                if (!deleteable) {
                    writer.writeLnIndent("<b>Dependent plugins: </b>" + dependentNames.toString().replace("[", "").replace("]", ""));
                }
                writer.writeLnIndent("</td>");
                writer.writeLnIndent("</tr>");
            } else {
                brokenPlugins.add(handler.getKey());
                writer.writeWarning("Unable to load '" + handler.getKey() + "', invalid plugin definition.");
                continue;
            }
        }
        writer.writeLnIndent("</table>");
        writer.endPanel();
    }

    /**
	 * Displays install/update servers. It is possible to add new update servers
	 * by hand; this might be interesting for big company with local update
	 * servers.
	 */
    public void renderInstallAndUpdate(String page, Hashtable<String, String> parameters, HtmlWriter writer) {
        boolean rowType = true;
        writer.startPanel("Install &amp; Update");
        writer.writeLnIndent("<p>");
        writer.writeLnIndent("Below is a list of new plugins and available updates (click button to refresh).");
        writer.writeLnIndent("</p>");
        if (updates.size() > 0) {
            writer.writeLnIndent("<table class='list' cellspacing='0' cellpadding='8'");
            writer.writeLnIndent("<tr>");
            writer.writeLnIndent("<th class='list' align='left' nowrap>");
            writer.writeLnIndent("<b>Name (Key)</b>");
            writer.writeLnIndent("</th>");
            writer.writeLnIndent("<th class='list' align='left' nowrap>");
            writer.writeLnIndent("<b>Version (current)</b>");
            writer.writeLnIndent("</th>");
            writer.writeLnIndent("<th class='list' align='left' nowrap>");
            writer.writeLnIndent("<b>Description</b>");
            writer.writeLnIndent("</th>");
            writer.writeLnIndent("<th class='list' align='left' nowrap>");
            writer.writeLnIndent("<b>Update URL</b>");
            writer.writeLnIndent("</th>");
            writer.writeLnIndent("</tr>");
            for (PluginInfo info : updates) {
                writer.writeLnIndent(String.format("<tr style='background-color:%1s;'>", rowType ? "#FFF4E4" : "#FFFFFF"));
                rowType = !rowType;
                writer.writeLnIndent("<td nowrap valign='top'>");
                writer.writeLnIndent(String.format("%1$s<br>(%2$s)", info.getName(), info.getKey()));
                writer.writeLnIndent("</td>");
                writer.writeLnIndent("<td nowrap  valign='top'>");
                String newVersion = info.getVersion();
                String oldVersion = "new";
                PluginHandler handler = container.getHandler(info.getKey());
                if (handler != null && handler.getDescriptor() != null) {
                    oldVersion = handler.getDescriptor().getVersion();
                }
                writer.writeLnIndent(String.format("%1$s (%2$s)", newVersion, oldVersion));
                writer.writeLnIndent("</td>");
                writer.writeLnIndent("<td valign='top'>");
                writer.writeLnIndent(info.getDescription());
                writer.writeLnIndent("</td>");
                writer.writeLnIndent("<td valign='top'>");
                writer.writeLnIndent(info.getUrl());
                writer.writeLnIndent("</td>");
                writer.writeLnIndent("</tr>");
            }
            writer.writeLnIndent("</table>");
            writer.writeLnIndent("<p>");
            writer.addButton("Install ALL updates", "?action=update");
        } else {
            writer.writeLnIndent("<p>");
            writer.writeLnIndent("<b>No updates available.</b>");
        }
        writer.addButton("Refresh update list", "?action=refreshUpdates");
        writer.writeLnIndent("</p>");
        if (updates.size() > 0) {
            writer.writeLnIndent("<p>");
            writer.writeLnIndent("Note that some updates will freeze this browser view or 'crash' Spamato.");
            writer.writeLnIndent("Unfortunately, we have not seen into this issue yet, so if you encounter");
            writer.writeLnIndent("any problems after updating Spamato, just restart Spamato / your email client.");
            writer.writeLnIndent("</p>");
        }
        if (unreachableServers.size() > 0) {
            writer.writeWarning("<p>The following servers were not reachable:</p>");
            writer.writeLnIndent("<ul>");
            for (String unreachableServer : unreachableServers) {
                writer.writeWarning(String.format("<li>%1$s</li>", unreachableServer));
            }
            writer.writeLnIndent("</ul>");
        }
        writer.endPanel();
        writer.startPanel("Custom Update Servers");
        writer.writeLnIndent("<p>");
        writer.writeLnIndent("Update servers are used for updating plugins.");
        writer.writeLnIndent("Normally you do not have to add servers since every plugin defines its own default one.");
        writer.writeLnIndent("But it can be used, for example, to add local update servers in larger companies.");
        writer.writeLnIndent("Note that the servers you specify here are contacted before the default servers and therefore have a higher priority.");
        writer.writeLnIndent("</p>");
        writer.write("<table class='list' cellspacing='0' cellpadding='4'>");
        writer.writeLnIndent("<tr>");
        writer.writeLnIndent("<th class='list' align='left'>");
        writer.writeLnIndent("Server");
        writer.writeLnIndent("</th>");
        writer.writeLnIndent("<th class='list' align='right'>");
        writer.writeLnIndent("Action");
        writer.writeLnIndent("</th>");
        writer.writeLnIndent("</tr>");
        for (String server : updateServers) {
            writer.writeLnIndent(String.format("<tr style='background-color:%1s;'>", rowType ? "#FFF4E4" : "#FFFFFF"));
            rowType = !rowType;
            writer.writeLnIndent(String.format("<td>%1$s</td>", server));
            writer.writeLnIndent("<td align='right'>");
            writer.addImageLink("Remove " + server, "?action=removeUpdateServer&key=" + server, "/images/delete.gif", "/images/delete_hover.gif", 14);
            writer.writeLnIndent("</td>");
            writer.writeLnIndent("</tr>");
        }
        writer.writeLnIndent("<tr>");
        writer.writeLnIndent("<td style='padding-top:8px;'>");
        writer.addTextField("updateServer", "http://", "30");
        writer.writeLnIndent("</td>");
        writer.writeLnIndent("<td align='right' style='padding-top:8px;'>");
        writer.addSubmitImageLink("add this server", "?action=addUpdateServer", "/images/plus.gif", "/images/plus_hover.gif", 14);
        writer.writeLnIndent("</td>");
        writer.writeLnIndent("</tr>");
        writer.writeLnIndent("</table>");
        writer.writeLnIndent("<p><b>Click 'Save' below to apply your changes.</b></p>");
        writer.endPanel();
    }

    /**
	 * Refreshes the list of all updated and new plugins and the list of
	 * unreachable servers.
	 */
    private void refreshUpdates() {
        clearUpdates();
        PluginInfos infos = container.findPlugins();
        updates.addAll(infos.getUpdatedPlugins());
        updates.addAll(infos.getNewPlugins());
        Collections.sort(updates, new Comparator<PluginInfo>() {

            public int compare(PluginInfo info1, PluginInfo info2) {
                return info1.getName().compareTo(info2.getName());
            }
        });
        unreachableServers.addAll(infos.getUnreachableServers());
        Collections.sort(unreachableServers);
    }

    private void clearUpdates() {
        updates.clear();
        unreachableServers.clear();
    }

    /**
	 * Returns an alphabetically sorted list of all existing plugins. 
	 */
    private List<PluginHandler> getHandlers() {
        Vector<PluginHandler> handlers = new Vector<PluginHandler>((this.spamatoFactory.getContainer()).getHandlers());
        Collections.sort(handlers, new Comparator<PluginHandler>() {

            public int compare(PluginHandler handler1, PluginHandler handler2) {
                if (handler1.getName() != null && handler2.getName() != null) {
                    return handler1.getName().compareTo(handler2.getName());
                } else if (handler1.getName() != null) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        return handlers;
    }

    public void renderDialog(String page, String dialog, Hashtable<String, String> parameters, HtmlWriter writer) {
        if ("remove".equals(dialog)) {
            String key = (String) parameters.get("key");
            PluginHandler handler = ((PluginContainer) this.spamatoFactory.getContainer()).getHandler(key);
            String message = "Are you sure you want to delete the plugin '" + handler.getName() + "'?";
            String actionUrl = "?action=remove&key=" + key;
            writer.renderYesNoDialog(message, actionUrl, ".");
        } else {
            super.renderDialog(page, dialog, parameters, writer);
        }
    }

    public String executeAction(String name, Hashtable<String, String> parameters) {
        ReadWriteLock lock = this.spamatoFactory.getUpdateLock();
        lock.writeLock().lock();
        try {
            String key = (String) parameters.get("key");
            if ("refreshPlugins".equals(name)) {
                spamatoFactory.getContainer().refresh();
            } else if ("remove".equals(name)) {
                this.spamatoFactory.getContainer().deletePlugin(key);
            } else if ("refreshUpdates".equals(name)) {
                refreshUpdates();
            } else if ("update".equals(name)) {
                for (PluginInfo info : updates) {
                    spamatoFactory.getContainer().installPlugin(info, false);
                }
                spamatoFactory.getContainer().refresh();
            } else if ("addUpdateServer".equals(name)) {
                String server = (String) parameters.get("updateServer");
                if (server != null && isSupportedProtocol(PluginContainerImpl.EXTENSION_POINT_SEARCH_PROTOCOLS, server)) this.updateServers.add(server);
            } else if ("removeUpdateServer".equals(name)) {
                this.updateServers.remove(key);
            }
            return "";
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
	 * Returns true if there exists a handler at the extension
	 * for the protocol of the specified server.
	 */
    private boolean isSupportedProtocol(String extensionName, String server) {
        ExtensionPoint extensionPoint = this.container.getHandler("runtime").getPluginContext().getExtensionPoint(extensionName);
        Collection<Extension> extensions = extensionPoint.getExtensions();
        for (Extension extension : extensions) {
            String protocolName = extension.get("@id") + "://";
            if (server.startsWith(protocolName) && server.length() > protocolName.length()) {
                return true;
            }
        }
        return false;
    }

    public void abort() {
        this.updateServers = spamatoFactory.getContainer().getUpdateServers();
        clearUpdates();
    }

    public void save() {
        spamatoFactory.getContainer().setUpdateServers(this.updateServers);
        clearUpdates();
    }

    public Configuration getConfig(String name) {
        return null;
    }
}
