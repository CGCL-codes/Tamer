package net.taylor.tracker.dashboard;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;

@Name("ticketPriorityPanel")
public class TicketPriorityPanel extends BaseTicketPanel {

    public TicketPriorityPanel() {
        super("priority");
    }

    public static TicketPriorityPanel instance() {
        return (TicketPriorityPanel) Component.getInstance(TicketPriorityPanel.class);
    }
}
