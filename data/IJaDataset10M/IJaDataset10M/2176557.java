package org.koossery.adempiere.core.backend.interfaces.sisv.ad;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.backend.interfaces.sisv.IKTADempiereSimpleService;
import org.koossery.adempiere.core.contract.criteria.ad.AD_ClientCriteria;
import org.koossery.adempiere.core.contract.dto.ad.AD_ClientDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;

public interface IAD_ClientSISV extends IKTADempiereSimpleService {

    public int createAD_Client(Properties ctx, AD_ClientDTO aD_ClientDTO, String trxname) throws KTAdempiereAppException;

    public AD_ClientDTO getAD_Client(Properties ctx, int aD_ClientID, String trxname) throws KTAdempiereAppException;

    public ArrayList<AD_ClientDTO> findAD_Client(Properties ctx, AD_ClientCriteria aD_ClientCriteria) throws KTAdempiereAppException;

    public void updateAD_Client(Properties ctx, AD_ClientDTO aD_ClientDTO) throws KTAdempiereAppException;

    public boolean deleteAD_Client(Properties ctx, AD_ClientCriteria aD_ClientCriteria) throws KTAdempiereAppException;

    public int createInitial_Client(Properties ctx, AD_ClientDTO aD_ClientDTO, String trxname) throws KTAdempiereAppException;
}
