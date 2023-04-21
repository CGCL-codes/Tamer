package com.tysanclan.site.projectewok.pages.member;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.NewsForum;
import com.tysanclan.site.projectewok.entities.Rank;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.CHANCELLOR)
public class EditForumPage extends TysanPage {

    /**
	 * 
	 */
    public EditForumPage(Forum forum) {
        super("Edit forum");
        Accordion accordion = new Accordion("accordion");
        accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
        accordion.setAutoHeight(false);
        accordion.add(new Label("title", "Edit forum " + forum.getName()));
        Form<Forum> editForm = new Form<Forum>("editform", ModelMaker.wrap(forum)) {

            private static final long serialVersionUID = 1L;

            @SpringBean
            private ForumService forumService;

            @SuppressWarnings("unchecked")
            @Override
            protected void onSubmit() {
                TextField<String> nameField = (TextField<String>) get("name");
                TextArea<String> descriptionArea = (TextArea<String>) get("description");
                CheckBox interactiveBox = (CheckBox) get("interactive");
                CheckBox publicAccessBox = (CheckBox) get("publicaccess");
                CheckBox membersOnlyBox = (CheckBox) get("membersonly");
                String name = nameField.getModelObject();
                String description = descriptionArea.getModelObject();
                Boolean interactive = interactiveBox.getModelObject();
                Boolean publicAccess = publicAccessBox.getModelObject();
                Boolean membersOnly = membersOnlyBox.getModelObject();
                Forum f = getModelObject();
                if (f.getName() == null || !f.getName().equals(name)) {
                    forumService.setForumName(f, name, getUser());
                }
                if (f.getDescription() == null || !f.getDescription().equals(description)) {
                    forumService.setForumDescription(f, description, getUser());
                }
                if (!Boolean.valueOf(f.isInteractive()).equals(interactive)) {
                    forumService.setInteractive(f, interactive, getUser());
                }
                if (!Boolean.valueOf(f.isPublicAccess()).equals(publicAccess)) {
                    forumService.setModeratorOnlyRestriction(getUser(), f, publicAccess);
                }
                if (!Boolean.valueOf(f.isMembersOnly()).equals(membersOnly)) {
                    forumService.setMembersOnly(getUser(), f, membersOnly);
                }
                setResponsePage(new ForumManagementPage());
            }
        };
        editForm.add(new TextField<String>("name", new Model<String>(forum.getName())));
        editForm.add(new TextArea<String>("description", new Model<String>(forum.getDescription())));
        editForm.add(new CheckBox("interactive", new Model<Boolean>(forum.isInteractive())).setEnabled(false));
        editForm.add(new CheckBox("publicaccess", new Model<Boolean>(!forum.isPublicAccess())).setEnabled(!(forum instanceof NewsForum)));
        editForm.add(new CheckBox("membersonly", new Model<Boolean>(forum.isMembersOnly())).setEnabled(!(forum instanceof NewsForum)));
        accordion.add(editForm);
        add(accordion);
        add(new Link<Void>("back") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                setResponsePage(new ForumManagementPage());
            }
        });
    }
}
