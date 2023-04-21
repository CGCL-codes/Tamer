package it.jplag.jplagClient;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.soap.message.SOAPFaultInfo;
import java.lang.IllegalArgumentException;

public final class JPlagTyp_continueResultDownload_Fault_SOAPBuilder implements com.sun.xml.rpc.encoding.SOAPInstanceBuilder {

    private SOAPFaultInfo instance = null;

    private java.lang.Object detail;

    private int index = -1;

    private static final int JPLAGTUTORIAL_JPLAGCLIENT_JPLAGEXCEPTION_INDEX = 0;

    public int memberGateType(int memberIndex) {
        return GATES_INITIALIZATION + REQUIRES_COMPLETION;
    }

    public void construct() {
    }

    public void setMember(int index, java.lang.Object memberValue) {
        this.index = index;
        detail = memberValue;
    }

    public void initialize() {
        switch(index) {
            case JPLAGTUTORIAL_JPLAGCLIENT_JPLAGEXCEPTION_INDEX:
                instance.setDetail(detail);
                break;
        }
    }

    public void setInstance(java.lang.Object instance) {
        this.instance = (SOAPFaultInfo) instance;
    }

    public java.lang.Object getInstance() {
        return instance;
    }
}
