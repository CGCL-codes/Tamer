package com.newisys.dv.ifgen.schema;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.newisys.langschema.NameKind;
import com.newisys.langschema.NamedObject;
import com.newisys.langschema.util.NameTable;

/**
 * Ifgen schema object for 'for' statements.
 * 
 * @author Jon Nall
 */
public final class IfgenForStatement extends IfgenTestbenchMember implements IfgenTestbenchMemberContainer {

    private final IfgenVariableDecl varDecl;

    private final IfgenExpression set;

    private final NameTable nameTable = new NameTable();

    private final List<IfgenTestbenchMember> members = new LinkedList<IfgenTestbenchMember>();

    public IfgenForStatement(IfgenSchema schema, IfgenVariableDecl variable, IfgenExpression set) {
        super(schema);
        assert (set.getType() instanceof IfgenSetType || set.getType() == null);
        varDecl = variable;
        nameTable.addObject(variable);
        this.set = set;
    }

    public IfgenVariableDecl getVarDecl() {
        return varDecl;
    }

    public IfgenExpression getSet() {
        return set;
    }

    public void addMember(IfgenTestbenchMember member) {
        members.add(member);
    }

    public List<? extends IfgenTestbenchMember> getMembers() {
        return new LinkedList<IfgenTestbenchMember>(members);
    }

    @Override
    public String toDebugString() {
        StringBuilder buf = new StringBuilder();
        buf.append("for ");
        buf.append(varDecl.getType());
        buf.append(" ");
        buf.append(varDecl.getName());
        buf.append(" (");
        buf.append(set.toDebugString());
        buf.append(") { ... }");
        return buf.toString();
    }

    public Iterator<? extends NamedObject> lookupObjects(String identifier, NameKind kind) {
        return nameTable.lookupObjects(identifier, kind);
    }
}
