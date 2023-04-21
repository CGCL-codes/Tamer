package org.fpdev.apps.admin.actions;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.fpdev.core.FPEngine;
import org.fpdev.core.Scenario;
import org.fpdev.core.basenet.BLink;
import org.fpdev.core.basenet.BLinkStreet;
import org.fpdev.core.basenet.BNode;
import org.fpdev.core.basenet.Path;
import org.fpdev.core.transit.SubRoute;
import org.fpdev.core.transit.TransitPath;
import org.fpdev.apps.admin.AdminClient;
import org.fpdev.util.FPUtil;

/**
 *
 * @author demory
 */
public class SplitLinkAction implements ACAction {

    private BLink link_, splitLink1_, splitLink2_;

    private BNode splitNode_;

    private double mx_, my_;

    private Map<SubRoute, TransitPath> oldTPaths_;

    private DeleteLinkAction deleteLinkAction_;

    private CreateLinkAction createLink1Action_, createLink2Action_;

    private CreateNodeAction createNodeAction_;

    /** Creates a new instance of SplitLinkAction */
    public SplitLinkAction(BLink link, double mx, double my, FPEngine engine) {
        link_ = link;
        mx_ = mx;
        my_ = my;
        oldTPaths_ = new HashMap<SubRoute, TransitPath>();
        for (Path path : link_.getRegisteredPaths()) {
            if (path.isTransitPath()) {
                TransitPath tPath = (TransitPath) path;
                SubRoute sub = tPath.getSubRoute();
                TransitPath copy = tPath.createCopy(engine, sub, false);
                oldTPaths_.put(sub, copy);
            }
        }
    }

    public String getName() {
        return "Split Link";
    }

