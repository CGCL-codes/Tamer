package educate.sis.billing.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;
import lebah.db.DataHelper;
import lebah.db.Db;
import lebah.db.SQLRenderer;
import lebah.util.DateTool;

/**
 * 
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 * @version 0.1
 */
public class BillingData {

    public static Vector getInvoiceList(Hashtable data, Hashtable data2, String programCode, String intake, String status, int month, int year, int month2, int year2) throws Exception {
        return getInvoiceList(data, data2, programCode, intake, status, "", month, year, month2, year2);
    }

    public static Vector getInvoiceListLocal(Hashtable data, Hashtable data2, String programCode, String intake, String status, int month, int year, int month2, int year2) throws Exception {
        return getInvoiceList(data, data2, programCode, intake, status, "local", month, year, month2, year2);
    }

    public static Vector getInvoiceListInternational(Hashtable data, Hashtable data2, String programCode, String intake, String status, int month, int year, int month2, int year2) throws Exception {
        return getInvoiceList(data, data2, programCode, intake, status, "international", month, year, month2, year2);
    }

    public static Vector getInvoiceList(Hashtable data, Hashtable data2, String programCode, String intake, String status, String currencyType, int month, int year, int month2, int year2) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            SQLRenderer r = new SQLRenderer();
            r.add("s.name").add("b.student_id").add("b.bill_no ref").add("b.bill_date issuedDate").add("p.program_code").add("b.amount_total amount").add("s.currency_type").add("s.id", r.unquote("b.student_id")).add("sc.student_id", r.unquote("b.student_id")).add("sc.student_id", r.unquote("s.id")).add("sc.program_code", r.unquote("p.program_code")).add("p.program_code", programCode).add("s.status", status);
            if (!"".equals(intake)) r.add("sc.intake_session", intake);
            if (!"".equals(currencyType)) r.add("s.currency_type", currencyType);
            Calendar calFrom = new GregorianCalendar(year, month - 1, 1);
            Calendar calTo = new GregorianCalendar(year2, month2 - 1, 1);
            calTo.set(Calendar.DAY_OF_MONTH, calTo.getActualMaximum(Calendar.DAY_OF_MONTH));
            String dateFrom = DateTool.getDateStr(calFrom.getTime());
            String dateTo = DateTool.getDateStr(calTo.getTime());
            r.add("b.bill_date", dateFrom, ">=");
            r.add("b.bill_date", dateTo, "<=");
            sql = r.getSQLSelect("student s, student_course sc, program p, student_billing b", "s.name");
            ResultSet rs = db.getStatement().executeQuery(sql);
            String id = "", lastId = "";
            Vector students = new Vector();
            Vector list = null;
            float amount = 0.0f;
            float amountTotal = 0.0f;
            Hashtable receipt = new Hashtable();
            String name = "", lastName = "";
            while (rs.next()) {
                if ("".equals(lastId)) {
                    id = rs.getString("student_id");
                    name = rs.getString("name");
                    lastId = id;
                    lastName = name;
                    Hashtable h = prepare(rs);
                    amount += ((Float) h.get("amount")).floatValue();
                    amountTotal += ((Float) h.get("amount")).floatValue();
                    list = new Vector();
                    list.addElement(h);
                } else if (!"".equals(lastId)) {
                    id = rs.getString("student_id");
                    name = rs.getString("name");
                    if (id.equals(lastId)) {
                        Hashtable h = prepare(rs);
                        amount += ((Float) h.get("amount")).floatValue();
                        amountTotal += ((Float) h.get("amount")).floatValue();
                        list.addElement(h);
                    } else {
                        data.put(lastId, list);
                        data2.put(lastId, new Vector());
                        receipt.put("student_id", lastId);
                        receipt.put("name", lastName);
                        receipt.put("amount", new Float(amount));
                        lastId = id;
                        lastName = name;
                        amount = 0f;
                        students.addElement(receipt);
                        Hashtable h = prepare(rs);
                        amount += ((Float) h.get("amount")).floatValue();
                        amountTotal += ((Float) h.get("amount")).floatValue();
                        list = new Vector();
                        list.addElement(h);
                        receipt = new Hashtable();
                    }
                }
            }
            if (!"".equals(id)) {
                data.put(id, list);
                data2.put(id, new Vector());
                receipt.put("student_id", lastId);
                receipt.put("name", lastName);
                receipt.put("amount", new Float(amount));
                students.addElement(receipt);
            }
            data.put("amountTotal", new Float(amountTotal));
            return students;
        } finally {
            if (db != null) db.close();
        }
    }

    public static Vector getPaymentList(Hashtable data, Hashtable payment, String programCode, String intake, String status, int month, int year, int month2, int year2) throws Exception {
        return getPaymentList(data, payment, programCode, intake, status, "", month, year, month2, year2);
    }

    public static Vector getPaymentListLocal(Hashtable data, Hashtable payment, String programCode, String intake, String status, int month, int year, int month2, int year2) throws Exception {
        return getPaymentList(data, payment, programCode, intake, status, "local", month, year, month2, year2);
    }

    public static Vector getPaymentListInternational(Hashtable data, Hashtable payment, String programCode, String intake, String status, int month, int year, int month2, int year2) throws Exception {
        return getPaymentList(data, payment, programCode, intake, status, "international", month, year, month2, year2);
    }

    public static Vector getPaymentList(Hashtable data, Hashtable payment, String programCode, String intake, String status, String currencyType, int month, int year, int month2, int year2) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            SQLRenderer r = new SQLRenderer();
            r.add("b.student_id").add("s.name").add("b.receipt_no ref").add("b.receipt_date issuedDate").add("p.program_code").add("b.amount_total amount").add("s.currency_type").add("s.id", r.unquote("b.student_id")).add("sc.student_id", r.unquote("b.student_id")).add("sc.student_id", r.unquote("s.id")).add("sc.program_code", r.unquote("p.program_code")).add("p.program_code", programCode).add("s.status", status);
            if (!"".equals(intake)) r.add("sc.intake_session", intake);
            if (!"".equals(currencyType)) r.add("s.currency_type", currencyType);
            Calendar calFrom = new GregorianCalendar(year, month - 1, 1);
            Calendar calTo = new GregorianCalendar(year2, month2 - 1, 1);
            calTo.set(Calendar.DAY_OF_MONTH, calTo.getActualMaximum(Calendar.DAY_OF_MONTH));
            String dateFrom = DateTool.getDateStr(calFrom.getTime());
            String dateTo = DateTool.getDateStr(calTo.getTime());
            r.add("b.receipt_date", dateFrom, ">=");
            r.add("b.receipt_date", dateTo, "<=");
            sql = r.getSQLSelect("student s, student_course sc, program p, student_receipt b", "s.name");
            ResultSet rs = db.getStatement().executeQuery(sql);
            String id = "", lastId = "";
            Vector students = new Vector();
            Vector list = null;
            float amount = 0.0f;
            float amountTotal = 0.0f;
            Hashtable receipt = new Hashtable();
            String name = "", lastName = "";
            while (rs.next()) {
                if ("".equals(lastId)) {
                    id = rs.getString("student_id");
                    name = rs.getString("name");
                    lastId = id;
                    lastName = name;
                    Hashtable h = prepare(rs);
                    amount += ((Float) h.get("amount")).floatValue();
                    amountTotal += ((Float) h.get("amount")).floatValue();
                    list = new Vector();
                    list.addElement(h);
                } else if (!"".equals(lastId)) {
                    id = rs.getString("student_id");
                    name = rs.getString("name");
                    if (id.equals(lastId)) {
                        Hashtable h = prepare(rs);
                        amount += ((Float) h.get("amount")).floatValue();
                        amountTotal += ((Float) h.get("amount")).floatValue();
                        list.addElement(h);
                    } else {
                        data.put(lastId, list);
                        receipt.put("student_id", lastId);
                        receipt.put("name", lastName);
                        receipt.put("amount", new Float(amount));
                        payment.put(lastId, new Float(amount));
                        lastId = id;
                        lastName = name;
                        amount = 0f;
                        students.addElement(receipt);
                        Hashtable h = prepare(rs);
                        amount += ((Float) h.get("amount")).floatValue();
                        amountTotal += ((Float) h.get("amount")).floatValue();
                        list = new Vector();
                        list.addElement(h);
                        receipt = new Hashtable();
                    }
                }
            }
            if (!"".equals(id)) {
                data.put(id, list);
                receipt.put("student_id", lastId);
                receipt.put("name", lastName);
                receipt.put("amount", new Float(amount));
                payment.put(lastId, new Float(amount));
                students.addElement(receipt);
            }
            data.put("amountTotal", new Float(amountTotal));
            return students;
        } finally {
            if (db != null) db.close();
        }
    }

    private static Hashtable prepare(ResultSet rs) throws SQLException {
        Hashtable h = new Hashtable();
        h.put("student_id", rs.getString("student_id"));
        h.put("refNo", rs.getString("ref"));
        h.put("issuedDate", DateTool.getDateStr(rs.getDate("issuedDate")));
        h.put("program_code", rs.getString("program_code"));
        h.put("amount", new Float(rs.getFloat("amount")));
        return h;
    }

    public static void getStudentStatus(String studentId) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
        } finally {
            if (db != null) db.close();
        }
    }

    public static void updateStudentStatus(String studentId, String status) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
        } finally {
            if (db != null) db.close();
        }
    }
}
