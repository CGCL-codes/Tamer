package geoannotate.gl;

import net.java.games.jogl.GL;

/**
 * @author Dave
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Cube extends Object {

    private static CartesianPoint[] vertices = { new CartesianPoint(0.5f, 0.5f, 0.5f), new CartesianPoint(-0.5f, 0.5f, 0.5f), new CartesianPoint(0.5f, -0.5f, 0.5f), new CartesianPoint(-0.5f, -0.5f, 0.5f), new CartesianPoint(0.5f, -0.5f, -0.5f), new CartesianPoint(-0.5f, -0.5f, -0.5f), new CartesianPoint(0.5f, 0.5f, -0.5f), new CartesianPoint(-0.5f, 0.5f, -0.5f) };

    private static CartesianVector[] normals = { new CartesianVector(0.0f, 0.0f, 1.0f), new CartesianVector(0.0f, -1.0f, 0.0f), new CartesianVector(0.0f, 0.0f, -1.0f), new CartesianVector(0.0f, 1.0f, 0.0f), new CartesianVector(1.0f, 0.0f, 0.0f), new CartesianVector(-1.0f, 0.0f, 0.0f) };

    private Quads _quads = new Quads();

    private int display_list;

    private Material material = new Material();

    private Cube() {
    }

    ;

    /**
	 * Initialise the cube, by creating a quadmesh.
	 */
    public Cube(GL gl) {
        this._quads.addQuad(new Quad(vertices[0], vertices[1], vertices[3], vertices[2]));
        this._quads.addQuad(new Quad(vertices[4], vertices[5], vertices[7], vertices[6]));
        this._quads.addQuad(new Quad(vertices[2], vertices[3], vertices[5], vertices[4]));
        this._quads.addQuad(new Quad(vertices[0], vertices[6], vertices[7], vertices[1]));
        this._quads.addQuad(new Quad(vertices[0], vertices[2], vertices[4], vertices[6]));
        this._quads.addQuad(new Quad(vertices[1], vertices[7], vertices[5], vertices[3]));
        material.setShininess(50f);
    }

    /**
	 * Draw the cube object in space.
	 */
    public void draw(GL gl) {
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        material.use(gl);
        this._quads.draw(gl);
    }

    public void setRed(float colour) {
        material.getAmbient().setRed(colour);
        material.getDiffuse().setRed(colour);
        material.getSpecular().setRed(colour);
    }

    public void setGreen(float colour) {
        material.getAmbient().setGreen(colour);
        material.getDiffuse().setGreen(colour);
        material.getSpecular().setGreen(colour);
    }

    public void setBlue(float colour) {
        material.getAmbient().setBlue(colour);
        material.getDiffuse().setBlue(colour);
        material.getSpecular().setBlue(colour);
    }

    public void setAlpha(float colour) {
        material.getAmbient().setAlpha(colour);
        material.getDiffuse().setAlpha(colour);
        material.getSpecular().setAlpha(colour);
    }
}
