package net.tinyos.sf.old;

import java.net.*;
import java.util.*;
import java.io.*;

public class SimSource implements DataSource {

    public static final int PACKET_TIME_SIZE = 8;

    public static final int PACKET_TIME_OFFSET = 0;

    public static final int PACKET_ID_SIZE = 2;

    public static final int PACKET_ID_OFFSET = PACKET_TIME_SIZE + PACKET_TIME_OFFSET;

    public static final int PACKET_PAYLOAD_SIZE = 36;

    public static final int PACKET_PAYLOAD_OFFSET = PACKET_ID_OFFSET + PACKET_ID_SIZE;

    public static final int PACKET_HEADER_SIZE = 10;

    public static final int TOSSIM_RFREADPORT = 10584;

    public static final int TOSSIM_RFWRITEPORT = 10579;

    public static final int TOSSIM_WRITEBACK_SIZE = 128;

    public Socket m_socketSimRead = null;

    public Socket m_socketSimWrite = null;

    public InputStream m_is = null;

    public OutputStream m_os = null;

    public OutputStream m_osWriteBack = null;

    public boolean m_bInitialized = false;

    public boolean m_bShutdown = false;

    public boolean m_bRespawn = true;

    public int m_nPacketsRead = 0;

    private SerialForward sf;

    public SimSource(SerialForward SF) {
        sf = SF;
    }

    public void setSerialForward(SerialForward SF) {
        sf = SF;
    }

    public boolean OpenSource() {
        m_bShutdown = false;
        m_bInitialized = false;
        sf.VERBOSE("Opening TOS Simulator data source");
        try {
            sf.VERBOSE("Listening for TOS Simulator on port " + TOSSIM_RFREADPORT);
            m_socketSimRead = new Socket("localhost", TOSSIM_RFREADPORT);
            m_is = m_socketSimRead.getInputStream();
            m_osWriteBack = m_socketSimRead.getOutputStream();
            sf.VERBOSE("Read Connection opened to TOS Simulator");
            m_bInitialized = true;
            sf.VERBOSE("Opening write conntection Nido on port " + TOSSIM_RFWRITEPORT);
            m_socketSimWrite = new Socket("127.0.0.1", TOSSIM_RFWRITEPORT);
            m_os = m_socketSimWrite.getOutputStream();
        } catch (IOException e) {
            if (!m_bShutdown) sf.VERBOSE("Cannot connect to TOS Simulator on port");
            return false;
        }
        sf.VERBOSE("Established connection to Nido");
        return true;
    }

    public byte[] ReadPacket() {
        byte[] packet = ReadPacketHelper();
        if (m_bRespawn && !m_bShutdown && packet == null) {
            boolean bStatus = CloseSource();
            bStatus = OpenSource();
            packet = ReadPacket();
        }
        return packet;
    }

    private byte[] ReadPacketHelper() {
        int serialByte;
        int nPacketSize = sf.PACKET_SIZE + PACKET_TIME_SIZE + PACKET_ID_SIZE;
        int count = 0;
        byte[] packet = new byte[sf.PACKET_SIZE];
        if (m_is == null) {
            sf.VERBOSE("SIMSOURCE: call OpenSource() first");
            return null;
        }
        try {
            if (m_nPacketsRead % TOSSIM_WRITEBACK_SIZE == 0) {
                m_osWriteBack.write(new byte[TOSSIM_WRITEBACK_SIZE]);
            }
            while (!m_bShutdown && (serialByte = m_is.read()) != -1) {
                sf.VERBOSE("SimSource: Read: " + serialByte);
                if (count >= PACKET_HEADER_SIZE) {
                    sf.VERBOSE("Read " + count + " header bytes, now reading in packet of " + sf.PACKET_SIZE + " bytes.");
                    packet[count - PACKET_HEADER_SIZE] = (byte) serialByte;
                }
                count++;
                sf.nBytesRead++;
                if (count == nPacketSize) {
                    m_nPacketsRead++;
                    return packet;
                }
            }
        } catch (IOException e) {
            m_bShutdown = true;
        }
        return null;
    }

    public boolean CloseSource() {
        sf.VERBOSE("Closing TOS Simulator data source");
        m_bInitialized = false;
        m_bShutdown = true;
        if (m_os != null) {
            try {
                m_os.close();
            } catch (IOException e) {
            }
        }
        if (m_is != null) {
            try {
                m_is.close();
            } catch (IOException e) {
            }
        }
        if (m_socketSimRead != null) {
            try {
                m_socketSimRead.close();
            } catch (IOException e) {
            }
        }
        if (m_socketSimWrite != null) {
            try {
                m_socketSimWrite.close();
            } catch (IOException e) {
            }
        }
        m_is = null;
        m_os = null;
        m_socketSimWrite = null;
        m_socketSimRead = null;
        return true;
    }

    public boolean WritePacket(byte[] packet) {
        try {
            if (m_is == null) {
                return false;
            }
            if (m_os != null) {
                byte[] tossimpacket = new byte[sf.PACKET_SIZE + PACKET_TIME_SIZE + PACKET_ID_SIZE];
                tossimpacket[PACKET_ID_OFFSET] = 0x00;
                tossimpacket[PACKET_ID_OFFSET + 1] = 0x00;
                for (int i = 0; i < sf.PACKET_SIZE; i++) {
                    tossimpacket[PACKET_PAYLOAD_OFFSET + i] = packet[i];
                }
                DataOutputStream d_os = new DataOutputStream(m_os);
                m_os.write(tossimpacket);
                m_os.flush();
                return true;
            }
            m_socketSimWrite.close();
        } catch (IOException e) {
            sf.VERBOSE("SIMSOURCE: Unable to write data to mote");
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
