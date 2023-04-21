package net.mjrz.fm.utils;

import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.mjrz.fm.constants.AccountTypes;
import net.mjrz.fm.entity.FManEntityManager;
import net.mjrz.fm.entity.beans.Account;
import net.mjrz.fm.entity.beans.Transaction;
import net.mjrz.fm.entity.beans.User;
import net.mjrz.fm.services.SessionManager;
import net.mjrz.fm.ui.FinanceManagerUI;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author Mjrz contact@mjrz.net
 *
 */
public class XMLProcessor {

    private static Document document;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private Account account;

    private FManEntityManager fman;

    private User user;

    private ArrayList<Transaction_> txList;

    private InputSource src = null;

    private String sourceType = null;

    private ArrayList accountsList = null;

    private BigDecimal ledgerBalance = null;

    private String balanceAsOfDate = null;

    private static Logger logger = Logger.getLogger(XMLProcessor.class.getName());

    public XMLProcessor(String xml, User user) throws Exception {
        StringReader in = null;
        txList = new ArrayList<Transaction_>();
        this.user = user;
        try {
            fman = new FManEntityManager();
            in = new StringReader(xml);
            src = new InputSource(in);
            accountsList = (ArrayList) fman.getAccountsForUser(SessionManager.getSessionUserId());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(src);
        } catch (Exception e) {
            throw e;
        } finally {
            if (in != null) in.close();
        }
    }

    public String validate() {
        String msg = "";
        NodeList nlist = document.getElementsByTagName("CODE");
        int i = 0;
        for (; i < nlist.getLength(); i++) {
            Node n = nlist.item(i).getParentNode();
            if (n != null) {
                Node gp = n.getParentNode();
                String nname = gp.getNodeName();
                if (nname.equalsIgnoreCase("STMTTRNRS")) {
                    break;
                }
                if (nname.equalsIgnoreCase("CCSTMTTRNRS")) {
                    break;
                }
            }
        }
        if (i >= nlist.getLength()) {
            msg = "No transactions to import";
            return msg;
        }
        if (i < nlist.getLength()) {
            Node x = nlist.item(i);
            if (!x.getTextContent().trim().equals("0")) {
                Node parent = x.getParentNode();
                NodeList nl = parent.getChildNodes();
                for (int j = 0; j < nl.getLength(); j++) {
                    Node tmp = nl.item(j);
                    if (tmp.getNodeName().equalsIgnoreCase("MESSAGE")) {
                        return tmp.getTextContent();
                    }
                }
                return "Unknown error";
            }
        }
        return "";
    }

    public void processXML() throws Exception {
        try {
            String from = getAccountNumber();
            account = fman.getAccountFromNumber(user.getUid(), from);
            if (account == null) {
                throw new IllegalArgumentException("Account doesnot exist yet");
            }
            getTransactions();
        } catch (Exception e) {
            throw e;
        }
    }

    private void getTransactions() {
        NodeList list = document.getElementsByTagName("STMTTRN");
        int sz = list.getLength();
        for (int i = 0; i < sz; i++) {
            buildTxObject(list.item(i));
        }
    }

