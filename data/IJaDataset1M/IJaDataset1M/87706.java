package org.koossery.adempiere.core.backend.interfaces.sisv.user;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.backend.interfaces.sisv.IKTADempiereSimpleService;
import org.koossery.adempiere.core.contract.criteria.user.AD_UserCriteria;
import org.koossery.adempiere.core.contract.dto.user.AD_UserDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;

public interface IAD_UserSISV extends IKTADempiereSimpleService {

    public int createAD_User(Properties ctx, AD_UserDTO aD_UserDTO, String trxname) throws KTAdempiereAppException;

    public AD_UserDTO getAD_User(Properties ctx, int aD_UserID, String trxname) throws KTAdempiereAppException;

    public ArrayList<AD_UserDTO> findAD_User(Properties ctx, AD_UserCriteria aD_UserCriteria) throws KTAdempiereAppException;

    public void updateAD_User(Properties ctx, AD_UserDTO aD_UserDTO) throws KTAdempiereAppException;

    public boolean deleteAD_User(Properties ctx, AD_UserCriteria aD_UserCriteria) throws KTAdempiereAppException;
}
