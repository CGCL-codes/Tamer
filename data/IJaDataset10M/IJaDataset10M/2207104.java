package com.jpatch.boundary.tools;

import com.jpatch.entity.GlMaterial;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.*;
import javax.vecmath.*;

public class Shape {

    private final Point3f[] vertices;

    private final Vector3f[] normals;

    private final int[] triangles;

    public Shape(Point3f[] vertices, int[] triangles) {
        if (triangles.length % 3 != 0) {
            throw new IllegalArgumentException("triangle array length must be a factor of 3");
        }
        this.vertices = new Point3f[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            this.vertices[i] = new Point3f(vertices[i]);
        }
        this.triangles = triangles.clone();
        normals = new Vector3f[triangles.length / 3];
        computeNormals();
    }

    public void draw(GL gl) {
        Point3f p = new Point3f();
        Vector3f v = new Vector3f();
        gl.glBegin(GL_TRIANGLES);
        for (int i = 0; i < normals.length; i++) {
            v.set(normals[i]);
            v.normalize();
            gl.glNormal3f(v.x, v.y, v.z);
            for (int t = i * 3; t < i * 3 + 3; t++) {
                p.set(vertices[triangles[t]]);
                gl.glVertex3f(p.x, p.y, p.z);
            }
        }
        gl.glEnd();
    }

    public void draw(GL gl, Matrix4d matrix) {
        Point3f p = new Point3f();
        Vector3f v = new Vector3f();
        gl.glBegin(GL_TRIANGLES);
        for (int i = 0; i < normals.length; i++) {
            v.set(normals[i]);
            matrix.transform(v);
            v.normalize();
            gl.glNormal3f(v.x, v.y, v.z);
            for (int t = i * 3; t < i * 3 + 3; t++) {
                p.set(vertices[triangles[t]]);
                matrix.transform(p);
                gl.glVertex3f(p.x, p.y, p.z);
            }
        }
        gl.glEnd();
    }

    public void drawOutline(GL gl, Matrix4d matrix) {
        Point3f p = new Point3f();
        gl.glLineWidth(1.5f);
        gl.glDisable(GL_LINE_SMOOTH);
        for (int i = 0; i < normals.length; i++) {
            gl.glBegin(GL_LINE_LOOP);
            for (int t = i * 3; t < i * 3 + 3; t++) {
                p.set(vertices[triangles[t]]);
                matrix.transform(p);
                gl.glVertex3f(p.x, p.y, p.z);
            }
            gl.glEnd();
        }
    }

    public void drawOutline(GL gl, Matrix4f matrix) {
        Point3f p = new Point3f();
        gl.glLineWidth(1.5f);
        gl.glDisable(GL_LINE_SMOOTH);
        for (int i = 0; i < normals.length; i++) {
            gl.glBegin(GL_LINE_LOOP);
            for (int t = i * 3; t < i * 3 + 3; t++) {
                p.set(vertices[triangles[t]]);
                matrix.transform(p);
                gl.glVertex3f(p.x, p.y, p.z);
            }
            gl.glEnd();
        }
    }

    private void computeNormals() {
        Vector3f a = new Vector3f();
        Vector3f b = new Vector3f();
        for (int i = 0; i < normals.length; i++) {
            int p0 = i * 3;
            int p1 = p0 + 1;
            int p2 = p0 + 2;
            a.sub(vertices[triangles[p1]], vertices[triangles[p0]]);
            b.sub(vertices[triangles[p2]], vertices[triangles[p0]]);
            normals[i] = new Vector3f();
            normals[i].cross(a, b);
        }
    }
}
