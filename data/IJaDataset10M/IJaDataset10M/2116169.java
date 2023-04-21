package com.acgvision.configurator.ws;

import com.acgvision.core.wsparam.AutomaticAction;
import com.acgvision.core.wsparam.Average;
import com.acgvision.core.wsparam.Command;
import com.acgvision.core.wsparam.Cpu;
import com.acgvision.core.wsparam.FileSystem;
import com.acgvision.core.wsparam.Groupe;
import com.acgvision.core.wsparam.Host;
import com.acgvision.core.wsparam.Job;
import com.acgvision.core.wsparam.LimitValue;
import com.acgvision.core.wsparam.Memory;
import com.acgvision.core.wsparam.Parametror;
import com.acgvision.core.wsparam.ParametrorService;
import com.acgvision.core.wsparam.Period;
import com.acgvision.core.wsparam.Script;
import com.acgvision.core.wsparam.Service;
import com.acgvision.core.wsparam.UpdateJobs;
import com.acgvision.core.wsparam.User;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.log4j.Logger;

/**
 *
 * @author Rémi Debay <remi.debay@acgcenter.com>
 */
public class WebService {

    private static final String pack = "http://wsparam.core.acgvision.com/";

    private static final String sName = "ParametrorService";

    private static QName qName = new QName(pack, sName);

    private static final Logger logger = Logger.getLogger(WebService.class);

    private URL urlNameService;

    private com.acgvision.core.wsparam.ParametrorService parametreurService = null;

    private com.acgvision.core.wsparam.Parametror Port = null;

    /**
     *
     * @param url
     * @return
     */
    public boolean init(String url) {
        try {
            logger.debug("Connect to webservice");
            if (logger.isInfoEnabled()) {
                logger.info(url);
            }
            urlNameService = new java.net.URL(url);
            this.getPort();
            if (logger.isInfoEnabled()) {
                logger.info("WebService wsdl location : " + this.parametreurService.getWSDLDocumentLocation());
                logger.info("WebService Service name : " + this.parametreurService.getServiceName().toString());
            }
            return true;
        } catch (MalformedURLException ex) {
            logger.fatal("Error while trying to contact webservice.");
            logger.fatal(ex);
            logger.fatal(ex.getStackTrace().toString());
            return false;
        }
    }

