package Terrain5;

import java.io.*;
import java.awt.*;
import java.math.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.vecmath.*;
import javax.media.j3d.*;
import java.util.*;
import General.*;

class Repellor {

    private Point3d pos;

    private TecPlate p1, p2;

    public Repellor(Point3d p, TecPlate plate1, TecPlate plate2) {
        pos = new Point3d(p);
        p1 = plate1;
        p2 = plate2;
    }

    public Point3d getPos() {
        return pos;
    }

    public boolean actsOn(TecPlate p) {
        return p.equals(p1) || p.equals(p2);
    }

    public Vector3d getForceAt(Point3d p) {
        Vector3d out = new Vector3d(p.x - pos.x, p.y - pos.y, p.z - pos.z);
        double dist = out.length();
        out.normalize();
        double force = Math.max(0, 1 - dist / 100);
        out.scale(force * 10);
        return out;
    }
}
