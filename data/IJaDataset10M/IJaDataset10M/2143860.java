package ie.ucd.nexus.service;

import ie.ucd.nexus.core.NamedTransformGroup;
import ie.ucd.nexus.core.Shell;
import ie.ucd.nexus.core.WorldWindow;
import java.util.Vector;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import com.agentfactory.platform.core.Agent;
import com.agentfactory.platform.service.PlatformService;
import com.agentfactory.platform.service.PlatformServiceDescriptor;
import com.agentfactory.platform.service.PlatformServiceManager;

public class WorldService extends PlatformService {

    /** A static instance of a WorldWindow shared by agents bound
     *  to this service
     */
    private static WorldWindow worldWindow;

    /** A Vector containing all Shell objects present in this NeXuS scenario */
    private static Vector<Shell> globalShells;

    /**
     *
     * @param descriptor
     * @param manager
     */
    @Override
    public void init(PlatformServiceDescriptor descriptor, PlatformServiceManager manager) {
        super.init(descriptor, manager);
        globalShells = new Vector<Shell>();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onBind(Agent agent) {
    }

    @Override
    public void onUnbind(Agent agent) {
    }

    @Override
    public void onStop() {
    }

    public synchronized void startWorldWindow(String cameraParaPath) {
        if (worldWindow == null) {
            worldWindow = new WorldWindow(cameraParaPath);
            worldWindow.setVisible(true);
        }
    }

    public synchronized void addShell(Shell aShell) throws Exception {
        String shellName = aShell.getName();
        System.out.println("WorldService: Attempting To Add Shell " + shellName);
        if (!shellIsPresent(shellName)) {
            System.out.println("WorldService: " + shellName + " Not Already " + "Present, Proceeding To Add");
            globalShells.add(aShell);
            String markerPattern = aShell.getMarkerPath();
            NamedTransformGroup transGroup = (NamedTransformGroup) aShell.getTransformGroup();
            transGroup.setName(shellName);
            if (!markerPattern.equals("")) {
                worldWindow.addTransformGroup(transGroup, markerPattern, aShell.transformKept());
            } else {
                worldWindow.addNamedTransformGroup(transGroup);
            }
            updateWorld();
            System.out.println("WorldService: Added Shell " + shellName);
        } else {
            System.out.println("What the hell is going on with " + shellName);
        }
    }

    public synchronized void setShell(Shell aShell) throws Exception {
        String shellName = aShell.getName();
        Vector<Shell> allShells = getGlobalShells();
        for (int i = 0; i < allShells.size(); i++) {
            Shell temp = allShells.get(i);
            if (shellName.equals(temp.getName())) {
                allShells.set(i, aShell);
                String markerPattern = aShell.getMarkerPath();
                if (!markerPattern.equals(temp.getMarkerPath())) {
                    worldWindow.removeTransformGroup(shellName);
                    NamedTransformGroup transGroup = (NamedTransformGroup) aShell.getTransformGroup();
                    transGroup.setName(shellName);
                    worldWindow.addTransformGroup(transGroup, markerPattern, aShell.transformKept());
                }
                setGlobalShells(allShells);
                return;
            }
        }
        throw new Exception("Shell " + shellName + " does not exist");
    }

    public synchronized void removeShell(Shell aShell) throws Exception {
        String shellName = aShell.getName();
        Vector<Shell> allShells = getGlobalShells();
        for (int i = 0; i < allShells.size(); i++) {
            Shell temp = allShells.get(i);
            if (shellName.equals(temp.getName())) {
                allShells.remove(i);
                worldWindow.removeTransformGroup(shellName);
                setGlobalShells(allShells);
                return;
            }
        }
        throw new Exception("Shell " + shellName + " does not exist");
    }

    public Vector<String> getWorldStateBeliefs() throws Exception {
        updateWorld();
        Vector<String> worldBeliefs = new Vector<String>();
        Vector<Shell> allShells = getGlobalShells();
        for (int i = 0; i < allShells.size(); i++) {
            Shell aShell = allShells.get(i);
            String shellName = aShell.getName();
            worldBeliefs.add("BELIEF(inWorld(" + shellName + "))");
            Matrix4f aMatrix = aShell.getLocation();
            worldBeliefs.add("BELIEF(shellPosition(" + shellName + ",))");
        }
        return worldBeliefs;
    }

    /**
     * Checks if a Shell with a given name is already present within the
     * NeXuS scenario world
     * @param shellName String
     * @return boolean
     */
    private boolean shellIsPresent(String shellName) throws Exception {
        boolean isPresent = false;
        Vector<Shell> allShells = getGlobalShells();
        if (!allShells.isEmpty()) {
            for (int i = 0; i < allShells.size(); i++) {
                Shell aShell = allShells.get(i);
                System.out.println("WorldService: " + aShell.getName() + " is in allShells Vector");
                if (shellName.equals(aShell.getName())) {
                    isPresent = true;
                }
            }
        }
        return isPresent;
    }

    /**
     * Returns a Vector containing all Shell objects currently in the NeXuS
     * scenario world
     * @return Vector<Shell>
     */
    public synchronized Vector<Shell> getGlobalShells() throws Exception {
        updateWorld();
        return globalShells;
    }

    /**
     * Returns a Shell of a given name from the Vector containing all Shell
     * objects currently in the NeXuS scenario world
     * @param shellName String
     * @return Shell
     * @throws java.lang.Exception
     */
    private Shell getShell(String shellName) throws Exception {
        Vector<Shell> allShells = getGlobalShells();
        for (int i = 0; i < allShells.size(); i++) {
            Shell aShell = allShells.get(i);
            if (shellName.equals(aShell.getName())) {
                return aShell;
            }
        }
        throw new Exception("Shell " + shellName + " does not exist");
    }

    /**
     * Sets the Vector containing all Shell objects currently in the NeXuS
     * scenario world
     * @param shellVector
     */
    private synchronized void setGlobalShells(Vector<Shell> shellVector) throws Exception {
        globalShells = shellVector;
        updateWorld();
    }

    private synchronized void updateWorld() throws Exception {
        for (int i = 0; i < globalShells.size(); i++) {
            Shell aShell = globalShells.get(i);
            String shellName = aShell.getName();
            if (aShell.getMarkerPath().equals("")) {
                worldWindow.setMatrix(shellName, aShell.getLocation());
            } else {
                aShell.setLocation(worldWindow.getMatrix(shellName));
                globalShells.set(i, aShell);
            }
            Vector3f shellOffset = new Vector3f(aShell.getOffset());
            worldWindow.setOffset(shellName, shellOffset);
            Vector3f desiredOffset = new Vector3f(aShell.getDesiredOffset());
            worldWindow.setDesiredOffset(shellName, desiredOffset);
            NamedTransformGroup shellHook = new NamedTransformGroup();
            shellHook.setName(aShell.getHookName());
            worldWindow.setMarkerHook(shellName, shellHook);
        }
    }
}
