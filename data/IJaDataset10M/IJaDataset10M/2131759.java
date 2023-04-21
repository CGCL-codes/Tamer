package org.jabusuite.webclient.transaction.creditnote;

import org.jabusuite.client.datalist.PageableJbsObjectList;
import org.jabusuite.client.transaction.PlCreditNotes;
import org.jabusuite.client.utils.ClientTools;
import org.jabusuite.core.utils.JbsBaseObject;
import org.jabusuite.transaction.CreditNote;
import org.jabusuite.transaction.session.CreditNotesRemote;
import org.jabusuite.webclient.dataediting.FmEditJbsBaseObject;
import org.jabusuite.webclient.main.ClientGlobals;
import org.jabusuite.webclient.transaction.PnTransactionList;

/**
 *
 * @author hilwers
 * @date 2008-05-14
 */
public class PnCreditNoteList extends PnTransactionList {

    @Override
    protected PageableJbsObjectList createJbsObjects() {
        return new PlCreditNotes(ClientGlobals.getUser(), ClientGlobals.getCompany());
    }

    @Override
    protected FmEditJbsBaseObject createEditForm() {
        return new FmCreditNoteEdit();
    }

    @Override
    public void doDeleteJbsObject(JbsBaseObject jbsObject) throws Exception {
        CreditNotesRemote cnManager = (CreditNotesRemote) ClientTools.getRemoteBean(CreditNotesRemote.class);
        cnManager.deleteDataset((CreditNote) jbsObject, ClientGlobals.getUser());
    }

    @Override
    protected void fillToolbar() {
        super.fillToolbar();
    }
}
