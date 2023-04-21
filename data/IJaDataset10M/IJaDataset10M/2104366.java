package de.ios.kontor.sv.postgres.impl;

import java.rmi.*;
import java.math.*;
import de.ios.framework.basic.*;
import de.ios.framework.db2.*;
import de.ios.kontor.sv.basic.impl.*;
import de.ios.kontor.sv.order.co.*;

/**
 * InvoiceServiceLineEntryControllerImpl deals with a set of InvoiceServiceLineEntrys
 * within the kontor framework (Postgres-Patch).
 */
public class InvoiceServiceLineEntryControllerImpl extends de.ios.kontor.sv.order.impl.InvoiceServiceLineEntryControllerImpl implements de.ios.kontor.sv.order.co.InvoiceServiceLineEntryController {

    /**
   * Constructor
   * @param _db Connection to the database
   * @param _f a factory to create InvoiceServiceLineEntry-objects
   * @exception java.rmi.RemoteException if the connection to the Server failed.
   * @exception FactoryException if determining the Class of the DBOs created by the Factory failed.
   */
    public InvoiceServiceLineEntryControllerImpl(DBObjectServer _db, de.ios.kontor.sv.order.impl.InvoiceServiceLineEntryFactoryImpl _f) throws java.rmi.RemoteException, FactoryException {
        super(_db, _f);
    }

    /**
   * @param printed if true: only printed Invoices, if false: only unprinted Invoices, if null: all Invoices.
   * @param canceled if true: only canceled Invoices, if false: only uncanceled Invoices, if null: all Invoices.
   * @param custNo if null: all Invoices, if not null: all Invoices of the specified Customer.
   * @param invNo if null: all Invoices, if not null: only the specified Invoice.
   * @param custOId if null: all Invoices, if not null: all Invoices of the specified Customer.
   * @param invOId if null: all Invoices, if not null: only the specified Invoice.
   * @param rowSize specifies the Length of the Row (the Amount of Data to be loaded).
   * @return the String for matching InvoiceLineEntries of 'this Kind' into InvoiceLineEntryDCs.
   * @exception de.ios.kontor.utils.KontorException if the loading of Invoices failed.
   */
    public String getInvoiceLineEntryDCQuery(Boolean printed, Boolean canceled, String custNo, Long invNo, Long custOId, Long invOId, int rowSize) {
        boolean gbp = (rowSize < 1);
        boolean gcu = (rowSize < 2);
        boolean giv = (rowSize < 3);
        boolean ccu = gcu || (custNo != null) || (custOId != null);
        boolean civ = giv || (invNo != null) || (custOId != null);
        String wc = null;
        if (ccu) wc = ((wc != null) ? (wc + " AND ") : "") + " (c.objectId = i.customerOId)";
        if (civ) wc = ((wc != null) ? (wc + " AND ") : "") + " (i.objectId = p.invoiceOId)";
        if (printed != null) wc = ((wc != null) ? (wc + " AND ") : "") + " (i.f_date is " + (printed.booleanValue() ? "not" : "") + " null)";
        if (canceled != null) wc = ((wc != null) ? (wc + " AND ") : "") + " (i.cancelation is " + (canceled.booleanValue() ? "not" : "") + " null)";
        if (custNo != null) wc = ((wc != null) ? (wc + " AND ") : "") + " (c.f_number like '" + custNo + "')";
        if (invNo != null) wc = ((wc != null) ? (wc + " AND ") : "") + " (i.f_number = " + invNo.longValue() + ")";
        if (custOId != null) wc = ((wc != null) ? (wc + " AND ") : "") + " (i.customerOId = " + custOId.longValue() + ")";
        if (invOId != null) wc = ((wc != null) ? (wc + " AND ") : "") + " (p.invoiceOId  = " + invOId.longValue() + ")";
        return "select" + (gbp ? (" co.shortname as custName," + " bp.objectid  as t1bpoid, ") : "") + (gcu ? (" c.f_number" + " as custNo" + ",") : "") + (gbp ? (" c.businesspoid" + " as bpoid" + ",") : "") + (giv ? (" i.f_number" + " as invNo" + "," + " i.shortDescription" + " as invDesc" + "," + " i.vatlessSum" + " as invVlSum" + "," + " i.finalSum" + " as invSum" + "," + " i.f_date" + " as invDate" + "," + " i.payable" + " as invPayD" + "," + " i.cancelation" + " as invCDate" + "," + " i.autoEntry" + " as invAEnt" + ",") : "") + " p.objectid" + " as objectid" + "," + " p.orderLineEntryOId   as oleOId" + "," + " p.lineNumber" + " as posNr" + "," + " p.cancelation" + " as posCDate" + "," + " p.numberOfPieces" + " as posNum" + "," + " p.shortName" + " as posName" + "," + " p.descriptionLines" + " as posDesc" + "," + " p.amountPerPiece" + " as posAmtPP" + "," + " p.VAT" + " as posVAT" + "," + " varchar '" + getKind() + "'" + " as posKind" + "," + " p.referenceNumber" + " as posRefNo" + "," + " p.f_comment" + " as posComt" + "," + " p.externalReference   as posXRef" + " " + "from" + " company co, businessp bp, " + (ccu ? " Customer c," : "") + (civ ? " Invoice i," : "") + " InvServLineEntry p " + "where " + (gbp ? ("(bp.company=co.objectid) AND (bp.objectid=c.businesspoid) ") : "") + ((wc != null) ? ((gbp ? ("AND ") : "") + wc) : "") + "UNION select" + (gbp ? (" (pr.name || ' ,')|| pr.firstname as custName," + " bp.objectid as t1bpoid, ") : "") + (gcu ? (" c.f_number" + " as custNo" + ",") : "") + (gbp ? (" c.businesspoid" + " as bpoid" + ",") : "") + (giv ? (" i.f_number" + " as invNo" + "," + " i.shortDescription" + " as invDesc" + "," + " i.vatlessSum" + " as invVlSum" + "," + " i.finalSum" + " as invSum" + "," + " i.f_date" + " as invDate" + "," + " i.payable" + " as invPayD" + "," + " i.cancelation" + " as invCDate" + "," + " i.autoEntry" + " as invAEnt" + ",") : "") + " p.objectid" + " as objectid" + "," + " p.orderLineEntryOId   as oleOId" + "," + " p.lineNumber" + " as posNr" + "," + " p.cancelation" + " as posCDate" + "," + " p.numberOfPieces" + " as posNum" + "," + " p.shortName" + " as posName" + "," + " p.descriptionLines" + " as posDesc" + "," + " p.amountPerPiece" + " as posAmtPP" + "," + " p.VAT" + " as posVAT" + "," + " varchar '" + getKind() + "'" + " as posKind" + "," + " p.referenceNumber" + " as posRefNo" + "," + " p.f_comment" + " as posComt" + "," + " p.externalReference   as posXRef" + " " + "from" + (gbp ? (" person pr, businessp bp, ") : "") + (ccu ? " Customer c," : "") + (civ ? " Invoice i," : "") + " InvServLineEntry p " + "where  " + (gbp ? ("(bp.person=pr.objectid) AND (bp.objectid=c.businesspoid) ") : "") + ((wc != null) ? ((gbp ? ("AND ") : "") + wc) : "");
    }
}

;
