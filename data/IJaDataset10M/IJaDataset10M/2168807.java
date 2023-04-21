package jade.domain.introspection;

import jade.core.Channel;
import jade.content.Concept;

/**
   An introspection event, recording the routing of an ACL message
   within the platform.

   @author Giovanni Rimassa -  Universita' di Parma
   @version $Date: 2003-11-19 17:04:37 +0100 (mer, 19 nov 2003) $ $Revision: 4567 $
*/
public class RoutedMessage implements Concept {

    private Channel from;

    private Channel to;

    private ACLMessage message;

    public void setFrom(Channel c) {
        from = c;
    }

    public Channel getFrom() {
        return from;
    }

    public void setTo(Channel c) {
        to = c;
    }

    public Channel getTo() {
        return to;
    }

    public void setMessage(ACLMessage msg) {
        message = msg;
    }

    public ACLMessage getMessage() {
        return message;
    }
}
