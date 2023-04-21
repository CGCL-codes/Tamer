package hu.sztaki.lpds.wfs.com;

/**
 * @author krisztian
 */
public class HistoryBean {

    private String tim = "";

    private String port = "";

    private String user = "";

    private String mdyid = "";

    private String ovalue = "";

    private String nvalue = "";

    /**
 * Class construcktor
 */
    public HistoryBean() {
    }

    /**
 * Class construcktor
 * @param pTim  Valtozas idopontja
 * @param pPort Job port azonosito
 * @param pUser Valtozast vegrehajto user
 * @param pMdyid Valtozas azonosito
 * @param pOvalue Regi ertek
 * @param pNvalue uj ertek
 * @see String
 */
    public HistoryBean(String pTim, String pPort, String pUser, String pMdyid, String pOvalue, String pNvalue) {
        tim = pTim;
        port = pPort;
        user = pUser;
        mdyid = pMdyid;
        ovalue = pOvalue;
        nvalue = pNvalue;
    }

    /**
 * Tulajdonsag megvaltozasanak idobelyege
 * @return Valtozas idopontja
 * @see String
 */
    public String getTim() {
        return tim;
    }

    /**
 * A job port azonositoja
 * @return port id
 * @see String
 */
    public String getPort() {
        return port;
    }

    /**
 * Modositast vegzo portal user lekerdezese
 * @return portal user
 * @see String
 */
    public String getUser() {
        return user;
    }

    /**
 * Tulajdonsag valtozas ID lekerdezese
 * @return tulajdonseg ID
 * @see String
 */
    public String getMdyid() {
        return mdyid;
    }

    /**
 * Tulajdonseg regi ertekenek lekerdezese
 * @return regi ertek
 * @see String
 */
    public String getOvalue() {
        return ovalue;
    }

    /**
 * Tulajdonsag uj ertekenek lekerdezese
 * @return uj ertek
 * @see String
 */
    public String getNvalue() {
        return nvalue;
    }

    /**
 * Valtoztatas idobelyegenek beallitasa
 * @param pValue
 * @see String
 */
    public void setTim(String pValue) {
        tim = pValue;
    }

    /**
 * A job port azonositoja
 * @param pValue
 * @see String
 */
    public void setPort(String pValue) {
        port = pValue;
    }

    /**
 * Az esemenyt kivalto portal user beallitasa
 * @param pValue portal user
 * @see String
 */
    public void setUser(String pValue) {
        user = pValue;
    }

    /**
 * Az esemenyt azonosito beallitasa
 * @param pValue konfiguracios esemeny
 * @see String
 */
    public void setMdyid(String pValue) {
        mdyid = pValue;
    }

    /**
 * Regi konfiguracios ertek beallitasa
 * @param pValue regi ertek
 * @see String
 */
    public void setOvalue(String pValue) {
        ovalue = pValue;
    }

    /**
 * Uj konfiguracios ertek beallitasa
 * @param pValue uj ertek
 * @see String
 */
    public void setNvalue(String pValue) {
        nvalue = pValue;
    }
}
