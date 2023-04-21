package fr.scenerenderer;

import java.util.List;
import fr.scenerenderer.renderengine.*;
import fr.scenerenderer.gui.*;
import fr.scenerenderer.objects3d.lights.*;
import fr.scenerenderer.objects3d.shapes3d.*;
import fr.scenerenderer.geometry.*;
import fr.scenerenderer.objects3d.shapes3d.materials.*;

public class SceneRenderer {

    public static void main(final String[] args) {
        RenderController renderController = RenderController.getInstance();
        Scene scene = new Scene(1.0f, RGBColor.BLACK);
        scene.getCameras().add(new Camera(new Point3D(-300, 0, 0), Point3D.ORIGIN, Vector3D.Z, 250, 640, 480));
        scene.getLights().add(new PointLight(new Point3D(0, 0, 300), RGBColor.WHITE, 100));
        scene.getShapes3D().add(new Sphere(new Point3D(0, 0, 0), 100, new Material(RGBColor.RED)));
        scene.getShapes3D().add(new Sphere(new Point3D(0, -200, -50), 80, new Material(RGBColor.GREEN, 0.7f, 0.3f, 0, 1)));
        renderController.setScene(scene);
        renderController.setCurrentCamera(scene.getCameras().get(0));
        renderController.setRenderEngine(new BasicRayTracingRenderEngine());
        renderController.setView(new SceneRendererView());
    }
}
