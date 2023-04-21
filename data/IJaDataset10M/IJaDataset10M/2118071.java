package com.i3sp.address;

import org.mortbay.util.Code;

public interface DifferenceHandler {

    void handle(AddressEntryComparator diff);

    void jndiOnly(AddressEntry entry);

    void newEntry(AddressEntry entry);

    void same(AddressEntry entry);
}
