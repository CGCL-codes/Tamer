package gui.gl;

import static java.lang.Math.PI;
import static java.lang.Math.tan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import javax.vecmath.*;
import com.xith3d.scenegraph.*;
import com.xith3d.loaders.texture.*;
import com.xith3d.render.*;
import com.xith3d.render.jogl.*;
import javax.imageio.ImageIO;
import java.io.*;
import com.xith3d.image.DirectBufferedImage;
import java.awt.image.BufferedImage;

public class Util3D {

    public static final float rad(float ang) {
        return (float) (ang / 180.0 * PI);
    }

    public static final float deg(float ang) {
        return (float) (ang / PI * 180.0);
    }

    /** 
     * Creates a size-by-size wu sized plane with assigned texture
     * coordinates. Texture coordinates are generated for two layers.
     *
     * @param size the size of the plane.
     *
     * @return the plane with the given normal and size.
     */
    public static Geometry createPlane(float size, float r) {
        return createQuad(0, 0, size, r, r);
    }

    public static Geometry createQuad(float x, float y, float size, float s, float t) {
        Point3f[] coords = new Point3f[] { new Point3f(x, 0f, y), new Point3f(x + size, 0f, y), new Point3f(x + size, 0f, y + size), new Point3f(x, 0f, y + size) };
        TexCoord2f[] texcoords = new TexCoord2f[] { new TexCoord2f(0f, 0f), new TexCoord2f(s, 0f), new TexCoord2f(s, s), new TexCoord2f(0f, s) };
        TexCoord2f[] texcoords2 = new TexCoord2f[] { new TexCoord2f(0f, 0f), new TexCoord2f(t, 0f), new TexCoord2f(t, t), new TexCoord2f(0f, t) };
        QuadArray plane = new QuadArray(coords.length, GeometryArray.COORDINATES | GeometryArray.TEXTURE_COORDINATE_2, 2, new int[] { 0, 1 });
        plane.setCoordinates(0, coords);
        plane.setTextureCoordinates(0, 0, texcoords);
        plane.setTextureCoordinates(1, 0, texcoords2);
        return plane;
    }

    /**
     * Create a new textured plane shape node.
     *
     * @param path the texture to apply to the plane.
     * @param s the size of the plane.
     * @param r the number of repetitions.
     */
    public static Shape3D createTexturedPlane(String path, float s, float r) {
        TextureLoader loader = TextureLoader.getInstance();
        loader.registerPath("./");
        loader.registerPath("./dat/tex/");
        loader.registerPath("./dat/mdl/");
        loader.registerPath("./dat/gfx/");
        Appearance appearance = new Appearance();
        appearance.setTexture((Texture2D) loader.getMinMapTexture(path));
        PolygonAttributes polyAttr = new PolygonAttributes();
        polyAttr.setPolygonOffset(10.0f);
        polyAttr.setPolygonOffsetFactor(20.0f);
        appearance.setPolygonAttributes(polyAttr);
        return new Shape3D(createPlane(s, r), appearance);
    }

