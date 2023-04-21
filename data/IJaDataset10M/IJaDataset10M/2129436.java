package com.gorillalogic.dal.teller;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.model.*;

class TableFactoryTeller extends BaseTeller implements TableFactory {

    private final TableFactory target;

    TableFactoryTeller(TableFactory target) {
        this.target = target;
    }

    String tellerKind() {
        return "factory";
    }

    String javaKind() {
        return "TableFactory";
    }

    String asNamedVar() {
        return "Table.factory";
    }

    public Table gclToTable(String gclExpr) throws AccessException {
        Sentence rhs = rd("gclToTable", str(gclExpr));
        Table rez = null;
        try {
            rez = target.gclToTable(gclExpr);
        } catch (AccessException e) {
            rhs.fault(e);
        } catch (RuntimeException e) {
            rhs.fault(e);
        }
        return rhs.defn(rez);
    }

    public Table gclToTable(String gclExpr, Table startingContextForExpr) throws AccessException {
        Sentence rhs = rd("gclToTable", str(gclExpr), str(startingContextForExpr));
        startingContextForExpr = TableTeller.unwrap(startingContextForExpr);
        Table rez = null;
        try {
            rez = target.gclToTable(gclExpr, startingContextForExpr);
        } catch (AccessException e) {
            rhs.fault(e);
        } catch (RuntimeException e) {
            rhs.fault(e);
        }
        return rhs.defn(rez);
    }

    public Table gclToTable(String gclExpr, boolean shortenPathAsNeeded) throws AccessException {
        Sentence rhs = rd("gclToTable", str(gclExpr), str(shortenPathAsNeeded));
        Table rez = null;
        try {
            rez = target.gclToTable(gclExpr, shortenPathAsNeeded);
        } catch (AccessException e) {
            rhs.fault(e);
        } catch (RuntimeException e) {
            rhs.fault(e);
        }
        return rhs.defn(rez);
    }

    public Table gclToTable(String gclExpr, Table startingContextForExpr, boolean shortenPathAsNeeded) throws AccessException {
        Sentence rhs = rd("gclToTable", str(gclExpr), str(startingContextForExpr), str(shortenPathAsNeeded));
        startingContextForExpr = TableTeller.unwrap(startingContextForExpr);
        Table rez = null;
        try {
            rez = target.gclToTable(gclExpr, startingContextForExpr, shortenPathAsNeeded);
        } catch (AccessException e) {
            rhs.fault(e);
        } catch (RuntimeException e) {
            rhs.fault(e);
        }
        return rhs.defn(rez);
    }

    public Table.Row gclToRow(String oidStr) throws AccessException {
        Sentence rhs = rd("gclToRow", str(oidStr));
        Table.Row rez = null;
        try {
            rez = target.gclToRow(oidStr);
        } catch (AccessException e) {
            rhs.fault(e);
        } catch (RuntimeException e) {
            rhs.fault(e);
        }
        return rhs.defn(rez);
    }

    public String gclToString(String gclExpr) throws AccessException {
        return target.gclToString(gclExpr);
    }

    public Expr gclToExpr(String gclExpr, Table startingContextForExpr) throws AccessException {
        Sentence rhs = rd("gclToExpr", str(gclExpr), str(startingContextForExpr));
        startingContextForExpr = TableTeller.unwrap(startingContextForExpr);
        Expr rez = null;
        try {
            rez = target.gclToExpr(gclExpr, startingContextForExpr);
        } catch (AccessException e) {
            rhs.fault(e);
        } catch (RuntimeException e) {
            rhs.fault(e);
        }
        return rhs.defn(rez);
    }

    public Table indexToTable(int tableIndex) {
        Sentence rhs = rd("indexToTable", str(tableIndex));
        Table rez = null;
        try {
            rez = target.indexToTable(tableIndex);
        } catch (RuntimeException e) {
            rhs.fault(e);
        }
        return rhs.defn(rez);
    }

    public Table makeTempTable() {
        Sentence rhs = wr("makeTempTable");
        Table rez = null;
        try {
            rez = target.makeTempTable();
        } catch (RuntimeException e) {
            rhs.fault(e);
        }
        return rhs.defn(rez);
    }

    public Table makeTempTable(String name) {
        Sentence rhs = wr("makeTempTable", str(name));
        Table rez = null;
        try {
            rez = target.makeTempTable(name);
        } catch (RuntimeException e) {
            rhs.fault(e);
        }
        return rhs.defn(rez);
    }

    public Table.Row makeTempRow() {
        Sentence rhs = wr("makeTempRow");
        Table.Row rez = null;
        try {
            rez = target.makeTempRow();
        } catch (RuntimeException e) {
            rhs.fault(e);
        }
        return rhs.defn(rez);
    }
}