    /**
     * returns commands stored in acgvision server
     * @return
     */
    public List<Command> getCommands() {
        logger.info("getCommands");
        java.util.List<com.acgvision.core.wsparam.Command> result = null;
        try {
            Parametror port = getPort();
            result = port.getCommands();
            logger.info("Result = " + result);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
        return result;
    }

    /**
     *
     * @param commands
     */
    public void saveCommands(List<Command> commands) {
        logger.info("saveCommands");
        java.util.List<com.acgvision.core.wsparam.Command> result = null;
        try {
            Parametror port = getPort();
            port.saveCommands(commands);
            logger.info("Result = " + result);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
    }

    /**
     * returns commands stored in acgvision server
     * @return
     */
    public List<LimitValue> getLimitValues() {
        logger.info("getLimitValues");
        List<LimitValue> result = null;
        try {
            Parametror port = getPort();
            result = port.getLimitValues();
            logger.info("Result = " + result);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
        return result;
    }

    public List<Average> getAverages() {
        logger.info("getAverages");
        List<Average> result = null;
        try {
            Parametror port = getPort();
            result = port.getAverages();
            logger.info("Result = " + result);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
        return result;
    }

    public void save(LimitValue l) {
        logger.info("save " + l.getName());
        try {
            Parametror port = getPort();
            port.saveLimitValue(l);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
    }

    public void save(Average l) {
        logger.info("save " + l.getName());
        try {
            Parametror port = getPort();
            port.saveAverage(l);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
    }

    /**
     * returns commands stored in acgvision server
     * @return
     */
    public List<Period> getPeriods() {
        logger.info("getPeriods");
        List<Period> result = null;
        try {
            Parametror port = getPort();
            result = port.getPeriods();
            logger.info("Result = " + result);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
        return result;
    }

    public List<UpdateJobs> getUpdates() {
        logger.info("getUpdates");
        java.util.List<com.acgvision.core.wsparam.UpdateJobs> result = null;
        try {
            Parametror port = getPort();
            result = port.getUpdates();
            logger.info("Result = " + result);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
        return result;
    }

    public List<Cpu> getCpus() {
        logger.info("getCpus");
        java.util.List<com.acgvision.core.wsparam.Cpu> result = null;
        try {
            Parametror port = getPort();
            result = port.getCpus();
            logger.info("Result = " + result);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
        return result;
    }

    public List<Memory> getMemories() {
        logger.info("getMemories");
        java.util.List<com.acgvision.core.wsparam.Memory> result = null;
        try {
            Parametror port = getPort();
            result = port.getMemories();
            logger.info("Result = " + result);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
        return result;
    }

    public List<FileSystem> getfileSystems() {
        logger.info("getfileSystems");
        java.util.List<com.acgvision.core.wsparam.FileSystem> result = null;
        try {
            Parametror port = getPort();
            result = port.getFileSystems();
            logger.info("Result = " + result);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
        return result;
    }

    public List<Script> getScripts() {
        logger.info("getScripts");
        java.util.List<com.acgvision.core.wsparam.Script> result = null;
        try {
            Parametror port = getPort();
            result = port.getScripts();
            logger.info("Result = " + result);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
        return result;
    }

    public void save(UpdateJobs l) {
        logger.info("save " + l.getName());
        try {
            Parametror port = getPort();
            com.acgvision.core.wsparam.UpdateJobs updateJobs = l;
            port.saveUpdate(updateJobs);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
    }

    public void save(Cpu l) {
        logger.info("save " + l.getName());
        try {
            Parametror port = getPort();
            port.saveCpu(l);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
    }

    public void save(Memory l) {
        logger.info("save " + l.getName());
        try {
            Parametror port = getPort();
            port.saveMemory(l);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
    }

    public void save(FileSystem l) {
        logger.info("save " + l.getName());
        try {
            Parametror port = getPort();
            port.saveFileSystem(l);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
    }

    public void save(Script l) {
        logger.info("save " + l.getName());
        try {
            Parametror port = getPort();
            port.saveScript(l);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
    }

    public void save(com.acgvision.core.wsparam.Service s) {
        logger.info("save " + s.getName());
        try {
            Parametror port = getPort();
            port.saveService(s);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
    }

    public List<Service> getServices() {
        logger.info("getServices");
        List<Service> services = new ArrayList<Service>();
        try {
            Parametror port = getPort();
            services = port.getJobServices();
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
        return services;
    }

    /**
       *
       * @return
       */
    public List<Job> getJobs() {
        logger.debug("getJobs");
        List<Job> jobs = new ArrayList<Job>();
        try {
            Parametror port = getPort();
            jobs = port.getJobs();
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
        return jobs;
    }

    /**
 *
 * @return
 */
    private Parametror getPort() {
        logger.debug("getPort");
        if (this.Port == null) {
            if (this.urlNameService != null) {
                try {
                    logger.info(this.urlNameService);
                    logger.info(qName);
                    this.parametreurService = new com.acgvision.core.wsparam.ParametrorService(urlNameService, qName);
                } catch (javax.xml.ws.WebServiceException ex) {
                    logger.error("Error while connecting to WSDL.", ex);
                }
                Port = this.parametreurService.getParametrorPort();
                return Port;
            } else {
                throw new java.lang.NullPointerException();
            }
        } else {
            return Port;
        }
    }

    public List<AutomaticAction> getAutomaticActions() {
        logger.info("getAutomaticActions");
        List<AutomaticAction> actions = new ArrayList<AutomaticAction>();
        try {
            Parametror port = getPort();
            actions = port.getAutomaticActions();
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
            actions = new ArrayList<AutomaticAction>();
        }
        return actions;
    }

    public void save(AutomaticAction l) {
        logger.info("save " + l.getName());
        List<AutomaticAction> actions = new ArrayList<AutomaticAction>();
        try {
            Parametror port = getPort();
            port.saveAutomaticAction(l);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
    }

    public List<Groupe> getGroupes() {
        logger.debug("getGroupes");
        List<Groupe> groupes = new ArrayList<Groupe>();
        try {
            Parametror port = getPort();
            groupes = port.getGroups();
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
        return groupes;
    }

    public List<Host> getHosts() {
        logger.debug("getHosts");
        List<Host> hosts = new ArrayList<Host>();
        try {
            Parametror port = getPort();
            hosts = port.getHosts();
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
        return hosts;
    }

    public List<User> getUsers() {
        logger.debug("getUsers");
        List<User> users = new ArrayList<User>();
        try {
            Parametror port = getPort();
            users = port.getUsers();
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
        return users;
    }

    public void save(Groupe l) {
        logger.info("save " + l.getName());
        try {
            Parametror port = getPort();
            port.saveGroupe(l);
        } catch (Exception ex) {
            logger.error("Error using webservice", ex);
        }
    }
}
