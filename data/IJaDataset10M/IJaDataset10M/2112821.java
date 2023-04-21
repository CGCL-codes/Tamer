package servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import bean.Goods;
import dao.ListGoods;
import dao.LoginCheck;

/**
 * Servlet implementation class AdminLoginServlet
 * 管理员登陆
 */
public class AdminLoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminLoginServlet() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        String usernumString = request.getParameter("usernum");
        String passwordString = request.getParameter("password");
        if (LoginCheck.check(usernumString, passwordString)) {
            List<Goods> list;
            list = ListGoods.getListGoods("select * from goods where goods_state=1 order by goods_id desc limit 25");
            request.setAttribute("list", list);
            request.setAttribute("page", 1);
            httpSession.setAttribute("adminname", usernumString);
            request.getRequestDispatcher("/admin/view/index.jsp").forward(request, response);
            return;
        }
        request.getRequestDispatcher("fail.jsp").forward(request, response);
    }
}
