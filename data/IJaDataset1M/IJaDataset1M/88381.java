package org.koossery.adempiere.core.contract.interfaces.report;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.dto.report.FinReportDTO;
import org.koossery.adempiere.core.contract.dto.report.StatementOfAccountDTO;
import org.koossery.adempiere.core.contract.dto.report.TrialBalanceDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IKTADempiereReportManagerSVCO {

    public ArrayList<StatementOfAccountDTO> getStatementOfAccount(Properties ctx, int ad_pinstance_id) throws KTAdempiereException;

    public ArrayList<TrialBalanceDTO> getTrialBalance(Properties ctx, int ad_pinstance_id) throws KTAdempiereException;

    public ArrayList<FinReportDTO> createFinReport(Properties ctx, int ad_pinstance_id) throws KTAdempiereException;
}
