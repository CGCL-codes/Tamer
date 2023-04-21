package org.koossery.adempiere.core.contract.interfaces.data.utility;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.criteria.data.utility.AD_AttachmentNoteCriteria;
import org.koossery.adempiere.core.contract.dto.data.utility.AD_AttachmentNoteDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.IKTADempiereServiceComposed;

public interface IAD_AttachmentNoteSVCO extends IKTADempiereServiceComposed {

    public int createAD_AttachmentNote(Properties ctx, AD_AttachmentNoteDTO aD_AttachmentNoteDTO, String trxname) throws KTAdempiereException;

    public AD_AttachmentNoteDTO findOneAD_AttachmentNote(Properties ctx, int aD_AttachmentNoteID) throws KTAdempiereException;

    public ArrayList<AD_AttachmentNoteDTO> findAD_AttachmentNote(Properties ctx, AD_AttachmentNoteCriteria aD_AttachmentNoteCriteria) throws KTAdempiereException;

    public void updateAD_AttachmentNote(Properties ctx, AD_AttachmentNoteDTO aD_AttachmentNoteDTO) throws KTAdempiereException;

    public boolean deleteAD_AttachmentNote(Properties ctx, AD_AttachmentNoteCriteria criteria) throws KTAdempiereException;
}
