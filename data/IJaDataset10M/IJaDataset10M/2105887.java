package com.hardwire.softphysics;

import javax.microedition.lcdui.Graphics;
import com.hardwire.blob.Game;
import com.hardwire.utils.Vector2;

/**
 * This class store info about a static (but movable) object. The object can be moved only by user. Can't move itself, rotate nor deform.
 */
public class StaticBody {

    /**
   * Positions determining shape of this body. These are relative to the "pos".
   */
    public Vector2[] points;

    /**
   * Position in the world.
   */
    public Vector2 pos;

    private Vector2[] stickedPoints;

    private int stickedPoints_len;

    /**
   * For internal use; use getAABB instead.
   */
    public int[] aabb_local;

    private int[] aabb;

    private SoftWorld world;

    /**
   * An unique ID of this object in the world.
   */
    public int _id;

    /**
   * Creates a new static body.
   * @param pWorld Reference to the world.
   * @param pPoints Shape of this object. Positions are relative to pPos.
   * @param pPos Position of this object.
   */
    public StaticBody(SoftWorld pWorld, Vector2[] pPoints, Vector2 pPos) {
        world = pWorld;
        points = pPoints;
        pos = pPos;
        stickedPoints_len = 0;
        aabb_local = new int[4];
        aabb = new int[4];
        aabb_local[0] = aabb_local[1] = Integer.MAX_VALUE;
        aabb_local[2] = aabb_local[3] = Integer.MIN_VALUE;
        for (int i = 0; i < points.length; i++) {
            if (points[i].x < aabb_local[0]) aabb_local[0] = points[i].x;
            if (points[i].x > aabb_local[2]) aabb_local[2] = points[i].x;
            if (points[i].y < aabb_local[1]) aabb_local[1] = points[i].y;
            if (points[i].y > aabb_local[3]) aabb_local[3] = points[i].y;
        }
    }

    /**
   * Gets the AABB of this object. Does almost no work.
   */
    public int[] getAABB() {
        aabb[0] = aabb_local[0] + pos.x;
        aabb[1] = aabb_local[1] + pos.y;
        aabb[2] = aabb_local[2] + pos.x;
        aabb[3] = aabb_local[3] + pos.y;
        return aabb;
    }

    /**
   * Moves this object by pMove. Does some work.
   */
    public void move(Vector2 pMove) {
        for (int i = 0; i < stickedPoints_len; i++) stickedPoints[i].add(pMove);
        Vector2 pt = new Vector2(pos);
        pos.add(pMove);
        if (world._ig_staticBodies != null) world._ig_staticBodies.updateObject(_id, aabb_local[0] + pt.x, aabb_local[1] + pt.y, aabb_local[2] + pt.x, aabb_local[3] + pt.y, aabb_local[0] + pos.x, aabb_local[1] + pos.y, aabb_local[2] + pos.x, aabb_local[3] + pos.y);
    }

    /**
   * Changes position of this object to pPos.
   */
    public void translate(Vector2 pPos) {
        move(pPos.minus(pos));
    }

    /**
   * For internal use only; sticks a point to this body.
   * 
   * @param pToAdd Position of the point sticked to this body.
   */
    public void _addStickedPoint(Vector2 pToAdd) {
        if (stickedPoints == null) stickedPoints = new Vector2[8]; else if (stickedPoints_len == stickedPoints.length) {
            Vector2[] tmp = new Vector2[stickedPoints_len << 1];
            System.arraycopy(stickedPoints, 0, tmp, 0, stickedPoints_len);
            stickedPoints = tmp;
        }
        stickedPoints[stickedPoints_len++] = pToAdd;
    }

    /**
   * For internal use; clears data holding sticked points.
   */
    public void _removeDeadStickedPoints() {
        for (int i = 0; i < stickedPoints_len; i++) {
            Vector2 pt = stickedPoints[i];
            if (pt.x == Integer.MAX_VALUE) {
                stickedPoints_len--;
                stickedPoints[i] = null;
                if (i != stickedPoints_len) {
                    stickedPoints[i] = stickedPoints[stickedPoints_len];
                    stickedPoints[stickedPoints_len] = null;
                }
                i--;
            }
        }
    }

    /**
   * Draws a shape of this object; useful for debug.
   * 
   * @param pGraph Graphics to draw with.
   * @param pOffset This will be added to each points' position.
   * @param pEdgesColor Color to draw with.
   */
    public void d_drawShape(Graphics pGraph, Vector2 pOffset, int pEdgesColor) {
        int len = points.length;
        int[][] positions = new int[len][2];
        for (int i = 0; i < len; i++) {
            positions[i][0] = pos.x + points[i].x + pOffset.x >> Game.DRAW_SHIFT;
            positions[i][1] = pos.y + points[i].y + pOffset.y >> Game.DRAW_SHIFT;
        }
        pGraph.setColor(pEdgesColor);
        for (int i = 0, j = len - 1; i < len; j = i, i++) pGraph.drawLine(positions[i][0], positions[i][1], positions[j][0], positions[j][1]);
    }

    /**
   * Clears memory allocated by this object.
   */
    public void clean() {
        points = null;
        pos = null;
        stickedPoints = null;
        aabb_local = null;
        aabb = null;
        world = null;
    }
}
