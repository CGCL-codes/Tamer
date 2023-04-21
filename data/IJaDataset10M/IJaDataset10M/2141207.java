package agonism.ce;

import java.util.Enumeration;
import java.util.EventObject;
import java.util.Properties;
import java.util.Vector;

/**
 * Dummy implementation of the {@link Controller} interface. Subclass this class
 * and override the {@link #step} method in turn-based games, or the {@link #run}
 * method in real-time games.
 */
public abstract class AbstractController implements Controller {

    protected ActorID[] m_actors = new ActorID[0];

    protected TeamID m_teamID = null;

    protected boolean m_isRealtime = false;

    private String m_name = null;

    private transient Scene m_scene = null;

    /**
	 * Initializes the <code>Controller</code>.
	 *
	 * @param actorIDs IDs of the actors that the controller will control
	 * @param props optional set of properties that may tune the controller's behavior
	 * @param teamID ID of the team which the controller is controlling
	 */
    public void initialize(ActorID[] actorIDs, Properties props, TeamID teamID) {
        m_actors = actorIDs;
        m_teamID = teamID;
        if (props != null) m_name = props.getProperty("controller.name", getClass().getName()); else m_name = getClass().getName();
    }

    public String getName() {
        return m_name;
    }

    public TeamID getTeamID() {
        return m_teamID;
    }

    public void setScene(Scene scene) {
        m_scene = scene;
    }

    public Scene getScene() {
        return m_scene;
    }

    /**
	 * Dummy implementation which does nothing. Override this method in real-time games
	 * to supply behavior.
	 */
    public void run() {
    }

    /**
	 * Dummy implementation which does nothing. Override this method in turn-based games
	 * to supply behavior.
	 */
    public void step(double t) {
    }

    public boolean isRealtime() {
        return m_isRealtime;
    }

    public void setRealtime(boolean b) {
        m_isRealtime = b;
    }

    public ActorID[] getActorIDs() {
        return m_actors;
    }

    protected int getNumActors() {
        return m_actors.length;
    }

    /**
	 * Convenience method to return the <code>i</code>th actor that the controller is
	 * controlling.
	 *
	 * @param index must be between 0 and {@link #getNumActors}.
	 */
    protected Actor actor(int index) {
        if (m_scene == null) return null; else return m_scene.fromID(m_actors[index]);
    }
}
