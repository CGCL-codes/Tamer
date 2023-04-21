package net.sf.jlibdc1394.config;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.sf.jlibdc1394.CameraException;
import net.sf.jlibdc1394.CameraPort;
import net.sf.jlibdc1394.JLibDC1394Helper;

/**
 * Holds and persits a set of {@link net.sf.jlibdc1394.config.CameraConfig}.
 * 
 * @author Alexander Bieber <fleque@users.sourceforge.net>
 *
 */
public class CameraRegistry {

    public static final String CAMERA_CONFIG_FILENAME = "net.sf.jlibdc1394.camconfig.xml";

    /**
	 * The file this registry uses to store the camera configurations
	 */
    private File confFile;

    /**
	 * key: Integer camConfigID<br/>
	 * value CameraConfig config<br/>
	 * 
	 * The configurations this registry holds
	 */
    private Map cameraConfigs;

    private int nextConfigID;

    private CameraPort camPort;

    public CameraRegistry() {
    }

    /**
	 * Constructs a new CameraRegistry. Do not use this
	 * constructor, use the static shared instance instead.
	 * 
	 * @see #getSharedInstance(String)
	 */
    protected CameraRegistry(boolean createPort, URL portConfURL) {
        super();
        if (createPort) createCamPort(portConfURL);
    }

    private void createCamPort(URL portConfURL) {
        try {
            if (portConfURL == null) camPort = JLibDC1394Helper.getCameraPort(); else camPort = JLibDC1394Helper.getJDC1394CamPort(portConfURL);
        } catch (CameraException e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * @return Returns the camPort.
	 */
    public CameraPort getCamPort() {
        return camPort;
    }

    /**
	 * Returns all JLibDC1394CamConfigs registered in this registry
	 * @return All JLibDC1394CamConfigs.
	 */
    public Collection getCamConfigs() {
        return cameraConfigs.values();
    }

    public CameraConfig getCamConfig(int camConfigID) {
        return (CameraConfig) cameraConfigs.get(new Integer(camConfigID));
    }

    public void removeCamConfig(int camConfigID) {
        Integer key = new Integer(camConfigID);
        if (cameraConfigs.containsKey(key)) cameraConfigs.remove(key);
    }

    public void removeCamConfig(CameraConfig config) {
        removeCamConfig(config.getConfigID());
    }

    /**
	 * Creates a new CameraConfig registeres and returnes it.
	 * 
	 * @return
	 */
    public CameraConfig newCamConfig() {
        CameraConfig result = new CameraConfig(++nextConfigID, this);
        setNextConfigID(nextConfigID);
        cameraConfigs.put(new Integer(result.getConfigID()), result);
        return result;
    }

    /**
	 * @return The probable configID assigned to the next config.
	 */
    public int getProbableNextConfigID() {
        return nextConfigID;
    }

    public void setRegistryDir(String registryDir) {
        File tmpDir = new File(registryDir);
        if (!tmpDir.exists()) {
            try {
                if (!tmpDir.createNewFile()) throw new RuntimeException("Given registryDir " + registryDir + " does not exists and could not be created.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        File newConfFile = new File(tmpDir, CAMERA_CONFIG_FILENAME);
        this.confFile = newConfFile;
    }

    /**
	 * @return Returns the cameraConfigs.
	 */
    public Map getCameraConfigs() {
        return cameraConfigs;
    }

    /**
	 * @param cameraConfigs The cameraConfigs to set.
	 */
    public void setCameraConfigs(Map cameraConfigs) {
        this.cameraConfigs = cameraConfigs;
    }

    /**
	 * @return Returns the nextConfigID.
	 */
    public int getNextConfigID() {
        return nextConfigID;
    }

    /**
	 * @param nextConfigID The nextConfigID to set.
	 */
    public void setNextConfigID(int nextConfigID) {
        this.nextConfigID = nextConfigID;
    }

    /**
	 * Store this instance of CameraRegistry to its current
	 * regitryDir.
	 */
    public void store() {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
                saveRegistry(this, this.getClass().getClassLoader());
            } finally {
                Thread.currentThread().setContextClassLoader(cl);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static CameraRegistry sharedInstance;

    /**
	 * Saves the given registry.
	 *  
	 * @throws IOException
	 */
    private static void saveRegistry(CameraRegistry registry, ClassLoader classLoader) throws IOException {
        File file = registry.confFile.getCanonicalFile();
        System.out.println("Saving CamRegistry to " + registry.confFile.getCanonicalFile());
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            if (!file.createNewFile()) {
                throw new RuntimeException("Registry confFile " + registry.confFile + " does not exist and can not be created.");
            }
        }
        FileOutputStream fout = new FileOutputStream(file);
        try {
            XMLEncoder e = new XMLEncoder(fout);
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(classLoader);
                try {
                    e.writeObject(registry);
                } finally {
                    if (e != null) e.close();
                }
            } finally {
                Thread.currentThread().setContextClassLoader(cl);
            }
        } finally {
            if (fout != null) fout.close();
        }
    }

    /**
	 * Loads the Registry from the given file or creates a
	 * empty one if the file is not existent.
	 *  
	 * @throws IOException
	 */
    private static CameraRegistry loadRegistry(File confFile, URL portConfURL, ClassLoader classLoader) throws IOException {
        CameraRegistry registry = null;
        if (!confFile.exists()) {
            registry = new CameraRegistry(true, portConfURL);
            registry.cameraConfigs = new HashMap();
            registry.nextConfigID = 0;
            registry.confFile = confFile;
            return registry;
        }
        FileInputStream fin = new FileInputStream(confFile);
        try {
            XMLDecoder d = new XMLDecoder(fin);
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(classLoader);
                try {
                    registry = (CameraRegistry) d.readObject();
                } finally {
                    if (d != null) d.close();
                }
            } finally {
                Thread.currentThread().setContextClassLoader(cl);
            }
        } finally {
            if (fin != null) fin.close();
        }
        if (registry != null) registry.confFile = confFile;
        if (registry != null) registry.createCamPort(portConfURL);
        if (registry != null) registry.validate();
        return registry;
    }

    /**
	 * Used after deserialization to assing itself to all contained
	 * Configurations
	 */
    protected void validate() {
        for (Iterator iter = cameraConfigs.values().iterator(); iter.hasNext(); ) {
            CameraConfig config = (CameraConfig) iter.next();
            config.setRegsitry(this);
        }
    }

    /**
	 * @return Returns the sharedInstance.
	 */
    public static CameraRegistry sharedInstance(String registryDir, URL portConfURL) {
        if (sharedInstance == null) {
            File tmpDir = new File(registryDir);
            if (!tmpDir.exists()) {
                try {
                    if (!tmpDir.createNewFile()) throw new RuntimeException("Given registryDir " + registryDir + " does not exists and could not be created.");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            File confFile = new File(tmpDir, CAMERA_CONFIG_FILENAME);
            try {
                sharedInstance = loadRegistry(confFile, portConfURL, CameraRegistry.class.getClassLoader());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (sharedInstance == null) throw new IllegalStateException("Could not create an instance of CameraRegistry");
        }
        return sharedInstance;
    }

    /**
	 * Shortcut to {@link #getSharedInstance(String)} passing null as registryDir.
	 * Use this only if you can be sure the shared instance was created prior. This 
	 * will return null if the shared instance has not been created already. 
	 * 
	 * @return The static instance of CameraRegistry or null.
	 */
    public static CameraRegistry sharedInstance() {
        if (sharedInstance == null) return null;
        return sharedInstance(null, null);
    }
}
