package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import bean.Goods;
import bean.User;
import dao.NewGoods;
import dao.NewGoodsUserSelect;

/**
 * Servlet implementation class NewGoodsServlet
 * 增加商品，将上个页面取到的值存入数据库，然后跳入商品展示页面，显示新增加的物品
 */
public class NewGoodsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
	 * @see HttpServlet#HttpServlet()
	 */
    public NewGoodsServlet() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String goodsnameString = new String(request.getParameter("goodsname").getBytes("iso-8859-1"), "UTF-8");
        String goodssortString = new String(request.getParameter("goodssort").getBytes("iso-8859-1"), "UTF-8");
        String goodspriceString = request.getParameter("goodsprice");
        float goodsprice = Float.valueOf(goodspriceString);
        String goodspictureString = new String(request.getParameter("goodspicture").getBytes("iso-8859-1"), "UTF-8");
        String goodsinforString = new String(request.getParameter("goodsinfor").getBytes("iso-8859-1"), "UTF-8");
        HttpServletRequest httpServletRequest = request;
        HttpSession session = httpServletRequest.getSession();
        String numString = (String) session.getAttribute("usernum");
        User user;
        user = NewGoodsUserSelect.getUser(numString);
        int userid = user.getUserId();
        Goods goods;
        goods = NewGoods.newGood(goodsnameString, goodssortString, goodsprice, goodspictureString, goodsinforString, userid);
        response.sendRedirect("/CampusTrade/view/detail.do?id=" + goods.getGoodsId());
        return;
    }
}
