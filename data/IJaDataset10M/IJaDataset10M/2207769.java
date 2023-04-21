package org.jsmpp.session;

import java.io.IOException;
import java.io.OutputStream;
import org.jsmpp.PDUSender;
import org.jsmpp.PDUStringException;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;

/**
 * @author uudashr
 *
 */
public class DataSmCommandTask extends AbstractSendCommandTask {

    private final String serviceType;

    private final TypeOfNumber sourceAddrTon;

    private final NumberingPlanIndicator sourceAddrNpi;

    private final String sourceAddr;

    private final TypeOfNumber destAddrTon;

    private final NumberingPlanIndicator destAddrNpi;

    private final String destinationAddr;

    private final ESMClass esmClass;

    private final RegisteredDelivery registeredDelivery;

    private final DataCoding dataCoding;

    private final OptionalParameter[] optionalParameters;

    public DataSmCommandTask(PDUSender pduSender, String serviceType, TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr, TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi, String destinationAddr, ESMClass esmClass, RegisteredDelivery registeredDelivery, DataCoding dataCoding, OptionalParameter[] optionalParameters) {
        super(pduSender);
        this.serviceType = serviceType;
        this.sourceAddrTon = sourceAddrTon;
        this.sourceAddrNpi = sourceAddrNpi;
        this.sourceAddr = sourceAddr;
        this.destAddrTon = destAddrTon;
        this.destAddrNpi = destAddrNpi;
        this.destinationAddr = destinationAddr;
        this.esmClass = esmClass;
        this.registeredDelivery = registeredDelivery;
        this.dataCoding = dataCoding;
        this.optionalParameters = optionalParameters;
    }

    public void executeTask(OutputStream out, int sequenceNumber) throws PDUStringException, IOException {
        pduSender.sendDataSm(out, sequenceNumber, serviceType, sourceAddrTon, sourceAddrNpi, sourceAddr, destAddrTon, destAddrNpi, destinationAddr, esmClass, registeredDelivery, dataCoding, optionalParameters);
    }

    public String getCommandName() {
        return "data_sm";
    }
}
