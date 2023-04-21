package it.tcon.xbeedriver;

import it.tcon.xbeedriver.XBeeNodeService;
import it.tcon.xbeedriver.XBeeNodeParam.State;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.zigbee.ZNetNodeIdentificationResponse.DeviceType;
import com.rapplogic.xbee.util.ByteUtils;

public class XBeeNode {

    private Logger logger = Logger.getLogger(XBeeNode.class);

    private XBeeAddress64 address64;

    private String address64Str;

    private DeviceType type;

    private Date lastTimeSaw;

    private ConcurrentHashMap services = new ConcurrentHashMap<String, XBeeNodeService>();

    private boolean isActive = false;

    private XBeeNodeParam ch = new XBeeNodeParam("CH");

    private XBeeNodeParam op = new XBeeNodeParam("OP");

    private XBeeNodeParam id = new XBeeNodeParam("ID");

    private XBeeNodeParam sc = new XBeeNodeParam("SC");

    private XBeeNodeParam sd = new XBeeNodeParam("SD");

    private XBeeNodeParam nj = new XBeeNodeParam("NJ");

    private XBeeNodeParam jv = new XBeeNodeParam("JV");

    private XBeeNodeParam my = new XBeeNodeParam("MY");

    private XBeeNodeParam sh = new XBeeNodeParam("SH");

    private XBeeNodeParam sl = new XBeeNodeParam("SL");

    private XBeeNodeParam dh = new XBeeNodeParam("DH");

    private XBeeNodeParam dl = new XBeeNodeParam("DL");

    private XBeeNodeParam ni = new XBeeNodeParam("NI");

    private XBeeNodeParam bh = new XBeeNodeParam("BH");

    private XBeeNodeParam ar = new XBeeNodeParam("AR");

    private XBeeNodeParam dd = new XBeeNodeParam("DD");

    private XBeeNodeParam nt = new XBeeNodeParam("NT");

    private XBeeNodeParam no = new XBeeNodeParam("NO");

    private XBeeNodeParam pl = new XBeeNodeParam("PL");

    private XBeeNodeParam pm = new XBeeNodeParam("PM");

    private XBeeNodeParam ee = new XBeeNodeParam("EE");

    private XBeeNodeParam eo = new XBeeNodeParam("EO");

    private XBeeNodeParam ky = new XBeeNodeParam("KY");

    private XBeeNodeParam bd = new XBeeNodeParam("BD");

    private XBeeNodeParam nb = new XBeeNodeParam("NB");

    private XBeeNodeParam d7 = new XBeeNodeParam("D7");

    private XBeeNodeParam d6 = new XBeeNodeParam("D6");

    private XBeeNodeParam ap = new XBeeNodeParam("AP");

    private XBeeNodeParam ao = new XBeeNodeParam("AO");

    private XBeeNodeParam sm = new XBeeNodeParam("SM");

    private XBeeNodeParam st = new XBeeNodeParam("ST");

    private XBeeNodeParam sp = new XBeeNodeParam("SP");

    private XBeeNodeParam sn = new XBeeNodeParam("SN");

    private XBeeNodeParam so = new XBeeNodeParam("SO");

    private XBeeNodeParam analog0 = new XBeeNodeParam("Analog0");

    private XBeeNodeParam analog1 = new XBeeNodeParam("Analog1");

    private XBeeNodeParam analog2 = new XBeeNodeParam("Analog2");

    private XBeeNodeParam analog3 = new XBeeNodeParam("Analog3");

    private boolean dIO0;

    private boolean dIO1;

    private boolean dIO2;

    private boolean dIO3;

    private boolean dIO4;

    private boolean dIO5;

    private boolean dIO6;

    private boolean dIO7;

    private boolean dIO10;

    private boolean dIO11;

    private boolean dIO12;

    private XBeeNodeParam digital0 = new XBeeNodeParam("digital0");

    private XBeeNodeParam digital1 = new XBeeNodeParam("digital1");

    private XBeeNodeParam digital2 = new XBeeNodeParam("digital2");

    private XBeeNodeParam digital3 = new XBeeNodeParam("digital3");

    private XBeeNodeParam digital4 = new XBeeNodeParam("digital4");

    private XBeeNodeParam digital5 = new XBeeNodeParam("digital5");

    private XBeeNodeParam digital6 = new XBeeNodeParam("digital6");

    private XBeeNodeParam digital7 = new XBeeNodeParam("digital7");

    private XBeeNodeParam digital8 = new XBeeNodeParam("digital8");

