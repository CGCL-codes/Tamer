package org.koossery.adempiere.core.contract.interfaces.generated;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.dto.generated.*;
import org.koossery.adempiere.core.contract.criteria.generated.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IC_ProjectSVCO {

    public int createC_Project(Properties ctx, C_ProjectDTO c_ProjectDTO, String trxname) throws KTAdempiereException;

    public C_ProjectDTO findOneC_Project(Properties ctx, int c_ProjectID) throws KTAdempiereException;

    public ArrayList<C_ProjectDTO> findC_Project(Properties ctx, C_ProjectCriteria c_ProjectCriteria) throws KTAdempiereException;

    public void updateC_Project(Properties ctx, C_ProjectDTO c_ProjectDTO) throws KTAdempiereException;

    public boolean updateC_Project(C_ProjectCriteria criteria) throws KTAdempiereException;
}
