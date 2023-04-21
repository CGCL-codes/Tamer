package marten.age.graphics.root;

import java.util.ArrayList;
import java.util.HashMap;
import marten.age.graphics.camera.Camera;
import marten.age.graphics.light.Light;
import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class AveragelyComplexRoot extends Root {

    private static org.apache.log4j.Logger log = Logger.getLogger(AveragelyComplexRoot.class);

    private HashMap<String, Camera> cameras = new HashMap<String, Camera>();

    private ArrayList<Light> lights = new ArrayList<Light>();

    private String activeCamera = null;

    private int[] lightNumbers = { GL11.GL_LIGHT0, GL11.GL_LIGHT1, GL11.GL_LIGHT2, GL11.GL_LIGHT3, GL11.GL_LIGHT4, GL11.GL_LIGHT5, GL11.GL_LIGHT6, GL11.GL_LIGHT7 };

    public AveragelyComplexRoot() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_NORMALIZE);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        log.info("GL_VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
        log.info("GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
        log.info("GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));
    }

    public void addCamera(String title, Camera newCamera) {
        cameras.put(title, newCamera);
    }

    public void removeCamera(String title) {
        cameras.remove(title);
    }

    public void setActiveCamera(String title) {
        if (!cameras.containsKey(title)) throw new RuntimeException("Attempted to activate non-existant camera.");
        this.activeCamera = title;
    }

    public void addLight(Light newLight) {
        if (lights.size() == 8) throw new RuntimeException("Too many lights added (only 8 are supported).");
        lights.add(newLight);
    }

    public void removeLight(Light oldLight) {
        lights.remove(oldLight);
    }

    public void render() {
        if (this.cameras.isEmpty()) throw new RuntimeException("Attempted to activate the root without initialized cameras.");
        if (this.activeCamera == null) throw new RuntimeException("Attempted to activate the root without an active camera.");
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        for (int index = 0; index < lights.size(); index++) {
            lights.get(index).set(lightNumbers[index]);
            if (!GL11.glIsEnabled(lightNumbers[index])) {
                GL11.glEnable(lightNumbers[index]);
                log.debug("Light number " + lightNumbers[index] + " enabled.");
            }
        }
        for (int index = lights.size(); index < 8; index++) if (GL11.glIsEnabled(lightNumbers[index])) {
            GL11.glDisable(lightNumbers[index]);
            log.debug("Light number " + lightNumbers[index] + " disabled.");
        }
        this.cameras.get(this.activeCamera).set();
        super.render();
        GL11.glFlush();
    }
}
