package de.sonivis.tool.mwapiconnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.sonivis.tool.core.datamodel.InfoSpace;
import de.sonivis.tool.core.datamodel.exceptions.CurrentlyNotSupportedException;
import de.sonivis.tool.core.datamodel.exceptions.DataModelInstantiationException;
import de.sonivis.tool.mwapiconnector.exception.MWApiQueryInstantiationException;

/**
 * Parent class for all <a href="http://www.mediawiki.org/wiki/API" target="_blank">MediaWiki
 * API</a> list queries.
 * 
 * @author Andreas Erber
 * @version $Revision: 1626 $, $Date: 2010-04-07 15:28:53 -0400 (Wed, 07 Apr 2010) $
 */
public abstract class AbstractListQuery extends AbstractMWApiQuery {

    /**
	 * Logger.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractListQuery.class);

    /**
	 * Which kind of Wiki API list query.
	 * 
	 * @see #ALLPAGES
	 * @see #ALLCATEGORIES
	 * @see #ALLUSERS
	 * @see #ALLLINKS
	 * @see #ALLIMAGES ...
	 */
    private int queryKind = 0;

    /**
	 * Use this constant in the context of queries for a list of pages (value: * * {@value} ).
	 */
    public static final int ALLPAGES = 1 << 1;

    /**
	 * Use this constant in the context of queries for a list of links (value: * * {@value} ).
	 */
    public static final int ALLLINKS = 1 << 2;

    /**
	 * Use this constant in the context of queries for a list of categories (value: {@value} ).
	 */
    public static final int ALLCATEGORIES = 1 << 3;

    /**
	 * Use this constant in the context of queries for a list of users (value: * * {@value} ).
	 */
    public static final int ALLUSERS = 1 << 4;

    /**
	 * Use this constant in the context of queries for a list of images (value: * {@value} ).
	 */
    public static final int ALLIMAGES = 1 << 5;

    /**
	 * Use this constant in the context of queries for a list of backlinks (value: {@value} ).
	 */
    public static final int BACKLINKS = 1 << 6;

    /**
	 * Use this constant in the context of queries for a list of blocks (value: * {@value} ).
	 */
    public static final int BLOCKS = 1 << 7;

    /**
	 * Use this constant in the context of queries for a list of category members (value: {@value}
	 * ).
	 */
    public static final int CATEGORYMEMBERS = 1 << 8;

    /**
	 * Use this constant in the context of queries for a list of embedded elements (value: {@value}
	 * ).
	 */
    public static final int EMBEDDEDIN = 1 << 9;

    /**
	 * Use this constant in the context of queries for a list of pages linking to a certain URL
	 * (value: {@value} ).
	 */
    public static final int EXTURLUSAGE = 1 << 10;

    /**
	 * Use this constant in the context of queries for a list of pages that contain a certain image
	 * (value: {@value} ).
	 */
    public static final int IMAGEUSAGE = 1 << 11;

    /**
	 * Use this constant in the context of queries for a list of log events (value: {@value} ).
	 */
    public static final int LOGEVENTS = 1 << 12;

    /**
	 * Use this constant in the context of queries for a list of recent changes (value: {@value} ).
	 */
    public static final int RECENTCHANGES = 1 << 13;

    /**
	 * Use this constant in the context of queries for a search for a certain string (value:
	 * {@value} ).
	 */
    public static final int SEARCH = 1 << 14;

    /**
	 * Use this constant in the context of queries for a list of user contributions (value: {@value}
	 * ).
	 */
    public static final int USERCONTRIBS = 1 << 15;

    /**
	 * Use this constant in the context of queries for the watchlist of a user (value: {@value} ).
	 */
    public static final int WATCHLIST = 1 << 16;

    /**
	 * Use this constant in the context of queries for a list of deleted revisions (value: {@value}
	 * ).
	 */
    public static final int DELETEDREVS = 1 << 17;

    /**
	 * Use this constant in the context of queries for a list of users (value: * * {@value} ).
	 */
    public static final int USERS = 1 << 18;

    /**
	 * Use this constant in the context of queries for a list of random pages (value: {@value} ).
	 */
    public static final int RANDOM = 1 << 19;

