package org.mobicents.protocols.ss7.isup.impl.stack.timers;

import java.util.Properties;
import org.mobicents.protocols.ss7.isup.ISUPTimeoutEvent;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupBlockingAckMessage;
import org.mobicents.protocols.ss7.isup.message.CircuitGroupBlockingMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitGroupSuperVisionMessageType;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.mobicents.protocols.ss7.isup.message.parameter.RangeAndStatus;

/**
 * @author baranowb
 *
 */
public class CGBTest extends DoubleTimers {

    protected long getSmallerT() {
        return ISUPTimeoutEvent.T18_DEFAULT + 3000;
    }

    protected long getBiggerT() {
        return ISUPTimeoutEvent.T19_DEFAULT;
    }

    protected int getSmallerT_ID() {
        return ISUPTimeoutEvent.T18;
    }

    protected int getBiggerT_ID() {
        return ISUPTimeoutEvent.T19;
    }

    protected ISUPMessage getRequest() {
        CircuitGroupBlockingMessage cgb = super.provider.getMessageFactory().createCGB(1);
        RangeAndStatus ras = super.provider.getParameterFactory().createRangeAndStatus();
        ras.setRange((byte) 7, true);
        ras.setAffected((byte) 1, true);
        ras.setAffected((byte) 0, true);
        cgb.setRangeAndStatus(ras);
        CircuitGroupSuperVisionMessageType cgsvmt = super.provider.getParameterFactory().createCircuitGroupSuperVisionMessageType();
        cgsvmt.setCircuitGroupSuperVisionMessageTypeIndicator(cgsvmt._CIRCUIT_GROUP_SMTIHFO);
        cgb.setSupervisionType(cgsvmt);
        return cgb;
    }

    protected ISUPMessage getAnswer() {
        CircuitGroupBlockingAckMessage cgba = super.provider.getMessageFactory().createCGBA();
        CircuitIdentificationCode cic = super.provider.getParameterFactory().createCircuitIdentificationCode();
        cic.setCIC(1);
        cgba.setCircuitIdentificationCode(cic);
        RangeAndStatus ras = super.provider.getParameterFactory().createRangeAndStatus();
        ras.setRange((byte) 7, true);
        ras.setAffected((byte) 1, true);
        ras.setAffected((byte) 0, true);
        cgba.setRangeAndStatus(ras);
        CircuitGroupSuperVisionMessageType cgsvmt = super.provider.getParameterFactory().createCircuitGroupSuperVisionMessageType();
        cgsvmt.setCircuitGroupSuperVisionMessageTypeIndicator(cgsvmt._CIRCUIT_GROUP_SMTIHFO);
        cgba.setSupervisionType(cgsvmt);
        return cgba;
    }

    protected Properties getSpecificConfig() {
        Properties p = new Properties();
        p.put("t18", getSmallerT() + "");
        p.put("t19", getBiggerT() + "");
        p.put("ni", "2");
        p.put("localspc", "2");
        return p;
    }
}
