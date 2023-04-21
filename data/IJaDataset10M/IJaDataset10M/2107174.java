package Race;

import vrml.*;
import vrml.field.*;
import vrml.node.*;
import java.lang.*;
import Race.*;

public class Movement {

    public Scene scene = new Scene();

    public int sceneOK = 0;

    private float newTranslation[] = { 0, 0, 0 };

    private float newRotation[] = { 0, 1, 0, 0 };

    private float lastRotation[] = { 0, 1, 0, 0 };

    private float newTireRotation[] = { 0, 1, 0, 0 };

    private float newWheelSpin[] = { 1, 0, 0, 0 };

    public double throttle = 0.0;

    public double wheel = 0.0;

    private int currentIndex = 0;

    private int collide[] = { 0, 0 };

    private int collEnabled = 1;

    public int createWorld() {
        if (this.scene.ReadTrackTypesFile() == 1) {
            if (this.scene.ReadCourseFile() == 1) {
                this.sceneOK = 1;
                return 1;
            }
        }
        return 0;
    }

    public float[] getTranslation() {
        if (sceneOK == 1) {
            float tempTranslation[] = { newTranslation[0], newTranslation[1], newTranslation[2] };
            double collTranslation[] = { newTranslation[0], newTranslation[1], newTranslation[2] };
            tempTranslation[0] -= Math.sin(this.newRotation[3]) * .029 * this.throttle;
            tempTranslation[2] -= Math.cos(this.newRotation[3]) * .029 * this.throttle;
            int end = 0;
            int counter = 1;
            int toggle = 1;
            int endnow = 0;
            if (scene.cgfreepointer > 0) {
                while (end == 0) {
                    if (scene.courseGraph[currentIndex].isInside(collTranslation[0], collTranslation[2]) == true) {
                        endnow = scene.courseGraph[currentIndex].didCollide(collTranslation, newTranslation[0], newTranslation[2], tempTranslation[0], tempTranslation[2], collide);
                        if (endnow == 0) end = 1;
                    } else {
                        if (counter == scene.cgfreepointer) {
                            end = 1;
                        } else {
                            currentIndex += (counter * toggle);
                            counter++;
                            if (currentIndex >= scene.cgfreepointer) currentIndex -= scene.cgfreepointer; else if (currentIndex <= -1) currentIndex += scene.cgfreepointer;
                            if (toggle == 1) toggle = -1; else toggle = 1;
                        }
                    }
                }
            }
            if (collide[0] == 0 || collEnabled == 0) newTranslation[0] = tempTranslation[0];
            if (collide[1] == 0 || collEnabled == 0) newTranslation[2] = tempTranslation[2];
        }
        return newTranslation;
    }

    public float[] getRotation() {
        if (this.throttle != 0) {
            lastRotation[3] = newRotation[3];
            newRotation[3] -= .06 * wheel;
            newRotation[3] %= 6.28;
        }
        return newRotation;
    }

    public float[] getTireRotation() {
        float wheelSpd = (float) 1.1;
        float threshold = (float) 1.047;
        newTireRotation[3] = wheelSpd * (newRotation[3] - lastRotation[3]);
        if (newTireRotation[3] > threshold) newTireRotation[3] = threshold; else if (newTireRotation[3] < (-1 * threshold)) newTireRotation[3] = (-1 * threshold);
        return newTireRotation;
    }

    public float[] getWheelSpin() {
        newWheelSpin[3] -= this.throttle * .15;
        return newWheelSpin;
    }

    public void keyUp(int newkey) {
        if (newkey == 37) wheel = 0; else if (newkey == 39) wheel = 0;
    }

    public int keyDown(int newkey) {
        if (newkey == 38) {
            if (throttle < 15) throttle++;
            return 38;
        } else if (newkey == 40) {
            if (throttle > -3) throttle--;
            return 40;
        } else if (newkey == 37) {
            if (wheel > -2) wheel--;
            return 37;
        } else if (newkey == 39) {
            if (wheel < 2) wheel++;
            return 39;
        } else if (newkey == 49) return 49; else if (newkey == 50) return 50; else if (newkey == 51) return 51; else if (newkey == 52) return 52; else if (newkey == 53) return 53; else if (newkey == 67) {
            collEnabled++;
            collEnabled %= 2;
            return 67;
        } else if (newkey == 84) return 84; else {
            return 0;
        }
    }
}
