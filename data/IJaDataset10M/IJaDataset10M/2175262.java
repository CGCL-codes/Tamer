package eu.planets_project.ifr.core.wee.impl.workflow;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import eu.planets_project.ifr.core.wee.api.wsinterface.WftRegistryService;
import eu.planets_project.ifr.core.wee.api.workflow.ServiceCallConfigs;
import eu.planets_project.ifr.core.wee.api.workflow.WorkflowContext;
import eu.planets_project.ifr.core.wee.api.workflow.WorkflowInstance;
import eu.planets_project.ifr.core.wee.api.workflow.WorkflowTemplate;
import eu.planets_project.ifr.core.wee.api.workflow.generated.WorkflowConf;
import eu.planets_project.ifr.core.wee.api.workflow.generated.WorkflowConf.Services.Service;
import eu.planets_project.ifr.core.wee.api.workflow.generated.WorkflowConf.Services.Service.Parameters;
import eu.planets_project.ifr.core.wee.api.workflow.generated.WorkflowConf.Services.Service.Parameters.Param;
import eu.planets_project.ifr.core.wee.impl.registry.WftRegistryImpl;
import eu.planets_project.ifr.core.wee.impl.utils.FileUtil;
import eu.planets_project.ifr.core.wee.impl.utils.RegistryUtils;
import eu.planets_project.services.PlanetsService;
import eu.planets_project.services.PlanetsServices;
import eu.planets_project.services.datatypes.DigitalObject;

/**
 * @author <a href="mailto:andrew.lindley@arcs.ac.at">Andrew Lindley</a>
 * @since 30.10.2008
 * 
 * A factory object that extracts the
 * - xml configuration (throug a JaxB extracted API of it) of a workflowTemplate (templateQName, serviceConfigs, etc.)
 * - initializes its declared services 
 * - initializes the data to invoke the workflow upon (in terms of DigitalObjects)
 * hands back a WorkflowInstance which can be executed by the WEE
 *
 */
public class WorkflowFactory {

    private static Log log = LogFactory.getLog(WorkflowFactory.class);

    private static String tempDir = RegistryUtils.getWeeDirBase() + RegistryUtils.getWeeTmpDir();

    private WorkflowFactory() {
    }

    private static String classpath = null;

    static {
        try {
            classpath = buildClasspath();
        } catch (IOException e) {
        }
    }

    /**
	 * Extracts the information handed over in the xml workflow configuration (workflowTemplate, serviceIDs, endpoints, params, etc.)
	 * to initialize the services of the workflowTemplate and the DigitalObjects (=data) the workflow shall start operating upon
	 * @param wfConf
	 * @param digos
	 * @return A workflowInstance which can be executed upon the WEE (WorkflowExecutionEngine)
	 * @throws Exception
	 */
    public static WorkflowInstance create(WorkflowConf wfConf, List<DigitalObject> digos) throws Exception {
        String QName = wfConf.getTemplate().getClazz();
        File fTemplateJava = fetchDataToTempDir(QName);
        File fClazz = compileTemplateClassAtRuntime(fTemplateJava);
        File fJar = buildJarFile(fClazz, QName);
        addClassToClassPath(Thread.currentThread().getContextClassLoader(), fJar);
        boolean bCheckOK = checkIsValidInstanceOfWFTemplate(QName);
        if (!bCheckOK) {
            String err = "The provided WFTemplate: " + QName + " is not a valid implementation";
            log.debug(err);
            throw new Exception(err);
        }
        WorkflowTemplate wft = (WorkflowTemplate) Class.forName(QName).newInstance();
        log.debug("WorkflowFactory: " + QName + " loaded, compiled and classloaded");
        workflowContext = null;
        List<Field> declaredServices = getDeclaredWFServices(wft);
        int iCount = 0;
        for (Field declaredService : declaredServices) {
            String declaredServiceID = declaredService.getName();
            for (Service serviceConf : wfConf.getServices().getService()) {
                String serviceConfID = serviceConf.getId();
                if (declaredServiceID.equals(serviceConfID)) {
                    initWFService(wft, declaredService, serviceConf);
                    iCount++;
                }
            }
        }
        if (iCount != declaredServices.size()) {
            String err = "The provided Workflow configuration is not suitable for the given workflow " + wft.getClass().getCanonicalName() + "1..n service configurations missing";
            log.debug(err);
            throw new Exception(err);
        }
        wft.setData(digos);
        wft.setWorkflowContext(getOrInitWFContext());
        WorkflowInstance wfi = new WorkflowInstanceImpl(wft);
        wft.setWorkflowInstanceID(wfi.getWorkflowID());
        return wfi;
    }

