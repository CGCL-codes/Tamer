package bee.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Scene Manager supporing BSP.
 * @author boto
 */
public class BspSceneManager implements SceneManager {

    protected SceneNode topNode = null;

    protected BSP bsp = null;

    protected List<SceneNode> visibleNodes = new ArrayList<SceneNode>();

    protected List<SceneNode> sceneNodes = new ArrayList<SceneNode>();

    protected boolean drawStats = false;

    protected Vec2dInt cameraPos = new Vec2dInt();

    protected Vec2dInt viewDims = new Vec2dInt(512, 512);

    protected Vec4dInt viewArea = new Vec4dInt(0, 0, viewDims.x, viewDims.y);

    protected Vec2dInt screenDim = Core.get().getConfig().getScreenDims();

    protected float pastTime = 0.0f;

    protected int frameCnt = 0;

    protected int fps = 0;

    protected Vec4dInt highlightRegion = null;

    protected Color highlightColor = Color.red;

    protected List<SceneNode> bspNodes = new ArrayList<SceneNode>();

    protected boolean processBSP = false;

    private int frameNumber = 0;

    public void addToScene(SceneNode node) {
        if (topNode == null) {
            sceneNodes.add(node);
        } else {
            topNode.addChild(node);
            topNode.calculateBounds();
        }
    }

    public void removeFromScene(SceneNode node) {
        if (topNode == null) {
            sceneNodes.remove(node);
        } else {
            List<SceneNode> parents = node.getParents();
            for (int p = 0; p < parents.size(); p++) {
                parents.get(p).children.remove(node);
            }
            topNode.calculateBounds();
        }
    }

    public void setupScene() {
        topNode = new SceneNode("$TopNode");
        topNode.type = SceneNode.TYPE_BSP_CELL;
        for (SceneNode node : sceneNodes) {
            topNode.addChild(node);
        }
        topNode.calculateBounds();
        topNode.setEnableCulling(false);
        sceneNodes.clear();
        processBSP = false;
    }

    public void setupScene(int maxcellsize, int maxdepth) {
        bsp = new BSP();
        topNode = bsp.buildBSP(sceneNodes, maxcellsize, maxdepth);
        if (topNode == null) {
            topNode = new SceneNode("$TopNode");
        }
        topNode.calculateBounds();
        topNode.setEnableCulling(false);
        sceneNodes.clear();
        processBSP = true;
    }

    public void clearScene() {
        visibleNodes.clear();
        sceneNodes.clear();
        if (topNode != null) {
            topNode.clear();
            topNode = null;
        }
        bsp = null;
        processBSP = false;
        SceneNode.clearResourceCache();
        drawStats = false;
        highlightRegion = null;
    }

    public SceneNode getTopNode() {
        return topNode;
    }

    public void setCameraPosition(Vec2dInt pos) {
        cameraPos = pos;
    }

    public Vec2dInt getCameraPosition() {
        return cameraPos;
    }

    public void setViewDimension(Vec2dInt dim) {
        viewDims = dim;
    }

    public Vec2dInt getViewDimension() {
        return viewDims;
    }

    public void setScreenDimension(Vec2dInt dim) {
        screenDim = dim;
    }

    public Vec2dInt getScreenDimension() {
        return screenDim;
    }

    /**
     * Get the current frames/second;
     */
    public int getFPS() {
        return fps;
    }

    public void update(float deltatime) {
        if (topNode == null) {
            Log.error(getClass().getSimpleName() + ": cannot update scene, the scene has not been setup. use setupScene() before");
            return;
        }
        visibleNodes.clear();
        viewArea.x1 = cameraPos.x;
        viewArea.y1 = cameraPos.y;
        viewArea.x2 = cameraPos.x + viewDims.x;
        viewArea.y2 = cameraPos.y + viewDims.y;
        final List<SceneNode> nodes = new ArrayList<SceneNode>();
        if (processBSP) {
            bspNodes.clear();
            for (SceneNode cell : topNode.getChildren()) {
                cell.cull(viewArea, nodes, SceneNode.TYPE_BSP_CELL, true);
            }
            for (SceneNode cell : nodes) {
                cell.cull(viewArea, bspNodes, SceneNode.TYPE_BSP_LEAF, false);
            }
            for (SceneNode bcell : bspNodes) {
                bcell.apply(new SceneNode.Visitor() {

                    @Override
                    public void apply(SceneNode n) {
                        if (((n.getType() & SceneNode.TYPE_BSP_LEAF) == 0) && (n.getType() & SceneNode.TYPE_VISIBLE) != 0) {
                            visibleNodes.add(n);
                        }
                        for (SceneNode c : n.getChildren()) {
                            c.apply(this);
                        }
                    }
                });
            }
            for (SceneNode child : topNode.getChildren()) {
                if ((child.getType() & SceneNode.TYPE_VISIBLE) != 0) visibleNodes.add(child);
            }
        } else {
            topNode.cull(viewArea, visibleNodes, SceneNode.TYPE_VISIBLE, false);
        }
        nodes.clear();
        for (SceneNode n : visibleNodes) {
            n.apply(new SceneNode.Visitor() {

                @Override
                public void apply(SceneNode n) {
                    for (SceneNode c : n.getChildren()) {
                        if (c.enable) {
                            if ((c.getType() & SceneNode.TYPE_VISIBLE) != 0) {
                                nodes.add(c);
                            }
                            c.apply(this);
                        }
                    }
                }
            });
        }
        visibleNodes.addAll(nodes);
        class CompNodes implements Comparator<SceneNode> {

            public int compare(SceneNode node1, SceneNode node2) {
                return node2.getLayer() - node1.getLayer();
            }
        }
        CompNodes comp = new CompNodes();
        Collections.sort(visibleNodes, comp);
        pastTime += deltatime;
        frameCnt++;
        if (pastTime > 1.0f) {
            fps = frameCnt;
            pastTime -= 1.0f;
            frameCnt = 0;
        }
    }