    /**
	 * Constructor
	 * <p>
	 * The argument has to be one of the following constants:
	 * </p>
	 * <ul>
	 * <li>{@link AbstractListQuery#ALLPAGES}</li>
	 * <li>{@link AbstractListQuery#ALLLINKS}</li>
	 * <li>{@link AbstractListQuery#ALLCATEGORIES}</li>
	 * <li>{@link AbstractListQuery#ALLUSERS}</li>
	 * <li>{@link AbstractListQuery#ALLIMAGES}</li>
	 * <li>{@link AbstractListQuery#BACKLINKS}</li>
	 * <li>{@link AbstractListQuery#BLOCKS}</li>
	 * <li>{@link AbstractListQuery#CATEGORYMEMBERS}</li>
	 * <li>{@link AbstractListQuery#EMBEDDEDIN}</li>
	 * <li>{@link AbstractListQuery#EXTURLUSAGE}</li>
	 * <li>{@link AbstractListQuery#IMAGEUSAGE}</li>
	 * <li>{@link AbstractListQuery#LOGEVENTS}</li>
	 * <li>{@link AbstractListQuery#RECENTCHANGES}</li>
	 * <li>{@link AbstractListQuery#SEARCH}</li>
	 * <li>{@link AbstractListQuery#USERCONTRIBS}</li>
	 * <li>{@link AbstractListQuery#WATCHLIST}</li>
	 * <li>{@link AbstractListQuery#DELETEDREVS}</li>
	 * <li>{@link AbstractListQuery#USERS}</li>
	 * <li>{@link AbstractListQuery#RANDOM}</li>
	 * </ul>
	 * 
	 * @param is
	 *            The {@link InfoSpace} this query operates on.
	 * @param queryKind
	 *            Identifier of query.
	 * @throws DataModelInstantiationException
	 *             if specified <em>queryKind</em> does not match any of the constants noted above.
	 * @throws CurrentlyNotSupportedException
	 *             if specified <em>queryKind</em> does match any of the constants noted above but
	 *             the operation is not yet supported.
	 */
    public AbstractListQuery(final InfoSpace is, final int queryKind) {
        super(is);
        if (queryKind == AbstractListQuery.ALLPAGES) {
            this.setQuery(this.getQuery() + "query&list=allpages");
            LOGGER.debug("Created base string for allpages query.");
        } else if (queryKind == AbstractListQuery.ALLLINKS) {
            this.setQuery(this.getQuery() + "query&list=alllinks");
            LOGGER.debug("Created base string for alllinks query.");
        } else if (queryKind == AbstractListQuery.ALLCATEGORIES) {
            this.setQuery(this.getQuery() + "query&generator=allcategories");
            LOGGER.debug("Created base string for allcategories query.");
        } else if (queryKind == AbstractListQuery.ALLUSERS) {
            this.setQuery(this.getQuery() + "query&list=allusers");
            LOGGER.debug("Created base string for allusers query.");
        } else if (queryKind == AbstractListQuery.ALLIMAGES) {
            this.setQuery(this.getQuery() + "query&list=allimages");
            LOGGER.debug("Created base string for allimages query.");
        } else if (queryKind == AbstractListQuery.BACKLINKS) {
            this.setQuery(this.getQuery() + "query&list=backlinks");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (queryKind == AbstractListQuery.BLOCKS) {
            this.setQuery(this.getQuery() + "query&list=blocks");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (queryKind == AbstractListQuery.CATEGORYMEMBERS) {
            this.setQuery(this.getQuery() + "query&list=categorymembers");
            LOGGER.debug("Created base string for categorymembers query.");
        } else if (queryKind == AbstractListQuery.EMBEDDEDIN) {
            this.setQuery(this.getQuery() + "query&list=embeddedin");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (queryKind == AbstractListQuery.EXTURLUSAGE) {
            this.setQuery(this.getQuery() + "query&list=extrurlusage");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (queryKind == AbstractListQuery.IMAGEUSAGE) {
            this.setQuery(this.getQuery() + "query&list=imageusage");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (queryKind == AbstractListQuery.LOGEVENTS) {
            this.setQuery(this.getQuery() + "query&list=logevents");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (queryKind == AbstractListQuery.RECENTCHANGES) {
            this.setQuery(this.getQuery() + "query&list=recentchanges");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (queryKind == AbstractListQuery.SEARCH) {
            this.setQuery(this.getQuery() + "query&list=search");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (queryKind == AbstractListQuery.USERCONTRIBS) {
            this.setQuery(this.getQuery() + "query&list=usercontribs");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (queryKind == AbstractListQuery.WATCHLIST) {
            this.setQuery(this.getQuery() + "query&list=watchlist");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (queryKind == AbstractListQuery.DELETEDREVS) {
            this.setQuery(this.getQuery() + "query&list=deletedrevs");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (queryKind == AbstractListQuery.USERS) {
            this.setQuery(this.getQuery() + "query&list=users");
        } else if (queryKind == AbstractListQuery.RANDOM) {
            this.setQuery(this.getQuery() + "query&list=random");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else {
            throw new MWApiQueryInstantiationException("Invalid parameter value provided for queryKind");
        }
        this.queryKind = queryKind;
    }

    /**
	 * Reset the query string to its initial default.
	 * 
	 * @throws CurrentlyNotSupportedException
	 *             if the specified <em>queryKind</em> argument for the constructor does match a
	 *             valid value but the operation is not yet supported.
	 */
    @Override
    public final void resetQuery() {
        super.resetQuery();
        if (this.queryKind == AbstractListQuery.ALLPAGES) {
            this.setQuery(this.getQuery() + "query&list=allpages");
        } else if (this.queryKind == AbstractListQuery.ALLLINKS) {
            this.setQuery(this.getQuery() + "query&list=alllinks");
        } else if (this.queryKind == AbstractListQuery.ALLCATEGORIES) {
            this.setQuery(this.getQuery() + "query&generator=allcategories");
        } else if (this.queryKind == AbstractListQuery.ALLUSERS) {
            this.setQuery(this.getQuery() + "query&list=allusers");
        } else if (this.queryKind == AbstractListQuery.ALLIMAGES) {
            this.setQuery(this.getQuery() + "query&list=allimages");
        } else if (this.queryKind == AbstractListQuery.BACKLINKS) {
            this.setQuery(this.getQuery() + "query&list=backlinks");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (this.queryKind == AbstractListQuery.BLOCKS) {
            this.setQuery(this.getQuery() + "query&list=blocks");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (this.queryKind == AbstractListQuery.CATEGORYMEMBERS) {
            this.setQuery(this.getQuery() + "query&list=categorymembers");
        } else if (this.queryKind == AbstractListQuery.EMBEDDEDIN) {
            this.setQuery(this.getQuery() + "query&list=embeddedin");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (this.queryKind == AbstractListQuery.EXTURLUSAGE) {
            this.setQuery(this.getQuery() + "query&list=extrurlusage");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (this.queryKind == AbstractListQuery.IMAGEUSAGE) {
            this.setQuery(this.getQuery() + "query&list=imageusage");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (this.queryKind == AbstractListQuery.LOGEVENTS) {
            this.setQuery(this.getQuery() + "query&list=logevents");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (this.queryKind == AbstractListQuery.RECENTCHANGES) {
            this.setQuery(this.getQuery() + "query&list=recentchanges");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (this.queryKind == AbstractListQuery.SEARCH) {
            this.setQuery(this.getQuery() + "query&list=search");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (this.queryKind == AbstractListQuery.USERCONTRIBS) {
            this.setQuery(this.getQuery() + "query&list=usercontribs");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (this.queryKind == AbstractListQuery.WATCHLIST) {
            this.setQuery(this.getQuery() + "query&list=watchlist");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (this.queryKind == AbstractListQuery.DELETEDREVS) {
            this.setQuery(this.getQuery() + "query&list=deletedrevs");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        } else if (this.queryKind == AbstractListQuery.USERS) {
            this.setQuery(this.getQuery() + "query&list=users");
        } else if (this.queryKind == AbstractListQuery.RANDOM) {
            this.setQuery(this.getQuery() + "query&list=random");
            throw new CurrentlyNotSupportedException("Valid parameter value but operation not yet supported.");
        }
    }

    /**
	 * The query's identification string.
	 * 
	 * @return query kind string
	 */
    public final int getQueryKind() {
        return this.queryKind;
    }
}
