package de.sonivis.tool.core.datamodel.extension;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import javax.persistence.Entity;
import org.hibernate.annotations.Proxy;
import de.sonivis.tool.core.datamodel.Actor;
import de.sonivis.tool.core.datamodel.GraphComponent;
import de.sonivis.tool.core.datamodel.InfoSpace;
import de.sonivis.tool.core.datamodel.InfoSpaceItem;
import de.sonivis.tool.core.datamodel.InfoSpaceItemProperty;
import de.sonivis.tool.core.datamodel.exceptions.DataModelInstantiationException;
import de.sonivis.tool.core.datamodel.extension.proxy.IHuman;

/**
 * A human {@link Actor}.
 * 
 * @author Benedikt Meuthrath
 * @author Andreas Erber
 * @version $Revision: 1626 $, $Date: 2010-04-07 15:28:53 -0400 (Wed, 07 Apr 2010) $
 */
@Entity(name = "de.sonivis.tool.core.datamodel.extension.Human")
@Proxy(proxyClass = IHuman.class)
public class Human extends Actor implements IHuman {

    /**
	 * Default constructor.
	 * <p>
	 * Provided for persistence purposes. Do not use otherwise.
	 * </p>
	 */
    protected Human() {
    }

    /**
	 * Initialization constructor.
	 * 
	 * @param infoSpace
	 *            The {@link InfoSpace} this {@link Human} belongs to, may not be <code>null</code>.
	 * @param name
	 *            Textual identifier, may not be <code>null</code>.
	 * @param registered
	 *            The {@link Date} of registration.
	 * @throws DataModelInstantiationException
	 *             if {@link Actor#Actor(InfoSpace, String, Date)} does.
	 * @see Actor#Actor(InfoSpace, String, Date)
	 */
    public Human(final InfoSpace infoSpace, final String name, final Date registered) {
        super(infoSpace, name, registered);
        this.setType(Human.class);
    }

    /**
	 * Initialization constructor.
	 * 
	 * @param infoSpace
	 *            The {@link InfoSpace} this {@link Human} belongs to, must not be <code>null</code>
	 *            .
	 * @param externalId
	 *            Optional external identifier.
	 * @param name
	 *            Textual identifier, must not be <code>null</code>.
	 * @param registered
	 *            The {@link Date} of registration.
	 * @throws DataModelInstantiationException
	 *             {@link #Human(InfoSpace, String, Date)}
	 */
    public Human(final InfoSpace infoSpace, final Long externalId, final String name, final Date registered) {
        this(infoSpace, name, registered);
        this.setExternalId(externalId);
    }

    /**
	 * Initialization constructor.
	 * 
	 * @param infoSpace
	 *            The {@link InfoSpace} this {@link Human} belongs to, must not be <code>null</code>
	 *            .
	 * @param props
	 *            A {@link Collection} of {@link InfoSpaceItemProperty properties}.
	 * @param representations
	 *            A {@link Set} of {@link GraphComponent} entities representing this
	 *            {@link InfoSpaceItem} entity.
	 * @param externalId
	 *            Optional external identifier.
	 * @param name
	 *            Canonical identifier, must not be <code>null</code>.
	 * @param registered
	 *            The {@link Date} of registration.
	 * @throws DataModelInstantiationException
	 *             in case {@link #Human(InfoSpace, Long, String, Date)} does
	 * @see #Human(InfoSpace, Long, String, Date)
	 */
    public Human(final InfoSpace infoSpace, final Collection<InfoSpaceItemProperty<?>> props, final Set<GraphComponent> representations, final Long externalId, final String name, final Date registered) {
        this(infoSpace, externalId, name, registered);
        this.addProperties(props);
        this.addRepresentations(representations);
    }
}
