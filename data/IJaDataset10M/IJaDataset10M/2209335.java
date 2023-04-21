package org.mates.sim;

import org.mates.util.PriorityQueue;
import org.mates.util.Position;
import org.mates.util.Position2D;
import org.mates.sim.models.MobilityModel;
import org.mates.sim.models.CPUSchedulingModel;
import org.mates.sim.models.mobility.StaticMobilityModel;
import org.mates.sim.models.cpu.DefaultCPUSchedulingModel;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @author <a href="http://www.sultanik.com/" target="_blank">Evan Sultanik</a>
 */
public class Host {

    String name;

    Simulator simulator;

    Hashtable agents;

    Hashtable agents_by_name;

    MobilityModel mobility_model;

    CPUSchedulingModel scheduling_model;

    UniqueID uid;

    Position position;

    Color color;

    PriorityQueue agent_queue, processed_agents;

    double radio_range;

    /**
     * The color of a host when it is selected in the GUI.
     */
    protected Color SELECTED_COLOR = new Color(0, 128, 0, 192);

    /**
     * The color of a host when the mouse is held over it in the GUI.
     */
    protected Color MOUSEOVER_COLOR = new Color(0, 0, 255, 128);

    /**
     * The color of a host's radio range when it is drawn in the GUI.
     */
    protected Color RADIO_RANGE_COLOR = new Color(0, 255, 0, 128);

    /**
     * Primary constructor.  Uses a {@link org.mates.sim.models.cpu.DefaultCPUSchedulingModel}.
     *
     * @param name a not-necessarily-unique name for this host.  This is mostly for debugging and conveniance.  This may be <code>null</code>.
     * @param position the initial position of the host
     * @param mobilityModel the mobility model this host will use
     * @param simulator the simulator in which this host will live.
     */
    public Host(String name, Position position, MobilityModel mobilityModel, Simulator simulator) {
        agents = new Hashtable();
        agents_by_name = new Hashtable();
        agent_queue = new PriorityQueue();
        processed_agents = new PriorityQueue();
        color = Color.black;
        this.simulator = simulator;
        this.mobility_model = mobilityModel;
        scheduling_model = new DefaultCPUSchedulingModel();
        uid = simulator.newUniqueID();
        if (name == null) this.name = uid.toString(); else this.name = name;
        setRadioRangeToDefault();
        setPosition(position);
    }

    /**
     * Primary constructor.  Uses a {@link
     * org.mates.sim.models.cpu.DefaultCPUSchedulingModel} and {@link
     * org.mates.util.Position2D} position.
     *
     * @param name a not-necessarily-unique name for this host.  This is mostly for debugging and conveniance.  This may be <code>null</code>.
     * @param x the initial x coordinate of the host
     * @param y the initial y coordinate of the host
     * @param mobilityModel the mobility model this host will use
     * @param simulator the simulator in which this host will live.
     *
     * @see Host#Host(String, Position, MobilityModel, Simulator)
     */
    public Host(String name, double x, double y, MobilityModel mobilityModel, Simulator simulator) {
        this(name, new Position2D(x, y), mobilityModel, simulator);
    }

    /**
     * Constructs a host with a {@link
     * org.mates.sim.models.mobility.StaticMobilityModel} and {@link
     * org.mates.util.Position2D} position.
     *
     * @param name a not-necessarily-unique name for this host.  This is mostly for debugging and conveniance.  This may be <code>null</code>.
     * @param x the initial x coordinate of the host
     * @param y the initial y coordinate of the host
     * @param simulator the simulator in which this host will live.
     *
     * @see Host#Host(String, double, double, MobilityModel, Simulator)
     */
    public Host(String name, double x, double y, Simulator simulator) {
        this(name, x, y, new StaticMobilityModel(), simulator);
    }

    /**
     * Constructs a host without a name.  The name will default to the
     * host's automatically-asigned unique identifier.
     *
     * @param x the initial x coordinate of the host
     * @param y the initial y coordinate of the host
     * @param mobilityModel the mobility model this host will use
     * @param simulator the simulator in which this host will live.
     *
     * @see Host#Host(String, double, double, MobilityModel, Simulator)
     */
    public Host(double x, double y, MobilityModel mobilityModel, Simulator simulator) {
        this(null, x, y, mobilityModel, simulator);
    }

    /**
     * Constructs a host.  The mobility model defaults to a {@link
     * org.mates.sim.models.mobility.StaticMobilityModel}.
     *
     * @param x the initial x coordinate of the host
     * @param y the initial y coordinate of the host
     * @param simulator the simulator in which this host will live.
     *
     * @see Host#Host(String, double, double, Simulator)
     * @see Host#Host(double, double, MobilityModel, Simulator)
     * @see Host#Host(String, double, double, MobilityModel, Simulator)
     */
    public Host(double x, double y, Simulator simulator) {
        this(null, x, y, new StaticMobilityModel(), simulator);
    }

