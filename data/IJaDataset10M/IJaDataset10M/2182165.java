package org.koossery.adempiere.core.backend.interfaces.sisv.product;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.dto.product.*;
import org.koossery.adempiere.core.contract.criteria.product.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;

public interface IM_RelatedProductSISV {

    public int createM_RelatedProduct(Properties ctx, M_RelatedProductDTO m_RelatedProductDTO, String trxname) throws KTAdempiereAppException;

    public M_RelatedProductDTO getM_RelatedProduct(Properties ctx, int m_RelatedProductID, String trxname) throws KTAdempiereAppException;

    public ArrayList<M_RelatedProductDTO> findM_RelatedProduct(Properties ctx, M_RelatedProductCriteria m_RelatedProductCriteria) throws KTAdempiereAppException;

    public void updateM_RelatedProduct(Properties ctx, M_RelatedProductDTO m_RelatedProductDTO) throws KTAdempiereAppException;

    public boolean updateM_RelatedProduct(M_RelatedProductCriteria m_RelatedProductCriteria) throws KTAdempiereAppException;
}
