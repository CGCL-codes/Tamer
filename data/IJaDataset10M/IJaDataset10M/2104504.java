package com.kongur.network.erp.manager.tc.impl;

import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eyeieye.melody.util.Money;
import com.eyeieye.melody.util.StringUtil;
import com.kongur.network.erp.domain.tc.TradeOrderDO;
import com.kongur.network.erp.domain.tc.TradeOrderLogisticsDO;
import com.kongur.network.erp.domain.tc.TradeOrderPayDO;
import com.kongur.network.erp.domain.tc.TradeOrderRefundDO;
import com.kongur.network.erp.enums.EnumPlatform;
import com.kongur.network.erp.enums.tc.OrderAttribute;
import com.kongur.network.erp.enums.tc.OrderStructure;
import com.kongur.network.erp.enums.tc.RefundStatus;
import com.kongur.network.erp.enums.tc.taobao.TaobaoOrderStatus;
import com.kongur.network.erp.enums.tc.taobao.TaobaoRefundStatus;
import com.kongur.network.erp.manager.tc.TradeOrderConvertor;
import com.kongur.network.erp.manager.tc.TradeOrderManager;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.PromotionDetail;
import com.taobao.api.domain.Refund;
import com.taobao.api.domain.Trade;

/**
 * �Ա�����ת����
 * 
 * @author zhengwei
 */
@Service("taobaoTradeOrderConvertor")
public class TaobaoTradeOrderConvertor implements TradeOrderConvertor<Trade, Refund> {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private TradeOrderManager tradeOrderManager;

    @Override
    public TradeOrderDO convert(Trade trade) {
        if (trade == null) {
            return null;
        }
        TradeOrderDO tradeOrder = new TradeOrderDO();
        tradeOrder.setOutOrderId(trade.getTid());
        tradeOrder.setSellerNick(trade.getSellerNick());
        tradeOrder.setBuyerNick(trade.getBuyerNick());
        tradeOrder.setSellerRate((trade.getSellerRate() != null && trade.getSellerRate()) ? 1 : 0);
        tradeOrder.setBuyerRate((trade.getBuyerRate() != null && trade.getBuyerRate()) ? 1 : 0);
        tradeOrder.setPlatformId(EnumPlatform.TAOBAO.getCode());
        tradeOrder.setMergeType(0);
        Long actualTotalFee = null;
        if (StringUtil.isNotBlank(trade.getPayment())) {
            actualTotalFee = new Money(trade.getPayment()).getCent();
        }
        tradeOrder.setActualTotalFee(actualTotalFee);
        tradeOrder.setBuyerFlag(trade.getBuyerFlag());
        tradeOrder.setBuyerMemo(trade.getBuyerMemo());
        tradeOrder.setBuyerMessage(trade.getBuyerMessage());
        tradeOrder.setBuyerNick(trade.getBuyerNick());
        tradeOrder.setCanRate((trade.getCanRate() != null && trade.getCanRate()) ? 1 : 0);
        tradeOrder.setCreatedTime(trade.getCreated());
        tradeOrder.setEndTime(trade.getEndTime());
        tradeOrder.setInvoiceName(trade.getInvoiceName());
        tradeOrder.setIs3D((trade.getIs3D() != null && trade.getIs3D()) ? 1 : 0);
        tradeOrder.setPromotion(trade.getPromotion());
        if (trade.getPromotionDetails() != null && !trade.getPromotionDetails().isEmpty()) {
            String jsonStr = JSONArray.fromObject(trade.getPromotionDetails()).toString();
            tradeOrder.addAttribute(OrderAttribute.PROMOTION_DETAIL, jsonStr);
        }
        tradeOrder.setSellerFlag(trade.getSellerFlag());
        tradeOrder.setSellerMemo(trade.getSellerMemo());
        tradeOrder.setSellerRate((trade.getSellerRate() != null && trade.getSellerRate()) ? 1 : 0);
        tradeOrder.setSnapshot(trade.getSnapshot());
        tradeOrder.setSnapshotUrl(trade.getSnapshotUrl());
        tradeOrder.setOrderStatus(TaobaoOrderStatus.getOrderStatus(trade.getStatus()));
        tradeOrder.addAttribute(OrderAttribute.OUT_TRADE_STATUS, trade.getStatus());
        tradeOrder.setOutOrderId(trade.getTid());
        tradeOrder.setTimeoutActionTime(trade.getTimeoutActionTime());
        tradeOrder.setTradeFrom(trade.getTradeFrom());
        tradeOrder.setOrderType(trade.getType());
        tradeOrder.addAttribute(OrderAttribute.ALIPAY_URL, trade.getAlipayUrl());
        tradeOrder.setTradeOrderPayDO(createTradeOrderPayDO(trade));
        tradeOrder.setTradeOrderLogisticsDO(createLogisticsDO(trade));
        tradeOrder.addAttribute(OrderAttribute.POST_FEE, trade.getPostFee());
        tradeOrder.addAttribute(OrderAttribute.SHIPPING_TYPE, trade.getShippingType());
        if (trade.getOrders() != null && !trade.getOrders().isEmpty()) {
            if (trade.getOrders().size() > 1) {
                tradeOrder.setOrderStructure(OrderStructure.MAIN_ORDER.getType());
                long buyAmount = 0;
                for (Order order : trade.getOrders()) {
                    TradeOrderDO subOrder = convert2SubTradeOrderDO(trade, order);
                    buyAmount += order.getNum();
                    tradeOrder.addSubTradeOrder(subOrder);
                }
                tradeOrder.setItemNum(buyAmount);
            } else if (trade.getOrders().size() == 1) {
                tradeOrder.setOrderStructure(OrderStructure.MAIN_AND_SUB_ORDER.getType());
                fillMainAndSubOrderData(tradeOrder, trade.getOrders().get(0));
            }
        }
        return tradeOrder;
    }

