package com.ibm.hannover.development.tools.configurations;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.internal.launching.VMDefinitionsContainer;
import org.eclipse.jdt.launching.AbstractVMInstallType;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMStandin;
import org.eclipse.osgi.service.environment.Constants;

public abstract class VMConfiguration implements IConfigure {

    private static String CLAZZ = VMConfiguration.class.getName();

    private static Logger logger = Logger.getLogger(CLAZZ);

    protected String jrePath;

    protected String vmArgs;

    private IVMInstallType fSelectedVMType;

    protected IVMInstall newVM = null;

    private boolean isDefault = true;

    /**
	 * VMs being displayed
	 */
    private Map<IVMInstall, String> fVMs = new HashMap<IVMInstall, String>(2);

    public VMConfiguration(String path, boolean isDefault, String typeName) {
        this.isDefault = isDefault;
        fSelectedVMType = JavaRuntime.getVMInstallType(typeName);
        jrePath = path;
        initialize();
    }

    public VMConfiguration(String path, String vmArgs, boolean isDefault, String typeName) {
        this(path, isDefault, typeName);
        this.vmArgs = vmArgs;
    }

    public void configure(IProgressMonitor monitor) {
        String method = "configure";
        logger.entering(CLAZZ, method);
        if (!fVMs.containsValue(jrePath)) {
            logger.logp(Level.FINE, CLAZZ, method, "Create new vm.");
            newVM = new VMStandin(fSelectedVMType, createUniqueId(fSelectedVMType));
            fillVMFields(newVM);
            IVMInstall[] installeds = fVMs.keySet().toArray(new IVMInstall[0]);
            for (int i = 0; i < installeds.length; i++) {
                if (newVM.getName().equals(installeds[i].getName())) fVMs.remove(installeds[i]);
            }
            fVMs.put(newVM, newVM.getInstallLocation().getAbsolutePath());
            monitor.worked(5);
            save();
            monitor.worked(10);
        }
        logger.exiting(CLAZZ, method);
    }

    /**
	 * init fields to the specified VM
	 * 
	 * @param vm
	 *            the VM to init from
	 */
    protected abstract void fillVMFields(IVMInstall newVM);

    public abstract String getGeneratedVMName();

    private void initialize() {
        IVMInstallType[] fVMTypes = JavaRuntime.getVMInstallTypes();
        for (int i = 0; i < fVMTypes.length; i++) {
            IVMInstallType type = fVMTypes[i];
            IVMInstall[] installs = type.getVMInstalls();
            for (int j = 0; j < installs.length; j++) {
                IVMInstall install = installs[j];
                fVMs.put(new VMStandin(install), install.getInstallLocation().getAbsolutePath());
            }
        }
    }

    private IVMInstall getCurrentDefaultVM() {
        if (isDefault) return newVM;
        return JavaRuntime.getDefaultVMInstall();
    }

    private void save() {
        final String method = "save";
        logger.entering(CLAZZ, method);
        IVMInstall defaultVM = getCurrentDefaultVM();
        IVMInstall[] vms = (IVMInstall[]) fVMs.keySet().toArray(new IVMInstall[fVMs.size()]);
        try {
            VMDefinitionsContainer vmContainer = new VMDefinitionsContainer();
            String defaultVMId = JavaRuntime.getCompositeIdFromVM(defaultVM);
            vmContainer.setDefaultVMInstallCompositeID(defaultVMId);
            for (int i = 0; i < vms.length; i++) {
                vmContainer.addVM(vms[i]);
            }
            String vmDefXML = vmContainer.getAsXML();
            JavaRuntime.getPreferences().setValue(JavaRuntime.PREF_VM_XML, vmDefXML);
            JavaRuntime.savePreferences();
        } catch (CoreException e) {
            log(method, e);
        }
        logger.exiting(CLAZZ, method);
    }

    private void log(String method, Exception e) {
        logger.logp(Level.SEVERE, CLAZZ, method, "SEVERE: Fail to save jre configuration.", e);
    }

