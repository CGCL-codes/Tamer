package com.vlee.bean.accounting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.TreeMap;
import java.util.Vector;
import com.vlee.ejb.accounting.BatchBean;
import com.vlee.ejb.accounting.GLCategoryNut;
import com.vlee.ejb.accounting.GLCategoryObject;
import com.vlee.ejb.accounting.GLCodeNut;
import com.vlee.ejb.accounting.GLCodeObject;
import com.vlee.ejb.accounting.GeneralLedgerNut;
import com.vlee.ejb.accounting.GeneralLedgerObject;
import com.vlee.ejb.accounting.JournalEntryObject;
import com.vlee.ejb.accounting.JournalTransactionBean;
import com.vlee.ejb.accounting.JournalTransactionNut;
import com.vlee.ejb.accounting.JournalTransactionObject;
import com.vlee.ejb.accounting.ProfitCostCenterNut;
import com.vlee.ejb.accounting.ProfitCostCenterObject;
import com.vlee.util.*;

public class AddJTxnSession extends java.lang.Object implements Serializable {

    static final long serialVersionUID = 0;

    private ProfitCostCenterObject pccObj = null;

    private Integer batch = BatchBean.PKID_DEFAULT;

    private Timestamp transactionDate = null;

    private Integer userId = null;

    private String description = "";

    private String docRef = "";

    private Long docKey = new Long(0);

    private TreeMap treeRows = null;

    private Vector prevJTxn = new Vector();

    private Vector vecSimilarDescription;

    public AddJTxnSession(Integer theUserId) {
        this.userId = theUserId;
        init();
    }

    private void init() {
        this.transactionDate = TimeFormat.getTimestamp();
        this.treeRows = new TreeMap();
        this.description = "";
        this.docRef = "";
        this.docKey = new Long(0);
        this.vecSimilarDescription = new Vector();
    }

    public synchronized void setPCCenter(Integer pcc) throws Exception {
        this.pccObj = ProfitCostCenterNut.getObject(pcc);
    }

    public TreeMap getRows() {
        return this.treeRows;
    }

    public String getDocRef() {
        return this.docRef;
    }

    public String getDocKey(String buf) {
        if (this.docKey.longValue() == 0) {
            return "";
        } else {
            return this.docKey.toString();
        }
    }

    public void setDocument(String docRef, String strKey) {
        if (docRef.length() < 1) {
            this.docKey = new Long(0);
            return;
        }
        try {
            this.docKey = new Long(strKey);
        } catch (Exception ex) {
            this.docKey = new Long(0);
            this.docRef = "";
            return;
        }
        this.docRef = docRef;
        this.docKey = new Long(strKey);
    }

    public void setDescription(String desc) {
        this.description = desc.trim();
        this.vecSimilarDescription.clear();
        if (this.description.length() > 3) {
            QueryObject query = new QueryObject(new String[] { JournalTransactionBean.DESCRIPTION + " ~* '" + this.description + "' " });
            query.setOrder(" ORDER BY " + JournalTransactionBean.PKID);
            query.setLimit(4);
            this.vecSimilarDescription = new Vector(JournalTransactionNut.getObjects(query));
        }
    }

