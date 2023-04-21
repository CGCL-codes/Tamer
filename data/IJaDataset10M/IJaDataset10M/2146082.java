package org.koossery.adempiere.core.backend.interfaces.dao.mail;

import java.util.ArrayList;
import org.koossery.adempiere.core.backend.interfaces.dao.IKTADempiereDataAccessObject;
import org.koossery.adempiere.core.contract.criteria.mail.R_MailTextCriteria;
import org.koossery.adempiere.core.contract.dto.mail.R_MailTextDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

;

public interface IR_MailTextDAO extends IKTADempiereDataAccessObject {

    public boolean isDuplicate(R_MailTextCriteria criteria) throws KTAdempiereException;

    public boolean update(R_MailTextCriteria r_MailTextCriteria) throws KTAdempiereException;

    public ArrayList<R_MailTextDTO> getR_MailText(R_MailTextCriteria r_MailTextCriteria) throws KTAdempiereException;
}
