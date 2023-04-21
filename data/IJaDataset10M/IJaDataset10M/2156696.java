package com.grt192.sensor;

import com.grt192.core.Sensor;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.Image;
import edu.wpi.first.wpilibj.image.NIVisionException;

/**
 *
 * @author anand
 */
public class GRTAxisCamera extends Sensor {

    private AxisCamera camera;

    public GRTAxisCamera(String id) {
        camera = AxisCamera.getInstance();
        init();
    }

    private void init() {
        if (camera != null) {
            camera.writeResolution(AxisCamera.ResolutionT.k320x240);
            camera.writeBrightness(0);
        }
    }

    public void poll() {
        if (camera == null) {
            camera = AxisCamera.getInstance();
            init();
        } else {
            setState("newImage", camera.freshImage());
            if (camera.freshImage()) {
                try {
                    if (state.get("image") != null) {
                        ((Image) state.get("image")).free();
                    }
                    state.put("image", camera.getImage());
                } catch (AxisCameraException ex) {
                    state.put("image", null);
                    ex.printStackTrace();
                } catch (NIVisionException ex) {
                    state.put("image", null);
                    ex.printStackTrace();
                }
            }
        }
    }
}
