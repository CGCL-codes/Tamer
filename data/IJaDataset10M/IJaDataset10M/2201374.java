package com.ravana.pos;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.Session;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Class to extend functionality of DataLogicSales
 * With Accounting
 * @author Manjuka Soysa
 */
public class DataLogicGl {

    public static final String ACC_STOCK = "1";

    public static final String ACC_CASH = "2";

    public static final String ACC_DEBTORS_CUSTOMER = "3";

    public static final String ACC_DEBTORS_CORPORATE = "4";

    public static final String ACC_PBS_DUE = "5";

    public static final String ACC_CHEQUE = "6";

    public static final String ACC_NOTE = "7";

    public static final String ACC_CARD = "8";

    public static final String ACC_GST = "11";

    public static final String ACC_CREDITORS = "12";

    public static final String ACC_SALES = "21";

    public static final String ACC_NOTE_BALANCE = "22";

    public static final String ACC_COGS = "31";

    public static final String ACC_BILLS = "32";

    public static final String ACC_GIVEAWAY = "33";

    public static final String ACC_STOCKLOSS = "34";

    public static final String ACC_COMMISSIONS = "35";

    public static final String ACC_ROUNDING = "36";

    public static final String ACC_PETTYCASH = "37";

    public static final String ACC_INTERBRANCH = "38";

    public static void postTransaction(Session s, TransactionType transType, String refId, String userId, List<PaymentInfo> payments, BigDecimal cogs, BigDecimal gstIncluded, BigDecimal govRecov) throws BasicException {
        HashMap<String, BigDecimal> changes = new HashMap();
        if (cogs.compareTo(BigDecimal.ZERO) != 0) {
            changes.put(ACC_COGS, cogs);
            changes.put(ACC_STOCK, cogs.negate());
        }
        BigDecimal balancing = BigDecimal.ZERO;
        BigDecimal totalRounding = BigDecimal.ZERO;
        for (PaymentInfo p : payments) {
            BigDecimal pPayment = p.getTotal();
            pPayment = pPayment.setScale(2, RoundingMode.HALF_EVEN);
            if ("cash".equals(p.getName()) || "cashrefund".equals(p.getName())) {
                BigDecimal rounding = PosRounding.getRounding(pPayment.abs());
                rounding = rounding.setScale(2, RoundingMode.HALF_EVEN);
                if (rounding.compareTo(BigDecimal.ZERO) != 0) {
                    if (pPayment.compareTo(BigDecimal.ZERO) < 0) rounding = rounding.negate();
                    pPayment = pPayment.add(rounding);
                    rounding = rounding.negate();
                    totalRounding = totalRounding.add(rounding);
                }
            }
            balancing = balancing.add(pPayment);
            changes.put(p.getAccount(), pPayment);
        }
        if (totalRounding.compareTo(BigDecimal.ZERO) != 0) {
            changes.put(ACC_ROUNDING, totalRounding);
            balancing = balancing.add(totalRounding);
        }
        if (gstIncluded.compareTo(BigDecimal.ZERO) != 0) {
            gstIncluded = gstIncluded.negate();
            changes.put(ACC_GST, gstIncluded);
            balancing = balancing.add(gstIncluded);
        }
        if (govRecov.compareTo(BigDecimal.ZERO) != 0) {
            changes.put(ACC_PBS_DUE, govRecov);
            balancing = balancing.add(govRecov);
        }
        changes.put(transType.getBalancingAccount(), balancing.negate());
        postTransaction(s, changes, transType.getName(), refId, userId);
    }

    public static void postTransaction(Session s, Map<String, BigDecimal> changes, String description, String reference, String userId) throws BasicException {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal amt : changes.values()) sum = sum.add(amt);
        if (sum.compareTo(BigDecimal.ZERO) != 0) throw new BasicException("Transaction change amounts must add up to zero");
        SentenceExec txInsert = new PreparedSentence(s, "INSERT INTO GL_TRANSACTION (ID, TX_TIME, DESCRIPTION, REFERENCE, USER_ID) VALUES (?, ?, ?, ?, ?)", new SerializerWriteBasic(new Datas[] { Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.STRING, Datas.STRING }));
        SentenceExec acctChgInsert = new PreparedSentence(s, "INSERT INTO GL_ACCT_CHANGE (ID, TX_ID, ACCT_ID, CHG_AMOUNT) VALUES (?, ?, ?, ?)", new SerializerWriteBasic(new Datas[] { Datas.STRING, Datas.STRING, Datas.STRING, Datas.DECIMAL }));
        String txId = UUID.randomUUID().toString();
        Object[] txParams = { txId, new Date(), description, reference, userId };
        txInsert.exec(txParams);
        for (String acct : changes.keySet()) {
            Object[] acctChgParams = { UUID.randomUUID().toString(), txId, acct, changes.get(acct) };
            acctChgInsert.exec(acctChgParams);
        }
    }

    static Datas[] analysisWriter = { Datas.STRING, Datas.INT, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.DECIMAL, Datas.DECIMAL, Datas.DECIMAL, Datas.DECIMAL, Datas.DECIMAL, Datas.TIMESTAMP, Datas.INT, Datas.INT, Datas.INT, Datas.INT, Datas.INT, Datas.STRING, Datas.STRING };

    static PreparedSentence analysisPs;

    public static void postSalesAnalysis(Session s, TicketInfo ti, String group, String subgroup, TicketLineInfo tl, BigDecimal cogs, BigDecimal govRecov, String money, String terminalId) throws BasicException {
        String insert = "insert into sales_analysis (ticket, line, money, product_id, product_group, product_subgroup, quantity, " + "sale_value, gst_value, cost_of_goods, govt_recovery, sale_time, sale_year, sale_month, sale_day_month, sale_day_week, sale_hour, sale_person, terminal_id) values " + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (analysisPs == null) {
            analysisPs = new PreparedSentence(s, insert, new SerializerWriteBasic(analysisWriter), null);
        }
        Object[] params = new Object[19];
        BigDecimal qty = tl.getMultiply();
        BigDecimal price = tl.getSubValue();
        BigDecimal gstValue = tl.getTax();
        params[0] = ti.getId();
        params[1] = tl.getTicketLine();
        params[2] = money;
        params[3] = tl.getProduct().getId();
        params[4] = group;
        params[5] = subgroup;
        params[6] = qty;
        params[7] = price;
        params[8] = gstValue;
        params[9] = cogs;
        params[10] = govRecov;
        params[11] = new java.sql.Timestamp(ti.getDate().getTime());
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(ti.getDate());
        params[12] = gc.get(GregorianCalendar.YEAR);
        params[13] = gc.get(GregorianCalendar.MONTH);
        params[14] = gc.get(GregorianCalendar.DAY_OF_MONTH);
        params[15] = gc.get(GregorianCalendar.DAY_OF_WEEK);
        params[16] = gc.get(GregorianCalendar.HOUR_OF_DAY);
        params[17] = ti.getUser().getId();
        params[18] = terminalId;
        analysisPs.exec(params);
    }
}
