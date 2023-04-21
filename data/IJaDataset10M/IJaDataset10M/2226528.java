package org.jabber.jabberbeans;

import org.jabber.jabberbeans.Extension.*;
import java.util.Vector;
import java.io.Serializable;

/**
 * An <code>InfoQuery</code> object represents a single IQ packet, which is 
 * used to set and query information between the client and server.<p>
 * <i>e.g.:</i><br>
 * &lt;iq type="get|set|result|error"&gt;<br>
 * &nbsp;&nbsp;&lt;query xmlns="........"&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;<i>information custom to the namespace..</i><br>
 * &nbsp;&nbsp;&lt;/query&gt;<br>
 * &lt;iq&gt;<p>
 * 
 * @see ContentPacket
 *
 * @author  David Waite <a href="mailto:dwaite@jabber.com">
 *                      <i>&lt;dwaite@jabber.com&gt;</i></a>
 * @author  $Author: mass $
 * @version $Revision: 1.21 $
 */
public class InfoQuery extends ContentPacket implements Serializable {

    /**
     * Creates a new <code>InfoQuery</code> instance. Note that because of
     * the complexity of the InfoQuery object and because the object is
     * immutable after creation, a builder object is needed in order to
     * construct this object.
     *
     * @param builder an <code>InfoQueryBuilder</code> holding appropriate
     * values
     * @exception InstantiationException if the values supplied by the
     * InfoQueryBuilder are not sufficient to creaate a 'correctly-formed'
     * InfoQuery packet.
     */
    public InfoQuery(InfoQueryBuilder builder) throws InstantiationException {
        super((PacketBuilder) builder);
        if (type == null) throw new InstantiationException("must specify type");
        if (!(type.equals("get") || type.equals("set") || type.equals("result") || type.equals("error"))) throw new InstantiationException("invalid type");
        if (extensions.size() > 1) throw new InstantiationException("info/query allows up to " + "one extension");
        if (extensions.size() == 0) return;
        if (!(extensions.firstElement() instanceof QueryExtension)) throw new InstantiationException("Non-Query extension passed.");
    }

    /**
     * <code>appendItem</code> appends the XML representation of the
     * current packet data to the specified <code>StringBuffer</code>.
     *
     * @param retval The <code>StringBuffer</code> to append to
     */
    public final void appendItem(StringBuffer retval) {
        retval.append("<iq");
        appendBaseAttribs(retval);
        retval.append(">");
        appendAnyError(retval);
        if (extensions.size() > 0) retval.append(extensions.firstElement());
        retval.append("</iq>");
    }
}