    /**
     * ������������Ϊ���Ӷ�������������Ʒ���
     * 
     * @param tradeOrder
     * @param order
     */
    private void fillMainAndSubOrderData(TradeOrderDO tradeOrder, Order order) {
        tradeOrder.addAttribute(OrderAttribute.ADJUST_FEE, order.getAdjustFee());
        tradeOrder.setBuyerRate((order.getBuyerRate() != null && order.getBuyerRate()) ? 1 : 0);
        tradeOrder.setOutCateId(order.getCid());
        Long discountFee = null;
        if (StringUtil.isNotBlank(order.getDiscountFee())) {
            discountFee = new Money(order.getDiscountFee()).getCent();
        }
        tradeOrder.setDiscountFee(discountFee);
        tradeOrder.setIsOversold((order.getIsOversold() != null && order.getIsOversold()) ? 1 : 0);
        tradeOrder.setIsServiceOrder((order.getIsServiceOrder() != null && order.getIsServiceOrder()) ? 1 : 0);
        tradeOrder.setItemMealId(order.getItemMealId());
        tradeOrder.setItemMealName(order.getItemMealName());
        tradeOrder.setItemNum(order.getNum());
        tradeOrder.setOutItemId(String.valueOf(order.getNumIid()));
        tradeOrder.setItemTitle(order.getTitle());
        tradeOrder.setItemPic(order.getPicPath());
        tradeOrder.setItemPrice(new Money(order.getPrice()).getCent());
        tradeOrder.addAttribute(OrderAttribute.OUTER_IID, order.getOuterIid());
        if (order.getOid() != null) {
            tradeOrder.addAttribute(OrderAttribute.OID, order.getOid().toString());
        }
        tradeOrder.setActualTotalFee(new Money(order.getPayment()).getCent());
        tradeOrder.setRefundId(order.getRefundId());
        tradeOrder.setRefundStatus(RefundStatus.getStatusByTaobaoRefundStatus(order.getRefundStatus()));
        tradeOrder.setSellerRate((order.getSellerRate() != null && order.getSellerRate()) ? 1 : 0);
        tradeOrder.addAttribute(OrderAttribute.SELLER_TYPE, order.getSellerType());
        tradeOrder.setOutSkuId(order.getSkuId());
        tradeOrder.addAttribute(OrderAttribute.SKU_PROPERTIES_NAME, order.getSkuPropertiesName());
        tradeOrder.setSnapshot(order.getSnapshot());
        tradeOrder.setSnapshotUrl(order.getSnapshotUrl());
        tradeOrder.setTimeoutActionTime(order.getTimeoutActionTime());
        tradeOrder.setModifiedTime(order.getModified());
        tradeOrder.addAttribute(OrderAttribute.TOTAL_FEE, order.getTotalFee());
    }

