package eu.popeye.middleware.pluginmanagement.plugin;

import java.awt.Container;
import java.awt.Dimension;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.naming.Binding;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.event.EventContext;
import javax.swing.JInternalFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import plugin.installPlugs.PluginFramework;
import eu.popeye.middleware.groupmanagement.management.Workgroup;
import eu.popeye.middleware.groupmanagement.management.WorkgroupManager;
import eu.popeye.middleware.groupmanagement.membership.Member;
import eu.popeye.middleware.groupmanagement.membership.MemberImpl;
import eu.popeye.middleware.pluginmanagement.datistatici.PopeyeFile;
import eu.popeye.middleware.pluginmanagement.extensionPoint.DeclaredHost;
import eu.popeye.middleware.pluginmanagement.extensionPoint.ExtensionPointDescriptor;
import eu.popeye.middleware.pluginmanagement.extensionPoint.HostDescriptor;
import eu.popeye.middleware.pluginmanagement.extensionPoint.IExtensionPoint;
import eu.popeye.middleware.pluginmanagement.extensionPoint.IGenericHost;
import eu.popeye.middleware.pluginmanagement.extensionPoint.event.EventService;
import eu.popeye.middleware.pluginmanagement.plugin.PlugManager.GrafoDipendenze.Nodo;
import eu.popeye.middleware.pluginmanagement.plugin.feature.FeatureDescriptor;
import eu.popeye.middleware.pluginmanagement.plugin.feature.XMLParseFeature;
import eu.popeye.middleware.pluginmanagement.runtime.GeneralURLClassLoader;
import eu.popeye.middleware.pluginmanagement.runtime.data.IPlugData;
import eu.popeye.middleware.pluginmanagement.runtime.data.PlugDataManager;
import eu.popeye.middleware.pluginmanagement.ui.PlugDataWin;
import eu.popeye.middleware.workspacemanagement.Workspace;
import eu.popeye.middleware.workspacemanagement.WorkspaceManagementImpl;
import eu.popeye.networkabstraction.communication.ApplicationMessageListener;
import eu.popeye.networkabstraction.communication.CommunicationChannel;
import eu.popeye.networkabstraction.communication.basic.BSMProvider;
import eu.popeye.networkabstraction.communication.basic.adapter.AddressAdapter;
import eu.popeye.networkabstraction.communication.basic.util.FileUtils;
import eu.popeye.networkabstraction.communication.basic.util.InitializationException;
import eu.popeye.networkabstraction.communication.basic.util.PopeyeClassLoader;
import eu.popeye.networkabstraction.communication.basic.util.PopeyeException;
import eu.popeye.networkabstraction.communication.message.PopeyeMessage;

public class PlugManager {

    private EventService eventService;

    private PlugDataWin plugDataWin;

    private PlugDataManager plugDataManager;

    public static final String PLUGIN_DIRECTORY = "downloadedPlugins";

    public static final String PLUGIN_MANAGEMENT_CC = "PLUGIN-MGNT";

    private PopeyeClassLoader pcl = null;

    private WorkgroupManager wgManager = null;

    private Workspace ws;

    private Workgroup currentGroup = null;

    private CommunicationChannel cc = null;

    private EventContext ctx = null;

    protected final Log log = LogFactory.getLog(getClass());

    private PluginRegistry pluginRegistry, pluginStarted;

    private InternalError internalError;

    private PluginModel[] pluginsCurrentFeature;

    private FeatureDescriptor featureDefault;

    private GeneralURLClassLoader generalClassLoader = new GeneralURLClassLoader();

    private int ordineAttivazionePlug = 0;

    public Workspace getWorkspace() {
        return this.ws;
    }

    public PlugManager(PlugDataWin pdw, PlugDataManager pd, Workspace ws) throws PopeyeException {
        plugDataManager = pd;
        plugDataWin = pdw;
        this.internalError = new InternalError();
        this.pluginRegistry = new PluginRegistry(internalError);
        this.pluginStarted = new PluginRegistry(internalError);
        this.ws = ws;
        try {
            this.wgManager = BSMProvider.getInstance().getBasicServicesManager().getWorkgroupManager();
        } catch (InitializationException e1) {
            e1.printStackTrace();
        }
        currentGroup = ws.getGroup();
        currentGroup.createNamedCommunicationChannel(PLUGIN_MANAGEMENT_CC);
        cc = currentGroup.getNamedCommunicationChannel(PLUGIN_MANAGEMENT_CC);
        ctx = currentGroup.getContext();
        cc.addApplicationMessageListener(new ApplicationMessageListener() {

            public void onMessage(PopeyeMessage msg) {
                try {
                    System.out.println("Dins del listener");
                    if (msg instanceof PluginRequestMessage) {
                        System.out.println("Instancia de PRM");
                        log.debug("PluginManager: Message received.");
                        log.debug("PluginManager: PluginRequestMessage received.");
                        PluginRequestMessage msgRequest = ((PluginRequestMessage) msg);
                        String pluginName = msgRequest.getPluginName();
                        sendPluginResponse(pluginName, msgRequest.getSource());
                        log.debug("PluginManager: PluginResponseMessage sent to ..." + msgRequest.getSource());
                    } else {
                        log.debug("PopeyeMessage not handled by PluginManager: " + msg.toString());
                    }
                } catch (Exception e) {
                    log.error(e.getStackTrace().toString());
                }
            }
        });
        try {
            pcl = new PopeyeClassLoader();
        } catch (MalformedURLException e) {
            throw new PopeyeException(e.getCause());
        }
    }

    public WorkgroupManager getWorkGroupManager() {
        return this.wgManager;
    }

    /**
	 * This method sends a serialized plugin to the requester through the communication channel
	 * @param pluginName
	 */
    private void sendPluginResponse(String pluginName, Member dest) throws PopeyeException {
        PluginResponseMessage plRespMsg = null;
        int sep = pluginName.lastIndexOf('.');
        String fileStub;
        if (sep > 0) {
            fileStub = pluginName.substring(sep + 1);
        } else {
            fileStub = pluginName;
        }
        String filename = "." + File.separator + PLUGIN_DIRECTORY + File.separator + fileStub + ".jar";
        log.debug("Trying to read the following file: " + filename);
        File file = new File(filename);
        byte[] jarFile = null;
        try {
            jarFile = FileUtils.file2array(file);
        } catch (IOException e) {
            throw new PopeyeException(e.getCause());
        }
        plRespMsg = new PluginResponseMessage(jarFile);
        cc.send(dest, plRespMsg);
    }

