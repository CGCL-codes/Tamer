package com.daffodilwoods.daffodildb.server.sql99.ddl.revoke;

import java.util.*;

public class PDCharacterstics extends _PDCharacterstics {

    private HashMap dependents;

    private HashMap ancestors;

    private int pdType;

    private int status;

    private boolean independentNode;

    public PDCharacterstics() {
        dependents = null;
        ancestors = null;
        pdType = 0;
        status = 0;
        independentNode = false;
    }

    public HashMap getDependents() {
        return dependents;
    }

    public void setDependents(HashMap depen) {
        dependents = depen;
    }

    public HashMap getAncestor() {
        return ancestors;
    }

    public void setAncestor(HashMap ances) {
        ancestors = ances;
    }

    public int getPDType() {
        return pdType;
    }

    public void setPDType(int type) {
        pdType = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int stat) {
        status = stat;
    }

    public boolean isIndependent() {
        return independentNode;
    }

    public void setIndependent(boolean value) {
        independentNode = value;
    }

    public String toString() {
        return "PDCharacterstics :: privilegeType=" + pdType + "  status=" + status + "  isIndependentNode=" + independentNode;
    }
}