    /**
     * �����Ӷ���
     * 
     * @param trade
     * @param order
     * @return
     */
    private TradeOrderDO convert2SubTradeOrderDO(Trade trade, Order order) {
        TradeOrderDO subOrder = new TradeOrderDO();
        subOrder.setPlatformId(EnumPlatform.TAOBAO.getCode());
        subOrder.setOrderStructure(OrderStructure.SUB_ORDER.getType());
        subOrder.addAttribute(OrderAttribute.ADJUST_FEE, order.getAdjustFee());
        subOrder.setBuyerNick(order.getBuyerNick());
        subOrder.setBuyerRate((order.getBuyerRate() != null && order.getBuyerRate()) ? 1 : 0);
        subOrder.setOutCateId(order.getCid());
        Long discountFee = null;
        if (StringUtil.isNotBlank(order.getDiscountFee())) {
            discountFee = new Money(order.getDiscountFee()).getCent();
        }
        subOrder.setDiscountFee(discountFee);
        subOrder.setIsOversold((order.getIsOversold() != null && order.getIsOversold()) ? 1 : 0);
        subOrder.setIsServiceOrder((order.getIsServiceOrder() != null && order.getIsServiceOrder()) ? 1 : 0);
        subOrder.setItemMealId(order.getItemMealId());
        subOrder.setItemMealName(order.getItemMealName());
        subOrder.setModifiedTime(order.getModified());
        subOrder.setItemNum(order.getNum());
        subOrder.setOutItemId(String.valueOf(order.getNumIid()));
        subOrder.setItemTitle(order.getTitle());
        subOrder.setItemPic(order.getPicPath());
        subOrder.setItemPrice(new Money(order.getPrice()).getCent());
        subOrder.addAttribute(OrderAttribute.OUTER_IID, order.getOuterIid());
        subOrder.setOutOrderId(order.getOid());
        subOrder.setActualTotalFee(new Money(order.getPayment()).getCent());
        subOrder.setRefundId(order.getRefundId());
        subOrder.setRefundStatus(RefundStatus.getStatusByTaobaoRefundStatus(order.getRefundStatus()));
        subOrder.setSellerNick(order.getSellerNick());
        subOrder.setSellerRate((order.getSellerRate() != null && order.getSellerRate()) ? 1 : 0);
        subOrder.addAttribute(OrderAttribute.SELLER_TYPE, order.getSellerType());
        subOrder.setOutSkuId(order.getSkuId());
        subOrder.addAttribute(OrderAttribute.SKU_PROPERTIES_NAME, order.getSkuPropertiesName());
        subOrder.setSnapshot(order.getSnapshot());
        subOrder.setSnapshotUrl(order.getSnapshotUrl());
        subOrder.setOrderStatus(TaobaoOrderStatus.getOrderStatus(order.getStatus()));
        subOrder.setTimeoutActionTime(order.getTimeoutActionTime());
        subOrder.addAttribute(OrderAttribute.TOTAL_FEE, order.getTotalFee());
        List<PromotionDetail> pdList = getCurrOrderPromoDetail(trade, order);
        if (pdList != null && !pdList.isEmpty()) {
            String jsonStr = JSONArray.fromObject(pdList).toString();
            subOrder.addAttribute(OrderAttribute.PROMOTION_DETAIL, jsonStr);
        }
        return subOrder;
    }

    /**
     * ��ȡ��ǰ�����Ż���ϸ
     * 
     * @param trade
     * @param order
     * @return
     */
    private List<PromotionDetail> getCurrOrderPromoDetail(Trade trade, Order order) {
        if (trade.getPromotionDetails() != null && !trade.getPromotionDetails().isEmpty()) {
            List<PromotionDetail> subOrderPromoList = new ArrayList<PromotionDetail>();
            for (PromotionDetail pd : trade.getPromotionDetails()) {
                if (pd.getId().longValue() == order.getOid().longValue()) {
                    subOrderPromoList.add(pd);
                }
            }
            return subOrderPromoList;
        }
        return null;
    }

