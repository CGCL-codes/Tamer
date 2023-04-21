package com.fujitsu.arcon.njs.actions;

import org.unicore.ajo.AbstractAction;
import org.unicore.ajo.ExportTask;
import org.unicore.outcome.AbstractActionStatus;
import org.unicore.outcome.ExportTask_Outcome;
import org.unicore.outcome.Outcome;
import org.unicore.resources.AlternativeUspace;
import com.fujitsu.arcon.njs.KnownActionDB;
import com.fujitsu.arcon.njs.NJSGlobals;

/**
 * Wrapper for ExportTask level
 *
 * @author Sven van den Berghe, fujitsu
 *
 * @version $Revision: 1.4 $ $Date: 2006/03/10 21:35:52 $
 *
 **/
public class ExportTaskEKA extends EKnownAction {

    public static class Factory extends KnownAction.Factory {

        public KnownAction create(AbstractAction a, Outcome o, ParentAction p, RootAJO r, KnownActionDB k) {
            return new ExportTaskEKA(a, o, p, r, k);
        }
    }

    public ExportTaskEKA(AbstractAction a, Outcome o, ParentAction p, RootAJO r, KnownActionDB k) {
        super(a, p, r, k);
        if (o == null) {
            outcome = new ExportTask_Outcome((ExportTask) a, AbstractActionStatus.PENDING);
        } else {
            outcome = (ExportTask_Outcome) o;
        }
        requires_uspace = true;
        seminary = com.fujitsu.arcon.njs.priest.Seminaries.getFileCopySeminary();
        setAAType("EXPORT");
        if (requested_storage == null && requested_alternative_uspace == null) {
            requested_storage = new MappedStorage.Storage(NJSGlobals.getDefaultHomeStorage());
        }
        if (requested_storage != null) {
            effective_storage = requested_storage;
        } else {
            effective_storage = requested_alternative_uspace;
            KnownAction rka = (KnownAction) kadb.get(((AlternativeUspace) requested_alternative_uspace.getStorage()).getUspace());
            if (rka instanceof RKnownAction) other_incarnations.add(rka.getIncarnatedUser());
        }
        clearRequestsNU();
        gets_on_bss = false;
    }
}
