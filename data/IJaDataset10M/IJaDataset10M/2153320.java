package client.game.view;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import client.game.Game;
import com.jme.bounding.BoundingBox;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.util.resource.MultiFormatResourceLocator;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.model.md5.MD5Node;
import com.model.md5.importer.MD5Importer;

/**
 * clase utilizada para importar un personaje en formato MD5 Hereda de
 * IPersonaje y se definen en ella los m�todos para que el personaje pueda
 * correr, moverse y la propia carga del personaje.
 * 
 * @author kike
 * 
 */
public class PersonaMD5 extends IPersonaje {

    private static final long serialVersionUID = 1L;

    private MD5Node md5n;

    public PersonaMD5(Node p) {
        super(p);
    }

    protected void run(boolean running) {
        if (running) this.md5n.getController(0).setSpeed(maxVelocity); else this.md5n.getController(0).setSpeed(minVelocity);
    }

    public void mover(boolean state, boolean run, boolean forward) {
        if (forward) md5n.getController(0).setRepeatType(Controller.RT_WRAP); else md5n.getController(0).setRepeatType(Controller.RT_BACK);
        md5n.getController(0).setActive(state);
        this.run(run);
    }

    @Override
    public Node cargar() {
        try {
            ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, new SimpleResourceLocator(Game.class.getClassLoader().getResource(this.paquete)));
        } catch (URISyntaxException e1) {
        }
        this.overrideTextureKey();
        URL bodyMesh = PersonaMD5.class.getClassLoader().getResource(this.personaje);
        URL bodyAnim = PersonaMD5.class.getClassLoader().getResource(this.animaciones);
        try {
            MD5Importer.getInstance().load(bodyMesh, "ModelNode", bodyAnim, "BodyAnimation", Controller.RT_WRAP);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.md5n = (MD5Node) MD5Importer.getInstance().getMD5Node();
        md5n.setModelBound(new BoundingBox());
        md5n.updateModelBound();
        this.padre.attachChild(md5n);
        this.padre.updateGeometricState(1, true);
        padre.setLocalScale(12);
        MD5Importer.getInstance().cleanup();
        return md5n;
    }

    protected void overrideTextureKey() {
        try {
            MultiFormatResourceLocator locator = new MultiFormatResourceLocator(this.getClass().getClassLoader().getResource(this.paquete), new String[] { ".tga", ".bmp", ".png", ".jpg", ".texture", ".jme", ".tif" });
            ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_TEXTURE, locator);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearAll() {
        md5n.clearControllers();
        md5n.detachAllChildren();
    }
}
