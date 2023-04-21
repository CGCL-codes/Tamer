package com.endigi.ceedws.order.web;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.endigi.ceedws.order.OrderService;
import com.endigi.ceedws.order.domain.Order;
import com.endigi.ceedws.order.domain.OrderDelayVO;
import com.endigi.ceedws.order.domain.OrderType;
import com.endigi.frame.base.genericdao.search.Search;
import com.endigi.frame.base.genericdao.search.SearchResult;
import com.endigi.frame.base.web.BaseController;
import com.endigi.frame.base.web.vo.GenericResponse;
import com.endigi.frame.base.web.vo.JqGridRequest;
import com.endigi.frame.base.web.vo.JqGridResponse;

/**
 * @author 于洋
 * @description 订单控制器
 * @filename OrderCtrl.java
 * @time 2011-9-8 下午11:32:30
 * @version 1.0
 */
@Controller
@RequestMapping(value = "/order")
public class OrderCtrl extends BaseController {

    @Resource
    private OrderService orderService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse saveOrder(@RequestBody Order order) {
        if (order.getOrderType() == OrderType.trail) {
            Search search = new Search(Order.class);
            search.addFilterEqual("user.id", order.getUser().getId());
            search.addFilterEqual("database.id", order.getDatabase().getId());
            search.addFilterEqual("orderType", order.getOrderType());
            SearchResult<Order> searchResult = orderService.searchOrder(search);
            if (searchResult.getTotalCount() > 0) {
                return getFailureResponse("您已经试用过该数据库，无法再次试用，请选择购买。");
            }
        }
        orderService.saveOrder(order);
        return getSuccessResponse("保存订单成功");
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public JqGridResponse getOrderByUser(@PathVariable String userId, JqGridRequest jqGridRequest) {
        Search search = jqGridRequest.getSearch(Order.class);
        search.addFilterEqual("user.id", userId);
        SearchResult<Order> searchResult = orderService.searchOrder(search);
        return jqGridRequest.getJqGridResponse(searchResult);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public JqGridResponse getAllOrder(JqGridRequest jqGridRequest) {
        Search search = jqGridRequest.getSearch(Order.class);
        SearchResult<Order> searchResult = orderService.searchOrder(search);
        return jqGridRequest.getJqGridResponse(searchResult);
    }

    @RequestMapping(value = "/{id}/pass", method = RequestMethod.PUT)
    @ResponseBody
    public GenericResponse passOrder(@RequestBody Order order) {
        orderService.passOrder(order);
        return getSuccessResponse("更新成功");
    }

    @RequestMapping(value = "/{id}/depass", method = RequestMethod.PUT)
    @ResponseBody
    public GenericResponse depassOrder(@RequestBody Order order) {
        orderService.updateOrder(order);
        return getSuccessResponse("更新成功");
    }

    @RequestMapping(value = "/{id}/delay", method = RequestMethod.PUT)
    @ResponseBody
    public GenericResponse delayOrder(@RequestBody OrderDelayVO vo) {
        orderService.delayOrder(vo);
        return getSuccessResponse("延期成功");
    }
}
