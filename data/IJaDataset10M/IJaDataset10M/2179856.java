package com.prem.share.dm;

public interface ScriptQuote {

    public String getScriptName();

    public String getScriptType();

    public double getScript52WH();

    public double getScript52WL();

    public double getScriptPreviousClose();

    public double getScriptDayOpen();

    public double getScriptDayHigh();

    public double getScriptDayLow();

    public double getScriptLastPrice();

    public double getScriptChange();

    public double getScriptChangePercentage();

    public long getScriptTotalTradeQuantity();

    public double getScriptClosePrice();

    public String getScriptCompanyName();

    public String getScriptDataAsOn();

    void setScriptName(String name);

    void setScriptType(String Type);

    void setScript52WH(double high52Week);

    void setScript52WL(double low52Week);

    void setScriptPreviousClose(double previousClose);

    void setScriptDayOpen(double dayOpen);

    void setScriptDayHigh(double dayHigh);

    void setScriptDayLow(double dayLow);

    void setScriptLastPrice(double lastPrice);

    void setScriptChange(double change);

    void setScriptChangePercentage(double changePercentage);

    void setScriptTotalTradeQuantity(long totalTradeQuantity);

    void setScriptClosePrice(double closePrice);

    void setScriptCompanyName(String companyName);

    void setScriptDataAsOn(String dataAsOn);

    public void refresh();
}
