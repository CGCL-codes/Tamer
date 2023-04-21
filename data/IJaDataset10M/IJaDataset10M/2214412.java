package de.sonivis.tool.core;

import de.sonivis.tool.core.datamodel.Actor;
import de.sonivis.tool.core.datamodel.ContentElement;
import de.sonivis.tool.core.datamodel.ContextRelation;
import de.sonivis.tool.core.datamodel.Graph;
import de.sonivis.tool.core.datamodel.GraphItem;
import de.sonivis.tool.core.datamodel.GraphItemProperty;
import de.sonivis.tool.core.datamodel.InfoSpaceItem;
import de.sonivis.tool.core.datamodel.InfoSpaceItemProperty;
import de.sonivis.tool.core.datamodel.Property;
import de.sonivis.tool.core.eventhandling.INetworkFilter;

/**
 * Predefined constants for names of {@link InfoSpaceItemProperty} and {@link GraphItemProperty}
 * entities.
 * <p>
 * This class bundles several constants to identify {@link Property} entities for bot,
 * {@link InfoSpaceItem}s and {@link GraphItem}s.
 * </p>
 * 
 * @author Andreas Erber
 * @version $Revision: 1626 $, $Date: 2010-04-07 15:28:53 -0400 (Wed, 07 Apr 2010) $
 */
public class PropertyConstants {

    /**
	 * {@link Property} name to indicate an <em>earliest</em> {@link ContextRelation} entity.
	 */
    public static final String FLAG_EARLIEST = "Earliest";

    /**
	 * {@link Property} name to indicate a <em>latest</em> {@link ContextRelation} entity.
	 */
    public static final String FLAG_LATEST = "Latest";

    /**
	 * {@link Property} name to indicate the <em>name of the user</em> that blocked an {@link Actor}
	 * entity.
	 */
    public static final String PROP_BLOCKED_BY = "BlockedBy";

    /**
	 * {@link Property} name to indicate the <em>reason for blocking</em> an {@link Actor} entity.
	 */
    public static final String PROP_BLOCK_REASON = "BlockReason";

    /**
	 * {@link Property} name to indicate an <em>URL</em> of an image description.
	 */
    public static final String PROP_DESC_URL = "DescriptionURL";

    /**
	 * {@link Property} name to indicate the <em>edit count</em> of an {@link Actor} entity.
	 */
    public static final String PROP_EDIT_COUNT = "EditCount";

    /**
	 * {@link Property} name to indicate the <em>existence</em> of a {@link ContextRelation} entity.
	 */
    public static final String PROP_EXISTS = "WikilinkExists";

    /**
	 * {@link Property} name to indicate an <em>external link</em>.
	 */
    public static final String PROP_EXTLINK = "ExternalLink";

    /**
	 * {@link Property} name to indicate the <em>filename</em> of an image.
	 */
    public static final String PROP_IMAGE = "ImageFileName";

    /**
	 * {@link Property} name to indicate a <em>language link</em>.
	 */
    public static final String PROP_LANGLINK = "LangLink";

    /**
	 * {@link Property} name to indicate a <em>minor edit</em> of a {@link ContentElement} entity.
	 */
    public static final String PROP_MINOR_EDIT = "MinorEdit";

    /**
	 * {@link Property} name to indicate a <em>comment</em> on a {@link ContentElement} entity.
	 */
    public static final String PROP_REV_COMMENT = "RevisionComment";

    /**
	 * {@link Property} name to indicate a <em>deleted</em> {@link ContentElement} entity.
	 */
    public static final String PROP_REV_DELETED = "Deleted";

    /**
	 * {@link Property} name to indicate a <em>reverted</em> {@link ContentElement} entity.
	 */
    public static final String PROP_REV_REVERTED = "Reverted";

    /**
	 * {@link Property} name to indicate a <em>rolled back</em> {@link ContentElement} entity.
	 */
    public static final String PROP_REV_ROLLBACK = "Rollback";

    /**
	 * {@link Property} name to indicate the content <em>size</em> of a {@link ContentElement}
	 * entity.
	 */
    public static final String PROP_REV_SIZE = "Size";

    /**
	 * {@link Property} name to indicate an <em>undone</em> {@link ContentElement} entity.
	 */
    public static final String PROP_REV_UNDONE = "Undone";

    /**
	 * {@link Property} name to indicate a <em>section title</em> of a {@link ContentElement}
	 * entity.
	 */
    public static final String PROP_SECTION = "SectionTitle";

    /**
	 * {@link Property} name to indicate a <em>group</em> an {@link Actor} entity belongs to.
	 */
    public static final String PROP_USER_GROUP = "Group";

    /**
	 * {@link Property} name to indicate an {@link INetworkFilter} of a {@link Graph}.
	 */
    public static final String PROP_NETWORK_FILTER = "NetworkFilter";

    /**
	 * Covered constructor.
	 */
    protected PropertyConstants() {
    }
}
