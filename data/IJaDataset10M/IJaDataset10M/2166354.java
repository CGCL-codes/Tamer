package com.bluesky.rivermanhp.jwf.common;

import java.io.IOException;
import java.io.StringWriter;
import com.bluesky.common.TreeNode;
import com.bluesky.javawebbrowser.domain.html.tags.A;
import com.bluesky.javawebbrowser.domain.html.tags.P;
import com.bluesky.javawebbrowser.domain.html.tags.Tag;
import com.bluesky.rivermanhp.domain.blog.Entry;

/**
 * the content will not fully displayed. the title is a hyperlink. and the
 * content may be cut as a summary.
 * 
 * @author jack
 * 
 */
public class BriefBlogView extends BlogView {

    @Override
    public void setEntry(Entry entry) {
        super.setEntry(entry);
        A title = (A) titleTag;
        title.setText(entry.getTitle());
        title.setHref("ViewBlog.jwf?id=" + entry.getId());
        contentTag.setBodyAndParse(entry.getContent());
        boolean cut = false;
        while (contentTag.getChildren().size() > 1 && contentTag.getHtmlLength() > 1000) {
            contentTag.getChildren().remove(contentTag.getChildren().size() - 1);
            cut = true;
        }
        if (cut) {
            P p = new P();
            p.setBodyAndParse("...");
            contentTag.addChild(p);
        }
    }
}
