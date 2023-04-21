package name.levering.ryan.sparql.logic.streamed;

import java.util.Collection;
import name.levering.ryan.sparql.common.RdfBindingSet;

public interface StackedRdfBindingSet extends RdfBindingSet {

    public Collection getDependencies();

    public void accept(StreamedBindingSetVisitor visitor);
}
