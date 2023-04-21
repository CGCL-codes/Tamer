package com.nodeshop.service;

import com.nodeshop.bean.Pager;
import com.nodeshop.entity.Member;
import com.nodeshop.entity.Order;

/**
 * Service接口 - 订单
 
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 
 
 
 
 
 * KEY: nodeshopC25D50D18A27A8E1B4A11F7643DAA055
 
 */
public interface OrderService extends BaseService<Order, String> {

    /**
	 * 获取最后生成的订单编号
	 * 
	 * @return 订单编号
	 */
    public String getLastOrderSn();

    /**
	 * 根据Member、Pager获取订单分页对象
	 * 
	 * @param member
	 *            Member对象
	 *            
	 * @param pager
	 *            Pager对象
	 *            
	 * @return 订单分页对象
	 */
    public Pager getOrderPager(Member member, Pager pager);

    /**
	 * 获取未处理订单数
	 *            
	 * @return 未处理订单数
	 */
    public Long getUnprocessedOrderCount();

    /**
	 * 获取已支付未发货订单数（不包含已完成或已作废订单）
	 *            
	 * @return 已支付未发货订单数
	 */
    public Long getPaidUnshippedOrderCount();
}
