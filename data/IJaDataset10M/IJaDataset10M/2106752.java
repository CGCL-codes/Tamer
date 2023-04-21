package com.schinzer.fin.dao.base.castor;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import com.schinzer.fin.dao.base.BaseDaoFactory;
import com.schinzer.fin.dao.base.NoTransactionFlavourFoundException;
import com.schinzer.fin.dao.base.PaymentTransactionDAO;
import com.schinzer.fin.dao.base.PaymentTransactionDTO;
import com.schinzer.fin.dao.base.TransactionDAO;
import com.schinzer.fin.dao.base.TransactionFilter;
import com.schinzer.fin.dao.base.TransactionPersistenceException;
import com.schinzer.fin.model.base.Payment;
import com.schinzer.fin.model.base.Transaction;

public class PaymentTransactionDaoCastor implements PaymentTransactionDAO {

    private static Log log = LogFactory.getLog(PaymentTransactionDaoCastor.class);

    private TransactionDAO tDao = BaseDaoFactory.getTransactionDaoInstance();

    public Transaction decorateTransaction(Transaction txn) throws NoTransactionFlavourFoundException, com.schinzer.fin.dao.base.PersistenceException {
        log.debug("decorateTransaction(" + txn.toString() + ")");
        log.trace("Retrieving database instance");
        Database db;
        try {
            db = CastorHelper.getJdoManager().getDatabase();
        } catch (PersistenceException e1) {
            log.error(e1);
            throw new com.schinzer.fin.dao.base.PersistenceException("A persistence framework (Castor) problem occurred while retrieving a Database instance", e1);
        }
        Payment pt = null;
        PaymentTransactionDTO dto = null;
        try {
            db.begin();
            log.trace("Retrieve Account Object from results");
            dto = (PaymentTransactionDTO) db.load(PaymentTransactionDTO.class, txn.getId(), Database.ReadOnly);
            pt = new Payment(txn);
            pt.setCategory(dto.getCategory());
            pt.setStatusCode(dto.getStatus());
            log.debug("The pt.category is " + pt.getCategory().toString());
            db.commit();
        } catch (ObjectNotFoundException e) {
            log.warn(e);
            StringBuffer msg = new StringBuffer();
            msg.append("The Transaction with TxnId=");
            msg.append(txn.getId());
            msg.append(" could not be decorated as Payment");
            throw new NoTransactionFlavourFoundException(msg.toString(), e);
        } catch (PersistenceException e) {
            log.error(e);
            throw new com.schinzer.fin.dao.base.PersistenceException("A problem occurred when selecting the Payment by TxnId: " + txn.getId(), e);
        }
        log.debug("leaving decorateTransaction()");
        return pt;
    }

    public List<Transaction> decorateTransactions(List<Transaction> txns) throws TransactionPersistenceException {
        log.debug("decorateTransactions( List " + txns.toString() + ")");
        log.trace("Incoming List has " + txns.size() + " Transaction elements");
        log.trace("Preparing iteration through List");
        int countAdded = 0;
        List<Transaction> result = new ArrayList<Transaction>();
        log.trace("entering for clause on List<Transaction> object from input parameter");
        for (Transaction t : txns) {
            log.trace("Creating Payment [decoration happens here ;-)]");
            Payment pt = null;
            try {
                pt = (Payment) decorateTransaction(t);
            } catch (NoTransactionFlavourFoundException e) {
                StringBuffer msg = new StringBuffer();
                msg.append("No Payment data was found for Transaction object with id: ");
                msg.append(t.getId());
                log.warn(msg.toString(), e);
            } catch (com.schinzer.fin.dao.base.PersistenceException e) {
                log.error("An error occurred on the persistence layer");
                throw new TransactionPersistenceException(e);
            }
            if (pt != null) {
                if (!result.add(pt)) log.error("Payment with id: " + pt.getId() + " could not be added to result array."); else countAdded++;
            } else {
                if (!result.add(t)) log.error("Transaction with id: " + t.getId() + " could not be added to result array."); else countAdded++;
            }
        }
        log.trace("Number of Payment objects added to result: " + countAdded);
        log.trace("Result List has " + result.size() + " Transaction elements");
        log.debug("leaving decorateTransactions( List<Transaction> )");
        return result;
    }

    public void delete(Payment txn) throws TransactionPersistenceException {
    }

    public Payment insert(Payment rptTxn) throws TransactionPersistenceException {
        return null;
    }

    public Payment select(long key) throws TransactionPersistenceException {
        log.debug("select(" + Long.toString(key) + ")");
        Payment pt = null;
        try {
            pt = (Payment) decorateTransaction(tDao.select(key));
        } catch (NoTransactionFlavourFoundException e) {
            log.error("No Payment could be found for the given key");
            throw new TransactionPersistenceException("No Payment flavour found", e);
        } catch (com.schinzer.fin.dao.base.PersistenceException e) {
            log.error("A persistence problem occurred when loading (Payment)Transaction object with the given key");
            throw new TransactionPersistenceException(e);
        }
        return pt;
    }

