package org.viewer.viewplugin;

import java.io.FileNotFoundException;
import java.net.URL;
import javax.media.j3d.Appearance;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.ImageIcon;
import org.jdesktop.j3d.loaders.vrml97.VrmlLoader;
import org.viewer.control.ViewerMain;
import org.viewer.view.Colours;
import org.viewer.view.NodeView;
import org.viewer.view.View2D;
import com.sun.j3d.loaders.Scene;

/**

 * A box shaped node. Labels are texture mapped onto the face.

 *

 * @author Tim Dwyer

 * @version 1.0

 */
public class StarNodeView extends NodeView implements View2D {

    public StarNodeView() {
        setTypeName("Star Node");
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
        String fileName = "star.WRL";
        VrmlLoader l = new VrmlLoader();
        Scene s = null;
        URL url = null;
        Transform3D scale = new Transform3D();
        scale.setScale(0.2);
        TransformGroup objTrans = new TransformGroup(scale);
        try {
            url = org.viewer.images.Images.class.getResource(fileName);
            if (url == null) throw new FileNotFoundException();
            s = l.load(url);
            Shape3D shape = (Shape3D) s.getNamedObjects().get("Star01_SHAPE");
            objTrans.addChild(shape);
            addTransformGroupChild(objTrans);
            makePickable(shape);
        } catch (FileNotFoundException e) {
            ViewerMain.showErrorDialog("VRML File: " + url.getPath() + ", not found when creating node view", e);
        }
    }

    public ImageIcon getIcon() {
        return new ImageIcon(org.viewer.images.Images.class.getResource("starnode.png"));
    }
}
