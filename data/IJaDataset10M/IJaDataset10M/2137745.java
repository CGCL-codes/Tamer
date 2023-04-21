package org.f2o.absurdum.puck.gui.panels;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.f2o.absurdum.puck.gui.graph.GraphEditingPanel;
import org.f2o.absurdum.puck.gui.graph.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author carlos
 *
 * Created at regulus, 20-jul-2005 19:40:27
 */
public class GraphElementPanel extends JPanel {

    private String id;

    private GraphEditingPanel gep;

    static {
        new BackgroundInitThread().start();
    }

    public GraphElementPanel() {
        super();
        this.id = PanelIDGenerator.newID();
    }

    public void setGraphEditingPanel(GraphEditingPanel gep) {
        this.gep = gep;
    }

    public void linkWithGraph() {
        ;
    }

    public void refresh() {
        ;
    }

    public GraphEditingPanel getGraphEditingPanel() {
        return gep;
    }

    public String getID() {
        return id;
    }

    public String getNameForElement() {
        return "";
    }

    public static boolean CACHE = true;

    private boolean initted = false;

    private org.w3c.dom.Node cachedNode = null;

    public boolean isCacheEnabled() {
        return CACHE && !(this instanceof WorldPanel);
    }

    public final synchronized void forceRealInitFromXml(boolean blocking) {
        if (isCacheEnabled() && !initted && cachedNode != null) {
            try {
                if (!blocking) wait(100);
                Runnable r = new Runnable() {

                    public void run() {
                        synchronized (GraphElementPanel.this) {
                            if (cachedNode != null) {
                                if (GraphElementPanel.this instanceof ArrowPanel && !((ArrowPanel) GraphElementPanel.this).hasSourceAndDestination()) return;
                                doInitFromXML(cachedNode);
                                initted = true;
                                cachedNode = null;
                                if (GraphElementPanel.this instanceof ArrowPanel) ((ArrowPanel) GraphElementPanel.this).forceRealCustomRelationshipsInitFromXML();
                            }
                        }
                    }
                };
                if (blocking) {
                    if (SwingUtilities.isEventDispatchThread()) r.run(); else SwingUtilities.invokeAndWait(r);
                } else SwingUtilities.invokeLater(r);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setVisible(boolean visible) {
        if (visible) forceRealInitFromXml(true);
        super.setVisible(visible);
    }

    public static void emptyQueue() {
        cachedNotInitted.clear();
    }

    private static LinkedBlockingQueue cachedNotInitted = new LinkedBlockingQueue();

    public final void initFromXML(org.w3c.dom.Node n) {
        if (isCacheEnabled()) {
            synchronized (this) {
                cachedNode = n;
            }
            {
                cachedNotInitted.offer(this);
            }
        } else doInitFromXML(n);
    }

    static class BackgroundInitThread extends Thread {

        public void run() {
            this.setPriority(Thread.MIN_PRIORITY);
            for (; ; ) {
                try {
                    GraphElementPanel g;
                    g = (GraphElementPanel) cachedNotInitted.take();
                    synchronized (g) {
                        if (!g.initted) g.forceRealInitFromXml(false);
                    }
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

    public final void initMinimal() {
        if (isCacheEnabled() && !initted && cachedNode != null) {
            Element e = (Element) cachedNode;
            doInitMinimal(e);
        }
        doInitMinimal();
    }

    public final org.w3c.dom.Node getXML(Document d) {
        forceRealInitFromXml(true);
        return doGetXML(d);
    }

    public void doInitMinimal(org.w3c.dom.Node e) {
        ;
    }

    public void doInitMinimal() {
        ;
    }

    public void doInitFromXML(org.w3c.dom.Node n) {
        ;
    }

    public org.w3c.dom.Node doGetXML(org.w3c.dom.Document d) {
        return null;
    }
}
