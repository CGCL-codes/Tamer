package com.hilaver.dzmis.web.servlet.order;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hilaver.dzmis.Constants;
import com.hilaver.dzmis.basicinfo.BiDZColor;
import com.hilaver.dzmis.order.OrderYarn;
import com.hilaver.dzmis.order.OrderYarnLot;
import com.hilaver.dzmis.service.impl.OrderYarnServiceImpl;

/**
 * Servlet implementation class for Servlet: OrderServlet
 * 
 */
public class OrderYarnServlet extends com.hilaver.dzmis.web.servlet.AbstractBaseServlet implements javax.servlet.Servlet {

    static final long serialVersionUID = 1L;

    public static final int ACTION_GET_MAX_REFERENCE_INDEX = 10;

    public static final int ACTION_GET_LOT_ALL = 100;

    public static final int ACTION_EDIT_LOT = 101;

    public static final int ACTION_DELETE_LOT = 102;

    public static final int ACTION_GET_LOT = 103;

    private OrderYarn orderYarn;

    private OrderYarnLot orderYarnLot;

    private OrderYarnServiceImpl orderYarnService;

    public OrderYarnServlet() {
        super();
        this.orderYarnService = new OrderYarnServiceImpl();
        this.orderYarnService.setRealFileDir(UPLOAD_TEMP_REAL_DIR);
        this.fullName = OrderYarn.class.getName();
        this.simpleName = OrderYarn.class.getSimpleName();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        try {
            String outputStr = execute(request);
            if (this.action == ACTION_EDIT || this.action == ACTION_EDIT_LOT) {
                response.setContentType(Constants.HTML_RESPONSE_TYPE);
            }
            writer.write(outputStr);
        } catch (Exception e) {
            writer.print(e.getMessage());
            e.printStackTrace();
        }
        writer.close();
    }

    protected String execute(HttpServletRequest request) throws Exception {
        this.setAction(request);
        this.orderYarnService.setLocale((Locale) request.getSession().getAttribute(Constants.SESSION_LOCALE));
        super.setAcUser(request, this.orderYarnService);
        String rtn = "";
        switch(this.action) {
            case ACTOIN_GET_ALL_PAGINATION:
                break;
            case ACTION_EDIT:
                this.fillObj(request);
                this.orderYarnService.edit(this.orderYarn);
                break;
            case ACTION_DELETE:
                this.setId(request);
                rtn = this.orderYarnService.delete(this.id);
                break;
            case ACTION_GET:
                this.setId(request);
                rtn = this.orderYarnService.get(this.id);
                break;
            case ACTION_GET_WITH_DESCRIPTION:
                this.setId(request);
                rtn = this.orderYarnService.get(this.id);
                break;
            case ACTION_GET_ALL_PAGINATION_FILTER:
                this.setFilterParam(request);
                this.setPaginationParam(request);
                rtn = this.orderYarnService.getAllPagination(page, sort, dir, filters);
                break;
            case ACTION_GET_ALL:
                this.setId(request);
                rtn = this.orderYarnService.getAll(this.id);
                break;
            case ACTION_EDIT_LOT:
                this.fillLot(request);
                rtn = this.orderYarnService.edit(this.orderYarnLot);
                break;
            case ACTION_GET_LOT_ALL:
                this.setId(request);
                rtn = this.orderYarnService.getLotAll(this.id);
                break;
            case ACTION_DELETE_LOT:
                this.setId(request);
                rtn = this.orderYarnService.delete(OrderYarnLot.class.getName(), this.id);
                break;
            case ACTION_GET_LOT:
                this.setId(request);
                rtn = this.orderYarnService.getLot(id);
                break;
            case ACTION_GET_MAX_REFERENCE_INDEX:
                rtn = this.orderYarnService.getMaxReferenceIndex();
                break;
        }
        return rtn;
    }

    protected void fillObj(HttpServletRequest request) throws Exception {
        super.fillObj(request);
        this.orderYarn = (OrderYarn) this.obj;
        String dzColorIdStr = (String) getValue(request, "dzColorId");
        if (dzColorIdStr != null) {
            Integer dzColorId = Integer.parseInt(dzColorIdStr);
            BiDZColor dzColor = new BiDZColor();
            dzColor.setId(dzColorId);
            this.orderYarn.setBiDZColor(dzColor);
        }
    }

    protected void fillLot(HttpServletRequest request) throws Exception {
        this.setId(request);
        this.fillObj(OrderYarnLot.class.getName(), request);
        this.orderYarnLot = (OrderYarnLot) this.obj;
        this.orderYarnLot.setOrderYarn(new OrderYarn());
        String orderYarnId = (String) this.getValue(request, "orderYarnId");
        this.orderYarnLot.getOrderYarn().setId(Integer.parseInt(orderYarnId));
    }
}
