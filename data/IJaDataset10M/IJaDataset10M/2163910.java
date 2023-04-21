package test3d.nehe;

import test3d.Test3D;
import xenon3d.Canvas3D;
import xenon3d.View3D;
import xenon3d.Xenon3D;
import xenon3d.scene.Appearance;
import xenon3d.scene.ColoringAttributes;
import xenon3d.scene.Geometry;
import xenon3d.scene.GeometryArray;
import xenon3d.scene.GraphicsContext3D;
import xenon3d.scene.PrimType;
import xenon3d.scene.Transform;
import xenon3d.scene.VertexFormat;
import xenon3d.vector.Color;
import xenon3d.vector.Color3f;

/**
 * NeHe Tutorial 03 "Adding Color".
 * New features: using the GraphicsContext3D object, Geometry, Appearance and
 * Transform.
 */
public class NeHe03 implements Test3D {

    /** The test title. */
    private static final String TITLE = "Adding Color";

    /** The test id. */
    private static final String ID = "03";

    /** The package screen name. */
    private static final String PACKAGE = "NeHe";

    /** The window caption. */
    private static final String CAPTION = PACKAGE + " " + ID + " - " + TITLE;

    /** The Xenon3D master window. */
    private Xenon3D xenon;

    /** The Canvas3D to render the scene into. */
    private Canvas3D canvas;

    /** The View3D which defines the view to the scene. */
    private View3D view;

    /** The triangle geometry. */
    private Geometry tri_geo = new GeometryArray(VertexFormat.VertexColor3, PrimType.Triangles, tri_data);

    /** The quad geometry. */
    private Geometry quad_geo = new GeometryArray(VertexFormat.Vertex3D, PrimType.Quads, quad_data);

    /** The triangle transform. */
    private Transform tri_trans = new Transform(-1.25f, 0.0f, -5.0f);

    /** The quad transform. */
    private Transform quad_trans = new Transform(1.25f, 0.0f, -5.0f);

    /** The quad appearance. */
    Appearance quad_app = new Appearance();

    /** The quad coloring attributes. */
    private ColoringAttributes ca = new ColoringAttributes();

    /**
     * Creates a new NeHe01 example object.
     */
    public NeHe03() {
        xenon = new Xenon3D(CAPTION);
        view = new View3D();
        view.setViewModePolicy(View3D.ViewModePolicy.Trace);
        canvas = createCanvas3D();
        canvas.attachView(view);
        xenon.attachCanvas(canvas);
        ca.setColor(new Color3f(Color.MidBlue));
        quad_app.setColoringAttributes(ca);
    }

    /**
     * Starts the NeHe01 example program.
     * @param args  the param array
     */
    public static void main(String[] args) {
        NeHe03 app = new NeHe03();
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    /**
     * Returns the test package screen name.
     * @return the package screen name
     */
    public String getPackageName() {
        return PACKAGE;
    }

    /**
     * Returns the test id.
     * @return the test id
     */
    public String getId() {
        return ID;
    }

    /**
     * Returns a new customized Canvas3D object.
     * @return the new Canvas3D
     */
    private Canvas3D createCanvas3D() {
        return new Canvas3D() {

            @Override
            public void init() {
                System.out.println("Canvas3D.init()");
            }

            @Override
            public void reshape(int width, int height) {
                System.out.println("Canvas3D.reshape(" + width + ", " + height + ")");
            }

            @Override
            public void preRender() {
                System.out.println("Canvas3D.preRender()");
            }

            @Override
            public void render() {
                System.out.println("Canvas3D.render()");
                GraphicsContext3D gc = GraphicsContext3D.getGC();
                gc.beginTransform();
                gc.setTransform(tri_trans);
                gc.draw(tri_geo);
                gc.endTransform();
                gc.beginTransform();
                gc.setTransform(quad_trans);
                gc.apply(quad_app);
                gc.draw(quad_geo);
                gc.endTransform();
            }

            @Override
            public void postRender() {
                System.out.println("Canvas3D.postRender()");
            }

            @Override
            public void dispose() {
                System.out.println("Canvas3D.dispose()");
            }
        };
    }

    static final float[] tri_data = new float[] { 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f };

    static final float[] quad_data = new float[] { -1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f };
}
