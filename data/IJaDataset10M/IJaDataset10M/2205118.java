package com.calefay.utils;

import com.jme.scene.shape.Quad;

public class QuadGenerator {

    public enum VerticalAlignment {

        TOP, MIDDLE, BOTTOM
    }

    ;

    public enum HorizontalAlignment {

        LEFT, MIDDLE, RIGHT
    }

    ;

    public enum HorizVertAxes {

        XY, XZ, ZY
    }

    public static Quad getQuad(String name, float width, float height, HorizontalAlignment ha, VerticalAlignment va, HorizVertAxes qa) {
        Quad q = new Quad(name, width, height);
        float tlx = 0, tly = 0, brx = 0, bry = 0;
        switch(va) {
            case TOP:
                tly = 0;
                bry = height;
                break;
            case MIDDLE:
                tly = -height / 2.0f;
                bry = height / 2.0f;
                break;
            case BOTTOM:
                tly = height;
                bry = 0;
                break;
        }
        switch(ha) {
            case LEFT:
                tlx = 0;
                brx = width;
                break;
            case MIDDLE:
                tlx = -width / 2.0f;
                brx = width / 2.0f;
                break;
            case RIGHT:
                tlx = -width;
                brx = 0;
                break;
        }
        q.getVertexBuffer().clear();
        switch(qa) {
            case XY:
                q.getVertexBuffer().put(tlx).put(tly).put(0);
                q.getVertexBuffer().put(tlx).put(bry).put(0);
                q.getVertexBuffer().put(brx).put(bry).put(0);
                q.getVertexBuffer().put(brx).put(tly).put(0);
                q.getNormalBuffer().clear();
                q.getNormalBuffer().put(0).put(0).put(-1);
                q.getNormalBuffer().put(0).put(0).put(-1);
                q.getNormalBuffer().put(0).put(0).put(-1);
                q.getNormalBuffer().put(0).put(0).put(-1);
                break;
            case XZ:
                q.getVertexBuffer().put(tlx).put(0).put(tly);
                q.getVertexBuffer().put(tlx).put(0).put(bry);
                q.getVertexBuffer().put(brx).put(0).put(bry);
                q.getVertexBuffer().put(brx).put(0).put(tly);
                q.getNormalBuffer().clear();
                q.getNormalBuffer().put(0).put(-1).put(0);
                q.getNormalBuffer().put(0).put(-1).put(0);
                q.getNormalBuffer().put(0).put(-1).put(0);
                q.getNormalBuffer().put(0).put(-1).put(0);
                break;
            case ZY:
                q.getVertexBuffer().put(0).put(tly).put(tlx);
                q.getVertexBuffer().put(0).put(tly).put(brx);
                q.getVertexBuffer().put(0).put(bry).put(brx);
                q.getVertexBuffer().put(0).put(bry).put(tlx);
                q.getNormalBuffer().clear();
                q.getNormalBuffer().put(1).put(0).put(0);
                q.getNormalBuffer().put(1).put(0).put(0);
                q.getNormalBuffer().put(1).put(0).put(0);
                q.getNormalBuffer().put(1).put(0).put(0);
                break;
        }
        return q;
    }
}
