package com.bluesky.rivermanhp.jwf.blog.comment;

import com.bluesky.javawebbrowser.domain.html.tags.Tag;
import com.bluesky.jwf.ComponentFactory;
import com.bluesky.jwf.component.list.ListItem;
import com.bluesky.rivermanhp.domain.blog.Comment;
import com.bluesky.rivermanhp.jwf.common.BlogView;
import com.bluesky.rivermanhp.jwf.common.BriefBlogView;

public class CommentListItem extends ListItem {

    protected Tag itemTag;

    private String commentTemplate;

    @Override
    public void init(String html) {
        super.init(html);
        itemTag = loadByJwfClass("item");
        commentTemplate = itemTag.descendantToHtml();
        itemTag.removeAllChildren();
    }

    @Override
    public void setModel(Object model) {
        super.setModel(model);
        Comment comment = (Comment) model;
        CommentView view = (CommentView) ComponentFactory.getInstance().createComponent(CommentView.class.getName(), commentTemplate);
        view.setComment(comment);
        itemTag.removeAllChildren();
        itemTag.addChild(view);
    }
}
