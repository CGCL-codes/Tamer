package uk.ac.lkl.common.util.database.io;

import java.util.List;
import uk.ac.lkl.common.util.database.NamedDefinition;

public abstract class ParentDefinitionInitialiser<P extends NamedDefinition, C extends NamedDefinition> extends NamedDefinitionInitialiser<P> {

    public abstract List<C> getChildDefinitions();
}
