package edu.mit.lcs.haystack.adenine.instructions;

import edu.mit.lcs.haystack.adenine.interpreter.ContinuationException;
import edu.mit.lcs.haystack.adenine.interpreter.Message;

/**
 * @version 	1.0
 * @author		Dennis Quan
 */
public class ReturnException extends ContinuationException {

    public Message m_retVal;

    public ReturnException(Message retVal) {
        m_retVal = retVal;
    }

    public Object getReturnValue() {
        return m_retVal.getPrimaryValue();
    }
}
