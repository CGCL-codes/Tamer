package com.j2biz.blogunity.render.macro;

import java.io.IOException;
import java.io.Writer;
import org.radeox.macro.parameter.MacroParameter;
import com.j2biz.blogunity.BlogunityManager;
import com.j2biz.blogunity.dao.BlogDAO;
import com.j2biz.blogunity.exception.BlogunityException;
import com.j2biz.blogunity.pojo.Blog;

public class BlogMacro extends AbstractMacro {

    public String getName() {
        return "blog";
    }

    public String getDescription() {
        return "Shows link to the blog with given name.";
    }

    public String[] getParamDescription() {
        return new String[] { "1: name of the blog" };
    }

    public void execute(Writer writer, MacroParameter params) throws IllegalArgumentException, IOException {
        if (params != null && params.getLength() == 1) {
            String name = params.get(0);
            Blog blog;
            try {
                BlogDAO dao = new BlogDAO();
                blog = dao.getBlogByUrlName(name);
            } catch (BlogunityException e) {
                return;
            }
            String ctxPath = BlogunityManager.getContextPath();
            String style = "individual_blog";
            String title = "individual blog";
            if (blog.getType() == Blog.COMMUNITY_BLOG) {
                if (blog.getCommunityType() == Blog.PUBLIC_COMMUNTIY) {
                    style = "public_community_blog";
                    title = "public community blog";
                } else if (blog.getCommunityType() == Blog.PRIVATE_COMMUNTIY) {
                    style = "private_community_blog";
                    title = "private community blog";
                }
            }
            String icon = BlogunityManager.getBase() + "/images/" + style + ".gif";
            StringBuffer out = new StringBuffer();
            out.append("<nobr>");
            out.append("<img src=\"" + icon + "\"/>&nbsp;");
            out.append("<a class=\"" + style + "\" href=\"" + ctxPath + "/blogs/" + blog.getUrlName() + "\" title=\"" + title + "\">");
            out.append(blog.getFullName());
            out.append("</a>");
            out.append("</nobr>");
            writer.write(out.toString());
        } else throw macroError("Unknown parameters for macro 'blog' found!");
    }
}
