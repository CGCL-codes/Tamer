package org.vrforcad.model.scene;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.PointLight;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3f;
import org.vrforcad.lib.lights.GenericLight;

/**
 * A point light in scene.
 *  
 * @version 1.0 
 * @author Daniel Cioi <dan.cioi@vrforcad.org>
 */
public class PointLightInScene implements GenericLight {

    private BoundingSphere bounds;

    private BranchGroup bg;

    private String name = "No name";

    private Color3f lightColor;

    private PointLight light;

    private Point3f position;

    private Point3f attenuation;

    /**
	 * Default constructor.
	 * @param bg
	 * @param bounds
	 */
    public PointLightInScene(BranchGroup bg, BoundingSphere bounds) {
        this.bounds = bounds;
        this.bg = bg;
        initialize();
    }

    /**
	 * This method initialize the light.
	 */
    private void initialize() {
        lightColor = new Color3f(1f, 1f, 1f);
        position = new Point3f(5f, 1f, 5f);
        attenuation = new Point3f(1f, 0f, 0f);
        light = new PointLight(lightColor, position, attenuation);
        light.setCapability(PointLight.ALLOW_COLOR_READ);
        light.setCapability(PointLight.ALLOW_COLOR_WRITE);
        light.setCapability(PointLight.ALLOW_STATE_READ);
        light.setCapability(PointLight.ALLOW_STATE_WRITE);
        light.setCapability(PointLight.ALLOW_POSITION_READ);
        light.setCapability(PointLight.ALLOW_POSITION_WRITE);
        light.setCapability(PointLight.ALLOW_ATTENUATION_READ);
        light.setCapability(PointLight.ALLOW_ATTENUATION_WRITE);
        light.setInfluencingBounds(bounds);
        BranchGroup bgLight = new BranchGroup();
        bgLight.setCapability(BranchGroup.ALLOW_DETACH);
        bgLight.addChild(light);
        bg.addChild(bgLight);
    }

    /**
	 * This method sets a new light's color. 
	 * @param color
	 */
    public void setColor(Color3f color) {
        lightColor = color;
        light.setColor(lightColor);
    }

    /**
	 * This method get the current light's color.
	 * @return current light's color
	 */
    public Color3f getColor() {
        return lightColor;
    }

    /**
	 * This method sets a new position.
	 * @param newPosition
	 */
    public void setPosition(Point3f newPosition) {
        position = newPosition;
        light.setPosition(position);
    }

    /**
	 * This method get the current position.
	 * @return current position
	 */
    public Point3f getPosition() {
        return position;
    }

    /**
	 * This method sets new attenuation parameters. 
	 * @param newAttenuation
	 */
    public void setAttenuation(Point3f newAttenuation) {
        attenuation = newAttenuation;
        light.setAttenuation(attenuation);
    }

    public Point3f getAttenuation() {
        return attenuation;
    }

    /**
	 * Method to set a name for this object.
	 * @param n
	 */
    public void setName(String n) {
        name = n;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void setEnable(Boolean enable) {
        light.setEnable(enable);
    }

    @Override
    public int getLightType() {
        return GenericLight.LIGHT_TYPE_POINT;
    }

    @Override
    public Tuple3f[] getTuple() {
        Tuple3f[] tuples = new Tuple3f[3];
        tuples[0] = getColor();
        tuples[1] = getPosition();
        tuples[2] = getAttenuation();
        return tuples;
    }

    @Override
    public void setTuple(Tuple3f[] tuples) {
        if (tuples.length == 3) {
            setColor(new Color3f(tuples[0]));
            setPosition(new Point3f(tuples[1]));
            setAttenuation(new Point3f(tuples[2]));
        }
    }
}