    /**
	 * Uses Java reflection to get the list of declared Planets services within a workflowTemplate
	 * @param wft
	 * @return
	 */
    private static List<Field> getDeclaredWFServices(WorkflowTemplate wft) {
        Class clazz = wft.getClass();
        List<Field> ret = new ArrayList<Field>();
        for (Field f : clazz.getDeclaredFields()) {
            if (wft.isServiceTypeSupported(f)) {
                ret.add(f);
            }
        }
        return ret;
    }

    /**
	 * This method initializes the pre-declared services (of a workflowTemplate) with the information
	 * provided in the workflow configuration xml by using Java reflection API.
	 * Currently only the endpoint information is used - parameters will be added later
	 * 
	 * Service service = Service.create(new URL(serviceConf.getEndpoint()), new QName(PlanetsServices.NS,
	                Identify.NAME));
	   planetsService = (Identify) service.getPort(Identify.class);
	 * 
	 * @param wft The workflowTemplate instance 
	 * @param declaredWFService A java.lang.reflect.Field of the wft containing a PlanetService
	 * @param Service: serviceConf: The endpoint information and parameters obtained by the workflow config xml
	 * @throws Exception
	 */
    private static void initWFService(WorkflowTemplate wft, Field declaredWFService, Service serviceConf) throws Exception {
        String sServiceType = declaredWFService.getType().getCanonicalName();
        Class<PlanetsService> planetsServiceType = (Class<PlanetsService>) Class.forName(sServiceType);
        Field fServiceName = planetsServiceType.getField("NAME");
        String sServiceNameValue = (String) fServiceName.get(wft);
        log.debug("Creating a workflow service object with: " + serviceConf.getEndpoint() + " " + PlanetsServices.NS + " " + sServiceNameValue);
        javax.xml.ws.Service service = javax.xml.ws.Service.create(new URL(serviceConf.getEndpoint()), new QName(PlanetsServices.NS, sServiceNameValue));
        PlanetsService planetsService = planetsServiceType.cast(service.getPort(planetsServiceType));
        declaredWFService.setAccessible(true);
        declaredWFService.set(wft, planetsService);
        log.debug("Successfully instantiated a PlanetsService of interface type: " + planetsService.describe().getClassname());
        if (serviceConf.getParameters() != null) {
            setServiceParameters(wft, planetsService, serviceConf.getParameters());
        }
        getOrInitWFContext().putContextObject(planetsService, WorkflowContext.Property_ServiceEndpoint, serviceConf.getEndpoint());
    }

    /**
	 * @param wft the workflowTemplate we're currently configuring
	 * @param planetsService the PlanetsService instance obtained by reflection for which to set the parameter configuration
	 * @param serviceParams the parameter data for a service handed over by the xml config
	 */
    private static void setServiceParameters(WorkflowTemplate wft, PlanetsService planetsService, Parameters serviceParams) {
        ServiceCallConfigs serCallConf = new ServiceCallConfigs();
        for (Param param : serviceParams.getParam()) {
            if ((param.getName() != null) && (param.getValue() != null)) {
                serCallConf.setProperty(param.getName(), param.getValue());
            }
        }
        wft.setServiceCallConfigs(planetsService, serCallConf);
    }

    private static WorkflowContext workflowContext = null;

    private static WorkflowContext getOrInitWFContext() {
        if (workflowContext == null) {
            workflowContext = new WorkflowContext();
        }
        return workflowContext;
    }

    /**
	 * Uses the class com.sun.tools.javac.Main is the programming interface of the Javac compiler. 
	 * which provides static methods to compile Java source files. Loads a templateFile.java and compiles
	 * it within the same directory and returns the .class as File.
	 * @param templateJava
	 * @return errorCode: in 0 indicates success
	 */
    private static File compileTemplateClassAtRuntime(File templateJava) throws Exception {
        try {
            Class.forName("com.sun.tools.javac.Main");
        } catch (Exception e) {
            throw new Exception("WorkflowFactory: missing com.sun.tools.javac.Main - check tools.jar in the Classpath?");
        }
        int errorCode = com.sun.tools.javac.Main.compile(new String[] { "-classpath", buildClasspath(), templateJava.getAbsolutePath() });
        if (errorCode != 0) throw new Exception("WorkflowFactory: compile file " + templateJava.getName() + " not successfull");
        String sPath = templateJava.getParent();
        int p = templateJava.getName().indexOf(".java");
        int k = templateJava.getName().length();
        String name = templateJava.getName().substring(0, k - (k - p)) + ".class";
        return new File(sPath + "/" + name);
    }

