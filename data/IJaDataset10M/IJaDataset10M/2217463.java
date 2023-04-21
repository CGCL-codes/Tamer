package org.easyrec.service.web;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import org.easyrec.arm.AssocRuleMiningService;
import org.easyrec.model.web.Operator;
import org.easyrec.model.web.Queue;
import org.easyrec.model.web.RemoteTenant;
import org.easyrec.store.dao.web.OperatorDAO;
import org.easyrec.store.dao.web.RemoteTenantDAO;
import org.springframework.beans.factory.InitializingBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easyrec.arm.store.dao.RuleMinerLogDAO;
import org.easyrec.model.web.EasyRecSettings;
import org.easyrec.plugin.configuration.ConfigurationHelper;
import org.easyrec.plugin.container.PluginRegistry;
import org.easyrec.plugin.generator.Generator;
import org.easyrec.plugin.generator.GeneratorConfiguration;
import org.easyrec.plugin.stats.GeneratorStatistics;
import org.easyrec.service.core.TenantService;
import org.springframework.beans.factory.DisposableBean;

/**
 * This class schedules an attached ruleminer for each tenant.
 *
 * All tenants, that have an active autoruleminer scheduler flag are added
 * to the ruleminer TaskList. The Ruleminer TaskList is a Hashmap that contains
 * the tenant id as key and a Ruleminer Task as value.
 *
 * A Ruleminer Task adds a tenant to the execution queue at its execution time.
 *
 * The queue holds a list of all tenants that are waiting to be 'rulemined'.
 * After a tenant is processed by the ruleminer, he is removed from the queue.
 *
 * @author phlavac
 */
public class RuleMinerScheduler implements InitializingBean, DisposableBean {

    private static final int SCHEDULER_PAUSE = 30 * 1000;

    private final Log logger = LogFactory.getLog(getClass());

    private RemoteTenantDAO remoteTenantDAO;

    private OperatorDAO operatorDAO;

    private HashMap<Integer, RuleminerTimerTask> ruleMinerTasks;

    private AssocRuleMiningService assocRuleMiningService;

    private RuleMinerLogDAO ruleMinerLogDAO;

    private Queue queue;

    private TenantService tenantService;

    private RemoteTenantService remoteTenantService;

    private EasyRecSettings easyrecSettings;

    private PluginRegistry pluginRegistry;

    private Scheduler scheduler;

    public RuleMinerScheduler() {
        queue = new Queue();
    }

    /** 
     * Init Autoruleminer timertasks for all Tenants.
     * @throws java.lang.Exception
     */
    public void afterPropertiesSet() throws Exception {
        if (easyrecSettings.isRuleminer()) {
            initTasks();
            ruleMinerLogDAO.setAllDone();
            scheduler = new Scheduler(queue, assocRuleMiningService);
            scheduler.start();
            logger.info("Rulemining Scheduler started.");
        }
    }

    /**
     * Shut down scheduler
     * @throws Exception
     */
    public void destroy() throws Exception {
        Thread interruptThread = scheduler;
        scheduler = null;
        interruptThread.interrupt();
        for (RuleminerTimerTask task : ruleMinerTasks.values()) {
            task.destroy();
            task = null;
        }
        ruleMinerTasks.clear();
        logger.info("Shutting down RuleminerScheduler completed");
    }

    /**
     * Add a ruleminer Task to a Tenant     
     */
    public void addTask(RemoteTenant remoteTenant) {
        if (ruleMinerTasks != null) {
            ruleMinerTasks.put(remoteTenant.getId(), new RuleminerTimerTask(remoteTenant, queue));
        }
    }

