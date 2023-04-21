package it.tukano.jps.engines.java3d15;

import com.sun.j3d.utils.universe.SimpleUniverse;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.Canvas3D;

/**
 * This kills the j3d thread using Thread.stop.
 */
class J3DBadDisposer {

    /**
     * Default no arg constructor
     */
    public J3DBadDisposer() {
    }

    void badJava3DKill(final Canvas3D canvas, final SimpleUniverse universe) {
        canvas.stopRenderer();
        universe.removeAllLocales();
        universe.cleanup();
        final Thread killer = new Thread("Java3D renderer killer...") {

            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Java3D15Engine.class.getName()).log(Level.SEVERE, "Killer Thread interrupted", ex);
                    return;
                }
                java.util.logging.Logger.getLogger(Java3D15Engine.class.getName()).log(java.util.logging.Level.INFO, "Destroying Java3D Engine");
                for (Thread t : new J3DThreadFinder().findJava3DThreads()) {
                    try {
                        t.stop();
                    } catch (Throwable ex) {
                        Logger.getLogger(Java3D15Engine.class.getName()).log(Level.INFO, "", ex);
                    }
                }
            }
        };
        killer.setDaemon(true);
        killer.start();
    }
}
