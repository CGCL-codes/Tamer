package org.commsuite.web.beans.servers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javolution.util.FastTable;
import org.apache.log4j.Logger;
import org.commsuite.model.FakedSAPInstanceDef;
import org.commsuite.model.ws.WSMessage;
import org.commsuite.model.ws.WSSAPInstanceDef;
import org.commsuite.util.SpringAdminPanelContext;
import org.commsuite.web.beans.BeansUtils;
import org.commsuite.web.beans.LanguageSelectionBean;
import org.commsuite.ws.ICommunicateWS;
import org.commsuite.ws.WebServiceException;

/**
 * @since 1.0
 * @author Szymon Kuzniak
 */
public class SearchServerBean extends ServerBean {

    public static final String START_ICON_PATH = "/pages/files/gif/media-playback-start.gif";

    public static final String STOP_ICON_PATH = "/pages/files/gif/media-playback-stop.gif";

    public static final String UNKNOWN_ICON_PATH = "/pages/files/gif/help-browser.gif";

    public static final String TEST_FAIL_ICON_PATH = "/pages/files/gif/dialog-warning.gif";

    public static final String TEST_OK_ICON_PATH = "/pages/files/gif/dialog-information.gif";

    private static final int ROWS_ON_PAGE = 5;

    private static final Logger logger = Logger.getLogger(SearchServerBean.class);

    private static final String CHOICE_ALL_LABEL = "CHOICE_ALL";

    private String lastTestException = null;

    private List<ServerExtended> servers;

    private List<String> serverStates = new FastTable<String>();

    private String serverId;

    private boolean searching;

    private int totalServers;

    public int getTotalServers() {
        return totalServers;
    }

    public void setTotalServers(int totalServers) {
        this.totalServers = totalServers;
    }

    /**
     * @return Returns the serverId.
     */
    public String getServerId() {
        return serverId;
    }

    /**
     * @param serverId
     *            The serverId to set.
     */
    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public boolean isSearching() {
        return searching;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
    }

    public SearchServerBean() {
        this.servers = new ArrayList<ServerExtended>();
    }

    public WSSAPInstanceDef createInstanceFromFields() {
        final WSSAPInstanceDef instance = new FakedSAPInstanceDef();
        instance.setAdminEmail(this.adminEmail);
        instance.setClient(this.client);
        instance.setGroup(this.group);
        instance.setGwhost(this.gwhost);
        instance.setGwserv(this.gwserv);
        instance.setHost(this.host);
        instance.setMaxConnectionsInPool(this.maxConnectionsInPool);
        instance.setName(this.name);
        instance.setPassword(this.password);
        instance.setProgid(this.progid);
        instance.setR3name(this.r3name);
        instance.setSystemNumber(this.systemNumber);
        instance.setUser(this.user);
        instance.setActiveInstance(this.activeInstance);
        if (1 == this.getUnicodeSearchIndex()) {
            instance.setUnicode(true);
        } else {
            instance.setUnicode(false);
        }
        if (1 == this.getDefaultSearchIndex()) {
            instance.setDefaultInstance(true);
        } else {
            instance.setDefaultInstance(false);
        }
        if (1 == this.getLoadSearchIndex()) {
            instance.setLoadBalancing(true);
        } else {
            instance.setLoadBalancing(false);
        }
        if (1 == this.getActiveSearchIndex()) {
            instance.setActiveInstance(true);
        } else {
            instance.setActiveInstance(false);
        }
        return instance;
    }

