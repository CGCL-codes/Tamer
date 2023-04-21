package org.vizzini.ui.game.boardgame;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vizzini.game.DefaultHumanAgent;
import org.vizzini.game.IAdjudicator;
import org.vizzini.game.IAgent;
import org.vizzini.game.IEnvironment;
import org.vizzini.game.IHumanAgent;
import org.vizzini.game.ITeam;
import org.vizzini.game.action.IAction;
import org.vizzini.game.event.IConcedeListener;
import org.vizzini.ui.ApplicationSupport;

/**
 * Provides a user interface for a human agent which uses the text input to
 * place a token.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.1
 */
public abstract class TextAgent implements IHumanAgent {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(TextAgent.class.getName());

    /** Agent delegate. */
    private IHumanAgent _agentDelegate;

    /**
     * Construct this object.
     *
     * @since  v0.4
     */
    public TextAgent() {
        _agentDelegate = new DefaultHumanAgent(this);
    }

    /**
     * Construct this object with the given parameter.
     *
     * @param  owner  Owner.
     *
     * @since  v0.4
     */
    public TextAgent(IHumanAgent owner) {
        _agentDelegate = new DefaultHumanAgent(owner);
    }

    /**
     * @see  org.vizzini.game.IAgent#addConcedeListener(org.vizzini.game.event.IConcedeListener)
     */
    public void addConcedeListener(IConcedeListener listener) {
        _agentDelegate.addConcedeListener(listener);
    }

    /**
     * @see  org.vizzini.game.IAgent#addPropertyChangeListener(java.lang.String,
     *       java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        _agentDelegate.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * @see  org.vizzini.game.IHumanAgent#beep()
     */
    public void beep() {
        _agentDelegate.beep();
    }

    /**
     * @see  org.vizzini.game.IAgent#clone()
     */
    @Override
    public Object clone() {
        return _agentDelegate.clone();
    }

    /**
     * @see  org.vizzini.game.IAgent#concede()
     */
    public void concede() {
        _agentDelegate.concede();
    }

    /**
     * @see  org.vizzini.game.IAgent#getAction(org.vizzini.game.IEnvironment, org.vizzini.game.IAdjudicator)
     */
    public IAction getAction(IEnvironment environment, IAdjudicator adjudicator) {
        return _agentDelegate.getAction(environment, adjudicator);
    }

    /**
     * @see  org.vizzini.game.IAgent#getName()
     */
    public String getName() {
        return _agentDelegate.getName();
    }

    /**
     * @see  org.vizzini.game.IAgent#getOwner()
     */
    public IAgent getOwner() {
        return _agentDelegate.getOwner();
    }

    /**
     * @see  org.vizzini.game.IAgent#getScore()
     */
    public long getScore() {
        return _agentDelegate.getScore();
    }

    /**
     * @see  org.vizzini.game.IAgent#getTeam()
     */
    public ITeam getTeam() {
        return _agentDelegate.getTeam();
    }

    /**
     * @see  org.vizzini.game.IAgent#removeConcedeListener(org.vizzini.game.event.IConcedeListener)
     */
    public void removeConcedeListener(IConcedeListener listener) {
        _agentDelegate.removeConcedeListener(listener);
    }

    /**
     * @see  org.vizzini.game.IAgent#removePropertyChangeListener(java.lang.String,
     *       java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        _agentDelegate.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * @see  org.vizzini.game.IAgent#reset()
     */
    public void reset() {
        _agentDelegate.reset();
    }

    /**
     * @see  org.vizzini.game.IAgent#setName(java.lang.String)
     */
    public void setName(String name) {
        _agentDelegate.setName(name);
    }

    /**
     * @see  org.vizzini.game.IAgent#setScore(long)
     */
    public void setScore(long score) {
        _agentDelegate.setScore(score);
    }

    /**
     * @see  org.vizzini.game.IAgent#setTeam(org.vizzini.game.ITeam)
     */
    public void setTeam(ITeam team) {
        _agentDelegate.setTeam(team);
    }

    /**
     * @return  the prompt string.
     *
     * @since   v0.1
     */
    protected String getPrompt() {
        return getName() + " " + ApplicationSupport.getResource("STRING_promptString");
    }

    /**
     * Return the position obtained by parsing the given string.
     *
     * @param   line  The string to parse for position information.
     *
     * @return  Array which contains input coordinates.
     *
     * @since   v0.1
     */
    protected List<String> parseInput(String line) {
        List<String> answer = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(line, ",");
        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();
            if ((token != null) && (token.length() > 0) && token.toLowerCase().startsWith("q")) {
                System.out.println("Quitting as requested.");
                System.exit(0);
            }
            answer.add(token);
        }
        return answer;
    }

    /**
     * Read string input from standard in until a carriage return is read.
     *
     * @return  the string.
     *
     * @since   v0.1
     */
    protected String readInput() {
        StringBuilder sb = new StringBuilder();
        try {
            char ch = 0;
            while ((ch = (char) System.in.read()) != '\n') {
                sb.append(ch);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return sb.toString();
    }
}
