package com.aionemu.gameserver.model.geometry;

import java.awt.Point;
import com.aionemu.gameserver.utils.MathUtil;

/**
 * This class implements cylinder area
 * 
 * @author SoulKeeper
 */
public class CylinderArea extends AbstractArea {

    /**
	 * Center of cylinder
	 */
    private final int centerX;

    /**
	 * Center of cylinder
	 */
    private final int centerY;

    /**
	 * Cylinder radius
	 */
    private final int radius;

    /**
	 * Creates new cylinder with given radius
	 * 
	 * @param center
	 *            center of the circle
	 * @param radius
	 *            radius of the circle
	 * @param minZ
	 *            min z
	 * @param maxZ
	 *            max z
	 */
    public CylinderArea(Point center, int radius, int minZ, int maxZ) {
        this(center.x, center.y, radius, minZ, maxZ);
    }

    /**
	 * Creates new cylider with given radius
	 * 
	 * @param x
	 *            center coord
	 * @param y
	 *            center coord
	 * @param radius
	 *            radius of the circle
	 * @param minZ
	 *            min z
	 * @param maxZ
	 *            max z
	 */
    public CylinderArea(int x, int y, int radius, int minZ, int maxZ) {
        super(minZ, maxZ);
        this.centerX = x;
        this.centerY = y;
        this.radius = radius;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean isInside2D(int x, int y) {
        return MathUtil.getDistance(centerX, centerY, x, y) < radius;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public double getDistance2D(int x, int y) {
        if (isInside2D(x, y)) {
            return 0;
        } else {
            return Math.abs(MathUtil.getDistance(centerX, centerY, x, y) - radius);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public double getDistance3D(int x, int y, int z) {
        if (isInside3D(x, y, z)) {
            return 0;
        } else if (isInsideZ(z)) {
            return getDistance2D(x, y);
        } else {
            if (z < getMinZ()) {
                return MathUtil.getDistance(centerX, centerY, getMinZ(), x, y, z);
            } else {
                return MathUtil.getDistance(centerX, centerY, getMaxZ(), x, y, z);
            }
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Point getClosestPoint(int x, int y) {
        if (isInside2D(x, y)) {
            return new Point(x, y);
        } else {
            int vX = x - this.centerX;
            int vY = y - this.centerY;
            double magV = MathUtil.getDistance(centerX, centerY, x, y);
            double pointX = centerX + vX / magV * radius;
            double pointY = centerY + vY / magV * radius;
            return new Point((int) Math.round(pointX), (int) Math.round(pointY));
        }
    }
}
