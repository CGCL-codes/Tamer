package org.koossery.adempiere.core.backend.interfaces.dao.generated;

import java.util.ArrayList;
import org.koossery.adempiere.core.contract.dto.generated.*;
import org.koossery.adempiere.core.contract.criteria.generated.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IGL_JournalLineDAO {

    public boolean isDuplicate(String name) throws KTAdempiereException;

    public boolean update(GL_JournalLineCriteria gL_JournalLineCriteria) throws KTAdempiereException;

    public ArrayList<GL_JournalLineDTO> getGL_JournalLine(GL_JournalLineCriteria gL_JournalLineCriteria) throws KTAdempiereException;
}
