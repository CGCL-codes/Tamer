package servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import bean.Goods;
import dao.GetGoodsCount;
import dao.ListGoods;

/**
 * Servlet implementation class SelectListServlet
 * list查询和分页
 */
public class SelectListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
	 * @see HttpServlet#HttpServlet()
	 */
    public SelectListServlet() {
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
        String pageString = request.getParameter("page");
        String nameString = request.getParameter("goodsname");
        String sortString = request.getParameter("goodssort");
        String lowpriceString = request.getParameter("lowprice");
        String highpriceString = request.getParameter("highprice");
        if (lowpriceString.equals("")) lowpriceString = "0";
        if (highpriceString.equals("")) highpriceString = "100000";
        int page = Integer.parseInt(pageString);
        String name = new String(nameString.getBytes("iso-8859-1"), "UTF-8");
        String sort = new String(sortString.getBytes("iso-8859-1"), "UTF-8");
        float low = Float.parseFloat(lowpriceString);
        float high = Float.parseFloat(highpriceString);
        if (low > high) {
            float temp = low;
            low = high;
            high = temp;
        }
        int count = GetGoodsCount.getCount("SELECT count(*) FROM goods WHERE goods_state=1 AND goods_sort LIKE \"%" + sort + "%\" AND goods_name LIKE \"%" + name + "%\" AND goods_price<=" + high + " AND goods_price>=" + low);
        int pagecount = (count - 1) / 20 + 1;
        if (page == -1 || page > pagecount) page = pagecount;
        if (page == 0) page = 1;
        List<Goods> list;
        list = ListGoods.getListGoods("SELECT * FROM goods WHERE goods_state=1 AND goods_sort LIKE \"%" + sort + "%\" AND goods_name LIKE \"%" + name + "%\" AND goods_price<=" + high + " AND goods_price>=" + low + " order by goods_id desc LIMIT " + (page - 1) * 20 + ",20");
        request.setAttribute("tenten", list);
        request.setAttribute("page", page);
        request.setAttribute("goodsname", name);
        request.setAttribute("goodssort", sort);
        request.setAttribute("lowprice", low);
        request.setAttribute("highprice", high);
        request.getRequestDispatcher("/view/list.jsp").forward(request, response);
        return;
    }
}
