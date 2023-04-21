package org.opennms.web.admin.pollerConfig;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.opennms.netmgt.ConfigFileConstants;
import org.opennms.netmgt.config.CapsdConfig;
import org.opennms.netmgt.config.CapsdConfigFactory;
import org.opennms.netmgt.config.PollerConfig;
import org.opennms.netmgt.config.PollerConfigFactory;
import org.opennms.netmgt.config.capsd.CapsdConfiguration;
import org.opennms.netmgt.config.capsd.ProtocolPlugin;
import org.opennms.netmgt.config.poller.Monitor;
import org.opennms.netmgt.config.poller.PollerConfiguration;
import org.opennms.netmgt.config.poller.Service;
import org.opennms.web.Util;

/**
 * A servlet that handles managing or unmanaging interfaces and services on a
 * node
 * 
 * @author <A HREF="mailto:jacinta@opennms.org">Jacinta Remedios </A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 */
public class AddPollerConfigServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    PollerConfiguration pollerConfig = null;

    CapsdConfiguration capsdConfig = null;

    protected String redirectSuccess;

    Map<String, Service> pollerServices = new HashMap<String, Service>();

    Map<String, ProtocolPlugin> capsdProtocols = new HashMap<String, ProtocolPlugin>();

    List<ProtocolPlugin> capsdColl = new ArrayList<ProtocolPlugin>();

    org.opennms.netmgt.config.poller.Package pkg = null;

    Collection<ProtocolPlugin> pluginColl = null;

    Properties props = new Properties();

    PollerConfig pollerFactory = null;

    CapsdConfig capsdFactory = null;

    boolean errorflag = false;

    public void init() throws ServletException {
        ServletConfig config = this.getServletConfig();
        try {
            props.load(new FileInputStream(ConfigFileConstants.getFile(ConfigFileConstants.POLLER_CONF_FILE_NAME)));
            PollerConfigFactory.init();
            pollerFactory = PollerConfigFactory.getInstance();
            pollerConfig = pollerFactory.getConfiguration();
            if (pollerConfig == null) {
                errorflag = true;
                throw new ServletException("Poller Configuration file is empty");
            }
            CapsdConfigFactory.init();
            capsdFactory = CapsdConfigFactory.getInstance();
            capsdConfig = capsdFactory.getConfiguration();
            if (capsdConfig == null) {
                errorflag = true;
                throw new ServletException("Capsd Configuration file is empty");
            }
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
        initPollerServices();
        initCapsdProtocols();
        this.redirectSuccess = config.getInitParameter("redirect.success");
        if (this.redirectSuccess == null) {
            throw new ServletException("Missing required init parameter: redirect.success");
        }
    }

    public void reloadFiles() throws ServletException {
        ServletConfig config = this.getServletConfig();
        try {
            props.load(new FileInputStream(ConfigFileConstants.getFile(ConfigFileConstants.POLLER_CONF_FILE_NAME)));
            PollerConfigFactory.init();
            pollerFactory = PollerConfigFactory.getInstance();
            pollerConfig = pollerFactory.getConfiguration();
            if (pollerConfig == null) {
                errorflag = true;
                throw new ServletException("Poller Configuration file is empty");
            }
            CapsdConfigFactory.init();
            capsdFactory = CapsdConfigFactory.getInstance();
            capsdConfig = capsdFactory.getConfiguration();
            if (capsdConfig == null) {
                errorflag = true;
                throw new ServletException("Capsd Configuration file is empty");
            }
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
        initPollerServices();
        initCapsdProtocols();
        this.redirectSuccess = config.getInitParameter("redirect.success");
        if (this.redirectSuccess == null) {
            throw new ServletException("Missing required init parameter: redirect.success");
        }
    }

    public void initCapsdProtocols() {
        capsdProtocols = new HashMap<String, ProtocolPlugin>();
        pluginColl = getProtocolPlugins();
        if (pluginColl != null) {
            Iterator<ProtocolPlugin> pluginiter = pluginColl.iterator();
            while (pluginiter.hasNext()) {
                ProtocolPlugin plugin = pluginiter.next();
                capsdColl.add(plugin);
                capsdProtocols.put(plugin.getProtocol(), plugin);
            }
        }
    }

    private List<ProtocolPlugin> getProtocolPlugins() {
        return capsdConfig.getProtocolPluginCollection();
    }

    public void initPollerServices() {
        pollerServices = new HashMap<String, Service>();
        Collection<org.opennms.netmgt.config.poller.Package> packageColl = getPackages();
        if (packageColl != null) {
            Iterator<org.opennms.netmgt.config.poller.Package> pkgiter = packageColl.iterator();
            if (pkgiter.hasNext()) {
                pkg = pkgiter.next();
                Collection<Service> svcColl = getServicesForPackage();
                Iterator<Service> svcIter = svcColl.iterator();
                Service svcProp = null;
                while (svcIter.hasNext()) {
                    svcProp = svcIter.next();
                    pollerServices.put(svcProp.getName(), svcProp);
                }
            }
        }
    }

    private List<Service> getServicesForPackage() {
        return pkg.getServiceCollection();
    }

    private List<org.opennms.netmgt.config.poller.Package> getPackages() {
        return pollerConfig.getPackageCollection();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user_id = request.getRemoteUser();
        errorflag = false;
        reloadFiles();
        {
            String check1 = request.getParameter("check1");
            String name1 = request.getParameter("name1");
            String protoArray1 = request.getParameter("protArray1");
            String port1 = request.getParameter("port1");
            List<String> checkedList = new ArrayList<String>();
            if (name1 != null && !name1.equals("")) {
                addPollerInfo(check1, name1, port1, user_id, protoArray1, response, request);
                if (errorflag) {
                    return;
                }
                checkedList.add(name1);
                addCapsdInfo(name1, port1, user_id, protoArray1, response, request);
                if (!errorflag) {
                    props.setProperty("service." + name1 + ".protocol", protoArray1);
                } else {
                    return;
                }
            }
            props.store(new FileOutputStream(ConfigFileConstants.getFile(ConfigFileConstants.POLLER_CONF_FILE_NAME)), null);
            FileWriter poller_fileWriter = new FileWriter(ConfigFileConstants.getFile(ConfigFileConstants.POLLER_CONFIG_FILE_NAME));
            FileWriter capsd_fileWriter = new FileWriter(ConfigFileConstants.getFile(ConfigFileConstants.CAPSD_CONFIG_FILE_NAME));
            try {
                Marshaller.marshal(pollerConfig, poller_fileWriter);
                Marshaller.marshal(capsdConfig, capsd_fileWriter);
            } catch (MarshalException e) {
                throw new ServletException(e);
            } catch (ValidationException e) {
                throw new ServletException(e);
            }
        }
        if (!errorflag) {
            response.sendRedirect(this.redirectSuccess);
        }
    }

    @SuppressWarnings("null")
    public void addCapsdInfo(String name, String port, String user, String protocol, HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
        Collection<ProtocolPlugin> tmpCapsd = getProtocolPlugins();
        Iterator<ProtocolPlugin> iter = tmpCapsd.iterator();
        Service pollersvc = null;
        while (iter.hasNext()) {
            ProtocolPlugin svc = iter.next();
            if (svc.getProtocol().equals(name)) {
                Collection<Service> tmpPollers = getServicesForPackage();
                Iterator<Service> polleriter = tmpPollers.iterator();
                boolean removePoller = false;
                while (polleriter.hasNext()) {
                    pollersvc = polleriter.next();
                    if (pollersvc.getName().equals(name)) {
                        removePoller = true;
                        break;
                    }
                }
                if (removePoller) {
                    Collection<Service> tmpPoller = getServicesForPackage();
                    if (tmpPoller.contains(pollersvc) && pollersvc.getName().equals(name)) {
                        errorflag = true;
                        tmpPoller.remove(pollersvc);
                        response.sendRedirect(Util.calculateUrlBase(request) + "/admin/error.jsp?error=1&name=" + name);
                        return;
                    }
                }
                break;
            }
        }
        ProtocolPlugin pluginAdd = new ProtocolPlugin();
        pluginAdd.setProtocol(name);
        String className = (String) props.get("service." + protocol + ".capsd-class");
        if (className != null) {
            pluginAdd.setClassName(className);
            pluginAdd.setScan("on");
            pluginAdd.setUserDefined("true");
            org.opennms.netmgt.config.capsd.Property newprop = new org.opennms.netmgt.config.capsd.Property();
            String banner = "*";
            if (props.get("banner") != null) {
                banner = (String) props.get("banner");
            }
            newprop.setValue(banner);
            newprop.setKey("banner");
            pluginAdd.addProperty(newprop);
            newprop = new org.opennms.netmgt.config.capsd.Property();
            if (port != null && !port.equals("")) {
                newprop.setValue(port);
                if (port.indexOf(":") == -1) {
                    newprop.setKey("port");
                } else {
                    newprop.setKey("ports");
                }
                pluginAdd.addProperty(newprop);
            } else {
                if (props.get("service." + protocol + ".port") == null || ((String) props.get("service." + protocol + ".port")).equals("")) {
                    errorflag = true;
                    response.sendRedirect(Util.calculateUrlBase(request) + "/admin/error.jsp?error=0&name=" + "service." + protocol + ".port ");
                    pluginAdd = null;
                    return;
                } else {
                    port = (String) props.get("service." + protocol + ".port");
                    newprop.setValue(port);
                    if (port.indexOf(":") == -1) {
                        newprop.setKey("port");
                    } else {
                        newprop.setKey("ports");
                    }
                    pluginAdd.addProperty(newprop);
                }
            }
            newprop = new org.opennms.netmgt.config.capsd.Property();
            String timeout = "3000";
            if (props.get("timeout") != null) {
                timeout = (String) props.get("timeout");
            }
            newprop.setValue(timeout);
            newprop.setKey("timeout");
            if (pluginAdd != null) {
                pluginAdd.addProperty(newprop);
            }
            newprop = new org.opennms.netmgt.config.capsd.Property();
            String retry = "3";
            if (props.get("retry") != null) {
                retry = (String) props.get("retry");
            }
            newprop.setValue(retry);
            newprop.setKey("retry");
            if (pluginAdd != null) {
                pluginAdd.addProperty(newprop);
                capsdProtocols.put(name, pluginAdd);
                pluginColl = capsdProtocols.values();
                capsdColl.add(pluginAdd);
                capsdConfig.addProtocolPlugin(pluginAdd);
            }
        } else {
            errorflag = true;
            response.sendRedirect(Util.calculateUrlBase(request) + "/admin/error.jsp?error=0&name=" + "service." + protocol + ".capsd-class ");
            return;
        }
    }

    public void addPollerInfo(String bPolled, String name, String port, String user, String protocol, HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
        Collection<Service> tmpPollers = getServicesForPackage();
        Iterator<Service> iter = tmpPollers.iterator();
        while (iter.hasNext()) {
            Service svc = iter.next();
            if (svc.getName().equals(name)) {
                errorflag = true;
                response.sendRedirect(Util.calculateUrlBase(request) + "/admin/error.jsp?error=1&name=" + name);
                return;
            }
        }
        if (pkg != null) {
            Service newService = new Service();
            newService.setName(name);
            if (bPolled != null) {
                newService.setStatus(bPolled);
            } else {
                newService.setStatus("off");
            }
            newService.setName(name);
            newService.setUserDefined("true");
            Collection<Monitor> monitorColl = getMonitors();
            Monitor newMonitor = new Monitor();
            String monitor = (String) props.get("service." + protocol + ".monitor");
            if (monitor != null) {
                newMonitor.setService(name);
                newMonitor.setClassName(monitor);
            } else {
                errorflag = true;
                response.sendRedirect(Util.calculateUrlBase(request) + "/admin/error.jsp?error=0&name=" + "service." + protocol + ".monitor ");
                return;
            }
            if (props.get("interval") != null) {
                newService.setInterval((new Long((String) props.get("interval"))).longValue());
            } else {
                newService.setInterval(300000);
            }
            org.opennms.netmgt.config.poller.Parameter newprop = new org.opennms.netmgt.config.poller.Parameter();
            String timeout = "3000";
            if (props.get("timeout") != null) {
                timeout = (String) props.get("timeout");
            }
            newprop.setValue(timeout);
            newprop.setKey("timeout");
            newService.addParameter(newprop);
            newprop = new org.opennms.netmgt.config.poller.Parameter();
            String banner = "*";
            if (props.get("banner") != null) {
                banner = (String) props.get("banner");
            }
            newprop.setValue(banner);
            newprop.setKey("banner");
            newService.addParameter(newprop);
            newprop = new org.opennms.netmgt.config.poller.Parameter();
            String retry = "3";
            if (props.get("retry") != null) {
                retry = (String) props.get("retry");
            }
            newprop.setValue(retry);
            newprop.setKey("retry");
            newService.addParameter(newprop);
            newprop = new org.opennms.netmgt.config.poller.Parameter();
            if (port == null || port.equals("")) {
                if (props.get("service." + protocol + ".port") == null || ((String) props.get("service." + protocol + ".port")).equals("")) {
                    errorflag = true;
                    newMonitor = null;
                    newService = null;
                    response.sendRedirect(Util.calculateUrlBase(request) + "/admin/error.jsp?error=0&name=" + "service." + protocol + ".port");
                    return;
                } else {
                    port = (String) props.get("service." + protocol + ".port");
                }
            }
            newprop.setValue(port);
            if (port.indexOf(":") != -1) {
                newprop.setKey("ports");
            } else {
                newprop.setKey("port");
            }
            if (newMonitor != null && newService != null) {
                if (monitorColl == null) {
                    pollerConfig.addMonitor(0, newMonitor);
                } else {
                    pollerConfig.addMonitor(newMonitor);
                }
                newService.addParameter(newprop);
                pkg.addService(newService);
            }
        }
    }

    private List<Monitor> getMonitors() {
        return pollerConfig.getMonitorCollection();
    }
}
