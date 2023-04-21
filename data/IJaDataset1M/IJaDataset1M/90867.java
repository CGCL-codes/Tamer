package pcgen.cdom.graph;

import java.util.Collection;
import java.util.List;
import pcgen.base.graph.core.DirectionalEdge;
import pcgen.cdom.base.AssociatedPrereqObject;
import pcgen.cdom.base.PrereqObject;
import pcgen.cdom.enumeration.AssociationKey;
import pcgen.cdom.enumeration.AssociationListKey;

public interface PCGraphEdge extends DirectionalEdge<PrereqObject>, AssociatedPrereqObject {

    String getSourceToken();

    public <T> void setAssociation(AssociationKey<T> name, T cost);

    public <T> T getAssociation(AssociationKey<T> name);

    public Collection<AssociationKey<?>> getAssociationKeys();

    public boolean hasAssociations();

    public PCGraphEdge createReplacementEdge(PrereqObject gn1, PrereqObject gn2);

    public <T> void addToAssociationList(AssociationListKey<T> name, T cost);

    public <T> List<T> getAssociationListFor(AssociationListKey<T> listKey);

    public Collection<AssociationListKey<?>> getAssociationListKeys();
}