    /**
	 * Creates a unique name for the VMInstallType
	 * 
	 * @param vmType
	 *            the vm install type
	 * @return a unique name
	 */
    private String createUniqueId(IVMInstallType vmType) {
        String id = null;
        do {
            id = String.valueOf(System.currentTimeMillis());
        } while (vmType.findVMInstall(id) != null);
        return id;
    }

    /**
	 * Auto-detects the default javadoc location
	 */
    protected URL detectJavadocLocation() {
        if (fSelectedVMType instanceof AbstractVMInstallType) {
            AbstractVMInstallType type = (AbstractVMInstallType) fSelectedVMType;
            return type.getDefaultJavadocLocation(new File(jrePath));
        } else return null;
    }

    /**
	 * Utility class to parse command line arguments.
	 * 
	 * @since 3.1
	 */
    private static class ArgumentParser {

        private String fArgs;

        private int fIndex = 0;

        private int ch = -1;

        public ArgumentParser(String args) {
            fArgs = args;
        }

        public String[] parseArguments() {
            List<String> v = new ArrayList<String>();
            ch = getNext();
            while (ch > 0) {
                if (Character.isWhitespace((char) ch)) {
                    ch = getNext();
                } else {
                    if (ch == '"') {
                        StringBuffer buf = new StringBuffer();
                        buf.append(parseString());
                        if (buf.length() == 0 && Platform.getOS().equals(Constants.OS_WIN32)) {
                            buf.append("\"\"");
                        }
                        v.add(buf.toString());
                    } else {
                        v.add(parseToken());
                    }
                }
            }
            String[] result = new String[v.size()];
            v.toArray(result);
            return result;
        }

        private int getNext() {
            if (fIndex < fArgs.length()) return fArgs.charAt(fIndex++);
            return -1;
        }

        private String parseString() {
            ch = getNext();
            if (ch == '"') {
                ch = getNext();
                return "";
            }
            StringBuffer buf = new StringBuffer();
            while (ch > 0 && ch != '"') {
                if (ch == '\\') {
                    ch = getNext();
                    if (ch != '"') {
                        buf.append('\\');
                    } else {
                        if (Platform.getOS().equals(Constants.OS_WIN32)) {
                            buf.append('\\');
                        }
                    }
                }
                if (ch > 0) {
                    buf.append((char) ch);
                    ch = getNext();
                }
            }
            ch = getNext();
            return buf.toString();
        }

        private String parseToken() {
            StringBuffer buf = new StringBuffer();
            while (ch > 0 && !Character.isWhitespace((char) ch)) {
                if (ch == '\\') {
                    ch = getNext();
                    if (Character.isWhitespace((char) ch)) {
                        buf.append('\\');
                        return buf.toString();
                    }
                    if (ch > 0) {
                        if (ch != '"') {
                            buf.append('\\');
                        } else {
                            if (Platform.getOS().equals(Constants.OS_WIN32)) {
                                buf.append('\\');
                            }
                        }
                        buf.append((char) ch);
                        ch = getNext();
                    } else if (ch == -1) {
                        buf.append('\\');
                    }
                } else if (ch == '"') {
                    buf.append(parseString());
                } else {
                    buf.append((char) ch);
                    ch = getNext();
                }
            }
            return buf.toString();
        }
    }

    /**
	 * Parses the given command line into separate arguments that can be passed
	 * to <code>DebugPlugin.exec(String[], File)</code>. Embedded quotes and
	 * slashes are escaped.
	 * 
	 * @param args
	 *            command line arguments as a single string
	 * @return individual arguments
	 * @since 3.1
	 */
    public static String[] parseArguments(String args) {
        if (args == null) return new String[0];
        ArgumentParser parser = new ArgumentParser(args);
        String[] res = parser.parseArguments();
        return res;
    }

    public String getName() {
        return "Configure vm";
    }
}