    private XBeeNodeParam digital9 = new XBeeNodeParam("digital9");

    private XBeeNodeParam digital10 = new XBeeNodeParam("digital10");

    private XBeeNodeParam digital11 = new XBeeNodeParam("digital11");

    private XBeeNodeParam digital12 = new XBeeNodeParam("digital12");

    private XBeeNodeParam vr = new XBeeNodeParam("VR");

    private XBeeNodeParam hv = new XBeeNodeParam("HV");

    private XBeeNodeParam ai = new XBeeNodeParam("AI");

    private XBeeNodeParam sv = new XBeeNodeParam("SV");

    /**
	 * Constructor for a new XBeeNode, with the address provided
	 */
    public XBeeNode(XBeeAddress64 address64) {
        this(address64, DeviceType.END_DEVICE);
    }

    /**
	 * Constructor for a new XBeeNode, with the address and type provided
	 */
    public XBeeNode(XBeeAddress64 address64, DeviceType type) {
        this.address64 = address64;
        this.address64Str = ByteUtils.toBase16(address64.getAddress());
        this.lastTimeSaw = new Date();
        this.type = type;
        this.services = new ConcurrentHashMap<String, XBeeNodeService>();
    }

    /**
	 * Get the XBeeAddress64 of this XBeeNode
	 * @return <tt>XBeeAddress64</tt> The XBee 64bit addresses of this node
	 */
    public synchronized XBeeAddress64 getAddress64() {
        if (this.address64 != null) return this.address64; else return null;
    }

    /**
	 * Set the XBeeAddress64 of this XBeeNode
	 * @param address The 64Bit address of this node
	 */
    public synchronized void setAddress64(XBeeAddress64 address) {
        this.address64 = address;
    }

    /**
	 * Set the 64Bit address of this node in string form
	 * @param address64Str The address to set
	 */
    public synchronized void setAddress64String(String address64Str) {
        this.address64Str = address64Str;
    }

    /**
	 * Get the 64Bit address in string form
	 * @return <tt>String</tt> of the 64Bit address
	 */
    public synchronized String getAddress64String() {
        return this.address64Str;
    }

    /**
	 * Get the 64bit address of this XBeeNode in int[] form
	 * @return <tt>int[]</tt> containing the 64bit address of this node
	 */
    public synchronized int[] getAddress64BitInt() {
        return address64.getAddress();
    }

    /**
	 * Get the operating channel of this node
	 * @return <tt>XBeeNodeParam</tt> The operating channel parameter
	 */
    public synchronized XBeeNodeParam getOperatingChannel() {
        return this.ch;
    }

    /**
	 * Set the operating channel of this node
	 * @param ch the channel number used for transmitting and receiving between RF modules 
	 */
    public synchronized void setOperatingChannel(int[] ch) {
        this.ch.setData(ch);
        this.ch.setState(State.PARAM_SET);
    }

    /**
	 * Get the operating PAN ID.
	 * The OP value reflects the operating PAN ID that the module is running on.
	 * @return <tt>XBeeNodeParam</tt> of the operating PAN ID
	 */
    public synchronized XBeeNodeParam getOperatingPANID() {
        return this.op;
    }

    /**
	 * Set the operating PAN ID parameter of this node.
	 * @param panid the array containing the PAN ID received from an ID message
	 */
    public synchronized void setOperatingPANID(int[] panid) {
        this.op.setData(panid);
    }

    /**
	 * Get the referred PAN ID of this device
	 * @return XBeeNodeParam of this PAN ID
	 */
    public synchronized XBeeNodeParam getPANID() {
        return this.id;
    }

    /**
	 * Set the preferred PAN ID. Set to ID = 0xFFFF to auto-select
	 * <br />Coordinator: Set the preferred PAN ID.
	 * <br />When the device searches for a Coordinator, it attempts to join to parent that has a matching PAN ID.
	 * Set ID = 0xFFFF to join a parent operating on any PAN ID
	 * @param panid
	 */
    public synchronized void setPANID(int[] panid) {
        this.id.setData(panid);
        this.id.setState(State.PARAM_SET);
    }

    /**
	 * Get the list of channels to scan
	 * @return XBeeNodeParam of the list of channel to scan
	 */
    public synchronized XBeeNodeParam getScanChannels() {
        return this.sc;
    }

    /**
	 * Set the list of channel to scan
	 * @param scanChannels the list of channels
	 */
    public synchronized void setScanChannels(int[] scanChannels) {
        this.sc.setData(scanChannels);
        this.sc.setState(State.PARAM_SET);
    }

