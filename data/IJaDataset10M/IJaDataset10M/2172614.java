package org.lnicholls.galleon.goback;

import java.net.InetAddress;
import java.rmi.dgc.VMID;
import org.apache.log4j.Logger;
import org.lnicholls.galleon.server.*;
import org.lnicholls.galleon.goback.*;
import org.lnicholls.galleon.util.*;

/**
 * Extracted from: TiVo Connect Automatic Machine Discovery Protocol Specification. Revision: 1.5.1, Last updated:
 * 3/5/2003 Copyright � 2003, TiVo Inc. All rights reserved
 *
 * The TiVo Connect Automatic Machine Discovery Protocol allows two or more machines running TiVo Connect software (or
 * simply TCM for "TiVo Connect Machine") to discover each other by periodically exchanging UDP and/or TCP packets of
 * TiVo-defined identifying information. Because of their periodic nature, such packets are typically referred to as
 * "beacons". Within this protocol, beacons transmitted via UDP are always "broadcast-based", meaning that the packets
 * are sent blindly onto the local network, with absolutely no special acknowledgment or other handshaking required.
 * Interested TCMs need simply "listen" passively in order to detect other TCMs. (Imagine a lighthouse lamp rotating
 * over a foggy bay, announcing its presence to any ships close enough to see it.) On the other hand, beacons
 * transmitted via TCP are "connection-based", requiring explicit twoway handshaking. This approach is required to
 * overcome limitations of certain network configurations in which the one-way broadcasting of UDP packets isn't
 * effective due to network topology or policy, preventing certain TCMs from being able to "hear" each other. (Imagine
 * shining an infinitely powerful flashlight directly at someone lost in a dark cavern.) In addition to machine
 * identification, the protocol also has a simple mechanism for announcing the availability of services provided by each
 * TCM. From the perspective of a single TCM, once identity and service availability have been established, the
 * discovery phase is largely complete. From then on, other protocols (such as HTTP) can kick in, normally as part of
 * accessing and/or providing various services. However, see section 3.5 for details about how updates to the discovered
 * information occur.
 *
 * There are three main components to the workings of the protocol. � Beacon Packet Data Format � Broadcast-based
 * Discovery � Connection-based Discovery The packet data format is common to all other aspects of the protocol, while
 * the broadcast-based and connection-based discovery mechanisms, although similar, each have their own associated
 * details. Every TCM must be prepared to participate in broadcast- and connection-based discovery simultaneously.
 * Regardless of the method used to transmit beacon packets, each participating TCM maintains an internal list of all
 * other TCMs from which it has heard. Records in this list are updated whenever new information arrives. Records for
 * TCMs that have not been heard from recently (or whose departures have been explicitly detected) are eventually cycled
 * off this list. In this way, whenever further communication is needed, the set of networked machines able to "TiVo
 * Connect", as well as their available services, can be known at any given moment with no need to query the network.
 */
public class Beacon implements Constants {

    private static Logger log = Logger.getLogger(Beacon.class.getName());

    public static String guid = new VMID().toString();

    static {
        try {
            guid = NetworkInfo.getMacAddress();
        } catch (Exception ex) {
        }
    }

    public Beacon(int port, int hmoPort) {
        this(false, port, hmoPort, false);
    }

    public Beacon(boolean connected, int port, int hmoPort) {
        this(false, port, hmoPort, false);
    }

