package com.perfitrak.webservice.helpers;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import com.perfitrak.webservice.Account;
import com.perfitrak.webservice.BindingStub;
import com.perfitrak.webservice.PerfiTrakServiceLocator;
import com.perfitrak.webservice.Tag;
import com.perfitrak.webservice.TagBudget;
import com.perfitrak.webservice.Transaction;
import com.perfitrak.webservice.TransactionMetaData;

public class PerfiTrakManager {

    private static final String PERFITRAK_URL = "http://www.perfitrak.com/api/service.php";

    private static final int API_VERSION = 1;

    private URL pftUrl;

    private BindingStub pftStub;

    private String openIdUri;

    private String passCode;

    private List<Account> accounts;

    private List<Tag> tags;

    private List<TagBudget> tagBudgets;

    private List<Transaction> transactions;

    public PerfiTrakManager() {
        try {
            pftUrl = new URL(PERFITRAK_URL);
        } catch (MalformedURLException ex) {
        }
    }

    public String getOpenIdUri() {
        return openIdUri;
    }

    public boolean connect(String openIdUri, String passCode) throws RemoteException, WrongAPIVersionException {
        this.openIdUri = openIdUri;
        this.passCode = passCode;
        pftStub = new BindingStub(pftUrl, new PerfiTrakServiceLocator());
        int apiVersion = pftStub.getAPIVersion(openIdUri, passCode);
        if (apiVersion > API_VERSION) throw new WrongAPIVersionException(API_VERSION, apiVersion);
        return true;
    }

    public Account getAccount(int accountId) throws RemoteException {
        for (Account pftAccount : getAccounts()) if (pftAccount.getAccountId() == accountId) return pftAccount;
        return null;
    }

    public List<Account> getAccounts() throws RemoteException {
        if (accounts == null) accounts = new ArrayList<Account>(Arrays.asList(pftStub.getAccounts(openIdUri, passCode)));
        return accounts;
    }

    public Account saveAccount(Account account) throws RemoteException {
        if (account.getAccountId() == null) {
            account = pftStub.saveAccount(openIdUri, passCode, account);
            accounts.add(account);
        } else {
            pftStub.saveAccount(openIdUri, passCode, account);
        }
        return account;
    }

    public Tag getTag(int tagId) throws RemoteException {
        for (Tag pftTag : getTags()) if (pftTag.getTagId() == tagId) return pftTag;
        return null;
    }

    public List<Tag> getTags() throws RemoteException {
        if (tags == null) tags = new ArrayList<Tag>(Arrays.asList(pftStub.getTags(openIdUri, passCode)));
        return tags;
    }

    public Tag saveTag(Tag tag) throws RemoteException {
        if (tag.getTagId() == null) {
            tag = pftStub.saveTag(openIdUri, passCode, tag);
            tags.add(tag);
        } else {
            pftStub.saveTag(openIdUri, passCode, tag);
        }
        return tag;
    }

    public List<TagBudget> getTagBudgets() throws RemoteException {
        if (tagBudgets == null) tagBudgets = new ArrayList<TagBudget>(Arrays.asList(pftStub.getTagBudgets(openIdUri, passCode)));
        return tagBudgets;
    }

    public List<TagBudget> getTagBudgets(int tagId) throws RemoteException {
        List<TagBudget> tagsBudgets = new ArrayList<TagBudget>();
        for (TagBudget tb : getTagBudgets()) if (tb.getTagId() == tagId) tagsBudgets.add(tb);
        return tagsBudgets;
    }

    public TagBudget getTagBudget(int tagId, Date startDate) throws RemoteException {
        for (TagBudget tb : getTagBudgets()) if (tb.getTagId() == tagId && tb.getStart().equals(startDate)) return tb;
        return null;
    }

    public void saveTagBudget(TagBudget tagBudget) throws RemoteException {
        pftStub.saveTagBudget(openIdUri, passCode, tagBudget);
        if (!tagBudgets.contains(tagBudget)) tagBudgets.add(tagBudget);
    }

    public void deleteTagBudget(TagBudget tagBudget) throws RemoteException {
        pftStub.deleteTagBudget(openIdUri, passCode, tagBudget);
        tagBudgets.remove(tagBudget);
    }

    public Transaction getTransaction(int transId, boolean allowProxy) throws RemoteException {
        for (Transaction pftTrans : transactions) if (pftTrans.getTransactionId() == transId && (allowProxy || !(pftTrans instanceof TransactionProxy))) return pftTrans;
        int transIndex = findTransIndex(transId);
        Transaction[] transArray = pftStub.getTransactions(openIdUri, passCode, new int[] { transId });
        if (transArray != null && transArray.length > 0) {
            Transaction trans = transArray[0];
            if (transIndex >= 0) transactions.set(transIndex, trans);
            return trans;
        } else throw new RuntimeException("apparently the transaction with ID " + transId + " doesn't exist");
    }

    public List<Transaction> getTransactions(boolean onlyProxies) throws RemoteException {
        if (transactions == null) {
            if (onlyProxies) {
                List<TransactionMetaData> transMetaDataList = Arrays.asList(pftStub.findTransactionMetaData(openIdUri, passCode, null, null, null, null));
                transactions = new ArrayList<Transaction>(transMetaDataList.size());
                for (TransactionMetaData transMetaData : transMetaDataList) transactions.add(new TransactionProxy(this, transMetaData));
            } else transactions = new ArrayList<Transaction>(Arrays.asList(pftStub.findTransactions(openIdUri, passCode, null, null, null, null)));
        }
        return transactions;
    }

    public Transaction saveTransaction(Transaction trans) throws RemoteException {
        if (trans.getTransactionId() == null) {
            trans = pftStub.saveTransaction(openIdUri, passCode, trans);
            transactions.add(trans);
        } else {
            int transIndex = findTransIndex(trans.getTransactionId());
            trans = pftStub.saveTransaction(openIdUri, passCode, trans);
            transactions.set(transIndex, trans);
        }
        return trans;
    }

    public void deleteTransaction(int transId) throws RemoteException {
        int transIndex = findTransIndex(transId);
        pftStub.deleteTransaction(openIdUri, passCode, transId);
        transactions.remove(transIndex);
    }

    private int findTransIndex(int transId) {
        for (int i = 0; i < transactions.size(); i++) if (transactions.get(i).getTransactionId() == transId) return i;
        return -1;
    }

    public Stack<Tag> getTagAncestors(Tag pftTag, boolean includeChild) throws RemoteException {
        Stack<Tag> path = new Stack<Tag>();
        if (includeChild) path.add(pftTag);
        while (pftTag.getParentId() != null) {
            pftTag = getTag(pftTag.getParentId());
            path.add(pftTag);
        }
        return path;
    }
}