    /**
	 * Get the scan duration exponent
	 * @return XBeeNodeParam The scan duration exponent
	 */
    public synchronized XBeeNodeParam getScanDuration() {
        return this.sd;
    }

    /**
	 * Set the scan duration exponent of this node
	 * @param scanDuration the scan duration received by this node
	 */
    public synchronized void setScanDuration(int[] scanDuration) {
        this.sd.setData(scanDuration);
        this.sd.setState(State.PARAM_SET);
    }

    /**
	 * Get the time a coordinator/Router allows nodes to join
	 * @return XBeeNodeParam containing the join duration parameter
	 */
    public synchronized XBeeNodeParam getNodeJoinDuration() {
        return this.nj;
    }

    /**
	 * Set the time this node allows nodes to join
	 * @param joinDuration The join duration
	 */
    public synchronized void setNodeJoinDuration(int[] joinDuration) {
        this.nj.setData(joinDuration);
        this.nj.setState(State.PARAM_SET);
    }

    /**
	 * Get the channel verification parameter
	 * @return the cnahhel verification parameter
	 */
    public synchronized XBeeNodeParam getChannelVerification() {
        return this.jv;
    }

    /**
	 * Set the channel verification parameter for this node
	 * @param channelVerification the channel verification parameter of this node
	 */
    public synchronized void setChannelVerification(int[] channelVerification) {
        this.jv.setData(channelVerification);
        this.jv.setState(State.PARAM_SET);
    }

    public synchronized XBeeNodeParam getMy16BitAddress() {
        return this.my;
    }

    public synchronized void setMy16BitAddress(int[] address) {
        this.my.setData(address);
        this.my.setState(State.PARAM_SET);
    }

    /**
	 *  Get the high 32 bits of the RF module's unique IEEE 64-bit address. 64-bit source address is always enabled. 
	 * @return <tt>XBeeNodeParam</tt> The high 32 bits unique 64-bit address of the module.
	 */
    public synchronized XBeeNodeParam getSerialNumberHigh() {
        return this.sh;
    }

    /**
	 *  Set the high 32 bits of the RF module's unique IEEE 64-bit address. 64-bit source address is always enabled. 
	 * @param serialHigh The SH value of an ATSH command
	 */
    public synchronized void setSerialNumberHigh(int[] serialHigh) {
        this.sh.setData(serialHigh);
        this.sh.setState(State.PARAM_SET);
    }

    /**
	 *  Get the low 32 bits of the RF module's unique IEEE 64-bit address. 64-bit source address is always enabled. 
	 * @return <tt>XBeeNodeParam</tt> The low 32 bits unique 64-bit address of the module.
	 */
    public synchronized XBeeNodeParam getSerialNumberLow() {
        return this.sl;
    }

    /**
	 *  Set the low 32 bits of the RF module's unique IEEE 64-bit address. 64-bit source address is always enabled. 
	 * @param serialLow The SL value of an ATSL command
	 */
    public synchronized void setSerialNumberLow(int[] serialLow) {
        this.sl.setData(serialLow);
        this.sl.setState(State.PARAM_SET);
    }

    /**
	 * Get the upper 32bit of the 64bit destination address.
	 * When combined with DL, it defines the destination address used for transmission
	 * @return XBeeNodeParam the upper 32 bit of the destination address
	 */
    public synchronized XBeeNodeParam getDestinationAddrHigh() {
        return this.dh;
    }

    /**
	 * Set the upper 32bit of the 64bit destination address.
	 * When combined with DL, it defines the destination address used for transmission
	 * @param destAddrHigh the upper 32 bit of the destination address
	 */
    public synchronized void setDestinationAddrHigh(int[] destAddrHigh) {
        this.dh.setData(destAddrHigh);
        this.dh.setState(State.PARAM_SET);
    }

    /**
	 * Get the lower 32bit of the 64bit destination address.
	 * When combined with DL, it defines the destination address used for transmission
	 * @return XBeeNodeParam of the lower 32bit of the destination address
	 */
    public synchronized XBeeNodeParam getDestinationAddrLow() {
        return this.dl;
    }

    /**
	 * Set the lower 32bit of the 64bit destination address.
	 * When combined with DL, it defines the destination address used for transmission
	 * @param destAddrLow the lower 32 bit of the destination address
	 */
    public synchronized void setDestinationAddrLow(int[] destAddrLow) {
        this.dl.setData(destAddrLow);
        this.dl.setState(State.PARAM_SET);
    }