    public static Geometry createRectangle(float w, float h, Color3f color) {
        Point3f[] coords = new Point3f[] { new Point3f(0f, 0f, 0f), new Point3f(w, 0f, 0f), new Point3f(w, 0f, 0f), new Point3f(w, 0f, h), new Point3f(w, 0f, h), new Point3f(0f, 0f, h), new Point3f(0f, 0f, h), new Point3f(0f, 0f, 0f) };
        LineArray lines = new LineArray(coords.length, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        Color3f[] colors = new Color3f[] { new Color3f(color.x, color.y, color.z), new Color3f(color.x, color.y, color.z), new Color3f(color.x, color.y, color.z), new Color3f(color.x, color.y, color.z), new Color3f(color.x, color.y, color.z), new Color3f(color.x, color.y, color.z), new Color3f(color.x, color.y, color.z), new Color3f(color.x, color.y, color.z) };
        lines.setCoordinates(0, coords);
        lines.setColors(0, colors);
        return lines;
    }

    /**
     * Creates the default coordinate axes with specified length.
     * @param size the length of the axes.
     */
    public static Geometry createCoordAxes(float size) {
        Point3f[] coords = new Point3f[] { new Point3f(0, 0, 0), new Point3f(size, 0, 0), new Point3f(0, 0, 0), new Point3f(0, size, 0), new Point3f(0, 0, 0), new Point3f(0, 0, size) };
        Color3f[] colors = new Color3f[] { new Color3f(0, 0, 1), new Color3f(0, 0, 1), new Color3f(1, 0, 0), new Color3f(1, 0, 0), new Color3f(0, 1, 0), new Color3f(0, 1, 0) };
        LineArray lines = new LineArray(coords.length, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        lines.setCoordinates(0, coords);
        lines.setColors(0, colors);
        return lines;
    }

    /**
     * Creates a new grid with the specified number of rows and columns,
     * and with the specified cell size.
     *
     * @param cols the number of cols in the grid.
     * @param rows the number of rows in the grid.
     * @param size the size of a single cell.
     */
    public static Shape3D createGrid(int cols, int rows, float size) {
        rows++;
        cols++;
        Point3f[] coords = new Point3f[2 * (rows + cols)];
        LineArray grid = new LineArray(coords.length, GeometryArray.COORDINATES);
        for (int i = 1; i < 2 * rows; i += 2) {
            coords[i - 1] = new Point3f(0, 0.01f, .5f * size * (i - 1));
            coords[i] = new Point3f((cols - 1) * size, 0.01f, .5f * size * (i - 1));
        }
        for (int j = 1; j < 2 * cols; j += 2) {
            coords[2 * rows + j - 1] = new Point3f(.5f * size * (j - 1), 0.01f, 0);
            coords[2 * rows + j] = new Point3f(.5f * size * (j - 1), 0.01f, (rows - 1) * size);
        }
        grid.setCoordinates(0, coords);
        return new Shape3D(grid);
    }

    /**
     * Returns the point in world space corresponding to the given
     * coordinates in screen space.
     *
     * @param c the associated canvas.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @return the point in world space at the given depth.
     */
    public static Point3f toWorld(Canvas3D c, int x, int y) {
        return toWorld(c, x, y, c.getView().getFrontClipDistance());
    }

    /**
     * Returns the point in world space corresponding to the given
     * coordinates in screen space.
     *
     * @param c the associated canvas.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate (depth).
     * @return the point in world space at the given depth.
     */
    public static Point3f toWorld(Canvas3D c, int x, int y, float z) {
        float fov = c.getView().getFieldOfView();
        float width = c.getWidth();
        float height = c.getHeight();
        float panelY = (float) (tan(fov) * z);
        float panelX = panelY * (width / height);
        float xp = x / (width / 2);
        float yp = y / (height / 2);
        Point3f pt = new Point3f((xp - 1) * panelX, (1 - yp) * panelY, -z);
        Transform3D v = new Transform3D();
        c.getView().getTransform(v);
        v.transform(pt);
        return pt;
    }

    /**
     * Returns the point in screen space corresponding to the given
     * coordinates in world space.
     *
     * @param c the associated canvas.
     * @param x the world x coordinate.
     * @param y the world y coordinate.
     * @param z the world z coordinate.
     * @return the corresponding point in screen space.
     */
    public static Point3f toScreen(Canvas3D c, Point3f p) {
        return toScreen(c, p.x, p.y, p.z);
    }

    public static Point3f toScreen(Canvas3D c, float x, float y, float z) {
        float fov = c.getView().getFieldOfView();
        float width = c.getWidth();
        float height = c.getHeight();
        Transform3D v = new Transform3D();
        c.getView().getTransform(v);
        Point3f p = new Point3f(x, y, z);
        v.invert();
        v.transform(p);
        float panelY = (float) (tan(fov) * p.z);
        float panelX = panelY * (width / height);
        Point3f pt = new Point3f(p.x / panelX + 1, -p.y / panelY + 1, p.z);
        pt.x = width - pt.x * (width / 2);
        pt.y = height - pt.y * (height / 2);
        return pt;
    }

    /**
     * Returns the ray eminating from the point (x, y) in screen space 
     * directed along the screen z-axis.
     *
     * @param c the associated canvas.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @return the ray eminating from x, y in screen space.
     */
    public static PickRay castRay(Canvas3D c, int x, int y) {
        Point3f center = toWorld(c, x, y);
        Point3f d = toWorld(c, x, y, c.getView().getFrontClipDistance() + 0.1f);
        Vector3f forward = new Vector3f(center);
        forward.sub(d);
        return new PickRay(center, forward);
    }

    /**
     * 
     * @param path the path to the image to open.
     */
    public static BufferedImage getBufferedImage(String path) {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(path.getClass().getResource(path));
        } catch (Exception e) {
            try {
                bufferedImage = ImageIO.read(new File(path));
            } catch (Exception x) {
                System.err.println("craps!");
                x.printStackTrace(System.err);
            }
        }
        return bufferedImage;
    }

    /**
     * Sets the specified texture to the specified appearance. The texture
     * mode is set to modulated.
     *
     * reference: Java Cool Dude
     */
    public static void setTexture2D(Shape3D shape, String path) {
        setTexture2D(shape, path, false, true, true);
    }

    public static void setTexture2D(Shape3D shape, String path, boolean clip, boolean blend, boolean depth) {
        Appearance appr = shape.getAppearance();
        if (appr == null) {
            appr = new Appearance();
            shape.setAppearance(appr);
        }
        setTexture2D(path, appr, clip, blend, depth);
    }

    public static void setTexture2D(String path, Appearance appr) {
        setTexture2D(path, appr, false, false, false);
    }

    public static void setTexture2D(String path, Appearance appr, boolean clip, boolean blend, boolean depth) {
        BufferedImage bufferedImage = getBufferedImage(path);
        TextureLoader tl = TextureLoader.getInstance();
        if (!depth) {
            RenderingAttributes rendattr = new RenderingAttributes();
            rendattr.setDepthBufferEnable(false);
            appr.setRenderingAttributes(rendattr);
        }
        if (blend) {
            TransparencyAttributes aattr = new TransparencyAttributes();
            aattr.setTransparencyMode(TransparencyAttributes.BLENDED);
            aattr.setTransparency(0.0f);
            aattr.setDstBlendFunction(TransparencyAttributes.BLEND_ONE);
            aattr.setSrcBlendFunction(TransparencyAttributes.BLEND_SRC_ALPHA);
            appr.setTransparencyAttributes(aattr);
        }
        if (clip) {
            TextureAttributes texattr = new TextureAttributes();
            texattr.setTextureMode(TextureAttributes.MODULATE);
            texattr.setPerspectiveCorrectionMode(TextureAttributes.FASTEST);
            appr.setTextureAttributes(texattr);
        }
        bufferedImage = DirectBufferedImage.make(bufferedImage, false);
        Texture2D texture = (Texture2D) tl.constructTexture(bufferedImage, "RGB", false, Texture.BASE_LEVEL_LINEAR, Texture.BASE_LEVEL_LINEAR, Texture.WRAP, false, TextureLoader.SCALE_DRAW_BEST);
        appr.setTexture(texture);
    }

    /**
     * Enable picking of this node by traversing the scenegraph to the 
     * root and setting each node to be pickable.
     *
     * @param name the name used to identify this node.
     */
    public static void setPickable(Node node, String name) {
        Node g = node.getParent();
        while (g != null) {
            g.setPickable(true);
            g = g.getParent();
        }
        if (node instanceof Group) {
            setPickableRecursive((Group) node, name);
        }
    }

    /**
     * Set all children of the specified group to be pickable.
     *
     * @param group the node at which to start.
     * @param name the name used to identify the nodes.
     */
    public static void setPickableRecursive(Group group, String name) {
        if (group == null) {
            return;
        }
        group.setPickable(true);
        java.util.Enumeration e = group.getAllChildren();
        while (e.hasMoreElements()) {
            Node node = (Node) e.nextElement();
            if (node instanceof Group) {
                setPickableRecursive((Group) node, name);
            } else {
                node.setPickable(true);
                if (node instanceof Shape3D) {
                    node.setName(name);
                }
            }
        }
    }
}