    /**
     * Sets the position of this host.
     *
     * @return <code>true</code> if the new position was valid.
     *
     * @see Simulator#isValidPosition(Position)
     */
    public boolean setPosition(Position newPosition) {
        if (simulator.isValidPosition(newPosition)) {
            position = newPosition;
            return true;
        }
        return false;
    }

    /**
     * Returns the current position of this host.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Called once per simulator iteration, it is this function's
     * responsibility to schedule processing time for each of the
     * agents currently on this host, respecting the agents'
     * priorities.
     */
    public void iterate() {
        scheduling_model.runAgents(this);
    }

    /**
     * Returns the name of this host.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an Enumeration of the agents currently on this host.
     * This does not include any agents in transit to or from this
     * host.
     */
    public Enumeration getAgents() {
        return agents.elements();
    }

    /**
     * Returns the radio range of this host.
     */
    public double getRadioRange() {
        return radio_range;
    }

    /**
     * Sets the radio range for this host.
     *
     * @param radio_range the new radio range for this host.
     */
    public void setRadioRange(double radio_range) {
        this.radio_range = radio_range;
    }

    /**
     * Sets this host's radio range to the default radio range as
     * defined by the simulator.
     *
     * This is equivalent to {@link Host#setRadioRange(double)
     * setRadioRange}<code>(</code>{@link Host#getSimulator()
     * getSimulator}<code>().</code>{@link
     * Simulator#getDefaultRadioRange()
     * getDefaultRadioRange}<code>())</code>.
     */
    public void setRadioRangeToDefault() {
        setRadioRange(simulator.getDefaultRadioRange());
    }

    /**
     * Returns this host's mobility model.
     */
    public MobilityModel getMobilityModel() {
        return mobility_model;
    }

    /**
     * Sets the mobility model of this host.
     *
     * @throws NullPointerException when <code>model == null</code>
     */
    public void setMobilityModel(MobilityModel model) {
        if (model == null) throw new NullPointerException();
        mobility_model = model;
    }

    /**
     * Returns this host's CPU scheduling model;
     */
    public CPUSchedulingModel getCPUSchedulingModel() {
        return scheduling_model;
    }

    /**
     * Sets the CPU scheduling model of this host.
     *
     * @throws NullPointerException when <code>model == null</code>
     */
    public void setCPUSchedulingModel(CPUSchedulingModel model) {
        if (model == null) throw new NullPointerException();
        scheduling_model = model;
    }

    /**
     * Adds an agent to this host.
     *
     * Note that this function will <i>not</i> set the {@link
     * Agent#host} member variable.  The effect of this function is
     * that the agent will now be scheduled execution time during each
     * call to {@link Host#iterate()}.  Also, the agent will not be
     * added if <code>agent.</code>{@link Agent#isAlive()
     * isAlive}<code>()</a> returns <code>false</code>.
     */
    public void addAgent(Agent agent) {
        if (!agent.isAlive()) return;
        agents.put(agent.getUniqueID(), agent);
        agents_by_name.put(agent.getName(), agent);
        agent_queue.push(agent, agent.getPriority());
    }

    /**
     * An internal function for updating this host to the fact that
     * one of its agents has updated its priority.
     *
     * In general, this function does not ever need to be called by
     * the user; it is automatically called from {@link
     * Agent#setPriority(int)}.
     */
    public void updateAgentPriority(Agent agent) {
        agent_queue.updatePriority(agent, agent.getPriority());
    }

    /**
     * Sets the color that this host should be displayed if a
     * graphical user interface is chosen.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns the color of this host, if it is to be displayed
     * graphically.  The dafault color is {@link Color#black}.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Removes an agent from this host.
     *
     * Note that this function does not alter the {@link Agent#host}
     * member variable.
     */
    public void removeAgent(Agent agent) {
        agents.remove(agent.getUniqueID());
        agents_by_name.remove(agent.getName());
        agent_queue.remove(agent);
        processed_agents.remove(agent);
    }

    /**
     * Returns an agent on this host.
     *
     * @return an agent with the associated identifier, if that agent
     * is currently on this host.  Returns <code>null</code> if the
     * agent is not present.
     */
    public Agent getAgent(UniqueID id) {
        return (Agent) agents.get(id);
    }

    /**
     * Returns an agent on this host.
     *
     * @return an agent with the associated name, if that agent is
     * currently on this host.  Since agent names are not unique, this
     * function will return the first match found.  Returns
     * <code>null</code> if the agent is not present.
     */
    public Agent getAgentByName(String name) {
        return (Agent) agents_by_name.get(name);
    }

