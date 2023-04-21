package de.ios.kontor.sv.order.co;

import de.ios.kontor.sv.basic.co.*;

/**
 * InvoiceServiceLineEntryFactory can create InvoiceServiceLineEntry-Objects. If you want to use
 * your own InvoiceServiceLineEntry-Type you have to implement your own Factory and
 * hand it over to the InvoiceServiceLineEntryController when creating it.
 */
public interface InvoiceServiceLineEntryFactory extends BasicFactory {
}

;