    /**
	 * Get the node identifier string
	 * @return XBeeNodeParam that contains the string of the Node Identifier parameter
	 */
    public synchronized XBeeNodeParam getNodeIdentifier() {
        return this.ni;
    }

    /**
	 * Set the node identifier string
	 * @param nodeIdentifier the parameter containing the node identifier string
	 */
    public synchronized void setNodeIdentifier(int[] nodeIdentifier) {
        this.ni.setData(nodeIdentifier);
        this.ni.setState(State.PARAM_SET);
    }

    /**
	 * Get the maximum numbers of hops for each broadcast data transmission.
	 * @return XBeeNodeParam the numbers of hops parameter
	 */
    public synchronized XBeeNodeParam getBroadcastRadius() {
        return this.bh;
    }

    /**
	 * Set the maximum number of hops for each broadcast data transmission.
	 * @param broadcastRadius the maximum numbers of hops
	 */
    public synchronized void setBroadcastRadius(int[] broadcastRadius) {
        this.bh.setData(broadcastRadius);
        this.bh.setState(State.PARAM_SET);
    }

    /**
	 * Get the time between consecutive aggregate route broadcast messages
	 * @return XBeeNodeParam the time of the aggregate route broadcasting
	 */
    public synchronized XBeeNodeParam getAggregationRouteBCTime() {
        return this.ar;
    }

    /**
	 * Set the time between consecutive aggregate route broadcast messages
	 * @param aggregTime the time of the aggregate route broadcasting
	 */
    public synchronized void setAggregationRouteBCTime(int[] aggregTime) {
        this.ar.setData(aggregTime);
        this.ar.setState(State.PARAM_SET);
    }

    /**
	 * Get the device type value
	 * @return XBeeNodeParam the device type
	 */
    public synchronized XBeeNodeParam getDeviceTypeIdentifier() {
        return this.dd;
    }

    /**
	 * Set the device type value
	 * @param dtIdentifier the device type
	 */
    public synchronized void setDeviceTypeIdentifier(int[] dtIdentifier) {
        this.dd.setData(dtIdentifier);
        this.dd.setState(State.PARAM_SET);
    }

    /**
	 * Get the amount of time a node will spend discovering other nodes when ND or DN is issued
	 * @return The discovery time
	 */
    public synchronized XBeeNodeParam getNodeDiscoveryBackoff() {
        return this.nt;
    }

    /**
	 * Set the amount of time a node will spend discovering other nodes when ND or DN is issued
	 * @param ndBackoff the amount of time
	 */
    public synchronized void setNodeDiscoveryBackoff(int[] ndBackoff) {
        this.nt.setData(ndBackoff);
        this.nt.setState(State.PARAM_SET);
    }

    /**
	 * Get the options value for the network discovery command
	 * @return The Options: <br />0x01 = Append DD value<br />Local device sends ND response frame when ND is issued
	 */
    public synchronized XBeeNodeParam getNodeDiscoveryOptions() {
        return this.no;
    }

    /**
	 * Get the options value for the network discovery command
	 * @param ndOptions <br />0x01 = Append DD value<br />Local device sends ND response frame when ND is issued
	 */
    public synchronized void setNodeDiscoveryOptions(int[] ndOptions) {
        this.no.setData(ndOptions);
        this.no.setState(State.PARAM_SET);
    }

    /**
	 * Get the power level of this node
	 * @return the power level at wich this node transmits conducted power
	 * (<tt>0-4 -> 10dBm-18dBm</tt>) 
	 */
    public synchronized XBeeNodeParam getPowerLevel() {
        return this.pl;
    }

    /**
	 * Set the power level of this node
	 * @param powerLevel The power level at witch this node transmits conducted power:
	 * <br /> 0 = -10/10dBm
	 * <br /> 1 = -6/12dBm
	 * <br /> 2 = -4/16 dBm
	 * <br /> 3 = -2/dBm
	 * <br /> 4 = 0/18dBm
	 */
    public synchronized void setPowerLevel(int[] powerLevel) {
        this.pl.setData(powerLevel);
        this.pl.setState(State.PARAM_SET);
    }

    /**
	 * Get the power mode of this node
	 * @return the power mode of this node. With a power mode 1, 
	 * the boost is enabled the receive sensitivity is increased by 1dB
	 * and the transmit power is increased by 2dB
	 */
    public synchronized XBeeNodeParam getPowerMode() {
        return this.pm;
    }

