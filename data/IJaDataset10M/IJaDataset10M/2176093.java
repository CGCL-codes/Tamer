package com.ark.fix.model.fixml;

import com.ark.fix.model.*;

public class MDEntryGroup extends FIXMLAggregate {

    private MDEntryType _MDEntryType;

    private MDEntryPx _MDEntryPx;

    private Currency _Currency;

    private MDEntrySize _MDEntrySize;

    private MDEntryDate _MDEntryDate;

    private MDEntryTime _MDEntryTime;

    private TickDirection _TickDirection;

    private MDMkt _MDMkt;

    private TrdSesID _TrdSesID;

    private QuoteConditionList _QuoteConditionList;

    private TradeConditionList _TradeConditionList;

    private MDEntryOriginator _MDEntryOriginator;

    private MMLocationID _MMLocationID;

    private DeskID _DeskID;

    private OpenCloseSettFlag _OpenCloseSettFlag;

    private OrderDuration _OrderDuration;

    private ExpireDate _ExpireDate;

    private MinQty _MinQty;

    private ExecInstList _ExecInstList;

    private SellerDays _SellerDays;

    private OrderID _OrderID;

    private QuoteEntryID _QuoteEntryID;

    private MDEntryBuyer _MDEntryBuyer;

    private MDEntrySeller _MDEntrySeller;

    private MDNoOrders _MDNoOrders;

    private MDEntryPositionNo _MDEntryPositionNo;

    private Text _Text;

    private EncodedTextGroup _EncodedTextGroup;

    public MDEntryType getMDEntryType() {
        return _MDEntryType;
    }

    public void setMDEntryType(MDEntryType obj) {
        _MDEntryType = obj;
    }

    public void initMDEntryType(Object obj) throws ModelException {
        if (_MDEntryType != null) throw new ModelException("Value has already been initialized for MDEntryType.");
        setMDEntryType((MDEntryType) obj);
    }

    public MDEntryPx getMDEntryPx() {
        return _MDEntryPx;
    }

    public void setMDEntryPx(MDEntryPx obj) {
        _MDEntryPx = obj;
    }

    public void initMDEntryPx(Object obj) throws ModelException {
        if (_MDEntryPx != null) throw new ModelException("Value has already been initialized for MDEntryPx.");
        setMDEntryPx((MDEntryPx) obj);
    }

    public Currency getCurrency() {
        return _Currency;
    }

    public void setCurrency(Currency obj) {
        _Currency = obj;
    }

    public void initCurrency(Object obj) throws ModelException {
        if (_Currency != null) throw new ModelException("Value has already been initialized for Currency.");
        setCurrency((Currency) obj);
    }

    public MDEntrySize getMDEntrySize() {
        return _MDEntrySize;
    }

    public void setMDEntrySize(MDEntrySize obj) {
        _MDEntrySize = obj;
    }

    public void initMDEntrySize(Object obj) throws ModelException {
        if (_MDEntrySize != null) throw new ModelException("Value has already been initialized for MDEntrySize.");
        setMDEntrySize((MDEntrySize) obj);
    }

    public MDEntryDate getMDEntryDate() {
        return _MDEntryDate;
    }

    public void setMDEntryDate(MDEntryDate obj) {
        _MDEntryDate = obj;
    }

    public void initMDEntryDate(Object obj) throws ModelException {
        if (_MDEntryDate != null) throw new ModelException("Value has already been initialized for MDEntryDate.");
        setMDEntryDate((MDEntryDate) obj);
    }

    public MDEntryTime getMDEntryTime() {
        return _MDEntryTime;
    }

    public void setMDEntryTime(MDEntryTime obj) {
        _MDEntryTime = obj;
    }

    public void initMDEntryTime(Object obj) throws ModelException {
        if (_MDEntryTime != null) throw new ModelException("Value has already been initialized for MDEntryTime.");
        setMDEntryTime((MDEntryTime) obj);
    }

    public TickDirection getTickDirection() {
        return _TickDirection;
    }

