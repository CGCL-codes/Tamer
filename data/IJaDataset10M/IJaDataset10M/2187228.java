package org.vardb.sequences.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.vardb.lists.CIdList;
import org.vardb.lists.IdList;
import org.vardb.resources.dao.CPfam;
import org.vardb.sequences.ISequence;
import org.vardb.sequences.ISequenceView;
import org.vardb.util.CAbstractDaoImpl;
import org.vardb.util.CIdentifierListHelper;
import org.vardb.util.CStringHelper;
import org.vardb.util.IPaging;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

public class CSequenceDaoImpl extends CAbstractDaoImpl implements ISequenceDao {

    @SuppressWarnings("unchecked")
    public IdList searchSequences(String user_id, String query) {
        if (!CStringHelper.hasContent(query)) return new CIdList();
        StringBuilder buffer = new StringBuilder();
        buffer.append("SELECT sequence.id\n");
        buffer.append("FROM vw_sequences as sequence\n");
        buffer.append("WHERE " + query + "\n");
        filterUsersequences(user_id, buffer);
        buffer.append("ORDER BY sequence.id\n");
        List<Integer> ids = getSession().createSQLQuery(buffer.toString()).list();
        return new CIdList(ids);
    }

    @SuppressWarnings("unchecked")
    public IdList searchSequences(String user_id, IdList idlist, String query) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("SELECT sequence.id\n");
        buffer.append("FROM vw_sequences as sequence\n");
        buffer.append("WHERE ").append(idlist.toSql("sequence.id")).append("\n");
        filterUsersequences(user_id, buffer);
        if (CStringHelper.hasContent(query)) buffer.append("AND " + query + "\n");
        buffer.append("ORDER BY sequence.id\n");
        List<Integer> ids = getSession().createSQLQuery(buffer.toString()).list();
        return new CIdList(ids);
    }

    @SuppressWarnings("unchecked")
    public IdList searchSequences(String user_id, String search, IPaging paging) {
        String subquery = paging.getFilter().getSql();
        StringBuilder buffer = new StringBuilder();
        buffer.append("FROM vw_sequences as sequence\n");
        buffer.append("WHERE " + search + "\n");
        if (CStringHelper.hasContent(subquery)) buffer.append("AND " + subquery + "\n");
        filterUsersequences(user_id, buffer);
        String sql = buffer.toString();
        Query query = getSession().createSQLQuery("SELECT count(*) " + sql);
        BigInteger total = (BigInteger) (query.uniqueResult());
        paging.setTotal(Integer.valueOf(total.toString()));
        System.out.println("total=" + paging.getTotal());
        query = getSession().createSQLQuery("SELECT sequence.id\n" + sql + " ORDER BY " + paging.getSorting().getSql("sequence."));
        if (paging.getPagesize() != null) {
            System.out.println("start=" + paging.getStart() + ", number=" + paging.getPagesize());
            query.setFirstResult(paging.getStart());
            query.setMaxResults(paging.getPagesize());
        }
        List<Integer> list = query.list();
        System.out.println("numresults=" + list.size());
        return new CIdList(list);
    }

    @SuppressWarnings("unchecked")
    public List<CSequenceView> getSequences(String user_id, String search) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("SELECT *\n");
        buffer.append("FROM vw_sequences as sequence\n");
        buffer.append("WHERE " + search + "\n");
        filterUsersequences(user_id, buffer);
        buffer.append("ORDER BY id\n");
        String sql = buffer.toString();
        Query query = getSession().createSQLQuery(sql).addEntity(CSequenceView.class);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<CSequenceView> getSequences(String user_id, String search, IPaging paging) {
        String subquery = paging.getFilter().getSql();
        StringBuilder buffer = new StringBuilder();
        buffer.append("FROM vw_sequences as sequence\n");
        buffer.append("WHERE " + search + "\n");
        if (CStringHelper.hasContent(subquery)) buffer.append("AND " + subquery + "\n");
        filterUsersequences(user_id, buffer);
        String sql = buffer.toString();
        Query query = getSession().createSQLQuery("SELECT count(*) " + sql);
        BigInteger total = (BigInteger) (query.uniqueResult());
        paging.setTotal(Integer.valueOf(total.toString()));
        System.out.println("total=" + paging.getTotal());
        query = getSession().createSQLQuery("SELECT *\n" + sql + " ORDER BY " + paging.getSorting().getSql("sequence.")).addEntity(CSequenceView.class);
        if (paging.getPagesize() != null) {
            System.out.println("start=" + paging.getStart() + ", number=" + paging.getPagesize());
            query.setFirstResult(paging.getStart());
            query.setMaxResults(paging.getPagesize());
        }
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<ISequenceView> getSequenceViewsById(IdList ids) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("SELECT *\n");
        buffer.append("FROM vw_sequences AS sequence\n");
        buffer.append("WHERE " + ids.toSql("sequence.id") + "\n");
        String sql = buffer.toString();
        Query query = getSession().createSQLQuery(sql).addEntity(CSequenceView.class);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<ISequenceView> getSequenceViewsById(IdList ids, IPaging paging) {
        String subquery = paging.getFilter().getSql();
        StringBuilder buffer = new StringBuilder();
        buffer.append("FROM vw_sequences as sequence\n");
        buffer.append("WHERE " + ids.toSql("sequence.id") + "\n");
        if (CStringHelper.hasContent(subquery)) buffer.append("AND " + subquery + "\n");
        String sql = buffer.toString();
        Query query = getSession().createSQLQuery("SELECT count(*) " + sql);
        BigInteger total = (BigInteger) (query.uniqueResult());
        paging.setTotal(Integer.valueOf(total.toString()));
        System.out.println("total=" + paging.getTotal());
        query = getSession().createSQLQuery("SELECT *\n" + sql + " ORDER BY " + paging.getSorting().getSql("sequence.")).addEntity(CSequenceView.class);
        if (paging.getPagesize() != null) {
            System.out.println("start=" + paging.getStart() + ", number=" + paging.getPagesize());
            query.setFirstResult(paging.getStart());
            query.setMaxResults(paging.getPagesize());
        }
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public IdList lookupIdentifiers(String user_id, Collection<String> identifiers) {
        if (identifiers.isEmpty()) return new CIdList();
        StringBuilder buffer = new StringBuilder();
        buffer.append("SELECT sequence.id\n");
        buffer.append("FROM vw_sequences as sequence\n");
        buffer.append("WHERE " + CIdentifierListHelper.toSql("sequence.identifier", identifiers) + "\n");
        filterUsersequences(user_id, buffer);
        buffer.append("ORDER BY sequence.id\n");
        List<Integer> ids = getSession().createSQLQuery(buffer.toString()).list();
        return new CIdList(ids);
    }

    @SuppressWarnings("unchecked")
    public Multimap<String, Integer> getIdentifierMap(Collection<String> identifiers) {
        Multimap<String, Integer> map = TreeMultimap.create();
        if (identifiers.isEmpty()) return map;
        StringBuilder buffer = new StringBuilder();
        buffer.append("select sequence.identifier, sequence.id\n");
        from(buffer);
        buffer.append("and " + CIdentifierListHelper.toSql("sequence.identifier", identifiers) + "\n");
        List<Object[]> list = find(buffer);
        for (Object[] row : list) {
            map.put(row[0].toString(), (Integer) row[1]);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public List<ISequence> getSequencesByIdentifier(Collection<String> identifiers) {
        if (identifiers.isEmpty()) return new ArrayList<ISequence>();
        StringBuilder buffer = new StringBuilder();
        buffer.append("select sequence\n");
        from(buffer);
        buffer.append("and " + CIdentifierListHelper.toSql("sequence.identifier", identifiers) + "\n");
        return find(buffer);
    }

    @SuppressWarnings("unchecked")
    public List<ISequence> getSequencesById(IdList idlist) {
        if (idlist.isEmpty()) return new ArrayList<ISequence>();
        StringBuilder buffer = new StringBuilder();
        buffer.append("select sequence\n");
        from(buffer);
        buffer.append("and ").append(idlist.toSql("sequence.id")).append("\n");
        return find(buffer);
    }

    @SuppressWarnings("unchecked")
    public List<CSequence> getSequencesByUserId(String user_id) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("select sequence\n");
        from(buffer);
        filterUsersequences(user_id, buffer);
        return find(buffer);
    }

    @SuppressWarnings("unchecked")
    public List<CSequence> getSequencesByUserId(String user_id, IdList idlist) {
        if (idlist.isEmpty()) return new ArrayList<CSequence>();
        StringBuilder buffer = new StringBuilder();
        buffer.append("select sequence\n");
        from(buffer);
        filterUsersequences(user_id, buffer);
        buffer.append("and ").append(idlist.toSql("sequence.id")).append("\n");
        return find(buffer);
    }

    @SuppressWarnings("unchecked")
    public List<ISequence> getSequences() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("select sequence\n");
        from(buffer);
        return find(buffer);
    }

    public int getNumSequences(boolean include_uploaded) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("select count(*)\n");
        buffer.append("from CSequence as sequence\n");
        if (!include_uploaded) buffer.append("where sequence.uploaded=FALSE\n");
        Query query = getSession().createQuery(buffer.toString());
        Long total = (Long) (query.uniqueResult());
        return total.intValue();
    }

    @SuppressWarnings("unchecked")
    public List<ISequence> getSequences(boolean include_uploaded, int start, int limit) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("select sequence\n");
        buffer.append("from CSequence as sequence\n");
        if (!include_uploaded) buffer.append("where sequence.uploaded=FALSE\n");
        String sql = buffer.toString();
        Query query = getSession().createQuery(sql);
        query.setFirstResult(start);
        query.setMaxResults(limit);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<String> getUpdatableSequences(String family_identifier, List<String> sources, int ntlength) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("SELECT sequence.accession\n");
        buffer.append("FROM vw_sequences as sequence\n");
        buffer.append("WHERE family_identifier=:family_identifier\n");
        buffer.append("AND ntlength<=:ntlength\n");
        buffer.append("AND source_identifier in ('" + CStringHelper.join(sources, "','") + "')\n");
        buffer.append("ORDER BY sequence.accession\n");
        Query query = getSession().createSQLQuery(buffer.toString());
        query.setParameter("family_identifier", family_identifier);
        query.setParameter("ntlength", ntlength);
        return query.list();
    }

    public ISequence getSequence(int sequence_id) {
        return (CSequence) load(CSequence.class, sequence_id);
    }

    public ISequence getSequence(String identifier) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("select sequence\n");
        from(buffer);
        buffer.append("and sequence.identifier=:identifier\n");
        return (ISequence) findUnique(buffer, "identifier", identifier);
    }

    public CSequenceView getSequenceView(String identifier) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("SELECT *\n");
        buffer.append("FROM vw_sequences as sequence\n");
        buffer.append("WHERE identifier=:identifier\n");
        String sql = buffer.toString();
        Query query = getSession().createSQLQuery(sql).addEntity(CSequenceView.class);
        query.setString("identifier", identifier);
        return (CSequenceView) query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<CSequenceSummaryData> getSequenceSummaryDataById(IdList idlist) {
        if (idlist.isEmpty()) return new ArrayList<CSequenceSummaryData>();
        StringBuilder buffer = new StringBuilder();
        buffer.append("select sequence\n");
        buffer.append("from CSequenceSummaryData as sequence\n");
        buffer.append("where ").append(idlist.toSql("sequence.id")).append("\n");
        return find(buffer);
    }

    @SuppressWarnings("unchecked")
    public List<CAdvancedSequenceSummaryData> getAdvancedSequenceSummaryDataById(IdList idlist) {
        if (idlist.isEmpty()) return new ArrayList<CAdvancedSequenceSummaryData>();
        StringBuilder buffer = new StringBuilder();
        buffer.append("select sequence\n");
        buffer.append("from CAdvancedSequenceSummaryData as sequence\n");
        buffer.append("where ").append(idlist.toSql("sequence.id")).append("\n");
        String sql = buffer.toString();
        return find(sql);
    }

    public void addSequence(ISequence sequence) {
        save(sequence);
    }

    public void addSequences(final Collection<ISequence> sequences) {
        updateSequences(sequences);
    }

    public void updateSequence(ISequence sequence) {
        saveOrUpdate(sequence);
    }

    public void updateSequences(Collection<ISequence> sequences) {
        saveOrUpdateAll(sequences);
    }

    public void deleteSequence(int id) {
        delete(getSequence(id));
    }

    public void deleteSequences(Collection<CSequence> sequences) {
        deleteAll(sequences);
    }

    public int deleteUserSequences(String user_id) {
        List<CSequence> sequences = getSequencesByUserId(user_id);
        int num = sequences.size();
        deleteSequences(sequences);
        return num;
    }

    public int deleteUserSequences(String user_id, IdList ids) {
        List<CSequence> sequences = getSequencesByUserId(user_id, ids);
        int num = sequences.size();
        deleteSequences(sequences);
        return num;
    }

    private void from(StringBuilder buffer) {
        buffer.append("from CSequence as sequence\n");
        buffer.append("where 1=1\n");
    }

    private void filterUsersequences(String user_id, StringBuilder buffer) {
        buffer.append("AND (sequence.uploaded=FALSE OR (sequence.uploaded=TRUE AND sequence.user_id='");
        buffer.append(CStringHelper.escape(user_id));
        buffer.append("'))\n");
    }

    @SuppressWarnings("unchecked")
    public List<CSequenceset> getSequencesets() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("select sequenceset\n");
        buffer.append("from CSequenceset as sequenceset\n");
        String sql = buffer.toString();
        return find(sql);
    }

    public CSequenceset getSequenceset(String identifier) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("select sequenceset\n");
        buffer.append("from CSequenceset as sequenceset\n");
        buffer.append("where sequenceset.identifier=:identifier\n");
        String sql = buffer.toString();
        return (CSequenceset) findUnique(sql, "identifier", identifier);
    }

    public CSequenceset getSequenceset(int id) {
        return (CSequenceset) load(CSequenceset.class, id);
    }

    public int addSequenceset(CSequenceset sequenceset) {
        save(sequenceset);
        return sequenceset.getId();
    }

    public void updateSequenceset(CSequenceset sequenceset) {
        saveOrUpdate(sequenceset);
    }

    public void deleteSequenceset(int id) {
        delete(getSequenceset(id));
    }

    @SuppressWarnings("unchecked")
    public List<String> findPfamIdentifiers() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("SELECT DISTINCT architecture\n");
        buffer.append("FROM sequences\n");
        buffer.append("WHERE architecture!=''\n");
        Query query = getSession().createSQLQuery(buffer.toString());
        List<String> architectures = query.list();
        List<String> identifiers = new ArrayList<String>();
        for (String architecture : architectures) {
            for (String identifier : CStringHelper.split(architecture, ";")) {
                if (!identifiers.contains(identifier)) identifiers.add(identifier);
            }
        }
        Collections.sort(identifiers);
        return identifiers;
    }

    @SuppressWarnings("unchecked")
    public void indexPfamDomains() {
        Map<String, CPfam> pfams = new HashMap<String, CPfam>();
        for (CPfam pfam : (List<CPfam>) find("select pfam from CPfam as pfam")) {
            pfams.put(pfam.getIdentifier(), pfam);
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append("select sequence\n");
        buffer.append("from CSequence as sequence\n");
        buffer.append("where sequence.architecture!=''\n");
        List<CSequence> sequences = find(buffer.toString());
        for (CSequence sequence : sequences) {
            for (String identifier : CStringHelper.split(sequence.getArchitecture(), ";")) {
                CPfam pfam = pfams.get(identifier);
                if (pfam == null) {
                    System.err.println("can't find pfam domain: " + identifier + " in sequence " + sequence.getAccession());
                    continue;
                }
                pfam.add(sequence);
            }
        }
    }
}
