package org.koossery.adempiere.core.backend.interfaces.dao.product;

import java.util.ArrayList;
import org.koossery.adempiere.core.contract.dto.product.*;
import org.koossery.adempiere.core.contract.criteria.product.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IM_ProductPriceDAO {

    public boolean isDuplicate(M_ProductPriceCriteria criteria) throws KTAdempiereException;

    public boolean update(M_ProductPriceCriteria m_ProductPriceCriteria) throws KTAdempiereException;

    public ArrayList<M_ProductPriceDTO> getM_ProductPrice(M_ProductPriceCriteria m_ProductPriceCriteria) throws KTAdempiereException;
}