    public void setTickDirection(TickDirection obj) {
        _TickDirection = obj;
    }

    public void initTickDirection(Object obj) throws ModelException {
        if (_TickDirection != null) throw new ModelException("Value has already been initialized for TickDirection.");
        setTickDirection((TickDirection) obj);
    }

    public MDMkt getMDMkt() {
        return _MDMkt;
    }

    public void setMDMkt(MDMkt obj) {
        _MDMkt = obj;
    }

    public void initMDMkt(Object obj) throws ModelException {
        if (_MDMkt != null) throw new ModelException("Value has already been initialized for MDMkt.");
        setMDMkt((MDMkt) obj);
    }

    public TrdSesID getTrdSesID() {
        return _TrdSesID;
    }

    public void setTrdSesID(TrdSesID obj) {
        _TrdSesID = obj;
    }

    public void initTrdSesID(Object obj) throws ModelException {
        if (_TrdSesID != null) throw new ModelException("Value has already been initialized for TrdSesID.");
        setTrdSesID((TrdSesID) obj);
    }

    public QuoteConditionList getQuoteConditionList() {
        return _QuoteConditionList;
    }

    public void setQuoteConditionList(QuoteConditionList obj) {
        _QuoteConditionList = obj;
    }

    public void initQuoteConditionList(Object obj) throws ModelException {
        if (_QuoteConditionList != null) throw new ModelException("Value has already been initialized for QuoteConditionList.");
        setQuoteConditionList((QuoteConditionList) obj);
    }

    public TradeConditionList getTradeConditionList() {
        return _TradeConditionList;
    }

    public void setTradeConditionList(TradeConditionList obj) {
        _TradeConditionList = obj;
    }

    public void initTradeConditionList(Object obj) throws ModelException {
        if (_TradeConditionList != null) throw new ModelException("Value has already been initialized for TradeConditionList.");
        setTradeConditionList((TradeConditionList) obj);
    }

    public MDEntryOriginator getMDEntryOriginator() {
        return _MDEntryOriginator;
    }

    public void setMDEntryOriginator(MDEntryOriginator obj) {
        _MDEntryOriginator = obj;
    }

    public void initMDEntryOriginator(Object obj) throws ModelException {
        if (_MDEntryOriginator != null) throw new ModelException("Value has already been initialized for MDEntryOriginator.");
        setMDEntryOriginator((MDEntryOriginator) obj);
    }

    public MMLocationID getMMLocationID() {
        return _MMLocationID;
    }

    public void setMMLocationID(MMLocationID obj) {
        _MMLocationID = obj;
    }

    public void initMMLocationID(Object obj) throws ModelException {
        if (_MMLocationID != null) throw new ModelException("Value has already been initialized for MMLocationID.");
        setMMLocationID((MMLocationID) obj);
    }

    public DeskID getDeskID() {
        return _DeskID;
    }

    public void setDeskID(DeskID obj) {
        _DeskID = obj;
    }

    public void initDeskID(Object obj) throws ModelException {
        if (_DeskID != null) throw new ModelException("Value has already been initialized for DeskID.");
        setDeskID((DeskID) obj);
    }

    public OpenCloseSettFlag getOpenCloseSettFlag() {
        return _OpenCloseSettFlag;
    }

    public void setOpenCloseSettFlag(OpenCloseSettFlag obj) {
        _OpenCloseSettFlag = obj;
    }

    public void initOpenCloseSettFlag(Object obj) throws ModelException {
        if (_OpenCloseSettFlag != null) throw new ModelException("Value has already been initialized for OpenCloseSettFlag.");
        setOpenCloseSettFlag((OpenCloseSettFlag) obj);
    }

    public OrderDuration getOrderDuration() {
        return _OrderDuration;
    }

    public void setOrderDuration(OrderDuration obj) {
        _OrderDuration = obj;
    }

