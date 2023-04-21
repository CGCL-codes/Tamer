package geovista.touchgraph;

import java.util.logging.Level;
import java.util.logging.Logger;
import geovista.touchgraph.graphelements.TGForEachEdge;
import geovista.touchgraph.graphelements.TGForEachNode;
import geovista.touchgraph.graphelements.TGForEachNodePair;

/**
 * TGLayout is the thread responsible for graph layout. It updates the real
 * coordinates of the nodes in the graphEltSet object. TGPanel sends it
 * resetDamper commands whenever the layout needs to be adjusted. After every
 * adjustment cycle, TGLayout triggers a repaint of the TGPanel.
 * 
 * ******************************************************************** This is
 * the heart of the TouchGraph application. Please provide a Reference to
 * TouchGraph.com if you are influenced by what you see below. Your cooperation
 * will insure that this code remains opensource
 * ********************************************************************
 * 
 * <p>
 * <b> Parts of this code build upon Sun's Graph Layout example.
 * http://java.sun.com/applets/jdk/1.1/demo/GraphLayout/Graph.java </b>
 * </p>
 * 
 * @author Alexander Shapiro
 * 
 */
public class TGLayout implements Runnable {

    protected static final Logger logger = Logger.getLogger(TGLayout.class.getName());

    private final transient TGPanel tgPanel;

    private transient Thread relaxer;

    private transient double damper = 0.0;

    private transient double maxMotion = 0;

    private transient double lastMaxMotion = 0;

    private transient double motionRatio = 0;

    private transient boolean damping = true;

    private transient double rigidity = 1;

    private transient double newRigidity = 1;

    transient Node dragNode = null;

    /**
	 * Constructor with a supplied TGPanel <tt>tgp</tt>.
	 */
    public TGLayout(TGPanel tgp) {
        tgPanel = tgp;
        relaxer = null;
    }

    void setRigidity(double r) {
        newRigidity = r;
    }

    void setDragNode(Node n) {
        dragNode = n;
    }

    private synchronized void relaxEdges() {
        TGForEachEdge fee = new TGForEachEdge() {

            @Override
            public void forEachEdge(Edge e) {
                double vx = e.to.x - e.from.x;
                double vy = e.to.y - e.from.y;
                double len = Math.sqrt(vx * vx + vy * vy);
                double dx = vx * rigidity;
                double dy = vy * rigidity;
                dx /= (e.getLength() * 100);
                dy /= (e.getLength() * 100);
                if (e.to.justMadeLocal || !e.from.justMadeLocal) {
                    e.to.dx -= dx * len;
                    e.to.dy -= dy * len;
                } else {
                    e.to.dx -= dx * len / 10;
                    e.to.dy -= dy * len / 10;
                }
                if (e.from.justMadeLocal || !e.to.justMadeLocal) {
                    e.from.dx += dx * len;
                    e.from.dy += dy * len;
                } else {
                    e.from.dx += dx * len / 10;
                    e.from.dy += dy * len / 10;
                }
            }
        };
        tgPanel.getGES().forAllEdges(fee);
    }

    private synchronized void avoidLabels() {
        TGForEachNodePair fenp = new TGForEachNodePair() {

            @Override
            public void forEachNodePair(Node n1, Node n2) {
                double dx = 0;
                double dy = 0;
                double vx = n1.x - n2.x;
                double vy = n1.y - n2.y;
                double len = vx * vx + vy * vy;
                if (len == 0) {
                    dx = Math.random();
                    dy = Math.random();
                } else if (len < 600 * 600) {
                    dx = vx / len;
                    dy = vy / len;
                }
                int repSum = n1.repulsion * n2.repulsion / 100;
                if (n1.justMadeLocal || !n2.justMadeLocal) {
                    n1.dx += dx * repSum * rigidity;
                    n1.dy += dy * repSum * rigidity;
                } else {
                    n1.dx += dx * repSum * rigidity / 10;
                    n1.dy += dy * repSum * rigidity / 10;
                }
                if (n2.justMadeLocal || !n1.justMadeLocal) {
                    n2.dx -= dx * repSum * rigidity;
                    n2.dy -= dy * repSum * rigidity;
                } else {
                    n2.dx -= dx * repSum * rigidity / 10;
                    n2.dy -= dy * repSum * rigidity / 10;
                }
            }
        };
        tgPanel.getGES().forAllNodePairs(fenp);
    }

    public void startDamper() {
        damping = true;
    }

    public void stopDamper() {
        damping = false;
        damper = 1.0;
    }

    public void resetDamper() {
        damping = true;
        damper = 1.0;
    }

    public void damp() {
        if (damping) {
            if (motionRatio <= 0.001) {
                if ((maxMotion < 0.2 || (maxMotion > 1 && damper < 0.9)) && damper > 0.01) {
                    damper -= 0.01;
                } else if (maxMotion < 0.4 && damper > 0.003) {
                    damper -= 0.003;
                } else if (damper > 0.0001) {
                    damper -= 0.0001;
                }
            }
        }
        if (maxMotion < 0.001 && damping) {
            damper = 0;
        }
    }

    private synchronized void moveNodes() {
        lastMaxMotion = maxMotion;
        final double[] maxMotionA = new double[1];
        maxMotionA[0] = 0;
        TGForEachNode fen = new TGForEachNode() {

            @Override
            public void forEachNode(Node n) {
                double dx = n.dx;
                double dy = n.dy;
                dx *= damper;
                dy *= damper;
                n.dx = dx / 2;
                n.dy = dy / 2;
                double distMoved = Math.sqrt(dx * dx + dy * dy);
                if (!n.fixed && !(n == dragNode)) {
                    n.x += Math.max(-30, Math.min(30, dx));
                    n.y += Math.max(-30, Math.min(30, dy));
                }
                maxMotionA[0] = Math.max(distMoved, maxMotionA[0]);
            }
        };
        tgPanel.getGES().forAllNodes(fen);
        maxMotion = maxMotionA[0];
        if (maxMotion > 0) {
            motionRatio = lastMaxMotion / maxMotion - 1;
        } else {
            motionRatio = 0;
        }
        damp();
    }

    private synchronized void relax() {
        for (int i = 0; i < 10; i++) {
            relaxEdges();
            avoidLabels();
            moveNodes();
        }
        if (rigidity != newRigidity) {
            rigidity = newRigidity;
        }
        tgPanel.repaintAfterMove();
    }

    private void myWait() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
    }

    public void run() {
        Thread me = Thread.currentThread();
        while (relaxer == me) {
            relax();
            try {
                Thread.sleep(20);
                while (damper < 0.1 && damping && maxMotion < 0.001) {
                    myWait();
                }
                if (logger.isLoggable(Level.FINEST)) {
                    logger.finest("damping " + damping + " damp " + damper + "  maxM " + maxMotion + "  motR " + motionRatio);
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void start() {
        relaxer = new Thread(this);
        relaxer.start();
    }

    public void stop() {
        relaxer = null;
    }
}
