package com.kescom.matrix.sel.parser.javacc;

public interface SelParserTreeConstants {

    public int JJTSTART = 0;

    public int JJTVOID = 1;

    public int JJTADD = 2;

    public int JJTADDITIVEOPERATOR = 3;

    public int JJTMULT = 4;

    public int JJTMULTIPLICATIVEOPERATOR = 5;

    public int JJTID = 6;

    public int JJTINTEGER = 7;

    public int JJTDOUBLE = 8;

    public String[] jjtNodeName = { "Start", "void", "Add", "AdditiveOperator", "Mult", "MultiplicativeOperator", "ID", "Integer", "Double" };
}
