package org.vardb.util;

import java.util.ArrayList;
import java.util.List;
import org.vardb.CConstants;

public class CSorting implements ISorting {

    protected List<SortTerm> terms = new ArrayList<SortTerm>();

    public List<SortTerm> getTerms() {
        return this.terms;
    }

    public void addTerm(SortTerm term) {
        this.terms.add(term);
    }

    public void addTerm(String field) {
        addTerm(new SortTerm(field));
    }

    public void addTerm(String field, CConstants.SortDir dir) {
        addTerm(new SortTerm(field, dir));
    }

    public void setMultiSort(String sort, String dir) {
        if (!CStringHelper.hasContent(sort) || !CStringHelper.hasContent(dir)) return;
        List<String> fields = CStringHelper.split(sort, ",");
        List<String> dirs = CStringHelper.split(dir, ",");
        this.terms.clear();
        for (int index = 0; index < fields.size(); index++) {
            String field = fields.get(index);
            CConstants.SortDir sortdir = CConstants.SortDir.ASC;
            if (index < dirs.size()) sortdir = parseDir(dirs.get(index));
            addTerm(field, sortdir);
        }
    }

    public void setSort(String str) {
        if (!CStringHelper.hasContent(str)) return;
        this.terms.clear();
        for (String term : CStringHelper.split(str.trim(), ",")) {
            term = term.trim();
            int index = term.indexOf(" ");
            String field = term;
            CConstants.SortDir dir = CConstants.SortDir.ASC;
            if (index != -1) {
                field = term.substring(0, index);
                dir = parseDir(term.substring(index + 1));
            }
            addTerm(field, dir);
        }
    }

    private CConstants.SortDir parseDir(String dir) {
        return CConstants.SortDir.valueOf(dir.toUpperCase());
    }

    public String getSql(String prefix) {
        if (this.terms.isEmpty()) return null;
        List<String> terms = new ArrayList<String>();
        for (SortTerm term : this.terms) {
            terms.add(term.getSql(prefix));
        }
        return CStringHelper.join(terms, ", ");
    }

    public String getHql(String prefix) {
        String hql = getSql(prefix);
        if (hql == null) return null;
        hql = CStringHelper.replace(hql, "_name", ".name");
        hql = CStringHelper.replace(hql, "_identifier", ".identifier");
        hql = CStringHelper.replace(hql, "_dtype", ".dtype");
        return hql;
    }

    public static class SortTerm {

        protected String field;

        protected CConstants.SortDir dir = CConstants.SortDir.ASC;

        public SortTerm(String field) {
            this.field = field;
        }

        public SortTerm(String field, CConstants.SortDir dir) {
            this(field);
            this.dir = dir;
        }

        public String getField() {
            return this.field;
        }

        public void setField(final String field) {
            this.field = field;
        }

        public CConstants.SortDir getDir() {
            return this.dir;
        }

        public void setDir(final CConstants.SortDir dir) {
            this.dir = dir;
        }

        public String getSql(String prefix) {
            return prefix + this.field + " " + this.dir.name();
        }
    }
}
