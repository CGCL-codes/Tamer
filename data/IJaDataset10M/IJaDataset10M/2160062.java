package org.magicdroid.app.sqlite;

import org.magicdroid.commons.Closure;
import org.magicdroid.commons.Injector.Inject;
import org.magicdroid.services.TransactionService;

public class SQliteTransactionServiceImpl implements TransactionService {

    private final ClientDbSqlite clientDbSqlite;

    @Inject
    public SQliteTransactionServiceImpl(ClientDbSqlite clientDbSqlite) {
        this.clientDbSqlite = clientDbSqlite;
    }

    private void begin() {
        this.clientDbSqlite.getDatabase().beginTransaction();
    }

    private void commit() {
        this.clientDbSqlite.getDatabase().setTransactionSuccessful();
    }

    private void rollback() {
    }

    private void release() {
        this.clientDbSqlite.getDatabase().endTransaction();
    }

    @Override
    public void tx(Closure block) {
        this.begin();
        try {
            block.process();
            this.commit();
        } catch (RuntimeException e) {
            this.rollback();
            throw e;
        } catch (Throwable e) {
            this.rollback();
            throw new RuntimeException(e);
        } finally {
            this.release();
        }
    }
}
