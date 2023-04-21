package net.tourbook.importdata;

/**
 * Contains device data
 * 
 * @author Wolfgang Schramm
 */
public class DeviceData {

    /**
	 * the transfer date is used to create the filename for the transfered tours
	 */
    public short transferYear = 1999;

    public short transferMonth = 1;

    public short transferDay = 1;

    /**
	 * Contains a unique id so that each import can be identified.
	 */
    public long importId;
}