    public void setEnableStats(boolean en) {
        drawStats = en;
    }

    public boolean getEnableStats() {
        return drawStats;
    }

    public void setHighlight(Vec4dInt rectangle) {
        highlightRegion = rectangle;
    }

    public Vec4dInt getHighlight() {
        return highlightRegion;
    }

    public void setHighlightColor(Color col) {
        highlightColor = col;
    }

    public Color getHighlightColor() {
        return highlightColor;
    }

    public void draw(Graphics g) {
        int xoffset = (screenDim.x - viewDims.x) / 2;
        int yoffset = (screenDim.y - viewDims.y) / 2;
        Vec2dInt trans = cameraPos.mult(-1, -1);
        trans.x += xoffset;
        trans.y += yoffset;
        drawScene(g, trans);
    }

    protected void drawScene(Graphics g, Vec2dInt trans) {
        frameNumber++;
        for (SceneNode n : visibleNodes) {
            if (drawStats) {
                n.drawOutlineRectangle(g, trans);
            }
            n.draw(g, trans, frameNumber);
        }
        if (drawStats && (bsp != null)) bsp.drawBspNodes(g, topNode, viewArea, trans);
        if (highlightRegion != null) {
            Line2D left = new Line2D.Float(highlightRegion.x1, highlightRegion.y1, highlightRegion.x1, highlightRegion.y2);
            Line2D top = new Line2D.Float(highlightRegion.x1, highlightRegion.y1, highlightRegion.x2, highlightRegion.y1);
            Line2D right = new Line2D.Float(highlightRegion.x2, highlightRegion.y1, highlightRegion.x2, highlightRegion.y2);
            Line2D bottom = new Line2D.Float(highlightRegion.x1, highlightRegion.y2, highlightRegion.x2, highlightRegion.y2);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            g2.setColor(highlightColor);
            g2.draw(left);
            g2.draw(top);
            g2.draw(right);
            g2.draw(bottom);
        }
        if (drawStats) {
            Vec4dInt camview = new Vec4dInt(viewArea.x1 + trans.x, viewArea.y1 + trans.y, viewArea.x2 + trans.x, viewArea.y2 + trans.y);
            Line2D left = new Line2D.Float(camview.x1, camview.y1, camview.x1, camview.y2);
            Line2D top = new Line2D.Float(camview.x1, camview.y1, camview.x2, camview.y1);
            Line2D right = new Line2D.Float(camview.x2, camview.y1, camview.x2, camview.y2);
            Line2D bottom = new Line2D.Float(camview.x1, camview.y2, camview.x2, camview.y2);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(6, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 15.0f, new float[] { 10.0f, 10.0f }, 5.0f));
            g2.setColor(Color.yellow);
            g2.draw(left);
            g2.draw(top);
            g2.draw(right);
            g2.draw(bottom);
            String text1 = new String("FPS " + fps);
            String text2 = new String("Screen Size " + screenDim.toString());
            String text3 = new String("BSP Leaves " + bspNodes.size());
            String text4 = new String("Render Scene Nodes " + visibleNodes.size());
            String text5 = new String("Camera Position " + cameraPos.toString());
            int size = 12;
            g2.setColor(Color.white);
            Font font = new Font("Helvetica", Font.PLAIN, size);
            FontRenderContext frc = g2.getFontRenderContext();
            TextLayout tl1 = new TextLayout(text1, font, frc);
            TextLayout tl2 = new TextLayout(text2, font, frc);
            TextLayout tl3 = new TextLayout(text3, font, frc);
            TextLayout tl4 = new TextLayout(text4, font, frc);
            TextLayout tl5 = new TextLayout(text5, font, frc);
            Vec2dInt offset = new Vec2dInt(6, viewDims.y - 76);
            tl1.draw(g2, offset.x + camview.x1, offset.y + camview.y1 + 1 * (2 + size));
            tl2.draw(g2, offset.x + camview.x1, offset.y + camview.y1 + 2 * (2 + size));
            tl3.draw(g2, offset.x + camview.x1, offset.y + camview.y1 + 3 * (2 + size));
            tl4.draw(g2, offset.x + camview.x1, offset.y + camview.y1 + 4 * (2 + size));
            tl5.draw(g2, offset.x + camview.x1, offset.y + camview.y1 + 5 * (2 + size));
        }
    }
}