    public Vector getSimilarDescription() {
        return this.vecSimilarDescription;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDate(String buf) {
        return TimeFormat.strDisplayDate(this.transactionDate);
    }

    public void setDate(Timestamp tsDate) {
        this.transactionDate = tsDate;
    }

    public int balanceSignum() {
        Vector vecRows = new Vector(this.treeRows.values());
        BigDecimal balance = new BigDecimal(0);
        for (int cnt1 = 0; cnt1 < vecRows.size(); cnt1++) {
            Row theRow = (Row) vecRows.get(cnt1);
            balance = balance.add(theRow.amount);
        }
        return balance.signum();
    }

    public int absoluteSignum() {
        Vector vecRows = new Vector(this.treeRows.values());
        BigDecimal absolute = new BigDecimal(0);
        for (int cnt1 = 0; cnt1 < vecRows.size(); cnt1++) {
            Row theRow = (Row) vecRows.get(cnt1);
            absolute = absolute.add(theRow.amount.abs());
        }
        return absolute.signum();
    }

    public BigDecimal absoluteAmt() {
        Vector vecRows = new Vector(this.treeRows.values());
        BigDecimal absolute = new BigDecimal(0);
        for (int cnt1 = 0; cnt1 < vecRows.size(); cnt1++) {
            Row theRow = (Row) vecRows.get(cnt1);
            if (theRow.amount.signum() > 0) {
                absolute = absolute.add(theRow.amount.abs());
            }
        }
        return absolute;
    }

    public boolean canSave() {
        if (this.pccObj == null) {
            return false;
        }
        if (balanceSignum() != 0) {
            return false;
        }
        if (absoluteSignum() == 0) {
            return false;
        }
        return true;
    }

    public synchronized void saveNewJTxn() throws Exception {
        if (!canSave()) {
            return;
        }
        if (balanceSignum() != 0) {
            throw new Exception("The debit and credit total are not balanced!");
        }
        JournalTransactionObject jtxnObj = new JournalTransactionObject();
        jtxnObj.txnCode = JournalTransactionBean.TXNCODE_MANUAL;
        jtxnObj.pcCenterId = this.pccObj.mPkid;
        jtxnObj.batchId = this.batch;
        jtxnObj.typeId = JournalTransactionBean.TYPEID_POSTED;
        jtxnObj.name = this.description;
        jtxnObj.description = this.description;
        jtxnObj.amount = absoluteAmt();
        jtxnObj.transactionDate = this.transactionDate;
        jtxnObj.docRef = this.docRef;
        jtxnObj.docKey = this.docKey;
        jtxnObj.userIdCreate = this.userId;
        jtxnObj.userIdEdit = this.userId;
        jtxnObj.userIdCancel = this.userId;
        jtxnObj.timestampCreate = TimeFormat.getTimestamp();
        jtxnObj.timestampEdit = TimeFormat.getTimestamp();
        jtxnObj.timestampCancel = TimeFormat.getTimestamp();
        Vector vecRows = new Vector(this.treeRows.values());
        ProfitCostCenterObject pccObj = ProfitCostCenterNut.getObject(this.pccObj.mPkid);
        for (int cnt1 = 0; cnt1 < vecRows.size(); cnt1++) {
            Row theRow = (Row) vecRows.get(cnt1);
            GeneralLedgerObject glObj = GeneralLedgerNut.getObjectFailSafe(theRow.glcode, this.batch, this.pccObj.mPkid, pccObj.mCurrency);
            JournalEntryObject jeObj = new JournalEntryObject();
            jeObj.glId = glObj.pkId;
            jeObj.description = theRow.itemDescription;
            System.out.println("---------------GL DESCRIPTION" + theRow.itemDescription);
            jeObj.currency = pccObj.mCurrency;
            jeObj.amount = theRow.amount;
            jtxnObj.vecEntry.add(jeObj);
        }
        JournalTransactionNut.fnCreate(jtxnObj);
        this.prevJTxn.add(jtxnObj);
        this.treeRows.clear();
        init();
    }

    public Vector getPrevJTxn() {
        return this.prevJTxn;
    }

    public void addItem(String glCode, String itemDescription, BigDecimal debit, BigDecimal credit) throws Exception {
        if (debit.signum() <= 0 && credit.signum() <= 0) {
            throw new Exception("Both debit and credit amount are ZERO!");
        }
        GLCodeObject glCodeObj = GLCodeNut.getValueObjectByCode(glCode);
        if (glCodeObj == null) {
            throw new Exception(" Invalid GL Code! ");
        }
        GLCategoryObject glCatObj = GLCategoryNut.getObject(glCodeObj.glCategoryId);
        if (glCatObj == null) {
            throw new Exception(" Invalid GL Category!");
        }
        Row theRow = new Row();
        GUIDGenerator gen = new GUIDGenerator();
        theRow.key = glCatObj.postToSection + glCodeObj.code + gen.getUUID().toString();
        theRow.glcode = glCodeObj.code;
        theRow.name = glCatObj.name + " " + glCodeObj.name;
        theRow.postSection = glCatObj.postToSection;
        theRow.itemDescription = itemDescription;
        System.out.println("---------------ITEM DESCRIPTION" + theRow.itemDescription);
        if (debit.signum() > 0) {
            theRow.amount = debit;
        }
        if (credit.signum() > 0) {
            theRow.amount = credit.negate();
        }
        this.treeRows.put(theRow.key, theRow);
        Log.printVerbose("ROW COUNT: " + this.treeRows.size());
    }

    public void removeItem(String key) {
        this.treeRows.remove(key);
    }

    public String getPCCenter(String buf) {
        if (this.pccObj != null) {
            buf = this.pccObj.mPkid.toString();
        }
        return buf;
    }

    public ProfitCostCenterObject getPCCenter() {
        return this.pccObj;
    }

    public boolean validPCCenter() {
        if (this.pccObj == null) {
            return false;
        } else {
            return true;
        }
    }

    public static class Row {

        public String key;

        public String glcode;

        public String name;

        public String description;

        public String postSection;

        public String itemDescription;

        public BigDecimal amount;

        public Row() {
            this.key = "";
            this.glcode = "";
            this.name = "";
            this.description = "";
            this.postSection = "";
            this.itemDescription = "";
            this.amount = new BigDecimal(0);
        }
    }
}