    /**
     * Update a tenant's ruleminer task
     * @param remoteTenant
     * @param enabled
     * @param executionTime
     */
    public void updateTask(RemoteTenant remoteTenant) {
        boolean tenantInTaskList = false;
        if (ruleMinerTasks != null) {
            RuleminerTimerTask ruleMinerTask = (RuleminerTimerTask) ruleMinerTasks.get(remoteTenant.getId());
            if (ruleMinerTask != null) {
                tenantInTaskList = true;
                ruleMinerTask.destroy();
                ruleMinerTasks.remove(remoteTenant.getId());
            }
            if (remoteTenant.isAutoRuleminingEnabled()) {
                ruleMinerTasks.put(remoteTenant.getId(), new RuleminerTimerTask(remoteTenant, queue));
                if (!tenantInTaskList) {
                    logger.info("'" + remoteTenant.getOperatorId() + " - " + remoteTenant.getStringId() + "' added to RuleminerTask List");
                }
            } else {
                if (tenantInTaskList) {
                    logger.info("'" + remoteTenant.getOperatorId() + " - " + remoteTenant.getStringId() + "' removed from Ruleminertask List");
                }
            }
        }
    }

    /**
     * Stops a tenant's ruleminer task
     * @param remoteTenant
     * @param enabled
     * @param executionTime
     */
    public void stopTask(RemoteTenant remoteTenant) {
        if (ruleMinerTasks != null) {
            RuleminerTimerTask ruleMinerTask = (RuleminerTimerTask) ruleMinerTasks.get(remoteTenant.getId());
            if (ruleMinerTask != null) {
                ruleMinerTask.destroy();
                ruleMinerTasks.remove(remoteTenant.getId());
            }
            logger.info("'" + remoteTenant.getOperatorId() + " - " + remoteTenant.getStringId() + "' removed from Ruleminertask");
        }
    }

    public void initTasks() {
        ruleMinerTasks = new HashMap<Integer, RuleminerTimerTask>();
        List<Operator> operators = operatorDAO.getOperators(0, Integer.MAX_VALUE);
        for (Operator operator : operators) {
            List<RemoteTenant> tenants = remoteTenantDAO.getTenantsFromOperator(operator.getOperatorId());
            for (RemoteTenant r : tenants) {
                if (r.isAutoRuleminingEnabled()) {
                    addTask(r);
                    logger.info("'" + r.getOperatorId() + " - " + r.getStringId() + "' added to RuleminerTask List");
                }
            }
        }
    }

