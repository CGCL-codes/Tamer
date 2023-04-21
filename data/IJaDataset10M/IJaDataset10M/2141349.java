package org.wilmascope.view;

import java.util.Properties;
import javax.vecmath.Vector3f;

/**
 * Title:        WilmaToo
 * Description:  Sequel to the ever popular Wilma graph drawing engine
 * Copyright:    Copyright (c) 2001
 * Company:      WilmaOrg
 * @author Tim Dwyer
 * @version 1.0
 */
public class Constants extends org.wilmascope.global.AbstractConstants {

    public Constants(String fileName) {
        super(fileName);
    }

    protected Properties getDefaultProperties() {
        Properties d = new Properties();
        d.setProperty("FogDensity", "0.2");
        d.setProperty("FogColourR", "0.5");
        d.setProperty("FogColourG", "0.5");
        d.setProperty("FogColourB", "0.5");
        d.setProperty("BackgroundColourR", "0");
        d.setProperty("BackgroundColourG", "0");
        d.setProperty("BackgroundColourB", "0");
        d.setProperty("AmbientLightColourR", "1");
        d.setProperty("AmbientLightColourG", "1");
        d.setProperty("AmbientLightColourB", "1");
        d.setProperty("DirectionalLightVectorX", "-1");
        d.setProperty("DirectionalLightVectorY", "-1");
        d.setProperty("DirectionalLightVectorZ", "-1");
        d.setProperty("DirectionalLightColourR", "1");
        d.setProperty("DirectionalLightColourG", "1");
        d.setProperty("DirectionalLightColourB", "1");
        return d;
    }

    public static org.wilmascope.global.Constants gc = org.wilmascope.global.Constants.getInstance();

    public static final Vector3f vX = gc.vX;

    public static final Vector3f vY = gc.vY;

    public static final Vector3f vZ = gc.vZ;

    public static final Vector3f vZero = gc.vZero;
}
