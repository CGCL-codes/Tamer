package tradeWatch;

public class Alert {

    private SimpleContract contract;

    private SimpleOrder order;

    private Double alertPrice = -1.0;

    private Quote quote;

    private Double currentPrice = -1.0;

    private Double metric = 100.0;

    private AverageTrueRange ATR;

    private boolean triggered = false;

    private Integer WLorderID = -1;

    private Alert linkedAlert;

    private boolean dayTradeFlag = false;

    private Integer strategyCode = -1;

    private Integer parentId = -1;

    public Alert(WorkingOrder workingOrder) {
        this.quote = new Quote(workingOrder.getSymbol());
        setWorkingOrder(workingOrder);
        this.ATR = new AverageTrueRange(workingOrder.getContract());
    }

    public void setWorkingOrder(WorkingOrder workingOrder) {
        this.contract = workingOrder.getContract();
        this.order = workingOrder.getOrder();
        this.WLorderID = workingOrder.getWLorderID();
        this.strategyCode = workingOrder.getStrategyCode();
        this.parentId = workingOrder.getParentId();
        processStrategy();
        if (this.order.m_orderType.equals("LMT")) {
            this.alertPrice = new Double(this.order.m_lmtPrice);
        } else if (order.m_orderType.equals("STP")) {
            this.alertPrice = new Double(this.order.m_auxPrice);
        } else {
            this.alertPrice = -1.0;
            this.metric = 0.0;
        }
    }

    public void processStrategy() {
        if ((this.order.m_orderType.equals("LMT") || this.order.m_orderType.equals("STP")) && this.order.m_action.equals("BUY")) {
            if (this.strategyCode == -1) {
                Double price = order.m_lmtPrice * 100.;
                double floor = Math.floor(price);
                double code = price - floor;
                Double strategyVal = Math.rint(code / 0.111);
                int strategyCode = strategyVal.intValue();
                code = strategyCode * 0.111;
                price -= code;
                price /= 100.0;
                price = new Double(String.format("%7.2f", price));
                this.order.m_lmtPrice = price;
                this.strategyCode = strategyCode;
                ;
            }
        }
    }

    public Double getATR() {
        return this.ATR.get();
    }

    public void setATR(AverageTrueRange ATR) {
        this.ATR = ATR;
    }

    public void updateQuote(Quote quote) throws Exception {
        if (!contract.equals(quote.getContract())) {
            throw (new Exception("Input quote does not match this contract."));
        }
        this.quote = quote;
        metric = 100.;
        if (alertPrice < 0) {
            currentPrice = 0.5 * (quote.getAskPrice() + quote.getBidPrice());
            if (currentPrice.equals(0.0)) currentPrice = quote.getClosePrice();
            if (currentPrice > 0) {
                metric = 0.0;
            }
        } else {
            if (order.m_action.equals("BUY")) {
                currentPrice = quote.getAskPrice();
                if (currentPrice.equals(0.0)) currentPrice = quote.getClosePrice();
                if (currentPrice > 0) {
                    metric = (currentPrice - alertPrice) / ATR.get() * 100.0;
                }
            } else {
                currentPrice = quote.getBidPrice();
                if (currentPrice.equals(0.0)) currentPrice = quote.getClosePrice();
                if (currentPrice > 0) {
                    metric = (alertPrice - currentPrice) / ATR.get() * 100.0;
                }
            }
            if (this.getOrder().m_orderType.equals("STP") && currentPrice > 0) {
                metric = -metric;
            }
        }
    }

    public String getSymbol() {
        return this.contract.m_symbol;
    }

    public boolean isBuy() {
        return this.order.m_action.equals("BUY");
    }

    public boolean isSell() {
        return !this.isBuy();
    }

    public SimpleContract getContract() {
        return this.contract;
    }

    public SimpleOrder getOrder() {
        return this.order;
    }

    public Quote getQuote() {
        return this.quote;
    }

    public Double getAlertPrice() {
        return this.alertPrice;
    }

    public Double getMetric() {
        return this.metric;
    }

    public Double getCurrentPrice() {
        return this.currentPrice;
    }

    public boolean getTriggered() {
        return this.triggered;
    }

    public Integer getWLorderID() {
        return this.WLorderID;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public boolean getDayTradeFlag() {
        return this.dayTradeFlag;
    }

    public void setDayTradeFlag(boolean flag) {
        this.dayTradeFlag = flag;
    }

    public void setLinkedAlert(Alert alert) {
        this.linkedAlert = alert;
    }

    public Alert getLinkedAlert() {
        return this.linkedAlert;
    }

    public Integer getStrategyCode() {
        return this.strategyCode;
    }

    public void setStrategyCode(Integer strategyCode) {
        this.strategyCode = strategyCode;
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String toString() {
        return "Alert: " + contract.m_symbol + " " + order.m_action + " " + order.m_totalQuantity + ", " + order.m_orderType + "=" + alertPrice;
    }

    public boolean equals(Alert alert) {
        return contract.equals(alert.getContract()) && order.equals(alert.getOrder()) && alertPrice.equals(alert.getAlertPrice());
    }
}
