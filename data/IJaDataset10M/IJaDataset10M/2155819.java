package pub.servlets.annotation_task;

import java.util.*;
import java.sql.*;
import pub.db.*;
import pub.utils.ListUtils;
import pub.utils.Log;
import pub.beans.GeneBean;
import pub.beans.BeanFactory;
import pub.beans.IdsToBeanIterator;

public class UnannotatedLocusStrategy implements AnnotationStrategyI {

    private PubConnection conn;

    private List candidateIds;

    private Set cached_locus_types;

    public UnannotatedLocusStrategy(PubConnection conn) {
        this.conn = conn;
        this.candidateIds = new ArrayList();
        this.cached_locus_types = null;
        initializeCandidates();
    }

    private void initializeCandidates() {
        try {
            candidateIds = getCandidateIds(conn);
        } catch (SQLException e) {
            throw new RollbackException(e);
        }
    }

    /** Returns a list of candidate genes.  Each gene is connected to
     * a locus that doesn't yet have the requistite three term types
     * annotated to it.  Also, each gene has at least one valid hit.
     **/
    private List getCandidateIds(PubConnection conn) throws SQLException {
        List candidates = collectUnsortedCandidates(conn);
        Collections.sort(candidates);
        List results = new ArrayList();
        for (int i = 0; i < candidates.size(); i++) {
            results.add(((GeneIdCountPair) candidates.get(i)).gene_id);
        }
        return results;
    }

    private Set getLocusTypePairSet() throws SQLException {
        if (cached_locus_types == null) {
            cached_locus_types = makeLocusTypePairSet(conn);
        }
        return cached_locus_types;
    }

    /** Returns a set of LocusTypePair elements.  Utility to
     * efficiently find loci annotated to all three term types. */
    private Set makeLocusTypePairSet(PubConnection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select pub_gene_locus.pub_locus_id, obj_term.type" + "    from pub_termannotation pta, pub_term gene_term, pub_term obj_term, pub_gene_locus" + "    where pta.subject_term_id = gene_term.id" + "      and pta.object_term_id = obj_term.id" + "      and gene_term.pub_gene_id = pub_gene_locus.pub_gene_id" + "      and pta.is_obsolete='n'" + "      and gene_term.is_obsolete='n'" + "      and obj_term.is_obsolete='n'" + "      and pub_gene_locus.is_obsolete='n'");
        try {
            ResultSet rs = stmt.executeQuery();
            Set results = new HashSet();
            while (rs.next()) {
                results.add(new LocusTypePair(rs.getString(1), rs.getString(2).intern()));
            }
            return results;
        } finally {
            stmt.close();
        }
    }

    private List collectUnsortedCandidates(PubConnection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select distinct pub_locus.id, " + "              gene_term.pub_gene_id, " + "              upper(gene_term.name) " + "    from pub_locus, pub_gene_locus, pub_term gene_term, " + "         pub_hit, pub_article, pub_source " + "    left join pub_annotationtask " + "        on gene_term.id =pub_annotationtask.pub_term_id" + "    where" + "" + "      (pub_annotationtask.id is null or (pub_annotationtask.status not in ('dropped', 'unfinished', 'deprecated')))" + "" + "      and pub_locus.id = pub_gene_locus.pub_locus_id" + "      and pub_gene_locus.pub_gene_id = gene_term.pub_gene_id" + "      and gene_term.id = pub_hit.pub_term_id" + "      and pub_hit.pub_article_id = pub_article.id" + "      and pub_article.pub_source_id = pub_source.id" + "" + "      and pub_source.type != 'conference_proceedings'" + "" + "      and pub_locus.is_obsolete='n'" + "      and pub_gene_locus.is_obsolete='n'" + "      and gene_term.is_obsolete='n'" + "      and pub_hit.is_obsolete='n' " + "          and (pub_hit.is_valid = 'y' or pub_hit.is_valid is null)" + "      and pub_article.is_obsolete='n'" + "      and pub_source.is_obsolete='n'");
        try {
            ResultSet rs = stmt.executeQuery();
            List candidates = new ArrayList();
            while (rs.next()) {
                String locus_id = rs.getString(1);
                String gene_id = rs.getString(2);
                String gene_name = rs.getString(3);
                int count = countDistinctAnnotationTypesToLocus(locus_id);
                final int THRESHOLD = 3;
                if (count < THRESHOLD) {
                    candidates.add(new GeneIdCountPair(gene_id, gene_name, count));
                }
            }
            return candidates;
        } finally {
            stmt.close();
        }
    }

    private int countDistinctAnnotationTypesToLocus(String locus_id) throws SQLException {
        int count = 0;
        if (getLocusTypePairSet().contains(new LocusTypePair(locus_id, "comp"))) {
            count++;
        }
        if (getLocusTypePairSet().contains(new LocusTypePair(locus_id, "proc"))) {
            count++;
        }
        if (getLocusTypePairSet().contains(new LocusTypePair(locus_id, "func"))) {
            count++;
        }
        return count;
    }

    public List getCandidateGenes(int max) {
        List results = new ArrayList();
        Iterator iter = this.iterator();
        int i = 0;
        while (i < max && iter.hasNext()) {
            results.add(iter.next());
            i++;
        }
        return results;
    }

    public Iterator iterator() {
        return new IdsToBeanIterator(conn, "GENE", candidateIds);
    }

    public int countTotalCandidates() {
        return candidateIds.size();
    }

    public static class GeneIdCountPair implements Comparable {

        public String gene_id;

        public Integer count;

        public String name;

        public GeneIdCountPair(String gene_id, String name, int count) {
            this.gene_id = gene_id;
            this.name = name;
            this.count = new Integer(count);
        }

        private boolean looksLikeTigrName() {
            return pub.utils.StringUtils.looksLikeTigrModelName(name);
        }

        public int compareTo(Object o) {
            GeneIdCountPair other = (GeneIdCountPair) o;
            if (this.looksLikeTigrName() && (!other.looksLikeTigrName())) {
                return 1;
            } else if (other.looksLikeTigrName() && (!this.looksLikeTigrName())) {
                return -1;
            }
            int value = this.count.compareTo(other.count);
            if (value != 0) {
                return value;
            }
            return this.gene_id.compareTo(other.gene_id);
        }
    }

    private static class LocusTypePair {

        public String locus;

        public String type;

        public LocusTypePair(String locus, String type) {
            this.locus = locus;
            this.type = type;
        }

        public int hashCode() {
            return locus.hashCode() * 31 + type.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj instanceof LocusTypePair) {
                LocusTypePair other = (LocusTypePair) obj;
                return ((other.locus.equals(this.locus) && (other.type.equals(this.type))));
            }
            return false;
        }
    }
}
