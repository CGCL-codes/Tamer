package gnu.hylafax.status;

/**
 * Represents a SEND event sent by the fax server.
 * 
 * @version $Revision: 84 $
 * @author Steven Jardine <steve@mjnservices.com>
 */
public class SendStatusEvent extends BaseStatusEvent {

    public SendStatusEvent(String event) {
        super(event);
    }
}
