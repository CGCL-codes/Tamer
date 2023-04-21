package org.koossery.adempiere.core.backend.interfaces.dao.system;

import java.util.ArrayList;
import org.koossery.adempiere.core.backend.interfaces.dao.IKTADempiereDataAccessObject;
import org.koossery.adempiere.core.contract.criteria.system.R_IssueSystemCriteria;
import org.koossery.adempiere.core.contract.dto.system.R_IssueSystemDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IR_IssueSystemDAO extends IKTADempiereDataAccessObject {

    public boolean isDuplicate(R_IssueSystemCriteria criteria) throws KTAdempiereException;

    public boolean update(R_IssueSystemCriteria r_IssueSystemCriteria) throws KTAdempiereException;

    public ArrayList<R_IssueSystemDTO> getR_IssueSystem(R_IssueSystemCriteria r_IssueSystemCriteria) throws KTAdempiereException;
}
