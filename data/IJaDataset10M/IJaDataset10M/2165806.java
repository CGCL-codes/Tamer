package org.vardb.util.dao;

public interface ISorting {

    void addTerm(String field);

    void addTerm(String field, SortDir dir);

    void setMultiSort(String sort, String dir);

    void setSort(String str);

    String getSql(String prefix);

    String getHql(String prefix);

    public enum SortDir {

        ASC(false), DESC(true);

        private boolean reverse;

        SortDir(final boolean reverse) {
            this.reverse = reverse;
        }

        public boolean getReverse() {
            return this.reverse;
        }
    }

    ;
}