    public PlugInDescriptor getInstalledPluginDescription(String name) {
        return pluginRegistry.getPluginDescriptor(name);
    }

    public ArrayList getInstalledPlugins() {
        ArrayList<PlugInDescriptor> list = new ArrayList<PlugInDescriptor>();
        PlugInDescriptor[] it = this.pluginRegistry.getAllPluginDescriptor();
        for (int i = 0; i < it.length; i++) {
            list.add(it[i]);
        }
        return list;
    }

    public ArrayList getStartedPlugins() {
        ArrayList<PlugInDescriptor> list = new ArrayList<PlugInDescriptor>();
        PlugInDescriptor[] it = this.pluginStarted.getAllPluginDescriptor();
        for (int i = 0; i < it.length; i++) {
            list.add(it[i]);
        }
        return list;
    }

    /**
	 * Instantiates the plugin specified in the PluginDescription parameter
	 * 
	 * @param pluginDesc
	 * @param cont
	 */
    public void instantiatePlugin(PlugInDescriptor pluginDesc, Container cont, String groupName) throws PopeyeException {
        Class pluginClass = null;
        Class pluginPlugDataClass = null;
        Object pluginInstance = null;
        Object plugDataInstance = null;
        boolean plugDataFlag = false;
        try {
            System.err.println("PlugManager.instantiatePlugin: plugin descriptor: " + pluginDesc);
            pluginClass = pcl.loadClass(pluginDesc.getClassStartup());
            pluginInstance = pluginClass.newInstance();
            if (pluginDesc.getPlugData() != null) if (!pluginDesc.getPlugData().equals("null")) {
                pluginPlugDataClass = pcl.loadClass(pluginDesc.getPlugData());
                plugDataInstance = pluginPlugDataClass.newInstance();
                plugDataFlag = true;
            }
            Method methodInit = pluginClass.getMethod("init", (Class[]) null);
            Class[] paramSetPlugDataWin = new Class[] { PlugDataWin.class };
            Method methodSetPlugDataWin = pluginClass.getMethod("setPlugDataWin", paramSetPlugDataWin);
            Class[] paramSetPlugData;
            Method methodSetPlugData = null;
            Method methodSetMain = null;
            if (plugDataFlag) {
                paramSetPlugData = new Class[] { IPlugData.class };
                methodSetPlugData = pluginClass.getMethod("setPlugData", paramSetPlugData);
                methodSetMain = pluginPlugDataClass.getMethod("setMainClass", pluginClass);
            }
            pluginStarted.addNewSessionPlugin(pluginDesc);
            Object[] objSetPlugDataWin = new Object[] { plugDataWin };
            methodSetPlugDataWin.invoke(pluginInstance, objSetPlugDataWin);
            Object[] objSetPlugData;
            if (plugDataFlag) {
                objSetPlugData = new Object[] { plugDataInstance };
                try {
                    methodSetPlugData.invoke(pluginInstance, objSetPlugData);
                    methodSetMain.invoke(plugDataInstance, pluginInstance);
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
            plugDataFlag = false;
            methodInit.invoke(pluginInstance, (Object[]) null);
            Method methodGetContentPane = pluginClass.getMethod("getContentPane", (Class[]) null);
            JInternalFrame pluginGUI = (JInternalFrame) methodGetContentPane.invoke(pluginInstance, (Object[]) null);
            cont.add(pluginGUI);
            ((Plugin) pluginInstance).setPluginDescriptor(pluginDesc);
            ((Plugin) pluginInstance).setSessionId(pluginDesc.getIdentifier());
            pluginGUI.setSize(new Dimension(550, 450));
            pluginGUI.setLocation(100, 100);
            pluginGUI.show();
            cont.doLayout();
            cont.setVisible(true);
            log.debug("PluginManager... layout done!");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new PopeyeException(e.getCause());
        }
    }

    /**
	 * Returns the PluginDescription for the plugin contained in a JAR file
	 * @param jarFile The JAR file 
	 * @return The PluginDescription object for the plugin.
	 */
    private PlugInDescriptor obtainPluginDescription(JarFile jarFile) throws PopeyeException {
        PlugInDescriptor pd = null;
        Enumeration en = jarFile.entries();
        while (en.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) en.nextElement();
            String name = jarEntry.getName();
            if (name.endsWith(".xml")) {
                log.debug("--> XML file: " + jarEntry.getName() + " [" + jarEntry.getSize() + "]");
                InputStream is;
                try {
                    is = jarFile.getInputStream(jarEntry);
                    Properties props = getPropertiesFromXML(is);
                    pd = new PlugInDescriptor(props, this.pluginRegistry);
                } catch (IOException e) {
                    throw new PopeyeException(e.getCause());
                }
                break;
            }
        }
        return pd;
    }

    private Properties getPropertiesFromXML(InputStream is) throws PopeyeException {
        Properties props = new Properties();
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(is);
            doc.getDocumentElement().normalize();
            log.debug("Root element of the doc is " + doc.getDocumentElement().getNodeName());
            Element plugin = doc.getDocumentElement();
            String name = plugin.getAttribute("name");
            String id = plugin.getAttribute("id");
            String version = plugin.getAttribute("version");
            String vendorName = plugin.getAttribute("vendor-name");
            String className = plugin.getAttribute("class");
            String plugData = plugin.getAttribute("plugData");
            props.setProperty("name", name);
            props.setProperty("id", id);
            props.setProperty("vendorName", vendorName);
            props.setProperty("version", version);
            props.setProperty("class", className);
            if (plugData != null) {
                if (plugData.equals("")) props.setProperty("plugData", "null"); else props.setProperty("plugData", plugData);
            } else props.setProperty("plugData", "null");
        } catch (SAXParseException err) {
            log.error("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            log.error(" " + err.getMessage());
            throw new PopeyeException(err.getCause());
        } catch (SAXException e) {
            throw new PopeyeException(e.getCause());
        } catch (IOException e) {
            log.error("Problems opening XML file");
            throw new PopeyeException(e.getCause());
        } catch (ParserConfigurationException e) {
            log.error("Problems parsing XML file");
            throw new PopeyeException(e.getCause());
        }
        return props;
    }

    /**
	 * Registers a plugin into the naming service
	 * @param classname The complete class name of the plugin
	 */
    private void registerPluginNamingService(String classname) throws PopeyeException {
        String boundName = pluginName2boundName(classname);
        Set<Member> set = null;
        boolean alreadyRegistered = false;
        Member m = new MemberImpl(AddressAdapter.getLocalhostAddress());
        try {
            set = (Set<Member>) ctx.lookup(boundName);
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Member registeredMember = (Member) it.next();
                if (registeredMember.equals(m)) {
                    alreadyRegistered = true;
                    log.debug("PLUGIN " + classname + " is already registered in the Naming Service.");
                    break;
                }
            }
            if (!alreadyRegistered) {
                log.debug("PLUGIN " + classname + " is not registered in the Naming Service by this peer yet. Current peers: " + set);
                set.add(m);
            }
        } catch (NamingException nE) {
            log.debug("PLUGIN " + classname + " is not registered in the Naming Service by any peer yet.");
            set = new HashSet();
            set.add(m);
        }
        try {
            if (!alreadyRegistered) {
                ctx.rebind(boundName, set);
                log.info("Plugin " + classname + " registered in the Naming Service.");
            }
        } catch (Exception e) {
            throw new PopeyeException(e.getCause());
        }
    }

