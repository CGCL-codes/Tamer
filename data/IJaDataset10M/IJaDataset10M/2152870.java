package org.koossery.adempiere.core.backend.interfaces.sisv.pa;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.dto.pa.*;
import org.koossery.adempiere.core.contract.criteria.pa.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;

public interface IPA_ReportLineSISV {

    public int createPA_ReportLine(Properties ctx, PA_ReportLineDTO pA_ReportLineDTO, String trxname) throws KTAdempiereAppException;

    public PA_ReportLineDTO getPA_ReportLine(Properties ctx, int pA_ReportLineID, String trxname) throws KTAdempiereAppException;

    public ArrayList<PA_ReportLineDTO> findPA_ReportLine(Properties ctx, PA_ReportLineCriteria pA_ReportLineCriteria) throws KTAdempiereAppException;

    public void updatePA_ReportLine(Properties ctx, PA_ReportLineDTO pA_ReportLineDTO) throws KTAdempiereAppException;

    public boolean updatePA_ReportLine(PA_ReportLineCriteria pA_ReportLineCriteria) throws KTAdempiereAppException;
}
