package com.daffodilwoods.daffodildb.server.sql99.dcl.sqltransactionstatement;

import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.daffodildb.server.sessionsystem.*;
import com.daffodilwoods.daffodildb.server.sql99.token.*;

public class transactioncharacteristics implements com.daffodilwoods.daffodildb.utils.parser.StatementExecuter {

    public transactionmode[] _OptRepScomma94843605transactionmode0;

    public transactionmode _transactionmode1;

    public SRESERVEDWORD1206543922 _SRESERVEDWORD12065439222;

    public Object run(Object object) throws com.daffodilwoods.database.resource.DException {
        ServerSession serverSession = (ServerSession) object;
        SessionTransactionMode transMode = new SessionTransactionMode();
        boolean ok = checkSyntax(transMode, object);
        if (!ok) {
            throw new Error("Syntax Error ...");
        }
        if (transMode.getTransactionAccessMode() == null) {
            if (((String) transMode.getIsolationLevel()).equalsIgnoreCase((String) SessionCharacteristics.READUNCOMMIT)) {
                transMode.setTransactionAccessMode("Read Only");
            } else {
                transMode.setTransactionAccessMode("Read Write");
            }
        }
        _UserSession userSession = serverSession.getUserSession();
        userSession.setTransactionMode(transMode);
        return transMode;
    }

    private boolean checkSyntax(SessionTransactionMode transMode, Object object) throws com.daffodilwoods.database.resource.DException {
        if (_OptRepScomma94843605transactionmode0 == null) {
            if (_transactionmode1 instanceof transactionaccessmode) {
                transMode.setTransactionAccessMode(_transactionmode1.run(object));
            }
            if (_transactionmode1 instanceof isolationlevel) {
                transMode.setIsolationLevel(((String) _transactionmode1.run(object)).toUpperCase());
            }
            if (_transactionmode1 instanceof diagnosticssize) {
                transMode.setDiagnosticsSize(_transactionmode1.run(object));
            }
            return true;
        }
        transactionmode[] returnedtransModes = (transactionmode[]) _OptRepScomma94843605transactionmode0;
        int length = returnedtransModes.length;
        transactionmode[] transactionModes = new transactionmode[length + 1];
        transactionModes[0] = _transactionmode1;
        System.arraycopy(returnedtransModes, 0, transactionModes, 1, length);
        return checkForMoreThanOnce(transactionModes, transMode, object);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append(_SRESERVEDWORD12065439222);
        sb.append(" ");
        sb.append(_transactionmode1);
        sb.append(" ");
        if (_OptRepScomma94843605transactionmode0 != null) {
            for (int i = 0; i < _OptRepScomma94843605transactionmode0.length; i++) {
                sb.append(",").append(_OptRepScomma94843605transactionmode0[i]);
            }
        }
        return sb.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return this;
    }

    private boolean checkForMoreThanOnce(transactionmode[] transactionmode, SessionTransactionMode transMode, Object object) throws com.daffodilwoods.database.resource.DException {
        int isolation = 0, tranactionAcess = 0, diagnostic = 0;
        for (int i = 0; i < transactionmode.length; i++) {
            if (transactionmode[i] instanceof isolationlevel) {
                isolation++;
                transMode.setIsolationLevel(((String) transactionmode[i].run(object)).toUpperCase());
            }
            if (transactionmode[i] instanceof transactionaccessmode) {
                tranactionAcess++;
                transMode.setTransactionAccessMode(transactionmode[i].run(object));
            }
            if (transactionmode[i] instanceof diagnosticssize) {
                diagnostic++;
                transMode.setDiagnosticsSize(transactionmode[i].run(object));
            }
        }
        return (isolation <= 1 && tranactionAcess <= 1 && diagnostic <= 1);
    }
}
