package org.cart.igd.entity;

import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import org.cart.igd.util.ColorRGBA;
import org.cart.igd.math.Vector3f;
import org.cart.igd.core.Kernel;
import org.cart.igd.models.obj.OBJAnimation;
import org.cart.igd.models.obj.OBJModel;

/**
 * Entity.java
 *
 * General Function:
 * Represends all objects that can be either selected or moved
 */
public abstract class Entity {

    public static ArrayList<Entity> allEntities = new ArrayList<Entity>();

    public static int globalIdCounter = 0;

    public int globalId;

    public Vector3f position;

    public Vector3f lastPosition;

    public Vector3f scale = new Vector3f(1f, 1f, 1f);

    public float facingDirection = 0.0f;

    public int id = 0;

    public float speed = 0.01f;

    public float turnSpeed = 0.1f;

    public OBJAnimation objAnimation;

    public OBJModel modelObj;

    protected float deltaTime;

    protected float animationSpeed = 7.0f;

    protected GL gl;

    protected GLU glu;

    protected ColorRGBA color = new ColorRGBA(1f, 1f, 1f, 0.9f);

    protected boolean drawBoundingSphere;

    public float boundingSphereRadius;

    public float yRotationRad = 0f;

    public boolean walking = false;

    public Entity(Vector3f pos, float fD, float bsr) {
        globalId = globalIdCounter++;
        gl = Kernel.display.getRenderer().getGL();
        glu = Kernel.display.getRenderer().getGLU();
        position = pos;
        lastPosition = pos;
        this.facingDirection = fD;
        this.boundingSphereRadius = bsr;
    }

    /**
	 * Constructor creates entity object
	 * @param Vector3f pos: location of the entity 
	 * @param float fD: direction entity is facing ( y rotation )
	 * @param float bsr: bounding sphere radius used for collision detection
	 * @param Model model: .obj format file data
	 **/
    public Entity(Vector3f pos, float fD, float bsr, OBJModel model) {
        this(pos, fD, bsr);
        this.modelObj = model;
        globalId = globalIdCounter++;
    }

    /**
	 * Constructor creates entity object
	 * @param Vector3f pos: location of the entity 
	 * @param float fD: direction entity is facing ( y rotation )
	 * @param float bsr: bounding sphere radius used for collision detection
	 * @param Model model: .obj format file data
	 **/
    public Entity(Vector3f pos, float fD, float bsr, OBJAnimation model) {
        this(pos, fD, bsr);
        this.objAnimation = model;
        globalId = globalIdCounter++;
    }

    /**
	 * update the entity animation it it has one
	 */
    public void update(long elapsedTime) {
        walking = false;
        if (objAnimation != null) {
            objAnimation.update(elapsedTime);
        }
    }

    /** get id used by the picking handler to detect selection */
    public final int getID() {
        return id;
    }

    /**
	 * get the position of the entity
	 * @param position location of the entity 
	 */
    public final Vector3f getPosition() {
        return position;
    }

    /**
	 * y rotation of the object in degrees
	 * @param int facingDirection in degrees
	 */
    public final float getFacingDirection() {
        return facingDirection;
    }

    public final void setYRotationDeg(float deg) {
        yRotationRad = deg * 0.0174532925f;
    }

    public final void setYRotationRad(float rad) {
        this.yRotationRad = rad;
    }

    public final float getYRotationDeg() {
        return yRotationRad * 57.29577951f;
    }

    public final float getYRotationRad() {
        return yRotationRad;
    }

    /** move the entity forward based on facing direction */
    public final void walkForward(long elapsedTime) {
        walking = true;
        position.x += (((float) elapsedTime * speed) * (float) Math.cos(facingDirection * 0.0174f));
        position.z += (((float) elapsedTime * speed) * (float) Math.sin(facingDirection * 0.0174f));
    }

    /** move the entity forward based on facing direction */
    public final void walkBackward(long elapsedTime) {
        walking = true;
        position.x -= (((float) elapsedTime * speed) * (float) Math.cos(facingDirection * 0.0174f));
        position.z -= (((float) elapsedTime * speed) * (float) Math.sin(facingDirection * 0.0174f));
    }

    /** move the entity left based on facing direction */
    public final void strafeLeft(long elapsedTime) {
        position.x += (((float) elapsedTime * speed) * (float) Math.cos((facingDirection - 90) * 0.0174f));
        position.z += (((float) elapsedTime * speed) * (float) Math.sin((facingDirection - 90) * 0.0174f));
    }

    /** move the entity right based on facing direction */
    public final void strafeRight(long elapsedTime) {
        position.x += (((float) elapsedTime * speed) * (float) Math.cos((facingDirection + 90) * 0.0174f));
        position.z += (((float) elapsedTime * speed) * (float) Math.sin((facingDirection + 90) * 0.0174f));
    }

    /** change the facingDirection(yRotation of the model)*/
    public final void turnRight(long elapsedTime) {
        facingDirection += ((float) elapsedTime * turnSpeed);
    }

    /** change the facingDirection(yRotation of the model)*/
    public final void turnLeft(long elapsedTime) {
        facingDirection -= ((float) elapsedTime * turnSpeed);
    }

    /**
	 * render the model at its location depending on which type was asigned
	 */
    public void render(GL gl) {
        if (modelObj != null) {
            gl.glPushMatrix();
            gl.glTranslatef(position.x, position.y - 2f, position.z);
            gl.glRotatef(facingDirection, 0f, -1f, 0f);
            modelObj.draw(gl);
            gl.glPopMatrix();
        }
        if (objAnimation != null) {
            objAnimation.render(gl, position, facingDirection);
        }
    }

    /**
	 * Render the entity at a location other than its current location 
	 */
    public void renderLocation(GL gl, Vector3f position) {
        if (modelObj != null) {
            gl.glPushMatrix();
            gl.glTranslatef(position.x, position.y - 2f, position.z);
            gl.glRotatef(facingDirection, 0f, -1f, 0f);
            modelObj.draw(gl);
            gl.glPopMatrix();
        }
        if (objAnimation != null) {
            objAnimation.render(gl, position, facingDirection);
        }
    }

    /**
	 * Render the entity at a location other than its current location 
	 * and facing a certain direction
	 */
    public void renderLocation(GL gl, Vector3f position, float fd) {
        if (modelObj != null) {
            gl.glPushMatrix();
            gl.glTranslatef(position.x, position.y - 2f, position.z);
            gl.glRotatef(fd, 0f, -1f, 0f);
            modelObj.draw(gl);
            gl.glPopMatrix();
        }
        if (objAnimation != null) {
            objAnimation.render(gl, position, fd);
        }
    }

    /**
	 * debug text for finding location of the entity 
	 */
    public String getName() {
        return "obj @ x: " + position.x + " y: " + position.y + " z " + position.z;
    }
}
