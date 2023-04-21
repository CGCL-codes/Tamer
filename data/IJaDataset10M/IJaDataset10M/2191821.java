package org.koossery.adempiere.core.backend.interfaces.sisv.cost;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.dto.cost.*;
import org.koossery.adempiere.core.contract.criteria.cost.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;

public interface IM_CostDetailSISV {

    public int createM_CostDetail(Properties ctx, M_CostDetailDTO m_CostDetailDTO, String trxname) throws KTAdempiereAppException;

    public M_CostDetailDTO getM_CostDetail(Properties ctx, int m_CostDetailID, String trxname) throws KTAdempiereAppException;

    public ArrayList<M_CostDetailDTO> findM_CostDetail(Properties ctx, M_CostDetailCriteria m_CostDetailCriteria) throws KTAdempiereAppException;

    public void updateM_CostDetail(Properties ctx, M_CostDetailDTO m_CostDetailDTO) throws KTAdempiereAppException;

    public boolean updateM_CostDetail(M_CostDetailCriteria m_CostDetailCriteria) throws KTAdempiereAppException;
}