    /**
     * Returns the unique identifier for this host.
     *
     * @return the unique identifier for this host. 
     */
    public UniqueID getUniqueID() {
        return uid;
    }

    /**
     * Conveniance function that calls {@link Host#getSimulator()
     * getSimulator}<code>().</code>{@link Simulator#getTopology()
     * getTopology}<code>().</code>{@link
     * org.mates.sim.network.Topology#getNeighborsHostCanHear(Host)
     * getNeighborsHostCanHear}<code>(this)</code>.
     */
    public Vector getNeighborsICanHear() {
        return simulator.getTopology().getNeighborsHostCanHear(this);
    }

    /**
     * Conveniance function that calls {@link Host#getSimulator()
     * getSimulator}<code>().</code>{@link
     * Simulator#getNeighbors(Host) getNeighbors}<code>(this)</code>.
     */
    public Vector getNeighbors() {
        return simulator.getNeighbors(this);
    }

    /**
     * Returns the simulator in which this host was created.
     */
    public Simulator getSimulator() {
        return simulator;
    }

    /**
     * Paints this host to a graphics object.  <code>graphics</code>
     * is assumed to have the same dimensions as the domain.  This
     * implementation will paint a filled circle of color {@link
     * Host#getColor()} at this host's current coordinates.  This
     * function is used by {@link
     * org.mates.ui.graphical.TopologyVisualizer} as a callback for
     * whenever a host needs to be painted.
     *
     * This function will only work if the host's position is an
     * instance of {@link Position2D}.  If this is not the case,
     * nothing will be painted.  If another type of {@link Position}
     * is to be used, one must extend this class and re-implement this
     * function.
     */
    public void paint(Graphics graphics) {
        if (!(position instanceof Position2D)) return;
        Position2D p2d = (Position2D) position;
        double x = p2d.getX();
        double y = p2d.getY();
        graphics.setColor(color);
        graphics.fillOval((int) x - 3, (int) y - 3, 6, 6);
    }

    /**
     * Paints the radio range for this host to a graphics
     * object. <code>graphics</code> is assumed to have the same
     * dimensions as the domain.  This implementation will draw a
     * circle according to {@link Host#getRadioRange()}.  The color of
     * the circle will be set according to {@link
     * Host#SELECTED_COLOR}, {@link Host#MOUSEOVER_COLOR}, and {@link
     * Host#RADIO_RANGE_COLOR}.  This function is used by {@link
     * org.mates.ui.graphical.TopologyVisualizer} as a callback for
     * whenever a radio range needs to be painted.
     *
     * This function will only work if the host's position is an
     * instance of {@link Position2D}.  If this is not the case,
     * nothing will be painted.  If another type of {@link Position}
     * is to be used, one must extend this class and re-implement this
     * function.
     *
     * @param mouseover is <code>true</code> when the mouse is held over this host in the GUI.
     * @param selected is <code>true</code> when this host is selected in the GUI.
     */
    public void paintRadioRange(Graphics graphics, boolean mouseover, boolean selected) {
        if (!(position instanceof Position2D)) return;
        Position2D p2d = (Position2D) position;
        double x = p2d.getX();
        double y = p2d.getY();
        if (selected) graphics.setColor(SELECTED_COLOR); else if (mouseover) graphics.setColor(MOUSEOVER_COLOR); else graphics.setColor(RADIO_RANGE_COLOR);
        int radio_range = (int) getRadioRange();
        int twice_radio_range = 2 * radio_range;
        graphics.drawOval((int) x - radio_range, (int) y - radio_range, twice_radio_range, twice_radio_range);
    }

    /**
     * Returns the name of the current host and/or its unique ID.
     */
    public String toString() {
        if (name == uid.toString()) return name; else return name + " (" + uid.toString() + ")";
    }

    /**
     * Returns <code>true</code> if a the graphical representation of
     * this host (as defined by {@link Host#paint(java.awt.Graphics)})
     * contains the pixel at coordinate (<code>x</code>,
     * <code>y</code>).  This function is used as a callback for
     * determining a mouse-over when the graphical user interface is
     * used.
     *
     * This function will only work if the host's position is an
     * instance of {@link Position2D}.  If this is not the case,
     * <code>false</code> will always be returned.  If another type of
     * {@link Position} is to be used, one must extend this class and
     * re-implement this function.
     */
    public boolean isContaining(int pixel_x, int pixel_y) {
        if (!(position instanceof Position2D)) return false;
        Position2D p2d = (Position2D) position;
        double x = p2d.getX();
        double y = p2d.getY();
        return (pixel_x >= x - 3 && pixel_x <= x + 3 && pixel_y >= y - 3 && pixel_y <= y + 3);
    }

    public int hashCode() {
        return uid.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof Host) {
            return uid.equals(((Host) o).getUniqueID());
        } else {
            return false;
        }
    }
}
