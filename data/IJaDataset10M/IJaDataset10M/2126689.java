package octlight.renderer;

import octlight.base.Color;
import octlight.scene.Node;
import octlight.scene.light.AmbientLight;
import octlight.scene.light.Light;
import octlight.util.ArrayUtil;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author $Author: creator $
 * @version $Revision: 1.3 $
 */
public class LightList {

    private ArrayList<Light> lights = new ArrayList<Light>();

    private Light[] currentLights;

    private boolean[] lightEnabled;

    private Light[] newCurrentLights;

    private boolean[] newLightEnabled;

    private Light[] newLights;

    private LightHandler lightHandler;

    private Color ambientLight = new Color();

    private boolean ambientChanged;

    public LightList(int maxSimultaneousLights, LightHandler lightHandler) {
        currentLights = new Light[maxSimultaneousLights];
        lightEnabled = new boolean[maxSimultaneousLights];
        newCurrentLights = new Light[maxSimultaneousLights];
        newLightEnabled = new boolean[maxSimultaneousLights];
        newLights = new Light[maxSimultaneousLights];
        this.lightHandler = lightHandler;
    }

    public void clear() {
        for (int i = 0; i < currentLights.length; i++) {
            currentLights[i] = null;
            lightEnabled[i] = false;
        }
        lights.clear();
    }

    public void addLight(Light light) {
        if (light instanceof AmbientLight) {
            ambientLight = ambientLight.add(light.getColor());
            ambientChanged = true;
        } else lights.add(light);
    }

    public void removeLight(Light light) {
        if (light instanceof AmbientLight) {
            ambientLight = ambientLight.sub(light.getColor());
            ambientChanged = true;
        } else lights.remove(light);
    }

    private void getLightsFor(Node node, int count) {
        for (int i = 0; i < count; i++) newLights[i] = lights.get(i);
    }

    public void updateLightsFor(Node node) {
        int count = Math.min(currentLights.length, lights.size());
        getLightsFor(node, count);
        Arrays.fill(newLightEnabled, false);
        for (int i = 0; i < currentLights.length; i++) {
            int index = ArrayUtil.indexOf(newLights, currentLights[i], 0, count);
            newLightEnabled[i] = index != -1;
            if (index != -1) {
                newCurrentLights[i] = currentLights[i];
                newLights[index] = null;
            }
        }
        int index = 0;
        for (int i = 0; i < count; i++) {
            if (newLights[i] != null) {
                while (newLightEnabled[index]) index++;
                newCurrentLights[index] = newLights[i];
                newLightEnabled[index++] = true;
            }
        }
        for (int i = 0; i < newLightEnabled.length; i++) {
            if (newCurrentLights[i] != currentLights[i]) lightHandler.setLight(i, newCurrentLights[i]);
            if (lightEnabled[i] != newLightEnabled[i]) lightHandler.enableLight(i, newLightEnabled[i]);
        }
        {
            boolean[] t = lightEnabled;
            lightEnabled = newLightEnabled;
            newLightEnabled = t;
        }
        {
            Light[] t = currentLights;
            currentLights = newCurrentLights;
            newCurrentLights = t;
        }
        if (ambientChanged) {
            lightHandler.setAmbientLight(ambientLight);
            ambientChanged = false;
        }
    }
}
