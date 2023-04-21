package org.xith3d.demos.utils;

import org.jagatoo.opengl.enums.DrawMode;
import org.openmali.FastMath;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.TexCoord2f;
import org.openmali.vecmath2.Tuple3f;
import org.openmali.vecmath2.Vector3f;
import org.xith3d.loaders.texture.TextureLoader;
import org.xith3d.loop.Updatable;
import org.xith3d.loop.UpdatingThread.TimingMode;
import org.xith3d.scenegraph.Appearance;
import org.xith3d.scenegraph.Geometry;
import org.xith3d.scenegraph.Material;
import org.xith3d.scenegraph.PolygonAttributes;
import org.xith3d.scenegraph.Shape3D;
import org.xith3d.scenegraph.StaticTransform;
import org.xith3d.scenegraph.Texture;
import org.xith3d.scenegraph.TriangleStripArray;
import org.xith3d.scenegraph.primitives.Rectangle;
import org.xith3d.scenegraph.primitives.Sphere;

/**
 * A factory class for cloth simulations.
 * 
 * @author Abdul Bezrati (aka JavaCoolDude)
 * @author Marvin Froehlich (aka Qudus)
 */
public class ClothFactory implements Updatable {

    private static final int GRID_WIDTH = 14, GRID_HEIGHT = 14;

    private final long timePrecision = 20000L;

    private long timeSinceLastUpdate = 0L;

    private int numberOfNodes = 0, numberOfSprings = 0;

    private final float damp = 0.9f, scale = 20f, radius = 4f, springLength = 0.9f, springConstant = 20f, clothInitalHeight = 17f;

    private Spring[] springs = null;

    private boolean wireMesh = false, lightingOn = true;

    private Shape3D ground = null, cloth = null;

    private Point3f gridStart = getGridStart();

    private Point3f[] clothData = null;

    private Vector3f tensionDirection = new Vector3f();

    private Vector3f sphereLocation = new Vector3f(0f, 12f, 0f);

    private Vector3f[] clothNormals = null;

    private Vector3f gravityVector = new Vector3f(0f, -0.98f, 0f);

    private Vector3f acceleration = new Vector3f();

    private Vector3f usefulVector = new Vector3f();

    private Vector3f windAction = new Vector3f();

    private Vector3f windForce = new Vector3f(-0.2f, 0f, -0.2f);

    private Vector3f force = new Vector3f();

    private Triangle[] clothMesh = null;

    private ClothNode[] clothNodes1 = null, clothNodes2 = null, nextClothNodes = null, currentClothNodes = null;

    private Appearance clothAppearance = new Appearance();

    private PolygonAttributes polygonAttributes = null;

    private TriangleStripArray clothGeomtry = null;

    private class Triangle {

        ClothNode clothNode0 = null;

        ClothNode clothNode1 = null;

        ClothNode clothNode2 = null;

        Vector3f normal = new Vector3f();

        Vector3f tool = new Vector3f();

        Point3f p0 = null;

        Point3f p1 = null;

        Point3f p2 = null;

        public void updateNormal() {
            tool.sub(p1, p0);
            normal.sub(p2, p0);
            normal.cross(tool, normal);
        }

        public void addForce(Tuple3f force) {
            clothNode0.force.add(force);
            clothNode1.force.add(force);
            clothNode2.force.add(force);
        }

        Triangle(ClothNode cn0, ClothNode cn1, ClothNode cn2) {
            clothNode0 = cn0;
            clothNode1 = cn1;
            clothNode2 = cn2;
            p0 = clothNode0.location;
            p1 = clothNode1.location;
            p2 = clothNode2.location;
            updateNormal();
        }
    }

    private class ClothNode {

        Vector3f velocity = new Vector3f();

        Vector3f normal = new Vector3f();

        Vector3f force = new Vector3f();

        Point3f location = new Point3f();

        boolean still = false;

        public void resetForce() {
            force.set(0f, 0f, 0f);
        }