    public PlugInDescriptor installPlugin(File file) throws PopeyeException {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(file);
        } catch (IOException e) {
            System.err.println("PlugManager.installPlugin: " + e);
            throw new PopeyeException(e.getCause());
        }
        PlugInDescriptor pd = obtainPluginDescription(jarFile);
        try {
            jarFile.close();
        } catch (IOException e) {
            throw new PopeyeException(e.getCause());
        }
        if (pd == null) {
            log.error("Unable to obtain the plugin description from the file " + jarFile + "!");
        } else {
            registerPluginNamingService(pd.getName());
            File goodFile = verifyJarFileName(pd.getName(), file.getName(), file);
            pcl.addJarFile(goodFile);
        }
        return pd;
    }

    private File verifyJarFileName(String pluginName, String fileName, File srcFile) throws PopeyeException {
        File destFile = null;
        log.debug("######### JAR filename verification  ##########");
        log.debug("PluginName: " + pluginName);
        log.debug("FileName: " + fileName);
        int sep = pluginName.lastIndexOf('.');
        String correctFileName = pluginName.substring(sep + 1) + ".jar";
        if (!correctFileName.equals(fileName)) {
            try {
                destFile = new File("." + File.separator + PLUGIN_DIRECTORY + File.separator + correctFileName);
                if (destFile.exists()) {
                }
                FileUtils.moveFile(srcFile, destFile);
            } catch (Exception e) {
                log.debug(e.getMessage());
                throw new PopeyeException(e.getCause());
            }
        }
        if (destFile == null) {
            destFile = srcFile;
        }
        return destFile;
    }

    public void requestStopPlugin(Plugin pluginInstance) {
        Container cont = this.pluginRegistry.get(pluginInstance);
        cont.removeAll();
        cont.repaint();
        this.pluginRegistry.remove(pluginInstance);
        pluginInstance.stopPlugin();
        log.info("Plugin stopped!");
    }

    /**
	 * Returns a list of remote available plugins
	 * @return a list of plugin names
	 */
    public ArrayList getRemoteAvailablePlugins() throws PopeyeException {
        ArrayList list = new ArrayList();
        Member m = new MemberImpl(AddressAdapter.getLocalhostAddress());
        NamingEnumeration bindings = null;
        try {
            bindings = ctx.listBindings("");
            while (bindings.hasMore()) {
                Binding bd = (Binding) bindings.next();
                if (isPlugin(bd.getName())) {
                    Set set = (Set) bd.getObject();
                    Iterator it = set.iterator();
                    boolean local = false;
                    while (it.hasNext()) {
                        Member registeredMember = (Member) it.next();
                        if (registeredMember.equals(m)) {
                            local = true;
                            break;
                        }
                    }
                    if (!local) {
                        list.add(boundName2PluginName(bd.getName()));
                    }
                }
            }
        } catch (NamingException e) {
            throw new PopeyeException(e.getCause());
        }
        return list;
    }

    public static boolean isPlugin(String name) {
        return name.startsWith("_PLUGIN_");
    }

    /**
	 * Returns the plugin name from a bound name of the naming service
	 * @param name
	 * @return
	 */
    public static String boundName2PluginName(String name) {
        return name.substring(8);
    }

    public static String pluginName2boundName(String name) {
        return "_PLUGIN_" + name;
    }

    public void deletedPlugin(Plugin plugin) {
        this.pluginStarted.removePlugins(plugin.getPluginDescriptor().getIdentifier());
    }

    /**
	 *  
	 *  1) Vengono parsati i manifest file e le info sono inserite nel registro.
	 *  2) Vengono istanziati i plugin contenuti nel registro
	 *  3) Vengono istanziati gli extension point contenuti nel reistro
	 * 
	 *  4) in seguito viene chiamato il metodo init() dalla classe Interfaccia
	 * MODIFICA: IMPLEMENTATE LE FEATURE - EZIO
	 * 
	 * @author Ezio
	 */
    public void start() {
        this.eventService = new EventService();
        processPluginsManifestFile();
        setFeatureDefault();
        GrafoDipendenze grafoDipendenze = new GrafoDipendenze();
        this.pluginsCurrentFeature = grafoDipendenze.makeGrafoFeaturePlugin(this.pluginRegistry.getAllPluginDescriptor());
        startPlug(grafoDipendenze);
        startExtensionPoint(grafoDipendenze);
    }

    /**
	 * 
	 *  Nuova versione: aggancio delle componenti host e extender di tutti i plug agli extension-point 
	 * * + ricrea le dipendenze fra gli host/extender con l'extension-point su cui si agganciano
	 * + in ogni caso fa anche plug.init()
	 */
    public void init() {
        PlugInDescriptor[] plugins = this.getPluginInFeature();
        if (plugins != null) for (int i = 0; i < plugins.length; i++) {
            if (plugins[i].isPlugEdit()) try {
                (plugins[i].getIstanceEdit()).setSessionId(plugins[i].getIdentifier());
                IPlugData plugData = plugins[i].getIstanceEdit().getPlugData();
                if (plugData != null) plugDataManager.addPlugin(plugData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (plugins != null) for (int i = 0; i < plugins.length; i++) {
            if (plugins[i].isPlugEdit()) try {
                plugins[i].getIstanceEdit().setPlugDataWin(plugDataWin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.initHost();
        this.extensionProcess();
        if (plugins != null) for (int i = 0; i < plugins.length; i++) {
            if (plugins[i].isPlugEdit()) try {
                plugins[i].getIstanceEdit().init();
                registerPluginNamingService(plugins[i].getName());
                pluginStarted.addNewSessionPlugin(plugins[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.internalError.displayError();
    }

    /**
	 * Vengono parsati tutti i manifest file (plugin.xml)dei plugin istallati
	 *  e le informazioni vengono inserite nel registro.
	 * 
	 *
	 *@author Ezio
	 */
    private void processPluginsManifestFile() {
        String dirPlugin = PopeyeFile.dirPlug();
        Collection sm = getListaOrder(dirPlugin);
        if (sm == null) return;
        Iterator ite = sm.iterator();
        while (ite.hasNext()) {
            File f = new File(filePlug(dirPlugin, (String) ite.next()));
            if ((f.exists()) && (f.isFile())) {
                XMLParsePlug xmlParser;
                xmlParser = new XMLParsePlug(this.internalError, this.pluginRegistry);
                PlugInDescriptor pd = xmlParser.parseFile(f);
                if ((pd != null) && (vincoliRispettati(pd))) {
                    this.pluginRegistry.addPlugin(pd);
                    pd.setGeneralClassLoader(this.generalClassLoader);
                }
            }
        }
    }

    /**
	 * ritorna i plugin elencati nel file di feature di default.
	 * - PluginModel[0] se nella feature di default non sono elencati plugin 
	 * - null se non esiste una feature di default o si sono riscontrati errori fatali nel parsing della feature di default.
	 * 
	 * 
	 * @author Ezio Di Nisio
	 * @return
	 */
    private FeatureDescriptor processFeature() {
        String dirFeatures = PopeyeFile.dirFeature();
        Collection sm = getListaOrder(dirFeatures);
        if (sm == null) return null;
        Iterator ite = sm.iterator();
        ArrayList lFileXml = new ArrayList();
        while (ite.hasNext()) {
            File f = new File(fileFeature(dirFeatures, (String) ite.next()));
            if ((f.exists()) && (f.isFile())) {
                lFileXml.add(f);
            }
        }
        File[] listaFileFeature = (File[]) lFileXml.toArray(new File[lFileXml.size()]);
        return this.parseFeature(listaFileFeature);
    }

    /**
	 * @author Ezio
	 * @param listaFileFeature
	 * @return
	 */
    private FeatureDescriptor parseFeature(File[] listaFileFeature) {
        for (int i = 0; i < listaFileFeature.length; i++) {
            XMLParseFeature xmlParser;
            xmlParser = new XMLParseFeature(this.internalError);
            FeatureDescriptor featureDescriptor = xmlParser.parseXmlFeature(listaFileFeature[i]);
            if (featureDescriptor != null) if (featureDescriptor.isFeatureDefault()) {
                return featureDescriptor;
            }
        }
        return null;
    }

    /**
	 * Vengono processate le features istallate.
	 * Rispetto alla feature di default, su tutti i plugin elencati viene settato a true il flag feature del plugin.
	 * 
	 * Se non c'e una feature di default definita, viene considerata la feature di default
	 * con tutti i plugin con versione piu recente istallati.
	 * 
	 *@author Ezio Di Nisio
	 *@return true se si riesce a caricare una feature di default definita.
	 * false altrimenti. In questo caso la feature di default diventa tutti i plugin istallati con versione piu recente.
	 *  
	 */
    private boolean setFeatureDefault() {
        boolean result = true;
        this.featureDefault = this.processFeature();
        if (featureDefault != null) for (int i = 0; i < featureDefault.getListaPluginFeature().length; i++) {
            PlugInDescriptor[] allPlugin = this.pluginRegistry.getAllPluginDescriptor();
            boolean ok = false;
            if (allPlugin != null) for (int j = 0; j < allPlugin.length; j++) {
                if (featureDefault.getListaPluginFeature()[i].matchModel(allPlugin[j])) {
                    allPlugin[j].setInFeature(true);
                    ok = true;
                }
            }
            if (!ok) this.internalError.addError(Error.ERROR, featureDefault.getListaPluginFeature()[i].getIdentifier(), featureDefault.getListaPluginFeature()[i].getVersion(), "plugin defined in default feature not found. See log file", null);
        } else {
            result = false;
            this.featureDefault = new FeatureDescriptor("Feature di Sistema", "1.0.0");
            PlugInDescriptor[] allPlugToLoad = this.pluginRegistry.getAllRecentPluginDescriptor();
            for (int i = 0; i < allPlugToLoad.length; i++) {
                featureDefault.addPluginFeature(allPlugToLoad[i].getIdentifier(), allPlugToLoad[i].getVersion());
                allPlugToLoad[i].setInFeature(true);
            }
        }
        return result;
    }

    /**
	 * Istanzia i plugin nel sistema 
	 * rispettando il grafo delle dipendenze fra i plugin
	 * 
	 */
    private void startPlug(GrafoDipendenze grafoDipendenze) {
        Nodo[] grafoPlugin = grafoDipendenze.makeGrafoMarcabile(this.pluginsCurrentFeature);
        if (grafoPlugin != null) for (int i = 0; i < grafoPlugin.length; i++) {
            PluginModel[] listaArchiUscenti = grafoPlugin[i].getListaArchiUscenti();
            if (listaArchiUscenti == null) {
                grafoPlugin[i].setMarchio('T');
                PlugInDescriptor pd = (PlugInDescriptor) grafoPlugin[i].getDataNodo();
                try {
                    this.istanzaPlugEdit(pd);
                    pd.setOrdineDiAttivazione(this.ordineAttivazionePlug);
                    ordineAttivazionePlug++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        while (!grafoDipendenze.IsGrafoMarcato(grafoPlugin)) {
            for (int i = 0; i < grafoPlugin.length; i++) {
                if (grafoDipendenze.IsNodoMarcabile(grafoPlugin[i], grafoPlugin)) {
                    grafoPlugin[i].setMarchio('T');
                    PlugInDescriptor pd = (PlugInDescriptor) grafoPlugin[i].getDataNodo();
                    try {
                        this.istanzaPlugEdit(pd);
                        pd.setOrdineDiAttivazione(this.ordineAttivazionePlug);
                        ordineAttivazionePlug++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
	 * 
	 *  Charmy Project
	 *  @author ezio di nisio
	 *  Per ogni plugin edit/algo istanzia ed inizializza tutti gli extension-point.
	 */
    private void startExtensionPoint(GrafoDipendenze grafoDipendenze) {
        ExtensionPointDescriptor[] allExtensionPointFeature = this.getExtensionPointInFeature();
        for (int i = 0; i < allExtensionPointFeature.length; i++) {
            this.IstanceExtPoint(allExtensionPointFeature[i]);
        }
        for (int i = 0; i < allExtensionPointFeature.length; i++) {
            allExtensionPointFeature[i].getExtensionPointReference().init(this, allExtensionPointFeature[i]);
        }
    }

    /**
	 * crea una istanza di una classe ricavato dal plugIndescriptor
	 * @param pd PlugInDescriptor che descrive la classe da istanziare
	 * 					e le funzioni di ingresso.
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
    private Plugin istanzaPlugEdit(PlugInDescriptor pd) {
        if (!pd.isPlugEdit()) {
            return null;
        }
        Plugin imp = null;
        try {
            imp = pd.getIstanceEdit();
            Method methodSetMain = null;
            Class pluginPlugDataClass = null;
            Class pluginClass = null;
            pluginClass = pcl.loadClass(pd.getClassStartup());
            Object plugDataInstance = null;
            if (pd.getPlugData() != null) if (!pd.getPlugData().equals("null")) {
                pluginPlugDataClass = pcl.loadClass(pd.getPlugData());
                plugDataInstance = pluginPlugDataClass.newInstance();
                imp.setPlugData((IPlugData) plugDataInstance);
                methodSetMain = pluginPlugDataClass.getMethod("setMainClass", pluginClass);
                methodSetMain.invoke(plugDataInstance, imp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imp;
    }

    /**
	 * 
	 *  Charmy Project
	 *  @author ezio di nisio
	 *  Istanzia un extension point 
	 */
    private IExtensionPoint IstanceExtPoint(ExtensionPointDescriptor extPointDescriptor) {
        Class extPointClass = null;
        IExtensionPoint refExtPoint = null;
        String classpath = extPointDescriptor.getClassPath();
        try {
            extPointClass = generalClassLoader.loadClass(classpath);
            refExtPoint = (IExtensionPoint) extPointClass.newInstance();
            extPointDescriptor.setExtensionPointReference(refExtPoint);
        } catch (ClassNotFoundException e) {
            return null;
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (SecurityException e) {
            return null;
        } catch (ExceptionInInitializerError e) {
            return null;
        } catch (ClassCastException e) {
            return null;
        }
        return refExtPoint;
    }

    private void initHost() {
        PlugInDescriptor[] plugins = this.getPluginInFeature();
        if (plugins != null) for (int i = 0; i < plugins.length; i++) {
            if (plugins[i].isPlugEdit()) try {
                Plugin refPlug = plugins[i].getIstanceEdit();
                if (refPlug != null) {
                    DeclaredHost[] hostsRuntime = null;
                    hostsRuntime = refPlug.initHost();
                    if (hostsRuntime == null) hostsRuntime = new DeclaredHost[0];
                    HostDescriptor[] hostsDeclaredInManifest = plugins[i].getHosts();
                    if (hostsDeclaredInManifest != null) for (int j = 0; j < hostsDeclaredInManifest.length; j++) {
                        String currentIdHost = hostsDeclaredInManifest[j].getId();
                        boolean atRuntime = false;
                        for (int k = 0; k < hostsRuntime.length; k++) {
                            if (hostsRuntime[k].getIdHost().compareTo(currentIdHost) == 0) {
                                atRuntime = true;
                                hostsDeclaredInManifest[j].setHostReference(hostsRuntime[k].getHost());
                                IGenericHost host = hostsRuntime[k].getHost();
                                Method setPluginOwner = host.getClass().getMethod("setPluginOwner", new Class[] { Plugin.class });
                                setPluginOwner.invoke(host, (Object) new Plugin[] { (Plugin) refPlug });
                                IExtensionPoint extPoint = this.getExtensionPoint(hostsDeclaredInManifest[j].getExtensionPoint());
                                Method setExtensionPointOwner = host.getClass().getMethod("setExtensionPointOwner", new Class[] { IExtensionPoint.class });
                                setExtensionPointOwner.invoke(host, new Object[] { extPoint });
                                break;
                            }
                        }
                        if (!atRuntime) {
                            try {
                                Class HostClass = generalClassLoader.loadClass(hostsDeclaredInManifest[j].getClassPath());
                                if (!HostClass.isInstance(plugins[i].getIstanceEdit())) {
                                    IGenericHost hostRef = (IGenericHost) HostClass.newInstance();
                                    hostsDeclaredInManifest[j].setHostReference(hostRef);
                                    Method setPluginOwner = hostRef.getClass().getMethod("setPluginOwner", new Class[] { Plugin.class });
                                    setPluginOwner.invoke(hostRef, new Object[] { refPlug });
                                    IExtensionPoint extPoint = this.getExtensionPoint(hostsDeclaredInManifest[j].getExtensionPoint());
                                    Method setExtensionPointOwner = hostRef.getClass().getMethod("setExtensionPointOwner", new Class[] { IExtensionPoint.class });
                                    setExtensionPointOwner.invoke(hostRef, new Object[] { extPoint });
                                } else {
                                    hostsDeclaredInManifest[j].setHostReference((IGenericHost) plugins[i].getIstanceEdit());
                                    IGenericHost host = (IGenericHost) plugins[i].getIstanceEdit();
                                    Method setPluginOwner = host.getClass().getMethod("setPluginOwner", new Class[] { Plugin.class });
                                    setPluginOwner.invoke(host, new Object[] { refPlug });
                                    IExtensionPoint extPoint = this.getExtensionPoint(hostsDeclaredInManifest[j].getExtensionPoint());
                                    Method setExtensionPointOwner = host.getClass().getMethod("setExtensionPointOwner", new Class[] { IExtensionPoint.class });
                                    setExtensionPointOwner.invoke(host, new Object[] { extPoint });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** 
	 *  Charmy Project -
	 * Vengono processate le richieste di aggancio degli host da 
	 * parte dei plugin sugli extension point. Nell'aggancio si segue il grafo delle dipendenze fra ext-point- 
	 * L'extension point esegue l'aggancio vero e proprio, se ritorna errore/null l'host viene rimosso dalla lista del PlugManager
	 * 
	 *  @author ezio di nisio
	 *
	 */
    private void extensionProcess() {
        GrafoDipendenze grafoDipendenze = new GrafoDipendenze();
        Nodo[] grafoExtensionPoint = grafoDipendenze.makeGrafoMarcabile(this.getExtensionPointInFeature());
        for (int y = 0; y < grafoExtensionPoint.length; y++) {
            PluginModel[] listaArchiUscenti = grafoExtensionPoint[y].getListaArchiUscenti();
            if (listaArchiUscenti == null) {
                grafoExtensionPoint[y].setMarchio('T');
                this.notifyHostAttach((ExtensionPointDescriptor) grafoExtensionPoint[y].getDataNodo());
            }
        }
        while (!grafoDipendenze.IsGrafoMarcato(grafoExtensionPoint)) {
            for (int y = 0; y < grafoExtensionPoint.length; y++) {
                if (grafoDipendenze.IsNodoMarcabile(grafoExtensionPoint[y], grafoExtensionPoint)) {
                    grafoExtensionPoint[y].setMarchio('T');
                    this.notifyHostAttach((ExtensionPointDescriptor) grafoExtensionPoint[y].getDataNodo());
                }
            }
        }
    }

    /** 
	 *  Charmy Project -
	 *  Notifica ad un extension point(idExtPoint) la richiesta di attacco degli host da parte di tutti i plugin
	 *  @author ezio di nisio
	 *
	 */
    private void notifyHostAttach(ExtensionPointDescriptor extPointDescriptor) {
        IExtensionPoint extensionPoint = this.getExtensionPoint(extPointDescriptor.getIdentifier());
        HostDescriptor[] list = this.pluginRegistry.getHostsForExtPointId(extPointDescriptor.getIdentifier());
        if (list != null) for (int i = 0; i < list.length; i++) {
            if (list[i].getHostReference() != null) {
                String[] hostsRequired = list[i].getHostRequired();
                if (hostsRequired != null) for (int k = 0; k < hostsRequired.length; k++) {
                    if (this.getHost(hostsRequired[k]) == null) {
                        this.pluginRegistry.removeHost(list[i].getId());
                        return;
                    }
                }
                boolean result = extensionPoint.addHost(list[i]);
                if (!result) {
                    this.pluginRegistry.removeHost(list[i].getId());
                } else {
                    list[i].setActivated(true);
                    list[i].getHostReference().setEventService(this.eventService);
                }
            }
        }
    }

    public class GrafoDipendenze {

        /**
		 * 
		 * @author Ezio
		 *
		 * TODO To change the template for this generated type comment go to
		 * Window - Preferences - Java - Code Style - Code Templates
		 */
        public class Nodo {

            private char marchio = 'F';

            private PluginModel dataNodo;

            public Nodo(PluginModel dataNodo) {
                this.dataNodo = dataNodo;
            }

            /**
			 * @return Returns the dataNodo.
			 */
            public PluginModel getDataNodo() {
                return dataNodo;
            }

            /**
			 * @return Returns the listaArchiUscenti.
			 */
            public PluginModel[] getListaArchiUscenti() {
                return this.dataNodo.dependenceList;
            }

            /**
			 * @return Returns the marchio.
			 */
            public char getMarchio() {
                return marchio;
            }

            /**
			 * @param marchio The marchio to set.
			 */
            public void setMarchio(char marchio) {
                this.marchio = marchio;
            }
        }

        /**
		 * 
		 * @param listaNodiCandidati
		 */
        public GrafoDipendenze() {
        }

        private PluginModel[] verificaCoerenza(PluginModel[] grafo) {
            ArrayList result = new ArrayList();
            if (grafo != null) for (int i = 0; i < grafo.length; i++) {
                PluginModel[] listaCorrenteArchiUscenti = grafo[i].getDependenceList();
                if (listaCorrenteArchiUscenti.length == 0) result.add(grafo[i]);
            }
            boolean finito = false;
            while (!finito) {
                finito = true;
                if (grafo != null) for (int i = 0; i < grafo.length; i++) {
                    PluginModel[] listaCorrenteArchiUscenti = grafo[i].getDependenceList();
                    boolean ok = true;
                    if (listaCorrenteArchiUscenti.length != 0) {
                        for (int j = 0; j < listaCorrenteArchiUscenti.length; j++) {
                            if (!this.nodoExist(listaCorrenteArchiUscenti[j], grafo)) {
                                ok = false;
                                finito = false;
                                break;
                            }
                        }
                        if (ok) {
                            if (!result.contains(grafo[i])) result.add(grafo[i]);
                        } else if (result.contains(grafo[i])) result.remove(grafo[i]);
                    }
                }
                grafo = (PluginModel[]) result.toArray(new PluginModel[result.size()]);
            }
            return grafo;
        }

        private PluginModel[] getSottoGrafoCompleto(PluginModel[] grafo, PluginModel nodoIniziale) {
            ArrayList result = new ArrayList();
            Stack stackAppoggio = new Stack();
            stackAppoggio.push(nodoIniziale);
            while (!stackAppoggio.isEmpty()) {
                PluginModel nodoCorrente = (PluginModel) stackAppoggio.pop();
                if (!result.contains(nodoCorrente)) {
                    result.add(nodoCorrente);
                    PluginModel[] listaArchiUscenti = nodoCorrente.getDependenceList();
                    if (listaArchiUscenti != null) for (int i = 0; i < listaArchiUscenti.length; i++) {
                        PluginModel nodoAdiacente = null;
                        for (int j = 0; j < grafo.length; j++) {
                            if (grafo[j].matchModel(listaArchiUscenti[i])) {
                                nodoAdiacente = grafo[j];
                                break;
                            }
                        }
                        if (nodoAdiacente != null) stackAppoggio.push(nodoAdiacente);
                    }
                }
            }
            return (PluginModel[]) result.toArray(new PluginModel[result.size()]);
        }

        public PluginModel[] makeGrafoFeaturePlugin(PluginModel[] allPluginInstalled) {
            ArrayList result = new ArrayList();
            PluginModel[] grafoCompletoCoerente = verificaCoerenza(allPluginInstalled);
            if (grafoCompletoCoerente != null) for (int i = 0; i < grafoCompletoCoerente.length; i++) {
                if ((grafoCompletoCoerente[i].isInFeature())) {
                    if (!result.contains(grafoCompletoCoerente[i])) {
                        result.add(grafoCompletoCoerente[i]);
                        PluginModel[] subGrafoFeature = this.getSottoGrafoCompleto(grafoCompletoCoerente, grafoCompletoCoerente[i]);
                        if (subGrafoFeature != null) for (int j = 0; j < subGrafoFeature.length; j++) {
                            if (!result.contains(subGrafoFeature[j])) result.add(subGrafoFeature[j]);
                        }
                    }
                }
            }
            return (PluginModel[]) result.toArray(new PluginModel[result.size()]);
        }

        public Nodo[] makeGrafoMarcabile(PluginModel[] nodi) {
            if (nodi == null) return new Nodo[0];
            Nodo[] result = new Nodo[nodi.length];
            for (int i = 0; i < nodi.length; i++) {
                result[i] = new Nodo(nodi[i]);
            }
            return result;
        }

        public boolean IsGrafoMarcato(Nodo[] grafoMarcabile) {
            if (grafoMarcabile == null) return true;
            for (int i = 0; i < grafoMarcabile.length; i++) {
                if (grafoMarcabile[i].getMarchio() == 'F') return false;
            }
            return true;
        }

        /**
		 * ritorna il nodo associato all'oggetto plug passato come argomento.
		 * la ricerca e fatta nel grafo passato come argomento.
		 * se ritorna null significa che il nodo non e presente nel grafo passato come argomento .
		 * @param dataNodo
		 * @return
		 */
        public Nodo getNodo(PluginModel dataNodo, Nodo[] grafo) {
            if ((grafo == null) || (dataNodo == null)) return null;
            for (int i = 0; i < grafo.length; i++) {
                if (dataNodo.matchModel(grafo[i].getDataNodo())) return grafo[i];
            }
            return null;
        }

        public boolean nodoExist(PluginModel dataNodo, PluginModel[] grafo) {
            if ((grafo == null) || (dataNodo == null)) return false;
            for (int i = 0; i < grafo.length; i++) {
                if (dataNodo.matchModel(grafo[i])) return true;
            }
            return false;
        }

        /**
		 * 
		 * PER DEFINIZIONE
		 * Un nodo di un grafo (Nodo[]) e definito marcabile se:
		 * 1) e gia marcato, oppure
		 * 2)non ha archi uscenti, oppure
		 * 3) gli archi uscenti arrivano tutti su nodi gia marcati
		 * 
		 * 
		 * @param grafo
		 * @author Ezio Di Nisio
		 * @return true se il nodo e marcabile, false altrimenti
		 */
        private boolean IsNodoMarcabile(Nodo nodo, Nodo[] grafo) {
            if (nodo == null) return false;
            if (nodo.getMarchio() == 'T') return false;
            PluginModel[] archiUscenti = nodo.getListaArchiUscenti();
            if (archiUscenti == null) return true;
            if (archiUscenti.length == 0) return true;
            int cont = 0;
            for (int i = 0; i < archiUscenti.length; i++) {
                if (this.getNodo(archiUscenti[i], grafo).getMarchio() == 'T') cont++;
            }
            if (cont == archiUscenti.length) return true; else return false;
        }

        private boolean isNodoSorgente(PluginModel nodo, PluginModel[] grafo) {
            int test = -1;
            if ((grafo != null) && (nodo != null)) for (int i = 0; i < grafo.length; i++) {
                PluginModel[] listaCorrenteArchiUscenti = grafo[i].getDependenceList();
                test = 1;
                if (listaCorrenteArchiUscenti != null) for (int j = 0; j < listaCorrenteArchiUscenti.length; j++) {
                    if (listaCorrenteArchiUscenti[j].matchModel(nodo)) {
                        test = 0;
                        break;
                    }
                }
                if (test == 0) break;
            }
            if (test == -1) return false;
            if (test == 0) return false;
            if (test == 1) return true;
            return false;
        }
    }

    /**
	 * Controlliamo i vincoli che deve rispettare un plugin per poter essere inserito nel registro
	 * - tipo id, classe , ... dichiararti nel manifest file ->>>>>> DA IMPLEMENTARE ---
	 * @param pd
	 * @return
	 */
    private boolean vincoliRispettati(PlugInDescriptor pd) {
        return true;
    }

    /**
	 * 
	 *  Charmy Project
	 * 
	 
	 * @author ezio di nisio     
	 * @return IExtensionPoint - Ritorna il reference all'extension-point 
	 * con identificatore uguale a quello passato come argomento, null se non c'e. 
	 */
    public IExtensionPoint getExtensionPoint(String extPointId) {
        return this.pluginRegistry.getExtensionPointForId(extPointId).getExtensionPointReference();
    }

    /**
	 * 
	 */
    public ExtensionPointDescriptor getExtensionPointDescriptor(String idExtPoint) {
        return this.pluginRegistry.getExtensionPointForId(idExtPoint);
    }

    public IGenericHost getHost(String idHost) {
        HostDescriptor host = this.pluginRegistry.getHostForId(idHost);
        if (host != null) return host.getHostReference();
        return null;
    }

    /**
	 * crea un'array di plug-in ordinato per nome
	 * @return ritorna una Collection Ordinata  di nomi di file 
	 * 				oppure null se la Collection ? vuota
	 */
    public Collection getListaOrder(String directory) {
        File fLista = new File(directory);
        TreeMap sm = null;
        if (!fLista.exists()) {
            return null;
        }
        String lista[] = fLista.list(new FiltraDir());
        if (lista.length > 0) {
            sm = new TreeMap();
            for (int i = 0; i < lista.length; i++) {
                sm.put(lista[i], lista[i]);
            }
        }
        if (sm == null) return null;
        return sm.values();
    }

    /**
	 * ritorna un riferimento al plug-in editor con 
	 * id ottenuto dal file xml = identificativo
	 * @return IMainTabPanel rappresentante il plug  oppure null se 
	 *                  nessun plug ha quell'identificativo
	 */
    public Plugin getPlugEdit(String identificativo) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        for (int i = 0; i < this.pluginsCurrentFeature.length; i++) {
            PlugInDescriptor pd = (PlugInDescriptor) pluginsCurrentFeature[i];
            if (pd.getIdentifier().equals(identificativo)) {
                if (pd.isPlugEdit()) {
                    return pd.getIstanceEdit();
                }
            }
        }
        return null;
    }

    /**
	 *  restituisce il PlugInDescriptor relativo al plug edit passato come argomento. 
	 * 
	 *  Charmy Project
	 *  @author ezio di nisio
	 *
	 */
    public PlugInDescriptor getPlugEditDescriptor(Plugin plug) {
        return this.pluginRegistry.getPluginDescriptorFor(plug);
    }

    /**
	 * restituisce il descriptor del plugin in feature passato come argomento.
	 * Se nella feature corrente non e presente il plug viene restituito il descriptor con versione piu recente istallato, null altrimenti.
	 * @param idPlugin 
	 * @return
	 */
    public PlugInDescriptor getPluginDescriptor(String idPlugin) {
        System.err.println("TEST - getPluginDescriptor");
        for (int i = 0; i < this.pluginsCurrentFeature.length; i++) {
            if (((PlugInDescriptor) pluginsCurrentFeature[i]).getIdentifier().compareTo(idPlugin) == 0) {
                return (PlugInDescriptor) pluginsCurrentFeature[i];
            }
        }
        return this.pluginRegistry.getPluginDescriptor(idPlugin);
    }

    /**
	 * ritorna una stringa del filename plugin.xml
	 * completo di  percorso assoluto
	 * 
	 * @param absFile directory da agganciare
	 * @return dir + FileSeparator + absFile + FileSeparator + plugin.xml
	 */
    private String filePlug(String dir, String absFile) {
        String d = PopeyeFile.addFileSeparator(dir).concat(absFile);
        return PopeyeFile.addFileSeparator(d).concat(PopeyeFile.PLUGIN_FILENAME);
    }

    /**
	 * ritorna una stringa del filename feature.xml
	 * completo di  percorso assoluto
	 * @author Ezio
	 * @param absFile directory da agganciare
	 * @return dir + FileSeparator + absFile + FileSeparator + feature.xml
	 */
    private String fileFeature(String dir, String absFile) {
        String d = PopeyeFile.addFileSeparator(dir).concat(absFile);
        return PopeyeFile.addFileSeparator(d).concat(PopeyeFile.FEATURE_FILENAME);
    }

    /**
	 * Inner class per implementare un filtro di file
	 * per cercare sole le directory
	 * @author michele
	 * Charmy plug-in
	 *
	 */
    public class FiltraDir implements FilenameFilter {

        public boolean accept(File dir, String name) {
            String direct = dir.getAbsolutePath();
            File f = new File(PopeyeFile.addFileSeparator(direct).concat(name));
            return f.isDirectory();
        }
    }

    /**
	 * Preleva il contenitore dei dati
	 * @return Contenitore centralizzato dei dati
	 */
    public PlugDataManager getPlugDataManager() {
        return plugDataManager;
    }

    /**
	 * Preleva il contenitore della finestra
	 * @return Contenitore dati finestre
	 */
    public PlugDataWin getPlugDataWin() {
        return plugDataWin;
    }

    /**
	 * @return Returns the eventService.
	 */
    public EventService getEventService() {
        return eventService;
    }

    /**
	 * @return Returns the pluginRegistry.
	 */
    public PluginRegistry getPluginRegistry() {
        return pluginRegistry;
    }

    /**
	 * @return Returns the pluginRegistry.
	 */
    public PluginRegistry getStartedPluginRegistry() {
        return pluginStarted;
    }

    public PlugInDescriptor[] getPluginInFeature() {
        PlugInDescriptor[] pd = new PlugInDescriptor[this.pluginsCurrentFeature.length];
        for (int j = 0; j < this.pluginsCurrentFeature.length; j++) {
            pd[j] = (PlugInDescriptor) pluginsCurrentFeature[j];
        }
        return pd;
    }

    public ExtensionPointDescriptor[] getExtensionPointInFeature() {
        ArrayList extensionPointFeature = new ArrayList();
        PlugInDescriptor[] list = this.getPluginInFeature();
        for (int i = 0; i < list.length; i++) {
            ExtensionPointDescriptor[] currentExtPoint = list[i].getExtensionPoints();
            if (currentExtPoint != null) for (int j = 0; j < currentExtPoint.length; j++) {
                extensionPointFeature.add(currentExtPoint[j]);
            }
        }
        return (ExtensionPointDescriptor[]) extensionPointFeature.toArray(new ExtensionPointDescriptor[extensionPointFeature.size()]);
    }

    /**
	 * @return Returns the featureDefault.
	 */
    public FeatureDescriptor getFeatureDefault() {
        return featureDefault;
    }

    public GeneralURLClassLoader getGeneralClassLoader() {
        return generalClassLoader;
    }
}
