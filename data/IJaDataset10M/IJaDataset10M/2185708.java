package de.sonivis.tool.mwapiconnector.datamodel.extension;

import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.hibernate.annotations.Proxy;
import de.sonivis.tool.core.datamodel.ContentElement;
import de.sonivis.tool.core.datamodel.InfoSpace;
import de.sonivis.tool.core.datamodel.InfoSpaceItemProperty;
import de.sonivis.tool.core.datamodel.exceptions.DataModelInstantiationException;
import de.sonivis.tool.core.datamodel.extension.GroupingElement;
import de.sonivis.tool.mwapiconnector.datamodel.extension.proxy.IHelpPage;

/**
 * A specialized {@link Page} of namespace 12, a <em>help page</em>.
 * 
 * @author Andreas Erber
 * @version $Revision$, $Date$
 */
@Entity(name = "de.sonivis.tool.mwapiconnector.datamodel.extension.HelpPage")
@Proxy(proxyClass = IHelpPage.class)
public class HelpPage extends Page implements IHelpPage {

    /**
	 * Default constructor.
	 * <p>
	 * Provided for persistence purposes. Do not use otherwise.
	 * </p>
	 */
    protected HelpPage() {
    }

    /**
	 * Initialization constructor.
	 * 
	 * @param infoSpace
	 *            The {@link InfoSpace} this {@link HelpPage} entity belongs to, must not be
	 *            <code>null</code>.
	 * @param title
	 *            Canonical identifier.
	 * @throws DataModelInstantiationException
	 *             if {@link Page#PageElement(InfoSpace, String) super class constructor} does.
	 * @see Page#PageElement(InfoSpace, String)
	 */
    public HelpPage(final InfoSpace infoSpace, final String title) {
        super(infoSpace, title);
        this.setType(HelpPage.class);
    }

    /**
	 * Initialization constructor.
	 * 
	 * @param infoSpace
	 *            The {@link InfoSpace} this {@link HelpPage} entity belongs to, must not be
	 *            <code>null</code>.
	 * @param externalId
	 *            External numerical identifier.
	 * @param title
	 *            Canonical identifier.
	 * @throws DataModelInstantiationException
	 *             if {@link #HelpPage(InfoSpace, String)} does
	 * @see #HelpPage(InfoSpace, String)
	 */
    public HelpPage(final InfoSpace infoSpace, final Long externalId, final String title) {
        this(infoSpace, title);
        this.setExternalId(externalId);
    }

    /**
	 * Initialization constructor.
	 * 
	 * @param infoSpace
	 *            The {@link InfoSpace} this {@link HelpPage} entity belongs to, must not be
	 *            <code>null</code>.
	 * @param title
	 *            Canonical identifier.
	 * @param props
	 *            Optional {@link Collection} of {@link InfoSpaceItemProperty properties}.
	 * @throws DataModelInstantiationException
	 *             if {@link #HelpPage(InfoSpace, String)} does.
	 * @see #HelpPage(InfoSpace, String)
	 */
    public HelpPage(final InfoSpace infoSpace, final String title, final Collection<InfoSpaceItemProperty<?>> props) {
        this(infoSpace, title);
        this.addProperties(props);
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return this.getTitle();
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @throws UnsupportedOperationException
	 *             any time called.
	 * @see ContentElement#setText(String)
	 */
    @Override
    public void setText(final String text) {
        throw new UnsupportedOperationException("Entities of this semantic level have no textual content. Use an associated RevisionElement to store content.");
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see GroupingElement#getLabel()
	 */
    @Transient
    @Override
    public String getLabel() {
        return "HelpPage";
    }
}
