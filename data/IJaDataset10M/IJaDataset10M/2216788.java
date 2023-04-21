package iwork.apps.pr15.testsink;

import iwork.apps.pr15.fw.mmp.*;
import iwork.apps.pr15.fw.mme.*;
import iwork.eheap2.*;

public class TestSinkUpdateManager {

    private EventHeap m_eh;

    private String m_sinkName;

    private int m_xRes = 1024;

    private int m_yRes = 768;

    private double m_physWidth = 32.0;

    private double m_physHeight = 24.0;

    private UpdateEventSender m_sender;

    public void setEventHeap(EventHeap eh) {
        m_eh = eh;
        m_sender = new UpdateEventSender(m_eh);
    }

    public void setSinkName(String sinkName) {
        m_sinkName = sinkName;
    }

    public void sendAllStatus() {
        if (m_sender == null) return;
        try {
            m_sender.sendScreenConfig(m_sinkName, m_physWidth, m_physHeight);
            m_sender.sendScreenStatus(m_sinkName, true);
            m_sender.sendMachineConfig(m_sinkName, m_xRes, m_yRes);
            m_sender.sendMachineStatus(m_sinkName, true);
            m_sender.sendScreenInput(m_sinkName, m_sinkName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendOffStatus() {
        if (m_sender == null) return;
        try {
            m_sender.sendMachineStatus(m_sinkName, false);
            m_sender.sendScreenStatus(m_sinkName, false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
