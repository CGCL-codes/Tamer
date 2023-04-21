package de.sonivis.tool.core.tests.datamodel.hibernate;

import java.util.Collection;
import java.util.Set;
import javax.persistence.Entity;
import org.hibernate.annotations.Proxy;
import de.sonivis.tool.core.datamodel.GraphComponent;
import de.sonivis.tool.core.datamodel.InfoSpace;
import de.sonivis.tool.core.datamodel.InfoSpaceItemProperty;
import de.sonivis.tool.core.datamodel.InteractionRelation;
import de.sonivis.tool.core.datamodel.proxy.IActor;

/**
 * Dummy implementation for an {@link InteractionRelation} for testing purposes.
 * 
 * @author Andreas Erber
 * @version $Revision$, $Date$
 */
@Entity(name = "de.sonivis.tool.core.tests.datamodel.hibernate.InteractionRelationTestImpl")
@Proxy(proxyClass = IInteractionRelationTestImpl.class)
public class InteractionRelationTestImpl<S extends IActor, T extends IActor> extends InteractionRelation<S, T> implements IInteractionRelationTestImpl<S, T> {

    public InteractionRelationTestImpl() {
        super();
    }

    public InteractionRelationTestImpl(final InfoSpace infoSpace, final S source, final T target, final Collection<InfoSpaceItemProperty<?>> props, final Set<GraphComponent> representations) {
        super(infoSpace, source, target, props, representations);
    }

    public InteractionRelationTestImpl(final InfoSpace infoSpace, final S source, final T target, final Collection<InfoSpaceItemProperty<?>> props) {
        super(infoSpace, source, target, props);
    }

    public InteractionRelationTestImpl(final InfoSpace infoSpace, final S source, final T target) {
        super(infoSpace, source, target);
    }
}
