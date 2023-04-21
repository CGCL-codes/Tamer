package org.koossery.adempiere.core.backend.interfaces.dao.generated;

import java.util.ArrayList;
import org.koossery.adempiere.core.contract.dto.generated.*;
import org.koossery.adempiere.core.contract.criteria.generated.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IGL_JournalDAO {

    public boolean isDuplicate(String name) throws KTAdempiereException;

    public boolean update(GL_JournalCriteria gL_JournalCriteria) throws KTAdempiereException;

    public ArrayList<GL_JournalDTO> getGL_Journal(GL_JournalCriteria gL_JournalCriteria) throws KTAdempiereException;
}
