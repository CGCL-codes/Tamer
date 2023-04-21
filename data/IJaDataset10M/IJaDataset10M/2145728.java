package org.xith3d.demos.utils.phong;

import org.xith3d.scenegraph.*;
import org.openmali.vecmath2.*;

/**
 * @author Abdul Bezrati (aka JavaCoolDude)
 */
public class JCDModel extends GeomGroup {

    public TransformGroup getModel(String filename, String basemap, String normalmap, String glossmap, Tuple3f specular, Tuple3f ambient, float scale, int shininess) throws Exception {
        parent.setTransform(transform);
        parent.addChild(ShapeFactory.getTexturedShape(JCD3DSFactory.readJCDModel(filename, scale), basemap, normalmap, glossmap, specular.getX(), specular.getY(), specular.getZ(), ambient.getX(), ambient.getY(), ambient.getZ(), shininess));
        return (parent);
    }
}
