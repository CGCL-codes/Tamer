package edu.ucsd.ncmir.jinx.events;

import JSci.maths.Quaternion;
import edu.ucsd.ncmir.asynchronous_event.AsynchronousEvent;

/**
 *
 * @author spl
 */
public class JxLoaderEvent extends AsynchronousEvent {

    private double _u = Double.MAX_VALUE;

    private double _v = Double.MAX_VALUE;

    private double _plane = Double.MAX_VALUE;

    private double _size = Double.MAX_VALUE;

    private Quaternion _quaternion = new Quaternion(1, 0, 0, 0);

    private String _object_name = null;

    public void parseLocation(String location_string) {
        if (location_string != null) {
            String[] triplet = location_string.split(",");
            this._u = Double.parseDouble(triplet[0]);
            this._v = Double.parseDouble(triplet[1]);
            this._plane = Double.parseDouble(triplet[2]);
        }
    }

    public void parseSize(String size_string) {
        if (size_string != null) {
            this._size = Double.parseDouble(size_string);
        }
    }

    public void parseView(String view_string) {
        if (view_string != null) {
            if (view_string.equals("XY")) this._quaternion = new Quaternion(1, 0, 0, 0); else if (view_string.equals("YZ")) this._quaternion = new Quaternion(0.5, -0.5, -0.5, -0.5); else if (view_string.equals("XZ")) this._quaternion = new Quaternion(0.5, 0.5, 0.5, 0.5); else new JxErrorEvent().send("Bad view " + view_string);
        }
    }

    public void parseQuaternion(String quaternion_string) {
        if (quaternion_string != null) {
            String[] quad = quaternion_string.split(",");
            this._quaternion = new Quaternion(Double.parseDouble(quad[0]), Double.parseDouble(quad[1]), Double.parseDouble(quad[2]), Double.parseDouble(quad[3]));
        }
    }

    public void parseObjectName(String object_name) {
        this._object_name = object_name;
    }

    public double getU() {
        return this._u;
    }

    public double getV() {
        return this._v;
    }

    public double getPlane() {
        return this._plane;
    }

    public double getSize() {
        return this._size;
    }

    public Quaternion getQuaternion() {
        return this._quaternion;
    }

    public String getObjectName() {
        return this._object_name;
    }
}