    /**
	 * Set the power mode of the device. 
	 * Enabling boost mode will improve the receive and transmit sensitivity/power.
	 * <br />0 - Boost mode disabled;<br />1 - Boost mode enabled
	 * @param powerMode <tt>0</tt> to disable boost mode, <tt>1</tt> to enable boost mode
	 */
    public synchronized void setPowerMode(int[] powerMode) {
        this.pm.setData(powerMode);
        this.pm.setState(State.PARAM_SET);
    }

    /**
	 * Get the encryption enable setting
	 * @return the setting of the encryption option
	 */
    public synchronized XBeeNodeParam getEncryptionEnable() {
        return this.ee;
    }

    /**
	 * Set the encryption enable setting
	 * @param encryptionEnable 0 - Encryprion disabled<br />1 - Encryption enabled
	 */
    public synchronized void setEncryptionEnable(int encryptionEnable) {
        this.ee.setData(new int[] { encryptionEnable });
        this.ee.setState(State.PARAM_SET);
    }

    /**
	 * Get the encryption option
	 * @return 0x01 - Send the security key unsecured over-the-air during joins<br />0x02 - Use the trust center
	 */
    public synchronized XBeeNodeParam getEncryptionOption() {
        return this.eo;
    }

    /**
	 * Set the encryption option.
	 * @param eOption 0x01 - Send the security key unsecured over-the-air during joins<br />0x02 - Use the trust center
	 */
    public synchronized void setEncryptionOption(int eOption) {
        this.eo.setData(new int[] { eOption });
        this.eo.setState(State.PARAM_SET);
    }

    /**
	 * Get the 128-bit AES encryption key. This command is read-only on the XBee; KY cannot be-read.
	 * @return The 128-bit AES encryption key
	 */
    public synchronized XBeeNodeParam getEncryptionKey() {
        return this.ky;
    }

    /**
	 * Set the 128-bit AES encryption key. This command is read-only on the XBee; KY cannot be-read.
	 * @param encryptionKey The 128-bit AES encryption key
	 */
    public synchronized void setEncryptionKey(int[] encryptionKey) {
        this.ky.setData(encryptionKey);
        this.ky.setState(State.PARAM_SET);
    }

    /**
	 * Get the serial interface data rate for communication between the module serial port and host.
	 * @return (0-7 standard Baud Rate) <br />0 = 1200bps<br />
	 * 1 = 2400bps<br />
	 * 2 = 4800bps<br />
	 * 3 = 9600bps<br />
	 * 4 = 19200bps<br />
	 * 5 = 38400bps<br />
	 * 6 = 57600bps<br />
	 * 7 = 115200bps<br />(0x80-0x38400 non-standard rates)
	 */
    public synchronized XBeeNodeParam getBaudRate() {
        return this.bd;
    }

    /**
	 * Set the serial interface data rate for communication between the module serial port and host.
	 * @param baudRate (0-7 standard Baud Rate) <br />0 = 1200bps<br />
	 * 1 = 2400bps<br />
	 * 2 = 4800bps<br />
	 * 3 = 9600bps<br />
	 * 4 = 19200bps<br />
	 * 5 = 38400bps<br />
	 * 6 = 57600bps<br />
	 * 7 = 115200bps<br />(0x80-0x38400 non-standard rates)
	 */
    public synchronized void setBaudRate(int[] baudRate) {
        this.bd.setData(baudRate);
        this.bd.setState(State.PARAM_SET);
    }

    /**
	 * Get the serial parity on the module
	 * @return 0 = no parity<br />1 = even parity<br />2 = odd parity<br />3 = mark parity
	 */
    public synchronized XBeeNodeParam getParity() {
        return this.nb;
    }

    /**
	 * Set the serial parity on the module
	 * @param parity <br />0 = no parity<br />1 = even parity<br />2 = odd parity<br />3 = mark parity
	 */
    public synchronized void setParity(int[] parity) {
        this.nb.setData(parity);
        this.nb.setState(State.PARAM_SET);
    }

    /**
	 * Get the options for the DIO7 line of the module 
	 * @return the options of the DIO7 line<br />0 = Disabled<br />1 = CTS Flow Control<br />3 = Digital input<br />4 = Digital Output,low<br />
	 * 5 = Digital Output,high<br />6 = RS485 transmit enable (low enable)<br />7 = RS482 transmit enable (high enable)
	 */
    public synchronized XBeeNodeParam getDIO7Configuration() {
        return this.d7;
    }

