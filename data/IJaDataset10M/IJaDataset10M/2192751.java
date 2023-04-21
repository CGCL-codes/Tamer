package org.apache.poi.hssf.record.formula.eval;

/**
 * @author Amol S. Deshmukh &lt; amolweb at ya hoo dot com &gt;
 *  
 */
public interface StringValueEval extends ValueEval {

    /**
     * @return never <code>null</code>, possibly empty string.
     */
    String getStringValue();
}
