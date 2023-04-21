package ru.cos.sim.communication.messages;

import ru.cos.sim.communication.dto.MeterDTO;

/**
 * 
 * @author zroslaw
 */
public class MeterDataMessage extends AbstractMessage {

    private MeterDTO meterData;

    public MeterDTO getMeterData() {
        return meterData;
    }

    public void setMeterData(MeterDTO meterData) {
        this.meterData = meterData;
    }

    @Override
    public final MessageType getMessageType() {
        return MessageType.METER_DATA;
    }
}
