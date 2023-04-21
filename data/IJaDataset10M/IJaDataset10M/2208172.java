package net.matmas.pneditor.features;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.matmas.pnapi.Arc;
import net.matmas.pnapi.DrawingOptions;
import net.matmas.pnapi.Element;
import net.matmas.pnapi.Node;
import net.matmas.pnapi.Place;
import net.matmas.pnapi.Transition;
import net.matmas.pneditor.Canvas;
import net.matmas.pneditor.PNEditor;
import net.matmas.pneditor.commands.AddArcCommand;
import net.matmas.pneditor.commands.AddPlaceCommand;
import net.matmas.pneditor.commands.AddTransitionCommand;
import net.matmas.pneditor.commands.SetArcMultiplicityCommand;
import net.matmas.util.CollectionTools;
import net.matmas.util.Point;

/**
 *
 * @author matmas
 */
public class ArcFeature extends Feature implements MouseListener, MouseMotionListener, KeyListener {

    public ArcFeature(Canvas canvas) {
        super(canvas);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addKeyListener(this);
    }

    private Element sourceElement = null;

    private Arc connectingArc = null;

    private Node helperNode = null;

    private List<Element> backgroundElements = new ArrayList<Element>();

    private boolean started = false;

    private boolean dragging = false;

    private DrawingOptions drawingOptions = new DrawingOptions();

    public void mousePressed(MouseEvent e) {
        Point clickedPoint = getClickedPoint(e);
        Element clickedElement = getClickedElement(clickedPoint);
        if (PNEditor.getInstance().getToolSelector().isSelectedTool_Arc()) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (clickedElement instanceof Node) {
                    if (!started) {
                        sourceElement = clickedElement;
                        helperNode = sourceElement instanceof Transition ? new Place() : new Transition();
                        helperNode.setCenter(clickedPoint);
                        connectingArc = new Arc();
                        connectingArc.setStart(sourceElement.getCenter());
                        connectingArc.setDestination(helperNode);
                        drawingOptions.setElementColor(connectingArc, Color.blue);
                        drawingOptions.setElementColor(helperNode, Color.blue);
                        backgroundElements.add(connectingArc);
                        backgroundElements.add(helperNode);
                        started = true;
                        dragging = true;
                    }
                }
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (started) {
                    cancelDraggingAndRefreshCanvas(e);
                } else {
                    PNEditor.getInstance().getToolSelector().selectTool_Select();
                }
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
        mouseDragged(e);
    }

    public void mouseDragged(MouseEvent e) {
        Point clickedPoint = getClickedPoint(e);
        Element targetElement = getClickedElement(clickedPoint);
        if (started) {
            if (sourceElement instanceof Place && targetElement instanceof Transition || sourceElement instanceof Transition && targetElement instanceof Place) {
                helperNode.setCenter(targetElement.getCenter());
            } else {
                helperNode.setCenter(clickedPoint);
            }
            canvas.repaint();
        }
    }

    public void mouseReleased(MouseEvent e) {
        Point clickedPoint = getClickedPoint(e);
        Element targetElement = getClickedElement(clickedPoint);
        if (started) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (sourceElement != targetElement) {
                    Set<Element> newlyCreatedElements = new HashSet<Element>();
                    if (targetElement == null) {
                        if (sourceElement instanceof Place) {
                            PNEditor.getInstance().getUndoManager().executeCommand(new AddTransitionCommand(clickedPoint, PNEditor.getInstance().getDocument().getPetriNet()));
                            targetElement = CollectionTools.getLastElement(PNEditor.getInstance().getDocument().getPetriNet().getTransitions());
                            newlyCreatedElements.add(targetElement);
                        }
                        if (sourceElement instanceof Transition) {
                            PNEditor.getInstance().getUndoManager().executeCommand(new AddPlaceCommand(clickedPoint, PNEditor.getInstance().getDocument().getPetriNet()));
                            targetElement = CollectionTools.getLastElement(PNEditor.getInstance().getDocument().getPetriNet().getPlaces());
                            newlyCreatedElements.add(targetElement);
                        }
                    }
                    if (sourceElement instanceof Place && targetElement instanceof Transition || sourceElement instanceof Transition && targetElement instanceof Place) {
                        boolean placeToTransition = sourceElement instanceof Place && targetElement instanceof Transition;
                        Place place;
                        Transition transition;
                        if (placeToTransition) {
                            place = (Place) sourceElement;
                            transition = (Transition) targetElement;
                        } else {
                            transition = (Transition) sourceElement;
                            place = (Place) targetElement;
                        }
                        Arc arc = PNEditor.getInstance().getDocument().getPetriNet().getArc(place, transition, placeToTransition);
                        if (arc == null) {
                            if (transition instanceof Transition) {
                                PNEditor.getInstance().getUndoManager().executeCommand(new AddArcCommand(place, (Transition) transition, placeToTransition));
                            }
                            newlyCreatedElements.add(CollectionTools.getLastElement(PNEditor.getInstance().getDocument().getPetriNet().getArcs()));
                        } else {
                            PNEditor.getInstance().getUndoManager().executeCommand(new SetArcMultiplicityCommand(arc, arc.getMultiplicity() + 1));
                            canvas.getSelection().replaceWith(arc);
                        }
                    }
                    canvas.getSelection().replaceWith(newlyCreatedElements);
                    cancelDraggingAndRefreshCanvas(e);
                    if (!dragging) {
                        mousePressed(e);
                    }
                }
                if (sourceElement == targetElement) {
                    dragging = false;
                }
            }
        }
    }

    @Override
    public void setElementColors(MouseEvent e, Map<Element, Color> elementColors) {
        Point clickedPoint = getClickedPoint(e);
        Element targetElement = getClickedElement(clickedPoint);
        if (PNEditor.getInstance().getToolSelector().isSelectedTool_Arc()) {
            if (started) {
                if (!(targetElement instanceof Node)) {
                    elementColors.put(sourceElement, Color.blue);
                    drawingOptions.setElementColor(connectingArc, Color.blue);
                    drawingOptions.setElementColor(helperNode, Color.blue);
                } else {
                    if (sourceElement instanceof Place && targetElement instanceof Transition || sourceElement instanceof Transition && targetElement instanceof Place) {
                        elementColors.put(sourceElement, Color.green);
                        elementColors.put(targetElement, Color.green);
                        drawingOptions.setElementColor(connectingArc, Color.green);
                    } else if (sourceElement == targetElement) {
                        elementColors.put(sourceElement, Color.blue);
                    } else if (targetElement instanceof Node) {
                        elementColors.put(sourceElement, Color.red);
                        elementColors.put(targetElement, Color.red);
                        drawingOptions.setElementColor(connectingArc, Color.red);
                        drawingOptions.setElementColor(helperNode, Color.red);
                    }
                }
            }
        }
    }

    public void drawBackground(Graphics g) {
        for (Element element : backgroundElements) {
            element.draw(g, drawingOptions);
        }
    }

    private void cancelDraggingAndRefreshCanvas(MouseEvent e) {
        cancelDragging();
        canvas.refresh(e);
    }

    private void cancelDragging() {
        sourceElement = null;
        backgroundElements.clear();
        started = false;
    }

    public void keyPressed(KeyEvent e) {
        if (KeyEvent.getKeyText(e.getKeyCode()).equals("Escape")) {
            if (started) {
                cancelDragging();
                canvas.repaint();
            }
        }
    }

    public void drawForeground(Graphics g) {
    }

    public void drawMainLayer(Graphics g) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
