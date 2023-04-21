package org.javaldap.server.operation;

import java.util.*;
import org.javaldap.ldapv3.*;

public class CompareOperation implements Operation {

    LDAPMessage request = null;

    LDAPMessage response = null;

    public CompareOperation(LDAPMessage request) {
        this.request = request;
    }

    public LDAPMessage getResponse() {
        return this.response;
    }

    public void perform() {
    }
}
