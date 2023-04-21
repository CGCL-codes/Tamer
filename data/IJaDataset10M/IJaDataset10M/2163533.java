package org.javaldap.server.operation;

import java.util.*;
import java.math.BigInteger;
import org.javaldap.ldapv3.*;
import org.javaldap.server.Entry;
import org.javaldap.server.Credentials;
import org.javaldap.server.backend.BackendHandler;
import org.javaldap.server.util.DirectorySchemaViolation;
import org.javaldap.server.util.InvalidDNException;
import org.javaldap.server.syntax.DirectoryString;

public class AddOperation implements Operation {

    LDAPMessage request = null;

    LDAPMessage response = null;

    Credentials creds = null;

    public AddOperation(Credentials creds, LDAPMessage request) {
        this.request = request;
        this.creds = creds;
    }

    public LDAPMessage getResponse() {
        return this.response;
    }

    public void perform() {
        this.response = new LDAPMessage();
        AddResponse addResponse = new AddResponse();
        addResponse.matchedDN = new byte[0];
        addResponse.errorMessage = new byte[0];
        addResponse.referral = new Referral();
        try {
            addResponse.resultCode = BackendHandler.Handler().add(creds, requestToEntry());
        } catch (DirectorySchemaViolation dsv) {
            addResponse.resultCode = new LDAPResultEnum(65);
            addResponse.errorMessage = dsv.getMessage().getBytes();
        } catch (InvalidDNException ide) {
            addResponse.resultCode = new LDAPResultEnum(34);
            if (ide.getMessage() != null) {
                addResponse.errorMessage = ide.getMessage().getBytes();
            }
        }
        LDAPMessageChoice op = new LDAPMessageChoice();
        op.choiceId = LDAPMessageChoice.ADDRESPONSE_CID;
        op.addResponse = addResponse;
        this.response.messageID = new BigInteger(this.request.messageID.toString());
        this.response.protocolOp = op;
    }

    public Entry requestToEntry() throws InvalidDNException {
        Entry entry = new Entry(new DirectoryString(this.request.protocolOp.addRequest.entry));
        AttributeList attrList = this.request.protocolOp.addRequest.attributes;
        for (Enumeration enumAttr = attrList.elements(); enumAttr.hasMoreElements(); ) {
            AttributeListSeq als = (AttributeListSeq) enumAttr.nextElement();
            DirectoryString type = new DirectoryString(als.type);
            als.type = null;
            Vector values = new Vector();
            for (Enumeration enumVal = als.vals.elements(); enumVal.hasMoreElements(); ) {
                byte[] thisVal = (byte[]) enumVal.nextElement();
                if (thisVal.length > 0) values.addElement(new DirectoryString(thisVal));
            }
            als.vals = null;
            if (values.size() > 0) {
                entry.put(type, values);
            }
        }
        return entry;
    }
}