        ClothNode() {
        }
    }

    private class Spring {

        float tension = 0f;

        float naturalLength = 0f;

        int firstNode = 0;

        int secondNode = 0;

        Spring() {
        }
    }

    private void computeNodeNormals() {
        for (int i = 0; i < numberOfNodes; ++i) currentClothNodes[i].normal.set(0f, 0f, 0f);
        Point3f p0 = null;
        Point3f p1 = null;
        Point3f p2 = null;
        Point3f p3 = null;
        Vector3f n0 = null;
        Vector3f n1 = null;
        Vector3f n2 = null;
        Vector3f n3 = null;
        Vector3f tool = Vector3f.fromPool();
        Vector3f normal = Vector3f.fromPool();
        for (int j = 0; j < GRID_HEIGHT - 1; ++j) {
            for (int i = 0; i < GRID_WIDTH - 1; ++i) {
                p0 = currentClothNodes[(j + 0) * GRID_WIDTH + (i + 0)].location;
                p1 = currentClothNodes[(j + 0) * GRID_WIDTH + (i + 1)].location;
                p2 = currentClothNodes[(j + 1) * GRID_WIDTH + (i + 0)].location;
                p3 = currentClothNodes[(j + 1) * GRID_WIDTH + (i + 1)].location;
                n0 = currentClothNodes[(j + 0) * GRID_WIDTH + (i + 0)].normal;
                n1 = currentClothNodes[(j + 0) * GRID_WIDTH + (i + 1)].normal;
                n2 = currentClothNodes[(j + 1) * GRID_WIDTH + (i + 0)].normal;
                n3 = currentClothNodes[(j + 1) * GRID_WIDTH + (i + 1)].normal;
                tool.sub(p1, p0);
                normal.sub(p2, p0);
                normal.cross(normal, tool);
                n0.add(normal);
                n1.add(normal);
                n2.add(normal);
                tool.sub(p1, p2);
                normal.sub(p3, p2);
                normal.cross(normal, tool);
                n1.add(normal);
                n2.add(normal);
                n3.add(normal);
            }
        }
        Vector3f.toPool(normal);
        Vector3f.toPool(tool);
        for (int i = 0; i < numberOfNodes; ++i) currentClothNodes[i].normal.normalize();
    }

    /**
     * {@inheritDoc}
     */
    public void update(long gameTime, long frameTime, TimingMode timingMode) {
        boolean updated = false;
        timeSinceLastUpdate += timingMode.getMicroSeconds(frameTime);
        while (timeSinceLastUpdate > timePrecision) {
            timeSinceLastUpdate -= timePrecision;
            updated = true;
            for (int i = 0; i < numberOfNodes; ++i) currentClothNodes[i].resetForce();
            for (int i = 0; i < clothMesh.length; i++) {
                float dot = clothMesh[i].normal.dot(windForce);
                windAction.scale(dot, clothMesh[i].normal);
                clothMesh[i].addForce(windAction);
            }
            for (int i = 0; i < numberOfSprings; ++i) {
                usefulVector.sub(currentClothNodes[springs[i].firstNode].location, currentClothNodes[springs[i].secondNode].location);
                springs[i].tension = springConstant * (usefulVector.length() - springs[i].naturalLength) / springs[i].naturalLength;
            }
            for (int i = 0; i < numberOfNodes; ++i) {
                nextClothNodes[i].still = currentClothNodes[i].still;
                if (currentClothNodes[i].still) {
                    nextClothNodes[i].location = currentClothNodes[i].location;
                    nextClothNodes[i].velocity.set(0f, 0f, 0f);
                } else {
                    force.add(gravityVector, currentClothNodes[i].force);
                    for (int j = 0; j < numberOfSprings; ++j) {
                        if (springs[j].firstNode == i) {
                            tensionDirection.sub(currentClothNodes[springs[j].secondNode].location, currentClothNodes[i].location);
                            tensionDirection.normalize();
                            force.scaleAdd(springs[j].tension, tensionDirection, force);
                        }
                        if (springs[j].secondNode == i) {
                            tensionDirection.sub(currentClothNodes[springs[j].firstNode].location, currentClothNodes[i].location);
                            tensionDirection.normalize();
                            force.scaleAdd(springs[j].tension, tensionDirection, force);
                        }
                    }
                    acceleration.scale(100f, force);
                    nextClothNodes[i].velocity.scaleAdd(0.01f, acceleration, currentClothNodes[i].velocity);
                    nextClothNodes[i].velocity.scale(damp);
                    usefulVector.add(nextClothNodes[i].velocity, currentClothNodes[i].velocity);
                    usefulVector.scale(0.005f);
                    nextClothNodes[i].location.add(usefulVector, nextClothNodes[i].location);
                    usefulVector.sub(nextClothNodes[i].location, sphereLocation);
                    final float testRadius = radius * 1.03f;
                    if (usefulVector.lengthSquared() < testRadius * testRadius) {
                        usefulVector.normalize();
                        usefulVector.scaleAdd(testRadius, usefulVector, sphereLocation);
                        nextClothNodes[i].location.set(usefulVector);
                    }
                    if (nextClothNodes[i].location.getY() < 0.2f) nextClothNodes[i].location.setY(0.2f);
                }
            }
        }
        if (updated) {
            for (int i = 0; i < clothMesh.length; i++) clothMesh[i].updateNormal();
            computeNodeNormals();
            clothGeomtry.setCoordinates(0, clothData);
            clothGeomtry.setNormals(0, clothNormals);
        }
    }