    /**
     * Iterates through all tenants and adds or removes a tenant
     */
    private void updateTasks() {
        List<Operator> operators = operatorDAO.getOperators(0, Integer.MAX_VALUE);
        for (Operator operator : operators) {
            List<RemoteTenant> tenants = remoteTenantDAO.getTenantsFromOperator(operator.getOperatorId());
            for (RemoteTenant r : tenants) {
                updateTask(r);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private class Scheduler extends Thread {

        private final Log logger = LogFactory.getLog(getClass());

        Queue queue;

        AssocRuleMiningService assocRuleMiningService;

        RemoteTenant remoteTenant;

        Scheduler(Queue queue, AssocRuleMiningService assocRuleMiningService) {
            this.queue = queue;
            this.assocRuleMiningService = assocRuleMiningService;
        }

        @Override
        public void run() {
            Thread thisThread = Thread.currentThread();
            while (!thisThread.isInterrupted() && scheduler == thisThread) {
                updateTasks();
                if (queue.isEmpty()) {
                    try {
                        sleep(SCHEDULER_PAUSE);
                        logger.debug("pausing ruleminer Scheduler for " + SCHEDULER_PAUSE + "ms.");
                    } catch (InterruptedException ex) {
                        logger.debug("ruleminer scheduler " + ex.getMessage());
                        Thread.currentThread().interrupt();
                    }
                } else {
                    remoteTenant = queue.poll();
                    logger.debug("performing rulemining for tenant '" + remoteTenant.getOperatorId() + " - " + remoteTenant.getStringId() + "'");
                    Properties tenantConfig = tenantService.getTenantConfig(remoteTenant.getId());
                    if (tenantConfig != null) {
                        if ("true".equals(tenantConfig.getProperty(RemoteTenant.AUTO_ARCHIVER_ENABLED))) {
                            String days = tenantConfig.getProperty(RemoteTenant.AUTO_ARCHIVER_TIME_RANGE);
                            logger.info("Archiving actions older than " + days + " day(s)");
                            try {
                                assocRuleMiningService.archive(remoteTenant.getId(), Integer.valueOf(days));
                            } catch (Exception e) {
                            }
                        }
                    }
                    if (remoteTenant.getPluginsEnabled()) {
                        logger.info("starting generator plugin for tenant: " + remoteTenant.getOperatorId() + ":" + remoteTenant.getStringId());
                        String generator = tenantConfig.getProperty(PluginRegistry.GENERATOR_PROP);
                        if (generator != null) {
                            Generator<GeneratorConfiguration, GeneratorStatistics> gen = pluginRegistry.getGenerators().get(generator);
                            GeneratorConfiguration genconf = gen.newConfiguration();
                            genconf.setTenantId(remoteTenant.getId());
                            ConfigurationHelper confhelper = new ConfigurationHelper(genconf);
                            confhelper.setValues(tenantConfig, generator);
                            gen.setConfiguration(genconf);
                            try {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                confhelper.getValuesAsProperties().store(baos, "");
                                ruleMinerLogDAO.start(remoteTenant.getId(), generator.substring(0, generator.lastIndexOf("/")), generator.substring(generator.lastIndexOf("/") + 1, generator.length()), genconf.getAssociationType(), baos.toString());
                                GeneratorStatistics stats = gen.execute();
                                JAXBContext context = JAXBContext.newInstance(stats.getClass());
                                Marshaller m = context.createMarshaller();
                                m.setProperty("jaxb.fragment", Boolean.TRUE);
                                StringWriter sw = new StringWriter();
                                XmlRootElement xmlrootElementAnn = stats.getClass().getAnnotation(XmlRootElement.class);
                                if (xmlrootElementAnn == null) {
                                    JAXBElement jaxbElement = new JAXBElement(new QName(GeneratorStatistics.class.getName()), GeneratorStatistics.class, stats);
                                    m.marshal(jaxbElement, sw);
                                } else {
                                    m.marshal(stats, sw);
                                }
                                ruleMinerLogDAO.finish(remoteTenant.getId(), generator.substring(0, generator.lastIndexOf("/")), generator.substring(generator.lastIndexOf("/") + 1, generator.length()), genconf.getAssociationType(), sw.toString());
                            } catch (Exception e) {
                                logger.error("An error occured running the generator " + generator + "!", e);
                                ruleMinerLogDAO.finish(remoteTenant.getId(), generator.substring(0, generator.lastIndexOf("/")), generator.substring(generator.lastIndexOf("/") + 1, generator.length()), genconf.getAssociationType(), "<error>An error occurred during execution: " + e + "</error>");
                            }
                        }
                    }
                    logger.info("Archiving actions finished! Now starting rulemining for tenant: " + remoteTenant.getOperatorId() + ":" + remoteTenant.getStringId());
                    assocRuleMiningService.perform(remoteTenant.getId());
                    remoteTenantService.updateTenantStatistics(remoteTenant.getId());
                }
            }
            logger.debug("ruleminer Scheduler stopped. ");
        }
    }

    public void setAssocRuleMiningService(AssocRuleMiningService assocRuleMiningService) {
        this.assocRuleMiningService = assocRuleMiningService;
    }

    public void setOperatorDAO(OperatorDAO operatorDAO) {
        this.operatorDAO = operatorDAO;
    }

    public void setRemoteTenantDAO(RemoteTenantDAO remoteTenantDAO) {
        this.remoteTenantDAO = remoteTenantDAO;
    }

    public void setRuleMinerLogDAO(RuleMinerLogDAO ruleMinerLogDAO) {
        this.ruleMinerLogDAO = ruleMinerLogDAO;
    }

    public void setTenantService(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    public void setRemoteTenantService(RemoteTenantService remoteTenantService) {
        this.remoteTenantService = remoteTenantService;
    }

    public void setEasyrecSettings(EasyRecSettings easyrecSettings) {
        this.easyrecSettings = easyrecSettings;
    }

    public void setPluginRegistry(PluginRegistry pluginRegistry) {
        this.pluginRegistry = pluginRegistry;
    }
}
