package org.koossery.adempiere.core.contract.interfaces.bank;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.dto.bank.*;
import org.koossery.adempiere.core.contract.criteria.bank.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IC_BankSVCO {

    public int createC_Bank(Properties ctx, C_BankDTO c_BankDTO, String trxname) throws KTAdempiereException;

    public C_BankDTO findOneC_Bank(Properties ctx, int c_BankID) throws KTAdempiereException;

    public ArrayList<C_BankDTO> findC_Bank(Properties ctx, C_BankCriteria c_BankCriteria) throws KTAdempiereException;

    public void updateC_Bank(Properties ctx, C_BankDTO c_BankDTO) throws KTAdempiereException;

    public boolean updateC_Bank(C_BankCriteria criteria) throws KTAdempiereException;
}
