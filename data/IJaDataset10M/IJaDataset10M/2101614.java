package com.tysanclan.site.projectewok.components;

import org.apache.wicket.markup.html.link.Link;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.pages.ForumPage;

/**
 * Link to a ForumThread
 * 
 * @author Jeroen Steenbeeke
 */
public class ForumLink extends Link<Forum> {

    private static final long serialVersionUID = 1L;

    private long page;

    public ForumLink(String id, Forum forum) {
        this(id, forum, 1L);
    }

    public ForumLink(String id, Forum forum, long page) {
        super(id, ModelMaker.wrap(forum));
        this.page = page;
    }

    @Override
    public void onClick() {
        setResponsePage(new ForumPage(getModelObject(), page));
    }
}
