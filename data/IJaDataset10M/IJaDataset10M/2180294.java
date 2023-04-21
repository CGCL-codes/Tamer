package org.apache.axis2.dispatchers;

import junit.framework.TestCase;
import org.apache.axis2.addressing.AddressingConstants;
import org.apache.axis2.context.MessageContext;

public class AddressingBasedDispatcherTest extends TestCase {

    public void testValidateActionFlag() throws Exception {
        MessageContext mc = new MessageContext();
        mc.setProperty(AddressingConstants.IS_ADDR_INFO_ALREADY_PROCESSED, Boolean.TRUE);
        mc.setProperty(AddressingConstants.ADDR_VALIDATE_ACTION, Boolean.FALSE);
        AddressingBasedDispatcher dispatcher = new AddressingBasedDispatcher();
        dispatcher.invoke(mc);
    }
}
