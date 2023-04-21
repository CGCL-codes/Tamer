package au.edu.uq.itee.maenad.pronto.owl.entrytypes;

import au.edu.uq.itee.maenad.pronto.owl.EntityRenderer;
import au.edu.uq.itee.maenad.pronto.owl.EntryType;
import org.semanticweb.owl.model.OWLObject;

public class DefaultEntryType implements EntryType<OWLObject> {

    public String getSectionName() {
        return "Other Entries";
    }

    public boolean coversObject(OWLObject object) {
        return true;
    }

    public String renderObject(OWLObject object, EntityRenderer entityRenderer) {
        return object.toString();
    }
}
