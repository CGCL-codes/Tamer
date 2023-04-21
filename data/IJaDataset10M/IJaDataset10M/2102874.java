package org.koossery.adempiere.core.backend.interfaces.dao.pa;

import java.util.ArrayList;
import org.koossery.adempiere.core.contract.dto.pa.*;
import org.koossery.adempiere.core.contract.criteria.pa.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IPA_RatioElementDAO {

    public boolean isDuplicate(String name) throws KTAdempiereException;

    public boolean update(PA_RatioElementCriteria pA_RatioElementCriteria) throws KTAdempiereException;

    public ArrayList<PA_RatioElementDTO> getPA_RatioElement(PA_RatioElementCriteria pA_RatioElementCriteria) throws KTAdempiereException;
}