    private void getLedgerBal() {
        NodeList nlist = document.getElementsByTagName("LEDGERBAL");
        int i = 0;
        for (; i < nlist.getLength(); i++) {
            Node n = nlist.item(i);
            NodeList children = n.getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                Node x = children.item(j);
                if (x.getNodeType() == Node.ELEMENT_NODE) {
                    String val = x.getTextContent();
                    if (x.getNodeName().equals("BALAMT")) {
                        ledgerBalance = new BigDecimal(val);
                    }
                    if (x.getNodeName().equals("DTASOF")) {
                        balanceAsOfDate = val;
                    }
                }
            }
        }
    }

    public ArrayList<Transaction_> getTxList() {
        return txList;
    }

    public BigDecimal getLedgerBalance() {
        getLedgerBal();
        return ledgerBalance;
    }

    private void buildTxObject(Node n) {
        NodeList children = n.getChildNodes();
        BigDecimal amt = new BigDecimal(0d);
        Date dt = new Date();
        String dest = String.valueOf("");
        String notes = String.valueOf("");
        String fitid = String.valueOf("");
        for (int j = 0; j < children.getLength(); j++) {
            Node x = children.item(j);
            if (x.getNodeType() == Node.ELEMENT_NODE) {
                String val = x.getTextContent();
                if (x.getNodeName().equals("TRNAMT")) {
                    amt = new BigDecimal(val);
                    continue;
                }
                if (x.getNodeName().equals("DTPOSTED")) {
                    try {
                        dt = sdf.parse(parseDate(val));
                    } catch (java.text.ParseException e) {
                        System.out.println(e.getMessage());
                        dt = new Date();
                    }
                    continue;
                }
                if (x.getNodeName().equals("NAME")) {
                    dest = val;
                    if (dest.length() >= 30) {
                        dest = dest.substring(0, 30);
                    }
                    continue;
                }
                if (x.getNodeName().equals("MEMO")) {
                    notes = val;
                    continue;
                }
                if (x.getNodeName().equals("FITID")) {
                    fitid = val;
                    continue;
                }
            }
        }
        try {
            Account destAcct = fman.getAccount(user.getUid(), dest);
            int asz = accountsList.size();
            for (int i = 0; i < asz; i++) {
                Account tmp = (Account) accountsList.get(i);
                double match = MiscUtils.getPercentMatch(dest, tmp.getAccountName());
                if (match >= 50) destAcct = tmp;
            }
            if (destAcct == null) {
                if (amt.doubleValue() > 0) {
                    if (sourceType.equals("BANKACCT")) {
                        destAcct = createDestinationAccount(AccountTypes.ACCT_TYPE_INCOME, AccountTypes.ACCT_TYPE_INCOME, dest, dest);
                    }
                    if (sourceType.equals("CCACCT")) {
                        destAcct = createDestinationAccount(AccountTypes.ACCT_TYPE_CASH, AccountTypes.ACCT_TYPE_CASH, dest, dest);
                    }
                } else {
                    destAcct = createDestinationAccount(AccountTypes.ACCT_TYPE_EXPENSE, AccountTypes.ACCT_TYPE_EXPENSE, dest, dest);
                }
                accountsList.add(destAcct);
            }
            Transaction_ t = new Transaction_();
            t.setInitiatorId(user.getUid());
            t.setTxDate(dt);
            if (amt.doubleValue() < 0) {
                t.setTxAmount(amt.negate());
            } else {
                t.setTxAmount(amt);
            }
            t.setCreateDate(new Date());
            t.setFitid(fitid);
            t.setTxNotes(dest + " - " + notes);
            t.setActivityBy("Created by OXF file import");
            if (amt.doubleValue() > 0 && destAcct.getAccountType() == AccountTypes.ACCT_TYPE_INCOME) {
                t.toAccount = account;
                t.fromAccount = (Account) destAcct.clone();
            } else if (amt.doubleValue() > 0) {
                t.toAccount = account;
                t.fromAccount = (Account) destAcct.clone();
            } else {
                t.toAccount = (Account) destAcct.clone();
                t.fromAccount = account;
            }
            txList.add(t);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Account getAccount() {
        if (account == null) {
            String from = getAccountNumber();
            try {
                Account account = fman.getAccountFromNumber(user.getUid(), from);
                return account;
            } catch (Exception e) {
                logger.error(MiscUtils.stackTrace2String(e));
            }
        }
        return account;
    }

    public String getAccountNumber() {
        NodeList nlist = document.getElementsByTagName("BANKACCTFROM");
        sourceType = "BANKACCT";
        if (nlist == null || nlist.getLength() == 0) {
            nlist = document.getElementsByTagName("CCACCTFROM");
            sourceType = "CCACCT";
        }
        if (nlist != null && nlist.getLength() > 0) {
            Node n = nlist.item(0);
            NodeList children = n.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node x = children.item(i);
                if (x.getNodeName().equals("ACCTID")) {
                    return x.getTextContent();
                }
            }
        }
        return null;
    }

    private Account createDestinationAccount(int type, int catid, String aname, String memo) throws Exception {
        Account a = new Account();
        a.setAccountName(aname);
        a.setAccountNotes(aname);
        a.setAccountType(type);
        a.setCategoryId(Long.valueOf(catid));
        a.setCurrentBalance(new BigDecimal(0d));
        a.setStartDate(new Date());
        a.setStartingBalance(new BigDecimal(0d));
        a.setAccountParentType(a.getAccountType());
        a.setStatus(AccountTypes.ACCOUNT_ACTIVE);
        return a;
    }

    private String parseDate(String in) throws ParseException {
        StringBuffer dt = new StringBuffer();
        if (in.length() < 8) throw new ParseException("Invalid date format", in.length());
        dt.append(in.substring(0, 4));
        dt.append("-");
        dt.append(in.substring(4, 6));
        dt.append("-");
        dt.append(in.substring(6, 8));
        return dt.toString();
    }

    public class Transaction_ extends Transaction {

        Account fromAccount;

        Account toAccount;

        public Account getFromAccount() {
            return fromAccount;
        }

        public Account getToAccount() {
            return toAccount;
        }

        public String getFromName() {
            return fromAccount.getAccountName();
        }

        public String getToName() {
            return toAccount.getAccountName();
        }
    }
}
