package game.ecosysteme;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.PointArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Animation3DApplet extends Applet {

    public Animation3DApplet() {
        this.setLayout(new BorderLayout());
        Canvas3D canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        this.add(canvas3D, BorderLayout.CENTER);
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
        simpleU.getViewingPlatform().setNominalViewingTransform();
        BranchGroup scene = createSceneGraph();
        scene.compile();
        simpleU.addBranchGraph(scene);
    }

    /**
	 * Creation de la scene 3D qui contient tous les objets 3D
	 * 
	 * @return scene 3D
	 */
    public BranchGroup createSceneGraph() {
        BranchGroup parent = new BranchGroup();
        Point3f points[] = new Point3f[2];
        Color3f colors[] = new Color3f[2];
        points[0] = new Point3f(0, 0, 0);
        colors[0] = new Color3f(Color.yellow);
        points[1] = new Point3f(0.02f, 0.0f, 0);
        colors[1] = new Color3f(Color.red);
        Shape3D shape = new Shape3D();
        PointArray pointArray = new PointArray(2, PointArray.COORDINATES | PointArray.COLOR_3);
        pointArray.setCoordinates(0, points);
        pointArray.setColors(0, colors);
        shape.setGeometry(pointArray);
        parent.addChild(shape);
        return parent;
    }

    /**
	 * Etape 9 : Methode main() nous permettant d'utiliser cette classe comme
	 * une applet ou une application.
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        new MainFrame(new Animation3DApplet(), 256, 256);
    }
}
