package com.ynhenc.droute.map;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import com.ynhenc.droute.*;
import com.ynhenc.droute.map.square.GeoSquare;
import com.ynhenc.droute.render.IncludeInfo;

public abstract class Map extends ComLibRoute {

    public Mbr getMbr() {
        if (this.mbr == null) {
            this.mbr = this.calcMbr();
        }
        return this.mbr;
    }

    private final Mbr calcMbr() {
        Mbr mbr = null;
        MbrInterface[] objs = this.getNodeIndexer().getAllIndexElments();
        for (MbrInterface obj : objs) {
            if (obj != null) {
                mbr = Mbr.union(mbr, obj.getMbr());
            }
        }
        if (mbr != null) {
            mbr = Mbr.getMbrWithMargin(mbr, 0.01);
        }
        return mbr;
    }

    public Projection getProjection(Dimension panelSize) {
        if (this.projection == null || !panelSize.equals(this.projection.getPixelSize())) {
            this.projection = Projection.getProject(this.getMbr(), panelSize);
        }
        return this.projection;
    }

    public int getIndexSize() {
        NodeIndexer nodeIndexer = this.nodeIndexer;
        if (nodeIndexer == null) {
            return -1;
        } else {
            return nodeIndexer.getSize();
        }
    }

    public LinkList getLinkList() {
        return this.linkList;
    }

    public LinkGroupIndexer getLinkGroupIndexer() {
        return this.linkGroupIndexer;
    }

    public final NodeIndexer getNodeIndexer() {
        return this.nodeIndexer;
    }

    public final Node getNodeNearest(Vert vert, Find.Mode findMode) {
        Dimension nodeSize = this.getNodeSize();
        IncludeInfo inclInfo = new IncludeInfo(vert, nodeSize);
        double distMin = Double.MAX_VALUE;
        double dist;
        Node nodeMin = null;
        GeoObject geo;
        for (Node node : nodeIndexer.getAllIndexElments()) {
            geo = node.getGeoObject();
            dist = geo.getPhysicalDist(vert, findMode);
            if (dist < distMin) {
                distMin = dist;
                nodeMin = node;
            }
        }
        return nodeMin;
    }

    public final Node getNodeAt(IncludeInfo inclInfo) {
        for (Node node : nodeIndexer.getAllIndexElments()) {
            if (node.isIncluded(inclInfo)) {
                return node;
            }
        }
        return null;
    }

    public final LinkGroup getLinkGroup(Node fromNode) {
        return this.linkGroupIndexer.get(fromNode.getIndex());
    }

    public final void setLinkGroup(Node node, LinkGroup linkGroup) {
        this.linkGroupIndexer.add(linkGroup);
    }

    public final void createWorld(int indexSize) throws Exception {
        this.indexSize = indexSize;
        this.nodeIndexer = this.createNodeIndexer();
        this.createLinkList();
        this.buildLink();
    }

    public Node getNodeAt(int index) {
        return this.nodeIndexer.get(index);
    }

    public final void buildLink() throws Exception {
        boolean localDebug = true;
        int indexSize = this.getIndexSize();
        this.debug("IndexSize = " + indexSize, localDebug);
        LinkGroupIndexer linkGroupIndexer = new LinkGroupIndexer(indexSize);
        LinkList linkList = this.getLinkList();
        Link fromLink, toLink;
        for (int i = 0, iLen = linkList.getSize(); i < iLen; ) {
            fromLink = linkList.get(i);
            int k = i + 1;
            for (int kLen = iLen; k < kLen; ) {
                toLink = linkList.get(k);
                if (fromLink.getFromNode() == toLink.getFromNode()) {
                    k++;
                } else {
                    kLen = -1;
                }
            }
            linkGroupIndexer.add(new LinkGroup(fromLink.getFromNode(), i, k));
            i = k;
        }
        this.linkGroupIndexer = linkGroupIndexer;
    }

    public final void buildNode() {
    }

    public PathFinder getPathFinder(SrchOption srchOption) {
        return PathFinder.getNewPathFinder(srchOption);
    }

    public abstract NodeIndexer createNodeIndexer() throws Exception;

    public abstract void createLinkList() throws Exception;

    public abstract void updateLinkRealTimeInfo() throws Exception;

    public abstract Dimension getNodeSize();

    protected Dimension nodeSize;

    private int indexSize;

    private NodeIndexer nodeIndexer;

    protected LinkList linkList;

    private Mbr mbr;

    private Projection projection;

    private transient LinkGroupIndexer linkGroupIndexer;
}
