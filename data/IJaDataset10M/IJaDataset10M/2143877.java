package imi.utils.instruments;

import java.util.Map;
import javolution.util.FastMap;
import org.jdesktop.mtgame.WorldManager;

/**
 * The default implementation of the instrumentation interface
 * @author Ronald E Dahlgren
 */
public class DefaultInstrumentation implements Instrumentation {

    /** The manager of the world.  **/
    private WorldManager worldManager = null;

    /** State mapping **/
    private Map<InstrumentedSubsystem, Boolean> enabledMapping = new FastMap<InstrumentedSubsystem, Boolean>();

    public DefaultInstrumentation(WorldManager wm) {
        worldManager = wm;
        worldManager.addUserData(Instrumentation.class, this);
        enabledMapping.put(InstrumentedSubsystem.SkeletonAnimationSystem, true);
        enabledMapping.put(InstrumentedSubsystem.PoseTransferToGPU, true);
        enabledMapping.put(InstrumentedSubsystem.Texturing, true);
        enabledMapping.put(InstrumentedSubsystem.VertexDeformation, true);
    }

    public boolean disableSubsytem(InstrumentedSubsystem system) {
        boolean result = false;
        switch(system) {
            case SkeletonAnimationSystem:
                result = disableAnimationSystem();
                break;
            case PoseTransferToGPU:
                result = disablePoseTransfer();
                break;
            case Texturing:
                result = disableTexturing();
                break;
            case VertexDeformation:
                result = disableVertexDeforming();
                break;
            default:
                break;
        }
        return result;
    }

    public boolean enableSubsystem(InstrumentedSubsystem system) {
        boolean result = false;
        switch(system) {
            case SkeletonAnimationSystem:
                result = enableAnimationSystem();
                break;
            case PoseTransferToGPU:
                result = enablePoseTransfer();
                break;
            case Texturing:
                result = enableTexturing();
                break;
            case VertexDeformation:
                result = enableVertexDeforming();
                break;
            default:
                break;
        }
        return result;
    }

    public boolean enableAllSubsystems() {
        return false;
    }

    public boolean disableAllSubsystems() {
        return false;
    }

    public boolean isSubsystemEnabled(InstrumentedSubsystem system) {
        Boolean result = enabledMapping.get(system);
        if (result != null) return result;
        return false;
    }

    private boolean disableAnimationSystem() {
        enabledMapping.put(InstrumentedSubsystem.SkeletonAnimationSystem, false);
        return true;
    }

    private boolean disablePoseTransfer() {
        enabledMapping.put(InstrumentedSubsystem.PoseTransferToGPU, false);
        return true;
    }

    private boolean disableTexturing() {
        return true;
    }

    private boolean disableVertexDeforming() {
        return true;
    }

    private boolean enableAnimationSystem() {
        enabledMapping.put(InstrumentedSubsystem.SkeletonAnimationSystem, true);
        return true;
    }

    private boolean enablePoseTransfer() {
        enabledMapping.put(InstrumentedSubsystem.PoseTransferToGPU, true);
        return true;
    }

    private boolean enableTexturing() {
        return true;
    }

    private boolean enableVertexDeforming() {
        return true;
    }
}
