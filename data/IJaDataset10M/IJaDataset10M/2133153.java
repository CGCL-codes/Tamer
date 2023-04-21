package org.koossery.adempiere.core.backend.interfaces.sisv.payroll;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.dto.payroll.*;
import org.koossery.adempiere.core.contract.criteria.payroll.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;

public interface IHR_Concept_AcctSISV {

    public int createHR_Concept_Acct(Properties ctx, HR_Concept_AcctDTO hR_Concept_AcctDTO, String trxname) throws KTAdempiereAppException;

    public HR_Concept_AcctDTO getHR_Concept_Acct(Properties ctx, int hR_Concept_AcctID, String trxname) throws KTAdempiereAppException;

    public ArrayList<HR_Concept_AcctDTO> findHR_Concept_Acct(Properties ctx, HR_Concept_AcctCriteria hR_Concept_AcctCriteria) throws KTAdempiereAppException;

    public void updateHR_Concept_Acct(Properties ctx, HR_Concept_AcctDTO hR_Concept_AcctDTO) throws KTAdempiereAppException;

    public boolean updateHR_Concept_Acct(HR_Concept_AcctCriteria hR_Concept_AcctCriteria) throws KTAdempiereAppException;
}