    /**
	 * Set the options for the DIO7 line of the module
	 * @param dio7Config <br />0 = Disabled<br />1 = CTS Flow Control<br />3 = Digital input<br />4 = Digital Output,low<br />
	 * 5 = Digital Output,high<br />6 = RS485 transmit enable (low enable)<br />7 = RS482 transmit enable (high enable)
	 */
    public synchronized void setDIO7Configuration(int[] dio7Config) {
        this.d7.setData(dio7Config);
        this.d7.setState(State.PARAM_SET);
    }

    /**
	 * Get the options for the DIO6 line of the RF module. 
	 * @return 0 - Disabled<br />1 - RTS FLow Control
	 */
    public synchronized XBeeNodeParam getDIO6Configuration() {
        return this.d6;
    }

    /**
	 * Set the options for the DIO6 line of the RF module. 
	 * @param dio6Config 0 - Disabled<br />1 - RTS FLow Control
	 */
    public synchronized void setDIO6Configuration(int[] dio6Config) {
        this.d6.setData(dio6Config);
        this.d6.setState(State.PARAM_SET);
    }

    /**
	 * Get the API enable mode
	 * @return 1 - API enabled<br />2 - API enabled(w/escape control characters)
	 */
    public synchronized XBeeNodeParam getAPIEnable() {
        return this.ap;
    }

    /**
	 * Set the API enable mode
	 * @param apiEnable <br />1 - API enabled<br />2 - API enabled(w/escape control characters)
	 */
    public synchronized void setAPIEnable(int[] apiEnable) {
        this.ap.setData(apiEnable);
        this.ap.setState(State.PARAM_SET);
    }

    /**
	 * Get options dor API. Current options select the type of the receive API frame to send out the Uart for received RF data packets
	 * @return 0 - Default receive API indicators enabled<br />1 - Explicit RX data indicator API frame enabled (0x91)
	 */
    public synchronized XBeeNodeParam getAPIOptions() {
        return this.ao;
    }

    /**
	 * Set options dor API. Current options select the type of the receive API frame to send out the Uart for received RF data packets
	 * @param apiOptions 0 - Default receive API indicators enabled<br />1 - Explicit RX data indicator API frame enabled (0x91)
	 */
    public synchronized void setAPIOptions(int[] apiOptions) {
        this.ao.setData(apiOptions);
        this.ao.setState(State.PARAM_SET);
    }

    /**
	 * Get the sleep mode option
	 * @return The sleep mode option: 0 - Sleep disabled<br />1 - Pin sleep enabled<br />4 - Cyclic sleep enabled
	 */
    public synchronized XBeeNodeParam getSleepMode() {
        return this.sm;
    }

    /**
	 * Set the sleep mode option
	 * @param sMode  <br />0 - Sleep disabled<br />1 - Pin sleep enabled<br />4 - Cyclic sleep enabled
	 */
    public synchronized void setSleepMode(int[] sMode) {
        this.sm.setData(sMode);
        this.sm.setState(State.PARAM_SET);
    }

    /**
	 * Get the time before sleep timer on an end device. The timer is reset each time serial or RF data is received. Once the timer expires, and end device may enter low power operation. Applicable for cyclic sleep end device only.
	 * @return the time before sleep timer on an end device.
	 */
    public synchronized XBeeNodeParam getTimeBeforeSleep() {
        return this.st;
    }

    /**
 	 * Set the time before sleep timer on an end device. The timer is reset each time serial or RF data is received. Once the timer expires, and end device may enter low power operation. Applicable for cyclic sleep end device only.
	 * @param time 1-oxFFFE (x 1ms)
	 */
    public synchronized void setTimeBeforeSleep(int[] time) {
        this.st.setData(time);
        this.st.setState(State.PARAM_SET);
    }

    public synchronized XBeeNodeParam getCyclicSleepPeriod() {
        return this.sp;
    }

    public synchronized void setCyclicSleepPeriod(int[] period) {
        this.sp.setData(period);
        this.sp.setState(State.PARAM_SET);
    }

    public synchronized XBeeNodeParam getNumberOfCycleToPwrDown() {
        return this.sn;
    }

    public synchronized void setNumberOfCycleToPwrDown(int[] cycle) {
        this.sn.setData(cycle);
        this.sn.setState(State.PARAM_SET);
    }

    public synchronized XBeeNodeParam getSleepOption() {
        return this.so;
    }

    public synchronized void setSleepOption(int[] option) {
        this.so.setData(option);
        this.so.setState(State.PARAM_SET);
    }

    /**
	 * Get the Firmware Version of this node
	 * @return The firmware version
	 */
    public synchronized XBeeNodeParam getFirmwareVersion() {
        return this.vr;
    }

