package sf.net.algotrade.c2api.response;

import sf.net.algotrade.C2Response;

public class PositionStatusResponse extends C2Response {

    private String symbol;

    private String calctime;

    private boolean isLongPosition;

    public PositionStatusResponse() {
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCalctime() {
        return calctime;
    }

    public boolean isLongPosition() {
        return isLongPosition;
    }

    protected void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    protected void setCalctime(String calctime) {
        this.calctime = calctime;
    }

    protected void setLongPosition(int position) {
        this.isLongPosition = (position > 0);
    }
}
