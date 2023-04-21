package org.vizzini.game.cardgame;

import java.beans.PropertyChangeListener;
import org.vizzini.game.DefaultToken;
import org.vizzini.game.IAgent;
import org.vizzini.game.IPosition;
import org.vizzini.game.ITeam;
import org.vizzini.game.IToken;
import org.vizzini.game.IntegerPosition;
import org.vizzini.util.event.PropertyChangeManager;

/**
 * Provides a card for card games in the game framework.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.4
 */
public class DefaultCard implements ICard {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Owner. */
    private ICard _owner;

    /** Property change manager. */
    private transient PropertyChangeManager _propertyManager;

    /** Suit. */
    private ISuit _suit;

    /** Token delegate. */
    private IToken _tokenDelegate;

    /**
     * Construct this object with the given parameters.
     *
     * @param    name   Name.
     * @param    value  Value.
     * @param    suit   Suit.
     *
     * @version  v0.4
     */
    public DefaultCard(String name, int value, ISuit suit) {
        _owner = this;
        IPosition position = IntegerPosition.get(0, 0, 0);
        _tokenDelegate = new DefaultToken(this, position, name, value);
        _suit = suit;
    }

    /**
     * Construct this object with the given parameters.
     *
     * @param  owner  Owner.
     * @param  name   Name.
     * @param  value  Value.
     * @param  suit   Suit.
     *
     * @since  v0.4
     */
    public DefaultCard(ICard owner, String name, int value, ISuit suit) {
        _owner = owner;
        IPosition position = IntegerPosition.get(0, 0, 0);
        _tokenDelegate = new DefaultToken(owner, position, name, value);
        _suit = suit;
    }

    /**
     * @see  org.vizzini.game.IToken#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        _tokenDelegate.addPropertyChangeListener(propertyName, listener);
        getPropertyChangeManager().addPropertyChangeListener(propertyName, listener);
    }

    /**
     * @see  java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        try {
            DefaultCard clone = (DefaultCard) super.clone();
            clone._tokenDelegate = (IToken) _tokenDelegate.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    /**
     * Compare this to the given object.
     *
     * @param   object  Object.
     *
     * @return  -1,0,1 if this is less than, equal to, greater than object.
     *
     * @throws  ClassCastException  if another is not an instance of <code>
     *                              AbstractCard</code>.
     *
     * @since   v0.4
     */
    public int compareTo(ICard object) {
        DefaultCard another = (DefaultCard) object;
        int answer = 0;
        if (getIndex() < another.getIndex()) {
            answer = -1;
        } else if (getIndex() > another.getIndex()) {
            answer = 1;
        }
        if ((answer == 0) && (_suit != null)) {
            answer = _suit.compareTo(another._suit);
        }
        return answer;
    }

    /**
     * @see  java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        boolean answer = false;
        if (object == this) {
            answer = true;
        } else if (object == null) {
            answer = false;
        } else if (getClass() != object.getClass()) {
            answer = false;
        } else {
            DefaultCard another = (DefaultCard) object;
            answer = _tokenDelegate.equals(another._tokenDelegate) && ((_suit == another._suit) || ((_suit != null) && _suit.equals(another._suit)));
        }
        return answer;
    }

    /**
     * @see  org.vizzini.game.IToken#getAgent()
     */
    public IAgent getAgent() {
        return _tokenDelegate.getAgent();
    }

    /**
     * @see  org.vizzini.game.cardgame.ICard#getIndex()
     */
    public int getIndex() {
        return getValue();
    }

    /**
     * @see  org.vizzini.game.IToken#getName()
     */
    public String getName() {
        return _tokenDelegate.getName();
    }

    /**
     * @see  org.vizzini.game.IToken#getPosition()
     */
    public IPosition getPosition() {
        return _tokenDelegate.getPosition();
    }

    /**
     * @see  org.vizzini.game.cardgame.ICard#getSuit()
     */
    public ISuit getSuit() {
        return _suit;
    }

    /**
     * @see  org.vizzini.game.IToken#getTeam()
     */
    public ITeam getTeam() {
        return _tokenDelegate.getTeam();
    }

    /**
     * @see  org.vizzini.game.IToken#getValue()
     */
    public int getValue() {
        return _tokenDelegate.getValue();
    }

    /**
     * @see  java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int answer = _tokenDelegate.hashCode();
        if (_suit != null) {
            answer += _suit.hashCode();
        }
        return answer;
    }

    /**
     * @see  org.vizzini.game.IToken#removePropertyChangeListener(java.lang.String,
     *       java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        _tokenDelegate.removePropertyChangeListener(propertyName, listener);
        getPropertyChangeManager().removePropertyChangeListener(propertyName, listener);
    }

    /**
     * @see  org.vizzini.game.IToken#setAgent(org.vizzini.game.IAgent)
     */
    public void setAgent(IAgent agent) {
        _tokenDelegate.setAgent(agent);
    }

    /**
     * @see  org.vizzini.game.IToken#setName(java.lang.String)
     */
    public void setName(String name) {
        _tokenDelegate.setName(name);
    }

    /**
     * @see  org.vizzini.game.IToken#setPosition(org.vizzini.game.IPosition)
     */
    public void setPosition(IPosition position) {
        _tokenDelegate.setPosition(position);
    }

    /**
     * @see  org.vizzini.game.IToken#setTeam(org.vizzini.game.ITeam)
     */
    public void setTeam(ITeam team) {
        _tokenDelegate.setTeam(team);
    }

    /**
     * @see  org.vizzini.game.IToken#setValue(int)
     */
    public void setValue(int value) {
        _tokenDelegate.setValue(value);
    }

    /**
     * @see  java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(" [");
        sb.append(_tokenDelegate.toString());
        if (_suit != null) {
            sb.append(",_suit=").append(_suit);
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * @return  the property change manager.
     *
     * @since   v0.4
     */
    protected PropertyChangeManager getPropertyChangeManager() {
        if (_propertyManager == null) {
            _propertyManager = new PropertyChangeManager(_owner);
        }
        return _propertyManager;
    }
}