    public void initOrderDuration(Object obj) throws ModelException {
        if (_OrderDuration != null) throw new ModelException("Value has already been initialized for OrderDuration.");
        setOrderDuration((OrderDuration) obj);
    }

    public ExpireDate getExpireDate() {
        return _ExpireDate;
    }

    public void setExpireDate(ExpireDate obj) {
        _ExpireDate = obj;
    }

    public void initExpireDate(Object obj) throws ModelException {
        if (_ExpireDate != null) throw new ModelException("Value has already been initialized for ExpireDate.");
        setExpireDate((ExpireDate) obj);
    }

    public MinQty getMinQty() {
        return _MinQty;
    }

    public void setMinQty(MinQty obj) {
        _MinQty = obj;
    }

    public void initMinQty(Object obj) throws ModelException {
        if (_MinQty != null) throw new ModelException("Value has already been initialized for MinQty.");
        setMinQty((MinQty) obj);
    }

    public ExecInstList getExecInstList() {
        return _ExecInstList;
    }

    public void setExecInstList(ExecInstList obj) {
        _ExecInstList = obj;
    }

    public void initExecInstList(Object obj) throws ModelException {
        if (_ExecInstList != null) throw new ModelException("Value has already been initialized for ExecInstList.");
        setExecInstList((ExecInstList) obj);
    }

    public SellerDays getSellerDays() {
        return _SellerDays;
    }

    public void setSellerDays(SellerDays obj) {
        _SellerDays = obj;
    }

    public void initSellerDays(Object obj) throws ModelException {
        if (_SellerDays != null) throw new ModelException("Value has already been initialized for SellerDays.");
        setSellerDays((SellerDays) obj);
    }

    public OrderID getOrderID() {
        return _OrderID;
    }

    public void setOrderID(OrderID obj) {
        _OrderID = obj;
    }

    public void initOrderID(Object obj) throws ModelException {
        if (_OrderID != null) throw new ModelException("Value has already been initialized for OrderID.");
        setOrderID((OrderID) obj);
    }

    public QuoteEntryID getQuoteEntryID() {
        return _QuoteEntryID;
    }

    public void setQuoteEntryID(QuoteEntryID obj) {
        _QuoteEntryID = obj;
    }

    public void initQuoteEntryID(Object obj) throws ModelException {
        if (_QuoteEntryID != null) throw new ModelException("Value has already been initialized for QuoteEntryID.");
        setQuoteEntryID((QuoteEntryID) obj);
    }

    public MDEntryBuyer getMDEntryBuyer() {
        return _MDEntryBuyer;
    }

    public void setMDEntryBuyer(MDEntryBuyer obj) {
        _MDEntryBuyer = obj;
    }

    public void initMDEntryBuyer(Object obj) throws ModelException {
        if (_MDEntryBuyer != null) throw new ModelException("Value has already been initialized for MDEntryBuyer.");
        setMDEntryBuyer((MDEntryBuyer) obj);
    }

    public MDEntrySeller getMDEntrySeller() {
        return _MDEntrySeller;
    }

    public void setMDEntrySeller(MDEntrySeller obj) {
        _MDEntrySeller = obj;
    }

    public void initMDEntrySeller(Object obj) throws ModelException {
        if (_MDEntrySeller != null) throw new ModelException("Value has already been initialized for MDEntrySeller.");
        setMDEntrySeller((MDEntrySeller) obj);
    }

    public MDNoOrders getMDNoOrders() {
        return _MDNoOrders;
    }

    public void setMDNoOrders(MDNoOrders obj) {
        _MDNoOrders = obj;
    }

    public void initMDNoOrders(Object obj) throws ModelException {
        if (_MDNoOrders != null) throw new ModelException("Value has already been initialized for MDNoOrders.");
        setMDNoOrders((MDNoOrders) obj);
    }

    public MDEntryPositionNo getMDEntryPositionNo() {
        return _MDEntryPositionNo;
    }

    public void setMDEntryPositionNo(MDEntryPositionNo obj) {
        _MDEntryPositionNo = obj;
    }