    /**
	 * Set the firmware version of this node on list after an ATVR command
	 * @param fwVersion the data returned by an ATVR command
	 */
    public synchronized void setFirmwareVersion(int[] fwVersion) {
        this.vr.setData(fwVersion);
        this.vr.setState(State.PARAM_SET);
    }

    /**
	 * Get the hardware version of this node
	 * @return the hardware version of this node
	 */
    public synchronized XBeeNodeParam getHardwareVersion() {
        return this.hv;
    }

    /**
	 * Set the hardware version of this node on list after an ATHV command
	 * @param hwVersion the hardware version data received from an ATHV command
	 */
    public synchronized void setHardwareVersion(int[] hwVersion) {
        this.hv.setData(hwVersion);
        this.hv.setState(State.PARAM_SET);
    }

    /**
	 * Get the information regarding last node join request
	 * @return 0x21 - Scan found no PANs<br />0x22 - Scan found no PANsbased on current SC and ID settings
	 * <br />0x23 - Valid Coordinator or Routers found, but they are not allowing joining (NJ expected)<br />0x27 - Node JOining attempt failed
	 * <br />0x2A - Coordinator Start attempt ailed<br />0xFF - Scanning for a Parent
	 */
    public synchronized XBeeNodeParam getAssociationIndication() {
        return this.ai;
    }

    /**
	 * Set the information regarding the last node join request
	 * @param assocInd 0x21 - Scan found no PANs<br />0x22 - Scan found no PANsbased on current SC and ID settings
	 * <br />0x23 - Valid Coordinator or Routers found, but they are not allowing joining (NJ expected)<br />0x27 - Node JOining attempt failed
	 * <br />0x2A - Coordinator Start attempt ailed<br />0xFF - Scanning for a Parent
	 */
    public synchronized void setAssociationIndication(int[] assocInd) {
        this.ai.setData(assocInd);
        this.ai.setState(State.PARAM_SET);
    }

    /**
	 * Get the supply voltage of the device on the Vcc pin. To convert the reading to a mV reading, divede the read value by 1023 and multiply by 1200.
	 * A %V reading of 0x8FE(2302 decimal) represent 2700mV of 2,70V.
	 * @return the supply voltage of this device
	 */
    public synchronized XBeeNodeParam getSupplyVoltage() {
        return this.sv;
    }

    /**
	 * Get the supply voltage of the device on the Vcc pin. To convert the reading to a mV reading, divede the read value by 1023 and multiply by 1200.
	 * A %V reading of 0x8FE(2302 decimal) represent 2700mV of 2,70V.
	 * @param supplyVoltage the supply voltage of this device received from an ATHV command
	 */
    public synchronized void setSupplyVoltage(int[] supplyVoltage) {
        this.sv.setData(supplyVoltage);
        this.sv.setState(State.PARAM_SET);
    }

    /**
	 * Get the last time that this node is saw by a Coordinator
	 * @return <tt>Date</tt> The Date of the last time saw by Coordinator
	 */
    public synchronized Date getLastTimeSaw() {
        if (lastTimeSaw != null) return lastTimeSaw; else return null;
    }

    /**
	 * Add a XBeeNodeService to an XBeeNode
	 * @param service an XBeeNodeService 
	 */
    public synchronized void addService(XBeeNodeService service) {
        if (!this.services.containsKey(service.getServiceName())) this.services.put(service.getServiceName(), service);
    }

    /**
	 * Get the list of all services that provides this node
	 * @return <tt>Collection</tt> of XBeeNodeService containing all the services
	 */
    public synchronized ConcurrentHashMap<String, XBeeNodeService> getServices() {
        return this.services;
    }

    public synchronized String startService(String serviceName) {
        if (this.services.containsKey(serviceName)) {
            logger.debug("Service present");
            XBeeNodeService service = (XBeeNodeService) this.services.get(serviceName);
            logger.debug("Service present on the List");
            return service.getService();
        } else return null;
    }

    /**
	 * Get the type of this Node: <tt>COORDINATOR,END_DEVICE</tt> or <tt>ROUTER</tt>
	 * @return <tt>XBeeNodeType</tt> for this node
	 */
    public synchronized DeviceType getType() {
        return this.type;
    }

    public synchronized int getIntType() {
        return this.type.getValue();
    }

    /**
	 * Remove the desidered service from this node
	 * @param service the XBeeService to remove
	 */
    public synchronized void removeService(XBeeNodeService service) {
    }

    /**
	 * Set the last time that the coordinator saw this node
	 * @param data The date of the last time saw
	 */
    public synchronized void setLastTimeSaw(Date data) {
        this.lastTimeSaw = new Date();
    }

