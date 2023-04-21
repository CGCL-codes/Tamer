package org.vardb.blast.dao;

import java.util.*;
import javax.persistence.*;
import org.vardb.*;
import org.vardb.blast.CBlastParams;
import org.vardb.sequences.*;
import org.vardb.search.CFilter;
import org.vardb.search.CTextFilter;
import org.vardb.search.dao.CSearch;
import org.vardb.users.CUserDetails;
import org.vardb.util.*;

@Entity
@Table(name = "blasts")
public class CBlast extends CSerializableIdList {

    protected String m_id;

    protected String m_username;

    protected String m_name;

    protected String m_query;

    protected String m_filter;

    protected String m_sortby = "rating";

    protected CConstants.SortDir m_dir = CConstants.SortDir.DESC;

    protected int m_start = 0;

    protected int m_pagesize = 20;

    protected int m_total = 0;

    protected String m_xml;

    protected Date m_date;

    protected String m_database;

    protected float m_evalue = 10.0f;

    protected boolean m_lowcomplexity = true;

    protected CBlastParams.Program m_program = CBlastParams.Program.blastp;

    protected int m_maxresults = 500;

    protected CBlastParams.Matrix m_matrix = CBlastParams.Matrix.BLOSUM62;

    protected int m_gapopen = -1;

    protected int m_gapextend = -1;

    public CBlast() {
    }

    public CBlast(String query, CBlastParams params, String xml, List<Integer> ids) {
        super(ids);
        CBeanHelper.copyProperties(this, params);
        m_id = CStringHelper.generateID();
        m_date = new Date();
        m_username = CAcegiHelper.getUsername();
        m_query = query;
        m_name = CStringHelper.truncate(query, 50, "...");
        m_xml = xml;
        m_total = ids.size();
    }

    @Id
    public String getId() {
        return m_id;
    }

    public void setId(String id) {
        m_id = id;
    }

    public String getUsername() {
        return m_username;
    }

    public void setUsername(final String username) {
        m_username = username;
    }

    public String getName() {
        return m_name;
    }

    public void setName(final String name) {
        m_name = name;
    }

    public String getQuery() {
        return m_query;
    }

    public void setQuery(final String query) {
        m_query = query;
    }

    public String getFilter() {
        return m_filter;
    }

    public void setFilter(final String filter) {
        m_filter = filter;
    }

    public String getSortby() {
        return m_sortby;
    }

    public void setSortby(final String sortby) {
        m_sortby = sortby;
    }

    @Enumerated(EnumType.STRING)
    public CConstants.SortDir getDir() {
        return m_dir;
    }

    public void setDir(final CConstants.SortDir dir) {
        m_dir = dir;
    }

    public int getStart() {
        return m_start;
    }

    public void setStart(final int start) {
        m_start = start;
    }

    public int getPagesize() {
        return m_pagesize;
    }

    public void setPagesize(final int pagesize) {
        m_pagesize = pagesize;
    }

    public int getTotal() {
        return m_total;
    }

    public void setTotal(final int total) {
        m_total = total;
    }

    public String getXml() {
        return m_xml;
    }

    public void setXml(final String xml) {
        m_xml = xml;
    }

    public Date getDate() {
        return m_date;
    }

    public void setDate(Date date) {
        m_date = date;
    }

    public String getDatabase() {
        return m_database;
    }

    public void setDatabase(final String database) {
        m_database = database;
    }

    public float getEvalue() {
        return m_evalue;
    }

    public void setEvalue(final float evalue) {
        m_evalue = evalue;
    }

    public boolean getLowcomplexity() {
        return m_lowcomplexity;
    }

    public void setLowcomplexity(final boolean lowcomplexity) {
        m_lowcomplexity = lowcomplexity;
    }

    @Enumerated(EnumType.STRING)
    public CBlastParams.Program getProgram() {
        return m_program;
    }

    public void setProgram(final CBlastParams.Program program) {
        m_program = program;
    }

    public int getMaxresults() {
        return m_maxresults;
    }

    public void setMaxresults(final int maxresults) {
        m_maxresults = maxresults;
    }

    @Enumerated(EnumType.STRING)
    public CBlastParams.Matrix getMatrix() {
        return m_matrix;
    }

    public void setMatrix(final CBlastParams.Matrix matrix) {
        m_matrix = matrix;
    }

    public int getGapopen() {
        return m_gapopen;
    }

    public void setGapopen(final int gapopen) {
        m_gapopen = gapopen;
    }

    public int getGapextend() {
        return m_gapextend;
    }

    public void setGapextend(final int gapextend) {
        m_gapextend = gapextend;
    }

    @Transient
    public CBlastParams getParams() {
        CBlastParams params = new CBlastParams();
        CBeanHelper.copyProperties(params, this);
        return params;
    }

    @Transient
    public IPaging getPaging(IVardbService vardbService) {
        String sort = CBlast.getSort(m_sortby, m_dir);
        String filter = CTextFilter.convert(m_filter, vardbService);
        IPaging paging = new CPaging(m_start, m_pagesize, filter, sort);
        return paging;
    }

    public static String getSort() {
        return getSort("rating", CConstants.SortDir.DESC);
    }

    public static String getSort(String sortby) {
        return getSort(sortby, CConstants.SortDir.ASC);
    }

    public static String getSort(String sortby, CConstants.SortDir dir) {
        String sort = "sequence." + sortby;
        sort += " " + dir;
        return sort;
    }
}
