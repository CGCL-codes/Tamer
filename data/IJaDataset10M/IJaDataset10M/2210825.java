package educate.sis.billing;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import lebah.db.Db;
import lebah.db.DbDelegator;
import lebah.db.SQLRenderer;

public class FeeData2 {

    public static Vector getBillingFeeList(String student_id) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            boolean international = false;
            {
                sql = r.reset().add("currency_type").add("id", student_id).getSQLSelect("student");
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    international = "international".equals(rs.getString(1)) ? true : false;
                }
            }
            Hashtable paid = new Hashtable();
            {
                sql = r.reset().add("fee_id").add("SUM(amount) amount").add("student_id", student_id).getSQLSelect("student_receipt_detail").concat(" group by fee_id");
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String fee_id = lebah.db.Db.getString(rs, "fee_id");
                    String amount = lebah.db.Db.getString(rs, "amount");
                    paid.put(fee_id, amount);
                }
            }
            Vector v = new Vector();
            {
                sql = r.reset().add("fee_id").add("fee_code").add("fee_description").add("amount").add("student_id", student_id).getSQLSelect("student_billing_detail");
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Hashtable h = new Hashtable();
                    String fee_id = lebah.db.Db.getString(rs, "fee_id");
                    h.put("id", fee_id);
                    h.put("code", lebah.db.Db.getString(rs, "fee_code"));
                    h.put("description", lebah.db.Db.getString(rs, "fee_description"));
                    float amount_billed = rs.getFloat("amount");
                    String payment = (String) paid.get(fee_id);
                    if (payment != null) {
                        float amount_paid = Float.parseFloat(payment);
                        float amount_balance = amount_billed - amount_paid;
                        if (amount_balance == 0) h.put("isPaid", new Boolean(true)); else h.put("isPaid", new Boolean(false));
                        h.put("amount", BillingData.getDecimalFormatted(amount_balance));
                        h.put("amount_billed", BillingData.getDecimalFormatted(amount_billed));
                        h.put("amount_paid", BillingData.getDecimalFormatted(amount_paid));
                        h.put("amount_float", new Float(amount_balance));
                        h.put("amount_billed_float", new Float(amount_billed));
                        h.put("amount_paid_float", new Float(amount_paid));
                    } else {
                        h.put("isPaid", new Boolean(false));
                        h.put("amount", BillingData.getDecimalFormatted(amount_billed));
                        h.put("amount_billed", BillingData.getDecimalFormatted(amount_billed));
                        h.put("amount_paid", "0.00");
                        h.put("amount_float", new Float(amount_billed));
                        h.put("amount_billed_float", new Float(amount_billed));
                        h.put("amount_paid_float", new Float(0.0f));
                    }
                    v.addElement(h);
                }
            }
            return v;
        } finally {
            if (db != null) db.close();
        }
    }

    public static Vector getBillingFeeList(String student_id, String[] feearray) throws Exception {
        List fees = Arrays.asList(feearray);
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            Hashtable paid = new Hashtable();
            {
                sql = r.reset().add("fee_id").add("SUM(amount) amount").add("student_id", student_id).getSQLSelect("student_receipt_detail").concat(" group by fee_id");
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String fee_id = Db.getString(rs, "fee_id");
                    String amount = Db.getString(rs, "amount");
                    paid.put(fee_id, amount);
                }
            }
            Vector v = new Vector();
            {
                sql = r.reset().add("fee_id").add("fee_code").add("fee_description").add("amount").add("student_id", student_id).getSQLSelect("student_billing_detail");
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Hashtable h = new Hashtable();
                    String fee_id = lebah.db.Db.getString(rs, "fee_id");
                    if (fees.contains(fee_id)) {
                        String fee_code = Db.getString(rs, "fee_code");
                        float amount_billed = rs.getFloat("amount");
                        h.put("id", fee_id);
                        h.put("code", fee_code);
                        h.put("description", Db.getString(rs, "fee_description"));
                        String payment = (String) paid.get(fee_id);
                        if (payment != null) {
                            float amount_paid = Float.parseFloat(payment);
                            float amount_balance = amount_billed - amount_paid;
                            h.put("amount", BillingData.getDecimalFormatted(amount_balance));
                            h.put("amount_billed", BillingData.getDecimalFormatted(amount_billed));
                            h.put("amount_paid", BillingData.getDecimalFormatted(amount_paid));
                            h.put("amount_float", new Float(amount_balance));
                            h.put("amount_billed_float", new Float(amount_billed));
                            h.put("amount_paid_float", new Float(amount_paid));
                        } else {
                            h.put("amount", BillingData.getDecimalFormatted(amount_billed));
                            h.put("amount_billed", BillingData.getDecimalFormatted(amount_billed));
                            h.put("amount_paid", "0.00");
                            h.put("amount_float", new Float(amount_billed));
                            h.put("amount_billed_float", new Float(amount_billed));
                            h.put("amount_paid_float", new Float(0.0f));
                        }
                        v.addElement(h);
                    }
                }
            }
            return v;
        } finally {
            if (db != null) db.close();
        }
    }

    public static Vector getBillingFeeListMore(String student_id, String[] feearray) throws Exception {
        List fees = Arrays.asList(feearray);
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            Hashtable paid = new Hashtable();
            {
                sql = r.reset().add("fee_id").add("amount").add("student_id", student_id).getSQLSelect("student_receipt_detail");
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String fee_id = lebah.db.Db.getString(rs, "fee_id");
                    String amount = lebah.db.Db.getString(rs, "amount");
                    paid.put(fee_id, amount);
                }
            }
            Vector v = new Vector();
            {
                sql = r.reset().add("fee_id").add("fee_code").add("fee_description").add("amount").getSQLSelect("student_billing_detail").concat(" GROUP BY fee_id");
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Hashtable h = new Hashtable();
                    String fee_id = lebah.db.Db.getString(rs, "fee_id");
                    if (!fees.contains(fee_id)) {
                        h.put("id", fee_id);
                        h.put("code", lebah.db.Db.getString(rs, "fee_code"));
                        h.put("description", lebah.db.Db.getString(rs, "fee_description"));
                        float amount_billed = rs.getFloat("amount");
                        String payment = (String) paid.get(fee_id);
                        if (payment != null) {
                            float amount_paid = Float.parseFloat(payment);
                            float amount_balance = amount_billed - amount_paid;
                            h.put("amount", BillingData.getDecimalFormatted(amount_balance));
                            h.put("amount_billed", BillingData.getDecimalFormatted(amount_billed));
                            h.put("amount_paid", BillingData.getDecimalFormatted(amount_paid));
                        } else {
                            h.put("amount", BillingData.getDecimalFormatted(amount_billed));
                            h.put("amount_billed", BillingData.getDecimalFormatted(amount_billed));
                            h.put("amount_paid", "0.00");
                        }
                        v.addElement(h);
                    }
                }
            }
            return v;
        } finally {
            if (db != null) db.close();
        }
    }

    public static Vector getBillingFeeListMore(String student_id, Hashtable receiptInfo) throws Exception {
        Vector details = (Vector) receiptInfo.get("detail");
        String[] ids = new String[details.size()];
        for (int i = 0; i < details.size(); i++) {
            Hashtable detail = (Hashtable) details.elementAt(i);
            ids[i] = (String) detail.get("id");
        }
        return getBillingFeeListMore(student_id, ids);
    }
}
