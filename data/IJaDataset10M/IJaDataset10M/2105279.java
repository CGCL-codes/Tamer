package org.koossery.adempiere.core.backend.interfaces.dao.request;

import java.util.ArrayList;
import org.koossery.adempiere.core.backend.interfaces.dao.IKTADempiereDataAccessObject;
import org.koossery.adempiere.core.contract.criteria.request.R_CategoryCriteria;
import org.koossery.adempiere.core.contract.dto.request.R_CategoryDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IR_CategoryDAO extends IKTADempiereDataAccessObject {

    public boolean isDuplicate(R_CategoryCriteria criteria) throws KTAdempiereException;

    public boolean update(R_CategoryCriteria r_CategoryCriteria) throws KTAdempiereException;

    public ArrayList<R_CategoryDTO> getR_Category(R_CategoryCriteria r_CategoryCriteria) throws KTAdempiereException;
}