    /**
	 * Set the type of this node
	 * @param type The type of this node <tt>COORDINATOR,END_DEVICE</tt> or <tt>ROUTER</tt> defined by XBeeNodeType
	 */
    public synchronized void setType(DeviceType type) {
        this.type = type;
    }

    /**
	 * Add a new received packet to the packet list of this node
	 * @param responsePacket The received packet 
	 */
    public synchronized boolean addNewPacket(XBeeResponse responsePacket) {
        return false;
    }

    /**
	 * Get the interface data rate for this node
	 * @return <tt>int</tt> containing the reference to the data rate in bps specified by the XBee Series 2 Manual
	 */
    public synchronized XBeeNodeParam getInterfaceDataRate() {
        return null;
    }

    /**
	 * Get the PAN ID of the network joined by this node
	 * @return an <tt>int</tt> corrisponding to the channel of the node PAN
	 */
    public synchronized XBeeNodeParam getPanID() {
        return null;
    }

    public synchronized void getPackets() {
    }

    /**
	 * Set the active parameter on this node. A device is inactive if it's on the XBeeNodeList
	 * but it's sleeping or not reachable.
	 * @param active <tt>true</tt> to set Active, <tt>false</tt> to set Inactive.
	 */
    public synchronized void setActive(boolean active) {
        this.isActive = active;
    }

    /**
	 * Setter/Getter for Analog Input.
	 * The setter save the analog input to an XBeeNodeParam
	 * The Getter return the value of the analog input
	 */
    public synchronized void setAnalog0(int[] an0) {
        this.analog0.setData(an0);
        this.analog0.setState(State.PARAM_SET);
    }

    public synchronized void setAnalog1(int[] an1) {
        this.analog1.setData(an1);
        this.analog1.setState(State.PARAM_SET);
    }

    public synchronized void setAnalog2(int[] an2) {
        this.analog2.setData(an2);
        this.analog2.setState(State.PARAM_SET);
    }

    public synchronized void setAnalog3(int[] an3) {
        this.analog3.setData(an3);
        this.analog3.setState(State.PARAM_SET);
    }

    public synchronized XBeeNodeParam getAnalog0() {
        return this.analog0;
    }

    public synchronized XBeeNodeParam getAnalog1() {
        return this.analog1;
    }

    public synchronized XBeeNodeParam getAnalog2() {
        return this.analog2;
    }

    public synchronized XBeeNodeParam getAnalog3() {
        return this.analog3;
    }

    /**
	 * Setter/Getter for Digital Input.
	 * The setter save the analog input to an XBeeNodeParam
	 * The Getter return the value of the analog input
	 */
    public synchronized void setDigital0(boolean d0) {
        this.dIO0 = d0;
    }

    public synchronized void setDigital1(boolean d1) {
        this.dIO1 = d1;
    }

    public synchronized void setDigital2(boolean d2) {
        this.dIO2 = d2;
    }

    public synchronized void setDigital3(boolean d3) {
        this.dIO3 = d3;
    }

    public synchronized void setDigital4(boolean d4) {
        this.dIO4 = d4;
    }

    public synchronized void setDigital5(boolean d5) {
        this.dIO5 = d5;
    }

    public synchronized void setDigital6(boolean d6) {
        this.dIO6 = d6;
    }

    public synchronized void setDigital7(boolean d7) {
        this.dIO7 = d7;
    }

    public synchronized void setDigital10(boolean d10) {
        this.dIO10 = d10;
    }

    public synchronized void setDigital11(boolean d11) {
        this.dIO11 = d11;
    }

    public synchronized void setDigital12(boolean d12) {
        this.dIO12 = d12;
    }

    public synchronized boolean getDigital0() {
        return this.dIO0;
    }

    public synchronized boolean getDigital1() {
        return this.dIO1;
    }

    public synchronized boolean getDigital2() {
        return this.dIO2;
    }

    public synchronized boolean getDigital3() {
        return this.dIO3;
    }

    public synchronized boolean getDigital4() {
        return this.dIO4;
    }

    public synchronized boolean getDigital5() {
        return this.dIO5;
    }

    public synchronized boolean getDigital6() {
        return this.dIO6;
    }

    public synchronized boolean getDigital7() {
        return this.dIO7;
    }

    public synchronized boolean getDigital10() {
        return this.dIO10;
    }

    public synchronized boolean getDigital11() {
        return this.dIO11;
    }

    public synchronized boolean getDigital12() {
        return this.dIO12;
    }
}
