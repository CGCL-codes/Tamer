package com.bizsensors.gourangi.pmode;

import org.apache.log4j.Logger;
import com.bizsensors.gourangi.jpcap.TCPSessionProcessor;
import com.bizsensors.gourangi.jpcap.TCPSessionReconstructor;
import jpcap.JpcapCaptor;

public class PModeCapture implements Runnable {

    TCPSessionProcessor tcpSP = null;

    JpcapCaptor jpcap = null;

    Logger log = Logger.getLogger(this.getClass());

    public PModeCapture(TCPSessionProcessor tcpSP, JpcapCaptor jpcap) {
        this.tcpSP = tcpSP;
        this.jpcap = jpcap;
    }

    public void run() {
        log.debug("Packet Capturing in progress....");
        jpcap.loopPacket(-1, new TCPSessionReconstructor(tcpSP));
    }
}
