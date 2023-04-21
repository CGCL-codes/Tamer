package gr.gousios.ereceipt.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import net.sf.json.JSONObject;

public class Error extends ModelObject {

    private String msg;

    private String code;

    public static Error noSuchUser(String name) {
        return new Error("Δεν υπάρχει ο χρήστης " + name, "100");
    }

    public static Error unameTooShort(String uname) {
        return new Error("Πολύ μικρό αναγνωριστικό χρήστη:" + uname + " Χρειάζονται τουλάχιστον 4 χαρακτήρες", "101");
    }

    public static Error passwdTooShort(String uname) {
        return new Error("Πολύ μικρός κωδικός:" + uname + " Χρειάζονται τουλάχιστον 6 χαρακτήρες", "102");
    }

    public static Error userExists(String uname) {
        return new Error("Ο χρήστης " + uname + " υπάρχει ήδη", "103");
    }

    public static Error wrongPasswd(String uname) {
        return new Error("Λάθος κωδικός για το χρήστη " + uname, "104");
    }

    public static Error noCompany(String afm) {
        return new Error("Δεν υπάρχει οντότητα με ΑΦΜ " + afm, "200");
    }

    public static Error wrongAFM(String afm) {
        return new Error("Ο ΑΦΜ " + afm + " δεν είναι έγκυρος", "201");
    }

    public static Error noCompanyName(String afm, String nameid) {
        return new Error("Δεν υπάρχει όνομα " + nameid + " για την εταιρεία " + afm, "202");
    }

    public static Error duplComp(String afm1) {
        return new Error("Η εταιρεία με ΑΦΜ:" + afm1 + " υπάρχει ήδη", "203");
    }

    public static Error wrongReceiptId(String receiptid) {
        return new Error("Ο κωδικός αριθμός απόδειξης " + receiptid + " δεν είναι έγκυρος", "301");
    }

    public static Error notUsersReceipt() {
        return new Error("Το κλειδί απόδειξης αντιστοιχεί σε " + "απόδειξη άλλου χρήστη", "302");
    }

    public static Error noReceipt(String string) {
        return new Error("Δεν υπάρχει απόδειξη με κωδικό " + string, "303");
    }

    public static Error wrongAmount(String amount) {
        return new Error("Το ποσό " + amount + " δεν είναι αριθμός", "304");
    }

    public static Error wrongDate(String date) {
        return new Error("Λάθος ημερομηνία" + date, "305");
    }

    public static Error dateTooOld(Date date) {
        return new Error("Η ημερομηνία " + date + " είναι πολύ παλιά " + "(έγκυρες ημερομηνίες > 1/1/2010)", "306");
    }

    public static Error noAppId(String string) {
        return new Error("Δεν υπάρχει εφαρμογή με κωδικό " + string, "401");
    }

    public static Error appNotAuthorised(String key) {
        return new Error("Το κλειδί " + key + "  δεν αντιστοιχεί σε κάποιa καταχωρημένη εφαρμογή. " + "Επικοινωνήστε με τον διαχειριστή.", "402");
    }

    public static Error unknownError() {
        return new Error("Άγνωστο λάθος", "500");
    }

    public static Error unknownCall(String name) {
        return new Error("Λάθος παράμετρος " + name, "501");
    }

    public static Error missParam(String name) {
        if (name != null) return new Error("Λείπει η παράμετρος " + name, "502");
        return new Error("Λείπει μια παράμετρος ", "502");
    }

    public static Error notAuthorised(String key) {
        return new Error("Το κλειδί " + key + "  δεν αντιστοιχεί σε κάποιο χρήστη", "503");
    }

    public Error() {
    }

    public Error(String msg, String code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toJSON(EntityManager em) {
        JSONObject json = new JSONObject();
        Map<String, String> values = new HashMap<String, String>();
        values.put("msg", msg);
        values.put("code", code);
        json.element("error", values);
        return json.toString(1);
    }
}
