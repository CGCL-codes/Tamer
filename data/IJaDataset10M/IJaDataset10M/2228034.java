package org.koossery.adempiere.core.backend.interfaces.sisv.product;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.dto.product.*;
import org.koossery.adempiere.core.contract.criteria.product.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;

public interface IM_ProductPriceSISV {

    public int createM_ProductPrice(Properties ctx, M_ProductPriceDTO m_ProductPriceDTO, String trxname) throws KTAdempiereAppException;

    public M_ProductPriceDTO getM_ProductPrice(Properties ctx, int m_ProductPriceID, String trxname) throws KTAdempiereAppException;

    public ArrayList<M_ProductPriceDTO> findM_ProductPrice(Properties ctx, M_ProductPriceCriteria m_ProductPriceCriteria) throws KTAdempiereAppException;

    public void updateM_ProductPrice(Properties ctx, M_ProductPriceDTO m_ProductPriceDTO) throws KTAdempiereAppException;

    public boolean updateM_ProductPrice(M_ProductPriceCriteria m_ProductPriceCriteria) throws KTAdempiereAppException;
}
