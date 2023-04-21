package rbe;

public class EBWBuyReqTrans extends EBTransition {

    public String request(EB eb, String html) {
        RBE rbe = eb.rbe;
        String url = rbe.buyReqURL;
        if (eb.cid != eb.ID_UNKNOWN) {
            url = url + "?" + rbe.field_retflag + "=Y&" + rbe.unameAndPass(eb.cid);
        } else {
            url = url + "?" + rbe.field_retflag + "=N";
            eb.fname = rbe.astring(eb.rand, 8, 15);
            eb.lname = rbe.astring(eb.rand, 8, 15);
            url = url + "&" + rbe.field_fname + "=" + eb.fname;
            url = url + "&" + rbe.field_lname + "=" + eb.lname;
            url = url + "&" + rbe.field_street1 + "=" + rbe.astring(eb.rand, 15, 40);
            url = url + "&" + rbe.field_street2 + "=" + rbe.astring(eb.rand, 15, 40);
            url = url + "&" + rbe.field_city + "=" + rbe.astring(eb.rand, 10, 30);
            url = url + "&" + rbe.field_state + "=" + rbe.astring(eb.rand, 2, 20);
            url = url + "&" + rbe.field_zip + "=" + rbe.astring(eb.rand, 5, 10);
            url = url + "&" + rbe.field_country + "=" + rbe.unifCountry(eb.rand);
            url = url + "&" + rbe.field_phone + "=" + rbe.nstring(eb.rand, 9, 16);
            url = url + "&" + rbe.field_email + "=" + rbe.astring(eb.rand, 8, 15) + "%40" + rbe.astring(eb.rand, 2, 9) + ".com";
            url = url + "&" + rbe.field_birthdate + "=" + rbe.unifDOB(eb.rand);
            url = url + "&" + rbe.field_data + "=" + rbe.astring(eb.rand, 100, 500);
        }
        return (eb.addIDs(url));
    }

    public void postProcess(EB eb, String html) {
        if (eb.cid == eb.ID_UNKNOWN) {
            eb.cid = eb.findID(html, RBE.yourCID);
            if (eb.cid == eb.ID_UNKNOWN) {
                eb.rbe.stats.error("Unable to find C_ID in buy request page page.", "<???>");
            }
        }
    }
}
