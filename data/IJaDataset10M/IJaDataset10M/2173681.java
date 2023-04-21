package com.vladium.jcd.opcodes;

/**
 * @author Vlad Roubtsov, (C) 2003
 */
public interface IOpcodeVisitor {

    void visit(int opcode, boolean wide, int offset, Object ctx);
}
