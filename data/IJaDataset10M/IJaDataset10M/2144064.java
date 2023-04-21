package org.koossery.adempiere.core.contract.interfaces.change;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.criteria.change.AD_ChangeLogCriteria;
import org.koossery.adempiere.core.contract.dto.change.AD_ChangeLogDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.IKTADempiereServiceComposed;

public interface IAD_ChangeLogSVCO extends IKTADempiereServiceComposed {

    public int createAD_ChangeLog(Properties ctx, AD_ChangeLogDTO aD_ChangeLogDTO, String trxname) throws KTAdempiereException;

    public AD_ChangeLogDTO findOneAD_ChangeLog(Properties ctx, int aD_ChangeLogID, int aD_SessionID, int aD_TableID, int aD_ColumnID) throws KTAdempiereException;

    public ArrayList<AD_ChangeLogDTO> findAD_ChangeLog(Properties ctx, AD_ChangeLogCriteria aD_ChangeLogCriteria) throws KTAdempiereException;

    public void updateAD_ChangeLog(Properties ctx, AD_ChangeLogDTO aD_ChangeLogDTO) throws KTAdempiereException;

    public boolean deleteAD_ChangeLog(Properties ctx, AD_ChangeLogCriteria criteria) throws KTAdempiereException;
}
