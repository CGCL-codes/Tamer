package gestalt.candidates;

import javax.media.opengl.GL;
import gestalt.Gestalt;
import gestalt.impl.jogl.context.JoglGLContext;
import gestalt.shape.Color;
import gestalt.shape.AbstractShape;
import gestalt.util.JoglUtil;
import gestalt.render.Disposable;
import gestalt.context.GLContext;
import gestalt.impl.jogl.shape.JoglMaterial;

public class JoglSmoothEdgeDisk extends AbstractShape implements Disposable {

    protected float edgewidth = 10;

    protected Color mEdgeColor = new Color();

    protected int mResolution = 36;

    protected float _myRadius;

    private boolean mCompileIntoList;

    public JoglSmoothEdgeDisk() {
        material = new JoglMaterial();
        mEdgeColor.set(material.color);
        mEdgeColor.a = 0.0f;
        mCompileIntoList = false;
    }

    public void compile_into_displaylist() {
        mCompileIntoList = true;
    }

    public void edge_width(final float theValue) {
        edgewidth = theValue;
    }

    public void resolution(int theValue) {
        mResolution = theValue;
    }

    public Color edge_color() {
        return mEdgeColor;
    }

    public void draw(final GLContext theRenderContext) {
        final GL gl = ((JoglGLContext) theRenderContext).gl;
        material.begin(theRenderContext);
        gl.glPushMatrix();
        JoglUtil.applyTransform(gl, _myTransformMode, transform, rotation, scale);
        _myRadius = scale().length();
        gl.glPushMatrix();
        JoglUtil.applyOrigin(gl, Gestalt.SHAPE_ORIGIN_CENTERED);
        if (mCompileIntoList) {
            createDisplayList(gl);
        } else {
            drawCircle(gl);
        }
        gl.glPopMatrix();
        material.end(theRenderContext);
    }

    private boolean _myIsCompiled = false;

    private int _myDisplayList;

    private void createDisplayList(GL gl) {
        if (!_myIsCompiled) {
            _myIsCompiled = true;
            _myDisplayList = gl.glGenLists(1);
            gl.glNewList(_myDisplayList, GL.GL_COMPILE);
            drawCircle(gl);
            gl.glEndList();
        }
        if (_myIsCompiled) {
            gl.glCallList(_myDisplayList);
        }
    }

    public void dispose(GLContext theRenderContext) {
        if (_myIsCompiled) {
            final GL gl = ((JoglGLContext) theRenderContext).gl;
            gl.glDeleteLists(_myDisplayList, 1);
        }
    }

    protected void drawCircle(GL gl) {
        final float myEdgeWidthRatio = (2 * edgewidth) / _myRadius;
        float[] myCircleX = new float[mResolution];
        float[] myCircleY = new float[mResolution];
        gl.glNormal3f(0, 0, 1);
        for (int i = 0; i < myCircleX.length; i++) {
            final float mRadiant = Gestalt.TWO_PI * (float) i / (float) mResolution;
            myCircleX[i] = (float) Math.sin(mRadiant) / 2.0f;
            myCircleY[i] = (float) Math.cos(mRadiant) / 2.0f;
        }
        gl.glBegin(GL.GL_QUAD_STRIP);
        for (int i = 0; i < myCircleX.length + 1; i++) {
            final int myIndex = i % myCircleX.length;
            final float myCenterX = myCircleX[myIndex] + 0.5f;
            final float myCenterY = myCircleY[myIndex] + 0.5f;
            final float myOuterX = myCircleX[myIndex] * (1 + myEdgeWidthRatio) + 0.5f;
            final float myOuterY = myCircleY[myIndex] * (1 + myEdgeWidthRatio) + 0.5f;
            gl.glColor4f(material.color.r, material.color.g, material.color.b, material.color.a);
            gl.glTexCoord2f(myCenterX, myCenterY);
            gl.glVertex2f(myCenterX, myCenterY);
            gl.glColor4f(mEdgeColor.r, mEdgeColor.g, mEdgeColor.b, mEdgeColor.a);
            gl.glTexCoord2f(myOuterX, myOuterY);
            gl.glVertex2f(myOuterX, myOuterY);
        }
        gl.glEnd();
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glColor4f(material.color.r, material.color.g, material.color.b, material.color.a);
        gl.glTexCoord2f(0.5f, 0.5f);
        gl.glVertex2f(0.5f, 0.5f);
        for (int i = 0; i < myCircleX.length; i++) {
            final int myNextIndex = (i + 1) % myCircleX.length;
            final float mNextPointX = myCircleX[myNextIndex] + 0.5f;
            final float mNextPointY = myCircleY[myNextIndex] + 0.5f;
            final float myOuterX = myCircleX[i] + 0.5f;
            final float myOuterY = myCircleY[i] + 0.5f;
            gl.glColor4f(material.color.r, material.color.g, material.color.b, material.color.a);
            gl.glTexCoord2f(mNextPointX, mNextPointY);
            gl.glVertex2f(mNextPointX, mNextPointY);
            gl.glColor4f(material.color.r, material.color.g, material.color.b, material.color.a);
            gl.glTexCoord2f(myOuterX, myOuterY);
            gl.glVertex2f(myOuterX, myOuterY);
        }
        gl.glEnd();
        gl.glPopMatrix();
    }
}
