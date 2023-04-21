package com.jmbaai.bombsight.graphic.shape;

import gov.nasa.worldwind.render.DrawContext;
import java.awt.Color;
import java.nio.DoubleBuffer;
import java.util.Collection;
import javax.media.opengl.GL;
import com.jmbaai.bombsight.tech.math.Tuple3;
import com.sun.opengl.util.BufferUtil;

/**
 * A simple shape (points, lines, polygons) defined by vertices. The type of
 * shape is defined by the OpenGL "primitive type" (e.g. GL.GL_POINTS,
 * GL.GL_LINES, etc.).
 * <p>
 * Derived loosely from gov.nasa.worldwind.render.Polyline.
 * @author jonb
 */
public class VertexShape implements ApparentShape {

    public VertexShape(int primType) {
        _primType = primType;
    }

    public VertexShape(int primType, Collection<Tuple3> verts) {
        _primType = primType;
        setVertices(verts);
    }

    public void setVertices(Collection<Tuple3> verts) {
        if (verts == null) throw new IllegalArgumentException();
        _verts = BufferUtil.newDoubleBuffer(3 * verts.size());
        for (Tuple3 vert : verts) {
            _verts.put(vert.x).put(vert.y).put(vert.z);
        }
    }

    @Override
    public void setColor(Color color) {
        if (color == null) throw new IllegalArgumentException();
        _color = color;
    }

    @Override
    public Color getColor() {
        return _color;
    }

    @Override
    public AntialiasHint getAntiAliasHint() {
        return _antiAliasHint;
    }

    @Override
    public void setAntiAliasHint(AntialiasHint hint) {
        _antiAliasHint = hint;
    }

    @Override
    public double getLineWidth() {
        return _lineWidth;
    }

    @Override
    public void setLineWidth(double lineWidth) {
        _lineWidth = lineWidth;
    }

    @Override
    public short getStipplePattern() {
        return _stipplePattern;
    }

    /**
	 * Sets the stipple pattern for specifying line types other than solid. See
	 * the OpenGL specification or programming guides for a description of this
	 * parameter. Stipple is also affected by the path's stipple factor,
	 * {@link #setStippleFactor(int)}.
	 * 
	 * @param stipplePattern the stipple pattern.
	 */
    @Override
    public void setStipplePattern(short stipplePattern) {
        _stipplePattern = stipplePattern;
    }

    @Override
    public int getStippleFactor() {
        return _stippleFactor;
    }

    /**
	 * Sets the stipple factor for specifying line types other than solid. See
	 * the OpenGL specification or programming guides for a description of this
	 * parameter. Stipple is also affected by the path's stipple pattern,
	 * {@link #setStipplePattern(short)}.
	 * 
	 * @param stippleFactor the stipple factor.
	 */
    @Override
    public void setStippleFactor(int stippleFactor) {
        _stippleFactor = stippleFactor;
    }

    public void render(DrawContext dc) {
        if (dc == null) throw new IllegalArgumentException();
        if (_verts == null || _verts.capacity() <= 0) return;
        GL gl = dc.getGL();
        int attrBits = GL.GL_ENABLE_BIT | GL.GL_HINT_BIT | GL.GL_CURRENT_BIT | GL.GL_LINE_BIT;
        if (!dc.isPickingMode()) {
            if (_color.getAlpha() != 255) {
                attrBits |= GL.GL_COLOR_BUFFER_BIT;
            }
        }
        gl.glPushAttrib(attrBits);
        gl.glPushClientAttrib(GL.GL_CLIENT_VERTEX_ARRAY_BIT);
        if (!dc.isPickingMode()) {
            if (_color.getAlpha() != 255) {
                gl.glEnable(GL.GL_BLEND);
                gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
            }
            dc.getGL().glColor4ub((byte) _color.getRed(), (byte) _color.getGreen(), (byte) _color.getBlue(), (byte) _color.getAlpha());
        }
        if (_stippleFactor > 0) {
            gl.glEnable(GL.GL_LINE_STIPPLE);
            gl.glLineStipple(_stippleFactor, _stipplePattern);
        } else {
            gl.glDisable(GL.GL_LINE_STIPPLE);
        }
        int hintAttr = GL.GL_LINE_SMOOTH_HINT;
        if (_primType == GL.GL_POLYGON) {
            hintAttr = GL.GL_POLYGON_SMOOTH_HINT;
        }
        gl.glHint(hintAttr, _antiAliasHint.getHintGl());
        gl.glLineWidth((float) _lineWidth);
        gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL.GL_DOUBLE, 0, _verts.rewind());
        gl.glDrawArrays(_primType, 0, _verts.capacity() / 3);
        gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
        gl.glPopAttrib();
        gl.glPopClientAttrib();
    }

    private int _primType;

    private DoubleBuffer _verts = null;

    private AntialiasHint _antiAliasHint = ApparentShape.AntialiasHint.ANTIALIAS_FASTEST;

    private Color _color = Color.WHITE;

    private double _lineWidth = 1;

    private short _stipplePattern = (short) 0xAAAA;

    private int _stippleFactor = 0;
}