    public List<ServerExtended> getServersAvailable() {
        final ICommunicateWS ws = (ICommunicateWS) SpringAdminPanelContext.getCommunicateWS();
        try {
            Collection<WSSAPInstanceDef> serversList;
            logger.debug("SEARCHING: " + this.searching);
            if (this.searching) {
                final WSSAPInstanceDef instance = this.createInstanceFromFields();
                logger.debug("INSTAMCE NAME: " + instance.getName());
                boolean def = false;
                boolean load = false;
                boolean unicode = false;
                boolean active = false;
                if (this.getUnicodeSearchIndex() < 10) {
                    unicode = true;
                }
                if (this.getDefaultSearchIndex() < 10) {
                    def = true;
                }
                if (this.getLoadSearchIndex() < 10) {
                    load = true;
                }
                if (this.getActiveSearchIndex() < 10) {
                    active = true;
                }
                serversList = this.getServers(instance, def, load, unicode, active, ws);
            } else {
                serversList = this.getServers(null, false, false, false, false, ws);
            }
            this.servers = new FastTable<ServerExtended>();
            int i = 0;
            logger.debug("Servers list size: " + serversList.size());
            for (WSSAPInstanceDef def : serversList) {
                if (null == this.serverId || !def.getId().toString().equals(this.serverId)) {
                    String workingIcon;
                    String label;
                    if (ws.isSapServerWorking(def.getName())) {
                        workingIcon = STOP_ICON_PATH;
                        label = BeansUtils.LABEL_STOP;
                    } else {
                        workingIcon = START_ICON_PATH;
                        label = BeansUtils.LABEL_START;
                    }
                    String active;
                    if (def.isActiveInstance()) {
                        active = BeansUtils.LABEL_ACTIVE;
                    } else {
                        active = BeansUtils.LABEL_INACTIVE;
                    }
                    this.serverStates.add(i, UNKNOWN_ICON_PATH);
                    this.servers.add(i, new ServerExtended(def, this.serverStates.get(i), workingIcon, label, active));
                } else if (null != this.serverId && def.getId().toString().equals(this.serverId)) {
                    final String testResult = ws.serverTest(def.getName());
                    logger.debug("Result of SAP server testing: " + testResult);
                    if (null == testResult) {
                        this.serverStates.set(i, TEST_OK_ICON_PATH);
                    } else {
                        LanguageSelectionBean.showMessageWithException(BeansUtils.SERVER_TEST_FAIL, lastTestException, BeansUtils.SHOW_SERVERS_NAVIGATION);
                        this.serverStates.set(i, TEST_FAIL_ICON_PATH);
                    }
                    String workingIcon;
                    String label;
                    if (ws.isSapServerWorking(def.getName())) {
                        workingIcon = STOP_ICON_PATH;
                        label = BeansUtils.LABEL_STOP;
                    } else {
                        workingIcon = START_ICON_PATH;
                        label = BeansUtils.LABEL_START;
                    }
                    String active;
                    if (def.isActiveInstance()) {
                        active = BeansUtils.LABEL_ACTIVE;
                    } else {
                        active = BeansUtils.LABEL_INACTIVE;
                    }
                    this.servers.add(i, new ServerExtended(def, this.serverStates.get(i), workingIcon, label, active));
                }
                i++;
            }
            this.serverId = null;
            return this.servers;
        } catch (WebServiceException wse) {
            logger.fatal("Exception while receiving available servers", wse);
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_DATABASE, BeansUtils.MESSAGE_SERVER_ERROR_FETCH, BeansUtils.SEARCH_SERVER_NAVIGATION);
            return new ArrayList<ServerExtended>();
        } catch (Exception e) {
            logger.fatal("Exception while receiving available servers", e);
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_DATABASE, BeansUtils.MESSAGE_SERVER_ERROR_FETCH, BeansUtils.SEARCH_SERVER_NAVIGATION);
            return new ArrayList<ServerExtended>();
        }
    }

    public void selectServer(ActionEvent event) {
        try {
            this.serverId = getParameter("serverId");
        } catch (IllegalArgumentException iae) {
            logger.fatal("Wrong parameter name", iae);
        }
    }

    public void doSearching(ActionEvent e) {
        try {
            final String val = getParameter("searching");
            logger.debug("PARAMETER: " + val);
            this.searching = "true".equals(val);
        } catch (IllegalArgumentException iae) {
            logger.fatal("Wrong parameter name", iae);
        }
    }

    public String startStopServer() {
        final ICommunicateWS ws = SpringAdminPanelContext.getCommunicateWS();
        try {
            for (ServerExtended server : this.servers) {
                if (this.serverId.equals(String.valueOf(server.getInstance().getId()))) {
                    if (START_ICON_PATH.equals(server.getWorking())) {
                        logger.debug("STARTING SERVER");
                        ws.startSapServer(server.getInstance().getName());
                    } else {
                        logger.debug("STOPING SERVER");
                        ws.stopSapServer(server.getInstance().getName());
                    }
                }
            }
            this.serverId = null;
        } catch (WebServiceException wse) {
            logger.fatal("Exception while starting/stoping server instance", wse);
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_DATABASE, BeansUtils.MESSAGE_SERVER_ERROR_START, BeansUtils.SEARCH_SERVER_NAVIGATION);
        } catch (Exception e) {
            logger.fatal("Exception", e);
            LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_DATABASE, BeansUtils.MESSAGE_SERVER_ERROR_START, BeansUtils.SEARCH_SERVER_NAVIGATION);
        }
        return BeansUtils.SEARCH_SERVER_NAVIGATION;
    }

    public String deleteServer() {
        final ICommunicateWS ws = SpringAdminPanelContext.getCommunicateWS();
        try {
            Collection<WSMessage> messages = ws.getMessagesByServer(this.serverId);
            for (WSMessage message : messages) {
                ws.deleteMessage(message.getId().toString());
            }
            ws.deleteServer(this.serverId);
        } catch (WebServiceException wse) {
            logger.fatal("Cannot delete server", wse);
            if (wse.getMessage().equals("error in deleteServer method")) {
                LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_SERVER_MESSAGES, BeansUtils.SEARCH_SERVER_NAVIGATION);
            } else {
                LanguageSelectionBean.showMessage(BeansUtils.MESSAGE_ERROR_DATABASE, BeansUtils.SEARCH_SERVER_NAVIGATION);
            }
            this.serverId = null;
        }
        return BeansUtils.SEARCH_SERVER_NAVIGATION;
    }

    private List<WSSAPInstanceDef> getServers(WSSAPInstanceDef model, boolean def, boolean load, boolean unicode, boolean active, ICommunicateWS ws) throws WebServiceException {
        this.totalServers = ws.getInstancesSize(model, def, load, unicode, active);
        this.totalPages = ((this.totalServers - 1) / ROWS_ON_PAGE) + 1;
        int begin = (this.actualPage - 1) * ROWS_ON_PAGE;
        int end = (this.actualPage * ROWS_ON_PAGE) - 1;
        if (end >= this.totalServers) end = (this.totalServers - 1);
        logger.debug("begin & end: " + begin + " " + end);
        if (null == model) {
            return (List<WSSAPInstanceDef>) ws.getInstancesSubset(begin, end);
        } else {
            return (List<WSSAPInstanceDef>) ws.getSelectedInstancesSubset(model, def, load, unicode, active, begin, end);
        }
    }

    public List<SelectItem> getBooleanSearchList() {
        List<SelectItem> items = new FastTable<SelectItem>();
        SelectItem item = new SelectItem();
        item = new SelectItem();
        item.setLabel("true");
        item.setValue(1);
        items.add(item);
        item = new SelectItem();
        item.setLabel("false");
        item.setValue(0);
        items.add(item);
        item = new SelectItem();
        item.setLabel(LanguageSelectionBean.getLabel(SearchServerBean.CHOICE_ALL_LABEL));
        item.setValue(10);
        items.add(item);
        return items;
    }
}
