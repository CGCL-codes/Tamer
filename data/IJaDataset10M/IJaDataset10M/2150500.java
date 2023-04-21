package com.gorillalogic.accounts.authentication;

import com.gorillalogic.accounts.*;
import com.gorillalogic.dal.*;
import org.w3c.dom.Element;

class SignatureAuthenticationStrategy extends CommonAuthenticationStrategy {

    public String getString() {
        return "signature";
    }

    class TicketImpl extends CommonTicket {

        private boolean valid = false;

        TicketImpl(GXEAccount account, Element signature, Element body) throws InvalidPasswordException, OperationException {
            super(account);
        }

        public boolean valid() {
            return valid;
        }

        private void authenticateFor(GXEAccount account, String passwordToCheck) throws InvalidPasswordException, OperationException {
            throw new UnsupportedException("Sig auth");
        }
    }
}
