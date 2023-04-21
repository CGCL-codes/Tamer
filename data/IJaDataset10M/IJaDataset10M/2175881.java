package com.peterhi.classroom;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import com.peterhi.runtime.Util;

public class MyJoglRadialGradientByCuttingExample2 extends MyJoglExampleTemplate {

    public static void main(String[] args) {
        new MyJoglRadialGradientByCuttingExample2().run();
    }

    private double[][] triangleVertices = new double[][] { new double[] { -2.0, 2.0 }, new double[] { -2.0, -2.0 }, new double[] { 2.0, -2.0 } };

    private double[] circleCenter = new double[] { -1.95, 1.9 };

    private double radius = 1.0;

    private int sectorDegrees = 10;

    private int triangle;

    private int circle;

    private boolean debug = true;

    private double rotation;

    public MyJoglRadialGradientByCuttingExample2() {
    }

    protected void onInit(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        triangle = gl.glGenLists(1);
        gl.glNewList(triangle, GL.GL_COMPILE);
        gl.glBegin(GL.GL_TRIANGLES);
        if (debug) {
            gl.glColor3d(0.2, 0.2, 0.2);
        } else {
            gl.glColor3d(0.0, 1.0, 0.0);
        }
        for (double[] triangleVertex : triangleVertices) {
            gl.glVertex3d(triangleVertex[0], triangleVertex[1], 0.0);
        }
        gl.glEnd();
        gl.glEndList();
        List<double[]> triangles = new ArrayList<double[]>();
        double[] nextCoordsModifier = null;
        boolean insert = false;
        for (int i = 0 / sectorDegrees; i < 360 / sectorDegrees; i++) {
            double x0 = circleCenter[0];
            double y0 = circleCenter[1];
            double x1 = circleCenter[0] + Math.cos(Math.toRadians(i * sectorDegrees)) * radius;
            double y1 = circleCenter[1] + Math.sin(Math.toRadians(i * sectorDegrees)) * radius;
            double x2 = circleCenter[0] + Math.cos(Math.toRadians((i + 1) * sectorDegrees)) * radius;
            double y2 = circleCenter[1] + Math.sin(Math.toRadians((i + 1) * sectorDegrees)) * radius;
            if (nextCoordsModifier != null) {
                x1 = nextCoordsModifier[0];
                y1 = nextCoordsModifier[1];
                nextCoordsModifier = null;
            }
            int intersectionALineIndex = 0;
            double[] intersectionA = getIntersectionFromCenter(x0, y0, x1, y1, triangleVertices[0][0], triangleVertices[0][1], triangleVertices[1][0], triangleVertices[1][1]);
            if (intersectionA == null) {
                intersectionALineIndex = 1;
                intersectionA = getIntersectionFromCenter(x0, y0, x1, y1, triangleVertices[1][0], triangleVertices[1][1], triangleVertices[2][0], triangleVertices[2][1]);
            }
            if (intersectionA == null) {
                intersectionALineIndex = 2;
                intersectionA = getIntersectionFromCenter(x0, y0, x1, y1, triangleVertices[2][0], triangleVertices[2][1], triangleVertices[0][0], triangleVertices[0][1]);
            }
            int intersectionBLineIndex = 0;
            double[] intersectionB = getIntersectionFromCenter(x0, y0, x2, y2, triangleVertices[0][0], triangleVertices[0][1], triangleVertices[1][0], triangleVertices[1][1]);
            if (intersectionB == null) {
                intersectionBLineIndex = 1;
                intersectionB = getIntersectionFromCenter(x0, y0, x2, y2, triangleVertices[1][0], triangleVertices[1][1], triangleVertices[2][0], triangleVertices[2][1]);
            }
            if (intersectionB == null) {
                intersectionBLineIndex = 2;
                intersectionB = getIntersectionFromCenter(x0, y0, x2, y2, triangleVertices[2][0], triangleVertices[2][1], triangleVertices[0][0], triangleVertices[0][1]);
            }
            if (intersectionA != null && intersectionB != null) {
                if (intersectionALineIndex != intersectionBLineIndex) {
                    x1 = triangleVertices[intersectionBLineIndex][0];
                    y1 = triangleVertices[intersectionBLineIndex][1];
                    x2 = intersectionB[0];
                    y2 = intersectionB[1];
                    triangles.add(new double[] { x0, y0, intersectionA[0], intersectionA[1], triangleVertices[intersectionBLineIndex][0], triangleVertices[intersectionBLineIndex][1] });
                } else {
                    x1 = intersectionA[0];
                    y1 = intersectionA[1];
                    x2 = intersectionB[0];
                    y2 = intersectionB[1];
                }
            } else {
                double[] intersectionC = Util.segmentIntersect(x1, y1, x2, y2, triangleVertices[0][0], triangleVertices[0][1], triangleVertices[1][0], triangleVertices[1][1]);
                if (intersectionC == null) {
                    intersectionC = Util.segmentIntersect(x1, y1, x2, y2, triangleVertices[1][0], triangleVertices[1][1], triangleVertices[2][0], triangleVertices[2][1]);
                }
                if (intersectionC == null) {
                    intersectionC = Util.segmentIntersect(x1, y1, x2, y2, triangleVertices[2][0], triangleVertices[2][1], triangleVertices[0][0], triangleVertices[0][1]);
                }
                if (intersectionA != null) {
                    if (intersectionC == null) {
                        System.out.println("intersectionA / intersectionC");
                        continue;
                    }
                    x1 = intersectionC[0];
                    y1 = intersectionC[1];
                    if (triangles.isEmpty()) {
                        insert = true;
                    } else {
                        double[] triangle = triangles.get(triangles.size() - 1);
                        triangle[4] = x1;
                        triangle[5] = y1;
                    }
                }
                if (intersectionB != null) {
                    if (intersectionC == null) {
                        System.out.println("intersectionB / intersectionC");
                        continue;
                    }
                    x2 = intersectionC[0];
                    y2 = intersectionC[1];
                    nextCoordsModifier = new double[] { x2, y2 };
                    nextCoordsModifier[0] = x2;
                    nextCoordsModifier[1] = y2;
                }
            }
            triangles.add(new double[] { x0, y0, x1, y1, x2, y2 });
        }
        if (insert) {
            double[] first = triangles.get(0);
            double[] last = triangles.get(triangles.size() - 1);
            triangles.add(new double[] { circleCenter[0], circleCenter[1], last[4], last[5], first[2], first[3] });
        }
        circle = gl.glGenLists(1);
        gl.glNewList(circle, GL.GL_COMPILE);
        gl.glBegin(GL.GL_TRIANGLES);
        for (int i = 0; i < triangles.size(); i++) {
            double[] triangle = triangles.get(i);
            double[] color = indexToColor(i);
            gl.glColor3d(color[0], color[1], color[2]);
            gl.glVertex3d(triangle[0], triangle[1], 0.0);
            gl.glVertex3d(triangle[2], triangle[3], 0.0);
            gl.glVertex3d(triangle[4], triangle[5], 0.0);
        }
        gl.glEnd();
        gl.glEndList();
    }

    protected void onDisplay(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glLoadIdentity();
        gl.glTranslated(0.0, 0.0, -5.0);
        gl.glRotated(rotation, 0.0, 1.0, 0.0);
        gl.glCallList(triangle);
        gl.glLoadIdentity();
        gl.glTranslated(0.0, 0.0, -4.99);
        gl.glRotated(rotation, 0.0, 1.0, 0.0);
        gl.glCallList(circle);
    }

    private double[] indexToColor(int index) {
        double[] ret = new double[3];
        if (index % 4 == 1) {
            ret[0] = 1.0;
            ret[1] = 1.0;
            ret[2] = 0.0;
        } else if (index % 4 == 2) {
            ret[0] = 0.0;
            ret[1] = 0.0;
            ret[2] = 1.0;
        } else if (index % 4 == 3) {
            ret[0] = 0.0;
            ret[1] = 1.0;
            ret[2] = 0.0;
        } else {
            ret[0] = 1.0;
            ret[1] = 0.0;
            ret[2] = 0.0;
        }
        return ret;
    }

    private double[] getIntersectionFromCenter(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3) {
        return Util.segmentIntersect(x0, y0, x1, y1, x2, y2, x3, y3);
    }
}