    /**
     * �����ڲ��������
     * 
     * @param trade
     * @return
     */
    private TradeOrderLogisticsDO createLogisticsDO(Trade trade) {
        TradeOrderLogisticsDO logistics = new TradeOrderLogisticsDO();
        logistics.setPlatformId(EnumPlatform.TAOBAO.getCode());
        logistics.setOutOrderId(trade.getTid());
        logistics.setCodStatus(trade.getCodStatus());
        logistics.setConsignTime(trade.getConsignTime());
        logistics.setCodFee(new Money(trade.getCodFee()).getCent());
        logistics.setLgType((trade.getIsLgtype() != null && trade.getIsLgtype()) ? 1 : 0);
        Long postFee = null;
        if (trade.getPostFee() != null) {
            postFee = new Money(trade.getPostFee()).getCent();
        }
        logistics.setPostFee(postFee);
        logistics.setReceiverAddress(trade.getReceiverAddress());
        logistics.setReceiverState(trade.getReceiverState());
        logistics.setReceiverCity(trade.getReceiverCity());
        logistics.setReceiverDistrict(trade.getReceiverDistrict());
        logistics.setReceiverMobile(trade.getReceiverMobile());
        logistics.setReceiverName(trade.getReceiverName());
        logistics.setReceiverPhone(trade.getReceiverPhone());
        logistics.setReceiverZip(trade.getReceiverZip());
        logistics.setShippingType(trade.getShippingType());
        return logistics;
    }

    /**
     * �����ڲ�������
     * 
     * @param trade
     * @return
     */
    private TradeOrderPayDO createTradeOrderPayDO(Trade trade) {
        TradeOrderPayDO pay = new TradeOrderPayDO();
        pay.setPlatformId(EnumPlatform.TAOBAO.getCode());
        pay.setOutOrderId(trade.getTid());
        if (StringUtil.isNotBlank(trade.getTotalFee())) {
            pay.setTotalFee(new Money(trade.getTotalFee()).getCent());
        }
        if (StringUtil.isNotBlank(trade.getAdjustFee())) {
            pay.setAdjustFee(new Money(trade.getAdjustFee()).getCent());
        }
        pay.setAlipayBuyerId(trade.getAlipayId());
        pay.setOutPayId(trade.getAlipayNo());
        pay.setAlipayWarnMsg(trade.getAlipayWarnMsg());
        if (StringUtil.isNotBlank(trade.getAvailableConfirmFee())) {
            pay.setAvailableConfirmFee(new Money(trade.getAvailableConfirmFee()).getCent());
        }
        pay.setBuyerAlipayNo(trade.getBuyerAlipayNo());
        if (StringUtil.isNotBlank(trade.getBuyerCodFee())) {
            pay.setBuyerCodFee(new Money(trade.getBuyerCodFee()).getCent());
        }
        if (StringUtil.isNotBlank(trade.getCodFee())) {
            pay.setCodFee(new Money(trade.getCodFee()).getCent());
        }
        pay.setObtainPoint(trade.getBuyerObtainPointFee());
        Long discountFee = null;
        if (trade.getDiscountFee() != null) {
            discountFee = new Money(trade.getDiscountFee()).getCent();
        }
        pay.setDiscountFee(discountFee);
        pay.setEndTime(trade.getEndTime());
        Long expressAgencyFee = null;
        if (trade.getExpressAgencyFee() != null) {
            expressAgencyFee = new Money(trade.getExpressAgencyFee()).getCent();
        }
        pay.setExpressAgencyFee(expressAgencyFee);
        pay.setHasPostFee((trade.getHasPostFee() != null && trade.getHasPostFee()) ? 1 : 0);
        pay.setPayTime(trade.getPayTime());
        Long actualTotalFee = null;
        if (trade.getPayment() != null) {
            actualTotalFee = new Money(trade.getPayment()).getCent();
        }
        pay.setActualTotalFee(actualTotalFee);
        pay.setPointFee(trade.getPointFee());
        pay.setRealPointFee(trade.getRealPointFee());
        Long confirmPaidFee = null;
        if (trade.getReceivedPayment() != null) {
            confirmPaidFee = new Money(trade.getReceivedPayment()).getCent();
        }
        pay.setConfirmPaidFee(confirmPaidFee);
        pay.setAlipaySellerId(trade.getSellerAlipayNo());
        Long sellerCodFee = null;
        if (trade.getSellerCodFee() != null) {
            sellerCodFee = new Money(trade.getSellerCodFee()).getCent();
        }
        pay.setSellerCodFee(sellerCodFee);
        pay.setSellerEmail(trade.getSellerEmail());
        pay.setBuyerEmail(trade.getBuyerEmail());
        pay.setBuyerArea(trade.getBuyerArea());
        return pay;
    }

