package dmeduc.wicket.weblink;

import modelibra.wicket.component.EntityFormPanel;
import modelibra.wicket.component.EntityListPanel;
import modelibra.wicket.component.EntityListWithPropertyNamesPanel;
import modelibra.wicket.component.EntityPanel;
import modelibra.wicket.component.EntityTablePanel;
import modelibra.wicket.component.widget.PropertyLabelPanel;
import modelibra.wicket.model.EntitiesModel;
import modelibra.wicket.model.EntityModel;
import modelibra.wicket.model.EntityPropertyModel;
import org.apache.wicket.Application;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.modelibra.wicket.view.View;
import org.modelibra.wicket.view.ViewModel;
import org.modelibra.wicket.widget.LabelPanel;
import dmeduc.weblink.WebLink;
import dmeduc.weblink.comment.Comment;
import dmeduc.weblink.member.Member;
import dmeduc.wicket.app.DmEducApp;
import dmeduc.wicket.weblink.comment.model.CommentModel;
import dmeduc.wicket.weblink.comment.model.CommentTextModel;

public class ShowCasePage extends WebPage {

    public ShowCasePage() {
        DmEducApp dmEducApp = (DmEducApp) Application.get();
        WebLink webLink = dmEducApp.getDmEduc().getWebLink();
        Member member = webLink.getMembers().first();
        Comment comment = webLink.getComments().getComment(1194622721907L);
        ViewModel memberViewModel = new ViewModel(webLink);
        View memberView = new View();
        memberView.setWicketId("memberLabelPanel");
        memberViewModel.setEntity(member);
        memberViewModel.setPropertyCode("lastName");
        add(new LabelPanel(memberViewModel, memberView));
        EntityPropertyModel model = new EntityPropertyModel(member, "lastName");
        add(new PropertyLabelPanel("memberPanel", model));
        add(new Label("memberLabel", model));
        IModel memberModel = new Model(member);
        EntityPropertyModel modelAroundModel = new EntityPropertyModel(memberModel, "lastName");
        add(new Label("memberModelLabel", modelAroundModel));
        ViewModel commentViewModel = new ViewModel(webLink);
        View commentView = new View();
        commentView.setWicketId("commentLabelPanel");
        commentView.getUserProperties().addUserProperty("shortText", true);
        commentViewModel.setEntity(comment);
        commentViewModel.setPropertyCode("text");
        add(new LabelPanel(commentViewModel, commentView));
        EntityPropertyModel commentTextModel = new EntityPropertyModel(comment, "text");
        commentTextModel.setShortText(true);
        add(new PropertyLabelPanel("commentTextPanel", commentTextModel));
        add(new Label("commentLabel", commentTextModel));
        EntityModel commentEntityModel = new EntityModel(comment);
        add(new EntityPanel("commentEntityPanel", commentEntityModel));
        CommentTextModel commentWithReadOnlyTextModel = new CommentTextModel(comment);
        add(new EntityPanel("commentWithReadOnlyTextPanel", commentWithReadOnlyTextModel));
        CommentTextModel commentWithTextModel = new CommentTextModel(comment);
        commentWithTextModel.setReadOnly(false);
        add(new EntityPanel("commentWithTextPanel", commentWithTextModel));
        CommentModel commentUpdateModel = new CommentModel(comment.copy(), webLink.getComments());
        commentUpdateModel.setReadOnly(false);
        add(new EntityFormPanel("commentUpdateForm", commentUpdateModel));
        CommentModel commentAddModel = new CommentModel(new Comment(webLink), webLink.getComments());
        commentAddModel.setReadOnly(false);
        commentAddModel.setAdd(true);
        add(new EntityFormPanel("commentAddForm", commentAddModel));
        add(new EntityListPanel("commentList", new EntitiesModel(webLink.getComments())));
        add(new EntityListWithPropertyNamesPanel("commentListWithPropertyNames", new EntitiesModel(webLink.getComments())));
        add(new EntityTablePanel("commentTable", new EntitiesModel(webLink.getComments())));
    }
}
