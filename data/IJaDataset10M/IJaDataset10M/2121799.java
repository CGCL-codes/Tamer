package org.koossery.adempiere.core.backend.interfaces.dao;

import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IJournalBatchDAO {

    public void updateBatch(int journalID) throws KTAdempiereException;
}
