package org.koossery.adempiere.core.contract.interfaces.pa;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.dto.pa.*;
import org.koossery.adempiere.core.contract.criteria.pa.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IPA_ReportSVCO {

    public int createPA_Report(Properties ctx, PA_ReportDTO pA_ReportDTO, String trxname) throws KTAdempiereException;

    public PA_ReportDTO findOnePA_Report(Properties ctx, int pA_ReportID) throws KTAdempiereException;

    public ArrayList<PA_ReportDTO> findPA_Report(Properties ctx, PA_ReportCriteria pA_ReportCriteria) throws KTAdempiereException;

    public void updatePA_Report(Properties ctx, PA_ReportDTO pA_ReportDTO) throws KTAdempiereException;

    public boolean updatePA_Report(PA_ReportCriteria criteria) throws KTAdempiereException;
}
