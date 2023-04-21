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
import de.sonivis.tool.mwapiconnector.datamodel.extension.proxy.ICategory;

/**
 * Special {@link Page} to represent a Category.
 * <p>
 * From the view of the <a href="http://www.sonivis.org/wiki/index.php/Data_Model"
 * target="_blank">SONIVIS data model</a> this is just some {@link ContentElement}. For the
 * SONIVIS:Tool software categories are special in a sense to be able to group other entities.
 * </p>
 * 
 * @author Benedikt
 * @author Andreas Erber
 * @version $Revision: 1417 $, $Date: 2010-01-28 14:24:56 +0000 (Do, 28 Jan 2010) $
 */
@Entity(name = "de.sonivis.tool.mwapiconnector.datamodel.extension.Category")
@Proxy(proxyClass = ICategory.class)
public class Category extends GroupingElement implements ICategory {

    /**
	 * Default constructor.
	 * <p>
	 * Provided for persistence purposes. Do not use otherwise.
	 * </p>
	 */
    protected Category() {
    }

    /**
	 * Initialization constructor.
	 * 
	 * @param infoSpace
	 *            The {@link InfoSpace} this {@link Category} belongs to, must not be
	 *            <code>null</code>.
	 * @param title
	 *            Canonical identifier.
	 * @throws DataModelInstantiationException
	 *             if {@link Page#PageElement(InfoSpace, String) super class constructor} does.
	 * @see Page#PageElement(InfoSpace, String)
	 */
    public Category(final InfoSpace infoSpace, final String title) {
        super(infoSpace, title);
        this.setType(Category.class);
    }

    /**
	 * Initialization constructor.
	 * 
	 * @param infoSpace
	 *            The {@link InfoSpace} this {@link Category} belongs to, must not be
	 *            <code>null</code>.
	 * @param externalId
	 *            External numerical identifier.
	 * @param title
	 *            Canonical identifier.
	 * @throws DataModelInstantiationException
	 *             if {@link #Category(InfoSpace, String)} does.
	 * @see #Category(InfoSpace, String)
	 */
    public Category(final InfoSpace infoSpace, final Long externalId, final String title) {
        this(infoSpace, title);
        this.setExternalId(externalId);
    }

    /**
	 * Initialization constructor.
	 * 
	 * @param infoSpace
	 *            The {@link InfoSpace} this {@link Category} belongs to, must not be
	 *            <code>null</code>.
	 * @param title
	 *            Canonical identifier.
	 * @param props
	 *            {@link Collection} of {@link InfoSpaceItemProperty} entities.
	 *@throws DataModelInstantiationException
	 *             if {@link #Category(InfoSpace, String)} does.
	 * @see #Category(InfoSpace, String)
	 */
    public Category(final InfoSpace infoSpace, final String title, final Collection<InfoSpaceItemProperty<?>> props) {
        this(infoSpace, title);
        this.addProperties(props);
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see Object#toString()
	 */
    @Override
    public String toString() {
        return this.getTitle();
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see GroupingElement#getLabel()
	 */
    @Transient
    @Override
    public String getLabel() {
        return "Category";
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime * this.getInfoSpace().hashCode();
        result = prime * result + ((this.getTitle() == null) ? 0 : this.getTitle().hashCode());
        return result;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see Object#equals(Object)
	 */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Category)) return false;
        final Category other = (Category) obj;
        if (this.getTitle() == null) {
            if (other.getTitle() != null) return false;
        } else if (!this.getTitle().equals(other.getTitle())) return false;
        if (infoSpace == null) {
            if (other.infoSpace != null) return false;
        } else if (!infoSpace.equals(other.getInfoSpace())) return false;
        return true;
    }
}
