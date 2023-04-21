package uk.ac.ebi.rhea.mapper.util.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.LinkedHashSet;
import uk.ac.ebi.rhea.domain.Database;
import uk.ac.ebi.rhea.mapper.MapperException;
import uk.ac.ebi.rhea.mapper.util.CompoundSearchResult;
import uk.ac.ebi.rhea.mapper.util.ICompoundNameSearchStrategy;
import uk.ac.ebi.rhea.mapper.util.CompoundNameSearchException;

/**
 * This implementation of {@link ICompoundNameSearchStrategy} uses a database
 * connection to get ChEBI IDs from compound names.
 * <br>
 * Please note that:
 * <ul>
 * 	<li>searches are <b>case sensitive</b>.</li>
 * 	<li>no scores are returned.</li>
 * 	<li>the returned results are ordered according to the list of preferred
 * 		name sources (see {@link #NAME_SOURCES}) first, and then by status
 * 		(first checked - C -, then ok - E).</li>
 * </ul>
 * @author rafalcan
 *
 */
public class ChebiNameDbSearcher implements ICompoundNameSearchStrategy {

    /**
	 * Ordered list of preference for name sources. <code>ChEBI</code> means
	 * ChEBI name from the COMPOUNDS table (any source), the rest are values
	 * of the <code>CHEBI.NAMES.SOURCE</code> column.
	 */
    public static final String[] NAME_SOURCES = new String[] { "UniProt", "ChEBI", "IUPAC", "KEGG COMPOUND", "%" };

    private static final String SELECT_CHEBISRC_IDS = "SELECT id, name, status FROM compounds" + " WHERE name LIKE ? AND source LIKE ?" + " AND parent_id IS NULL AND status IN ({0})" + " ORDER BY status ASC";

    private static final String SELECT_OTHERSRC_IDS = "SELECT c.id, c.name, c.status FROM compounds c, names n" + " WHERE n.name LIKE ? AND n.source LIKE ? AND n.status in ({0})" + " AND c.status IN ({0}) AND n.compound_id = c.id" + " ORDER BY c.status ASC";

    private PreparedStatement[] searchNameStm, searchNameOnlyCheckedStm;

    private Connection connection;

    public ChebiNameDbSearcher(Connection connection) {
        this.connection = connection;
    }

    /**
     * @see #searchCompoundName(String, boolean, int)
     */
    public Collection<CompoundSearchResult> searchCompoundName(String name) throws CompoundNameSearchException {
        return searchCompoundName(name, false, 0);
    }

    /**
	 * @see #searchCompoundName(String, boolean, int)
	 */
    public Collection<CompoundSearchResult> searchCompoundName(String name, boolean onlyPublic) throws CompoundNameSearchException {
        return searchCompoundName(name, onlyPublic, 0);
    }

    /**
     * Searches ChEBI for a name using different queries until found.
	 * @param onlyPublic Search only checked compounds?
     * @param compoundNames
     * @param n index of the query string to be used @see {@link #searchNameStm})
     * @return A collection of ChEBI results matching the name,
     * 		or <code>null</code> if none found. The returned compounds
     * 		have no parents in ChEBI. They are ordered
     * 		according to the preferred name source (see {@link #NAMES_SOURCES}
     * 		for the list of preferences on name source), and then by status
     * 		(first checked - C - then ok - E).
     * @throws NameSearchException
     */
    private Collection<CompoundSearchResult> searchCompoundName(String compoundName, boolean onlyPublic, int n) throws CompoundNameSearchException {
        String name = compoundName.replace('*', '%').replace('?', '_');
        try {
            Collection<CompoundSearchResult> results = null;
            String originalCompoundName = null;
            StringBuilder errorMsg = new StringBuilder();
            if (originalCompoundName == null) originalCompoundName = name;
            PreparedStatement[] stm = getSearchNameStm(onlyPublic);
            stm[n].setString(1, name);
            stm[n].setString(2, NAME_SOURCES[n].equals("ChEBI") ? "%" : NAME_SOURCES[n]);
            ResultSet idsRs = stm[n].executeQuery();
            while (idsRs.next()) {
                try {
                    if (results == null) results = new LinkedHashSet<CompoundSearchResult>();
                    results.add(new CompoundSearchResult(Database.CHEBI.getName(), "CHEBI:" + idsRs.getString("id"), idsRs.getString("name"), idsRs.getString("status"), 0.0f));
                } catch (SQLException e) {
                    errorMsg.append("Database problem when retrieving data for '").append(originalCompoundName).append("'");
                } catch (IllegalArgumentException e) {
                    errorMsg.append("'").append(originalCompoundName).append("' appears more than once in ChEBI. ").append(e.getMessage());
                }
                if (errorMsg.length() > 0) {
                    idsRs.beforeFirst();
                    while (idsRs.next()) {
                        int compoundId = idsRs.getInt("id");
                        int parentId = idsRs.getInt("parent_id");
                        errorMsg.append("\n\tCHEBI:").append(compoundId);
                        if (parentId > 0) {
                            errorMsg.append(" (parent: CHEBI:").append(parentId).append(")");
                        }
                    }
                    throw new CompoundNameSearchException(errorMsg.toString());
                }
            }
            if (n < stm.length - 1) {
                Collection<CompoundSearchResult> moreResults = searchCompoundName(name, onlyPublic, n + 1);
                if (results == null) results = moreResults; else if (moreResults != null) results.addAll(moreResults);
            }
            return results;
        } catch (SQLException e) {
            throw new CompoundNameSearchException("Searching compound name", e);
        }
    }

    private PreparedStatement[] getSearchNameStm(boolean onlyChecked) throws SQLException {
        PreparedStatement[] stmts = onlyChecked ? searchNameOnlyCheckedStm : searchNameStm;
        if (stmts == null) {
            String wantedStatuses = onlyChecked ? "'C'" : "'C','E'";
            PreparedStatement chebiSrcStm = connection.prepareStatement(MessageFormat.format(SELECT_CHEBISRC_IDS, wantedStatuses), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            PreparedStatement otherSrcStm = connection.prepareStatement(MessageFormat.format(SELECT_OTHERSRC_IDS, wantedStatuses), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmts = new PreparedStatement[NAME_SOURCES.length];
            for (int i = 0; i < NAME_SOURCES.length; i++) {
                stmts[i] = NAME_SOURCES[i].equals("ChEBI") ? chebiSrcStm : otherSrcStm;
            }
            if (onlyChecked) searchNameOnlyCheckedStm = stmts; else searchNameStm = stmts;
        }
        return onlyChecked ? searchNameOnlyCheckedStm : searchNameStm;
    }

    /**
	 * Note that this method does not close the database connection passed to
	 * the constructor.
     * @throws MapperException
     */
    public void close() throws MapperException {
        if (searchNameStm != null) {
            for (int i = 0; i < searchNameStm.length; i++) {
                try {
                    searchNameStm[i].close();
                } catch (SQLException e) {
                    throw new MapperException("Closing", e);
                }
            }
        }
    }

    @Override
    protected void finalize() throws MapperException {
        close();
    }
}
