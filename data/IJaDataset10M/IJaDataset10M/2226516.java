package de.ios.kontor.sv.stock.impl;

import java.rmi.*;
import de.ios.framework.db2.*;
import de.ios.framework.basic.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.stock.co.*;
import de.ios.kontor.sv.basic.impl.*;

/**
 * Implements the StockTransaction-Interface, encapsulating a StockTransactionDBO-Object.
 *
 * @author js (Joachim Schaaf).
 * @version $Id: StockTransactionImpl.java,v 1.1.1.1 2004/03/24 23:03:45 nanneb Exp $.
 */
public class StockTransactionImpl extends BasicImpl implements StockTransaction {

    /** The internal DBObject */
    protected StockTransactionDBO dbo = null;

    /**
   * constructor
   *
   * @param _db The DBObject-Server.
   * @param _sc The Session-Carrier.
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public StockTransactionImpl(DBObjectServer _db, SessionCarrier _sc) throws RemoteException {
        this(_db, _sc, new StockTransactionDBO());
    }

    /**
   * constructor
   *
   * @param _db The DBObject-Server.
   * @param _sc The Session-Carrier.
   * @param _dbo The internal DBObject.
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public StockTransactionImpl(DBObjectServer _db, SessionCarrier _sc, StockTransactionDBO _dbo) throws RemoteException {
        super(_db, _sc, _dbo);
        dbo = _dbo;
    }

    /**
   * Return the StockTransactionContollerImpl
   *
   * @return the StockTransactionControllerImpl.
   *
   * @exception de.ios.framework.basic.KontorException if the Operation failed due to a Server error..
   */
    public BasicControllerImpl getBasicControllerImpl() throws KontorException {
        return getSessionImpl().getStockTransactionControllerImpl();
    }

    /**
   * Modify a StockTransaction.
   *
   * @param _stockOId The ObjectId of the stock.
   * @param _articleOId The ObjectId of the stocked Article.
   * @param _location The location of the stocked Article.
   * @param _kindOId The ObjectId of the stock transaction kind.
   * @param _entryOId The ObjectId of the Stock entry
   * @param _transaction The stock transaction (input/output).
   * @param _cancel Cancellation of the transaction (amount).
   * @param _changeDate The changedate.
   * @param _changeTime The changetime.
   * @param _changedBy The username.
   * @param _purpose A short note about the purpose of this transaction
   * @param _description A (longer) description
   * @param _delNoteLiEntOId Objectid of DeliveryNoteLineEntry this StockTransaction belongs to.
   *
   * The stock transaction number (an identification) is set in store().
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public void modify(Long _stockOId, Long _articleOId, String _location, Long _kindOId, Long _entryOId, Long _transaction, Long _cancel, IoSDate _changeDate, IoSTime _changeTime, String _changedBy, String _purpose, String _description, Long _delNoteLiEntOId) throws java.rmi.RemoteException, KontorException {
        try {
            ((StockTransactionDBO) dbo).setAll(_stockOId, _articleOId, _location, _kindOId, _entryOId, _transaction, _cancel, _changeDate, _changeTime, _changedBy, _purpose, _description, getTransactionId(), _delNoteLiEntOId, true);
        } catch (Throwable t) {
            throw new KontorException("Modification of a StockTransaction failed!", t);
        }
    }

    /**
   * Store the Stock transaction and update the corresponding Stock entry.
   * Note: if the stock transaction should be modified, the stock entry is not changed! (except cancel)
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   * @exception de.ios.framework.basic.KontorException if the Operation failed due to a Server-Error.
   * @exception de.ios.framework.basic.KontorUpdateException if the Data to be changed wasn't up-to-date anymore.
   */
    public void store(int tid) throws KontorException, KontorUpdateConflict {
        StockEntryImpl se;
        try {
            if (!this.wasStored()) {
                if (dbo.transactionId.getLong() == null) dbo.transactionId.set(getSessionImpl().getStockTransactionControllerImpl().getNextTransactionId());
                super.store(tid);
                if (getEntryOId() != null) {
                    se = (StockEntryImpl) getSessionImpl().getStockEntryControllerImpl().getStockEntryByOId(getEntryOId().longValue());
                    if ((getTransaction().longValue() < 0) & (se.getAmount().longValue() < (getTransaction().longValue() * (-1)))) {
                        if (!(getSessionImpl().getStockTransactionControllerImpl().shortageAllowed())) {
                            throw new KontorException("Stock Transaction value too big,\n" + "Database Transaction aborted!");
                        }
                    }
                    se.setAmount(new Long(se.getAmount().longValue() + getTransaction().longValue()));
                    se.store(tid);
                } else throw new KontorException("Store of Stock transaction failed,\n" + "no Stock Entry!");
            } else {
                super.store(tid);
                if (getCancel() != null) {
                    se = (StockEntryImpl) getSessionImpl().getStockEntryControllerImpl().getStockEntryByOId(getEntryOId().longValue());
                    if ((getTransaction().longValue() < 0) & (se.getAmount().longValue() < (getTransaction().longValue() * (-1)))) {
                        if (!(getSessionImpl().getStockTransactionControllerImpl().shortageAllowed())) {
                            throw new KontorException("Stock Transaction value too big,\n" + "Database Transaction aborted!");
                        }
                    }
                    se.setAmount(new Long(se.getAmount().longValue() + getTransaction().longValue()));
                    se.store(tid);
                }
            }
        } catch (KontorUpdateConflict uc) {
            try {
                throw new KontorUpdateConflict("Update conflict while storing a Stock transaction! ", new StockTransactionImpl(db, sc, (StockTransactionDBO) uc.getNewValue()));
            } catch (Throwable t1) {
                throw new KontorException("Creation of a new Stock transaction failed while handling an Update conflict!", t1);
            }
        } catch (Throwable t2) {
            throw new KontorException("Store of Stock transaction failed,\n" + "Transaction aborted!", t2);
        }
    }

