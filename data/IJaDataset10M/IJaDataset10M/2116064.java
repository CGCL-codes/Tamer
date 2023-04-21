package sf.net.algotrade.c2api.response;

import java.util.ArrayList;
import java.util.Hashtable;
import sf.net.algotrade.C2Response;

public class GetSystemHypotheticalResponse extends C2Response {

    private Hashtable<String, C2SystemHypotheticalDetails> systemsDetails = new Hashtable<String, C2SystemHypotheticalDetails>();

    public GetSystemHypotheticalResponse() {
    }

    protected void addSystem(String systemid) {
        systemsDetails.put(systemid, new C2SystemHypotheticalDetails(systemid));
    }

    protected void setSystemname(String theSystemid, String systemname) {
        systemsDetails.get(theSystemid).setSystemname(systemname);
    }

    protected void setTotalEquity(String systemid, String totalequity) {
        systemsDetails.get(systemid).setTotalequityavail(Double.parseDouble(totalequity));
    }

    protected void setCash(String systemid, String cash) {
        systemsDetails.get(systemid).setCash(Double.parseDouble(cash));
    }

    protected void setEquity(String systemid, String equity) {
        systemsDetails.get(systemid).setEquity(Double.parseDouble(equity));
    }

    protected void setMarginUsed(String systemid, String marginUsed) {
        systemsDetails.get(systemid).setMarginused(Double.parseDouble(marginUsed));
    }

    public ArrayList<String> getAvailableSystems() {
        return (new ArrayList<String>(systemsDetails.keySet()));
    }

    public int getNumAvailableSystems() {
        return systemsDetails.size();
    }

    public C2SystemHypotheticalDetails getSystemDetails(String systemid) {
        return systemsDetails.get(systemid);
    }

    public String getSystemName(String systemid) {
        return systemsDetails.get(systemid).getSystemname();
    }

    public double getEquityAvailable(String systemid) {
        return systemsDetails.get(systemid).getTotalequityavail();
    }

    public double getCash(String systemid) {
        return systemsDetails.get(systemid).getCash();
    }

    public double getEquity(String systemid) {
        return systemsDetails.get(systemid).getEquity();
    }

    public double getMarginUsed(String systemid) {
        return systemsDetails.get(systemid).getMarginused();
    }
}
