package reasoning;

import java.util.Collection;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.GeneralizationSet;
import org.eclipse.uml2.uml.Property;

public interface AdabtorTest {

    Collection<Class> getAllClasses();

    Collection<Association> getAllAssociation();

    Collection<GeneralizationSet> getAllGeneralizationSets();

    Collection<AssociationClass> getAllassociationClasses();

    Collection<Property> getMemberEnds();

    Collection<Class> getParents();

    Collection<Association> getAssociations();
}