    public void letGo(int index) {
        switch(index) {
            case 1:
                clothNodes1[0].still = false;
                break;
            case 2:
                clothNodes1[GRID_WIDTH - 1].still = false;
                break;
            case 3:
                clothNodes1[GRID_WIDTH * (GRID_HEIGHT - 1)].still = false;
                break;
            case 4:
                clothNodes1[GRID_HEIGHT * GRID_WIDTH - 1].still = false;
                break;
        }
    }

    private Point3f getGridStart() {
        float halfSize = springLength / 2f;
        return (new Point3f((-GRID_WIDTH + springLength) * halfSize, clothInitalHeight, (-GRID_HEIGHT + springLength) * halfSize));
    }

    public final Shape3D getGround() {
        return (ground);
    }

    public final Shape3D getCloth() {
        return (cloth);
    }

    public void switchFillWire() {
        wireMesh = !wireMesh;
        if (wireMesh) polygonAttributes.setDrawMode(DrawMode.LINE); else polygonAttributes.setDrawMode(DrawMode.FILL);
    }

    public void resetCloth() {
        for (int j = 0; j < GRID_HEIGHT; j++) {
            for (int i = 0; i < GRID_WIDTH; i++) {
                if (clothNodes1[j * GRID_WIDTH + i] == null) clothNodes1[j * GRID_WIDTH + i] = new ClothNode();
                clothNodes1[j * GRID_WIDTH + i].location.set(gridStart.getX() + springLength * i, gridStart.getY() + 0f, gridStart.getZ() + springLength * j);
                clothNodes1[j * GRID_WIDTH + i].velocity.set(0f, 0f, 0f);
                clothNodes1[j * GRID_WIDTH + i].still = false;
            }
        }
        clothNodes1[0].still = true;
        clothNodes1[GRID_WIDTH - 1].still = true;
        clothNodes1[GRID_WIDTH * (GRID_HEIGHT - 1)].still = true;
        clothNodes1[GRID_HEIGHT * GRID_WIDTH - 1].still = true;
        System.arraycopy(clothNodes1, 0, clothNodes2, 0, numberOfNodes);
        currentClothNodes = clothNodes1;
        nextClothNodes = clothNodes2;
        if (springs[0] != null) return;
        int springProgress = 0;
        for (int j = 0; j < GRID_HEIGHT; ++j) {
            for (int i = 0; i < GRID_WIDTH - 1; ++i) {
                springs[springProgress] = new Spring();
                springs[springProgress].firstNode = j * GRID_WIDTH + i;
                springs[springProgress].secondNode = j * GRID_WIDTH + i + 1;
                springs[springProgress].naturalLength = springLength;
                ++springProgress;
            }
        }
        for (int j = 0; j < GRID_HEIGHT - 1; ++j) {
            for (int i = 0; i < GRID_WIDTH; ++i) {
                springs[springProgress] = new Spring();
                springs[springProgress].firstNode = j * GRID_WIDTH + i;
                springs[springProgress].secondNode = (j + 1) * GRID_WIDTH + i;
                springs[springProgress].naturalLength = springLength;
                ++springProgress;
            }
        }
        for (int j = 0; j < GRID_HEIGHT - 1; ++j) {
            for (int i = 0; i < GRID_WIDTH - 1; ++i) {
                springs[springProgress] = new Spring();
                springs[springProgress].firstNode = j * GRID_WIDTH + i;
                springs[springProgress].secondNode = (j + 1) * GRID_WIDTH + i + 1;
                springs[springProgress].naturalLength = springLength * FastMath.sqrt(2f);
                ++springProgress;
            }
        }
        for (int j = 0; j < GRID_HEIGHT - 1; ++j) {
            for (int i = 1; i < GRID_WIDTH; ++i) {
                springs[springProgress] = new Spring();
                springs[springProgress].firstNode = j * GRID_WIDTH + i;
                springs[springProgress].secondNode = (j + 1) * GRID_WIDTH + i - 1;
                springs[springProgress].naturalLength = springLength * FastMath.sqrt(2f);
                ++springProgress;
            }
        }
        for (int j = 0; j < GRID_HEIGHT; ++j) {
            for (int i = 0; i < GRID_WIDTH - 2; ++i) {
                springs[springProgress] = new Spring();
                springs[springProgress].firstNode = j * GRID_WIDTH + i;
                springs[springProgress].secondNode = j * GRID_WIDTH + i + 2;
                springs[springProgress].naturalLength = springLength * 2;
                ++springProgress;
            }
        }
        for (int j = 0; j < GRID_HEIGHT - 2; ++j) {
            for (int i = 0; i < GRID_WIDTH; ++i) {
                springs[springProgress] = new Spring();
                springs[springProgress].firstNode = j * GRID_WIDTH + i;
                springs[springProgress].secondNode = (j + 2) * GRID_WIDTH + i;
                springs[springProgress].naturalLength = springLength * 2;
                ++springProgress;
            }
        }
    }