    public void initMDEntryPositionNo(Object obj) throws ModelException {
        if (_MDEntryPositionNo != null) throw new ModelException("Value has already been initialized for MDEntryPositionNo.");
        setMDEntryPositionNo((MDEntryPositionNo) obj);
    }

    public Text getText() {
        return _Text;
    }

    public void setText(Text obj) {
        _Text = obj;
    }

    public void initText(Object obj) throws ModelException {
        if (_Text != null) throw new ModelException("Value has already been initialized for Text.");
        setText((Text) obj);
    }

    public EncodedTextGroup getEncodedTextGroup() {
        return _EncodedTextGroup;
    }

    public void setEncodedTextGroup(EncodedTextGroup obj) {
        _EncodedTextGroup = obj;
    }

    public void initEncodedTextGroup(Object obj) throws ModelException {
        if (_EncodedTextGroup != null) throw new ModelException("Value has already been initialized for EncodedTextGroup.");
        setEncodedTextGroup((EncodedTextGroup) obj);
    }

    public String[] getProperties() {
        String[] properties = { "MDEntryType", "MDEntryPx", "Currency", "MDEntrySize", "MDEntryDate", "MDEntryTime", "TickDirection", "MDMkt", "TrdSesID", "QuoteConditionList", "TradeConditionList", "MDEntryOriginator", "MMLocationID", "DeskID", "OpenCloseSettFlag", "OrderDuration", "ExpireDate", "MinQty", "ExecInstList", "SellerDays", "OrderID", "QuoteEntryID", "MDEntryBuyer", "MDEntrySeller", "MDNoOrders", "MDEntryPositionNo", "Text", "EncodedTextGroup" };
        return properties;
    }

    public String[] getRequiredProperties() {
        String[] properties = {};
        return properties;
    }

    public String toFIXMessage() {
        StringBuffer sb = new StringBuffer("");
        if (_MDEntryType != null) sb.append(_MDEntryType.toFIXMessage());
        if (_MDEntryPx != null) sb.append(_MDEntryPx.toFIXMessage());
        if (_Currency != null) sb.append(_Currency.toFIXMessage());
        if (_MDEntrySize != null) sb.append(_MDEntrySize.toFIXMessage());
        if (_MDEntryDate != null) sb.append(_MDEntryDate.toFIXMessage());
        if (_MDEntryTime != null) sb.append(_MDEntryTime.toFIXMessage());
        if (_TickDirection != null) sb.append(_TickDirection.toFIXMessage());
        if (_MDMkt != null) sb.append(_MDMkt.toFIXMessage());
        if (_TrdSesID != null) sb.append(_TrdSesID.toFIXMessage());
        if (_QuoteConditionList != null) sb.append(_QuoteConditionList.toFIXMessage());
        if (_TradeConditionList != null) sb.append(_TradeConditionList.toFIXMessage());
        if (_MDEntryOriginator != null) sb.append(_MDEntryOriginator.toFIXMessage());
        if (_MMLocationID != null) sb.append(_MMLocationID.toFIXMessage());
        if (_DeskID != null) sb.append(_DeskID.toFIXMessage());
        if (_OpenCloseSettFlag != null) sb.append(_OpenCloseSettFlag.toFIXMessage());
        if (_OrderDuration != null) sb.append(_OrderDuration.toFIXMessage());
        if (_ExpireDate != null) sb.append(_ExpireDate.toFIXMessage());
        if (_MinQty != null) sb.append(_MinQty.toFIXMessage());
        if (_ExecInstList != null) sb.append(_ExecInstList.toFIXMessage());
        if (_SellerDays != null) sb.append(_SellerDays.toFIXMessage());
        if (_OrderID != null) sb.append(_OrderID.toFIXMessage());
        if (_QuoteEntryID != null) sb.append(_QuoteEntryID.toFIXMessage());
        if (_MDEntryBuyer != null) sb.append(_MDEntryBuyer.toFIXMessage());
        if (_MDEntrySeller != null) sb.append(_MDEntrySeller.toFIXMessage());
        if (_MDNoOrders != null) sb.append(_MDNoOrders.toFIXMessage());
        if (_MDEntryPositionNo != null) sb.append(_MDEntryPositionNo.toFIXMessage());
        if (_Text != null) sb.append(_Text.toFIXMessage());
        if (_EncodedTextGroup != null) sb.append(_EncodedTextGroup.toFIXMessage());
        return sb.toString();
    }

