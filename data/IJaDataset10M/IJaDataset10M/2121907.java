package net.sourceforge.jcoupling.peer;

import java.util.List;
import net.sourceforge.jcoupling.bus.server.CommunicatorID;
import net.sourceforge.jcoupling.peer.property.ChooseClause;
import net.sourceforge.jcoupling.wca.WCAChannel;

/**
 * @author Lachlan Aldred
 */
public class ReceiveRequest extends KeyedRequest {

    private ChooseClause _chooser;

    private CommunicatorID _communicatorID;

    /**
	 * Only for receive requests.
	 * 
	 * @param destinations
	 *          the list of channel over which to receive
	 * @param messageQuery
	 *          PRE: SQL, must select only a result set of msgids, can be multicolumn though.
	 * @param chooser
	 *          either first(x), last(x) or all messages.
	 * @param communicatorID
	 */
    public ReceiveRequest(List<WCAChannel> destinations, String messageQuery) {
        super(destinations, messageQuery);
        _chooser = new ChooseClause();
    }

    public ReceiveRequest(WCAChannel destination, String messageQuery) {
        super(destination, messageQuery);
        _chooser = new ChooseClause();
    }

    public ChooseClause getChooser() {
        return _chooser;
    }

    public CommunicatorID getCommunicatorID() {
        return _communicatorID;
    }
}
