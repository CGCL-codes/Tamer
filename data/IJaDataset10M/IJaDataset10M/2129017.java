package org.ceno.communication.cli.net4j;

import org.ceno.protocol.event.IEvent;
import java.io.IOException;
import org.eclipse.net4j.signal.Request;
import org.eclipse.net4j.signal.SignalProtocol;
import org.eclipse.net4j.util.io.ExtendedDataOutputStream;

/**
 * @author Andre Albert &lt;andre.albert82@googlemail.com&gt
 * @created 06.12.2009
 * @version 0.0.1
 * @since 0.0.1
 */
public class Net4JRequest extends Request {

    private IEvent event;

    /**
	 * @param event
	 * @param protocol
	 */
    public Net4JRequest(SignalProtocol<?> protocol, IEvent event) {
        super(protocol, event.getSignalId());
        this.event = event;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void requesting(ExtendedDataOutputStream out) throws IOException {
        out.writeObject(event);
    }
}
