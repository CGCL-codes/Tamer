package de.haumacher.timecollect;

public final class TicketIdIndex extends IndexedSet<Integer, Ticket> {

    @Override
    protected Integer getKey(Ticket value) {
        return value.getId();
    }
}
