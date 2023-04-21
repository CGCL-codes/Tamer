package org.gpc.evm.gen;

import java.io.IOException;
import org.gpc.evm.DataType;
import org.gpc.gen.CodeBytes;

/** Abstraction to build a program
 * 
 * @author EaseWay
 *
 */
public interface IProgramBuilder {

    IClassBuilder createClass(int accessFlags, String qname) throws IOException;

    int addClassReference(String qname) throws IOException;

    int addMethodReference(int classRefIndex, String name, DataType returnType, DataType[] argTypes) throws IOException;

    int addFieldReference(int classRefIndex, String name, DataType type) throws IOException;

    int addString(String content) throws IOException;

    int addConstant(CodeBytes bytes) throws IOException;
}
