package com.citep.formats.input.gnucash;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import com.citep.formats.input.accounts.AccountImportSink;
import com.citep.formats.input.accounts.ImportedAccount;
import com.citep.business.Account;
import com.citep.business.AccountTypes;

public class GNUCashAccountsXMLHandler implements ContentHandler {

    private AccountImportSink listener;

    private ImportedAccount currentAccount;

    private STATE state;

    private StringBuffer buf;

    public GNUCashAccountsXMLHandler(AccountImportSink listener) {
        this.listener = listener;
        buf = new StringBuffer();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        buf.append(ch, start, length);
    }

    public void endDocument() throws SAXException {
        System.out.println("End document: process now");
        if (listener != null) listener.onDocumentEnd();
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        String data = buf.toString().trim();
        if ("gnc:account".equals(qName)) {
            if (listener != null) {
                listener.onImportAccount(currentAccount);
            }
            currentAccount = new ImportedAccount();
        } else if ("act:id".equals(qName)) {
            currentAccount.setImportId(data);
        } else if ("act:type".equals(qName)) {
            setAccountType(data);
        } else if ("act:description".equals(qName)) {
            currentAccount.setDescription(data);
        } else if ("act:parent".equals(qName)) {
            currentAccount.setImportParentId(data);
        } else if ("act:name".equals(qName)) {
            currentAccount.setName(data);
        }
        clearBuffer();
    }

    private void setAccountType(String data) {
        if ("ASSET".equals(data)) {
            currentAccount.setType(AccountTypes.ASSETS);
        } else if ("BANK".equals(data)) {
            currentAccount.setType(AccountTypes.ASSETS);
        } else if ("CASH".equals(data)) {
            currentAccount.setType(AccountTypes.ASSETS);
        } else if ("ASSET".equals(data)) {
            currentAccount.setType(AccountTypes.ASSETS);
        } else if ("CREDIT".equals(data)) {
            currentAccount.setType(AccountTypes.LIABILITY);
        } else if ("LIABILITY".equals(data)) {
            currentAccount.setType(AccountTypes.LIABILITY);
        } else if ("PAYABLE".equals(data)) {
            currentAccount.setType(AccountTypes.LIABILITY);
        } else if ("RECEIVABLE".equals(data)) {
            currentAccount.setType(AccountTypes.ASSETS);
        } else if ("STOCK".equals(data)) {
            currentAccount.setType(AccountTypes.ASSETS);
        } else if ("MUTUAL".equals(data)) {
            currentAccount.setType(AccountTypes.ASSETS);
        } else if ("INCOME".equals(data)) {
            currentAccount.setType(AccountTypes.INCOME);
        } else if ("EXPENSE".equals(data)) {
            currentAccount.setType(AccountTypes.EXPENSES);
        } else if ("EQUITY".equals(data)) {
            currentAccount.setType(AccountTypes.EQUITY);
        } else if ("CURRENCY".equals(data)) {
            currentAccount.setType(AccountTypes.ASSETS);
        }
    }

    private void clearBuffer() {
        buf.delete(0, buf.length());
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    public void processingInstruction(String target, String data) throws SAXException {
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void skippedEntity(String name) throws SAXException {
    }

    public void startDocument() throws SAXException {
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if ("gnc:account".equals(qName)) {
            currentAccount = new ImportedAccount();
        } else if ("act:id".equals(qName)) {
            state = STATE.ID;
        } else if ("act:type".equals(qName)) {
            state = STATE.TYPE;
        } else if ("act:description".equals(qName)) {
            state = STATE.DESCRIPTION;
        } else if ("act:parent".equals(qName)) {
            state = STATE.PARENT;
        }
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    private enum STATE {

        ID, TYPE, DESCRIPTION, NAME, PARENT
    }
}
