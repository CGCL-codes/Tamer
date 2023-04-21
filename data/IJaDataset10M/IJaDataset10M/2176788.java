package com.googlecode.contraildb.core.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import com.googlecode.contraildb.core.ContrailException;
import com.googlecode.contraildb.core.IContrailService;
import com.googlecode.contraildb.core.IContrailSession;
import com.googlecode.contraildb.core.storage.StorageSystem;
import com.googlecode.contraildb.core.storage.provider.IStorageProvider;

/**
 * Implementation of Contrail service.
 * 
 * @author Ted Stockwell
 */
public class ContrailServiceImpl implements IContrailService {

    private Stack<IContrailSession> _transactions = new Stack<IContrailSession>();

    StorageSystem _storageSystem;

    public ContrailServiceImpl(IStorageProvider storageProvider) throws IOException {
        _storageSystem = new StorageSystem(storageProvider);
    }

    @Override
    public IContrailSession beginSession(Mode mode) throws ContrailException, IOException {
        IContrailSession session = new ContrailSessionImpl(this, mode);
        _transactions.push(session);
        return session;
    }

    @Override
    public IContrailSession beginSession(long revisionNumber) throws IOException, ContrailException {
        IContrailSession session = new ContrailSessionImpl(this, revisionNumber);
        _transactions.push(session);
        return session;
    }

    @Override
    public Collection<IContrailSession> getActiveSessions() {
        return Collections.unmodifiableCollection(_transactions);
    }

    @Override
    public List<Long> getAvailableRevisions() throws IOException {
        return _storageSystem.getAvailableRevisions();
    }

    void onClose(IContrailSession session) {
        _transactions.remove(session);
    }

    @Override
    public void close() throws IOException, ContrailException {
        _storageSystem.close();
    }
}
