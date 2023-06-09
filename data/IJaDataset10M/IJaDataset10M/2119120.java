package jade.domain;

import java.util.Enumeration;
import java.util.Vector;
import java.util.Date;
import jade.util.leap.HashMap;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import jade.util.leap.Iterator;
import jade.util.leap.Properties;
import jade.util.Logger;
import java.net.InetAddress;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAAgentManagement.InternalError;
import jade.domain.JADEAgentManagement.*;
import jade.domain.KBManagement.*;
import jade.domain.DFGUIManagement.*;
import jade.domain.DFGUIManagement.GetDescription;
import jade.domain.introspection.AMSSubscriber;
import jade.domain.introspection.Event;
import jade.domain.introspection.IntrospectionVocabulary;
import jade.domain.introspection.DeadAgent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.ISO8601;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.proto.SubscriptionResponder;
import jade.proto.AchieveREInitiator;
import jade.content.*;
import jade.content.lang.*;
import jade.content.lang.sl.*;
import jade.content.onto.basic.*;

/**
 <p>
 Standard <em>Directory Facilitator</em> agent. This class implements
 <em><b>FIPA</b></em> <em>DF</em> agent. <b>JADE</b> applications
 typically don't use this class directly, but interact with the DF agent through
 <em>ACL</em> message passing. The <code>DFService</code> class provides
 a number of static methods that facilitate this task.
 More <em>DF</em> agents can be created
 by application programmers to divide a platform into many
 <em><b>Agent Domains</b></em>.
 <p>
 A DF agent accepts a number of optional configuration parameters that can be set
 either as command line options or within a properties file (to be passed to 
 the DF as an argument).
 </p>
 <table border="1" cellspacing="0">
 <tr>
 <th>Parameter</th>
 <th>Description</th>
 </tr>
 <tr>
 <td>
 <code>jade_domain_df_autocleanup</code>
 </td> 
 <td> 
 If set to <code>true</code>, indicates that the DF will automatically 
 clean up registrations as soon as an agent terminates. The default is <code>false</code>
 </td>
 </tr>
 <tr>
 <td>
 <code>jade_domain_df_maxleasetime</code>
 </td>
 <td>
 Indicates the maximum lease time (in millisecond) that the DF will grant for agent 
 description registrations (defaults to infinite).
 </td>
 </tr>
 <tr>
 <td>
 <code>jade_domain_df_maxresult</code>
 </td>
 <td>
 Indicates the maximum number of items found in a search operation that the DF 
 will return to the requester (defaults to 100).
 </td>
 </tr>
 <tr>
 <td>
 <code>jade_domain_df_disablevalidation</code>
 </td>
 <td>
 Turns off content validation on incoming/outgoing messages (defaults to false)
 </td>
 </tr>
 <tr>
 <td>
 <code>jade_domain_df_db-default</code>
 </td>
 <td>
 If set to <code>true</code>, indicates that the DF will store its catalogue into an internal HSQL database, 
 running within the same VM. (The HSQL jar files have to added to the Java CLASSPATH)
 </td>
 </tr>
 <tr>
 <td>
 <code>jade_domain_df_db-url</code>
 </td>
 <td>
 Indicates the JDBC URL of the database the DF will store its catalogue into. 
 This parameter is ignored if <code>jade_domain_df_db-default</code> is set. If neither this parameter nor 
 <code>jade_domain_df_db-default</code> is specified the DF will keep its catalogue in memory.
 </td>
 </tr>
 <tr>
 <td>
 <code>jade_domain_df_db-driver</code>
 </td>
 <td>
 Indicates the JDBC driver to be used to access the DF database (defaults to the ODBC-JDBC bridge). This parameter 
 is ignored if <code>jade_domain_df_db-url</code> is not set or <code>jade_domain_df_db-default</code> is set.
 </td>
 </tr>
 <tr>
 <td>
 <code>jade_domain_df_db-username</code>,
 <code>jade_domain_df_db-password</code>
 </td>
 <td>
 Indicate the username and password to be used to access the DF database (default to null). 
 These parameters are ignored if <code>jade_domain_df_db-url</code> is not set or 
 <code>jade_domain_df_db-default</code> is set.
 </td>
 </tr>
 <td>
 <code>jade_domain_df_db-cleantables</code>
 </td> 
 <td>
 If set to <code>true</code>, indicates that the DF will clean the content of all pre-existing database tables,
 used by the DF. This parameter is ignored if the catalogue is not stored in a database.
 </td>
 </tr>
 <tr>
 <td>
 <code>jade_domain_df_db-abortonerror</code>
 </td>
 <td>
 If set to <code>true</code>, indicates that the DF will immediately terminate in case it cannot 
 connect to the database. This parameter is ignored if the catalogue is not stored in a database. 
 </td>
 </tr>
 <tr>
 <td>
 <code>jade_domain_df_kb-factory</code>
 </td>
 <td>
 Indicates the name of the factory class that
 should be used to create the knowledge base objects for the DF. The class has to be
 a sub class of jade.domain.DFKBFactory.
 </td>
 </tr>
 <tr>
 <td>
 <code>jade_domain_df_poolsize</code>
 </td> 
 <td> 
 The dimension of the pool of thread dedicated to serving registration, deregistration and search
 requests. If <code>0</code> (default) is specified then registration, deregistration and search 
 requests are served directly by the df agent Thread. This parameter is ignored when using a 
 volatile (in-memory) knowledge base.
 </td>
 </tr>
 </table>
 
 <p>
 For instance the following command line will launch a JADE main container
 with a DF that will store its catalogue into a database accessible at 
 URL jdbc:odbc:dfdb and that will keep agent registrations for 1 hour at most.
 
 <code>
 java jade.Boot -gui -jade_domain_df_db-url jdbc:odbc:dfdb -jade_domain_df_maxleasetime 3600000
 </code>
 <p>
 Each DF has a GUI but, by default, it is not visible. The GUI of the
 agent platform includes a menu item that allows to show the GUI of the
 default DF. 
 In order to show the GUI, you should simply send the following message
 to each DF agent: <code>(request :content (action DFName (SHOWGUI))
 :ontology JADE-Agent-Management :protocol fipa-request)</code>
 
 @see DFService
 @author Giovanni Rimassa - Universita' di Parma
 @author Tiziana Trucco - TILAB S.p.A.
 @author Elisabetta Cortese - TILAB S.p.A.
 @author Giovanni Caire - TILAB
 @author Roland Mungenast - Profactor
 @version $Date: 2007-03-30 18:30:14 +0200 (ven, 30 mar 2007) $ $Revision: 5952 $
 */
public class df extends GuiAgent implements DFGUIAdapter {

    private static final int SEARCH_ID_CACHE_SIZE = 16;

    private jade.util.HashCache searchIdCache = new jade.util.HashCache(SEARCH_ID_CACHE_SIZE);

    private int searchIdCnt = 0;