    public Beacon(boolean connected, int port, int hmoPort, boolean clear) {
        mPort = port;
        mHMOPort = hmoPort;
        mTivoConnect = "1";
        if (connected) mMethod = METHOD_UNICAST; else mMethod = METHOD_BROADCAST;
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            mMachine = inetAddress.getHostName();
        } catch (Exception ex) {
        }
        mIdentity = guid;
        mPlatform = PLATFORM_PC + '/' + System.getProperty("os.name");
        if (clear) mServices = ""; else mServices = "TiVoMediaServer:" + mHMOPort + "/http";
        mSWVersion = Tools.getVersion();
    }

    /**
     * Parse a received beacon
     */
    public Beacon(byte[] buf) {
        String beacon = new String(buf);
        String nameValue = "";
        try {
            for (int i = 0; i < beacon.length(); i++) {
                if (beacon.charAt(i) == '\n') {
                    if (nameValue.indexOf("=") > 0) {
                        String name = nameValue.substring(0, nameValue.indexOf("=")).toLowerCase();
                        String value = nameValue.substring(nameValue.indexOf("=") + 1);
                        if (name.equals(NAME_TIVOCONNECT.toLowerCase())) mTivoConnect = value; else if (name.equals(NAME_METHOD.toLowerCase())) mMethod = value; else if (name.equals(NAME_PLATFORM.toLowerCase())) mPlatform = value; else if (name.equals(NAME_MACHINE.toLowerCase())) mMachine = value; else if (name.equals(NAME_IDENTIFY.toLowerCase())) mIdentity = value; else if (name.equals(NAME_SERVICES.toLowerCase())) mServices = value; else if (name.equals(NAME_SWVERSION.toLowerCase())) mSWVersion = value;
                    }
                    nameValue = "";
                } else {
                    nameValue = nameValue + beacon.charAt(i);
                }
            }
        } catch (Exception ex) {
            Tools.logException(Beacon.class, ex);
        }
    }

    public boolean isValid() {
        return mTivoConnect != null && mMethod != null && mPlatform != null && mMachine != null && mIdentity != null && mSWVersion != null;
    }

    /**
     * tivoconnect= <number>method= <method>platform= <type>[/ <sub-type>] machine= <string>identity= <string>
     * services= <name>[: <port>][/ <protocol>], ... swversion= <string>
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        synchronized (buffer) {
            buffer.append(NAME_TIVOCONNECT + "=" + mTivoConnect + '\n');
            if (mMethod != null) buffer.append(NAME_METHOD + "=" + mMethod + '\n');
            if (mPlatform != null) buffer.append(NAME_PLATFORM + "=" + mPlatform + '\n');
            buffer.append(NAME_MACHINE + "=" + mMachine + '\n');
            buffer.append(NAME_IDENTIFY + "=" + mIdentity + '\n');
            if (mServices != null) buffer.append(NAME_SERVICES + "=" + mServices + '\n');
            if (mSWVersion != null) buffer.append(NAME_SWVERSION + "=" + mSWVersion + '\n');
        }
        return buffer.toString();
    }

    /**
     * This value indicates the particular flavor of TiVo Connect Automatic Machine Discovery Protocol supported by the
     * originating TCM. For now, <number>should always be "1". As the one exception to the previously stated ordering
     * assumptions, this value must always appear at the very beginning of every packet so the sequence "tivoconnect"
     * (in the first 11 character positions) can serve as an identifying "signature". Note that "TIVOCONNECT" and
     * "tiVoCOnNecT" are both examples of valid signatures. This value is not optional.
     */
    public String getTivoConnect() {
        return mTivoConnect;
    }

    /**
     * This value indicates the way in which the packet was transmitted, where <method>should be one of the following
     * values: � broadcast (for packets sent using UDP) � connected (for packets sent using TCP) This value is not
     * optional.
     */
    public String getMethod() {
        return mMethod;
    }

    /**
     * This value contains human readable text, naming the TCM, suitable for display to the user. Windows computer
     * beacons should set <string>to the Windows computer name. TiVo DVR beacons should set <string>to the name of the
     * DVR (e.g., "The Upstairs Master Bedroom Closet TiVo"). Packets originating from other platforms should contain a
     * similar suitable string. This value is optional (or can be left blank) if the machine does not have a name.
     * However, a name is highly recommended since it can be used by software to enhance the user�s experience.
     */
    public String getMachine() {
        return mMachine;
    }

    /**
     * This value should be unique to the originating TCM (perhaps even globally unique, but certainly unique across the
     * local network). This information is intended to allow TCMs to unambiguously identify each other even when their
     * names or IP addresses have changed. TiVo DVR beacons should set <string>to the DVR's serial number. Windows
     * computer packets should set <string>to a GUID (generated once and stored in the registry), formatted using the
     * StringFromGUID2() function of the Windows API. This value is not optional.
     */
    public String getIdentity() {
        return mIdentity;
    }

    /**
     * This value indicates what kind of TCM sent the packet, where <type>should be one of the following values: � tcd
     * (for TiVo DVR beacons) � pc (for Windows computer beacons) The use of unique values for <type>creates platform
     * "namespaces", within which various values for the optional <sub-type>portion can be freely determined by the
     * associated software development groups without risk of conflict. Future development efforts should use their own
     * value for <type>(e.g., "c64" for Commodore-64 software applications). This value is not optional.
     */
    public String getPlatform() {
        return mPlatform;
    }

    /**
     * This value provides a comma-delimited list of entries indicating the availability of services and optionally the
     * port numbers and protocols through which they communicate. This value is optional (or can be left blank) as not
     * every TCM will necessarily provide any services.
     */
    public String getServices() {
        return mServices;
    }

    /**
     * This value describes the "primary" software running on the TCM. There is no required format for <string>. This
     * value is optional.
     */
    public String getSWVersion() {
        return mSWVersion;
    }

    private int mPort;

    private int mHMOPort;

    private String mTivoConnect;

    private String mMethod;

    private String mMachine;

    private String mIdentity;

    private String mPlatform;

    private String mServices;

    private String mSWVersion;
}
