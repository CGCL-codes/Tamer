package net.sourceforge.ftgl;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import javax.media.opengl.GL;
import net.sourceforge.ftgl.util.Vector3f;

/**
 * FTBBox is a convenience class for handling bounding boxes.
 */
public class FTBBox {

    /** Is used to correct the scale of a given {@link Shape#getBounds2D()}. */
    public static final float SCALE = 64.0f;

    public float lowerX = 0.0f, lowerY = 0.0f, lowerZ = 0.0f, upperX = 0.0f, upperY = 0.0f, upperZ = 0.0f;

    /**
	 * Default constructor. Bounding box is set to zero.
	 */
    public FTBBox() {
    }

    /**
	 * Constructor.
	 * @param lx The lower x bound.
	 * @param ly The lower y bound.
	 * @param lz The lower z bound.
	 * @param ux The upper x bound.
	 * @param uy The upper y bound.
	 * @param uz The upper z bound.
	 */
    public FTBBox(float lx, float ly, float lz, float ux, float uy, float uz) {
        this.lowerX = lx;
        this.lowerY = ly;
        this.lowerZ = lz;
        this.upperX = ux;
        this.upperY = uy;
        this.upperZ = uz;
    }

    /**
	 * Constructor. Extracts a bounding box from a freetype glyph. Uses the
	 * control box for the glyph. <code>FT_Glyph_Get_CBox()</code>
	 * 
	 * @param glyph A freetype glyph
	 */
    public FTBBox(Shape glyph) {
        Rectangle2D bbox = glyph.getBounds2D();
        this.lowerX = (float) bbox.getMinX();
        this.lowerY = (float) bbox.getMinY();
        this.lowerZ = 0.0f;
        this.upperX = (float) bbox.getMaxX();
        this.upperY = (float) bbox.getMaxY();
        this.upperZ = 0.0f;
    }

    /**
	 * Move the Bounding Box by a vector.
	 * @param distance The distance to move the bbox in 3D space.
	 * @return This FTBBox after the move process,
	 */
    public FTBBox move(Vector3f distance) {
        this.lowerX += distance.x;
        this.lowerY += distance.y;
        this.lowerZ += distance.z;
        this.upperX += distance.x;
        this.upperY += distance.y;
        this.upperZ += distance.z;
        return this;
    }

    /**
	 * Merges this FTBBoxes with another to one. The new one can hold both
	 * original FTBBoxes.
	 * @param bbox The box to merge.
	 * @return This FTBBox after the merge process.
	 */
    public FTBBox merge(FTBBox bbox) {
        this.lowerX = bbox.lowerX < this.lowerX ? bbox.lowerX : this.lowerX;
        this.lowerY = bbox.lowerY < this.lowerY ? bbox.lowerY : this.lowerY;
        this.lowerZ = bbox.lowerZ < this.lowerZ ? bbox.lowerZ : this.lowerZ;
        this.upperX = bbox.upperX > this.upperX ? bbox.upperX : this.upperX;
        this.upperY = bbox.upperY > this.upperY ? bbox.upperY : this.upperY;
        this.upperZ = bbox.upperZ > this.upperZ ? bbox.upperZ : this.upperZ;
        return this;
    }

    /**
	 * Sets the depth of this FTBBox.
	 * @param depth The new depth of this FTBBox.
	 */
    public void setDepth(float depth) {
        this.upperZ = this.lowerZ + depth;
    }

    /**
	 * Returns the depth of this box.
	 * @return The depth of this box.
	 */
    public float getDepth() {
        return this.upperZ - this.lowerZ;
    }

    /**
	 * Returns the width of this box.
	 * @return The width of this box.
	 */
    public float getWidth() {
        return this.upperX - this.lowerX;
    }

    /**
	 * Returns the height of this box.
	 * @return The height of this box.
	 */
    public float getHeight() {
        return this.upperY - this.lowerY;
    }

    /**
	 * {@inheritDoc}
	 */
    public String toString() {
        return "[" + this.lowerX + "|" + this.lowerY + "|" + this.lowerZ + "] [" + this.upperX + "|" + this.upperY + "|" + this.upperZ + "]";
    }

    public static boolean renderBBox(GL gl, Vector3f pos, FTBBox box) {
        gl.glPushMatrix();
        gl.glTranslated(pos.x, pos.y, pos.z);
        gl.glBitmap(0, 0, 0f, 0f, pos.x, pos.y, (byte[]) null, 0);
        gl.glPushAttrib(GL.GL_CURRENT_BIT | GL.GL_LIGHTING_BIT | GL.GL_COLOR_BUFFER_BIT);
        gl.glColor4f(1.0f, 1.0f, 0.0f, 1.0f);
        gl.glDisable(GL.GL_LIGHTING);
        gl.glDisable(GL.GL_BLEND);
        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3f(box.lowerX, box.lowerY, box.lowerZ);
        gl.glVertex3f(box.lowerX, box.upperY, box.lowerZ);
        gl.glVertex3f(box.upperX, box.upperY, box.lowerZ);
        gl.glVertex3f(box.upperX, box.lowerY, box.lowerZ);
        gl.glEnd();
        if (box.lowerZ != box.upperZ) {
            gl.glBegin(GL.GL_LINE_LOOP);
            gl.glVertex3f(box.lowerX, box.lowerY, box.upperZ);
            gl.glVertex3f(box.lowerX, box.upperY, box.upperZ);
            gl.glVertex3f(box.upperX, box.upperY, box.upperZ);
            gl.glVertex3f(box.upperX, box.lowerY, box.upperZ);
            gl.glEnd();
            gl.glBegin(GL.GL_LINES);
            gl.glVertex3f(box.lowerX, box.lowerY, box.lowerZ);
            gl.glVertex3f(box.lowerX, box.lowerY, box.upperZ);
            gl.glVertex3f(box.lowerX, box.upperY, box.lowerZ);
            gl.glVertex3f(box.lowerX, box.upperY, box.upperZ);
            gl.glVertex3f(box.upperX, box.upperY, box.lowerZ);
            gl.glVertex3f(box.upperX, box.upperY, box.upperZ);
            gl.glVertex3f(box.upperX, box.lowerY, box.lowerZ);
            gl.glVertex3f(box.upperX, box.lowerY, box.upperZ);
            gl.glEnd();
        }
        gl.glPopAttrib();
        gl.glPopMatrix();
        gl.glBitmap(0, 0, 0f, 0f, -pos.x, -pos.y, (byte[]) null, 0);
        return true;
    }
}
