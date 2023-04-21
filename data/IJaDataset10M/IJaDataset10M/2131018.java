package com.taobao.top.domain;

import java.util.Date;
import java.util.List;
import com.taobao.top.mapping.ApiClass;
import com.taobao.top.mapping.ApiField;
import com.taobao.top.mapping.ApiListClass;
import com.taobao.top.mapping.ApiListField;

/**
 * Trade Data Structure.
 *
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
@ApiClass("trade")
@ApiListClass("trades")
public class Trade extends BaseObject {

    private static final long serialVersionUID = 1L;

    @ApiField("adjust_fee")
    private String adjustFee;

    @ApiField("alipay_no")
    private String alipayNo;

    @ApiField("available_confirm_fee")
    private String availableConfirmFee;

    @ApiField("buyer_alipay_no")
    private String buyerAlipayNo;

    @ApiField("buyer_email")
    private String buyerEmail;

    @ApiField("buyer_flag")
    private Integer buyerFlag;

    @ApiField("buyer_ip")
    private String buyerIp;

    @ApiField("buyer_memo")
    private String buyerMemo;

    @ApiField("buyer_message")
    private String buyerMessage;

    @ApiField("buyer_nick")
    private String buyerNick;

    @ApiField("buyer_obtain_point_fee")
    private Integer buyerObtainPointFee;

    @ApiField("buyer_rate")
    private Boolean buyerRate;

    @ApiField("cod_fee")
    private String codFee;

    @ApiField("cod_status")
    private String codStatus;

    @ApiField("commission_fee")
    private String commissionFee;

    @ApiField("consign_time")
    private String consignTime;

    @ApiField("created")
    private Date created;

    @ApiField("discount_fee")
    private String discountFee;

    @ApiField("end_time")
    private String endTime;

    @ApiField("has_post_fee")
    private Boolean hasPostFee;

    @ApiField("iid")
    private String iid;

    @ApiField("invoice_name")
    private String invoiceName;

    @ApiField("is_3D")
    private Boolean is3D;

    @ApiField("modified")
    private Date modified;

    @ApiField("num")
    private Integer num;

    @ApiField("num_iid")
    private Long numIid;

    @ApiField("order")
    @ApiListField("orders")
    private List<Order> orders;

    @ApiField("pay_time")
    private Date payTime;

    @ApiField("payment")
    private String payment;

    @ApiField("pic_path")
    private String picPath;

    @ApiField("point_fee")
    private Integer pointFee;

    @ApiField("post_fee")
    private String postFee;

    @ApiField("price")
    private String price;

    @ApiField("promotion")
    private String promotion;

    @ApiField("promotion_detail")
    @ApiListField("promotion_details")
    private List<PromotionDetail> promotionDetails;

    @ApiField("real_point_fee")
    private Integer realPointFee;

    @ApiField("received_payment")
    private String receivedPayment;

    @ApiField("receiver_address")
    private String receiverAddress;

    private String receiverAddr;

    @ApiField("receiver_city")
    private String receiverCity;

    @ApiField("receiver_district")
    private String receiverDistrict;

    @ApiField("receiver_mobile")
    private String receiverMobile;

    @ApiField("receiver_name")
    private String receiverName;

    @ApiField("receiver_phone")
    private String receiverPhone;

    @ApiField("receiver_state")
    private String receiverState;

    @ApiField("receiver_zip")
    private String receiverZip;

    @ApiField("seller_alipay_no")
    private String sellerAlipayNo;

    @ApiField("seller_email")
    private String sellerEmail;

    @ApiField("seller_flag")
    private Integer sellerFlag;

    @ApiField("seller_memo")
    private String sellerMemo;

    @ApiField("seller_mobile")
    private String sellerMobile;

    @ApiField("seller_name")
    private String sellerName;

    @ApiField("seller_nick")
    private String sellerNick;

    @ApiField("seller_phone")
    private String sellerPhone;

    @ApiField("seller_rate")
    private Boolean sellerRate;

    @ApiField("shipping_type")
    private String shippingType;

    @ApiField("sid")
    private String sid;

    @ApiField("snapshot")
    private String snapshot;

    @ApiField("snapshot_url")
    private String snapshotUrl;

    @ApiField("status")
    private String status;

    @ApiField("tid")
    private Long tid;

    @ApiField("timeout_action_time")
    private String timeoutActionTime;

    @ApiField("title")
    private String title;

    @ApiField("total_fee")
    private String totalFee;

    @ApiField("trade_memo")
    private String tradeMemo;

    @ApiField("type")
    private String type;

    public String getAdjustFee() {
        return this.adjustFee;
    }

    public void setAdjustFee(String adjustFee) {
        this.adjustFee = adjustFee;
    }

    public String getAlipayNo() {
        return this.alipayNo;
    }

    public void setAlipayNo(String alipayNo) {
        this.alipayNo = alipayNo;
    }

    public String getAvailableConfirmFee() {
        return this.availableConfirmFee;
    }

    public void setAvailableConfirmFee(String availableConfirmFee) {
        this.availableConfirmFee = availableConfirmFee;
    }

    public String getBuyerAlipayNo() {
        return this.buyerAlipayNo;
    }

    public void setBuyerAlipayNo(String buyerAlipayNo) {
        this.buyerAlipayNo = buyerAlipayNo;
    }

    public String getBuyerEmail() {
        return this.buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public Integer getBuyerFlag() {
        return this.buyerFlag;
    }

    public void setBuyerFlag(Integer buyerFlag) {
        this.buyerFlag = buyerFlag;
    }

    public String getBuyerIp() {
        return this.buyerIp;
    }

    public void setBuyerIp(String buyerIp) {
        this.buyerIp = buyerIp;
    }

    public String getBuyerMemo() {
        return this.buyerMemo;
    }

    public void setBuyerMemo(String buyerMemo) {
        this.buyerMemo = buyerMemo;
    }

    public String getBuyerMessage() {
        return this.buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public String getBuyerNick() {
        return this.buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }

    public Integer getBuyerObtainPointFee() {
        return this.buyerObtainPointFee;
    }

    public void setBuyerObtainPointFee(Integer buyerObtainPointFee) {
        this.buyerObtainPointFee = buyerObtainPointFee;
    }

    public Boolean getBuyerRate() {
        return this.buyerRate;
    }

    public void setBuyerRate(Boolean buyerRate) {
        this.buyerRate = buyerRate;
    }

    public String getCodFee() {
        return this.codFee;
    }

    public void setCodFee(String codFee) {
        this.codFee = codFee;
    }

    public String getCodStatus() {
        return this.codStatus;
    }

    public void setCodStatus(String codStatus) {
        this.codStatus = codStatus;
    }

    public String getCommissionFee() {
        return this.commissionFee;
    }

    public void setCommissionFee(String commissionFee) {
        this.commissionFee = commissionFee;
    }

    public String getConsignTime() {
        return this.consignTime;
    }

    public void setConsignTime(String consignTime) {
        this.consignTime = consignTime;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getDiscountFee() {
        return this.discountFee;
    }

    public void setDiscountFee(String discountFee) {
        this.discountFee = discountFee;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Boolean getHasPostFee() {
        return this.hasPostFee;
    }

    public void setHasPostFee(Boolean hasPostFee) {
        this.hasPostFee = hasPostFee;
    }

    public String getIid() {
        return this.iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public String getInvoiceName() {
        return this.invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public Boolean getIs3D() {
        return this.is3D;
    }

    public void setIs3D(Boolean is3D) {
        this.is3D = is3D;
    }

    public Date getModified() {
        return this.modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Integer getNum() {
        return this.num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getNumIid() {
        return this.numIid;
    }

    public void setNumIid(Long numIid) {
        this.numIid = numIid;
    }

    public List<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Date getPayTime() {
        return this.payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getPayment() {
        return this.payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPicPath() {
        return this.picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public Integer getPointFee() {
        return this.pointFee;
    }

    public void setPointFee(Integer pointFee) {
        this.pointFee = pointFee;
    }

    public String getPostFee() {
        return this.postFee;
    }

    public void setPostFee(String postFee) {
        this.postFee = postFee;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPromotion() {
        return this.promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public List<PromotionDetail> getPromotionDetails() {
        return this.promotionDetails;
    }

    public void setPromotionDetails(List<PromotionDetail> promotionDetails) {
        this.promotionDetails = promotionDetails;
    }

    public Integer getRealPointFee() {
        return this.realPointFee;
    }

    public void setRealPointFee(Integer realPointFee) {
        this.realPointFee = realPointFee;
    }

    public String getReceivedPayment() {
        return this.receivedPayment;
    }

    public void setReceivedPayment(String receivedPayment) {
        this.receivedPayment = receivedPayment;
    }

    public String getReceiverAddress() {
        return this.receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverAddr() {
        return this.receiverAddr;
    }

    public void setReceiverAddr(String receiverAddr) {
        this.receiverAddr = receiverAddr;
    }

    public String getReceiverCity() {
        return this.receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverDistrict() {
        return this.receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    public String getReceiverMobile() {
        return this.receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverName() {
        return this.receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return this.receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverState() {
        return this.receiverState;
    }

    public void setReceiverState(String receiverState) {
        this.receiverState = receiverState;
    }

    public String getReceiverZip() {
        return this.receiverZip;
    }

    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip;
    }

    public String getSellerAlipayNo() {
        return this.sellerAlipayNo;
    }

    public void setSellerAlipayNo(String sellerAlipayNo) {
        this.sellerAlipayNo = sellerAlipayNo;
    }

    public String getSellerEmail() {
        return this.sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public Integer getSellerFlag() {
        return this.sellerFlag;
    }

    public void setSellerFlag(Integer sellerFlag) {
        this.sellerFlag = sellerFlag;
    }

    public String getSellerMemo() {
        return this.sellerMemo;
    }

    public void setSellerMemo(String sellerMemo) {
        this.sellerMemo = sellerMemo;
    }

    public String getSellerMobile() {
        return this.sellerMobile;
    }

    public void setSellerMobile(String sellerMobile) {
        this.sellerMobile = sellerMobile;
    }

    public String getSellerName() {
        return this.sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerNick() {
        return this.sellerNick;
    }

    public void setSellerNick(String sellerNick) {
        this.sellerNick = sellerNick;
    }

    public String getSellerPhone() {
        return this.sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public Boolean getSellerRate() {
        return this.sellerRate;
    }

    public void setSellerRate(Boolean sellerRate) {
        this.sellerRate = sellerRate;
    }

    public String getShippingType() {
        return this.shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getSid() {
        return this.sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSnapshot() {
        return this.snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public String getSnapshotUrl() {
        return this.snapshotUrl;
    }

    public void setSnapshotUrl(String snapshotUrl) {
        this.snapshotUrl = snapshotUrl;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTid() {
        return this.tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getTimeoutActionTime() {
        return this.timeoutActionTime;
    }

    public void setTimeoutActionTime(String timeoutActionTime) {
        this.timeoutActionTime = timeoutActionTime;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotalFee() {
        return this.totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getTradeMemo() {
        return this.tradeMemo;
    }

    public void setTradeMemo(String tradeMemo) {
        this.tradeMemo = tradeMemo;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
