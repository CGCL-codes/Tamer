package com.bbs.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.bbs.entity.Article;
import com.bbs.entity.Blocks;
import com.bbs.entity.User;
import com.bbs.service.ArticleService;
import com.bbs.entity.Moderator;
import com.bbs.service.BlocksService;
import com.bbs.service.CollectionService;
import com.bbs.service.ModeratorService;
import com.bbs.util.ChangeUtil;
import com.bbs.service.ReplyService;
import com.bbs.service.UserService;
import com.bbs.util.Page;

public class BlocksServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");
        if (method == null || "".equals(method)) {
            index(request, response);
        } else if ("manageblocks".equals(method)) {
            manageblocks(request, response);
        } else if ("show".equals(method)) {
            show(request, response);
        } else if ("delete".equals(method)) {
            delete(request, response);
        } else {
            response.sendError(404);
        }
    }

    /**
	 * 通过id删除帖子
	 * @param request
	 * @param response
	 * @throws IOException
	 */
    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        List<Article> alist = new ArticleService().findByBlocksId(Integer.parseInt(id));
        for (Article a : alist) {
            new CollectionService().deleteByAid(a.getId());
            new ReplyService().deleteByAid(a.getId());
            new ArticleService().deleteById(a.getId());
        }
        List<Integer> uidlist = new ModeratorService().findMlistByBid(Integer.parseInt(id));
        for (int uid : uidlist) {
            User u = new UserService().findByid(uid);
            u.setRole(1);
            new UserService().update(u);
        }
        new ModeratorService().deleteBybid(Integer.parseInt(id));
        new BlocksService().deleteById(Integer.parseInt(id));
        response.sendRedirect("blocks.jspx?_method=manageblocks");
    }

    /**
	 * 板块显示
	 * @param request
	 * @param response
	 * @throws IOException
	 */
    private void show(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String pid = request.getParameter("page");
        BlocksService blocksService = new BlocksService();
        ChangeUtil cu = new ChangeUtil();
        int p = cu.changeStringToInt(pid);
        int bid = cu.changeStringToInt(id);
        Page page = new Page();
        page.setPageSize(10);
        page.setTotleSize(blocksService.getCount(Integer.parseInt(id)));
        page.setCurrentPage(p);
        if (p <= 0 || bid <= 0 || p > page.getTotlePages()) {
            response.sendRedirect("index.jspx?_method=showblocks&id=0&page=0");
        } else {
            Blocks blocks = blocksService.findByIdAndPage(id, page);
            if (blocks == null) {
                response.sendError(404);
            } else {
                HttpSession session = request.getSession();
                ModeratorService ms = new ModeratorService();
                List<Moderator> listM = ms.findByBid(Integer.valueOf(id));
                User u = (User) session.getAttribute("user");
                if (listM != null && u != null) {
                    if (u.getRole() == 0 || u.getRole() == 2) {
                        Map<String, String> map = ms.getByBlist(listM);
                        System.out.println(map.get(String.valueOf(u.getId())));
                        if (id.equals(map.get(String.valueOf(u.getId())))) {
                            u.setRole(2);
                            session.setAttribute("user", u);
                        } else {
                            u.setRole(0);
                            session.setAttribute("user", u);
                        }
                    }
                }
                session.setAttribute("listM", listM);
                session.setAttribute("blocks", blocks);
                session.setAttribute("page", page);
                response.sendRedirect("index.jspx?_method=showblocks&id=" + id + "&page=" + p);
            }
        }
    }

    /**
	 * 板块管理
	 * @param request
	 * @param response
	 * @throws IOException
	 */
    private void manageblocks(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Blocks> blist = new BlocksService().findBlockslist();
        HttpSession session = request.getSession();
        session.setAttribute("blockslist", blist);
        response.sendRedirect("index.jspx?_method=manageblocks");
    }

    /**
	 * 主页板块查询显示跳转
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
    private void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Blocks> list = new BlocksService().findAll();
        List<Article> anclist = new ArticleService().findAnc();
        HttpSession session = request.getSession();
        session.setAttribute("ancList", anclist);
        session.setAttribute("blocksList", list);
        response.sendRedirect("index.jspx?_method=showmain");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
