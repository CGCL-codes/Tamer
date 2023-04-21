package edu.asu.vspace.dspace.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observer;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;
import java.util.UUID;
import de.mpiwg.vspace.common.project.ProjectManager;
import de.mpiwg.vspace.common.project.ProjectObservable;
import de.mpiwg.vspace.extension.ExceptionHandlingService;
import de.mpiwg.vspace.util.PropertyHandler;
import de.mpiwg.vspace.util.PropertyHandlerRegistry;
import edu.asu.vspace.dspace.Activator;
import edu.asu.vspace.dspace.Constants;

public class DSpaceHostManager extends Observable {

    protected static final String DSPACE_HOST_FILE = "dspaceHosts.properties";

    protected static final String DSPACE_HOST_INFO_SEPARATOR = "$#$";

    protected static final String DSPACE_HOST_INFO_SEPARATOR_REGEX = "\\$\\#\\$";

    List<DSpaceHost> dspaceHosts;

    public static final DSpaceHostManager INSTANCE = new DSpaceHostManager();

    PropertyHandler dspaceHostHandler;

    private DSpaceHostManager() {
    }

    protected void init() {
        dspaceHosts = new ArrayList<DSpaceHost>();
        File folder = ProjectManager.getInstance().getFolder(Constants.DSPACE_FOLDER_NAME);
        if (folder == null) return;
        PropertyHandler handler = PropertyHandlerRegistry.REGISTRY.getPropertyHandler(Activator.PLUGIN_ID, Constants.PROPERTIES_FILE);
        File hostsFile = new File(folder.getAbsolutePath() + File.separator + DSPACE_HOST_FILE);
        if (!hostsFile.exists()) {
            try {
                hostsFile.createNewFile();
            } catch (IOException e) {
                ExceptionHandlingService.INSTANCE.handleException(handler.getProperty("_dspace_host_manager_file_creation_error"), e);
                return;
            }
        }
        dspaceHostHandler = PropertyHandlerRegistry.REGISTRY.getPropertyHandler(hostsFile);
        Set<Entry<Object, Object>> hosts = dspaceHostHandler.getProperties();
        Iterator<Entry<Object, Object>> it = hosts.iterator();
        while (it.hasNext()) {
            Entry<Object, Object> entry = it.next();
            String id = entry.getKey().toString();
            String values = entry.getValue().toString();
            String[] valuesSplit = values.split(DSPACE_HOST_INFO_SEPARATOR_REGEX);
            if (valuesSplit.length != 3) continue;
            Integer port = null;
            if (!valuesSplit[1].equals("")) port = new Integer(valuesSplit[1]);
            DSpaceHost newHost = new DSpaceHost(id, valuesSplit[0], port, valuesSplit[2]);
            dspaceHosts.add(newHost);
        }
    }

    public DSpaceHost[] getDSpaceHosts() {
        if (dspaceHosts == null) return null;
        return dspaceHosts.toArray(new DSpaceHost[dspaceHosts.size()]);
    }

    /**
	 * Adds a new dspace host to the host list.
	 * 
	 * @param host
	 *            the new host to be added (id not set)
	 * @return the host with a set id
	 */
    public DSpaceHost addDSpaceHost(DSpaceHost host) {
        if (dspaceHostHandler == null) return null;
        String id = null;
        while (id == null) {
            id = UUID.randomUUID().toString();
            String doesIdExist = dspaceHostHandler.getProperty(id);
            if (!doesIdExist.trim().equals("")) id = null;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(host.getHost());
        sb.append(DSPACE_HOST_INFO_SEPARATOR);
        sb.append(host.getPort() != null ? host.getPort() : "");
        sb.append(DSPACE_HOST_INFO_SEPARATOR);
        sb.append(host.getLniService());
        host.setId(id);
        dspaceHostHandler.setProperty(id, sb.toString());
        dspaceHostHandler.save();
        dspaceHosts.add(host);
        setChanged();
        notifyObservers(host);
        return host;
    }

    public void deleteDSpaceHost(DSpaceHost host) {
        if (!dspaceHosts.contains(host)) return;
        dspaceHosts.remove(host);
        dspaceHostHandler.removeProperty(host.getId());
        dspaceHostHandler.save();
        setChanged();
        notifyObservers(host);
    }

    public void update(Object arg) {
        if (arg instanceof Integer && (Integer) arg == ProjectObservable.PROJECT_CLOSED) {
            dspaceHosts.clear();
            dspaceHostHandler = null;
        } else init();
        setChanged();
        notifyObservers();
    }

    public DSpaceHost getDSpaceHostByID(String id) {
        for (DSpaceHost host : dspaceHosts) {
            if (host.getId().equals(id)) return host;
        }
        return null;
    }
}
