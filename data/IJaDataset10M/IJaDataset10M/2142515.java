package org.koossery.adempiere.core.backend.interfaces.sisv.org.cash;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.backend.interfaces.sisv.IKTADempiereSimpleService;
import org.koossery.adempiere.core.contract.criteria.org.cash.C_CashBook_AcctCriteria;
import org.koossery.adempiere.core.contract.dto.org.cash.C_CashBook_AcctDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;

public interface IC_CashBook_AcctSISV extends IKTADempiereSimpleService {

    public int createC_CashBook_Acct(Properties ctx, C_CashBook_AcctDTO c_CashBook_AcctDTO, String trxname) throws KTAdempiereAppException;

    public C_CashBook_AcctDTO getC_CashBook_Acct(Properties ctx, int c_CashBookID, int c_AcctSchemaID, String trxname) throws KTAdempiereAppException;

    public ArrayList<C_CashBook_AcctDTO> findC_CashBook_Acct(Properties ctx, C_CashBook_AcctCriteria c_CashBook_AcctCriteria) throws KTAdempiereAppException;

    public void updateC_CashBook_Acct(Properties ctx, C_CashBook_AcctDTO c_CashBook_AcctDTO) throws KTAdempiereAppException;

    public boolean deleteC_CashBook_Acct(Properties ctx, C_CashBook_AcctCriteria c_CashBook_AcctCriteria) throws KTAdempiereAppException;
}
