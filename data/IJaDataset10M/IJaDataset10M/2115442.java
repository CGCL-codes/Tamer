package cn.ekuma.epos.datalogic.define.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import cn.ekuma.data.dao.ModifiedLogDAO;
import cn.ekuma.epos.db.table.I_CloseCash;
import com.openbravo.data.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.I_Session;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerRead;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadInteger;
import com.openbravo.data.loader.SerializerReadString;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.model.Field;
import com.openbravo.format.Formats;
import com.openbravo.bean.CloseCash;
import com.openbravo.bean.PaymentsLine;
import com.openbravo.bean.PaymentsModel;
import com.openbravo.bean.SalesLine;
import net.sourceforge.jtimepiece.TimeUtil;

/**
 *
 * @author Administrator
 */
public class CloseCashDAO extends ModifiedLogDAO<CloseCash> {

    public CloseCashDAO(I_Session s) {
        super(s);
    }

    @Override
    public TableDefinition getTable() {
        return new TableDefinition(s, "CLOSEDCASH", new Field[] { new Field(I_CloseCash.MONEY, Datas.STRING, Formats.STRING), new Field(I_CloseCash.HOST, Datas.STRING, Formats.STRING), new Field(I_CloseCash.HOSTSEQUENCE, Datas.INT, Formats.INT), new Field(I_CloseCash.DATESTART, Datas.TIMESTAMP, Formats.TIMESTAMP), new Field(I_CloseCash.DATEEND, Datas.TIMESTAMP, Formats.TIMESTAMP), new Field(I_CloseCash.HANDCASH, Datas.DOUBLE, Formats.DOUBLE), new Field(I_CloseCash.MEMO, Datas.STRING, Formats.STRING), new Field(I_CloseCash.NAME, Datas.STRING, Formats.STRING), new Field(I_CloseCash.LASTMODIFIED, Datas.TIMESTAMP, Formats.TIMESTAMP), new Field(I_CloseCash.PAYMENTNUM, Datas.INT, Formats.INT), new Field(I_CloseCash.PAYMENTSTOTAL, Datas.DOUBLE, Formats.DOUBLE), new Field(I_CloseCash.SALENUM, Datas.INT, Formats.INT), new Field(I_CloseCash.SALESBASE, Datas.DOUBLE, Formats.DOUBLE), new Field(I_CloseCash.SALESTAXES, Datas.DOUBLE, Formats.DOUBLE) }, new int[] { 0 });
    }

    @Override
    public void writeInsertValues(DataWrite dp, CloseCash obj) throws BasicException {
        dp.setString(1, obj.getMoneyID());
        dp.setString(2, obj.getHost());
        dp.setInt(3, obj.getHostSequence());
        dp.setTimestamp(4, obj.getDataStart());
        dp.setTimestamp(5, obj.getDataEnd());
        dp.setDouble(6, obj.getHandCash());
        dp.setString(7, obj.getMemo());
        dp.setString(8, obj.getName());
        dp.setTimestamp(9, obj.getLastModified());
        dp.setInt(10, obj.getPaymentNum());
        dp.setDouble(11, obj.getPaymentsTotal());
        dp.setInt(12, obj.getSaleNum());
        dp.setDouble(13, obj.getSalesBase());
        dp.setDouble(14, obj.getSalesTaxes());
    }

    public CloseCash readValues(DataRead dr, CloseCash obj) throws BasicException {
        if (obj == null) obj = new CloseCash();
        obj.setMoneyID(dr.getString(1));
        obj.setHost(dr.getString(2));
        obj.setHostSequence(dr.getInt(3));
        obj.setDataStart(dr.getTimestamp(4));
        obj.setDataEnd(dr.getTimestamp(5));
        obj.setHandCash(dr.getDouble(6));
        obj.setMemo(dr.getString(7));
        obj.setName(dr.getString(8));
        obj.setLastModified(dr.getTimestamp(9));
        obj.setPaymentNum(dr.getInt(10));
        obj.setPaymentsTotal(dr.getDouble(11));
        obj.setSaleNum(dr.getInt(12));
        obj.setSalesBase(dr.getDouble(13));
        obj.setSalesTaxes(dr.getDouble(14));
        return obj;
    }

    public final int getSequenceCash(String host) throws BasicException {
        Integer i = (Integer) new StaticSentence(s, "SELECT MAX(HOSTSEQUENCE) FROM CLOSEDCASH WHERE HOST = ?", SerializerWriteString.INSTANCE, SerializerReadInteger.INSTANCE).find(host);
        return (i == null) ? 1 : i.intValue();
    }

