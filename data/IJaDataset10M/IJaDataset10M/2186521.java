package org.koossery.adempiere.core.contract.interfaces.ad;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.criteria.ad.AD_ReferenceCriteria;
import org.koossery.adempiere.core.contract.dto.ad.AD_ReferenceDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IAD_ReferenceSVCO {

    public int createAD_Reference(Properties ctx, AD_ReferenceDTO aD_ReferenceDTO, String trxname) throws KTAdempiereException;

    public AD_ReferenceDTO findOneAD_Reference(Properties ctx, int aD_ReferenceID) throws KTAdempiereException;

    public ArrayList<AD_ReferenceDTO> findAD_Reference(Properties ctx, AD_ReferenceCriteria aD_ReferenceCriteria) throws KTAdempiereException;

    public void updateAD_Reference(Properties ctx, AD_ReferenceDTO aD_ReferenceDTO) throws KTAdempiereException;

    public boolean updateAD_Reference(AD_ReferenceCriteria criteria) throws KTAdempiereException;
}
