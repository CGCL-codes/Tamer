package org.koossery.adempiere.core.backend.interfaces.dao.pa;

import java.util.ArrayList;
import org.koossery.adempiere.core.contract.dto.pa.*;
import org.koossery.adempiere.core.contract.criteria.pa.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IPA_ReportLineDAO {

    public boolean isDuplicate(String name) throws KTAdempiereException;

    public boolean update(PA_ReportLineCriteria pA_ReportLineCriteria) throws KTAdempiereException;

    public ArrayList<PA_ReportLineDTO> getPA_ReportLine(PA_ReportLineCriteria pA_ReportLineCriteria) throws KTAdempiereException;
}
