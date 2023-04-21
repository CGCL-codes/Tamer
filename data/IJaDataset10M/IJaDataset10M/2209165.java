package com.jmex.awt.lwjgl;

import java.util.HashMap;
import org.lwjgl.LWJGLException;
import com.jme.system.JmeException;
import com.jme.system.canvas.CanvasConstructor;

public class LWJGLAWTCanvasConstructor implements CanvasConstructor {

    public LWJGLCanvas makeCanvas(HashMap<String, Object> props) {
        try {
            return new LWJGLCanvas();
        } catch (LWJGLException e) {
            e.printStackTrace();
            throw new JmeException("Unable to create lwjgl-awt canvas: " + e.getLocalizedMessage());
        }
    }
}