    @Override
    public TradeOrderRefundDO convert2TradeOrderRefundDO(Refund refund) {
        TradeOrderRefundDO orderRefund = new TradeOrderRefundDO();
        orderRefund.setPlatformId(EnumPlatform.TAOBAO.getCode());
        orderRefund.setSellerAddress(refund.getAddress());
        orderRefund.setAlipayNo(refund.getAlipayNo());
        orderRefund.setBuyerNick(refund.getBuyerNick());
        orderRefund.setLogiCompanyName(refund.getCompanyName());
        orderRefund.setGmtCreate(refund.getCreated());
        orderRefund.setDescription(refund.getDesc());
        orderRefund.setGoodReturnTime(refund.getGoodReturnTime());
        orderRefund.setGoodStatus(refund.getGoodStatus());
        orderRefund.setNeedGoodReturn((refund.getHasGoodReturn() != null && refund.getHasGoodReturn()) ? 1 : 0);
        orderRefund.addAttribute(OrderAttribute.ITEM_STR_ID, refund.getIid());
        orderRefund.setModifiedTime(refund.getModified());
        orderRefund.setItemNum(refund.getNum());
        orderRefund.setItemId(refund.getNumIid());
        orderRefund.setOutSubOrderId(refund.getOid());
        orderRefund.setOrderStatus(TaobaoOrderStatus.getOrderStatus(refund.getOrderStatus()));
        orderRefund.addAttribute(OrderAttribute.OUT_TRADE_STATUS, refund.getOrderStatus());
        if (StringUtil.isNotBlank(refund.getPayment())) {
            orderRefund.setPayment(new Money(refund.getPayment()).getCent());
        }
        if (StringUtil.isNotBlank(refund.getPrice())) {
            orderRefund.setItemPrice(new Money(refund.getPrice()).getCent());
        }
        orderRefund.setReason(refund.getReason());
        if (StringUtil.isNotBlank(refund.getRefundFee())) {
            orderRefund.setRefundFee(new Money(refund.getRefundFee()).getCent());
        }
        orderRefund.setRefundId(refund.getRefundId());
        orderRefund.setSellerNick(refund.getSellerNick());
        orderRefund.setShippingType(refund.getShippingType());
        orderRefund.setShippingNo(refund.getSid());
        orderRefund.setRefundStatus(TaobaoRefundStatus.getRefundStatus(refund.getStatus()));
        orderRefund.setOutTradeOrderId(refund.getTid());
        orderRefund.setItemTitle(refund.getTitle());
        if (StringUtil.isNotBlank(refund.getTotalFee())) {
            orderRefund.setTotalFee(new Money(refund.getTotalFee()).getCent());
        }
        if (refund.getRefundRemindTimeout() != null) {
            Boolean isExistTimeout = refund.getRefundRemindTimeout().getExistTimeout();
            orderRefund.setExistTimeout((isExistTimeout != null && isExistTimeout) ? 1 : 0);
            orderRefund.setRemindType(refund.getRefundRemindTimeout().getRemindType());
            orderRefund.setTimeout(refund.getRefundRemindTimeout().getTimeout());
        }
        TradeOrderDO tradeOrder = tradeOrderManager.getTradeOrderDO(refund.getSellerNick(), refund.getOid(), EnumPlatform.TAOBAO);
        if (tradeOrder != null) {
            if (refund.getTid().equals(refund.getOid())) {
                orderRefund.setTradeOrderId(tradeOrder.getId());
                orderRefund.setSubOrderId(tradeOrder.getId());
            } else {
                orderRefund.setTradeOrderId(tradeOrder.getParentId());
                orderRefund.setSubOrderId(tradeOrder.getId());
            }
        } else {
            logger.error("��ȡ�����ڲ��������[tradeOrderManager.getTradeOrderDO(sellerNick, outOrderid, platform)], sellerNick=" + refund.getSellerNick() + ", outOrderId=" + refund.getOid() + ", platform=" + EnumPlatform.TAOBAO.getCode());
        }
        return orderRefund;
    }
}
