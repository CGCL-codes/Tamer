package net.java.dev.joode.test.collision;

import java.util.Random;
import org.xith3d.render.config.CanvasConstructionInfo;
import net.java.dev.joode.Body;
import net.java.dev.joode.Mass;
import net.java.dev.joode.joint.JointContact;
import net.java.dev.joode.collision.CollisionListener;
import net.java.dev.joode.geom.*;
import net.java.dev.joode.space.Space;
import net.java.dev.joode.test.JOODETestDescription;
import net.java.dev.joode.test.TestingWorld;
import net.java.dev.joode.util.*;

@JOODETestDescription(fulltext = { "Tests collision between a trimesh and a sphere." }, authors = { "Arne Müller" })
public class TriMeshSphereCollisionTest extends TestingWorld {

    public TriMeshSphereCollisionTest(CanvasConstructionInfo canvasInfo) {
        super(true, true, canvasInfo);
        Geom mesh1 = createCompoundOfSnubDisicosidodecahedronAndDual(getSpace());
        Mass mass1 = Mass.createBox(0.01f, 2, 2, 2);
        Body body1 = new Body(getWorld(), mass1);
        body1.getPosition().setY(4);
        Random random = new Random();
        body1.getPosition().setX(2 * (random.nextFloat() - 0.5f));
        body1.getPosition().setZ(2 * (random.nextFloat() - 0.5f));
        Quaternion q = new Quaternion();
        q.setEuler((float) Math.PI, 0, 0);
        body1.setRotation(q);
        mesh1.setBody(body1);
        addMovingObject(body1);
        Geom sphere = new Sphere(getSpace(), 1.0f);
        sphere.getPosition().setY(0);
        sphere.getPosition().setX(0);
        getView().lookAt(0, 2, 10, 0, 2, 0, 0, 1, 0);
        this.getCollisionManager().addCollisionListener(new CollisionListener() {

            public void reset() {
            }

            public boolean allowCollision(JointContact cj) {
                System.out.println("collision = " + cj);
                return true;
            }

            ;
        });
    }

    @SuppressWarnings("unused")
    private static Geom createPyramid(Space space) {
        float sx = 2, sy = 2, sz = 2;
        Vector3[] vertices = new Vector3[] { new Vector3(0, 0, -sz), new Vector3(sx, 0, sz), new Vector3(-sx, 0, sz), new Vector3(0, sy, 0) };
        int[] indices = new int[] { 0, 1, 2, 0, 3, 1, 1, 3, 2, 2, 3, 0 };
        return new TriMesh(space, true, vertices, indices);
    }

