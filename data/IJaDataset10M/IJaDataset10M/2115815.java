package org.bhp.bbs.actions;

import org.bhp.bbs.beans.Post;
import org.bhp.bbs.dao.impl.SpringDao;
import org.apache.struts2.interceptor.ServletRequestAware;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Created by IntelliJ IDEA.
 * User: Ivan
 * Date: 2009-1-2
 * Time: 15:46:40
 */
public class EditPostAction extends ActionSupport implements ServletRequestAware {

    private SpringDao<Post> dao;

    private HttpServletRequest request;

    public void setDao(org.bhp.bbs.dao.impl.SpringDao dao) {
        this.dao = dao;
    }

    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String execute() {
        String id = request.getParameter("id");
        String edit = request.getParameter("edit");
        Post post = dao.find(Post.class, id);
        request.setAttribute("post", post);
        if (edit.trim().equals("true")) {
            request.setAttribute("edit", true);
        } else {
            request.setAttribute("edit", false);
        }
        return SUCCESS;
    }
}
