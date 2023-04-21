package org.fpdev.apps.admin.gui.map;

import org.fpdev.util.gui.MapCoordinates;
import org.fpdev.util.FPUtil;
import org.fpdev.apps.admin.gui.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import org.fpdev.core.basenet.BLink;
import org.fpdev.core.basenet.BNode;
import org.fpdev.core.basenet.Path;
import org.fpdev.core.transit.Station;
import org.fpdev.apps.admin.AdminClient;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import javax.swing.*;
import org.fpdev.core.basenet.BaseNetGraph;
import org.fpdev.core.basenet.BLinkStreet;
import org.fpdev.core.transit.SubRoute;
import org.fpdev.core.transit.TimePoint;
import org.fpdev.apps.admin.ACEvent;
import org.fpdev.apps.admin.EventTypes;
import org.jgrapht.traverse.ClosestFirstIterator;

/**
 * The central class for the interactive map GUI component. One of the largest
 * and most complex classes in the project, some attempt has been made to break
 * the class into more managable components (MapCanvas, etc.), but more work
 * to this end is still needed.
 * 
 * @author demory
 */
public class MapPanel extends JPanel implements MouseWheelListener {

    private AdminClient ac_;

    private ACGUI gui_;

    private volatile MapCanvas canvas_;

    private MapLayers layers_;

    private MapDrawItems drawItems_;

    private MapContextMenuManager cmManager_;

    private double initX_, initY_, initRes_;

    private boolean inStartup_;

    private boolean isStationary_;

    private boolean animating_;

    private boolean drawStreetNet_, drawRouteNet_;

    private int width_, height_, clickTolerance_;

    private double zoomInFactor_ = .2, zoomOutFactor_ = .25;

    private int mx_, my_, dragX_, dragY_;

    private int cmx_, cmy_;

    private int rsx1_, rsy1_, rsx2_, rsy2_;

    private int recenterX_, recenterY_;

    private BufferedImage bgImg_, baseImg_, fgImg_;

    private boolean updateBG_, updateBase_, updateFG_;

    private volatile MapBuffer buffer_;

    private MapBufferWorker bufferWorker_;

    private boolean bufferAdjacentTiles_;

    private Collection<BNode> visibleNodes_;

    private Collection<BLink> visibleLinks_;

    private BNode newLinkStart_, newLinkEnd_;

    private BNode mergeNode1_, mergeNode2_;

    private BNode firstNode_;

    private BNode draggingNode_;

    private BLink draggingSPLink_;

    private int draggingSPIndex_;

    private SelectedLinks selLinks_;

    private SelectedNodes selNodes_;

    private boolean drawHoverNode_;

    private boolean ignoreNodeHover_;

    private BNode hoverNode_;

    private Set<BNode> traceHoverDestNodes_;

    private NodeHoverListener nodeHoverListener_;

    private boolean drawHoverLinks_, traceHoverPath_;

    private BLink hoverLink_;

    private Set<BLink> hoverLinks_;

    private List<BLink> tracedHoverPath_;

    private Set<BLink> baseTraceLinks_;

    private LinkHoverListener linkHoverListener_;

    private Set<PointOfInterest> pointsOfInterest_;

    private PointOfInterest hoverPOI_;

    private Rectangle.Double hoverAnnotation_;

    private Timer mouseWheelTimer_;

    private boolean mouseWheelMoving_, recentering_;

    private int mouseWheelFactor_;

    enum DragMode {

        NONE, RANGESEL, PANNING, DRAG_NODE, DRAG_SHPPT
    }

    private DragMode dragMode_;

    private BufferedImage capture_;

    private String resizeToDrawableKey_;

