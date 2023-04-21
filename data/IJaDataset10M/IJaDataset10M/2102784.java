package org.jcrpg.threed.jme.tool;

import org.jcrpg.threed.input.action.CKeyAction;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

public class CameraProgram {

    public Camera camera;

    public Vector3f startPosition;

    public Vector3f endPosition;

    public Vector3f originalDirection;

    public Vector3f finalDirection;

    public Vector3f diffVector;

    public Vector3f dirDiffVector;

    public Vector3f currDiffVector;

    public Vector3f currDirDiffVector;

    public float runTime;

    float currentTime;

    public CameraProgram(Camera camera, Vector3f startPosition, Vector3f endPosition, Vector3f originalDirection, Vector3f finalDirection, float runTime) {
        this.camera = camera;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.originalDirection = originalDirection;
        this.finalDirection = finalDirection;
        this.runTime = runTime;
        diffVector = endPosition.subtract(startPosition);
        dirDiffVector = finalDirection.subtract(originalDirection);
        currDiffVector = new Vector3f();
        currDirDiffVector = new Vector3f();
    }

    public void stop() {
        camera.setLocation(endPosition);
        CKeyAction.setCameraDirection(camera, finalDirection.x, finalDirection.y, finalDirection.z);
    }

    public void start() {
        currentTime = 0;
    }

    /**
	 * returns true if finished.
	 * @param timePerFrame
	 * @return
	 */
    public boolean update(float timePerFrame) {
        currentTime += timePerFrame;
        float percent = currentTime / runTime;
        if (percent > 1.0f) percent = 1.0f;
        diffVector.mult(percent, currDiffVector);
        dirDiffVector.mult(percent, currDirDiffVector);
        currDiffVector.addLocal(startPosition);
        currDirDiffVector.addLocal(originalDirection);
        camera.setLocation(currDiffVector);
        CKeyAction.setCameraDirection(camera, currDirDiffVector.x, currDirDiffVector.y, currDirDiffVector.z);
        return percent == 1.0f;
    }
}
