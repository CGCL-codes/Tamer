package org.koossery.adempiere.core.contract.interfaces.user;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.criteria.user.AD_UserDef_WinCriteria;
import org.koossery.adempiere.core.contract.dto.user.AD_UserDef_WinDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.IKTADempiereServiceComposed;

public interface IAD_UserDef_WinSVCO extends IKTADempiereServiceComposed {

    public int createAD_UserDef_Win(Properties ctx, AD_UserDef_WinDTO aD_UserDef_WinDTO, String trxname) throws KTAdempiereException;

    public AD_UserDef_WinDTO findOneAD_UserDef_Win(Properties ctx, int aD_UserDef_WinID) throws KTAdempiereException;

    public ArrayList<AD_UserDef_WinDTO> findAD_UserDef_Win(Properties ctx, AD_UserDef_WinCriteria aD_UserDef_WinCriteria) throws KTAdempiereException;

    public void updateAD_UserDef_Win(Properties ctx, AD_UserDef_WinDTO aD_UserDef_WinDTO) throws KTAdempiereException;

    public boolean deleteAD_UserDef_Win(Properties ctx, AD_UserDef_WinCriteria criteria) throws KTAdempiereException;
}
