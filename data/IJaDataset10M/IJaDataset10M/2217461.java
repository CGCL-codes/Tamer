package com.szxys.mhub.interfaces;

/**
 * 采集器。
 */
public class Collector {

    /**
	 * 编号。
	 */
    public int Id;

    /**
	 * 心跳间隔。
	 */
    public int HeartBeatInterval;

    /**
	 * 物理编码。
	 */
    public String PhysicalCode;

    /**
	 * 采集器类型。
	 */
    public byte DeviceType;

    /**
	 * 蓝牙地址。
	 */
    public String Mac;

    /**
	 * 通道总数。
	 */
    public byte NumOfChannels;

    /**
	 * 蓝牙设备配对码。
	 */
    public String PairingCode;

    /**
	 * 是否是移动终端被采集器连接。
	 */
    public boolean PassiveMode;

    /**
	 * 蓝牙协议类型。
	 */
    public int ProtocolType;

    /**
	 * 描述信息。
	 */
    public String Desc;

    /**
	 * 构造函数。
	 */
    public Collector() {
    }

    /**
	 * 构造函数。
	 * 
	 * @param id
	 *            ：编号。
	 * @param heartBeatInterval
	 *            ：心跳间隔。
	 * @param physicalCode
	 *            ：物理编码。
	 * @param deviceType
	 *            ：采集器类型。
	 * @param mac
	 *            ：蓝牙地址。
	 * @param numOfChannels
	 *            ：通道总数。
	 * @param pairingCode
	 *            ：蓝牙设备配对码。
	 * @param passiveMode
	 *            ：是否是移动终端被采集器连接。
	 * @param protocolType
	 *            ：蓝牙协议类型。
	 * @param desc
	 *            ：描述信息。
	 */
    public Collector(int id, int heartBeatInterval, String physicalCode, byte deviceType, String mac, byte numOfChannels, String pairingCode, boolean passiveMode, int protocolType, String desc) {
        this.Id = id;
        this.HeartBeatInterval = heartBeatInterval;
        this.PhysicalCode = physicalCode;
        this.DeviceType = deviceType;
        this.Mac = mac;
        this.NumOfChannels = numOfChannels;
        this.PairingCode = pairingCode;
        this.PassiveMode = passiveMode;
        this.ProtocolType = protocolType;
        this.Desc = desc;
    }
}