    public List<Payment> select(TransactionFilter filter) throws TransactionPersistenceException {
        log.debug("select( " + filter.toString() + " )");
        Database db;
        try {
            db = CastorHelper.getInstance().getJdoManager().getDatabase();
        } catch (PersistenceException e) {
            log.error("Problem instantiating the Database", e);
            throw new TransactionPersistenceException("A problem occurred retrieving database access");
        }
        log.trace("initializing ArrayList<Long>");
        List<Long> list = new ArrayList<Long>();
        log.trace("defining SQL query");
        StringBuffer query = new StringBuffer();
        query.append("SELECT t.id FROM com.schinzer.fin.model.base.Transaction t,");
        query.append(" com.schinzer.fin.dao.base.PaymentTransactionDTO pt");
        query.append(" WHERE t.id=pt.id");
        Integer paramId = 1;
        if (filter.hasCounterpartFilter()) {
            query.append(" AND t.counterpart LIKE '$");
            query.append(paramId.toString());
            query.append("'");
            paramId += 1;
        }
        if (filter.hasDescriptionFilter()) {
            query.append(" AND t.description LIKE '$");
            query.append(paramId.toString());
            query.append("'");
            paramId += 1;
        }
        if (filter.hasMinDateFilter()) {
            query.append(" AND t.date > $");
            query.append(paramId.toString());
            paramId += 1;
        }
        if (filter.hasMaxDateFilter()) {
            query.append(" AND t.date < $");
            query.append(paramId.toString());
            paramId += 1;
        }
        if (filter.hasMinAmountFilter()) {
            query.append(" AND t.amount > $");
            query.append(paramId.toString());
            paramId += 1;
        }
        if (filter.hasMaxAmountFilter()) {
            query.append(" AND t.amount < $");
            query.append(paramId.toString());
            paramId += 1;
        }
        if (filter.getAccountId() > 0) {
            query.append(" AND t.account = $");
            query.append(paramId.toString());
            paramId += 1;
        }
        query.append(" ORDER BY t.date");
        query.append(" LIMIT $");
        query.append(paramId.toString());
        paramId += 1;
        query.append(" OFFSET $");
        query.append(paramId.toString());
        log.trace("SQL query string: " + query.toString());
        try {
            db.begin();
            OQLQuery oql;
            oql = db.getOQLQuery(query.toString());
            log.trace("OQLQuery: " + oql.toString());
            log.debug("Binding TransactionFilter parameters to OQLQuery:");
            if (filter.hasCounterpartFilter()) {
                log.trace("... counterpartFilter");
                oql.bind(filter.getCounterpartFilter());
            }
            if (filter.hasDescriptionFilter()) {
                log.trace("... descriptionFilter");
                oql.bind(filter.getDescriptionFilter());
            }
            if (filter.hasMinDateFilter()) {
                log.trace("... minDate");
                oql.bind(filter.getMinDate());
            }
            if (filter.hasMaxDateFilter()) {
                log.trace("... maxDate");
                oql.bind(filter.getMaxDate());
            }
            if (filter.hasMinAmountFilter()) {
                log.trace("... minAmount");
                oql.bind(filter.getMinAmount());
            }
            if (filter.hasMaxAmountFilter()) {
                log.trace("... maxAmount");
                oql.bind(filter.getMaxAmount());
            }
            if (filter.getAccountId() > 0) {
                log.trace("... accountId");
                oql.bind(filter.getAccountId());
            }
            log.trace("... pageSize");
            oql.bind(filter.getPageSize());
            log.trace("... pageCount");
            oql.bind(((filter.getPageCount() - 1) * filter.getPageSize()));
            log.debug("Execute OQLQuery and retrieve result set");
            QueryResults results;
            results = oql.execute(Database.ReadOnly);
            log.trace("result set has " + results.size() + " rows");
            while (results.hasMore()) {
                log.trace("Retrieve next Account Object from results");
                Long l = (Long) results.next();
                list.add(l);
            }
            log.debug("var list is populated with " + list.size() + " Elements");
            log.trace("Explicitly close the QueryResults");
            results.close();
            log.trace("Explicitly close the OQLQuery");
            oql.close();
            log.trace("Explicitly commit transaction");
            db.commit();
        } catch (PersistenceException e) {
            log.error("Problem with persistence", e);
        }
        List<Payment> result = new ArrayList<Payment>();
        log.trace("Iterating List<Long> object from query result");
        for (Long l : list) {
            Payment p = this.select(l);
        }
        return result;
    }

    public boolean update(Payment txn) throws TransactionPersistenceException {
        return false;
    }

    public List<Payment> xDecorateTransactions(List<Transaction> txns) throws TransactionPersistenceException {
        log.debug("xDecorateTransactions( List " + txns.toString() + ")");
        log.trace("Incoming List has " + txns.size() + " Transaction elements");
        log.trace("Preparing iteration through List");
        int countAdded = 0;
        List<Payment> result = new ArrayList<Payment>();
        log.trace("Iterating List<Transaction> object from input parameter");
        for (Transaction t : txns) {
            log.trace("Creating Payment [decoration happens here ;-)]");
            Payment pt = null;
            try {
                pt = (Payment) decorateTransaction(t);
            } catch (NoTransactionFlavourFoundException e) {
                StringBuffer msg = new StringBuffer();
                msg.append("No Payment data was found for Transaction object with id: ");
                msg.append(t.getId());
                log.warn(msg.toString(), e);
            } catch (com.schinzer.fin.dao.base.PersistenceException e) {
                log.error("An error occurred on the persistence layer");
                throw new TransactionPersistenceException(e);
            }
            if (pt != null) {
                if (!result.add(pt)) log.error("Payment with id: " + pt.getId() + " could not be added to result array."); else countAdded++;
            } else {
                StringBuffer msg = new StringBuffer();
                msg.append("No Payment data found for Transaction with Id ");
                msg.append(t.getId());
                log.warn(msg.toString());
            }
        }
        log.trace("Number of Payment objects added to result: " + countAdded);
        log.trace("Result List has " + result.size() + " Transaction elements");
        log.debug("leaving decorateTransactions( List<Transaction> )");
        return result;
    }
}
