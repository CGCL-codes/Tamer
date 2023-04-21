package edu.gsbme.yakitori.Renderer.Controller.Reference.Cells;

import java.util.ArrayList;

/**
 * Surface interpolation reference data structure
 * @author David
 *
 */
public class SurfaceInterpolationRef extends InterpolationRef {

    public ArrayList<ObjReference> surface_rendering;

    public SurfaceInterpolationRef(String id, String class_type) {
        super(id, class_type);
    }
}
