package org.hip.vif.bom;

import java.sql.SQLException;
import java.util.List;
import org.hip.kernel.bom.DomainObjectHome;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.exc.VException;
import org.hip.vif.bom.Text.ITextValues;

/**
 * The interface for the home of <code>Text</code> models.
 *
 * @author Luthiger
 * Created: 14.06.2010
 */
public interface TextHome extends DomainObjectHome {

    public static final String KEY_ID = "ID";

    public static final String KEY_VERSION = "Version";

    public static final String KEY_TITLE = "Title";

    public static final String KEY_AUTHOR = "Author";

    public static final String KEY_COAUTHORS = "CoAuthors";

    public static final String KEY_SUBTITLE = "Subtitle";

    public static final String KEY_YEAR = "Year";

    public static final String KEY_PUBLICATION = "Publication";

    public static final String KEY_PAGES = "Pages";

    public static final String KEY_VOLUME = "Volume";

    public static final String KEY_NUMBER = "Number";

    public static final String KEY_PUBLISHER = "Publisher";

    public static final String KEY_PLACE = "Place";

    public static final String KEY_REMARK = "Remark";

    public static final String KEY_REFERENCE = "Reference";

    public static final String KEY_TYPE = "Type";

    public static final String KEY_STATE = "State";

    public static final String KEY_FROM = "From";

    public static final String KEY_TO = "To";

    public static final String KEY_BIBLIO_TYPE = "biblioType";

    public static final String KEY_BIBLIOGRAPHY = "bibliography";

    /**
	 * Returns the published version of the text entry identified by the specified id.
	 * 
	 * @param inTextID String
	 * @return {@link Text}
	 * @throws VException
	 * @throws SQLException
	 */
    public Text getTextPublished(String inTextID) throws VException, SQLException;

    /**
	 * Tests for the existence of a published version of a bibliography entry with a specified ID.
	 * 
	 * @param inTextID String
	 * @return boolean <code>true</code> if there is a published version of the entry with the specified ID 
	 * @throws VException
	 * @throws SQLException
	 */
    public boolean hasPublishedVersion(String inTextID) throws VException, SQLException;

    /**
	 * Returns the text entry identified by the specified id.
	 * 
	 * @param inTextID String
	 * @param inVersion int
	 * @return {@link Text}
	 * @throws VException
	 * @throws SQLException
	 */
    public Text getText(String inTextID, int inVersion) throws VException, SQLException;

    /**
	 * Returns the text entry identified by the specified id-version string.
	 * 
	 * @param inIDVersion String the entry's id and version in a <code>String</code> of form '<code>ID-Version</code>'
	 * @return {@link Text}
	 * @throws VException
	 * @throws SQLException
	 */
    public Text getText(String inIDVersion) throws VException, SQLException;

    /**
	 * Returns the selection of published text entries that matches the string entered in the auto complete field.
	 * 
	 * @param inField String the name of the selection field
	 * @param inLookup String the search string entered in the auto complete field
	 * @return List<String>
	 * @throws VException
	 * @throws SQLException 
	 */
    public List<String> getAutoCompleteSelection(String inField, String inLookup) throws VException, SQLException;

    /**
	 * Returns a list of published entries
	 * 
	 * @param inTitle String
	 * @param inAuthor String
	 * @return {@link QueryResult}
	 * @throws VException 
	 * @throws SQLException 
	 */
    public QueryResult selectTitleOrAuthor(String inTitle, String inAuthor) throws VException, SQLException;

    /**
	 * Creates a new version of an existing entry using the specified values. 
	 * 
	 * @param inText {@link Text} the existing entry 
	 * @param inValues {@link ITextValues} new values
	 * @param inActorID Long
	 * @throws SQLException 
	 * @throws VException 
	 */
    public void createNewVersion(Text inText, ITextValues inValues, Long inActorID) throws VException, SQLException;

    /**
	 * Checks whether the specified reference is unique and, if not, create one being unique.
	 * 
	 * @param inReference String the reference to check
	 * @return String the unique reference
	 * @throws VException
	 * @throws SQLException
	 */
    public String checkReference(String inReference) throws VException, SQLException;
}
