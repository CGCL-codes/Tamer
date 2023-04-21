package org.jabber.jabberbeans.Extension;

import java.util.Vector;
import java.util.Enumeration;
import org.jabber.jabberbeans.*;

/**
 * <code>IQAgents</code> contains the jabber:iq:agents extension, which is a
 * list of (sub) agents known by the server or directed party.
 *
 * @author  David Waite <a href="mailto:dwaite@jabber.com">
 *                      <i>&lt;dwaite@jabber.com&gt;</i></a>
 * @author  $Author: mass $
 * @version $Revision: 1.9 $
 */
public class IQAgents extends XMLData implements QueryExtension {

    /** Vector of 'Agent' objects contained */
    private Vector Agents;

    /**
     * Creates a new <code>IQAgents</code> instance, based on the builder
     * state.
     *
     * @param builder an <code>IQAgentsBuilder</code> value
     * @exception InstantiationException if malformed or insufficient data is
     * in the builder.
     */
    public IQAgents(IQAgentsBuilder builder) throws InstantiationException {
        Agents = (Vector) builder.getAgents().clone();
    }

    /**
     * returns an enumeration of <code>agents</code> contained within this
     * object.
     *
     * @return an <code>Enumeration</code> value
     */
    public Enumeration agents() {
        return Agents.elements();
    }

    /**
     * <code>appendItem</code> converts this packet to XML, either an
     * &lt;agent&gt; block in a jabber:iq:agents extension, or its own
     * jabber:iq:agent extension. It then appends it to a StringBuffer.
     *
     * @param retval The <code>StringBuffer</code> to append to
     */
    public void appendItem(StringBuffer retval) {
        retval.append("<query xmlns=\"jabber:iq:agents\"");
        Enumeration items = agents();
        if (items == null) {
            retval.append("/>");
            return;
        } else retval.append('>');
        while (items.hasMoreElements()) ((Agent) items.nextElement()).appendItem(retval);
        retval.append("</query>");
    }
}
