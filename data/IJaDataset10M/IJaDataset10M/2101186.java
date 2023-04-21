package net.matmas.pneditor.features;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.swing.JOptionPane;
import net.matmas.pnapi.Element;
import net.matmas.pnapi.Marking;
import net.matmas.pnapi.Transition;
import net.matmas.pnapi.properties.RoleProperty;
import net.matmas.pneditor.Canvas;
import net.matmas.pneditor.PNEditor;
import net.matmas.util.Point;
import xesloganalyzer.XESEvent;
import xesloganalyzer.XESTrace;

/**
 *
 * @author matmas
 */
public class FireFeature extends Feature implements MouseListener {

    private boolean simulationInProgress = false;

    private Marking oldMarking = null;

    private XESTrace newTrace;

    private XESEvent event = null;

    public FireFeature(Canvas canvas) {
        super(canvas);
        canvas.addMouseListener(this);
    }

    public void mousePressed(MouseEvent e) {
        Point clickedPoint = getClickedPoint(e);
        Element clickedElement = getClickedElement(clickedPoint);
        Marking currentmarking = PNEditor.getInstance().getDocument().getPetriNet().getMarking();
        if (PNEditor.getInstance().getToolSelector().isSelectedTool_Fire()) {
            if (clickedElement instanceof Transition) {
                Transition transition = (Transition) clickedElement;
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (currentmarking.isEnabled(transition)) {
                        if (!simulationInProgress) {
                            simulationInProgress = true;
                            oldMarking = new Marking(currentmarking);
                            if (canvas.isLogging) {
                                newTrace = canvas.newTrace;
                            }
                        }
                        if (canvas.isLogging) {
                            String resource = "";
                            String time = "";
                            String name = "UNDEFINED";
                            String[] resources = new String[100];
                            if (transition.getProperties().getFilteredByClass(RoleProperty.class).size() == 0) {
                                resource = "UNDEFINED";
                            }
                            if (transition.getProperties().getFilteredByClass(RoleProperty.class).size() == 1) {
                                resource = transition.getProperties().getFilteredByClass(RoleProperty.class).get(0).getRoleId();
                            }
                            if (transition.getProperties().getFilteredByClass(RoleProperty.class).size() > 1) {
                                int i = 0;
                                for (RoleProperty proprol : transition.getProperties().getFilteredByClass(RoleProperty.class)) {
                                    resources[i] = proprol.getRoleId();
                                    i++;
                                }
                                resource = (String) JOptionPane.showInputDialog(null, "Choose role for transition", "Roles for transition", JOptionPane.QUESTION_MESSAGE, null, resources, resources[0]);
                            }
                            if (resource == null) {
                                resource = "UNDEFINED";
                            }
                            SimpleDateFormat ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date now = new Date();
                            time = ISO8601Local.format(now);
                            if (!transition.getLabel().getText().isEmpty()) {
                                name = transition.getLabel().getText().toString();
                            }
                            event = new XESEvent(name, time, resource, "complete");
                            newTrace.addEventToTrace(event);
                            PNEditor.getInstance().getMainFrame().infolog.setText("Event added --- event properies : name=" + name + "  time=" + time + "  resource=" + resource + "  lifecycle=complete");
                        }
                        currentmarking.fire(transition);
                        canvas.repaint();
                    }
                }
            }
            if (clickedElement == null) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (simulationInProgress) {
                        simulationInProgress = false;
                        PNEditor.getInstance().getDocument().getPetriNet().setMarking(oldMarking);
                        canvas.repaint();
                    } else {
                        PNEditor.getInstance().getToolSelector().selectTool_Select();
                        if (canvas.isLogging) {
                            canvas.logtable.addTraceToLog(newTrace);
                            newTrace = null;
                            canvas.logtable.show();
                            canvas.setIsLogging(false);
                            PNEditor.getInstance().getMainFrame().panel.setVisible(false);
                            PNEditor.getInstance().getMainFrame().panel.repaint();
                        }
                    }
                }
            }
        }
    }

    public void drawForeground(Graphics g) {
        Marking currentMarking = PNEditor.getInstance().getDocument().getPetriNet().getMarking();
        if (PNEditor.getInstance().getToolSelector().isSelectedTool_Fire()) {
            for (Element element : PNEditor.getInstance().getDocument().getPetriNet()) {
                if (element instanceof Transition) {
                    Transition transition = (Transition) element;
                    if (currentMarking.isEnabled(transition)) {
                        g.setColor(Color.green);
                    } else {
                        g.setColor(Color.red);
                    }
                    ((Graphics2D) g).setStroke(new BasicStroke(2f));
                    g.drawRect(transition.getStart().getX() + 1, transition.getStart().getY() + 1, transition.getWidth() - 3, transition.getHeight() - 3);
                    ((Graphics2D) g).setStroke(new BasicStroke(1f));
                }
            }
        }
    }

    @Override
    public void setElementColors(MouseEvent e, Map<Element, Color> elementColors) {
        Point clickedPoint = getClickedPoint(e);
        Element targetElement = getClickedElement(clickedPoint);
        Marking currentMarking = PNEditor.getInstance().getDocument().getPetriNet().getMarking();
        if (PNEditor.getInstance().getToolSelector().isSelectedTool_Fire()) {
            if (targetElement instanceof Transition) {
                if (currentMarking.isEnabled((Transition) targetElement)) {
                    elementColors.put(targetElement, Color.green);
                } else {
                    elementColors.put(targetElement, Color.red);
                }
            }
        }
    }

    public void drawMainLayer(Graphics g) {
    }

    public void drawBackground(Graphics g) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
