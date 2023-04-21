package edu.ua.j3dengine.core.geometry.impl;

import edu.ua.j3dengine.core.geometry.Geometry;
import edu.ua.j3dengine.core.geometry.BaseGeometry;
import javax.vecmath.Tuple3f;
import org.xith3d.scenegraph.View;

public class CameraGeometry extends BaseGeometry implements Geometry {

    private View view;

    private CameraGeometry() {
    }

    public CameraGeometry(View view) {
        this.view = view;
    }

    public Tuple3f getLocation() {
        return view.getPosition();
    }

    public Tuple3f getDirection() {
        return view.getFacingDirection();
    }

    public View getView() {
        return view;
    }
}
