package org.koossery.adempiere.core.contract.interfaces.hr;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.dto.hr.*;
import org.koossery.adempiere.core.contract.criteria.hr.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IHR_DepartmentSVCO {

    public int createHR_Department(Properties ctx, HR_DepartmentDTO hR_DepartmentDTO, String trxname) throws KTAdempiereException;

    public HR_DepartmentDTO findOneHR_Department(Properties ctx, int hR_DepartmentID) throws KTAdempiereException;

    public ArrayList<HR_DepartmentDTO> findHR_Department(Properties ctx, HR_DepartmentCriteria hR_DepartmentCriteria) throws KTAdempiereException;

    public void updateHR_Department(Properties ctx, HR_DepartmentDTO hR_DepartmentDTO) throws KTAdempiereException;

    public boolean updateHR_Department(HR_DepartmentCriteria criteria) throws KTAdempiereException;
}