    private static Geom createCompoundOfSnubDisicosidodecahedronAndDual(Space space) {
        Vector3[] vertices = new Vector3[] { new Vector3(0f, 0f, 1.064537f), new Vector3(0.6857815f, 0f, 0.8142133f), new Vector3(0.2972041f, 0.618034f, 0.8142133f), new Vector3(-0.1511059f, 0.668927f, 0.8142133f), new Vector3(-0.6683308f, 0.1537214f, 0.8142133f), new Vector3(-0.4281768f, -0.5356874f, 0.8142133f), new Vector3(0.2972041f, -0.618034f, 0.8142133f), new Vector3(0.8520131f, -0.5356874f, 0.3469184f), new Vector3(1.035695f, -0.1537214f, 0.1922097f), new Vector3(0.8520131f, 0.5356874f, 0.3469184f), new Vector3(0.3103144f, 1.f, 0.1922097f), new Vector3(-0.32544f, 0.8667604f, 0.5254092f), new Vector3(-0.3347882f, 0.286961f, 0.968922f), new Vector3(0.3905927f, 0.2046144f, 0.968922f), new Vector3(0.5114474f, 0.7717554f, 0.5254092f), new Vector3(-0.07826298f, 1.044112f, 0.1922097f), new Vector3(-0.7102553f, 0.713039f, 0.3469184f), new Vector3(-1.043798f, 0.0823466f, 0.1922097f), new Vector3(-0.9504093f, -0.331073f, 0.3469184f), new Vector3(-0.526573f, -0.904995f, 0.1922097f), new Vector3(0.12287f, -0.9176534f, 0.5254092f), new Vector3(-0.32544f, -0.8667604f, 0.5254092f), new Vector3(0.3103144f, -1.f, 0.1922097f), new Vector3(0.7102553f, -0.713039f, -0.3469184f), new Vector3(1.043798f, -0.0823466f, -0.1922097f), new Vector3(0.9329586f, 0.1773516f, 0.4810138f), new Vector3(0.6357544f, -0.4406824f, 0.7313378f), new Vector3(0.7009071f, -0.7953856f, 0.09659444f), new Vector3(0.8694638f, -0.381966f, -0.4810138f), new Vector3(0.9504093f, 0.331073f, -0.3469184f), new Vector3(0.526573f, 0.904995f, -0.1922097f), new Vector3(0.8613612f, 0.618034f, -0.09659444f), new Vector3(0.32544f, 0.8667604f, -0.5254092f), new Vector3(-0.3103144f, 1.f, -0.1922097f), new Vector3(-0.7009071f, 0.7953856f, -0.09659444f), new Vector3(-0.8694638f, 0.381966f, 0.4810138f), new Vector3(-0.7183579f, -0.286961f, 0.7313378f), new Vector3(-0.0433615f, -0.381966f, 0.9927041f), new Vector3(-0.12287f, 0.9176534f, -0.5254092f), new Vector3(-0.8520131f, 0.5356874f, -0.3469184f), new Vector3(-0.9329586f, -0.1773516f, -0.4810138f), new Vector3(-0.8613612f, -0.618034f, 0.09659444f), new Vector3(-1.035695f, 0.1537214f, -0.1922097f), new Vector3(-0.8520131f, -0.5356874f, -0.3469184f), new Vector3(-0.5114474f, -0.7717554f, -0.5254092f), new Vector3(0.07826298f, -1.044112f, -0.1922097f), new Vector3(-0.3103144f, -1.f, -0.1922097f), new Vector3(0.32544f, -0.8667604f, -0.5254092f), new Vector3(0.1511059f, -0.668927f, -0.8142133f), new Vector3(0.6683308f, -0.1537214f, -0.8142133f), new Vector3(0.7183579f, 0.286961f, -0.7313378f), new Vector3(0.3347882f, -0.286961f, -0.968922f), new Vector3(0.4281768f, 0.5356874f, -0.8142133f), new Vector3(0.0433615f, 0.381966f, -0.9927041f), new Vector3(-0.2972041f, 0.618034f, -0.8142133f), new Vector3(-0.6357544f, 0.4406824f, -0.7313378f), new Vector3(-0.6857815f, 0f, -0.8142133f), new Vector3(-0.3905927f, -0.2046144f, -0.968922f), new Vector3(-0.2972041f, -0.618034f, -0.8142133f), new Vector3(0f, 0f, -1.064537f), new Vector3(0.04038056f, 0.3557073f, 0.9261616f), new Vector3(0.8284406f, -0.1905479f, 0.5131386f), new Vector3(-0.8500719f, 0f, 0.5131386f), new Vector3(-0.07538082f, -0.6640202f, 0.7343898f), new Vector3(0.6123418f, 0.7660943f, 0.1551466f), new Vector3(-0.425036f, 0.8838594f, 0.1551466f), new Vector3(-0.6123418f, -0.7660943f, -0.1551466f), new Vector3(0.425036f, -0.8838594f, -0.1551466f), new Vector3(0.8500719f, 0f, -0.5131386f), new Vector3(0.07538082f, 0.6640202f, -0.7343898f), new Vector3(-0.8284406f, 0.1905479f, -0.5131386f), new Vector3(-0.04038056f, -0.3557073f, -0.9261616f) };
        int[] indices = new int[] { 60, 0, 2, 60, 2, 12, 60, 12, 13, 60, 13, 3, 60, 3, 0, 61, 1, 7, 61, 7, 25, 61, 25, 26, 61, 26, 8, 61, 8, 1, 62, 4, 17, 62, 17, 36, 62, 36, 35, 62, 35, 18, 62, 18, 4, 63, 5, 20, 63, 20, 37, 63, 37, 21, 63, 21, 6, 63, 6, 5, 64, 9, 30, 64, 30, 14, 64, 14, 31, 64, 31, 10, 64, 10, 9, 65, 11, 33, 65, 33, 16, 65, 16, 15, 65, 15, 34, 65, 34, 11, 66, 19, 43, 66, 43, 46, 66, 46, 41, 66, 41, 44, 66, 44, 19, 67, 22, 47, 67, 47, 27, 67, 27, 45, 67, 45, 23, 67, 23, 22, 68, 24, 49, 68, 49, 29, 68, 29, 28, 68, 28, 50, 68, 50, 24, 69, 32, 53, 69, 53, 38, 69, 38, 52, 69, 52, 54, 69, 54, 32, 70, 39, 56, 70, 56, 42, 70, 42, 55, 70, 55, 40, 70, 40, 39, 71, 48, 57, 71, 57, 51, 71, 51, 58, 71, 58, 59, 71, 59, 48, 0, 1, 2, 0, 3, 4, 0, 4, 5, 0, 5, 6, 0, 6, 1, 1, 6, 7, 1, 8, 9, 1, 9, 2, 2, 9, 10, 2, 10, 11, 2, 11, 12, 3, 13, 14, 3, 14, 15, 3, 15, 16, 3, 16, 4, 4, 16, 17, 4, 18, 5, 5, 18, 19, 5, 19, 20, 6, 21, 22, 6, 22, 7, 7, 22, 23, 7, 23, 24, 7, 24, 25, 8, 26, 27, 8, 27, 28, 8, 28, 29, 8, 29, 9, 9, 29, 30, 10, 31, 32, 10, 32, 33, 10, 33, 11, 11, 34, 35, 11, 35, 12, 12, 35, 36, 12, 36, 37, 12, 37, 13, 13, 37, 26, 13, 26, 25, 13, 25, 14, 14, 25, 31, 14, 30, 15, 15, 30, 38, 15, 38, 34, 16, 33, 39, 16, 39, 17, 17, 39, 40, 17, 40, 41, 17, 41, 36, 18, 35, 42, 18, 42, 43, 18, 43, 19, 19, 44, 45, 19, 45, 20, 20, 45, 27, 20, 27, 26, 20, 26, 37, 21, 37, 36, 21, 36, 41, 21, 41, 46, 21, 46, 22, 22, 46, 47, 23, 45, 48, 23, 48, 49, 23, 49, 24, 24, 50, 31, 24, 31, 25, 27, 47, 28, 28, 47, 51, 28, 51, 50, 29, 49, 52, 29, 52, 30, 30, 52, 38, 31, 50, 32, 32, 50, 53, 32, 54, 33, 33, 54, 39, 34, 38, 55, 34, 55, 42, 34, 42, 35, 38, 53, 55, 39, 54, 56, 40, 55, 57, 40, 57, 44, 40, 44, 41, 42, 56, 43, 43, 56, 58, 43, 58, 46, 44, 57, 48, 44, 48, 45, 46, 58, 47, 47, 58, 51, 48, 59, 49, 49, 59, 52, 50, 51, 53, 51, 57, 53, 52, 59, 54, 53, 57, 55, 54, 59, 56, 56, 59, 58 };
        return new TriMesh(space, true, vertices, indices);
    }

    public static void runTest(CanvasConstructionInfo canvasInfo) {
        TriMeshSphereCollisionTest test = new TriMeshSphereCollisionTest(canvasInfo);
        test.begin();
    }

    public static void main(String[] args) throws InterruptedException {
        runTest(null);
    }
}
