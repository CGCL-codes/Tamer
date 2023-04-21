package com.newisys.dv.ifgen.schema;

/**
 * Visitor over bind members.
 * 
 * @author Trevor Robinson
 */
public interface IfgenBindMemberVisitor {

    void visit(IfgenBindSignal obj);

    void visit(IfgenInterfaceDef obj);
}
