package de.ios.kontor.sv.order.co;

import java.rmi.*;
import java.math.*;
import de.ios.framework.basic.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.basic.co.*;

/**
 * Interface specifies Methods for accessing Data of some kind of an LineEntry-Implementation-Instance.
 * @see OrderLineEntry
 * @see InvoiceLineEntry
 */
public interface LineEntry extends Basic {

    /**
   * @return a Multiline-String of Description-Lines for this Line-Entry.
   * @exception RemoteException thrown if connection to the Server failed.
   * @exception de.ios.kontor.utils.KontorException if the Operation failed due to a Server-Error.
   */
    public String getDescriptionLines() throws RemoteException, KontorException;

    /**
   * @return the GLOBAL UNIQUE Reference-Number for this Line-Entry (used for quick, simple assignments).
   * @exception RemoteException thrown if connection to the Server failed.
   */
    public Long getReferenceNumber() throws RemoteException;

    /**
   * Set the GLOBAL UNIQUE Reference-Number for this Line-Entry (used for quick, simple assignments).
   * @param n The Reference number
   * @exception RemoteException thrown if connection to the Server failed.
   */
    public void setReferenceNumber(Long n) throws RemoteException;

    /**
   * @return the Line-Number of this Line-Entry inside it's 'Container' (unique for the Container, except for Rates if once a Rate-Line-Entry is changed).
   * @exception RemoteException thrown if connection to the Server failed.
   */
    public Long getLineNumber() throws RemoteException;

    /**
   * @return a price for the complete Line-Entry (Currency: Application-Default-Currency).
   * @exception RemoteException thrown if connection to the Server failed.
   * @exception de.ios.kontor.utils.KontorException if the Operation failed due to a Server-Error.
   */
    public BigDecimal getAmount() throws RemoteException, KontorException;

    /**
   * @return the VAT for the complete Line-Entry (Currency: Application-Default-Currency).
   * @exception RemoteException thrown if connection to the Server failed.
   * @exception de.ios.kontor.utils.KontorException if the Operation failed due to a Server-Error.
   */
    public BigDecimal getVATAmount() throws RemoteException, KontorException;

    /**
   * @return a price for a single Piece, Time-Unit, Length-Unit, ...
   * @exception RemoteException thrown if connection to the Server failed.
   * @exception de.ios.kontor.utils.KontorException if the Operation failed due to a Server-Error.
   */
    public BigDecimal getAmountPerPiece() throws RemoteException, KontorException;

    /**
   * @return the VAT for a single Piece.
   * @exception RemoteException thrown if connection to the Server failed.
   * @exception de.ios.kontor.utils.KontorException if the Operation failed due to a Server-Error.
   */
    public BigDecimal getVATAmountPerPiece() throws RemoteException, KontorException;

    /**
   * @return the explicite VAT-Percentage (not the Level) for this LineEntry.
   * @exception RemoteException thrown if connection to the Server failed.
   * @exception de.ios.kontor.utils.KontorException if the Operation failed due to a Server-Error.
   */
    public BigDecimal getVAT() throws RemoteException, KontorException;

    /**
   * @return the Number of Pieces, Time, Length.
   * @exception RemoteException thrown if connection to the Server failed.
   */
    public Integer getNumberOfPieces() throws RemoteException;

    /**
   * @return the Sale unit
   * @exception RemoteException thrown if connection to the Server failed.
   */
    public Integer getSaleUnit() throws RemoteException, KontorException;

    /**
   * @return the Unit for a Piece, i.e. "" (if it's just a piece), "Meter", "Hour", "Container",....
   * @exception RemoteException thrown if connection to the Server failed.
   * @exception de.ios.kontor.utils.KontorException if the Operation failed due to a Server-Error.
   */
    public String getUnit() throws RemoteException, KontorException;

    /**
   * @return the Comment stored with this Line-Entry.
   * @exception RemoteException thrown if connection to the Server failed.
   */
    public String getComment() throws RemoteException;

    /**
   * @return the ShortName for this Line-Entry.
   * @exception RemoteException thrown if connection to the Server failed.
   * @exception de.ios.kontor.utils.KontorException if the Operation failed due to a Server-Error.
   */
    public String getShortName() throws RemoteException, KontorException;

    /**
   * @return the external reference stored with this Line-Entry.
   * @exception RemoteException thrown if connection to the Server failed.
   */
    public String getExternalReference() throws RemoteException;

    /**
   * Set the external reference for this Line-Entry.
   * @param extRef the external Reference.
   * @exception RemoteException thrown if connection to the Server failed.
   */
    public void setExternalReference(String extRef) throws RemoteException;

    /**
   * Get the kind of this LineEntries.
   * @exception RemoteException thrown if connection to the Server failed.
   */
    public String getKind() throws RemoteException;

    /**
   * Get the view-kind for this LineEntries (normally the same, like the kind).
   * @exception RemoteException thrown if connection to the Server failed.
   */
    public String getViewKind() throws RemoteException, KontorException;
}
