package com.tysanclan.site.projectewok.pages.member.group;

import java.util.List;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.GroupForum;
import com.tysanclan.site.projectewok.entities.dao.GroupForumDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.GroupForumFilter;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class GroupForumManagementPage extends AbstractMemberPage {

    @SpringBean
    private GroupForumDAO groupForumDAO;

    /**
     * 
     */
    public GroupForumManagementPage(Group group) {
        super("Forums for " + group.getName());
        if (!group.getLeader().equals(getUser())) {
            throw new RestartResponseAtInterceptPageException(AccessDeniedPage.class);
        }
        Accordion accordion = new Accordion("accordion");
        accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
        accordion.setAutoHeight(false);
        GroupForumFilter filter = new GroupForumFilter();
        filter.setGroup(group);
        List<GroupForum> forums = groupForumDAO.findByFilter(filter);
        accordion.add(new ListView<GroupForum>("forums", ModelMaker.wrap(forums)) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<GroupForum> item) {
                GroupForum forum = item.getModelObject();
                item.add(new Label("name", forum.getName()));
                addEditLink(item);
                addModeratorLink(item);
                addDeleteLink(item);
            }

            private void addEditLink(ListItem<GroupForum> item) {
                GroupForum forum = item.getModelObject();
                Link<GroupForum> editLink = new Link<GroupForum>("edit", ModelMaker.wrap(forum)) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        setResponsePage(new EditGroupForumPage(getModelObject()));
                    }
                };
                editLink.add(new ContextImage("icon", "images/icons/book_edit.png"));
                item.add(editLink);
            }

            private void addModeratorLink(ListItem<GroupForum> item) {
                GroupForum forum = item.getModelObject();
                Link<GroupForum> moderatorLink = new Link<GroupForum>("moderators", ModelMaker.wrap(forum)) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick() {
                        setResponsePage(new EditGroupForumModeratorPage(getModelObject()));
                    }
                };
                moderatorLink.add(new ContextImage("icon", "images/icons/group_edit.png"));
                item.add(moderatorLink);
            }

            private void addDeleteLink(ListItem<GroupForum> item) {
                GroupForum forum = item.getModelObject();
                Link<GroupForum> deleteLink = new Link<GroupForum>("delete", ModelMaker.wrap(forum)) {

                    private static final long serialVersionUID = 1L;

                    @SpringBean
                    private ForumService forumService;

                    @Override
                    public void onClick() {
                        Group gr = getModelObject().getGroup();
                        if (!forumService.deleteForum(getUser(), getModelObject())) {
                            error("Could not delete non-empty forum!");
                        } else {
                            setResponsePage(new GroupForumManagementPage(gr));
                        }
                    }
                };
                deleteLink.add(new ContextImage("icon", "images/icons/delete.png"));
                deleteLink.setVisible(forum.getThreads().isEmpty());
                item.add(deleteLink);
            }
        });
        Link<Group> addLink = new Link<Group>("add", ModelMaker.wrap(group)) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(new CreateGroupForumPage(getModelObject()));
            }
        };
        addLink.add(new ContextImage("icon", "images/icons/book_add.png"));
        accordion.add(addLink);
        add(accordion);
    }
}
