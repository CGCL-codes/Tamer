package org.koossery.adempiere.core.backend.interfaces.sisv.generated;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.dto.generated.*;
import org.koossery.adempiere.core.contract.criteria.generated.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;

public interface IC_TaxDeclarationLineSISV {

    public int createC_TaxDeclarationLine(Properties ctx, C_TaxDeclarationLineDTO c_TaxDeclarationLineDTO, String trxname) throws KTAdempiereAppException;

    public C_TaxDeclarationLineDTO getC_TaxDeclarationLine(Properties ctx, int c_TaxDeclarationLineID, String trxname) throws KTAdempiereAppException;

    public ArrayList<C_TaxDeclarationLineDTO> findC_TaxDeclarationLine(Properties ctx, C_TaxDeclarationLineCriteria c_TaxDeclarationLineCriteria) throws KTAdempiereAppException;

    public void updateC_TaxDeclarationLine(Properties ctx, C_TaxDeclarationLineDTO c_TaxDeclarationLineDTO) throws KTAdempiereAppException;

    public boolean updateC_TaxDeclarationLine(C_TaxDeclarationLineCriteria c_TaxDeclarationLineCriteria) throws KTAdempiereAppException;
}
