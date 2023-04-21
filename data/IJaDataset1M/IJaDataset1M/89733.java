package de.sonivis.tool.mwapiconnector.datamodel.extension;

import java.util.Collection;
import java.util.Set;
import javax.persistence.Entity;
import org.hibernate.annotations.Proxy;
import de.sonivis.tool.core.datamodel.GraphComponent;
import de.sonivis.tool.core.datamodel.InfoSpace;
import de.sonivis.tool.core.datamodel.InfoSpaceItem;
import de.sonivis.tool.core.datamodel.InfoSpaceItemProperty;
import de.sonivis.tool.core.datamodel.exceptions.DataModelInstantiationException;
import de.sonivis.tool.core.datamodel.extension.LinksTo;
import de.sonivis.tool.core.datamodel.extension.RevisionElement;
import de.sonivis.tool.core.datamodel.extension.proxy.IRevisionElement;
import de.sonivis.tool.core.datamodel.proxy.IContentElement;
import de.sonivis.tool.mwapiconnector.datamodel.extension.proxy.ITemplate;
import de.sonivis.tool.mwapiconnector.datamodel.extension.proxy.ITemplateLink;

/**
 * Defines a relationship between a {@link IRevisionElement} entity and a {@link ITemplate} entity.
 * <p>
 * The semantics of this class impose that the {@link IRevisionElement} entity uses (links to) the
 * {@link ITemplate} entity.
 * </p>
 * 
 * @param <REV>
 *            Sub-type of {@link IRevisionElement} representing a narrowed {@link RevisionElement}
 *            entity.
 * @param <TEMPL>
 *            Sub-type of {@link ITemplate} representing a narrowed {@link Template} entity.
 * @author Andreas Erber
 * @version $Revision: 1417 $, $Date: 2010-01-28 14:24:56 +0000 (Do, 28 Jan 2010) $
 */
@Entity(name = "de.sonivis.tool.mwapiconnector.datamodel.extension.TemplateLink")
@Proxy(proxyClass = ITemplateLink.class)
public class TemplateLink<REV extends IRevisionElement, TEMPL extends ITemplate> extends LinksTo<REV, TEMPL> implements ITemplateLink<REV, TEMPL> {

    /**
	 * Default constructor.
	 * <p>
	 * Provided for persistence purposes. Do not use otherwise.
	 * </p>
	 */
    protected TemplateLink() {
    }

    /**
	 * Initialization constructor.
	 * <p>
	 * All arguments must not be <code>null</code>. They are required by the
	 * {@link LinksTo#LinksTo(InfoSpace, IContentElement, IContentElement) super class constructor}.
	 * They cannot be modified thereafter.
	 * </p>
	 * 
	 * @param infoSpace
	 *            The {@link InfoSpace} this {@link InfoSpaceItem} entity belongs to, must not be
	 *            <code>null</code>.
	 * @param revision
	 *            Source {@link IRevisionElement} entity of this relation, must not be
	 *            <code>null</code>.
	 * @param template
	 *            Target {@link ITemplate} entity of this relation, must not be <code>null</code>.
	 * @throws DataModelInstantiationException
	 *             in case {@link LinksTo#LinksTo(InfoSpace, IContentElement, IContentElement) super
	 *             class constructor} does.
	 * @see LinksTo#LinksTo(InfoSpace, IContentElement, IContentElement)
	 */
    public TemplateLink(final InfoSpace infoSpace, final REV revision, final TEMPL template) {
        super(infoSpace, revision, template);
        this.setType(TemplateLink.class);
    }

    /**
	 * Initialization constructor.
	 * <p>
	 * All arguments must not be <code>null</code>. They are required by the
	 * {@link #TemplateLink(InfoSpace, IRevisionElement, ITemplate)}. They cannot be modified
	 * thereafter.
	 * </p>
	 * 
	 * @param infoSpace
	 *            The {@link InfoSpace} this {@link InfoSpaceItem} entity belongs to, must not be
	 *            <code>null</code>.
	 * @param props
	 *            A {@link Collection} of {@link InfoSpaceItemProperty properties}.
	 * @param representations
	 *            A {@link Set} of {@link GraphComponent} entities representing this
	 *            {@link InfoSpaceItem}.
	 * @param revision
	 *            Source {@link IRevisionElement} entity of this relation, must not be
	 *            <code>null</code>.
	 * @param template
	 *            Target {@link ITemplate} entity of this relation, must not be <code>null</code>.
	 * @throws DataModelInstantiationException
	 *             in case {@link #TemplateLink(InfoSpace, IRevisionElement, ITemplate)} does.
	 * @see #TemplateLink(InfoSpace, IRevisionElement, ITemplate)
	 */
    public TemplateLink(final InfoSpace infoSpace, final Collection<InfoSpaceItemProperty<?>> props, final Set<GraphComponent> representations, final REV revision, final TEMPL template) {
        this(infoSpace, revision, template);
        this.addProperties(props);
        this.addRepresentations(representations);
    }
}