    public final Object[] findActiveCash(String sActiveCashIndex) throws BasicException {
        return (Object[]) new StaticSentence(s, "SELECT HOST, HOSTSEQUENCE, DATESTART, DATEEND FROM CLOSEDCASH WHERE MONEY = ? AND DATEEND IS NULL", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[] { Datas.STRING, Datas.INT, Datas.TIMESTAMP, Datas.TIMESTAMP })).find(sActiveCashIndex);
    }

    public Object[] findPeopleActiveCash(String sActiveCashIndex) throws BasicException {
        return (Object[]) new StaticSentence(s, "SELECT HOST, HOSTSEQUENCE, DATESTART, DATEEND FROM CLOSEDCASH WHERE MONEY = ? AND DATEEND IS NULL AND DATESTART>= ? ", new SerializerWriteBasic(new Datas[] { Datas.STRING, Datas.TIMESTAMP }), new SerializerReadBasic(new Datas[] { Datas.STRING, Datas.INT, Datas.TIMESTAMP, Datas.TIMESTAMP })).find(new Object[] { sActiveCashIndex, TimeUtil.getCurrentDateNoTime() });
    }

    public final void execInsertCash(Object[] cash) throws BasicException {
        new StaticSentence(s, "INSERT INTO CLOSEDCASH(MONEY, HOST, HOSTSEQUENCE, DATESTART, DATEEND, NAME,LASTMODIFIED) " + "VALUES (?, ?, ?, ?, ?, ?,?)", new SerializerWriteBasic(new Datas[] { Datas.STRING, Datas.STRING, Datas.INT, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.TIMESTAMP })).exec(cash);
    }

    public Object[] findSystemCash(String cashName) throws BasicException {
        return (Object[]) new StaticSentence(s, "SELECT MONEY, HOSTSEQUENCE, DATESTART, DATEEND FROM CLOSEDCASH WHERE NAME = ? AND DATEEND IS NULL", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[] { Datas.STRING, Datas.INT, Datas.TIMESTAMP, Datas.TIMESTAMP })).find(cashName);
    }

    public final boolean isCashActive(String id) throws BasicException {
        return new PreparedSentence(s, "SELECT MONEY FROM CLOSEDCASH WHERE DATEEND IS NULL AND MONEY = ?", SerializerWriteString.INSTANCE, SerializerReadString.INSTANCE).find(id) != null;
    }

    @Override
    public Class getSuportClass() {
        return CloseCash.class;
    }

    public PaymentsModel loadInstance(String cashIndex) throws BasicException {
        PaymentsModel p = new PaymentsModel();
        Object[] valcash = (Object[]) new StaticSentence(s, "SELECT HOST, HOSTSEQUENCE, DATESTART, DATEEND, NAME, HANDCASH, MEMO FROM CLOSEDCASH WHERE MONEY = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[] { Datas.STRING, Datas.INT, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.DOUBLE, Datas.STRING })).find(cashIndex);
        p.setM_sHost((String) valcash[0]);
        p.setM_iSeq((Integer) valcash[1]);
        p.setM_dDateStart((Date) valcash[2]);
        p.setM_dDateEnd((Date) valcash[3]);
        p.setM_sName((String) valcash[4]);
        p.setM_dHandCash((Double) valcash[5]);
        p.setMemo((String) valcash[6]);
        Object[] valtickets = (Object[]) new StaticSentence(s, "SELECT COUNT(*), SUM(PAYMENTS.TOTAL) " + "FROM PAYMENTS, RECEIPTS " + "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEY = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[] { Datas.INT, Datas.DOUBLE })).find(cashIndex);
        if (valtickets == null) {
            p.setM_iPayments(0);
            p.setM_dPaymentsTotal(0.0);
        } else {
            p.setM_iPayments((Integer) valtickets[0]);
            p.setM_dPaymentsTotal((Double) valtickets[1]);
        }
        List l = new StaticSentence(s, "SELECT PAYMENTS.PAYMENT, SUM(PAYMENTS.TOTAL) " + "FROM PAYMENTS, RECEIPTS " + "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEY = ? " + "GROUP BY PAYMENTS.PAYMENT", SerializerWriteString.INSTANCE, new SerializerRead() {

            @Override
            public Object readValues(DataRead dr) throws BasicException {
                PaymentsLine obj = new PaymentsLine();
                obj.setM_PaymentType(dr.getString(1));
                obj.setM_PaymentValue(dr.getDouble(2));
                return obj;
            }
        }).list(cashIndex);
        if (l == null) {
            p.setM_lpayments(new ArrayList());
        } else {
            p.setM_lpayments(l);
        }
        Object[] recsales = (Object[]) new StaticSentence(s, "SELECT COUNT(DISTINCT RECEIPTS.ID), SUM(TICKETLINES.UNITS * TICKETLINES.PRICE) " + "FROM RECEIPTS, TICKETLINES WHERE RECEIPTS.ID = TICKETLINES.TICKET AND RECEIPTS.MONEY = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[] { Datas.INT, Datas.DOUBLE })).find(cashIndex);
        if (recsales == null) {
            p.setM_iSales(null);
            p.setM_dSalesBase(null);
        } else {
            p.setM_iSales((Integer) recsales[0]);
            p.setM_dSalesBase((Double) recsales[1]);
        }
        Object[] rectaxes = (Object[]) new StaticSentence(s, "SELECT SUM(TAXLINES.AMOUNT) " + "FROM RECEIPTS, TAXLINES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND RECEIPTS.MONEY = ?", SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[] { Datas.DOUBLE })).find(cashIndex);
        if (rectaxes == null) {
            p.setM_dSalesTaxes(null);
        } else {
            p.setM_dSalesTaxes((Double) rectaxes[0]);
        }
        List<SalesLine> asales = new StaticSentence(s, "SELECT TAXCATEGORIES.NAME, SUM(TAXLINES.AMOUNT) " + "FROM RECEIPTS, TAXLINES, TAXES, TAXCATEGORIES WHERE RECEIPTS.ID = TAXLINES.RECEIPT AND TAXLINES.TAXID = TAXES.ID AND TAXES.CATEGORY = TAXCATEGORIES.ID " + "AND RECEIPTS.MONEY = ?" + "GROUP BY TAXCATEGORIES.NAME", SerializerWriteString.INSTANCE, new SerializerRead() {

            @Override
            public Object readValues(DataRead dr) throws BasicException {
                SalesLine obj = new SalesLine();
                obj.setM_SalesTaxName(dr.getString(1));
                obj.setM_SalesTaxes(dr.getDouble(2));
                return obj;
            }
        }).list(cashIndex);
        if (asales == null) {
            p.setM_lsales(new ArrayList<SalesLine>());
        } else {
            p.setM_lsales(asales);
        }
        return p;
    }
}
