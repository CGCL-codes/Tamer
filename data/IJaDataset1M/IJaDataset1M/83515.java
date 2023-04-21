package org.koossery.adempiere.core.backend.interfaces.dao.change;

import java.util.ArrayList;
import org.koossery.adempiere.core.backend.interfaces.dao.IKTADempiereDataAccessObject;
import org.koossery.adempiere.core.contract.criteria.change.M_ChangeNoticeCriteria;
import org.koossery.adempiere.core.contract.dto.change.M_ChangeNoticeDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IM_ChangeNoticeDAO extends IKTADempiereDataAccessObject {

    public boolean isDuplicate(M_ChangeNoticeCriteria criteria) throws KTAdempiereException;

    public boolean update(M_ChangeNoticeCriteria m_ChangeNoticeCriteria) throws KTAdempiereException;

    public ArrayList<M_ChangeNoticeDTO> getM_ChangeNotice(M_ChangeNoticeCriteria m_ChangeNoticeCriteria) throws KTAdempiereException;
}
