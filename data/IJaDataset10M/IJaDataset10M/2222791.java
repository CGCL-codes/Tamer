package pub.db.annotation_task;

import pub.db.CachedLookupTable;
import pub.db.PubConnection;
import java.sql.*;
import java.util.*;

public class GenesWithHits {

    private CachedLookupTable sourceToType;

    private CachedLookupTable articleToSource;

    private CachedLookupTable termToGene;

    List candidateGenes;

    public GenesWithHits(PubConnection conn) {
        try {
            initializeLookupTables(conn);
            initializeCandidateList(conn);
        } catch (SQLException e) {
            throw new pub.db.RollbackException(e);
        }
    }

    private void initializeLookupTables(PubConnection conn) throws SQLException {
        sourceToType = new CachedLookupTable(conn, "SELECT id, type " + " FROM pub_source WHERE is_obsolete='n'");
        articleToSource = new CachedLookupTable(conn, "SELECT id, pub_source_id " + " FROM pub_article WHERE is_obsolete='n'");
        termToGene = new CachedLookupTable(conn, "SELECT id, pub_gene_id " + " FROM pub_term WHERE pub_gene_id is not null and is_obsolete='n'");
    }

    private void initializeCandidateList(PubConnection conn) throws SQLException {
        Set candidateGenesSet = new HashSet();
        PreparedStatement stmt = conn.prepareStatement("select pub_term_id, pub_article_id from pub_hit where " + " is_obsolete='n' and (is_valid = 'y' or is_valid is null)");
        try {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String term_id = rs.getString("pub_term_id");
                String article_id = rs.getString("pub_article_id");
                String gene_id = termToGene.get(term_id);
                if (gene_id == null) {
                    continue;
                }
                String source_type = sourceToType.get(articleToSource.get(article_id));
                if (source_type.equalsIgnoreCase("conference_proceedings") == false) {
                    candidateGenesSet.add(gene_id);
                }
            }
            candidateGenes = new ArrayList(candidateGenesSet);
        } finally {
            stmt.close();
        }
    }

    public String get(int i) {
        return (String) candidateGenes.get(i);
    }

    public int size() {
        return candidateGenes.size();
    }
}
