package br.gov.frameworkdemoiselle.pagination;

import java.io.Serializable;
import java.util.Collection;
import java.util.Locale;
import br.gov.frameworkdemoiselle.configuration.Constant;
import br.gov.frameworkdemoiselle.internal.factory.ResourceBundleFactory;
import br.gov.frameworkdemoiselle.util.ResourceBundle;

/**
 * Structure used to handle pagination of data results.
 * 
 * @author SERPRO
 */
public class PagedResult<T> implements Serializable {

    private static final long serialVersionUID = -801711283661737483L;

    private Page page;

    private Long totalResults;

    private Collection<T> results;

    private ResourceBundle bundle;

    public PagedResult(Page page, Long totalResults, Collection<T> results) {
        this.bundle = new ResourceBundleFactory(Locale.getDefault()).create(Constant.CORE_BUNDLE_BASE_NAME);
        if (results != null && page != null) {
            if (results.size() > page.getSize()) {
                throw new PageException(this.bundle.getString("results-count-greater-page-size", String.valueOf(results.size()), String.valueOf(page.getSize())));
            }
        }
        this.page = page;
        this.totalResults = totalResults;
        this.results = results;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

    public Collection<T> getResults() {
        return results;
    }

    public void setResults(Collection<T> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return this.bundle.getString("page-result", String.valueOf(page), String.valueOf(totalResults));
    }
}
