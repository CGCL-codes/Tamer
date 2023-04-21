package org.maverickdbms.resolver.in2;

import org.maverickdbms.basic.ConstantString;
import org.maverickdbms.basic.MaverickException;
import org.maverickdbms.basic.Program;
import org.maverickdbms.basic.Resolver;
import org.maverickdbms.basic.MaverickString;

class QueryType implements VocType {

    private String type;

    QueryType(String type) {
        this.type = type;
    }

    public int compareTo(Object o) {
        VocType vt = (VocType) o;
        return (vt.getType().startsWith(type)) ? 0 : type.compareTo(vt.getType());
    }

    public String getType() {
        return type;
    }

    public Program resolveProgram(ConstantString record) throws MaverickException {
        throw new MaverickException(0, "Cannot resolve " + record);
    }

    public ConstantString resolveFile(Program program, MaverickString var, ConstantString type, ConstantString key, int flags, MaverickString status, ConstantString record) throws MaverickException {
        throw new MaverickException(0, "Cannot resolve " + key);
    }
}