    public MapPanel(AdminClient ac, ACGUI gui, double x, double y, double res) {
        ac_ = ac;
        gui_ = gui;
        width_ = height_ = 0;
        inStartup_ = true;
        initX_ = x;
        initY_ = y;
        initRes_ = res;
        animating_ = recentering_ = false;
        updateBG_ = updateBase_ = updateFG_ = isStationary_ = false;
        clickTolerance_ = 5;
        dragMode_ = DragMode.NONE;
        mouseWheelTimer_ = new Timer(500, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mouseWheelZoom();
            }
        });
        drawItems_ = new MapDrawItems();
        visibleNodes_ = new LinkedList<BNode>();
        visibleLinks_ = new LinkedList<BLink>();
        newLinkStart_ = newLinkEnd_ = null;
        mergeNode1_ = mergeNode2_ = null;
        firstNode_ = null;
        drawStreetNet_ = true;
        drawRouteNet_ = false;
        selLinks_ = new SelectedLinks();
        selNodes_ = new SelectedNodes();
        hoverLinks_ = new HashSet<BLink>();
        drawHoverLinks_ = drawHoverNode_ = true;
        traceHoverPath_ = false;
        traceHoverDestNodes_ = new HashSet<BNode>();
        baseTraceLinks_ = new HashSet<BLink>();
        hoverNode_ = null;
        nodeHoverListener_ = null;
        hoverLink_ = null;
        linkHoverListener_ = null;
        ignoreNodeHover_ = false;
        hoverAnnotation_ = null;
        pointsOfInterest_ = new HashSet<PointOfInterest>();
        System.out.println("init w=" + width_ + " h=" + height_);
        setBackground(Color.white);
        canvas_ = new MapCanvas(null, new MapCoordinates(), ac_.getEngine().getBaseNet());
        buffer_ = new MapBuffer(ac_, this);
        layers_ = new MapLayers(ac_, gui_);
        cmManager_ = new MapContextMenuManager(ac_, this);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    leftClick(e.getX(), e.getY(), e.isShiftDown());
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    rightClick(e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (ac_.getGUI() == null) {
                    return;
                }
                cancelBuffer();
                int sca = ac_.getGUI().getCurrentClickAction();
                MapCoordinates cc = canvas_.getCC();
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (sca == MapModeToolbar.ACT_MSELNODES || sca == MapModeToolbar.ACT_MSELLINKS || sca == MapMainToolbar.ACT_ZOOMRANGE) {
                        rsx1_ = rsx2_ = e.getX();
                        rsy1_ = rsy2_ = e.getY();
                        dragMode_ = DragMode.RANGESEL;
                    } else if (sca == MapModeToolbar.ACT_MDRAGNODE) {
                        BNode node = findNodeNearXY(cc.xToWorld(e.getX()), cc.yToWorld(e.getY()));
                        if (node != null) {
                            draggingNode_ = node;
                            dragMode_ = DragMode.DRAG_NODE;
                            isStationary_ = true;
                            ac_.getNetworkOps().startMovingNode(node);
                        }
                    } else if (sca == MapModeToolbar.ACT_MDRAGSHPPT) {
                        double x = cc.xToWorld(e.getX());
                        double y = cc.yToWorld(e.getY());
                        BLink link = findLinkNearXY(x, y);
                        if (link != null) {
                            int index = link.getShapePointIndexFromXY(x, y, getClickToleranceMapUnits());
                            if (index >= 0) {
                                draggingSPLink_ = link;
                                draggingSPIndex_ = index;
                                dragMode_ = DragMode.DRAG_SHPPT;
                                isStationary_ = true;
                                ac_.getNetworkOps().startMovingShpPt(link, index);
                            }
                        }
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    mx_ = e.getX();
                    my_ = e.getY();
                    dragX_ = dragY_ = 0;
                    dragMode_ = DragMode.PANNING;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                MapCoordinates cc = canvas_.getCC();
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (dragMode_ == DragMode.DRAG_NODE) {
                        ac_.getNetworkOps().finishedMovingNode(draggingNode_);
                    }
                    if (dragMode_ == DragMode.DRAG_SHPPT) {
                        ac_.getNetworkOps().finishedMovingShpPt(draggingSPLink_);
                    }
                    isStationary_ = false;
                    if (Math.abs(rsx1_ - rsx2_) > 5 && Math.abs(rsy1_ - rsy2_) > 5) {
                        double x1 = cc.xToWorld(Math.min(rsx1_, rsx2_));
                        double x2 = cc.xToWorld(Math.max(rsx1_, rsx2_));
                        double y1 = cc.yToWorld(Math.min(rsy1_, rsy2_));
                        double y2 = cc.yToWorld(Math.max(rsy1_, rsy2_));
                        rangeSelected(x1, y2, x2, y1);
                        repaint();
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (Math.abs(dragX_) > 0 || Math.abs(dragY_) > 0) {
                        double wdx = cc.dxToWorld((double) dragX_);
                        double wdy = cc.dyToWorld((double) dragY_);
                        recenterDelta(-wdx, wdy, false);
                    }
                }
                dragMode_ = DragMode.NONE;
                draggingNode_ = null;
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                cmx_ = e.getX();
                cmy_ = e.getY();
                gui_.statusText("");
                mousePointerMoved();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                MapCoordinates cc = canvas_.getCC();
                switch(dragMode_) {
                    case RANGESEL:
                        rsx2_ = e.getX();
                        rsy2_ = e.getY();
                        repaint();
                        break;
                    case PANNING:
                        int dx = e.getX() - mx_;
                        int dy = e.getY() - my_;
                        dragX_ += dx;
                        dragY_ += dy;
                        mx_ = e.getX();
                        my_ = e.getY();
                        repaint();
                        break;
                    case DRAG_NODE:
                        ac_.getNetworkOps().movingNode(draggingNode_, cc.xToWorld(e.getX()), cc.yToWorld(e.getY()));
                        refresh(false, true, true);
                        break;
                    case DRAG_SHPPT:
                        ac_.getNetworkOps().movingShpPt(cc.xToWorld(e.getX()), cc.yToWorld(e.getY()));
                        refresh(false, true, true);
                        break;
                }
            }
        });
        addMouseWheelListener(this);
        mouseWheelMoving_ = false;
        mouseWheelFactor_ = 0;
        mouseWheelTimer_.setRepeats(false);
    }

    public MapCanvas getCanvas() {
        return canvas_;
    }

    public MapBuffer getBuffer() {
        return buffer_;
    }

    public MapLayers getLayers() {
        return layers_;
    }

    public MapDrawItems getDrawItems() {
        return drawItems_;
    }

    public SelectedLinks getSelectedLinks() {
        return selLinks_;
    }

    public SelectedNodes getSelectedNodes() {
        return selNodes_;
    }

    public Iterator<BLink> getVisibleLinks() {
        return visibleLinks_.iterator();
    }

    public int getVisibleLinkCount() {
        return visibleLinks_.size();
    }

    public void startedUp() {
        updated(true, true, true);
        inStartup_ = false;
        repaint();
    }

    public void setResizeToDrawable(String key) {
        resizeToDrawableKey_ = key;
    }

    public void resized() {
        double newX1, newY1, newX2, newY2;
        if (width_ == 0 && height_ == 00) {
            width_ = getWidth();
            height_ = getHeight();
            System.out.println("resized startup w=" + width_ + " h=" + height_);
            newX1 = initX_ - initRes_ * (width_ / 2);
            newX2 = initX_ + initRes_ * (width_ / 2);
            newY1 = initY_ - initRes_ * (height_ / 2);
            newY2 = initY_ + initRes_ * (height_ / 2);
            canvas_.getCC().updateDim(width_, height_);
            canvas_.getCC().updateRange(newX1, newY1, newX2, newY2);
        } else {
            MapCoordinates cc = canvas_.getCC();
            double mx = (cc.getX2() + cc.getX1()) / 2, my = (cc.getY2() + cc.getY1()) / 2;
            double hx = (cc.getX2() - cc.getX1()) / 2, hy = (cc.getY2() - cc.getY1()) / 2;
            double xRatio = (double) getWidth() / width_;
            double yRatio = (double) getHeight() / height_;
            newX1 = mx - hx * xRatio;
            newX2 = mx + hx * xRatio;
            newY1 = my - hy * yRatio;
            newY2 = my + hy * yRatio;
            width_ = getWidth();
            height_ = getHeight();
            System.out.println("resized update w=" + width_ + " h=" + height_);
        }
        zoomRange(newX1, newY1, newX2, newY2);
        if (resizeToDrawableKey_ != null) {
            zoomDrawable(resizeToDrawableKey_, .1);
            resizeToDrawableKey_ = null;
        } else {
            repaint();
        }
    }

    private void rangeSelected(double wx1, double wy1, double wx2, double wy2) {
        switch(gui_.getCurrentClickAction()) {
            case MapMainToolbar.ACT_ZOOMRANGE:
                zoomRange(wx1, wy1, wx2, wy2);
                break;
            case MapModeToolbar.ACT_MSELNODES:
                gui_.msg("selecting nodes");
                selectNodesInRange(wx1, wy1, wx2, wy2);
                refresh(true, false, false);
                break;
            case MapModeToolbar.ACT_MSELLINKS:
                gui_.msg("selecting links");
                selectLinksInRange(wx1, wy1, wx2, wy2);
                refresh(true, false, false);
                break;
        }
    }

    public void setIsStationary(boolean b) {
        isStationary_ = b;
    }

    public void newMode(ACGUI.Mode oldmode, ACGUI.Mode mode) {
        selLinks_.setVisible(false);
        selNodes_.setVisible(false);
        int nodeDispMode = canvas_.getNodeDispMode();
        switch(mode) {
            case MAP:
                selLinks_.setVisible(true);
                selNodes_.setVisible(true);
                if (nodeDispMode == MapCanvas.NODEDISP_EDITONLY) {
                    updateBase_ = true;
                }
                break;
            case STATION:
                if (nodeDispMode == MapCanvas.NODEDISP_EDITONLY && oldmode == ACGUI.Mode.MAP) {
                    updateBase_ = true;
                }
                break;
            case ROUTE:
                if (nodeDispMode == MapCanvas.NODEDISP_EDITONLY && oldmode == ACGUI.Mode.MAP) {
                    updateBase_ = true;
                }
                break;
            case ANALYSIS:
                if (nodeDispMode == MapCanvas.NODEDISP_EDITONLY && oldmode == ACGUI.Mode.MAP) {
                    updateBase_ = true;
                }
                break;
        }
        updateBG_ = updateFG_ = true;
        repaint();
        newClickAction();
    }

    /**
   * Notifies the MapPanel that the user has selected a new primary mouse click 
   * action. 
   */
    public void newClickAction() {
        firstNode_ = null;
        newLinkStart_ = newLinkEnd_ = null;
        mergeNode1_ = mergeNode2_ = null;
        drawHoverNode_ = drawHoverLinks_ = false;
        clearTraceHoverPath();
        switch(gui_.getSelectedToolbarAction()) {
            case MapModeToolbar.ACT_MSELLINKS:
            case MapModeToolbar.ACT_MDELLINK:
            case MapModeToolbar.ACT_MSPLITLINK:
            case MapModeToolbar.ACT_SLINK:
            case MapModeToolbar.ACT_AQLINK:
                drawHoverLinks_ = true;
                break;
            case MapModeToolbar.ACT_RPATH:
                drawHoverLinks_ = traceHoverPath_ = true;
                SubRoute activeSub = ac_.getRouteOps().getActiveSubRoute();
                ac_.getEngine().getBaseNet().getGraph().setWeightMode(BaseNetGraph.WEIGHT_LINKLEN, ac_.getActiveScenario());
                if (activeSub != null && activeSub.getPath().linkCount() > 0) {
                    baseTraceLinks_.addAll(activeSub.getPath().getLinkSet());
                    traceHoverDestNodes_.add(activeSub.getPath().startNode());
                    traceHoverDestNodes_.add(activeSub.getPath().endNode());
                }
                break;
            case MapModeToolbar.ACT_MSELCLINKS:
                drawHoverLinks_ = true;
                ac_.getEngine().getBaseNet().getGraph().setWeightMode(BaseNetGraph.WEIGHT_LINKLEN, ac_.getActiveScenario());
                if (selLinks_.size() > 0) {
                    traceHoverDestNodes_.addAll(selLinks_.getNodes());
                    traceHoverPath_ = true;
                }
                break;
            case MapModeToolbar.ACT_RDIVERTPATH:
                drawHoverLinks_ = true;
                ac_.getEngine().getBaseNet().getGraph().setWeightMode(BaseNetGraph.WEIGHT_LINKLEN, ac_.getActiveScenario());
                if (ac_.getRouteOps().getDiversionPath() != null && ac_.getRouteOps().getDiversionPath().linkCount() > 0) {
                    traceHoverDestNodes_.add(ac_.getRouteOps().getDiversionPath().endNode());
                    traceHoverPath_ = true;
                }
                break;
            case MapModeToolbar.ACT_MSELNODES:
            case MapModeToolbar.ACT_AARRFACS:
            case MapModeToolbar.ACT_ATRIPEND:
            case MapModeToolbar.ACT_ADEPFACS:
            case MapModeToolbar.ACT_ATRIPSTART:
            case MapModeToolbar.ACT_AQNODE:
            case MapModeToolbar.ACT_MNEWLINK:
            case MapModeToolbar.ACT_MDELNODE:
            case MapModeToolbar.ACT_MDRAGNODE:
            case MapModeToolbar.ACT_MMERGENODE:
            case MapModeToolbar.ACT_MSTITCHLINKS:
            case MapModeToolbar.ACT_RADDSTOP:
            case MapModeToolbar.ACT_RADDTPOINT:
            case MapModeToolbar.ACT_RDELSTOP:
            case MapModeToolbar.ACT_RDELTPOINT:
            case MapModeToolbar.ACT_RCLIPDIVERSION:
            case MapModeToolbar.ACT_RCLIPPATHEND:
            case MapModeToolbar.ACT_SNODE:
            case MapModeToolbar.ACT_SRNNODE:
                drawHoverNode_ = true;
                break;
        }
        System.out.println(drawHoverNode_ + " " + drawHoverLinks_);
        refresh(true, false, true);
    }

    public void setIsAnimating(boolean animating) {
        animating_ = animating;
    }

    public void setNodeHoverListener(NodeHoverListener nhl) {
        nodeHoverListener_ = nhl;
    }

    public void clearNodeHoverListener() {
        nodeHoverListener_ = null;
    }

    public void setLinkHoverListener(LinkHoverListener lhl) {
        linkHoverListener_ = lhl;
    }

    public void clearLinkHoverListener() {
        linkHoverListener_ = null;
    }

    public void setIgnoreNodeHover(boolean val) {
        ignoreNodeHover_ = val;
    }

    public void mousePointerMoved() {
        MapCoordinates cc = canvas_.getCC();
        if (gui_.getStatusText().length() > 0) return;
        PointOfInterest poi = getPOIFromXY(cmx_, cmy_);
        if (poi != null) {
            if (hoverPOI_ != null && poi != hoverPOI_) hoverPOI_.mouseOut();
            poi.mouseOver(cmx_, cmy_);
            hoverPOI_ = poi;
            return;
        } else if (hoverPOI_ != null) {
            hoverPOI_.mouseOut();
            hoverPOI_ = null;
            hoverAnnotation_ = null;
        }
        BNode node = findNodeNearXY(cc.xToWorld(cmx_), cc.yToWorld(cmy_));
        if (!ignoreNodeHover_) {
            if (node != null) {
                String statusText = node.getDisplayText();
                SubRoute sub = ac_.getRouteOps().getActiveSubRoute();
                if (sub != null && sub.getPath().getTimePointCountAtNode(node) > 0) {
                    statusText += " / Timept" + (sub.getPath().getTimePointCountAtNode(node) > 1 ? "s" : "") + " for subroute " + sub.getMasterID() + " (";
                    for (Iterator<TimePoint> tpts = sub.getPath().getTimePoints(node); tpts.hasNext(); ) {
                        String name = tpts.next().getName();
                        statusText += (name.length() > 0 ? name : "unnamed") + (tpts.hasNext() ? ", " : ")");
                    }
                }
                gui_.statusText(statusText);
            }
            if (node != hoverNode_) {
                if (node != null) {
                    if (nodeHoverListener_ != null) {
                        nodeHoverListener_.nodeHoverOn(node);
                    }
                } else {
                    if (nodeHoverListener_ != null) {
                        nodeHoverListener_.nodeHoverOut(hoverNode_);
                    }
                }
                hoverNode_ = node;
                refresh(true, false, false);
            }
        }
        if (node == null) {
            BLink link = findLinkNearXY(cc.xToWorld(cmx_), cc.yToWorld(cmy_));
            if (link != null) {
                gui_.statusText(link.getDisplayText());
            }
            if (link != hoverLink_) {
                if (link != null) {
                    if (linkHoverListener_ != null) {
                        linkHoverListener_.linkHoverOn(link);
                    }
                    hoverLinks_.clear();
                    hoverLinks_.add(link);
                    if (traceHoverPath_) {
                        runHoverTrace(link);
                    }
                } else {
                    if (linkHoverListener_ != null) {
                        linkHoverListener_.linkHoverOut(hoverLink_);
                    }
                    hoverLinks_.clear();
                }
                hoverLink_ = link;
                refresh(true, false, false);
            }
        }
    }

    public void clearTraceHoverPath() {
        traceHoverPath_ = false;
        tracedHoverPath_ = null;
        traceHoverDestNodes_ = new HashSet<BNode>();
        baseTraceLinks_ = new HashSet<BLink>();
    }

    /**
   * Attempts to trace a path from the active hover link to any member of the
   * node set specified by traceHoverDestNodes_. If successful, tracedHoverPath_
   * is updated accordingly, and subsequent redraw actions will show the trace. 
   * 
   * @param link  the active hover link on which to perform the trace
   */
    private void runHoverTrace(BLink link) {
        tracedHoverPath_ = new LinkedList<BLink>();
        if (baseTraceLinks_.contains(link)) {
            return;
        }
        int limit = 10000;
        ClosestFirstIterator<BNode, BLink> cfiF = new ClosestFirstIterator<BNode, BLink>(ac_.getEngine().getBaseNet().getGraph(), link.getFNode(), limit);
        ClosestFirstIterator<BNode, BLink> cfiT = new ClosestFirstIterator<BNode, BLink>(ac_.getEngine().getBaseNet().getGraph(), link.getTNode(), limit);
        BNode destNodeF = null;
        while (cfiF.hasNext()) {
            BNode node = cfiF.next();
            if (traceHoverDestNodes_.contains(node)) {
                destNodeF = node;
                break;
            }
        }
        BNode destNodeT = null;
        while (cfiT.hasNext()) {
            BNode node = cfiT.next();
            if (traceHoverDestNodes_.contains(node)) {
                destNodeT = node;
                break;
            }
        }
        BNode origin, dest;
        ClosestFirstIterator<BNode, BLink> cfi;
        if (destNodeF != null && destNodeT != null) {
            if (cfiF.getShortestPathLength(destNodeF) < cfiT.getShortestPathLength(destNodeT)) {
                origin = link.getFNode();
                dest = destNodeF;
                cfi = cfiF;
            } else {
                origin = link.getTNode();
                dest = destNodeT;
                cfi = cfiT;
            }
        } else if (destNodeF != null && destNodeT == null) {
            origin = link.getFNode();
            dest = destNodeF;
            cfi = cfiF;
        } else if (destNodeF == null && destNodeT != null) {
            origin = link.getTNode();
            dest = destNodeT;
            cfi = cfiT;
        } else {
            return;
        }
        BNode node = dest;
        while (node != origin) {
            BLink pLink = cfi.getSpanningTreeEdge(node);
            hoverLinks_.add(pLink);
            tracedHoverPath_.add(pLink);
            node = ac_.getEngine().getBaseNet().opposite(node, pLink);
        }
        if (!tracedHoverPath_.contains(link)) {
            tracedHoverPath_.add(link);
        }
    }

    public List<BLink> getTracedHoverPath() {
        return tracedHoverPath_;
    }

    public void setBaseTraceLinks(Set<BLink> links) {
        baseTraceLinks_ = links;
    }

    public void addBaseTraceLinks(Collection<BLink> links) {
        baseTraceLinks_.addAll(links);
    }

    public void setTraceDestNodes(Set<BNode> nodes) {
        traceHoverDestNodes_ = nodes;
        traceHoverPath_ = true;
    }

    public void clearHover() {
        hoverLinks_.clear();
        hoverNode_ = null;
        hoverLink_ = null;
    }

    /**
   * Alerts MapPanel that one or more display layers have been affected and are
   * in need of a refresh, but does not trigger a repaint() call.
   * 
   * @param bg  refresh status flag for background annotations layer
   * @param base  refresh status flag for base layer
   * @param fg  refresh status flag for foreground annotations layer
   */
    public void updated(boolean bg, boolean base, boolean fg) {
        updateBG_ = updateBG_ || bg;
        updateBase_ = updateBase_ || base;
        updateFG_ = updateFG_ || fg;
    }

    /**
   * Refreshes the map display, including a repaint call. The refresh may apply
   * to the background annotations, base layer, foreground annotations, or any
   * combination thereof. 
   * 
   * @param bg  flag indicating whether to refresh background annotations
   * @param base  flag indicating whether to refresh base layer
   * @param fg  flag indicating whetehr to refresh foreground annotations
   */
    public void refresh(boolean bg, boolean base, boolean fg) {
        updated(bg, base, fg);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inStartup_) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        canvas_.setGraphics(g2d);
        g2d.setColor(this.getBGColor());
        g2d.fillRect(0, 0, getWidth(), getHeight());
        if (mouseWheelMoving_) {
            paintZoomPreview();
            return;
        }
        if (recentering_) {
            drawTiles(g2d, baseImg_, recenterX_, recenterY_);
            return;
        }
        RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(renderHints);
        if (dragMode_ == DragMode.RANGESEL) {
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.cyan);
            g2d.fillRect(Math.min(rsx1_, rsx2_), Math.min(rsy1_, rsy2_), Math.abs(rsx2_ - rsx1_), Math.abs(rsy2_ - rsy1_));
            g2d.drawImage(baseImg_, 0, 0, this);
            g2d.drawImage(fgImg_, 0, 0, this);
            return;
        }
        if (dragMode_ == DragMode.PANNING) {
            drawTiles(g2d, baseImg_, dragX_, dragY_);
            return;
        }
        if (bgImg_ == null) {
            updateBG_ = true;
        }
        if (baseImg_ == null) {
            updateBase_ = true;
        }
        if (fgImg_ == null) {
            updateFG_ = true;
        }
        updateImages(updateBG_, updateBase_, updateFG_);
        g2d.drawImage(bgImg_, 0, 0, this);
        g2d.drawImage(baseImg_, 0, 0, this);
        g2d.drawImage(fgImg_, 0, 0, this);
        updateBG_ = updateBase_ = updateFG_ = false;
    }

    private void drawTiles(Graphics2D g2d, Image mainTile, int offsetX, int offsetY) {
        g2d.drawImage(mainTile, offsetX, offsetY, this);
        Image n = buffer_.getTile(MapBuffer.NORTH);
        if (n != null) {
            g2d.drawImage(n, offsetX, offsetY - getHeight(), this);
        }
        Image e = buffer_.getTile(MapBuffer.EAST);
        if (e != null) {
            g2d.drawImage(e, offsetX + getWidth(), offsetY, this);
        }
        Image s = buffer_.getTile(MapBuffer.SOUTH);
        if (s != null) {
            g2d.drawImage(s, offsetX, offsetY + getHeight(), this);
        }
        Image w = buffer_.getTile(MapBuffer.WEST);
        if (w != null) {
            g2d.drawImage(w, offsetX - getWidth(), offsetY, this);
        }
        Image ne = buffer_.getTile(MapBuffer.NORTHEAST);
        if (ne != null) {
            g2d.drawImage(ne, offsetX + getWidth(), offsetY - getHeight(), this);
        }
        Image se = buffer_.getTile(MapBuffer.SOUTHEAST);
        if (se != null) {
            g2d.drawImage(se, offsetX + getWidth(), offsetY + getHeight(), this);
        }
        Image nw = buffer_.getTile(MapBuffer.NORTHWEST);
        if (nw != null) {
            g2d.drawImage(nw, offsetX - getWidth(), offsetY - getHeight(), this);
        }
        Image sw = buffer_.getTile(MapBuffer.SOUTHWEST);
        if (sw != null) {
            g2d.drawImage(sw, offsetX - getWidth(), offsetY + getHeight(), this);
        }
    }

    private void updateImages(boolean bg, boolean base, boolean fg) {
        if (bg) {
            updateBGImage();
        }
        if (base) {
            updateBaseImage();
        }
        if (fg) {
            updateFGImage();
        }
    }

    public Color getBGColor() {
        return new Color(242, 242, 242);
    }

    private void updateBGImage() {
        bgImg_ = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics ig = bgImg_.getGraphics();
        updateBG(ig);
    }

    private void updateBG(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        canvas_.setGraphics(g2d);
        canvas_.setColor(Color.cyan);
        if (drawHoverNode_ && hoverNode_ != null) {
            canvas_.drawPoint(hoverNode_.getX(), hoverNode_.getY(), 11);
        } else if (drawHoverLinks_) {
            Iterator<BLink> links = hoverLinks_.iterator();
            BLink link;
            while (links.hasNext()) {
                link = links.next();
                canvas_.setStroke(new BasicStroke(link.getDrawWidth(canvas_, ac_.getActiveScenario()) + 9, BasicStroke.CAP_ROUND, 0));
                link.drawCurve(canvas_);
            }
        }
        if (selLinks_.isVisible()) selLinks_.draw(canvas_, ac_.getActiveScenario());
        if (selNodes_.isVisible()) selNodes_.draw(canvas_);
    }

    private void updateBaseImage() {
        baseImg_ = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics ig = baseImg_.getGraphics();
        updateBase(ig, true, 0);
    }

    int ubCount_ = 0;

    public synchronized void updateBase(Graphics g, boolean recordVisible, int id) {
        double x1 = canvas_.getCC().getX1(), y1 = canvas_.getCC().getY1();
        double x2 = canvas_.getCC().getX2(), y2 = canvas_.getCC().getY2();
        Graphics2D g2d = (Graphics2D) g;
        canvas_.setGraphics(g2d);
        if (!drawStreetNet_) {
            return;
        }
        RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(renderHints);
        g2d.setColor(Color.black);
        List<BNode> staNodes = new LinkedList<BNode>();
        BNode node;
        int nodeDispMode = canvas_.getNodeDispMode();
        boolean drawNodes = (nodeDispMode == MapCanvas.NODEDISP_STANDARD) || (nodeDispMode == MapCanvas.NODEDISP_SHPPT) || (nodeDispMode == MapCanvas.NODEDISP_ID) || (nodeDispMode == MapCanvas.NODEDISP_EDITONLY && gui_.getMode() == ACGUI.Mode.MAP);
        boolean drawShpPts = (nodeDispMode == MapCanvas.NODEDISP_SHPPT);
        int majorCode = canvas_.getMajorCode();
        if (!isStationary_) {
            if (!ac_.getEngine().getDataPackage().getProperty("preloadNetwork").equals("true")) ac_.getEngine().initArea(x1, y1, x2, y2);
            List<BNode> visibleNodes = ac_.getEngine().getBaseNet().getNodes(x1, y1, x2, y2);
            if (recordVisible) {
                visibleNodes_ = visibleNodes;
            }
        }
        layers_.clearVisibleLinks(ac_.getEngine().getScenarios());
        ac_.getEngine().getBaseNet().updateVisibleLinks(x1, y1, x2, y2, layers_, majorCode);
        layers_.draw(canvas_, drawShpPts);
        if (recordVisible) {
            visibleLinks_ = layers_.getDrawnLinks();
        }
        Iterator<BNode> nodes = visibleNodes_.iterator();
        while (nodes.hasNext()) {
            node = nodes.next();
            if (node.isStation()) {
                staNodes.add(node);
            } else if (drawNodes) {
                node.draw(canvas_);
            }
        }
        nodes = staNodes.iterator();
        while (nodes.hasNext()) {
            nodes.next().draw(canvas_);
        }
        if (!isStationary_) {
            startBufferWorker();
        }
        gui_.updateBaseNetLabel();
    }

    private void updateFGImage() {
        fgImg_ = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics ig = fgImg_.getGraphics();
        updateFG(ig);
    }

    private void updateFG(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        canvas_.setGraphics(g2d);
        RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(renderHints);
        canvas_.setStroke(new BasicStroke(2));
        canvas_.setColor(Color.green);
        if (firstNode_ != null) {
            canvas_.setColor(Color.yellow);
            canvas_.drawPoint(firstNode_.getX(), firstNode_.getY(), 6);
        }
        if (drawRouteNet_) {
            canvas_.setStroke(new BasicStroke(3));
            ac_.getEngine().getRouteNetwork().draw(canvas_);
        }
        drawItems_.draw(canvas_, gui_.getMode());
    }

    public void resetView(double x, double y, double res) {
        double x1 = x - res * (width_ / 2);
        double x2 = x + res * (width_ / 2);
        double y1 = y - res * (height_ / 2);
        double y2 = y + res * (height_ / 2);
        canvas_.getCC().updateRange(x1, y1, x2, y2);
    }

    public void zoomIn() {
        zoomIn(zoomInFactor_);
    }

    public void zoomIn(double factor) {
        cancelBuffer();
        double dx = factor * canvas_.getCC().getXRange();
        double dy = factor * canvas_.getCC().getYRange();
        double x1 = canvas_.getCC().getX1() + dx;
        double y1 = canvas_.getCC().getY1() + dy;
        double x2 = canvas_.getCC().getX2() - dx;
        double y2 = canvas_.getCC().getY2() - dy;
        canvas_.getCC().updateRange(x1, y1, x2, y2);
        updated(true, true, true);
    }

    public void zoomOut() {
        zoomOut(zoomOutFactor_);
    }

    public void zoomOut(double factor) {
        cancelBuffer();
        double dx = factor * canvas_.getCC().getXRange();
        double dy = factor * canvas_.getCC().getYRange();
        double x1 = canvas_.getCC().getX1() - dx;
        double y1 = canvas_.getCC().getY1() - dy;
        double x2 = canvas_.getCC().getX2() + dx;
        double y2 = canvas_.getCC().getY2() + dy;
        canvas_.getCC().updateRange(x1, y1, x2, y2);
        updated(true, true, true);
    }

    public void zoomRange(Rectangle2D rect) {
        if (rect.getWidth() == 0 || rect.getHeight() == 0) {
            recenter(rect.getX(), rect.getY());
        } else {
            zoomRange(rect.getX(), rect.getY(), rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
        }
    }

    public void zoomRange(double wx1, double wy1, double wx2, double wy2) {
        cancelBuffer();
        MapCoordinates cc = canvas_.getCC();
        double gx1 = cc.xToScreen(wx1);
        double gy1 = cc.yToScreen(wy2);
        double gx2 = cc.xToScreen(wx2);
        double gy2 = cc.yToScreen(wy1);
        double screenAspect = (double) width_ / height_;
        double newAspect = (gx2 - gx1) / (gy2 - gy1);
        double x1, y1, x2, y2;
        if (newAspect < screenAspect) {
            y1 = wy1;
            y2 = wy2;
            double mx = (wx1 + wx2) / 2;
            double wxRange = screenAspect * (wy2 - wy1);
            x1 = mx - wxRange / 2;
            x2 = mx + wxRange / 2;
        } else {
            x1 = wx1;
            x2 = wx2;
            double my = (wy1 + wy2) / 2;
            double wyRange = (1 / screenAspect) * (wx2 - wx1);
            y1 = my - wyRange / 2;
            y2 = my + wyRange / 2;
        }
        canvas_.getCC().updateRange(x1, y1, x2, y2);
        updated(true, true, true);
    }

    public void zoomPath(Path path) {
        if (path.linkCount() > 0) {
            Rectangle2D.Double rect = path.getBoundingBox();
            zoomRange(rect.getX(), rect.getY(), rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
            zoomOut(.1);
            repaint();
        }
    }

    public void zoomDrawable(String key, double zOutFactor) {
        MapDrawable item = drawItems_.getItem(key);
        if (item != null) {
            zoomRange(item.getBoundingBox());
            zoomOut(.1);
            repaint();
        }
    }

    public void zoomToSelectedLinks() {
        if (selLinks_.isEmpty()) {
            return;
        }
        Rectangle2D.Double rect = selLinks_.getBoundingRect();
        zoomRange(rect.getX(), rect.getY(), rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
        zoomOut();
        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (!mouseWheelMoving_) {
            Dimension size = getSize();
            capture_ = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
            paint(capture_.getGraphics());
            mouseWheelMoving_ = true;
        }
        mouseWheelFactor_ += e.getWheelRotation();
        repaint();
        mouseWheelTimer_.restart();
    }

    private void mouseWheelZoom() {
        double x1 = canvas_.getCC().getX1();
        double y1 = canvas_.getCC().getY1();
        double x2 = canvas_.getCC().getX2();
        double y2 = canvas_.getCC().getY2();
        double factor = Math.pow(1.2, mouseWheelFactor_);
        double newW = (x2 - x1) * factor;
        double newH = (y2 - y1) * factor;
        double dx = (newW - (x2 - x1)) / 2.0;
        double dy = (newH - (y2 - y1)) / 2.0;
        mouseWheelMoving_ = false;
        mouseWheelFactor_ = 0;
        zoomRange(x1 - dx, y1 - dy, x2 + dx, y2 + dy);
        repaint();
    }

    private void paintZoomPreview() {
        double x1 = canvas_.getCC().getX1(), y1 = canvas_.getCC().getY1();
        double x2 = canvas_.getCC().getX2(), y2 = canvas_.getCC().getY2();
        double factor = Math.pow(1.2, -1 * mouseWheelFactor_);
        double newW = (x2 - x1) * factor;
        double newH = (y2 - y1) * factor;
        double dx = (newW - (x2 - x1)) / 2.0;
        double dy = (newH - (y2 - y1)) / 2.0;
        drawScaledTile(capture_, newW, newH, dx, dy, 0, 0);
        if (factor < 1) {
            drawScaledTile(buffer_.getTile(MapBuffer.WEST), newW, newH, dx, dy, -1, 0);
            drawScaledTile(buffer_.getTile(MapBuffer.NORTHWEST), newW, newH, dx, dy, -1, 1);
            drawScaledTile(buffer_.getTile(MapBuffer.NORTH), newW, newH, dx, dy, 0, 1);
            drawScaledTile(buffer_.getTile(MapBuffer.NORTHEAST), newW, newH, dx, dy, 1, 1);
            drawScaledTile(buffer_.getTile(MapBuffer.EAST), newW, newH, dx, dy, 1, 0);
            drawScaledTile(buffer_.getTile(MapBuffer.SOUTHEAST), newW, newH, dx, dy, 1, -1);
            drawScaledTile(buffer_.getTile(MapBuffer.SOUTH), newW, newH, dx, dy, 0, -1);
            drawScaledTile(buffer_.getTile(MapBuffer.SOUTHWEST), newW, newH, dx, dy, -1, -1);
        }
    }

    private void drawScaledTile(Image tile, double newW, double newH, double dx, double dy, double xOff, double yOff) {
        if (tile == null) return;
        double x1 = canvas_.getCC().getX1(), y1 = canvas_.getCC().getY1();
        double x2 = canvas_.getCC().getX2(), y2 = canvas_.getCC().getY2();
        MapCoordinates cc = canvas_.getCC();
        canvas_.getGraphics().drawImage(tile, (int) cc.xToScreen(x1 - dx + newW * xOff), (int) cc.yToScreen(y2 + dy + newH * yOff), (int) cc.xToScreen(x2 + dx + newW * xOff), (int) cc.yToScreen(y1 - dy + newH * yOff), 0, 0, width_, height_, this);
    }

    public void recenter() {
        recenter(canvas_.getCC().xToWorld(mx_), canvas_.getCC().yToWorld(my_));
    }

    public void recenter(BNode node) {
        recenter(node.getX(), node.getY());
    }

    public void recenter(double wx, double wy) {
        Point2D.Double center = canvas_.getCC().getCenter();
        double wdx = wx - center.x, wdy = wy - center.y;
        recenterDelta(wdx, wdy);
    }

    public void recenter(double wx, double wy, boolean animate) {
        Point2D.Double center = canvas_.getCC().getCenter();
        double wdx = wx - center.x, wdy = wy - center.y;
        recenterDelta(wdx, wdy, animate);
    }

    public void recenterDelta(double wdx, double wdy) {
        recenterDelta(wdx, wdy, Math.abs(wdx) < canvas_.getCC().getXRange() && Math.abs(wdy) < canvas_.getCC().getYRange());
    }

    public void recenterDelta(double wdx, double wdy, boolean animate) {
        double x1 = canvas_.getCC().getX1() + wdx;
        double y1 = canvas_.getCC().getY1() + wdy;
        double x2 = canvas_.getCC().getX2() + wdx;
        double y2 = canvas_.getCC().getY2() + wdy;
        canvas_.getCC().updateRange(x1, y1, x2, y2);
        if (animate) {
            new RecenterAnimator(canvas_.getCC().distToScreen(wdx), canvas_.getCC().distToScreen(wdy));
        } else {
            finishRecenter();
        }
    }

    private void finishRecenter() {
        recentering_ = false;
        updateImages(true, true, true);
        refresh(false, false, false);
    }

    public class RecenterAnimator implements ActionListener {

        private int n_, frames_;

        private Timer timer_;

        private double incrX_, incrY_;

        public RecenterAnimator(double dx, double dy) {
            frames_ = 8;
            incrX_ = -dx / frames_;
            incrY_ = dy / frames_;
            n_ = 1;
            recentering_ = true;
            timer_ = new Timer(25, this);
            timer_.start();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (n_ > frames_) {
                timer_.stop();
                finishRecenter();
                return;
            }
            recenterX_ = (int) incrX_ * n_;
            recenterY_ = (int) incrY_ * n_;
            repaint();
            n_++;
        }
    }

    private void startBufferWorker() {
        if (!bufferAdjacentTiles_) {
            return;
        }
        buffer_.clear();
        bufferWorker_ = new MapBufferWorker(this);
        System.out.println("executing worker");
        bufferWorker_.execute();
    }

    public void bufferThreadFinished() {
    }

    public void cancelBuffer() {
        if (bufferWorker_ == null) {
            return;
        }
        System.out.println("canceling from panel");
        bufferWorker_.cancel(false);
        bufferWorker_ = null;
    }

    private void leftClick(int x, int y, boolean shiftDown) {
        double wx = canvas_.getCC().xToWorld(x), wy = canvas_.getCC().yToWorld(y);
        BLink link;
        BNode node;
        ACEvent ave = null;
        Station station;
        switch(ac_.getGUI().getCurrentClickAction()) {
            case ACGUI.SPECIAL_SELNODE:
                node = findNodeNearXY(wx, wy);
                ave = new ACEvent(EventTypes.SPECIAL_CLICK_NODE);
                ave.setProperty("node", node);
                break;
            case ACGUI.SPECIAL_SELLINK:
                link = findLinkNearXY(wx, wy);
                ave = new ACEvent(EventTypes.SPECIAL_CLICK_LINK);
                ave.setProperty("link", link);
                break;
            case ACGUI.SPECIAL_SELNODE_IMPTEXT_1ST:
                node = findNodeNearXY(wx, wy);
                ac_.msg("Node selected: " + (node != null ? node.getID() : "null"));
                if (node != null) {
                    ac_.getRouteOps().importRoutePathFromText_1stNode(node);
                    gui_.setSpecialClickAction(ACGUI.SPECIAL_SELNODE_IMPTEXT_2ND);
                }
                break;
            case ACGUI.SPECIAL_SELNODE_IMPTEXT_2ND:
                node = findNodeNearXY(wx, wy);
                ac_.msg("Node selected: " + (node != null ? node.getID() : "null"));
                if (node != null) {
                    ac_.getRouteOps().importRoutePathFromText_2ndNode(node);
                    gui_.setSpecialClickAction(ACGUI.SPECIAL_NONE);
                }
                break;
            case MapModeToolbar.ACT_MSELNODES:
                node = findNodeNearXY(wx, wy);
                if (node != null) {
                    if (selNodes_.contains(node)) selNodes_.remove(node); else selNodes_.add(node);
                    refresh(true, false, false);
                }
                break;
            case MapModeToolbar.ACT_MSELLINKS:
                link = findLinkNearXY(wx, wy);
                if (link != null) {
                    if (selLinks_.contains(link)) selLinks_.remove(link); else selLinks_.add(link);
                    refresh(true, false, false);
                }
                break;
            case MapModeToolbar.ACT_MSELCLINKS:
                link = findLinkNearXY(wx, wy);
                if (tracedHoverPath_ != null && tracedHoverPath_.size() > 0) {
                    selLinks_.addAll(tracedHoverPath_);
                } else if (link != null) {
                    selLinks_.add(link);
                    traceHoverPath_ = true;
                } else {
                    break;
                }
                traceHoverDestNodes_.clear();
                BaseNetGraph graph = new BaseNetGraph(selLinks_.iterator());
                traceHoverDestNodes_.addAll(graph.getEndPoints());
                refresh(true, false, false);
                break;
            case MapModeToolbar.ACT_SNODE:
                ac_.getStationOps().toggleStationNode(findNodeNearXY(wx, wy));
                break;
            case MapModeToolbar.ACT_SRNNODE:
                ac_.getStationOps().renameStationNode(findNodeNearXY(wx, wy));
                break;
            case MapModeToolbar.ACT_SLINK:
                ac_.getStationOps().toggleStationLink(findLinkNearXY(wx, wy));
                break;
            case MapModeToolbar.ACT_RPATH:
                link = findLinkNearXY(wx, wy);
                ave = new ACEvent(EventTypes.MCLICK_RTE_TOGGLE_LINK);
                if (tracedHoverPath_ != null && tracedHoverPath_.size() > 0) ave.setProperty("links", tracedHoverPath_); else if (link != null) ave.setProperty("links", Collections.singletonList(link)); else return;
                break;
            case MapModeToolbar.ACT_RDIVERTPATH:
                link = findLinkNearXY(wx, wy);
                ave = new ACEvent(EventTypes.MCLICK_RTE_TOGGLE_DIVLINK);
                if (tracedHoverPath_ != null && tracedHoverPath_.size() > 0) ave.setProperty("links", tracedHoverPath_); else if (link != null) ave.setProperty("links", Collections.singletonList(link)); else return;
                break;
            case MapModeToolbar.ACT_RCLIPDIVERSION:
                ave = new ACEvent(EventTypes.MCLICK_RTE_CLIP_DIVERSION);
                ave.setProperty("node", findNodeNearXY(wx, wy));
                break;
            case MapModeToolbar.ACT_RCLIPPATHSTART:
                ave = new ACEvent(EventTypes.MCLICK_RTE_CLIP_START);
                ave.setProperty("node", findNodeNearXY(wx, wy));
                break;
            case MapModeToolbar.ACT_RCLIPPATHEND:
                ave = new ACEvent(EventTypes.MCLICK_RTE_CLIP_END);
                ave.setProperty("node", findNodeNearXY(wx, wy));
                break;
            case MapModeToolbar.ACT_RADDTPOINT:
                ave = new ACEvent(EventTypes.MCLICK_RTE_ADD_TIMEPT);
                ave.setProperty("node", findNodeNearXY(wx, wy));
                break;
            case MapModeToolbar.ACT_RADDSTOP:
                node = findNodeNearXY(wx, wy);
                ave = new ACEvent(EventTypes.MCLICK_RTE_ADD_STOP);
                ave.setProperty("node", node);
                break;
            case MapModeToolbar.ACT_RDELTPOINT:
                node = findNodeNearXY(wx, wy);
                ave = new ACEvent(EventTypes.MCLICK_RTE_DELETE_TIMEPT);
                ave.setProperty("node", node);
                break;
            case MapModeToolbar.ACT_RDELSTOP:
                node = findNodeNearXY(wx, wy);
                ave = new ACEvent(EventTypes.MCLICK_RTE_DELETE_STOP);
                ave.setProperty("node", node);
                break;
            case MapModeToolbar.ACT_AQNODE:
                ave = new ACEvent(EventTypes.MCLICK_AN_QUERY_NODE);
                ave.setProperty("node", findNodeNearXY(wx, wy));
                break;
            case MapModeToolbar.ACT_AQLINK:
                ave = new ACEvent(EventTypes.MCLICK_AN_QUERY_LINK);
                ave.setProperty("link", findLinkNearXY(wx, wy));
                break;
            case MapModeToolbar.ACT_ATRIPSTART:
                ave = new ACEvent(EventTypes.MCLICK_AN_TRIP_START);
                ave.setProperty("node", findNodeNearXY(wx, wy));
                break;
            case MapModeToolbar.ACT_ATRIPEND:
                ave = new ACEvent(EventTypes.MCLICK_AN_TRIP_END);
                ave.setProperty("node", findNodeNearXY(wx, wy));
                break;
            case MapModeToolbar.ACT_ADEPFACS:
                node = findNodeNearXY(wx, wy);
                if (node != null) {
                    drawItems_.addItem(MapDrawItems.SEARCH_NODE, new HighlightedPoint(node, Color.red, 8), "a");
                    refresh(false, false, true);
                    ac_.getAnalysisOps().showDepartingFacilities(node);
                }
                break;
            case MapModeToolbar.ACT_AARRFACS:
                node = findNodeNearXY(wx, wy);
                if (node != null) {
                    drawItems_.addItem(MapDrawItems.SEARCH_NODE, new HighlightedPoint(node, Color.red, 8), "a");
                    refresh(false, false, true);
                    ac_.getAnalysisOps().showArrivingFacilities(node);
                }
                break;
            case MapModeToolbar.ACT_MNEWNODE:
                ac_.getNetworkOps().userNewNode(wx, wy);
                refresh(true, true, false);
                break;
            case MapModeToolbar.ACT_MDELNODE:
                node = findNodeNearXY(wx, wy);
                if (node == null) return;
                ave = new ACEvent(EventTypes.MCLICK_MAP_DELETE_NODE);
                ave.setProperty("node", node);
                break;
            case MapModeToolbar.ACT_MNEWLINK:
                node = findNodeNearXY(wx, wy);
                if (node != null) {
                    if (newLinkStart_ == null) {
                        firstNode_ = newLinkStart_ = node;
                        refresh(true, false, true);
                    } else {
                        newLinkEnd_ = node;
                        ac_.getNetworkOps().userNewLink(newLinkStart_, newLinkEnd_);
                        newLinkStart_ = newLinkEnd_ = firstNode_ = null;
                        refresh(true, true, true);
                    }
                }
                break;
            case MapModeToolbar.ACT_MDELLINK:
                link = findLinkNearXY(wx, wy);
                if (link == null) return;
                ave = new ACEvent(EventTypes.MCLICK_MAP_DELETE_LINK);
                ave.setProperty("link", link);
                break;
            case MapModeToolbar.ACT_MSPLITLINK:
                link = findLinkNearXY(wx, wy);
                if (link == null) return;
                ave = new ACEvent(EventTypes.MCLICK_MAP_SPLIT_LINK);
                ave.setProperty("link", link);
                ave.setProperty("x", wx);
                ave.setProperty("y", wy);
                break;
            case MapModeToolbar.ACT_MMERGENODE:
                node = findNodeNearXY(wx, wy);
                if (node != null) {
                    if (mergeNode1_ == null) {
                        firstNode_ = mergeNode1_ = node;
                        updated(true, false, true);
                    } else {
                        mergeNode2_ = node;
                        ac_.getNetworkOps().mergeNode(mergeNode1_, mergeNode2_);
                        mergeNode1_ = mergeNode2_ = firstNode_ = null;
                        updated(true, true, true);
                    }
                }
                break;
            case MapModeToolbar.ACT_MDELSHPPT:
                link = findLinkNearXY(wx, wy);
                ave = new ACEvent(EventTypes.MCLICK_MAP_DELETE_SHPPT);
                ave.setProperty("link", link);
                ave.setProperty("x", wx);
                ave.setProperty("y", wy);
                break;
            case MapModeToolbar.ACT_MNEWSHPPT:
                link = findLinkNearXY(wx, wy);
                ave = new ACEvent(EventTypes.MCLICK_MAP_ADD_SHPPT);
                ave.setProperty("link", link);
                ave.setProperty("x", wx);
                ave.setProperty("y", wy);
                break;
            case MapModeToolbar.ACT_MSTITCHLINKS:
                node = findNodeNearXY(wx, wy);
                if (node == null) break;
                ave = new ACEvent(EventTypes.MCLICK_MAP_STITCH_LINKS);
                ave.setProperty("node", node);
                break;
        }
        if (ave != null) ac_.fireEvent(ave);
    }

    public void rightClick(int x, int y) {
        double wx = canvas_.getCC().xToWorld(x), wy = canvas_.getCC().yToWorld(y);
        BNode node = findNodeNearXY(wx, wy);
        if (node != null) {
            cmManager_.showNodeMenu(node, x, y);
            return;
        }
        BLink link = findLinkNearXY(wx, wy);
        if (link != null) {
            cmManager_.showLinkMenu(link, x, y);
            return;
        }
        if (node == null && link == null) cmManager_.showDefaultMenu(x, y);
    }

    public double getClickToleranceMapUnits() {
        return clickTolerance_ * canvas_.getCC().getResolution();
    }

    private BLink findLinkNearXY(double wx, double wy) {
        BLink closest = null;
        double minDist = clickTolerance_ * canvas_.getCC().getResolution();
        for (BLink link : visibleLinks_) {
            double dist = link.distToPoint(wx, wy);
            if (dist < minDist) {
                minDist = dist;
                closest = link;
            }
        }
        return closest;
    }

    private BNode findNodeNearXY(double wx, double wy) {
        BNode closest = null;
        double minDist = clickTolerance_ * canvas_.getCC().getResolution();
        for (BNode node : visibleNodes_) {
            double x = node.getX(), y = node.getY();
            double dist = FPUtil.magnitude(x, y, wx, wy);
            if (dist < minDist) {
                minDist = dist;
                closest = node;
            }
        }
        return closest;
    }

    public void setPointsOfInterest(Set<? extends PointOfInterest> points) {
        pointsOfInterest_ = (Set<PointOfInterest>) points;
    }

    public void clearPointsOfInterest() {
        pointsOfInterest_.clear();
    }

    public PointOfInterest getPOIFromXY(int cmx, int cmy) {
        PointOfInterest closest = null;
        double minDist = 15;
        for (PointOfInterest poi : pointsOfInterest_) {
            double dist = FPUtil.magnitude(cmx, cmy, poi.getScreenX(), poi.getScreenY());
            if (dist < minDist) {
                minDist = dist;
                closest = poi;
            }
        }
        return closest;
    }

    public void selectLinksFromKey(String key) {
        selLinks_.clear();
        Iterator<BLinkStreet> links = ac_.getEngine().getLocations().getStreetLinksFromKey(key);
        if (links != null) {
            while (links.hasNext()) {
                selLinks_.add(links.next());
            }
            refresh(true, false, false);
        } else {
            ac_.msg("selectLinksFromKey returned null for " + key);
        }
    }

    public void endNewLinkChain() {
        newLinkStart_ = newLinkEnd_ = null;
    }

    public void setDrawStreetNetwork(boolean value) {
        drawStreetNet_ = value;
    }

    public void setDrawRouteNetwork(boolean value) {
        drawRouteNet_ = value;
    }

    private void selectNodesInRange(double wx1, double wy1, double wx2, double wy2) {
        selLinks_.clear();
        selNodes_.clear();
        Iterator<BNode> vNodes = visibleNodes_.iterator();
        while (vNodes.hasNext()) {
            BNode node = vNodes.next();
            if (node.getX() > wx1 && node.getX() < wx2 && node.getY() > wy1 && node.getY() < wy2) selNodes_.add(node);
        }
    }

    private void selectLinksInRange(double wx1, double wy1, double wx2, double wy2) {
        selLinks_.clear();
        selNodes_.clear();
        Iterator<BLink> vLinks = visibleLinks_.iterator();
        while (vLinks.hasNext()) {
            BLink link = vLinks.next();
            if (link.getBoundingBox().intersects(wx1, wy1, wx2 - wx1, wy2 - wy1)) selLinks_.add(link);
        }
    }
}