    private List children = new ArrayList();

    private List parents = new ArrayList();

    private HashMap dscDFParentMap = new HashMap();

    private HashMap pendingRequests = new HashMap();

    private DFGUIInterface gui;

    private DFAgentDescription myDescription = null;

    private Codec codec = new SLCodec();

    private ThreadedBehaviourFactory tbf;

    private AMSSubscriber amsSubscriber;

    private static final String AUTOCLEANUP = "jade_domain_df_autocleanup";

    private static final String POOLSIZE = "jade_domain_df_poolsize";

    private static final String MAX_LEASE_TIME = "jade_domain_df_maxleasetime";

    private static final String MAX_RESULTS = "jade_domain_df_maxresult";

    private static final String DISABLE_VALIDATION = "jade_domain_df_disablevalidation";

    private static final String DB_DRIVER = "jade_domain_df_db-driver";

    private static final String DB_URL = "jade_domain_df_db-url";

    private static final String DB_USERNAME = "jade_domain_df_db-username";

    private static final String DB_PASSWORD = "jade_domain_df_db-password";

    private static final String KB_FACTORY = "jade_domain_df_kb-factory";

    private static final String DB_DEFAULT = "jade_domain_df_db-default";

    private static final String DB_CLEANTABLES = "jade_domain_df_db-cleantables";

    private static final String DB_ABORTONERROR = "jade_domain_df_db-abortonerror";

    private static final String DEFAULT_MAX_RESULTS = "100";

    private int maxResultLimit = Integer.parseInt(DEFAULT_MAX_RESULTS);

    private Date maxLeaseTime = null;

    private KB agentDescriptions = null;

    private KBSubscriptionManager subManager = null;

    private SubscriptionResponder dfSubscriptionResponder;

    private Logger logger;

    /**
	 Default constructor. This constructor does nothing; however,
	 applications can create their own DF agents using regular
	 platform management commands. Moreover, customized versions of
	 a Directory Facilitator agent can be built subclassing this
	 class and exploiting its protected interface.
	 */
    public df() {
    }

