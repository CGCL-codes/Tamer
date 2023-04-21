package au.edu.uq.itee.maenad.pronto.owl;

import java.util.Arrays;
import java.util.List;
import org.semanticweb.owl.model.OWLObject;
import au.edu.uq.itee.maenad.pronto.owl.entrytypes.ClassDefinitionEntryType;
import au.edu.uq.itee.maenad.pronto.owl.entrytypes.DataPropertyDefinitionEntryType;
import au.edu.uq.itee.maenad.pronto.owl.entrytypes.DefaultEntryType;
import au.edu.uq.itee.maenad.pronto.owl.entrytypes.DisjointClassesEntryType;
import au.edu.uq.itee.maenad.pronto.owl.entrytypes.EntityAnnotationEntryType;
import au.edu.uq.itee.maenad.pronto.owl.entrytypes.IndividualDefinitionEntryType;
import au.edu.uq.itee.maenad.pronto.owl.entrytypes.ObjectPropertyDefinitionEntryType;
import au.edu.uq.itee.maenad.pronto.owl.entrytypes.PropertyDomainEntryType;
import au.edu.uq.itee.maenad.pronto.owl.entrytypes.PropertyRangeEntryType;
import au.edu.uq.itee.maenad.pronto.owl.entrytypes.SubClassEntryType;

public interface EntryType<O extends OWLObject> {

    static final List<EntryType<? extends OWLObject>> KNOWN_ENTRY_TYPES = Arrays.asList(new ClassDefinitionEntryType(), new ObjectPropertyDefinitionEntryType(), new DataPropertyDefinitionEntryType(), new IndividualDefinitionEntryType(), new SubClassEntryType(), new DisjointClassesEntryType(), new PropertyDomainEntryType(), new PropertyRangeEntryType(), new EntityAnnotationEntryType(), new DefaultEntryType());

    String getSectionName();

    boolean coversObject(OWLObject object);

    String renderObject(O object, EntityRenderer entityRenderer);
}
