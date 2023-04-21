package org.blogsomy.controller.admin;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.blogsomy.controller.BaseController;
import com.google.code.lightsomy.annotations.Action;
import com.google.code.lightsomy.annotations.Controller;

@Controller
public class PostController extends BaseController {

    @Action
    public void edit(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.getWriter().append("edit").flush();
    }

    @Action
    public void delete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.getWriter().append("delete").flush();
    }

    @Action
    public void read(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.getWriter().append("read").flush();
    }

    @Action
    public void list(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.getWriter().append("list").flush();
    }
}
