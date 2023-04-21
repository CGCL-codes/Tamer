package org.koossery.adempiere.core.backend.interfaces.dao.pa;

import java.util.ArrayList;
import org.koossery.adempiere.core.contract.dto.pa.*;
import org.koossery.adempiere.core.contract.criteria.pa.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IPA_GoalDAO {

    public boolean isDuplicate(String name) throws KTAdempiereException;

    public boolean update(PA_GoalCriteria pA_GoalCriteria) throws KTAdempiereException;

    public ArrayList<PA_GoalDTO> getPA_Goal(PA_GoalCriteria pA_GoalCriteria) throws KTAdempiereException;
}
