package verinec.netsim.gui;

import java.awt.Color;
import org.jdom.Element;
import verinec.gui.core.DrawPanel;
import verinec.gui.core.PCNode;

/**Represents an Event of the Applicationlayer.
 *  
 * @author Renato Loeffel
 */
public class Layer5Event extends SimuEvent {

    private String program;

    private String type;

    private String parameters;

    private PCNode myOwner;

    private int startX;

    private int startY;

    private int endX;

    private int endY;

    private double dx;

    private double dy;

    /**Initialize variables and calculates the data for the animation.
	 * 
	 * @param event the definition of the event.
	 * @param drawPanel a reference to the drawPanel.
	 * @throws SimulationException If the Node is not found on the drawPanel.
	 */
    public Layer5Event(Element event, DrawPanel drawPanel) throws SimulationException {
        super(event, drawPanel);
        this.setBackground(new Color(255, 0, 0));
        myOwner = drawPanel.getPCNode(event.getAttributeValue("node"));
        if (myOwner == null) {
            throw new SimulationException("Node " + event.getAttributeValue("node") + " not found");
        }
        startX = myOwner.getX() + myOwner.getWidth() / 2;
        startY = myOwner.getY() + myOwner.getHeight() / 2;
        endX = myOwner.getX() - myOwner.getWidth() / 2;
        endY = myOwner.getY() - myOwner.getHeight() / 2;
        dx = (startX - endX) / 100.0;
        dy = (startY - endY) / 100.0;
        Element child = event.getChild("application", VERINEC_NAMESPACE);
        if (child != null) {
            program = child.getAttributeValue("program");
            type = child.getAttributeValue("type");
            parameters = child.getAttributeValue("parameters");
        }
        child = event.getChild("consume", VERINEC_NAMESPACE);
        if (child != null) {
            program = "consume";
            type = child.getAttributeValue("type");
            parameters = "";
        }
        setText("<html><body>" + "<h1>" + program + "</h1>" + "<h3>" + type + "</h3>" + "<h6>" + parameters + "</h6>" + "</body></html>");
        initializePosition();
    }

    /**Perform the actual animation. 
	 * That is, it resizes and moves the Event, so the user gets the impression
	 * the Event appears on the node.
     *
	 * @param time Current simulation time.
	 * @param substep Current substep.
	 * @param myPhase Phase the event belongs to.
	 * @param totalPhase Total number of events occurring at the same time and location.
	 */
    public void animate(int time, int substep, int myPhase, int totalPhase) {
        super.animate(time, substep, myPhase, totalPhase);
        if (substep <= 100) {
            int newX = startX - (int) (substep * dx);
            int newY = startY - (int) (substep * dy);
            int newWidth = (int) (2 * substep * dx);
            int newHeight = (int) (2 * substep * dy);
            this.setBounds(newX, newY, newWidth, newHeight);
        } else if (substep > SimulatorThread.SUBSTEP_UNITY - 100) {
            int newX = startX - (int) ((SimulatorThread.SUBSTEP_UNITY - substep) * dx);
            int newY = startY - (int) ((SimulatorThread.SUBSTEP_UNITY - substep) * dy);
            int newWidth = (int) (2 * (SimulatorThread.SUBSTEP_UNITY - substep) * dx);
            int newHeight = (int) (2 * (SimulatorThread.SUBSTEP_UNITY - substep) * dy);
            this.setBounds(newX, newY, newWidth, newHeight);
        } else {
            this.setBounds(endX, endY, (int) (200 * dx), (int) (200 * dy));
        }
    }

    /**
	 * Initialize the position.
	 */
    public void initializePosition() {
        this.setBounds(startX, startY, 0, 0);
    }
}
