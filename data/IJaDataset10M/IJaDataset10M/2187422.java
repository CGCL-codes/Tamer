package org.efs.openreports.actions.admin;

import java.util.List;
import org.apache.log4j.Logger;
import org.efs.openreports.actions.DisplayTagAction;
import org.efs.openreports.objects.ORTag;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.TagProvider;

public class SearchAction extends DisplayTagAction {

    private static final long serialVersionUID = 7643660200420455493L;

    protected static Logger log = Logger.getLogger(SearchAction.class);

    private String tags;

    private String search;

    private List results;

    private TagProvider tagProvider;

    public String execute() {
        try {
            tags = tagProvider.getTagList(null, ORTag.TAG_TYPE_UI);
            results = tagProvider.getTaggedObjects(new String[] { search }, null, ORTag.TAG_TYPE_UI);
        } catch (ProviderException pe) {
            addActionError(pe.getMessage());
            return ERROR;
        }
        return SUCCESS;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getTags() {
        return tags;
    }

    public List getResults() {
        return results;
    }

    public void setTagProvider(TagProvider tagProvider) {
        this.tagProvider = tagProvider;
    }
}
