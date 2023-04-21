package net.customware.confluence.plugin.scaffolding.actions;

import com.atlassian.confluence.pages.actions.AbstractPageAction;
import com.atlassian.confluence.pages.actions.PageAware;
import net.customware.confluence.plugin.scaffolding.ScaffoldInfo;

/**
 * This action redirects the user to either the Scaffold edit page if the page
 * contains any scaffolding elements, or the standard Confluence edit page if
 * not.
 */
public class RedirectEditAction extends AbstractPageAction implements PageAware {

    @Override
    public String doDefault() {
        if (ScaffoldInfo.isFieldPresent(getPage())) return "scaffold"; else return "page";
    }

    @Override
    public String getCancelResult() {
        return doDefault();
    }
}
