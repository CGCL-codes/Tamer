package visgraph.gl.visTypes;

import visgraph.gl.utilities.GLAppearance;
import visgraph.gl.utilities.GLMaterial;
import vizz3d.common.interfaces.Color4f;

public class GLCylinder extends GLMetaphorType {

    public GLCylinder() {
    }

    @Override
    public GLMetaphorType createObject(float bottom, float top, int texture, Color4f col, GLAppearance gradient, int textorientation) {
        if (gradient != null) {
            app = gradient;
        } else {
            app = new GLAppearance();
            app.setColor(col);
            GLMaterial mat = new GLMaterial();
            mat.setAmbientColor(col);
            mat.setEmissiveColor(col);
            mat.setDiffuseColor(col);
            mat.setSpecularColor(col);
            mat.setShininess(100.0f);
            app.setHeight(top);
            app.setRadius(bottom);
            app.setTexture(texture, 1.0f, 1.0f);
            app.setMaterial(mat);
            app.setTextOrientation(textorientation);
        }
        return this;
    }
}