    /**
	 This method starts all behaviours needed by <em>DF</em> agent to
	 perform its role within <em><b>JADE</b></em> agent platform.
	 */
    protected void setup() {
        logger = Logger.getMyLogger(getLocalName());
        String sDisableValidation = getProperty(DISABLE_VALIDATION, "false");
        String sAutocleanup = getProperty(AUTOCLEANUP, null);
        String sPoolsize = getProperty(POOLSIZE, null);
        String sMaxLeaseTime = getProperty(MAX_LEASE_TIME, null);
        String sMaxResults = getProperty(MAX_RESULTS, DEFAULT_MAX_RESULTS);
        String dbUrl = getProperty(DB_URL, null);
        String dbDriver = getProperty(DB_DRIVER, null);
        String dbUsername = getProperty(DB_USERNAME, null);
        String dbPassword = getProperty(DB_PASSWORD, null);
        String kbFactClass = getProperty(KB_FACTORY, null);
        String sDBDefault = getProperty(DB_DEFAULT, null);
        String sCleanTables = getProperty(DB_CLEANTABLES, null);
        String sDBAbortOnError = getProperty(DB_ABORTONERROR, null);
        Object[] args = this.getArguments();
        if (args != null && args.length > 0) {
            Properties p = new Properties();
            try {
                p.load((String) args[0]);
                sAutocleanup = p.getProperty(AUTOCLEANUP, sAutocleanup);
                sPoolsize = p.getProperty(POOLSIZE, sPoolsize);
                sMaxLeaseTime = p.getProperty(MAX_LEASE_TIME, sMaxLeaseTime);
                sMaxResults = p.getProperty(MAX_RESULTS, sMaxResults);
                sDisableValidation = p.getProperty(DISABLE_VALIDATION, sDisableValidation);
                dbUrl = p.getProperty(DB_URL, dbUrl);
                dbDriver = p.getProperty(DB_DRIVER, dbDriver);
                dbUsername = p.getProperty(DB_USERNAME, dbUsername);
                dbPassword = p.getProperty(DB_PASSWORD, dbPassword);
                kbFactClass = p.getProperty(KB_FACTORY, kbFactClass);
                sDBDefault = p.getProperty(DB_DEFAULT, sDBDefault);
                sCleanTables = p.getProperty(DB_CLEANTABLES, sCleanTables);
                sDBAbortOnError = p.getProperty(DB_ABORTONERROR, sDBAbortOnError);
            } catch (Exception e) {
                logger.log(Logger.SEVERE, "Error loading configuration from file " + args[0] + " [" + e + "].");
            }
        }
        try {
            maxLeaseTime = new Date(Long.parseLong(sMaxLeaseTime));
        } catch (Exception e) {
        }
        logger.log(Logger.CONFIG, "DF Max lease time = " + (maxLeaseTime != null ? ISO8601.toRelativeTimeString(maxLeaseTime.getTime()) : "infinite"));
        try {
            maxResultLimit = Integer.parseInt(sMaxResults);
            if (maxResultLimit < 0) {
                maxResultLimit = Integer.parseInt(DEFAULT_MAX_RESULTS);
                logger.log(Logger.WARNING, "The maxResult parameter of the DF Search Constraints can't be a negative value. It has been set to the default value: " + DEFAULT_MAX_RESULTS);
            } else if (maxResultLimit > Integer.parseInt(DEFAULT_MAX_RESULTS)) {
                logger.log(Logger.WARNING, "Setting the maxResult of the DF Search Constraint to large values can cause low performance or system crash !!");
            }
        } catch (Exception e) {
        }
        logger.log(Logger.CONFIG, "DF Max search result = " + maxResultLimit);
        StringBuffer sb = new StringBuffer("DF KB configuration:\n");
        DFKBFactory kbFactory = new DFKBFactory();
        if (kbFactClass != null) {
            try {
                Object o = Class.forName(kbFactClass).newInstance();
                if (o instanceof DFKBFactory) {
                    kbFactory = (DFKBFactory) o;
                    sb.append("- Factory class = " + kbFactClass);
                    sb.append('\n');
                } else {
                    logger.log(Logger.SEVERE, "The class " + kbFactClass + " is not a valid KB factory for the DF.");
                }
            } catch (Exception e) {
                logger.log(Logger.SEVERE, "Error loading class " + kbFactClass + ". " + e);
            }
        }
        boolean cleanTables = getBooleanProperty(sCleanTables, DB_CLEANTABLES);
        if (isRestarting()) {
            cleanTables = false;
        }
        boolean dbDefault = getBooleanProperty(sDBDefault, DB_DEFAULT);
        boolean dbAbortOnError = getBooleanProperty(sDBAbortOnError, DB_ABORTONERROR);
        if (dbDefault) {
            sb.append("- Type = persistent using internal HSQL database\n");
            try {
                agentDescriptions = kbFactory.getDFDBKB(maxResultLimit, null, null, null, null, cleanTables);
            } catch (Exception e) {
                logger.log(Logger.SEVERE, "Error creating persistent KB based on internal HSQLDB database", e);
                if (dbAbortOnError) {
                    doDelete();
                    return;
                } else {
                    logger.log(Logger.WARNING, "DF using volatile (in-memory) KB");
                    sb = new StringBuffer("DF KB configuration:\n");
                }
            }
        }
        if (agentDescriptions == null && dbUrl != null) {
            if (logger.isLoggable(Logger.CONFIG)) {
                sb.append("- Type = persistent\n");
                sb.append("- DB url = " + dbUrl);
                sb.append('\n');
                sb.append("- DB driver = " + dbDriver);
                sb.append('\n');
                sb.append("- DB username = " + dbUsername);
                sb.append('\n');
                sb.append("- DB password = " + dbPassword);
                sb.append('\n');
            }
            try {
                agentDescriptions = kbFactory.getDFDBKB(maxResultLimit, dbDriver, dbUrl, dbUsername, dbPassword, cleanTables);
            } catch (Exception e) {
                logger.log(Logger.SEVERE, "Error creating persistent KB (+url = " + getValue(dbUrl) + ", driver = " + getValue(dbDriver) + ", username = " + getValue(dbUsername) + ", password = " + getValue(dbPassword) + ")", e);
                if (dbAbortOnError) {
                    doDelete();
                    return;
                } else {
                    logger.log(Logger.WARNING, "DF using volatile (in-memory) KB");
                    sb = new StringBuffer("DF KB configuration:\n");
                }
            }
        }
        if (agentDescriptions == null) {
            sb.append("- Type = volatile\n");
            agentDescriptions = kbFactory.getDFMemKB(maxResultLimit);
            if (sPoolsize != null) {
                logger.log(Logger.WARNING, "Ignoring pool-size indication (" + sPoolsize + "). Parameter not supported when using volatile KB");
                sPoolsize = null;
            }
        }
        logger.log(Logger.CONFIG, sb.toString());
        subManager = new KBSubscriptionManager(agentDescriptions);
        subManager.setContentManager(getContentManager());
        getContentManager().registerLanguage(codec, FIPANames.ContentLanguage.FIPA_SL0);
        getContentManager().registerLanguage(codec, FIPANames.ContentLanguage.FIPA_SL1);
        getContentManager().registerLanguage(codec, FIPANames.ContentLanguage.FIPA_SL2);
        getContentManager().registerLanguage(codec, FIPANames.ContentLanguage.FIPA_SL);
        getContentManager().registerOntology(FIPAManagementOntology.getInstance());
        getContentManager().registerOntology(JADEManagementOntology.getInstance());
        getContentManager().registerOntology(DFAppletOntology.getInstance());
        boolean disableValidation = getBooleanProperty(sDisableValidation, DISABLE_VALIDATION);
        if (disableValidation) {
            getContentManager().setValidationMode(false);
        }
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        MessageTemplate mt1 = null;
        mt1 = MessageTemplate.and(mt, MessageTemplate.MatchOntology(FIPAManagementOntology.getInstance().getName()));
        mt1 = MessageTemplate.and(mt1, MessageTemplate.not(MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.ITERATED_FIPA_REQUEST)));
        int poolSize = getIntegerProperty(sPoolsize, POOLSIZE);
        if (poolSize == 0) {
            DFFipaAgentManagementBehaviour fipaRequestResponder = new DFFipaAgentManagementBehaviour(this, mt1);
            addBehaviour(fipaRequestResponder);
        } else {
            logger.log(Logger.INFO, "DF FIPA request pool-size = " + poolSize);
            tbf = new ThreadedBehaviourFactory();
            for (int i = 0; i < poolSize; ++i) {
                DFFipaAgentManagementBehaviour fipaRequestResponder = new DFFipaAgentManagementBehaviour(this, mt1);
                fipaRequestResponder.setBehaviourName(getLocalName() + "#FIPAManagementResponder-" + i);
                addBehaviour(tbf.wrap(fipaRequestResponder));
            }
        }
        mt1 = MessageTemplate.and(mt, MessageTemplate.MatchOntology(FIPAManagementOntology.getInstance().getName()));
        mt1 = MessageTemplate.and(mt1, MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.ITERATED_FIPA_REQUEST));
        DFIteratedSearchManagementBehaviour iteratedSearchResponder = new DFIteratedSearchManagementBehaviour(this, mt1);
        addBehaviour(iteratedSearchResponder);
        mt1 = MessageTemplate.and(mt, MessageTemplate.MatchOntology(JADEManagementOntology.getInstance().getName()));
        DFJadeAgentManagementBehaviour jadeRequestResponder = new DFJadeAgentManagementBehaviour(this, mt1);
        addBehaviour(jadeRequestResponder);
        mt1 = MessageTemplate.and(mt, MessageTemplate.MatchOntology(DFAppletOntology.getInstance().getName()));
        DFAppletManagementBehaviour appletRequestResponder = new DFAppletManagementBehaviour(this, mt1);
        addBehaviour(appletRequestResponder);
        mt1 = MessageTemplate.and(MessageTemplate.MatchOntology(FIPAManagementOntology.getInstance().getName()), MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE), MessageTemplate.MatchPerformative(ACLMessage.CANCEL)));
        dfSubscriptionResponder = new SubscriptionResponder(this, mt1, subManager) {

            protected ACLMessage handleCancel(ACLMessage cancel) throws FailureException {
                try {
                    Action act = (Action) myAgent.getContentManager().extractContent(cancel);
                    ACLMessage subsMsg = (ACLMessage) act.getAction();
                    Subscription s = getSubscription(subsMsg);
                    if (s != null) {
                        mySubscriptionManager.deregister(s);
                        s.close();
                    }
                } catch (Exception e) {
                    if (logger.isLoggable(Logger.CONFIG)) logger.log(Logger.CONFIG, "Unknown CANCEL content. Use default handler");
                    super.handleCancel(cancel);
                }
                return null;
            }
        };
        addBehaviour(dfSubscriptionResponder);
        setDescriptionOfThisDF(getDefaultDescription());
        agentDescriptions.setSubscriptionResponder(dfSubscriptionResponder);
        agentDescriptions.setLeaseManager(new LeaseManager() {

            public Date getLeaseTime(Object item) {
                return ((DFAgentDescription) item).getLeaseTime();
            }

            public void setLeaseTime(Object item, Date lease) {
                ((DFAgentDescription) item).setLeaseTime(lease);
            }

            /**
			 * Grant a lease to this request according to the
			 * policy of the DF: if the requested lease is
			 * greater than the max lease policy of this DF, then
			 * the granted lease is set to the max of the DF.
			 **/
            public Object grantLeaseTime(Object item) {
                if (maxLeaseTime != null) {
                    Date lease = getLeaseTime(item);
                    long current = System.currentTimeMillis();
                    if ((lease != null && lease.getTime() > (current + maxLeaseTime.getTime())) || ((lease == null) && (maxLeaseTime != null))) {
                        setLeaseTime(item, new Date(current + maxLeaseTime.getTime()));
                    }
                }
                return item;
            }

            public boolean isExpired(Date lease) {
                return (lease != null && (lease.getTime() <= System.currentTimeMillis()));
            }
        });
        myDescription = getDefaultDescription();
        boolean autocleanup = getBooleanProperty(sAutocleanup, AUTOCLEANUP);
        if (autocleanup) {
            logger.log(Logger.CONFIG, "Autocleanup activated");
            amsSubscriber = new AMSSubscriber() {

                protected void installHandlers(java.util.Map handlersTable) {
                    handlersTable.put(IntrospectionVocabulary.DEADAGENT, new EventHandler() {

                        public void handle(Event ev) {
                            DeadAgent da = (DeadAgent) ev;
                            AID id = da.getAgent();
                            try {
                                DFAgentDescription dfd = new DFAgentDescription();
                                dfd.setName(id);
                                DFDeregister(dfd);
                            } catch (Exception e) {
                            }
                            unsubscribeDeadAgent(id);
                        }
                    });
                }
            };
            addBehaviour(amsSubscriber);
        }
        Enumeration ss = agentDescriptions.getSubscriptions();
        while (ss.hasMoreElements()) {
            SubscriptionResponder.Subscription s = (SubscriptionResponder.Subscription) ss.nextElement();
            dfSubscriptionResponder.createSubscription(s.getMessage());
        }
    }

    private void unsubscribeDeadAgent(AID id) {
        Vector ss = dfSubscriptionResponder.getSubscriptions(id);
        for (int i = 0; i < ss.size(); i++) {
            try {
                subManager.deregister((SubscriptionResponder.Subscription) ss.elementAt(i));
            } catch (Exception e) {
                logger.log(Logger.WARNING, "Error deregistering subscription of dead agent " + id.getName(), e);
            }
        }
    }

    private boolean getBooleanProperty(String sValue, String name) {
        boolean b = false;
        if (sValue != null) {
            try {
                b = Boolean.valueOf(sValue).booleanValue();
            } catch (Exception e) {
                logger.log(Logger.WARNING, "\"" + sValue + "\" is not a valid value for boolean parameter" + name, e);
            }
        }
        return b;
    }

    private int getIntegerProperty(String sValue, String name) {
        int n = 0;
        if (sValue != null) {
            try {
                n = Integer.parseInt(sValue);
            } catch (Exception e) {
                logger.log(Logger.WARNING, "\"" + sValue + "\" is not a valid value for integer parameter" + name, e);
            }
        }
        return n;
    }

    private String getValue(String s) {
        return (s != null ? s : "null");
    }

    /**
	 Cleanup <em>DF</em> on exit. This method performs all necessary
	 cleanup operations during agent shutdown.
	 */
    protected void takeDown() {
        if (amsSubscriber != null) {
            send(amsSubscriber.getCancel());
        }
        if (tbf != null) {
            tbf.interrupt();
        }
        if (gui != null) {
            gui.disposeAsync();
        }
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        Iterator it = parents.iterator();
        while (it.hasNext()) {
            AID parentName = (AID) it.next();
            try {
                DFService.deregister(this, parentName, dfd);
            } catch (FIPAException fe) {
                fe.printStackTrace();
            }
        }
    }

    /**
	 Add the behaviour handling a recursive search.
	 If constraints contains a null search_id, then a new one is generated and
	 the new search_id is stored into searchIdCache 
	 for later check (i.e. to avoid search loops). 
	 */
    private void performRecursiveSearch(List localResults, DFAgentDescription dfd, SearchConstraints constraints, Search action) {
        int maxRes = getActualMaxResults(constraints);
        int maxDep = constraints.getMaxDepth().intValue();
        SearchConstraints newConstr = new SearchConstraints();
        newConstr.setMaxDepth(new Long((long) (maxDep - 1)));
        newConstr.setMaxResults(new Long((long) (maxRes - localResults.size())));
        String searchId = constraints.getSearchId();
        if (searchId == null) {
            searchId = getName() + String.valueOf(searchIdCnt++) + System.currentTimeMillis();
            if (searchIdCnt >= SEARCH_ID_CACHE_SIZE) {
                searchIdCnt = 0;
            }
            searchIdCache.add(searchId);
        }
        newConstr.setSearchId(searchId);
        if (logger.isLoggable(Logger.CONFIG)) logger.log(Logger.CONFIG, "Activating recursive search: " + localResults.size() + " item(s) found locally. " + maxRes + " expected. Search depth is " + maxDep + ". Search ID is " + searchId + ". Propagating search to " + children.size() + " federated DF(s)");
        addBehaviour(new RecursiveSearchHandler(localResults, dfd, newConstr, action));
    }

    /** 
	 Inner class RecursiveSearchHandler.
	 This is a behaviour handling recursive searches i.e. searches that
	 must be propagated to children (federated) DFs.
	 */
    private class RecursiveSearchHandler extends AchieveREInitiator {

        private static final long DEFAULTTIMEOUT = 300000;

        private List results;

        private DFAgentDescription template;

        private SearchConstraints constraints;

        private Search action;

        private int maxExpectedResults;

        private int receivedResults;

        /**
		 Construct a new RecursiveSearchHandler.
		 @param results The search results. Initially this includes the items found
		 locally.
		 @param template The DFAgentDescription used as tamplate for the search.
		 @param constraints The constraints for the search to be propagated.
		 @param action The original Search action. This is used as a key to retrieve
		 the incoming REQUEST message.
		 */
        private RecursiveSearchHandler(List results, DFAgentDescription template, SearchConstraints constraints, Search action) {
            super(df.this, null);
            this.results = results;
            this.template = template;
            this.constraints = constraints;
            this.action = action;
            maxExpectedResults = constraints.getMaxResults().intValue();
            receivedResults = 0;
        }

        /**
		 We broadcast the search REQUEST to all children (federated) DFs in parallel.
		 */
        protected Vector prepareRequests(ACLMessage request) {
            Vector requests = null;
            ACLMessage incomingRequest = (ACLMessage) pendingRequests.get(action);
            if (incomingRequest != null) {
                Date deadline = incomingRequest.getReplyByDate();
                if (deadline == null) {
                    deadline = new Date(System.currentTimeMillis() + DEFAULTTIMEOUT);
                }
                requests = new Vector(children.size());
                Iterator it = children.iterator();
                while (it.hasNext()) {
                    AID childDF = (AID) it.next();
                    ACLMessage msg = DFService.createRequestMessage(myAgent, childDF, FIPAManagementVocabulary.SEARCH, template, constraints);
                    msg.setReplyByDate(deadline);
                    requests.addElement(msg);
                }
            }
            return requests;
        }

        /** 
		 As long as we receive the replies we update the results. If we reach the
		 max-results we send back the notification to the requester and discard 
		 successive replies.
		 */
        protected void handleInform(ACLMessage inform) {
            if (logger.isLoggable(Logger.CONFIG)) logger.log(Logger.CONFIG, "Recursive search result received from " + inform.getSender().getName() + ".");
            int cnt = 0;
            if (receivedResults < maxExpectedResults) {
                try {
                    DFAgentDescription[] dfds = DFService.decodeResult(inform.getContent());
                    for (int i = 0; i < dfds.length; ++i) {
                        if (addResult(dfds[i])) {
                            receivedResults++;
                            cnt++;
                            if (receivedResults >= maxExpectedResults) {
                                sendPendingNotification(action, results);
                            }
                        }
                    }
                } catch (Exception e) {
                    if (logger.isLoggable(Logger.SEVERE)) logger.log(Logger.SEVERE, "WARNING: Error decoding reply from federated DF " + inform.getSender().getName() + " during recursive search [" + e.toString() + "].");
                }
            }
            if (logger.isLoggable(Logger.CONFIG)) logger.log(Logger.CONFIG, cnt + " new items found in recursive search.");
        }

        protected void handleRefuse(ACLMessage refuse) {
            if (logger.isLoggable(Logger.WARNING)) logger.log(Logger.WARNING, "REFUSE received from federated DF " + refuse.getSender().getName() + " during recursive search.");
        }

        protected void handleFailure(ACLMessage failure) {
            if (logger.isLoggable(Logger.WARNING)) logger.log(Logger.WARNING, "FAILURE received from federated DF " + failure.getSender().getName() + " during recursive search.");
        }

        protected void handleNotUnderstood(ACLMessage notUnderstood) {
            if (logger.isLoggable(Logger.WARNING)) logger.log(Logger.WARNING, "NOT_UNDERSTOOD received from federated DF " + notUnderstood.getSender().getName() + " during recursive search.");
        }

        protected void handleOutOfSequence(ACLMessage msg) {
            if (logger.isLoggable(Logger.WARNING)) logger.log(Logger.WARNING, "Out of sequence message " + ACLMessage.getPerformative(msg.getPerformative()) + " received from " + msg.getSender().getName() + " during recursive search.");
        }

        public int onEnd() {
            if (receivedResults < maxExpectedResults) {
                sendPendingNotification(action, results);
            }
            return super.onEnd();
        }

        private boolean addResult(DFAgentDescription newDfd) {
            Iterator it = results.iterator();
            while (it.hasNext()) {
                DFAgentDescription dfd = (DFAgentDescription) it.next();
                if (dfd.getName().equals(newDfd.getName())) {
                    return false;
                }
            }
            results.add(newDfd);
            return true;
        }
    }

    void DFRegister(DFAgentDescription dfd) throws AlreadyRegistered {
        Object old = agentDescriptions.register(dfd.getName(), dfd);
        if (old != null) throw new AlreadyRegistered();
        if (isADF(dfd)) {
            if (logger.isLoggable(Logger.INFO)) logger.log(Logger.INFO, "Added federation " + dfd.getName().getName() + " --> " + getName());
            children.add(dfd.getName());
            try {
                gui.addChildren(dfd.getName());
            } catch (Exception ex) {
            }
        }
        subManager.handleChange(dfd, null);
        try {
            gui.addAgentDesc(dfd.getName());
            gui.showStatusMsg("Registration of agent: " + dfd.getName().getName() + " done.");
        } catch (Exception ex) {
        }
    }

    void DFDeregister(DFAgentDescription dfd) throws NotRegistered {
        Object old = agentDescriptions.deregister(dfd.getName());
        if (old == null) throw new NotRegistered();
        if (children.remove(dfd.getName())) try {
            gui.removeChildren(dfd.getName());
        } catch (Exception e) {
        }
        dfd.clearAllServices();
        subManager.handleChange(dfd, (DFAgentDescription) old);
        try {
            gui.removeAgentDesc(dfd.getName(), df.this.getAID());
            gui.showStatusMsg("Deregistration of agent: " + dfd.getName().getName() + " done.");
        } catch (Exception e1) {
        }
    }

    void DFModify(DFAgentDescription dfd) throws NotRegistered {
        Object old = agentDescriptions.register(dfd.getName(), dfd);
        if (old == null) {
            agentDescriptions.deregister(dfd.getName());
            throw new NotRegistered();
        }
        subManager.handleChange(dfd, (DFAgentDescription) old);
        try {
            gui.removeAgentDesc(dfd.getName(), df.this.getAID());
            gui.addAgentDesc(dfd.getName());
            gui.showStatusMsg("Modify of agent: " + dfd.getName().getName() + " done.");
        } catch (Exception e) {
        }
    }

    List DFSearch(DFAgentDescription dfd, int maxResults) {
        return agentDescriptions.search(dfd, maxResults);
    }

    KBIterator DFIteratedSearch(DFAgentDescription dfd) {
        return agentDescriptions.iterator(dfd);
    }

    /**
	 Serve the Register action of the FIPA management ontology.
	 Package scoped since it is called by DFFipaAgentManagementBehaviour.
	 */
    void registerAction(Register r, AID requester) throws FIPAException {
        DFAgentDescription dfd = (DFAgentDescription) r.getDescription();
        DFService.checkIsValid(dfd, true);
        if (logger.isLoggable(Logger.CONFIG)) logger.log(Logger.CONFIG, "Agent " + requester.getName() + " requesting action Register for " + dfd.getName());
        if (dfd.getName().equals(getAID())) {
            throw new Unauthorised();
        }
        DFRegister(dfd);
    }

    /**
	 Serve the Deregister action of the FIPA management ontology.
	 Package scoped since it is called by DFFipaAgentManagementBehaviour.
	 */
    void deregisterAction(Deregister d, AID requester) throws FIPAException {
        DFAgentDescription dfd = (DFAgentDescription) d.getDescription();
        DFService.checkIsValid(dfd, false);
        if (logger.isLoggable(Logger.CONFIG)) logger.log(Logger.CONFIG, "Agent " + requester.getName() + " requesting action Deregister for " + dfd.getName());
        DFDeregister(dfd);
    }

    /**
	 Serve the Modify action of the FIPA management ontology.
	 Package scoped since it is called by DFFipaAgentManagementBehaviour.
	 */
    void modifyAction(Modify m, AID requester) throws FIPAException {
        DFAgentDescription dfd = (DFAgentDescription) m.getDescription();
        DFService.checkIsValid(dfd, true);
        if (logger.isLoggable(Logger.CONFIG)) logger.log(Logger.CONFIG, "Agent " + requester.getName() + " requesting action Modify for " + dfd.getName());
        DFModify(dfd);
    }

    /**
	 Serve the Search action of the FIPA management ontology.
	 Package scoped since it is called by DFFipaAgentManagementBehaviour.
	 @return the List of descriptions matching the template specified 
	 in the Search action. If no description is found an empty List
	 is returned. In case a recursive search is required it returns 
	 null to indicate that the result is not yet available.
	 */
    List searchAction(Search s, AID requester) throws FIPAException {
        DFAgentDescription dfd = (DFAgentDescription) s.getDescription();
        SearchConstraints constraints = s.getConstraints();
        List result = null;
        if (logger.isLoggable(Logger.CONFIG)) logger.log(Logger.CONFIG, "Agent " + requester.getName() + " requesting action Search");
        checkSearchId(constraints.getSearchId());
        int maxResult = getActualMaxResults(constraints);
        result = DFSearch(dfd, maxResult);
        if (result.size() < maxResult) {
            Long maxDepth = constraints.getMaxDepth();
            if ((children.size() > 0) && (maxDepth != null) && (maxDepth.intValue() > 0)) {
                performRecursiveSearch(result, dfd, constraints, s);
                return null;
            }
        }
        return result;
    }

    /**
	 Serve a Search action of the FIPA management ontology requested
	 using an iterated-fipa-request protocol.
	 Package scoped since it is called by DFIteratedSearchManagementBehaviour.
	 @return an iterator over the DFAgentDescription matching the specified
	 search template.
	 */
    KBIterator iteratedSearchAction(Search s, AID requester) throws FIPAException {
        DFAgentDescription dfd = (DFAgentDescription) s.getDescription();
        if (logger.isLoggable(Logger.CONFIG)) logger.log(Logger.CONFIG, "Agent " + requester.getName() + " requesting action Iterated-Search");
        return DFIteratedSearch(dfd);
    }

    /**
	 Serve the ShowGui action of the JADE management ontology.
	 Package scoped since it is called by DFJadeAgentManagementBehaviour.
	 @exception FailureException If the GUI is already visible or some
	 error occurs creating the GUI.
	 */
    void showGuiAction(ShowGui sg, AID requester) throws FailureException {
        if (logger.isLoggable(Logger.CONFIG)) logger.log(Logger.CONFIG, "Agent " + requester.getName() + " requesting action ShowGui");
        if (!showGui()) {
            throw new FailureException("Gui_is_being_shown_already");
        }
    }

    /**
	 Serve the GetParents action of the DF-Applet ontology
	 Package scoped since it is called by DFAppletManagementBehaviour.
	 */
    List getParentsAction(GetParents action, AID requester) {
        if (logger.isLoggable(Logger.CONFIG)) logger.log(Logger.CONFIG, "Agent " + requester + " requesting action GetParents.");
        return parents;
    }

    /**
	 Serve the GetDescription action of the DF-Applet ontology
	 Package scoped since it is called by DFAppletManagementBehaviour.
	 */
    List getDescriptionAction(GetDescription action, AID requester) {
        if (logger.isLoggable(Logger.CONFIG)) logger.log(Logger.CONFIG, "Agent " + requester + " requesting action GetDescription.");
        List tmp = new ArrayList();
        tmp.add(getDescriptionOfThisDF());
        return tmp;
    }

    /**
	 Serve the GetDescriptionUsed action of the DF-Applet ontology
	 Package scoped since it is called by DFAppletManagementBehaviour.
	 */
    List getDescriptionUsedAction(GetDescriptionUsed action, AID requester) {
        AID parent = action.getParentDF();
        if (logger.isLoggable(Logger.CONFIG)) logger.log(Logger.CONFIG, "Agent " + requester + " requesting action GetDescriptionUsed to federate with " + parent.getName());
        List tmp = new ArrayList();
        tmp.add(getDescriptionOfThisDF(parent));
        return tmp;
    }

    /**
	 Serve the Federate action of the DF-Applet ontology
	 Package scoped since it is called by DFAppletManagementBehaviour.
	 */
    void federateAction(final Federate action, AID requester) {
        AID remoteDF = action.getDf();
        if (logger.isLoggable(Logger.CONFIG)) logger.log(Logger.CONFIG, "Agent " + requester + " requesting action Federate with DF " + remoteDF.getName());
        Register r = new Register();
        DFAgentDescription tmp = action.getDescription();
        final DFAgentDescription dfd = (tmp != null ? tmp : getDescriptionOfThisDF());
        r.setDescription(dfd);
        Behaviour b = new RemoteDFRequester(remoteDF, r) {

            public int onEnd() {
                Object result = getResult();
                if (!(result instanceof InternalError)) {
                    addParent(getRemoteDF(), dfd);
                }
                sendPendingNotification(action, result);
                return 0;
            }
        };
        addBehaviour(b);
    }

    /**
	 Serve the RegisterWith action of the DF-Applet ontology
	 Package scoped since it is called by DFAppletManagementBehaviour.
	 */
    void registerWithAction(final RegisterWith action, AID requester) {
        AID remoteDF = action.getDf();
        if (logger.isLoggable(Logger.CONFIG)) logger.log(Logger.CONFIG, "Agent " + requester + " requesting action RegisterWith on DF " + remoteDF.getName());
        Register r = new Register();
        final DFAgentDescription dfd = action.getDescription();
        r.setDescription(dfd);
        Behaviour b = new RemoteDFRequester(remoteDF, r) {

            public int onEnd() {
                Object result = getResult();
                if (!(result instanceof InternalError)) {
                    if (dfd.getName().equals(myAgent.getAID())) {
                        addParent(getRemoteDF(), dfd);
                    }
                }
                sendPendingNotification(action, result);
                return 0;
            }
        };
        addBehaviour(b);
    }

    /**
	 Serve the DeregisterFrom action of the DF-Applet ontology
	 Package scoped since it is called by DFAppletManagementBehaviour.
	 */
    void deregisterFromAction(final DeregisterFrom action, AID requester) {
        AID remoteDF = action.getDf();
        if (logger.isLoggable(Logger.CONFIG)) logger.log(Logger.CONFIG, "Agent " + requester + " requesting action DeregisterFrom on DF " + remoteDF.getName());
        Deregister d = new Deregister();
        final DFAgentDescription dfd = action.getDescription();
        d.setDescription(dfd);
        Behaviour b = new RemoteDFRequester(remoteDF, d) {

            public int onEnd() {
                Object result = getResult();
                if (!(result instanceof InternalError)) {
                    if (dfd.getName().equals(myAgent.getAID())) {
                        removeParent(getRemoteDF());
                    }
                }
                sendPendingNotification(action, result);
                return 0;
            }
        };
        addBehaviour(b);
    }

    /**
	 Serve the ModifyOn action of the DF-Applet ontology
	 Package scoped since it is called by DFAppletManagementBehaviour.
	 */
    void modifyOnAction(final ModifyOn action, AID requester) {
        AID remoteDF = action.getDf();
        if (logger.isLoggable(Logger.CONFIG)) logger.log(Logger.CONFIG, "Agent " + requester + " requesting action ModifyOn on DF " + remoteDF.getName());
        Modify m = new Modify();
        m.setDescription(action.getDescription());
        Behaviour b = new RemoteDFRequester(remoteDF, m) {

            public int onEnd() {
                sendPendingNotification(action, getResult());
                return 0;
            }
        };
        addBehaviour(b);
    }

    /**
	 Serve the SearchOn action of the DF-Applet ontology
	 Package scoped since it is called by DFAppletManagementBehaviour.
	 */
    void searchOnAction(final SearchOn action, AID requester) {
        AID remoteDF = action.getDf();
        if (logger.isLoggable(Logger.CONFIG)) logger.log(Logger.CONFIG, "Agent " + requester + " requesting action SearchOn on DF " + remoteDF.getName());
        Search s = new Search();
        s.setDescription(action.getDescription());
        s.setConstraints(action.getConstraints());
        Behaviour b = new RemoteDFRequester(remoteDF, s) {

            public int onEnd() {
                sendPendingNotification(action, getResult());
                return 0;
            }
        };
        addBehaviour(b);
    }

    protected void onGuiEvent(GuiEvent ev) {
        try {
            switch(ev.getType()) {
                case DFGUIAdapter.EXIT:
                    {
                        gui.disposeAsync();
                        gui = null;
                        doDelete();
                        break;
                    }
                case DFGUIAdapter.CLOSEGUI:
                    {
                        gui.disposeAsync();
                        gui = null;
                        break;
                    }
                case DFGUIAdapter.REGISTER:
                    {
                        AID df = (AID) ev.getParameter(0);
                        final DFAgentDescription dfd = (DFAgentDescription) ev.getParameter(1);
                        DFService.checkIsValid(dfd, true);
                        if (getAID().equals(df)) {
                            DFRegister(dfd);
                        } else {
                            gui.showStatusMsg("Processing your request & waiting for result...");
                            Register r = new Register();
                            r.setDescription(dfd);
                            Behaviour b = new RemoteDFRequester(df, r) {

                                public int onEnd() {
                                    Object result = getResult();
                                    if (!(result instanceof InternalError)) {
                                        gui.showStatusMsg("Registration request processed. Ready for new request");
                                        if (dfd.getName().equals(myAgent.getAID())) {
                                            addParent(getRemoteDF(), dfd);
                                        }
                                    } else {
                                        gui.showStatusMsg("Error processing request. " + ((InternalError) result).getMessage());
                                    }
                                    return 0;
                                }
                            };
                            addBehaviour(b);
                        }
                        break;
                    }
                case DFGUIAdapter.DEREGISTER:
                    {
                        AID df = (AID) ev.getParameter(0);
                        final DFAgentDescription dfd = (DFAgentDescription) ev.getParameter(1);
                        DFService.checkIsValid(dfd, false);
                        if (getAID().equals(df)) {
                            DFDeregister(dfd);
                        } else {
                            gui.showStatusMsg("Processing your request & waiting for result...");
                            Deregister d = new Deregister();
                            d.setDescription(dfd);
                            Behaviour b = new RemoteDFRequester(df, d) {

                                public int onEnd() {
                                    Object result = getResult();
                                    if (!(result instanceof InternalError)) {
                                        gui.showStatusMsg("Deregistration request processed. Ready for new request");
                                        if (dfd.getName().equals(myAgent.getAID())) {
                                            removeParent(getRemoteDF());
                                        } else {
                                            gui.removeSearchResult(dfd.getName());
                                        }
                                    } else {
                                        gui.showStatusMsg("Error processing request. " + ((InternalError) result).getMessage());
                                    }
                                    return 0;
                                }
                            };
                            addBehaviour(b);
                        }
                        break;
                    }
                case DFGUIAdapter.MODIFY:
                    {
                        AID df = (AID) ev.getParameter(0);
                        DFAgentDescription dfd = (DFAgentDescription) ev.getParameter(1);
                        DFService.checkIsValid(dfd, true);
                        if (getAID().equals(df)) {
                            DFModify(dfd);
                        } else {
                            gui.showStatusMsg("Processing your request & waiting for result...");
                            Modify m = new Modify();
                            m.setDescription(dfd);
                            Behaviour b = new RemoteDFRequester(df, m) {

                                public int onEnd() {
                                    Object result = getResult();
                                    if (!(result instanceof InternalError)) {
                                        gui.showStatusMsg("Modification request processed. Ready for new request");
                                    } else {
                                        gui.showStatusMsg("Error processing request. " + ((InternalError) result).getMessage());
                                    }
                                    return 0;
                                }
                            };
                            addBehaviour(b);
                        }
                        break;
                    }
                case DFGUIAdapter.SEARCH:
                    {
                        AID df = (AID) ev.getParameter(0);
                        DFAgentDescription dfd = (DFAgentDescription) ev.getParameter(1);
                        SearchConstraints sc = (SearchConstraints) ev.getParameter(2);
                        gui.showStatusMsg("Processing your request & waiting for result...");
                        Search s = new Search();
                        s.setDescription(dfd);
                        s.setConstraints(sc);
                        Behaviour b = new RemoteDFRequester(df, s) {

                            public int onEnd() {
                                Object result = getResult();
                                if (!(result instanceof InternalError)) {
                                    gui.showStatusMsg("Search request processed. Ready for new request");
                                    gui.refreshLastSearchResults((List) result, getRemoteDF());
                                } else {
                                    gui.showStatusMsg("Error processing request. " + ((InternalError) result).getMessage());
                                }
                                return 0;
                            }
                        };
                        addBehaviour(b);
                        break;
                    }
                case DFGUIAdapter.FEDERATE:
                    {
                        AID df = (AID) ev.getParameter(0);
                        final DFAgentDescription dfd = (DFAgentDescription) ev.getParameter(1);
                        gui.showStatusMsg("Processing your request & waiting for result...");
                        Register r = new Register();
                        r.setDescription(dfd);
                        Behaviour b = new RemoteDFRequester(df, r) {

                            public int onEnd() {
                                Object result = getResult();
                                if (!(result instanceof InternalError)) {
                                    gui.showStatusMsg("Federation request processed. Ready for new request");
                                    addParent(getRemoteDF(), dfd);
                                } else {
                                    gui.showStatusMsg("Error processing request. " + ((InternalError) result).getMessage());
                                }
                                return 0;
                            }
                        };
                        addBehaviour(b);
                        break;
                    }
            }
        } catch (FIPAException fe) {
            gui.showStatusMsg("Error processing request. " + fe.getMessage());
            fe.printStackTrace();
        }
    }

    /**
	 This method returns the description of an agent registered with the DF.
	 */
    public DFAgentDescription getDFAgentDsc(AID name) throws FIPAException {
        DFAgentDescription template = new DFAgentDescription();
        template.setName(name);
        List l = agentDescriptions.search(template, 1);
        if (l.isEmpty()) return null; else return (DFAgentDescription) l.get(0);
    }

    /**
	 * This method returns the current description of this DF
	 */
    public DFAgentDescription getDescriptionOfThisDF() {
        return myDescription;
    }

    /**
	 * This method returns the description of this df used to federate with the given parent
	 */
    public DFAgentDescription getDescriptionOfThisDF(AID parent) {
        return (DFAgentDescription) dscDFParentMap.get(parent);
    }

    /**
	 This method make visible the GUI of the DF.
	 @return true if the GUI was not visible already, false otherwise.
	 */
    protected boolean showGui() {
        if (gui == null) {
            try {
                Class c = Class.forName("jade.tools.dfgui.DFGUI");
                gui = (DFGUIInterface) c.newInstance();
                gui.setAdapter(df.this);
                DFAgentDescription matchEverything = new DFAgentDescription();
                List agents = agentDescriptions.search(matchEverything, -1);
                List AIDList = new ArrayList();
                Iterator it = agents.iterator();
                while (it.hasNext()) AIDList.add(((DFAgentDescription) it.next()).getName());
                gui.refresh(AIDList.iterator(), parents.iterator(), children.iterator());
                gui.setVisible(true);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
	 * This method creates the DFAgent descriptor for this df used to federate with other df.
	 */
    private DFAgentDescription getDefaultDescription() {
        DFAgentDescription out = new DFAgentDescription();
        out.setName(getAID());
        out.addOntologies(FIPAManagementOntology.getInstance().getName());
        out.addLanguages(FIPANames.ContentLanguage.FIPA_SL0);
        out.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
        ServiceDescription sd = new ServiceDescription();
        sd.setName("df-service");
        sd.setType("fipa-df");
        sd.addOntologies(FIPAManagementOntology.getInstance().getName());
        sd.addLanguages(FIPANames.ContentLanguage.FIPA_SL0);
        sd.addProtocols(FIPANames.InteractionProtocol.FIPA_REQUEST);
        try {
            sd.setOwnership(InetAddress.getLocalHost().getHostName());
        } catch (java.net.UnknownHostException uhe) {
            sd.setOwnership("unknown");
        }
        out.addServices(sd);
        return out;
    }

    /**
	 * This method set the description of the df according to the DFAgentDescription passed.
	 * The programmers can call this method to provide a different initialization of the description of the df they are implementing.
	 * The method is called inside the setup of the agent and set the df description using a default description.
	 */
    protected void setDescriptionOfThisDF(DFAgentDescription dfd) {
        myDescription = dfd;
        myDescription.setName(getAID());
        if (!isADF(myDescription)) {
            if (logger.isLoggable(Logger.WARNING)) logger.log(Logger.WARNING, "The description set for this DF does not include a \"fipa-df\" service.");
        }
    }

    /**
	 * This method can be used to add a parent (a DF this DF is federated with). 
	 * @param dfName the parent df (the df with which this df has been registered)
	 * @param dfd the description used by this df to register with the parent.
	 */
    protected void addParent(AID dfName, DFAgentDescription dfd) {
        parents.add(dfName);
        if (gui != null) gui.addParent(dfName);
        dscDFParentMap.put(dfName, dfd);
    }

    /**
	 this method can be used to remove a parent (a DF with which this DF is federated).
	 */
    protected void removeParent(AID dfName) {
        parents.remove(dfName);
        if (gui != null) gui.removeParent(dfName);
        dscDFParentMap.remove(dfName);
    }

    /**
	 Store the request message
	 related to an action that is being processed by a Behaviour.
	 This information will be used by the Behaviour to send back the 
	 notification to the requester.
	 */
    void storePendingRequest(Object key, ACLMessage request) {
        pendingRequests.put(key, request);
    }

    void removePendingRequest(Object key) {
        pendingRequests.remove(key);
    }

    /**
	 Send the notification related to an action that has been processed 
	 by a Behaviour.
	 */
    private void sendPendingNotification(Concept action, Object result) {
        ACLMessage request = (ACLMessage) pendingRequests.remove(action);
        if (request != null) {
            ACLMessage notification = request.createReply();
            ContentElement ce = null;
            Action act = new Action(getAID(), action);
            if (result instanceof InternalError) {
                notification.setPerformative(ACLMessage.FAILURE);
                ContentElementList cel = new ContentElementList();
                cel.add(act);
                cel.add((Predicate) result);
                ce = cel;
            } else {
                notification.setPerformative(ACLMessage.INFORM);
                if (result != null) {
                    ce = new Result(act, result);
                } else {
                    ce = new Done(act);
                }
            }
            try {
                getContentManager().fillContent(notification, ce);
                send(notification);
                AID receiver = (AID) notification.getAllReceiver().next();
                if (logger.isLoggable(Logger.FINE)) logger.log(Logger.FINE, "Notification sent back to " + receiver.getName());
            } catch (Exception e) {
                if (logger.isLoggable(Logger.SEVERE)) logger.log(Logger.SEVERE, "Error encoding pending notification content.");
                e.printStackTrace();
            }
        } else {
            if (logger.isLoggable(Logger.WARNING)) logger.log(Logger.WARNING, "Processed action request not found.");
        }
    }

    /**
	 * @return 
	 * <ul>
	 * <li> 1 if constraints.maxResults == null (according to FIPA specs)
	 * <li> maxResultLimit if constraints.maxResults is infinite (i.e. < 0) or
	 * greater than maxResultLimit 
	 * <li> constraints.maxResults otherwise
	 * </ul>
	 * This is package-scoped since it is also used by the DFIteratedSearchManagementBehaviour 
	 **/
    int getActualMaxResults(SearchConstraints constraints) {
        int maxResult = (constraints.getMaxResults() == null ? 1 : constraints.getMaxResults().intValue());
        maxResult = ((maxResult < 0 || maxResult > maxResultLimit) ? maxResultLimit : maxResult);
        return maxResult;
    }

    /**    
	 Check if this search must be served, i.e. if it has not yet been received.
	 In particular the value of search_id must be different from any prior value that was received.
	 If search_id is not null and it has not yet been received, search_id is
	 added into the cache.
	 @exception FIPAException if the search id is already in the cache.
	 */
    private void checkSearchId(String searchId) throws FIPAException {
        if (searchId != null) {
            if (searchIdCache.contains(searchId)) {
                throw new InternalError("search-id already served");
            } else {
                searchIdCache.add(searchId);
            }
        }
    }

    private boolean isADF(DFAgentDescription dfd) {
        try {
            return ((ServiceDescription) dfd.getAllServices().next()).getType().equalsIgnoreCase("fipa-df");
        } catch (Exception e) {
            return false;
        }
    }
}
