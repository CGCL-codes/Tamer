package net.sf.pim.game.util;

import org.eclipse.swt.graphics.Point;

/**
 * 对diamond map的核心处理,包括TilePlotter/TileWalker/MouseMap
 * @author Administrator
 *
 */
public class DiamondMap {

    public enum IsoDirection {

        NORTH, NORTH_EAST, EAST, NORTH_WEST, CENTER, SOUTH_EAST, WEST, SOUTH_WEST, SOUTH
    }

    /**
	 * 将地图坐标转化为世界(屏幕)坐标
	 * @param ptMap
	 * @param tileWidth
	 * @param tileHeight
	 * @param offsetX
	 * @param offsetY
	 * @return 
	 */
    public static Point tilePlotter(Point ptMap, int tileWidth, int tileHeight, int offsetX, int offsetY) {
        Point worldPoint = new Point(0, 0);
        worldPoint.x = (ptMap.x - ptMap.y) * tileWidth / 2 + offsetX;
        worldPoint.y = (ptMap.x + ptMap.y) * tileHeight / 2 + offsetY;
        return worldPoint;
    }

    /**
	 * 处理地图的移动
	 * @param ptStart
	 * @param direction
	 * @return
	 */
    public static Point tileWalker(Point ptStart, IsoDirection direction) {
        switch(direction) {
            case NORTH:
                ptStart.x--;
                ptStart.y--;
                break;
            case NORTH_EAST:
                ptStart.y--;
                break;
            case EAST:
                ptStart.x++;
                ptStart.y--;
                break;
            case SOUTH_EAST:
                ptStart.x++;
                break;
            case SOUTH:
                ptStart.x++;
                ptStart.y++;
                break;
            case SOUTH_WEST:
                ptStart.y++;
                break;
            case WEST:
                ptStart.x--;
                ptStart.y++;
                break;
            case NORTH_WEST:
                ptStart.x--;
                break;
        }
        return ptStart;
    }

    /**
	 * 将世界(屏幕)坐标转化为地图坐标
	 * @param ptMouse
	 * @param tileWidth
	 * @param tileHeight
	 * @param offsetX
	 * @param offsetY
	 * @return
	 */
    public static Point mouseMapper(Point ptMouse, int tileWidth, int tileHeight, int offsetX, int offsetY) {
        Point ptPlot = tilePlotter(new Point(0, 0), tileWidth, tileHeight, offsetX, offsetY);
        ptMouse.x -= ptPlot.x;
        ptMouse.y -= ptPlot.y;
        Point ptMouseMapCoarse = new Point(0, 0);
        ptMouseMapCoarse.x = ptMouse.x / tileWidth;
        ptMouseMapCoarse.y = ptMouse.y / tileHeight;
        Point ptMouseFine = new Point(0, 0);
        ptMouseFine.x = ptMouse.x % tileWidth;
        ptMouseFine.y = ptMouse.y % tileHeight;
        if (ptMouseFine.x < 0) {
            ptMouseFine.x += tileWidth;
            ptMouseMapCoarse.x--;
        }
        if (ptMouseFine.y < 0) {
            ptMouseFine.y += tileHeight;
            ptMouseMapCoarse.y--;
        }
        Point ptMap = new Point(0, 0);
        while (ptMouseMapCoarse.y < 0) {
            ptMap = tileWalker(ptMap, IsoDirection.NORTH);
            ptMouseMapCoarse.y++;
        }
        while (ptMouseMapCoarse.y > 0) {
            ptMap = tileWalker(ptMap, IsoDirection.SOUTH);
            ptMouseMapCoarse.y--;
        }
        while (ptMouseMapCoarse.x < 0) {
            ptMap = tileWalker(ptMap, IsoDirection.WEST);
            ptMouseMapCoarse.x++;
        }
        while (ptMouseMapCoarse.x > 0) {
            ptMap = tileWalker(ptMap, IsoDirection.EAST);
            ptMouseMapCoarse.x--;
        }
        if (!checkPosition(ptMouseFine.y, ptMouseFine.x).equals(IsoDirection.CENTER)) {
            ptMap = tileWalker(ptMap, checkPosition(ptMouseFine.y, ptMouseFine.x));
        }
        return ptMap;
    }

    private static IsoDirection checkPosition(int y1, int x1) {
        if (x1 < 24 && y1 < 12 && y1 < -0.5 * x1 + 12) {
            return IsoDirection.NORTH_WEST;
        } else if (x1 > 24 && y1 < 12 && y1 < 0.5 * x1 - 12) {
            return IsoDirection.NORTH_EAST;
        } else if (x1 > 24 && y1 > 12 && y1 > -0.5 * x1 + 36) {
            return IsoDirection.SOUTH_EAST;
        } else if (x1 < 24 && y1 > 12 && y1 > 0.5 * x1 + 12) {
            return IsoDirection.SOUTH_WEST;
        } else return IsoDirection.CENTER;
    }

    /**
	 * 取next相对于origin的方位，先只判断能行走的4个方向
	 * @param origin
	 * @param next
	 * @return
	 */
    public static IsoDirection getDirection(Point origin, Point next) {
        IsoDirection direction = IsoDirection.SOUTH_WEST;
        if (origin.x > next.x) direction = IsoDirection.NORTH_EAST; else if (origin.y > next.y) direction = IsoDirection.NORTH_WEST; else if (origin.y < next.y) direction = IsoDirection.SOUTH_EAST;
        return direction;
    }

    /**
	 * 改变行动方向，-99为反身，1为逆时针，-1为顺时针
	 * @param delta
	 * @param direction
	 * @return
	 */
    public static IsoDirection changeDirection(int delta, IsoDirection direction) {
        if (delta == -99) {
            switch(direction) {
                case NORTH_EAST:
                    direction = IsoDirection.SOUTH_WEST;
                    break;
                case SOUTH_WEST:
                    direction = IsoDirection.NORTH_EAST;
                    break;
                case NORTH_WEST:
                    direction = IsoDirection.SOUTH_EAST;
                    break;
                case SOUTH_EAST:
                    direction = IsoDirection.NORTH_WEST;
            }
        } else if (delta == 1) {
            switch(direction) {
                case NORTH_EAST:
                    direction = IsoDirection.NORTH_WEST;
                    break;
                case SOUTH_WEST:
                    direction = IsoDirection.SOUTH_EAST;
                    break;
                case NORTH_WEST:
                    direction = IsoDirection.SOUTH_WEST;
                    break;
                case SOUTH_EAST:
                    direction = IsoDirection.NORTH_EAST;
            }
        } else if (delta == -1) {
            switch(direction) {
                case NORTH_EAST:
                    direction = IsoDirection.SOUTH_EAST;
                    break;
                case SOUTH_WEST:
                    direction = IsoDirection.NORTH_WEST;
                    break;
                case NORTH_WEST:
                    direction = IsoDirection.NORTH_EAST;
                    break;
                case SOUTH_EAST:
                    direction = IsoDirection.SOUTH_WEST;
            }
        }
        return direction;
    }
}