    public String toFIXML(String ident) {
        StringBuffer sb = new StringBuffer("");
        sb.append(ident + "<MDEntryGroup>\n");
        if (_MDEntryType != null) sb.append(_MDEntryType.toFIXML(ident + "\t") + "\n");
        if (_MDEntryPx != null) sb.append(_MDEntryPx.toFIXML(ident + "\t") + "\n");
        if (_Currency != null) sb.append(_Currency.toFIXML(ident + "\t") + "\n");
        if (_MDEntrySize != null) sb.append(_MDEntrySize.toFIXML(ident + "\t") + "\n");
        if (_MDEntryDate != null) sb.append(_MDEntryDate.toFIXML(ident + "\t") + "\n");
        if (_MDEntryTime != null) sb.append(_MDEntryTime.toFIXML(ident + "\t") + "\n");
        if (_TickDirection != null) sb.append(_TickDirection.toFIXML(ident + "\t") + "\n");
        if (_MDMkt != null) sb.append(_MDMkt.toFIXML(ident + "\t") + "\n");
        if (_TrdSesID != null) sb.append(_TrdSesID.toFIXML(ident + "\t") + "\n");
        if (_QuoteConditionList != null) sb.append(_QuoteConditionList.toFIXML(ident + "\t") + "\n");
        if (_TradeConditionList != null) sb.append(_TradeConditionList.toFIXML(ident + "\t") + "\n");
        if (_MDEntryOriginator != null) sb.append(_MDEntryOriginator.toFIXML(ident + "\t") + "\n");
        if (_MMLocationID != null) sb.append(_MMLocationID.toFIXML(ident + "\t") + "\n");
        if (_DeskID != null) sb.append(_DeskID.toFIXML(ident + "\t") + "\n");
        if (_OpenCloseSettFlag != null) sb.append(_OpenCloseSettFlag.toFIXML(ident + "\t") + "\n");
        if (_OrderDuration != null) sb.append(_OrderDuration.toFIXML(ident + "\t") + "\n");
        if (_ExpireDate != null) sb.append(_ExpireDate.toFIXML(ident + "\t") + "\n");
        if (_MinQty != null) sb.append(_MinQty.toFIXML(ident + "\t") + "\n");
        if (_ExecInstList != null) sb.append(_ExecInstList.toFIXML(ident + "\t") + "\n");
        if (_SellerDays != null) sb.append(_SellerDays.toFIXML(ident + "\t") + "\n");
        if (_OrderID != null) sb.append(_OrderID.toFIXML(ident + "\t") + "\n");
        if (_QuoteEntryID != null) sb.append(_QuoteEntryID.toFIXML(ident + "\t") + "\n");
        if (_MDEntryBuyer != null) sb.append(_MDEntryBuyer.toFIXML(ident + "\t") + "\n");
        if (_MDEntrySeller != null) sb.append(_MDEntrySeller.toFIXML(ident + "\t") + "\n");
        if (_MDNoOrders != null) sb.append(_MDNoOrders.toFIXML(ident + "\t") + "\n");
        if (_MDEntryPositionNo != null) sb.append(_MDEntryPositionNo.toFIXML(ident + "\t") + "\n");
        if (_Text != null) sb.append(_Text.toFIXML(ident + "\t") + "\n");
        if (_EncodedTextGroup != null) sb.append(_EncodedTextGroup.toFIXML(ident + "\t") + "\n");
        sb.append(ident + "</MDEntryGroup>");
        return sb.toString();
    }
}
