package org.jcr_blog.frontend.wtc.bb.forms;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sebastian Prehn <sebastian.prehn@planetswebdesign.de>
 */
@Named
@RequestScoped
public class QueryForm {

    private String queryString;

    @Inject
    private Conversation conversation;

    @PostConstruct
    public void init() {
        if (this.conversation.isTransient()) {
            this.conversation.begin();
        }
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getQueryString() {
        return queryString;
    }

    public String search() {
        return "query?faces-redirect=true&amp;includeViewParams=true";
    }
}
