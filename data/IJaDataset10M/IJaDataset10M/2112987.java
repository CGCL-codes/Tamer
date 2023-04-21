package org.koossery.adempiere.core.backend.interfaces.sisv.generated;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.dto.generated.*;
import org.koossery.adempiere.core.contract.criteria.generated.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;

public interface IC_DocTypeSISV {

    public int createC_DocType(Properties ctx, C_DocTypeDTO c_DocTypeDTO, String trxname) throws KTAdempiereAppException;

    public C_DocTypeDTO getC_DocType(Properties ctx, int c_DocTypeID, String trxname) throws KTAdempiereAppException;

    public ArrayList<C_DocTypeDTO> findC_DocType(Properties ctx, C_DocTypeCriteria c_DocTypeCriteria) throws KTAdempiereAppException;

    public void updateC_DocType(Properties ctx, C_DocTypeDTO c_DocTypeDTO) throws KTAdempiereAppException;

    public boolean updateC_DocType(C_DocTypeCriteria c_DocTypeCriteria) throws KTAdempiereAppException;
}
