package it.unipr.ce.dsg.deus.editor;

import javax.swing.JOptionPane;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Iterator;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.graph.Graph;

/**
 * A class that controls the relation of vertices when mouse is clicked or
 * released
 * 
 * @author Fabrizio Caramia (caramia@ce.unipr.it)
 * @author Mario Sabbatelli (smario@ce.unipr.it)
 * 
 */
public class CheckLinkMouseListener implements MouseListener {

    private DeusEdge edge;

    private DeusVertex vertPicked;

    private Graph<DeusVertex, DeusEdge> graph;

    private DeusVertex vertNext;

    private VisualizationViewer<DeusVertex, DeusEdge> vv;

    @SuppressWarnings("unchecked")
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Point2D p = e.getPoint();
            VisualizationViewer<DeusVertex, DeusEdge> vv = (VisualizationViewer<DeusVertex, DeusEdge>) e.getSource();
            GraphElementAccessor<DeusVertex, DeusEdge> pickSupport = vv.getPickSupport();
            DeusVertex vertPickedfirst = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
            if (vertPickedfirst != null) {
                if ((vertPickedfirst.getElementType() == "Engine" || vertPickedfirst.getElementType() == "Network") && vv != null) {
                    searchDuplicateEngine(vertPickedfirst.getElementType());
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Point2D p = e.getPoint();
            vv = (VisualizationViewer<DeusVertex, DeusEdge>) e.getSource();
            GraphElementAccessor<DeusVertex, DeusEdge> pickSupport = vv.getPickSupport();
            vertPicked = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
            graph = vv.getGraphLayout().getGraph();
            if (vertPicked != null) {
                Iterator<DeusVertex> vert = graph.getPredecessors(vertPicked).iterator();
                while (vert.hasNext()) {
                    vertNext = vert.next();
                    if (vertPicked.getElementType() == "Node") {
                        if (vertNext.getElementType() == "Engine") {
                            checkRelation();
                        } else if (vertNext.getElementType() == "Event") {
                            checkRelation();
                        } else if (vertNext.getElementType() == "Node") {
                            checkRelation();
                        }
                    } else if (vertPicked.getElementType() == "Engine") {
                        if (vertNext.getElementType() == "Node") {
                            checkRelation();
                        } else if (vertNext.getElementType() == "Network") {
                            checkRelation();
                        } else if (vertNext.getElementType() == "Event") {
                            checkRelation();
                        } else if (vertNext.getElementType() == "Process") {
                            checkRelation();
                        } else if (vertNext.getElementType() == "Engine") {
                            checkRelation();
                        }
                    } else if (vertPicked.getElementType() == "Event") {
                        if (vertNext.getElementType() == "Engine") {
                            checkRelation();
                        } else if (vertNext.getElementType() == "Network") {
                            checkRelation();
                        } else if (vertNext.getElementType() == "Node") {
                            checkRelation();
                        }
                    } else if (vertPicked.getElementType() == "Process") {
                        if (vertNext.getElementType() == "Node") {
                            checkRelation();
                        } else if (vertNext.getElementType() == "Event") {
                            checkRelation();
                        } else if (vertNext.getElementType() == "Network") {
                            checkRelation();
                        } else if (vertNext.getElementType() == "Process") {
                            checkRelation();
                        }
                    } else if (vertPicked.getElementType() == "Network") {
                        if (vertNext.getElementType() == "Node") {
                            checkRelation();
                        } else if (vertNext.getElementType() == "Event") {
                            checkRelation();
                        } else if (vertNext.getElementType() == "Network") {
                            checkRelation();
                        } else if (vertNext.getElementType() == "Process") {
                            checkRelation();
                        } else if (vertNext.getElementType() == "Engine") {
                            checkRelation();
                        }
                    }
                }
            }
        }
    }

    private void checkRelation() {
        JOptionPane.showMessageDialog(MainGUI.frame, "This relation isn't possible!", "Error", JOptionPane.ERROR_MESSAGE);
        edge = graph.findEdge(vertNext, vertPicked);
        vv.getGraphLayout().getGraph().removeEdge(edge);
        vv.repaint();
    }

    private void searchDuplicateEngine(String type) {
        int i = 0;
        Collection<DeusVertex> ver = graph.getVertices();
        if (ver != null) {
            for (DeusVertex v : ver) if (v.getElementType() == type) {
                i++;
            }
            if (i > 1) {
                JOptionPane.showMessageDialog(MainGUI.frame, "Only one " + type + " is possible!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