    /**
	 * Constructs the classpath which is used to compile workflowtemplates.java items
	 * i.e. loads all jar files from //ifr_server/server/default/lib
	 * @return
	 * @throws IOException
	 */
    private static String buildClasspath() throws IOException {
        if (classpath == null) {
            String classp = "";
            File dir = new File(RegistryUtils.getWeeDirBase() + "/../../lib/");
            File[] list = dir.listFiles();
            for (File f : list) {
                classp += f.getCanonicalPath() + System.getProperty("path.separator");
            }
            dir = new File(RegistryUtils.getWeeDirBase() + "/../../deploy/jbossws.sar/");
            list = dir.listFiles();
            for (File f : list) {
                classp += f.getCanonicalPath() + System.getProperty("path.separator");
            }
            classpath = classp;
        }
        return classpath;
    }

    /**
	 * Takes a .jar file and uses the URIClassLoader to add it.
	 * @param classLoader
	 * @param jars
	 */
    private static void addClassToClassPath(ClassLoader classLoader, File jar) throws Exception {
        if (classLoader instanceof URLClassLoader) {
            try {
                Method addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
                addUrlMethod.setAccessible(true);
                if (null != addUrlMethod) {
                    addUrlMethod.invoke(classLoader, jar.toURI().toURL());
                }
            } catch (Exception e) {
                String err = "WorkflowFactory error adding compiled workflowtemplate jar " + jar.getName() + " to classpath";
                log.error(err, e);
                throw new Exception(err, e);
            }
        }
    }

    /**
	 * Takes one .class file and uses the java.util.zip package to build a .jar in the same 
	 * folder and name as the .class 
	 * @param clazz
	 * @throws Exception
	 */
    private static File buildJarFile(File clazz, String QName) throws Exception {
        ZipOutputStream out = null;
        try {
            if (clazz.getName().indexOf(".class") == -1) throw new Exception("WorflowFactory buildJarFile: only .class files accepted");
            String temp = QName.replace(".", "/");
            String name = temp.substring(temp.lastIndexOf("/") + 1, temp.length());
            String javaPath = temp.substring(0, temp.lastIndexOf("/"));
            File outFile = new File(clazz.getParent() + "/" + name + ".jar");
            out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
            byte[] data = new byte[1000];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(clazz), 1000);
            out.putNextEntry(new ZipEntry(javaPath + "/" + clazz.getName()));
            int count;
            while ((count = in.read(data, 0, 1000)) != -1) {
                out.write(data, 0, count);
            }
            out.closeEntry();
            out.flush();
            out.close();
            return outFile;
        } catch (Exception e) {
            String err = "WorflowFactory buildJarFile: error creating jar file for " + clazz.getName();
            log.debug(err, e);
            throw new Exception(err, e);
        }
    }

    /**
	 * Fetches the .java from the data registry and stores it within the WEE's temporary directory
	 * @param QName
	 */
    private static File fetchDataToTempDir(String QName) throws Exception {
        WftRegistryService wftRegistry = WftRegistryImpl.getInstance();
        byte[] data = wftRegistry.getWFTemplate(QName);
        FileUtil.writeFile(data, tempDir, QName.replace(".", "/") + ".java");
        return FileUtil.getTempFile(QName, "java");
    }

    /**
	 * This method is used to validate any xmlconfig independent information as
	 * e.g. in terms of if it implements the WorkflowTemplate interface
	 *  This method can be extended to check additional general QA parameters.
	 * @param QName
	 */
    private static boolean checkIsValidInstanceOfWFTemplate(String QName) {
        String check1 = "eu.planets_project.ifr.core.wee.api.workflow.WorkflowTemplate";
        try {
            Class[] c1 = Class.forName(QName).getInterfaces();
            if (c1.length != 0) {
                for (int i = 0; i < c1.length; i++) {
                    if (c1[i].getCanonicalName().equals(check1)) {
                        return true;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            log.error("WorkflowFactory checkIsValidInstanceofWFTemplate ClassNotFound QName " + QName, e);
        } catch (Exception e) {
            log.error("WorkflowFactory checkIsValidInstanceofWFTemplate unknown QName " + QName, e);
        }
        return false;
    }
}
