package moteur.scene3d;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import outils.divers.Coordonnees3d;
import application.config.Env;
import com.microcrowd.loader.java3d.max3ds.Loader3DS;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.geometry.ColorCube;

public class Objet3D extends BranchGroup {

    public Objet3D() {
        super();
    }

    protected void ajouterObjet3D(BranchGroup modelScene, Coordonnees3d pos, float echelle) {
        Transform3D scaleEchelle = new Transform3D();
        scaleEchelle.setScale(echelle);
        Transform3D position = new Transform3D();
        position.setTranslation(new Vector3f(pos.getX(), pos.getY(), pos.getZ()));
        position.mul(scaleEchelle);
        TransformGroup TG1 = new TransformGroup(position);
        TG1.addChild(modelScene);
        this.addChild(TG1);
    }

    protected void creerObjet3D(Coordonnees3d pos, float echelle) {
        BranchGroup modelScene = new BranchGroup();
        ColorCube cube = new ColorCube(1.0);
        modelScene.addChild(cube);
        ajouterObjet3D(modelScene, pos, echelle);
    }

    protected void creerObjet3D(String fichier, Coordonnees3d pos, float echelle) {
        try {
            Object loader = null;
            Scene scene = null;
            String type = getTypeFichier3d(fichier);
            if (type.equalsIgnoreCase("obj")) {
                loader = new ObjectFile();
                scene = ((ObjectFile) loader).load(Env.rep_images_etres_modeles3d + File.separator + fichier);
            } else if (type.equalsIgnoreCase("3ds")) {
                loader = new Loader3DS();
                scene = ((Loader3DS) loader).load(Env.rep_images_etres_modeles3d + File.separator + fichier);
            }
            if (scene != null) {
                ajouterObjet3D(scene.getSceneGroup(), pos, echelle);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IncorrectFormatException e) {
            e.printStackTrace();
        } catch (ParsingErrorException e) {
            e.printStackTrace();
        }
    }

    private BoundingBox getBounds(Node node) {
        BoundingBox objectBounds = new BoundingBox(new Point3d(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), new Point3d(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
        computeBounds(node, objectBounds);
        return objectBounds;
    }

    private void computeBounds(Node node, BoundingBox bounds) {
        if (node instanceof Group) {
            Enumeration<?> enumeration = ((Group) node).getAllChildren();
            while (enumeration.hasMoreElements()) {
                computeBounds((Node) enumeration.nextElement(), bounds);
            }
        } else if (node instanceof Shape3D) {
            Bounds shapeBounds = ((Shape3D) node).getBounds();
            bounds.combine(shapeBounds);
        }
    }

    private String getTypeFichier3d(String fichier) {
        String sub = fichier.substring(fichier.indexOf('.') + 1);
        return sub;
    }
}
