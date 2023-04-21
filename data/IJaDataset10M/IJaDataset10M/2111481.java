package org.hip.vif.bom;

import java.sql.SQLException;
import org.hip.kernel.bom.DomainObjectHome;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.exc.VException;

/**
 * Interface of home for models to download files.
 *
 * @author Luthiger
 * Created: 19.09.2010
 */
public interface DownloadTextHome extends DomainObjectHome {

    public static final String KEY_ID = "ID";

    public static final String KEY_LABEL = "Label";

    public static final String KEY_UUID = "UUID";

    public static final String KEY_MIME = "Mime";

    public static final String KEY_DOCTYPE = "DocType";

    public static final String KEY_TEXTID = "TextID";

    public static final String KEY_MEMBERID = "MemberID";

    /**
	 * Returns all download entries belonging to the text entry with the specified key.
	 * 
	 * @param inTextID String text entry ID
	 * @return QueryResult
	 * @throws VException
	 * @throws SQLException 
	 */
    public QueryResult getDownloads(String inTextID) throws VException, SQLException;

    /**
	 * Returns the download entry for the specified ID.
	 * 
	 * @param inDownloadID String the entry's ID
	 * @return DownloadText
	 * @throws VException 
	 */
    public DownloadText getDownload(String inDownloadID) throws VException;

    /**
	 * Deletes the entry with the specified ID.
	 * 
	 * @param inDownloadID String the entry's ID
	 * @throws VException 
	 * @throws SQLException 
	 */
    public void deleteDownload(String inDownloadID) throws VException, SQLException;
}