    private void makeGround(Texture groundTex) {
        this.ground = new Rectangle(scale * 2f, scale * 2f, groundTex);
        StaticTransform.rotateX(ground, FastMath.PI_HALF);
    }

    public Shape3D getSphere(int divisions) {
        Sphere sphere = new Sphere(radius, divisions, divisions, Geometry.COORDINATES | Geometry.NORMALS, false, 2);
        Material material = new Material(Colorf.GRAY90, Colorf.GRAY90, Colorf.RED, Colorf.WHITE, 100f);
        material.setLightingEnabled(lightingOn);
        material.setColorTarget(Material.NONE);
        Appearance appearance = new Appearance();
        appearance.setMaterial(material);
        sphere.setAppearance(appearance);
        StaticTransform.translate(sphere, sphereLocation);
        return (sphere);
    }

    private void makeCloth(Texture clothTex) {
        int[] strips = new int[GRID_HEIGHT - 1];
        TexCoord2f[] textureData = new TexCoord2f[numberOfNodes];
        for (int i = 0; i < strips.length; i++) strips[i] = 2 * GRID_WIDTH;
        this.clothMesh = new Triangle[(GRID_HEIGHT - 1) * (GRID_WIDTH - 1) * 2];
        this.clothData = new Point3f[2 * (numberOfNodes - GRID_WIDTH)];
        this.clothNormals = new Vector3f[2 * (numberOfNodes - GRID_WIDTH)];
        this.clothGeomtry = new TriangleStripArray(clothData.length, strips);
        computeNodeNormals();
        for (int j = 0; j < GRID_HEIGHT; j++) for (int i = 0; i < GRID_WIDTH; i++) textureData[j * GRID_WIDTH + i] = new TexCoord2f((float) i / GRID_WIDTH, (float) j / GRID_HEIGHT);
        int progress = 0;
        int meshProgress = 0;
        for (int a = 0; a < GRID_HEIGHT - 1; a++) {
            for (int i = 0; i < GRID_WIDTH; i++) {
                clothData[progress + 0] = clothNodes1[(a + 0) * GRID_WIDTH + i].location;
                clothData[progress + 1] = clothNodes1[(a + 1) * GRID_WIDTH + i].location;
                clothNormals[progress + 0] = clothNodes1[(a + 0) * GRID_WIDTH + i].normal;
                clothNormals[progress + 1] = clothNodes1[(a + 1) * GRID_WIDTH + i].normal;
                clothGeomtry.setTextureCoordinate(0, progress + 0, textureData[(a + 0) * GRID_WIDTH + i]);
                clothGeomtry.setTextureCoordinate(0, progress + 1, textureData[(a + 1) * GRID_WIDTH + i]);
                if (i != GRID_WIDTH - 1) {
                    clothMesh[meshProgress++] = new Triangle(clothNodes1[(a + 0) * GRID_WIDTH + (i + 0)], clothNodes1[(a + 1) * GRID_WIDTH + (i + 0)], clothNodes1[(a + 0) * GRID_WIDTH + (i + 1)]);
                    clothMesh[meshProgress++] = new Triangle(clothNodes1[(a + 1) * GRID_WIDTH + (i + 0)], clothNodes1[(a + 1) * GRID_WIDTH + (i + 1)], clothNodes1[(a + 0) * GRID_WIDTH + (i + 1)]);
                }
                progress += 2;
            }
        }
        clothGeomtry.setCoordinates(0, clothData);
        clothGeomtry.setNormals(0, clothNormals);
        this.polygonAttributes = new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE);
        clothAppearance.setTexture(clothTex);
        clothAppearance.setPolygonAttributes(polygonAttributes);
        Material material = new Material(Colorf.GRAY80, Colorf.GRAY80, Colorf.GRAY20, Colorf.GRAY60, 20.0f);
        material.setLightingEnabled(lightingOn);
        material.setColorTarget(Material.NONE);
        this.clothAppearance.setMaterial(material);
        this.cloth = new Shape3D(clothGeomtry, clothAppearance);
    }

    public ClothFactory(Texture groundTex, Texture flagTex) {
        this.numberOfNodes = GRID_HEIGHT * GRID_WIDTH;
        this.numberOfSprings = (GRID_HEIGHT - 0) * (GRID_WIDTH - 1);
        this.numberOfSprings += (GRID_HEIGHT - 1) * (GRID_WIDTH - 0);
        this.numberOfSprings += (GRID_HEIGHT - 1) * (GRID_WIDTH - 1);
        this.numberOfSprings += (GRID_HEIGHT - 1) * (GRID_WIDTH - 1);
        this.numberOfSprings += (GRID_HEIGHT - 0) * (GRID_WIDTH - 2);
        this.numberOfSprings += (GRID_HEIGHT - 2) * (GRID_WIDTH - 0);
        this.clothNodes1 = new ClothNode[numberOfNodes];
        this.clothNodes2 = new ClothNode[numberOfNodes];
        this.springs = new Spring[numberOfSprings];
        resetCloth();
        makeGround(groundTex);
        makeCloth(flagTex);
    }

    public ClothFactory(String groundTex, String flagTex) {
        this(TextureLoader.getInstance().getTexture(groundTex), TextureLoader.getInstance().getTexture(flagTex));
    }
}
