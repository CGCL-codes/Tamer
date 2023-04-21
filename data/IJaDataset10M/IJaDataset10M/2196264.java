package org.vizzini.game.cardgame;

/**
 * Provides a default implementation of a suit for card games in the game
 * framework.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.1
 */
public class DefaultSuit implements ISuit {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Name. */
    private String _name;

    /**
     * Construct this object with the given parameter.
     *
     * @param  name  Name.
     */
    public DefaultSuit(String name) {
        _name = name;
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
     * @since   v0.1
     */
    public int compareTo(ISuit object) {
        DefaultSuit another = (DefaultSuit) object;
        return _name.compareTo(another._name);
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
            DefaultSuit another = (DefaultSuit) object;
            answer = ((_name == another._name) || _name.equals(another._name));
        }
        return answer;
    }

    /**
     * @return  the name.
     *
     * @since   v0.1
     */
    public String getName() {
        return _name;
    }

    /**
     * @see  java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return _name.hashCode();
    }

    /**
     * @see  java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(" [");
        sb.append("_name=").append(_name);
        sb.append("]");
        return sb.toString();
    }
}
