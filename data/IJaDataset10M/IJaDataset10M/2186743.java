package org.mobicents.protocols.ss7.isup.impl.stack.timers;

import java.util.Properties;
import org.mobicents.protocols.ss7.isup.ISUPTimeoutEvent;
import org.mobicents.protocols.ss7.isup.message.ConnectMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.SubsequentAddressMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.mobicents.protocols.ss7.isup.message.parameter.SubsequentNumber;

/**
 * @author baranowb
 * 
 */
public class SAM_CONTest extends SingleTimers {

    protected long getT() {
        return ISUPTimeoutEvent.T7_DEFAULT;
    }

    protected int getT_ID() {
        return ISUPTimeoutEvent.T7;
    }

    protected ISUPMessage getRequest() {
        SubsequentAddressMessage msg = super.provider.getMessageFactory().createSAM(1);
        SubsequentNumber sn = super.provider.getParameterFactory().createSubsequentNumber();
        sn.setAddress("11");
        msg.setSubsequentNumber(sn);
        return msg;
    }

    protected ISUPMessage getAnswer() {
        ConnectMessage ans = super.provider.getMessageFactory().createCON();
        CircuitIdentificationCode cic = super.provider.getParameterFactory().createCircuitIdentificationCode();
        cic.setCIC(1);
        ans.setCircuitIdentificationCode(cic);
        return ans;
    }

    protected Properties getSpecificConfig() {
        Properties p = new Properties();
        p.put("t7", getT() + "");
        p.put("ni", "2");
        p.put("localspc", "2");
        return p;
    }
}
