package net.rptools.maptool.client.ui.zone.vbl;

import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import net.rptools.maptool.util.GraphicsUtil;

public class AreaTree {

    private AreaOcean theOcean;

    public AreaTree(Area area) {
        digest(area);
    }

    public AreaOcean getOceanAt(Point2D point) {
        return theOcean.getDeepestOceanAt(point);
    }

    AreaOcean getOcean() {
        return theOcean;
    }

    private void digest(Area area) {
        if (area == null) {
            return;
        }
        List<AreaOcean> oceanList = new ArrayList<AreaOcean>();
        List<AreaIsland> islandList = new ArrayList<AreaIsland>();
        float[] coords = new float[6];
        AreaMeta areaMeta = new AreaMeta();
        for (PathIterator iter = area.getPathIterator(null); !iter.isDone(); iter.next()) {
            int type = iter.currentSegment(coords);
            switch(type) {
                case PathIterator.SEG_CLOSE:
                    {
                        areaMeta.close();
                        if (areaMeta.isHole()) {
                            oceanList.add(new AreaOcean(areaMeta));
                        } else {
                            islandList.add(new AreaIsland(areaMeta));
                        }
                        break;
                    }
                case PathIterator.SEG_LINETO:
                    {
                        areaMeta.addPoint(coords[0], coords[1]);
                        break;
                    }
                case PathIterator.SEG_MOVETO:
                    {
                        areaMeta = new AreaMeta();
                        areaMeta.addPoint(coords[0], coords[1]);
                        break;
                    }
            }
        }
        for (AreaOcean ocean : oceanList) {
            AreaIsland island = findSmallestContainer(ocean, islandList);
            if (island == null) {
                System.err.println("Weird, I couldn't find an island for an ocean");
                continue;
            }
            island.addOcean(ocean);
        }
        List<AreaIsland> globalIslandList = new ArrayList<AreaIsland>();
        for (AreaIsland island : islandList) {
            AreaOcean ocean = findSmallestContainer(island, oceanList);
            if (ocean == null) {
                globalIslandList.add(island);
                continue;
            }
            ocean.addIsland(island);
        }
        theOcean = new AreaOcean(null);
        for (AreaIsland island : globalIslandList) {
            theOcean.addIsland(island);
        }
    }

    private <T extends AreaContainer> T findSmallestContainer(AreaContainer item, List<T> list) {
        T smallest = null;
        for (T container : list) {
            if (!GraphicsUtil.contains(container.getBounds(), item.getBounds())) {
                continue;
            }
            smallest = getSmallest(smallest, container);
        }
        return smallest;
    }

    private <T extends AreaContainer> T getSmallest(T left, T right) {
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }
        double leftSize = left.getBounds().getBounds().getWidth() * left.getBounds().getBounds().getHeight();
        double rightSize = right.getBounds().getBounds().getWidth() * right.getBounds().getBounds().getHeight();
        return leftSize < rightSize ? left : right;
    }
}
