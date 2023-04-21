package org.alcibiade.sculpt.gui.viewport.painter;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.Set;
import java.util.TreeSet;
import org.alcibiade.sculpt.gui.viewport.ViewportPath;
import org.alcibiade.sculpt.math.Vector;
import org.alcibiade.sculpt.math.projection.Projection;
import org.alcibiade.sculpt.math.projection.ViewportCoord;
import org.alcibiade.sculpt.math.transformation.FrameTransformation;
import org.alcibiade.sculpt.math.transformation.Transformation;
import org.alcibiade.sculpt.math.transformation.Viewpoint;
import org.alcibiade.sculpt.mesh.Face;
import org.alcibiade.sculpt.mesh.Mesh;
import org.alcibiade.sculpt.mesh.Vertex;

/**
 * Solid painting for meshes in viewports.
 * 
 * @author Yannick Kirschhoffer
 * 
 */
public class SolidPainter extends AbstractMeshPainter {

    public static final double ALPHA_OPAQUE = 1.0;

    public static final double ALPHA_DEFAULT = 0.9;

    public static final double ALPHA_TRANSPARENT = 0.0;

    /**
	 * Viewport colors.
	 */
    private Color col_faces = Color.LIGHT_GRAY;

    private Color col_normal = Color.BLUE.darker();

    /**
	 * If this is set, faces normals are displayed.
	 */
    private boolean showNormals = false;

    /**
	 * Should the edges be displayed.
	 */
    private boolean showEdges = true;

    /**
	 * Alpha value to apply to faces fills.
	 */
    private double alpha = ALPHA_DEFAULT;

    /**
	 * Create a new solid painter with default alpha value.
	 */
    public SolidPainter() {
        super();
    }

    /**
	 * Create a new solid painter with explicit alpha value.
	 * 
	 * @param alpha
	 *            The transparency of faces fills.
	 */
    public SolidPainter(double alpha) {
        this.alpha = alpha;
    }

    /**
	 * Create a new solid painter with explicit alpha value.
	 * 
	 * @param alpha
	 *            The transparency of faces fills.
	 */
    public SolidPainter(boolean showEdges) {
        this.showEdges = showEdges;
    }

    /**
	 * Get the alpha transparency factor.
	 * 
	 * @return the alpha transparency factor.
	 */
    public double getAlpha() {
        return alpha;
    }

    /**
	 * Set the alpha transparency factor.
	 * 
	 * @param alpha
	 *            The new alpha transparency factor.
	 */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
	 * Set the normal display flag.
	 * 
	 * @param status
	 *            If this is true, faces normals will be displayed.
	 */
    public void setNormalsDisplay(boolean status) {
        showNormals = status;
    }

    public void paint(Graphics2D g2, Dimension size, Mesh mesh, Projection viewProjection, Viewpoint viewPoint) {
        Dimension half_size = new Dimension(size.width / 2, size.height / 2);
        double alpha = this.alpha;
        if (isFast) {
            alpha = ALPHA_OPAQUE;
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, isFast ? RenderingHints.VALUE_ANTIALIAS_OFF : RenderingHints.VALUE_ANTIALIAS_ON);
        Transformation viewTransformation = new FrameTransformation(viewPoint);
        g2.translate(half_size.width, half_size.height);
        g2.setStroke(new BasicStroke((float) 0.6));
        if (mesh != null) {
            Set<ViewportPath> paths = new TreeSet<ViewportPath>();
            for (Face f : mesh.getFaces()) {
                if (alpha >= ALPHA_OPAQUE) {
                    Vector normal = viewTransformation.transform(f.getNormal());
                    ViewportCoord vc_normal = viewProjection.project(normal);
                    if (vc_normal.z < 0) {
                        continue;
                    }
                }
                ViewportPath gpath = new ViewportPath();
                double sum_z = 0;
                for (int fv = 0; fv < f.size(); fv++) {
                    Vertex v = f.getVertex(fv);
                    ViewportCoord vc = viewProjection.project(viewTransformation.transform(v));
                    if (fv == 0) {
                        gpath.getPath().moveTo((float) vc.x, (float) vc.y);
                    } else {
                        gpath.getPath().lineTo((float) vc.x, (float) vc.y);
                    }
                    sum_z += vc.z;
                }
                gpath.setZ(sum_z / f.size());
                gpath.setLighting(f.getLighting());
                if (showNormals) {
                    Vector origin = f.getCenter();
                    Vector norm = f.getNormal();
                    norm.add(origin);
                    ViewportCoord vc1 = viewProjection.project(viewTransformation.transform(origin));
                    ViewportCoord vc2 = viewProjection.project(viewTransformation.transform(norm));
                    gpath.setNormal(new Line2D.Double(vc1.x, vc1.y, vc2.x, vc2.y));
                }
                paths.add(gpath);
            }
            for (ViewportPath gpath : paths) {
                org.alcibiade.sculpt.math.Color col = new org.alcibiade.sculpt.math.Color(col_faces);
                col.multiply(gpath.getLighting());
                g2.setColor(col.getAwtColor());
                if (alpha < ALPHA_OPAQUE) {
                    Composite previousComposite = g2.getComposite();
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
                    g2.fill(gpath.getPath());
                    g2.setComposite(previousComposite);
                } else {
                    g2.fill(gpath.getPath());
                }
                if (showEdges) {
                    g2.setColor(edgeColor);
                    g2.draw(gpath.getPath());
                }
                Line2D normal = gpath.getNormal();
                if (normal != null) {
                    g2.setColor(col_normal);
                    g2.draw(normal);
                }
            }
        }
        g2.translate(-half_size.width, -half_size.height);
    }
}