    /**
   * set the value of stockOId
   *
   * @param value The ObjectId of the stock.
   */
    public void setStockOId(Long stockOId) {
        dbo.stockOId.set(stockOId);
    }

    /**
   * get the value of stockOId
   *
   * @return The ObjectId of the stock
   */
    public Long getStockOId() {
        return dbo.stockOId.getLong();
    }

    /**
   * set the value of articleOId
   *
   * @param value The ObjectId of the stocked Article (may be null).
   */
    public void setArticleOId(Long articleOId) {
        dbo.articleOId.set(articleOId);
    }

    /**
   * get the value of articleOId
   *
   * @return The ObjectId of the stocked Article (may be null)
   */
    public Long getArticleOId() {
        return dbo.articleOId.getLong();
    }

    /**
   * set the value of location
   *
   * @param value The location of the stocked Article.
   */
    public void setLocation(String location) {
        dbo.location.set(location);
    }

    /**
   * get the value of location
   *
   * @return The location of the stocked Article
   */
    public String getLocation() {
        return dbo.location.get();
    }

    /**
   * set the value of kindOId
   *
   * @param value The ObjectId of the stock transaction kind.
   */
    public void setKindOId(Long kindOId) {
        dbo.kindOId.set(kindOId);
    }

    /**
   * get the value of kindOId
   *
   * @return The ObjectId of the stock transaction kind.
   */
    public Long getKindOId() {
        return dbo.kindOId.getLong();
    }

    /**
   * set the value of entryOId
   *
   * @param value The ObjectId of the Stock entry
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public void setEntryOId(Long entryOId) {
        dbo.entryOId.set(entryOId);
    }

    /**
   * get the value of entryOId
   *
   * @return The ObjectId of the Stock entry
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public Long getEntryOId() {
        return dbo.entryOId.getLong();
    }

    /**
   * set the value of transaction
   *
   * @param value The stock transaction (input/output).
   */
    public void setTransaction(Long transaction) {
        dbo.transaction.set(transaction);
    }

    /**
   * get the value of transaction
   *
   * @return The stock transaction (input/output)
   */
    public Long getTransaction() {
        return dbo.transaction.getLong();
    }

    /**
   * set the value of cancel
   *
   * @param value Cancellation of the transaction (amount).
   */
    public void setCancel(Long cancel) {
        dbo.cancel.set(cancel);
    }

    /**
   * get the value of cancel
   *
   * @return Cancellation of the transaction (amount)
   */
    public Long getCancel() {
        return dbo.cancel.getLong();
    }

    /**
   * set the value of changeDate
   *
   * @param value The changedate.
   */
    public void setChangeDate(IoSDate changeDate) {
        dbo.changeDate.set(changeDate);
    }

    /**
   * get the value of changeDate
   *
   * @return The changedate
   */
    public IoSDate getChangeDate() {
        return dbo.changeDate.get();
    }

    /**
   * set the value of changeTime
   *
   * @param value The changetime.
   */
    public void setChangeTime(IoSTime changeTime) {
        dbo.changeTime.set(changeTime);
    }

    /**
   * get the value of changeTime
   *
   * @return The changetime
   */
    public IoSTime getChangeTime() {
        return dbo.changeTime.get();
    }

    /**
   * set the value of changedBy
   *
   * @param value The username.
   */
    public void setChangedBy(String changedBy) {
        dbo.changedBy.set(changedBy);
    }

    /**
   * get the value of changedBy
   *
   * @return The username
   */
    public String getChangedBy() {
        return dbo.changedBy.get();
    }

    /**
   * set the value of purpose
   *
   * @param value A short note about the purpose of this transaction
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public void setPurpose(String purpose) {
        dbo.purpose.set(purpose);
    }

    /**
   * get the value of purpose
   *
   * @return A short note about the purpose of this transaction
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public String getPurpose() {
        return dbo.purpose.get();
    }

    /**
   * set the value of description
   *
   * @param value A (longer) description
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public void setDescription(String description) {
        dbo.description.set(description);
    }

    /**
   * get the value of description
   *
   * @return A (longer) description
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public String getDescription() {
        return dbo.description.get();
    }

    /**
   * set the value of transactionId
   *
   * @param value The stock transaction number (an identification).
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public void setTransactionId(Long transactionId) {
        dbo.transactionId.set(transactionId);
    }

    /**
   * get the value of transactionId
   *
   * @return The stock transaction number (an identification).
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public Long getTransactionId() {
        return dbo.transactionId.getLong();
    }

    /**
   * Set the fields to cancel this transaction.
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public void cancel() {
        setCancel(getTransaction());
        setTransaction(new Long(getTransaction().longValue() * (-1)));
    }

    /**
   * set the value of delNoteLiEntOId
   *
   * @param Objectid of DeliveryNoteLineEntry this StockTransaction belongs to.
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public void setDelNoteLiEntOId(Long _delNoteLiEntOId) {
        dbo.delNoteLiEntOId.set(_delNoteLiEntOId);
    }

    /**
   * get the value of delNoteLiEntOId
   *
   * @return Objectid of DeliveryNoteLineEntry this StockTransaction belongs to.
   *
   * @exception java.rmi.RemoteException if the connection to the server failed.
   */
    public Long getDelNoteLiEntOId() {
        return dbo.delNoteLiEntOId.getLong();
    }
}
