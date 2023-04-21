package org.koossery.adempiere.core.backend.interfaces.sisv.payroll;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.dto.payroll.*;
import org.koossery.adempiere.core.contract.criteria.payroll.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;

public interface IHR_YearSISV {

    public int createHR_Year(Properties ctx, HR_YearDTO hR_YearDTO, String trxname) throws KTAdempiereAppException;

    public HR_YearDTO getHR_Year(Properties ctx, int hR_YearID, String trxname) throws KTAdempiereAppException;

    public ArrayList<HR_YearDTO> findHR_Year(Properties ctx, HR_YearCriteria hR_YearCriteria) throws KTAdempiereAppException;

    public void updateHR_Year(Properties ctx, HR_YearDTO hR_YearDTO) throws KTAdempiereAppException;

    public boolean updateHR_Year(HR_YearCriteria hR_YearCriteria) throws KTAdempiereAppException;
}