    public boolean doAction(AdminClient ac) {
        double u = 0, ix = 0, iy = 0;
        List<Point2D.Double> shpPts1 = new LinkedList<Point2D.Double>();
        List<Point2D.Double> shpPts2 = new LinkedList<Point2D.Double>();
        if (!link_.getShapePoints().hasNext()) {
            double x1 = link_.getX1(), y1 = link_.getY1(), x2 = link_.getX2(), y2 = link_.getY2();
            double lineMag = FPUtil.magnitude(x1, y1, x2, y2);
            u = ((mx_ - x1) * (x2 - x1) + (my_ - y1) * (y2 - y1)) / (lineMag * lineMag);
            System.out.println("u=" + u);
            if (u > Double.MIN_VALUE && u <= 1) {
                ix = x1 + u * (x2 - x1);
                iy = y1 + u * (y2 - y1);
            } else {
                ac.msg("Invalid split coordinates");
                return false;
            }
        } else {
            List<Point2D.Double> points = new LinkedList<Point2D.Double>();
            Iterator<Point2D.Double> shpPts = link_.getShapePoints();
            while (shpPts.hasNext()) {
                points.add(shpPts.next());
            }
            points.add(new Point2D.Double(link_.getX2(), link_.getY2()));
            double x1 = link_.getX1(), y1 = link_.getY1(), x2, y2;
            Iterator<Point2D.Double> ptIter = points.iterator();
            int iseg = 0;
            boolean success = false;
            while (ptIter.hasNext()) {
                Point2D.Double pt = ptIter.next();
                x2 = pt.getX();
                y2 = pt.getY();
                double lineMag = FPUtil.magnitude(x1, y1, x2, y2);
                double useg = ((mx_ - x1) * (x2 - x1) + (my_ - y1) * (y2 - y1)) / (lineMag * lineMag);
                ix = x1 + useg * (x2 - x1);
                iy = y1 + useg * (y2 - y1);
                double distToSeg = FPUtil.magnitude(ix, iy, mx_, my_);
                if (useg > Double.MIN_VALUE && useg <= 1 && distToSeg < ac.getGUI().getMapPanel().getClickToleranceMapUnits()) {
                    u += (lineMag / link_.getMeasuredLength()) * useg;
                    shpPts2.add(pt);
                    for (int i = iseg; i < points.size() - 1; i++) {
                        shpPts2.add(ptIter.next());
                    }
                    success = true;
                    break;
                } else {
                    shpPts1.add(pt);
                    u += (lineMag / link_.getMeasuredLength());
                }
                x1 = x2;
                y1 = y2;
                iseg++;
            }
            if (!success) {
                ac.msg("cannot split link");
                return false;
            }
        }
        BNode fNode = ac.getEngine().getNodeFromID(link_.getFNodeID());
        BNode tNode = ac.getEngine().getNodeFromID(link_.getTNodeID());
        createNodeAction_ = ac.getNetworkOps().newNode(ix, iy, true);
        splitNode_ = createNodeAction_.getNode();
        Scenario scen = link_.getScenario();
        int linkType = link_.getType();
        String name = link_.getDisplayName();
        double len1 = 0, len2 = 0;
        if (!link_.getShapePoints().hasNext() && link_.getLengthFeet() > 0) {
            double len = link_.getLengthFeet();
            len1 = u * len;
            len2 = (1 - u) * len;
        }
        createLink1Action_ = ac.getNetworkOps().newLink(0, name, scen, fNode, splitNode_, linkType, shpPts1, len1, true);
        splitLink1_ = createLink1Action_.getLink();
        createLink2Action_ = ac.getNetworkOps().newLink(0, name, scen, splitNode_, tNode, linkType, shpPts2, len2, true);
        splitLink2_ = createLink2Action_.getLink();
        if (link_.getClassType() == BLink.CLASS_STREET) {
            ((BLinkStreet) splitLink1_).setParentID(((BLinkStreet) link_).getParentID());
            ((BLinkStreet) splitLink2_).setParentID(((BLinkStreet) link_).getParentID());
            int fAddrL = 0, fAddrR = 0, tAddrL = 0, tAddrR = 0;
            double mAddrL = 0, mAddrR = 0;
            fAddrL = ((BLinkStreet) link_).getFAddrL();
            fAddrR = ((BLinkStreet) link_).getFAddrR();
            tAddrL = ((BLinkStreet) link_).getTAddrL();
            tAddrR = ((BLinkStreet) link_).getTAddrR();
            if (fAddrL < tAddrL) {
                mAddrL = (double) fAddrL + u * (tAddrL - fAddrL);
                mAddrR = (double) fAddrR + u * (tAddrR - fAddrR);
            } else {
                mAddrL = (double) tAddrL + (1 - u) * (fAddrL - tAddrL);
                mAddrR = (double) tAddrR + (1 - u) * (fAddrR - tAddrR);
            }
            int mAddrLi = (int) mAddrL, mAddrRi = (int) mAddrR;
            if (mAddrLi % 2 != fAddrL % 2) mAddrLi++;
            if (mAddrRi % 2 != fAddrR % 2) mAddrRi++;
            ((BLinkStreet) splitLink1_).setAddressRange(fAddrL, fAddrR, mAddrLi, mAddrRi);
            ((BLinkStreet) splitLink2_).setAddressRange(mAddrLi, mAddrRi, tAddrL, tAddrR);
            ((BLinkStreet) splitLink1_).setZips(((BLinkStreet) link_).getZipL(), ((BLinkStreet) link_).getZipR());
            ((BLinkStreet) splitLink2_).setZips(((BLinkStreet) link_).getZipL(), ((BLinkStreet) link_).getZipR());
        }
        for (Path path : link_.getRegisteredPaths()) {
            splitLink1_.registerPath(path);
            splitLink2_.registerPath(path);
        }
        for (SubRoute sub : oldTPaths_.keySet()) sub.getPath().splitLink(link_, splitLink1_, splitLink2_);
        if (!oldTPaths_.isEmpty()) ac.getEngine().getRoutes().changesMade();
        deleteLinkAction_ = ac.getNetworkOps().deleteLink(link_, true);
        return true;
    }

    public boolean undoAction(AdminClient ac) {
        for (Map.Entry<SubRoute, TransitPath> entry : oldTPaths_.entrySet()) {
            entry.getKey().setPath(entry.getValue().createCopy(ac.getEngine(), false));
            ac.getGUI().getMapPanel().getDrawItems().transitPathItemUpdated(entry.getKey().getPath());
        }
        boolean success = true;
        success = success && createLink1Action_.undoAction(ac);
        success = success && createLink2Action_.undoAction(ac);
        success = success && createNodeAction_.undoAction(ac);
        success = success && deleteLinkAction_.undoAction(ac);
        return success;
    }

    public BLink getSplitLink1() {
        return splitLink1_;
    }

    public BLink getSplitLink2() {
        return splitLink2_;
    }
}
