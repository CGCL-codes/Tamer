package org.op4j.operators.qualities;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface INavigableMapEntryOperator {

    public INavigatingMapEntryOperator onKey();

    public INavigatingMapEntryOperator onValue();
}
