package br.gov.frameworkdemoiselle.internal.implementation;

import javax.enterprise.context.SessionScoped;
import br.gov.frameworkdemoiselle.DemoiselleException;
import br.gov.frameworkdemoiselle.transaction.Transaction;
import br.gov.frameworkdemoiselle.transaction.Transactional;

/**
 * Transaction strategy that actually does nothing but raise exceptions.
 * 
 * @author SERPRO
 * @see Transaction
 */
@SessionScoped
public class DefaultTransaction implements Transaction {

    private static final long serialVersionUID = 1L;

    @Override
    public void begin() {
        throw getException();
    }

    @Override
    public void commit() {
        throw getException();
    }

    @Override
    public boolean isActive() {
        throw getException();
    }

    @Override
    public boolean isMarkedRollback() {
        throw getException();
    }

    @Override
    public void rollback() {
        throw getException();
    }

    @Override
    public void setRollbackOnly() {
        throw getException();
    }

    private DemoiselleException getException() {
        return new DemoiselleException(CoreBundle.get().getString("transaction-not-defined", Transactional.class.getSimpleName()));
    }
}
