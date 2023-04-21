package org.expressme.webwind.demo;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.expressme.webwind.ActionContext;
import org.expressme.webwind.Mapping;
import org.expressme.webwind.demo.util.DbUtils;
import org.expressme.webwind.renderer.Renderer;
import org.expressme.webwind.renderer.TemplateRenderer;

public class Blog {

    @Mapping("/hello")
    public String hello() {
        return "<h1>Hello, world</h1>";
    }

    @Mapping("/hello/$1")
    public String hello(String name) {
        return "<h1>Hello, " + name + "</h1>";
    }

    @Mapping("/")
    public Renderer index() throws Exception {
        List<Post> posts = DbUtils.queryForList("select id, title, content, creation from Post order by id desc");
        return new TemplateRenderer("/index.htm", "posts", posts);
    }

    @Mapping("/blog/display/$1")
    public Renderer display(long id) throws Exception {
        List<Post> posts = DbUtils.queryForList("select id, title, content, creation from Post where id=?", id);
        if (posts.isEmpty()) throw new IllegalArgumentException("Post not found with id: " + id);
        return new TemplateRenderer("/display.htm", "post", posts.get(0));
    }

    /**
     * Show create page.
     */
    @Mapping("/blog/create")
    public Renderer create() {
        Post post = new Post((-1), "", "");
        return new TemplateRenderer("/edit.htm", "post", post);
    }

    /**
     * Show update page.
     */
    @Mapping("/blog/update/$1")
    public Renderer update(long id) throws Exception {
        List<Post> posts = DbUtils.queryForList("select id, title, content, creation from Post where id=?", id);
        if (posts.isEmpty()) throw new IllegalArgumentException("Post not found with id: " + id);
        return new TemplateRenderer("/edit.htm", "post", posts.get(0));
    }

    /**
     * Handle edit post.
     */
    @Mapping("/blog/edit")
    public String edit() throws Exception {
        HttpServletRequest request = ActionContext.getActionContext().getHttpServletRequest();
        long id = Long.parseLong(request.getParameter("id"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        if (id == (-1)) {
            id = DbUtils.nextId();
            if (1 != DbUtils.executeUpdate("insert into Post (id, title, content, creation) values (?, ?, ?, ?)", id, title, content, new Date())) throw new SQLException("Create post failed.");
        } else {
            if (1 != DbUtils.executeUpdate("update Post set title=?, content=? where id=?", title, content, id)) throw new IllegalArgumentException("Post not found with id: " + id);
        }
        return "redirect:/blog/display/" + id;
    }

    @Mapping("/blog/delete/$1")
    public String delete(long id) throws Exception {
        DbUtils.executeUpdate("delete from Post where id=?", id);
        return "redirect:/";
    }
}
