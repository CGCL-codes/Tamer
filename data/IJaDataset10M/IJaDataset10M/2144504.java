package de.iritgo.openmetix.exporter.minmidmax;

import de.iritgo.openmetix.core.gui.GUIPane;
import de.iritgo.openmetix.core.gui.IDesktopManager;
import de.iritgo.openmetix.core.gui.IDisplay;
import de.iritgo.openmetix.core.network.ClientTransceiver;
import de.iritgo.openmetix.framework.base.action.FrameworkAction;
import de.iritgo.openmetix.framework.base.action.FrameworkInputStream;
import de.iritgo.openmetix.framework.base.action.FrameworkOutputStream;
import de.iritgo.openmetix.framework.client.Client;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This action is executed on the server. It receives the historical data query.
 *
 * @version $Id: MinMidMaxQueryResponse.java,v 1.1 2005/04/24 18:10:45 grappendorf Exp $
 */
public class MinMidMaxQueryResponse extends FrameworkAction {

    /** Result list. */
    private List outputs;

    /** Instrument id. */
    private long instrumentUniqueId;

    /** Query start date. */
    private long startDate;

    /** Query end date. */
    private long endDate;

    /**
	 * Create a new MinMidMaxQueryResponse
	 */
    public MinMidMaxQueryResponse() {
        setTypeId("MMMQRS");
    }

    /**
	 * Create a new MinMidMaxQueryResponse
	 *
	 * @param instrumentUniqueId Id of the receiving instrument.
	 * @param outputs All values from the request.
	 * @param startDate Query start date.
	 * @param endDate Query stop date.
	 */
    public MinMidMaxQueryResponse(long instrumentUniqueId, List outputs, long startDate, long endDate) {
        this();
        this.instrumentUniqueId = instrumentUniqueId;
        this.outputs = outputs;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
	 * Read the attributes from the a stream.
	 *
	 * @param stream The stream to read from.
	 */
    public void readObject(FrameworkInputStream stream) throws IOException {
        instrumentUniqueId = stream.readLong();
        startDate = stream.readLong();
        endDate = stream.readLong();
        int size = stream.readInt();
        if (outputs == null) {
            outputs = new LinkedList();
        }
        for (int i = 0; i < size; ++i) {
            outputs.add(new Double(stream.readDouble()));
        }
    }

    /**
	 * Write the attributes to a stream.
	 *
	 * @param stream The stream to write to.
	 */
    public void writeObject(FrameworkOutputStream stream) throws IOException {
        stream.writeLong(instrumentUniqueId);
        stream.writeLong(startDate);
        stream.writeLong(endDate);
        stream.writeInt(outputs.size());
        for (int i = 0; i < outputs.size(); ++i) {
            stream.writeDouble(((Double) outputs.get(i)).doubleValue());
        }
    }

    /**
	 * Perform the action.
	 */
    public void perform() {
        ClientTransceiver clientTransceiver = (ClientTransceiver) transceiver;
        IDesktopManager desktopManager = Client.instance().getClientGUI().getDesktopManager();
        for (Iterator i = desktopManager.getDisplayIterator(); i.hasNext(); ) {
            try {
                GUIPane guiPane = ((IDisplay) i.next()).getGUIPane();
                if (guiPane.getTypeId().equals("MinMidMaxResult")) {
                    MinMidMaxResultGUIPane mMMRG = (MinMidMaxResultGUIPane) guiPane;
                    mMMRG.viewSensors(outputs, startDate, endDate);
                }
            } catch (ClassCastException noInstrumentDisplayIgnored) {
            }
        }
    }
}
