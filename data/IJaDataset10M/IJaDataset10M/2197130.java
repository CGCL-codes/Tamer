package br.net.woodstock.rockframework.persistence.orm.impl;

import java.util.Collection;
import br.net.woodstock.rockframework.persistence.orm.Page;
import br.net.woodstock.rockframework.persistence.orm.QueryResult;

class QueryResultImpl implements QueryResult {

    private static final long serialVersionUID = 135627383505235626L;

    private int total;

    private Collection<Object> result;

    private Page currentPage;

    public QueryResultImpl(final int total, final Collection<Object> result, final Page currentPage) {
        super();
        this.total = total;
        this.result = result;
        this.currentPage = currentPage;
    }

    @Override
    public int getTotal() {
        return this.total;
    }

    public void setTotal(final int total) {
        this.total = total;
    }

    @Override
    public Collection<Object> getResult() {
        return this.result;
    }

    public void setResult(final Collection<Object> result) {
        this.result = result;
    }

    @Override
    public Page getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(final Page currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public Page getFirstPage() {
        if (this.total > 0) {
            if (this.currentPage.getPageNumber() == 1) {
                return this.currentPage;
            }
            return new PageImpl(1, this.getCurrentPage().getResultsPerPage());
        }
        return null;
    }

    @Override
    public Page getPreviousPage() {
        if (this.total > 0) {
            if (this.currentPage.getPageNumber() > 1) {
                return new PageImpl(this.currentPage.getPageNumber() - 1, this.getCurrentPage().getResultsPerPage());
            }
        }
        return null;
    }

    @Override
    public Page getNextPage() {
        if (this.total > 0) {
            int nextResult = (this.currentPage.getPageNumber() * this.currentPage.getResultsPerPage()) + 1;
            if (nextResult < this.total) {
                return new PageImpl(this.currentPage.getPageNumber() + 1, this.getCurrentPage().getResultsPerPage());
            }
        }
        return null;
    }

    @Override
    public Page getLastPage() {
        if (this.total > 0) {
            int lastPage = this.total / this.currentPage.getResultsPerPage();
            int mod = this.total % this.currentPage.getResultsPerPage();
            if (mod > 0) {
                lastPage++;
            }
            if (this.currentPage.getPageNumber() == lastPage) {
                return this.currentPage;
            }
            return new PageImpl(lastPage, this.getCurrentPage().getResultsPerPage());
        }
        return null;
    }
}
