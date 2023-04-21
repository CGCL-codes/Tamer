package org.koossery.adempiere.core.backend.interfaces.sisv.role;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.backend.interfaces.sisv.IKTADempiereSimpleService;
import org.koossery.adempiere.core.contract.criteria.role.AD_Table_AccessCriteria;
import org.koossery.adempiere.core.contract.dto.role.AD_Table_AccessDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;

public interface IAD_Table_AccessSISV extends IKTADempiereSimpleService {

    public int createAD_Table_Access(Properties ctx, AD_Table_AccessDTO aD_Table_AccessDTO, String trxname) throws KTAdempiereAppException;

    public AD_Table_AccessDTO getAD_Table_Access(Properties ctx, int aD_Table_ID, int aD_Role_ID, String accesstyperule, String trxname) throws KTAdempiereAppException;

    public ArrayList<AD_Table_AccessDTO> findAD_Table_Access(Properties ctx, AD_Table_AccessCriteria aD_Table_AccessCriteria) throws KTAdempiereAppException;

    public void updateAD_Table_Access(Properties ctx, AD_Table_AccessDTO aD_Table_AccessDTO) throws KTAdempiereAppException;

    public boolean deleteAD_Table_Access(Properties ctx, AD_Table_AccessCriteria aD_Table_AccessCriteria) throws KTAdempiereAppException;
}
