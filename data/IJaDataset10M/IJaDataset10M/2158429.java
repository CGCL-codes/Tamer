package redrocket.jump.test.vt02;

import redrocket.jump.PhysData;
import redrocket.jump.Simulation;
import redrocket.jump.test.VtBase;
import redrocket.util.RedRocketConstants;
import xmage.math.Point3d;
import xmage.raster.Image;
import xmage.raster.codec.TGAInputCodec;
import xmage.turbine.Camera;
import xmage.turbine.shape.Cube;
import xmage.turbine.DynamicTriMesh;
import xmage.turbine.Material;
import xmage.turbine.Node;
import xmage.turbine.Skin;
import xmage.turbine.Texture;
import xmage.turbine.TriMesh;
import java.io.IOException;

public class Vt02BoxOnSlopeSlidingByTheWall extends VtBase {

    private Vt02BoxOnSlopeSlidingByTheWall() throws IOException {
        Node root = new Node();
        DynamicTriMesh floor = new DynamicTriMesh();
        floor.addVertex(0.0, -1.0, 5.0, (float) 0.0, (float) 0.0);
        floor.addVertex(0.0, -1.0, -5.0, (float) 1.0, (float) 0.0);
        floor.addVertex(0.0, 1.0, -5.0, (float) 1.0, (float) 1.0);
        floor.addVertex(0.0, 1.0, 5.0, (float) 0.0, (float) 1.0);
        floor.addTriangle(0, 1, 2);
        floor.addTriangle(0, 2, 3);
        floor.addVertex(5.0, -1.0, 5.0, (float) 0.0, (float) 0.0);
        floor.addVertex(5.0, 1.0, -5.0, (float) 1.0, (float) 0.0);
        floor.addVertex(0.0, 1.0, -5.0, (float) 1.0, (float) 1.0);
        floor.addVertex(0.0, -1.0, 5.0, (float) 0.0, (float) 1.0);
        floor.addTriangle(4, 5, 6);
        floor.addTriangle(4, 6, 7);
        floor.triangulate();
        floor.aux[RedRocketConstants.PHYS_AUX_INDEX] = new PhysData(PhysData.Type.STATIC);
        root.add(floor);
        TriMesh actor = new Cube(1.0, 2.0, 1.0).triangulate();
        actor.aux[RedRocketConstants.PHYS_AUX_INDEX] = new PhysData(PhysData.Type.DYNAMIC);
        root.add(actor);
        Image img1 = new TGAInputCodec().read("img/checkerboard_bw.tga");
        floor.setSkin(new Skin(new Material(new Texture(img1))));
        Image img2 = new TGAInputCodec().read("img/flood_yellow.tga");
        actor.setSkin(new Skin(new Material(new Texture(img2))));
        getCanvas().getRenderer().setRoot(root);
        final Camera camera = getCanvas().getRenderer().getCamera();
        camera.setPosition(5.0, 15.0, 10.0);
        camera.setTarget(0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        camera.setNear(1.0);
        camera.setFar(100.0);
        show();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        Point3d startPos = new Point3d(2.0, 7.0, 2.0);
        Point3d step = new Point3d(-1.5, 0.0, -1.5);
        Simulation simulation = new Simulation(root);
        int steps = 0;
        actor.setPosition(startPos);
        while (true) {
            ((PhysData) actor.aux[RedRocketConstants.PHYS_AUX_INDEX]).setMovement(step);
            simulation.update();
            steps++;
            if (steps == 70) {
                steps = 0;
                actor.setPosition(startPos);
            }
            getCanvas().getAsComponent().repaint();
            try {
                Thread.sleep(50);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Vt02BoxOnSlopeSlidingByTheWall();
    }
}
