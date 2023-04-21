package org.koossery.adempiere.core.backend.interfaces.sisv.bank;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.dto.bank.*;
import org.koossery.adempiere.core.contract.criteria.bank.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;

public interface IC_BankStatementSISV {

    public int createC_BankStatement(Properties ctx, C_BankStatementDTO c_BankStatementDTO, String trxname) throws KTAdempiereAppException;

    public C_BankStatementDTO getC_BankStatement(Properties ctx, int c_BankStatementID, String trxname) throws KTAdempiereAppException;

    public ArrayList<C_BankStatementDTO> findC_BankStatement(Properties ctx, C_BankStatementCriteria c_BankStatementCriteria) throws KTAdempiereAppException;

    public void updateC_BankStatement(Properties ctx, C_BankStatementDTO c_BankStatementDTO) throws KTAdempiereAppException;

    public boolean updateC_BankStatement(C_BankStatementCriteria c_BankStatementCriteria) throws KTAdempiereAppException;
}
