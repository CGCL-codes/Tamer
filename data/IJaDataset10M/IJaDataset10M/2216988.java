package org.viewer.viewplugin;

import org.viewer.view.*;
import com.sun.j3d.utils.geometry.Text2D;
import javax.media.j3d.*;
import javax.vecmath.Point3d;
import java.awt.Font;
import javax.swing.ImageIcon;
import com.sun.j3d.utils.geometry.Sphere;

/**

 * A box that always faces the viewer.  Labels are texture mapped onto the face.

 *

 * @author Tim Dwyer

 * @version 1.0

 */
public class OrientedBoxNodeView extends NodeView {

    public OrientedBoxNodeView() {
        setTypeName("Oriented Box Node");
    }

    protected void setupDefaultMaterial() {
        setupDefaultAppearance(Colours.defaultMaterial);
        getAppearance().setCapability(Appearance.ALLOW_TEXTURE_WRITE);
        getAppearance().setCapability(Appearance.ALLOW_TEXTURE_ATTRIBUTES_WRITE);
    }

    protected void setupHighlightMaterial() {
        setupHighlightAppearance(Colours.yellowMaterial);
    }

    protected void init() {
        float radius = getRadius();
        box = new OrientedLabelCube(getAppearance(), 1.0f, 1.0f);
        makePickable(box);
        addTransformGroupChild(box);
        Appearance app = new Appearance();
        app.setMaterial(getAppearance().getMaterial());
        border = new OrientedNoLabelCube(app, 1.0f, 1.0f);
        makePickable(border);
        addTransformGroupChild(border);
        Appearance transApp = new Appearance();
        transApp.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.99f));
        pickableSphere = new Sphere(2 * radius, Sphere.GENERATE_NORMALS, 5, transApp);
        makePickable(pickableSphere.getShape(Sphere.BODY));
        addTransformGroupChild(pickableSphere);
    }

    public void showLabel(String label) {
        Appearance appearance = getAppearance();
        Text2D textObject = new Text2D(label, org.viewer.view.Colours.black, "Arial", 55, Font.PLAIN);
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.DECAL);
        appearance.setTextureAttributes(texAttr);
        appearance.setTexture(textObject.getAppearance().getTexture());
        Point3d coord = new Point3d();
        QuadArray qa = (QuadArray) textObject.getGeometry();
        qa.getCoordinate(0, coord);
        float widthScale;
        if (coord.x > 2.1d) {
            widthScale = 3.0f;
        } else if (coord.x < 1.0d) {
            widthScale = 1.0f;
        } else {
            widthScale = (float) coord.x;
        }
        box.generateGeometry(1f, widthScale);
        border.generateGeometry(1f, widthScale);
    }

    public ImageIcon getIcon() {
        return new ImageIcon(org.viewer.images.Images.class.getResource("cube.png"));
    }

    OrientedLabelCube box;

    OrientedNoLabelCube border;

    Sphere pickableSphere;
}
