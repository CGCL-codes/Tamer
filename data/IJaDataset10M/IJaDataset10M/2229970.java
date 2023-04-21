package org.koossery.adempiere.core.contract.interfaces.data.replication;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.criteria.data.replication.AD_Replication_RunCriteria;
import org.koossery.adempiere.core.contract.dto.data.replication.AD_Replication_RunDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.IKTADempiereServiceComposed;

public interface IAD_Replication_RunSVCO extends IKTADempiereServiceComposed {

    public int createAD_Replication_Run(Properties ctx, AD_Replication_RunDTO aD_Replication_RunDTO, String trxname) throws KTAdempiereException;

    public AD_Replication_RunDTO findOneAD_Replication_Run(Properties ctx, int aD_Replication_RunID) throws KTAdempiereException;

    public ArrayList<AD_Replication_RunDTO> findAD_Replication_Run(Properties ctx, AD_Replication_RunCriteria aD_Replication_RunCriteria) throws KTAdempiereException;

    public void updateAD_Replication_Run(Properties ctx, AD_Replication_RunDTO aD_Replication_RunDTO) throws KTAdempiereException;

    public boolean deleteAD_Replication_Run(Properties ctx, AD_Replication_RunCriteria criteria) throws KTAdempiereException;
}
