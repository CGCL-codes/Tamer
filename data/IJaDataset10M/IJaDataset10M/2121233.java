package com.apocalyptech.minecraft.xray;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/***
 * Utility class which mimicks a first person shooters camera perspective
 * @author Vincent
 */
public class FirstPersonCameraController {

    private Vector3f position = null;

    private float yaw = 0.0f;

    private float pitch = 0.0f;

    /***
     * Create a camera at location x,y,z facing down the z axis
     * @param x
     * @param y
     * @param z
     */
    public FirstPersonCameraController(float x, float y, float z) {
        position = new Vector3f(x, y, z);
    }

    public void incYaw(float amount) {
        yaw += amount;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void incPitch(float amount) {
        pitch += amount;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void walkForward(float distance, boolean camera_lock) {
        position.x -= distance * (float) Math.sin(Math.toRadians(yaw));
        position.z += distance * (float) Math.cos(Math.toRadians(yaw));
        if (!camera_lock) {
            position.y += distance * (float) Math.sin(Math.toRadians(pitch));
        }
    }

    public void walkBackwards(float distance, boolean camera_lock) {
        position.x += distance * (float) Math.sin(Math.toRadians(yaw));
        position.z -= distance * (float) Math.cos(Math.toRadians(yaw));
        if (!camera_lock) {
            position.y -= distance * (float) Math.sin(Math.toRadians(pitch));
        }
    }

    public void strafeLeft(float distance) {
        position.x -= distance * (float) Math.sin(Math.toRadians(yaw - 90));
        position.z += distance * (float) Math.cos(Math.toRadians(yaw - 90));
    }

    public void strafeRight(float distance) {
        position.x -= distance * (float) Math.sin(Math.toRadians(yaw + 90));
        position.z += distance * (float) Math.cos(Math.toRadians(yaw + 90));
    }

    public void moveUp(float distance) {
        position.y -= distance;
    }

    public void applyCameraTransformation() {
        GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(position.x, position.y, position.z);
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public void setYawAndPitch(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
	 * Processes the given Nether warp factor (should generally be 8.0f or
	 * 1/8.0f, depending on the direction we're headed).
	 * 
	 * @param multiplier
	 */
    public void processNetherWarp(float multiplier) {
        this.position.x *= multiplier;
        this.position.z *= multiplier;
    }
}
