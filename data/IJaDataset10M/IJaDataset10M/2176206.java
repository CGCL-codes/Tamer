package com.openbravo.pos.sales.event;

import java.util.EventObject;
import com.openbravo.bean.sales.TicketLine;

public class TicketLineEvent extends EventObject {

    /**
	 * 
	 */
    TicketLine dest;

    public TicketLineEvent(TicketLine source) {
        super(source);
    }

    public TicketLineEvent(TicketLine source, TicketLine dest) {
        super(source);
        this.dest = dest;
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 3048005861699829687L;

    public TicketLine getSource() {
        return (TicketLine) source;
    }

    public TicketLine getDest() {
        return dest;
    }
}
