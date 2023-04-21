package de.sonivis.tool.core.datamodel.networkloader;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import de.sonivis.tool.core.datamodel.ContentElement;
import de.sonivis.tool.core.datamodel.Structure;
import de.sonivis.tool.core.datamodel.extension.CategoryElement;
import de.sonivis.tool.core.datamodel.extension.PageElement;
import de.sonivis.tool.core.datamodel.extension.RevisionElement;

/**
 * This class represents an interaction property. The following information of a
 * {@link ContentElement} of sub-type {@link RevisionElement} are saved: -
 * Content Element ID - Date of Creation - corresponding {@link PageElement} ID
 * - {@link Structure} of the corresponding {@link PageElement} - set of
 * corresponding {@link CategoryElement} IDs
 * 
 * @author Janette Lehmann
 * @version $Revision: 1288 $, $Date: 2009-11-30 17:16:45 -0500 (Mon, 30 Nov 2009) $
 */
public class InteractionProperty implements Serializable {

    /**
	 * Default serial ID.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * ID of a {@link ContentElement} entity of sub-type {@link RevisionElement}
	 * .
	 */
    private Long id;

    /**
	 * Creation date of the content element
	 */
    private Date created;

    /**
	 * ID of the corresponding content element
	 */
    private Long pageID;

    /**
	 * {@link Structure} of the content element
	 */
    private Structure structure;

    /**
	 * IDs of the corresponding content elements
	 */
    private HashSet<Long> categories;

    /**
	 * IDs of the corresponding content elements
	 */
    private HashSet<Long> templates;

    /**
	 * Default Constructor
	 */
    public InteractionProperty() {
    }

    /**
	 * Initialization constructor.
	 * 
	 * @param id
	 *            {@link Long} the content element ID
	 * @param created
	 *            {@link Date} the creation date of the content element
	 * @param pageID
	 *            {@link Long} the id of the corresponding page
	 * @param structure
	 *            {@link Structure} of the content element
	 * @param categories
	 *            Set of {@link Long}s for the corresponding categories
	 * @param templates
	 *            Set of {@link Long}s for the corresponding templates
	 */
    public InteractionProperty(final Long id, final Date created, final Long pageID, final Structure structure, final HashSet<Long> categories, final HashSet<Long> templates) {
        this.id = id;
        this.created = created;
        this.pageID = pageID;
        this.structure = structure;
        this.categories = categories;
        this.templates = templates;
    }

    /**
	 * Returns the id of this content element.
	 * 
	 * @return {@link Long}
	 */
    public final Long getID() {
        return this.id;
    }

    /**
	 * Returns the creation date of this content element.
	 * 
	 * @return {@link Date}
	 */
    public final Date getCreated() {
        return this.created;
    }

    /**
	 * Returns the ID of the corresponding {@link ContentElement} entity of
	 * sub-type {@link PageElement}.
	 * 
	 * @return {@link Long}
	 */
    public final Long getPageID() {
        return this.pageID;
    }

    /**
	 * Returns the {@link Structure} of the {@link ContentElement} entity of
	 * sub-type {@link PageElement}.
	 * 
	 * @return {@link Structure}
	 */
    public final Structure getStructure() {
        return this.structure;
    }

    /**
	 * Returns the IDs of the corresponding {@link ContentElement} entities of
	 * sub-type {@link CategoryElement}.
	 * 
	 * @return {@link HashSet}
	 */
    public final HashSet<Long> getCategories() {
        return this.categories;
    }

    /**
	 * Returns the IDs of the corresponding {@link ContentElement} entity of
	 * sub-type {@link TemplateElement}.
	 * 
	 * @return {@link HashSet}
	 */
    public final HashSet<Long> getTemplates() {
        return this.templates;
    }

    /**
	 * Sets the id of this content element.
	 * 
	 * @param id
	 */
    public final void setID(final Long id) {
        this.id = id;
    }

    /**
	 * Sets the creation date of this content element.
	 * 
	 * @param creation
	 *            date
	 */
    public final void setCreated(final Date created) {
        this.created = created;
    }

    /**
	 * Sets the ID of the corresponding {@link ContentElement} entity of
	 * sub-type {@link PageElement}.
	 * 
	 * @param page
	 *            ID
	 */
    public final void setPageID(final Long pageID) {
        this.pageID = pageID;
    }

    /**
	 * Sets the {@link Structure} of the {@link ContentElement} entity of
	 * sub-type {@link PageElement}.
	 * 
	 * @param structure
	 */
    public final void setStructure(final Structure structure) {
        this.structure = structure;
    }

    /**
	 * Sets the IDs of the corresponding {@link ContentElement} entities of
	 * sub-type {@link CategoryElement}.
	 * 
	 * @param categories
	 */
    public final void setCategories(final HashSet<Long> categories) {
        this.categories = categories;
    }

    /**
	 * Sets the IDs of the corresponding {@link ContentElement} entity of
	 * sub-type {@link TemplateElement}.
	 * 
	 * @param categories
	 */
    public final void setTemplates(final HashSet<Long> templates) {
        this.templates = templates;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.created == null) ? 0 : this.created.hashCode());
        result = prime * result + ((this.pageID == null) ? 0 : this.pageID.hashCode());
        result = prime * result + ((this.structure == null) ? 0 : this.structure.hashCode());
        result = prime * result + ((this.categories == null) ? 0 : this.categories.hashCode());
        result = prime * result + ((this.templates == null) ? 0 : this.templates.hashCode());
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final InteractionProperty other = (InteractionProperty) obj;
        if (!this.id.equals(other.id)) {
            return false;
        }
        if (!this.created.equals(other.created)) {
            return false;
        }
        if (!this.pageID.equals(other.pageID)) {
            return false;
        }
        if (!this.structure.equals(other.structure)) {
            return false;
        }
        if (this.categories.size() != other.categories.size()) {
            return false;
        } else {
            for (final Iterator<Long> i = this.categories.iterator(); i.hasNext(); ) {
                if (!other.categories.contains(i.next())) {
                    return false;
                }
            }
        }
        if (this.templates.size() != other.templates.size()) {
            return false;
        } else {
            for (final Iterator<Long> i = this.templates.iterator(); i.hasNext(); ) {
                if (!other.templates.contains(i.next())) {
                    return false;
                }
            }
        }
        return true;
    }
}
