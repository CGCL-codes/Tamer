package it.webscience.kpeople.dal.cross;

import it.webscience.kpeople.be.Keyword;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author depascalis
 * Factory del bean Keyword.
 */
public class KeywordFactory {

    /**
     * @param rs ResultSet.
     * @return oggetto Keyword.
     * @throws SQLException eccezione db.
     */
    public static Keyword createKeyword(final ResultSet rs) throws SQLException {
        Keyword keyword = new Keyword();
        keyword.setIdKeyword(rs.getInt("ID_KEYWORD"));
        keyword.setKeyword(rs.getString("KEYWORD"));
        keyword.setDescription(rs.getString("DESCRIPTION"));
        keyword.setHpmKeywordId(rs.getString("HPM_KEYWORD_ID"));
        keyword.setValue(rs.getString("VALUE"));
        return keyword;
    }
}
