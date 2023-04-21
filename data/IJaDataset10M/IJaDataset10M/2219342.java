package org.vrspace.server;

/**
Waypoint contains translation and rotation of a traveler, and time
needed to reach it.
*/
public class Waypoint {

    public float x, y, z, azimuth, elevation, tilt, time;

    public Waypoint() {
    }

    public Waypoint(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Waypoint(float x, float y, float z, float time) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.time = time;
    }

    /**
  Convinience constructor - uses Euler angles to represent rotation
  */
    public Waypoint(float x, float y, float z, float azimuth, float elevation, float tilt, float time) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.azimuth = azimuth;
        this.elevation = elevation;
        this.tilt = tilt;
        this.time = time;
    }
}
