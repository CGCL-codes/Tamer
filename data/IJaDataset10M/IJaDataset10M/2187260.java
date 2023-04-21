package org.koossery.adempiere.core.backend.interfaces.sisv.payroll;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.dto.payroll.*;
import org.koossery.adempiere.core.contract.criteria.payroll.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;

public interface IHR_MovementSISV {

    public int createHR_Movement(Properties ctx, HR_MovementDTO hR_MovementDTO, String trxname) throws KTAdempiereAppException;

    public HR_MovementDTO getHR_Movement(Properties ctx, int hR_MovementID, String trxname) throws KTAdempiereAppException;

    public ArrayList<HR_MovementDTO> findHR_Movement(Properties ctx, HR_MovementCriteria hR_MovementCriteria) throws KTAdempiereAppException;

    public void updateHR_Movement(Properties ctx, HR_MovementDTO hR_MovementDTO) throws KTAdempiereAppException;

    public boolean updateHR_Movement(HR_MovementCriteria hR_MovementCriteria) throws KTAdempiereAppException;
}
