package joeq.Compiler.Dataflow;

/**
 * TransferFunction
 * 
 * @author John Whaley
 * @version $Id: TransferFunction.java 1456 2004-03-09 22:01:46Z jwhaley $
 */
public interface TransferFunction {

    Fact apply(Fact f);
}
