package emulator.GUI;

import java.util.HashSet;

public class FrameRegistry {

    private HashSet<PersistentFrame> frame_registry = new HashSet<PersistentFrame>();

    private static FrameRegistry instance = new FrameRegistry();

    public static FrameRegistry getInstance() {
        return instance;
    }

    public void registerFrame(PersistentFrame frame) {
        frame_registry.add(frame);
    }

    public void unregisterFrame(PersistentFrame frame) {
        frame_registry.remove(frame);
    }

    public void saveFrameStates() {
        for (PersistentFrame frame : frame_registry) frame.savePosition();
    }
}
