package com.xohm.base;

import com.sun.jna.*;
import com.xohm.base.structs.ConnectedNspInfo;
import com.xohm.base.structs.ConnectionStatistics;
import com.xohm.base.structs.ContactInfo;
import com.xohm.base.structs.DeviceId;
import com.xohm.base.structs.DeviceInfo;
import com.xohm.base.structs.HardwareDeviceIdList;
import com.xohm.base.structs.InterfaceInfo;
import com.xohm.base.structs.LinkStatusInfo;
import com.xohm.base.structs.NSPInfo;
import com.xohm.base.structs.PackageInfo;
import com.xohm.base.structs.ProfileInfo;

/**
 * <B>This class is the definition of the WiMax Common API to the vendors driver.</B><br><br>
 *
 * <font size=-1>Open source WiMAX connection manager<br>
 * � Copyright Sprint Nextel Corp. 2008</font><br><br>
 *
 * @author Robin Katzer
 */
public interface CommonAPI extends Library {

    public int WiMaxAPIOpen(DeviceId[] pDeviceId);

    public int GetListDevice(DeviceId[] pDeviceId, HardwareDeviceIdList[] pHwDeviceIdList, int[] pHwDeviceIdListSize);

    public int WiMaxDeviceOpen(DeviceId pDeviceId);

    public int WiMaxDeviceClose(DeviceId pDeviceId);

    public int WiMaxAPIClose(DeviceId pDeviceId);

    public int CmdControlPowerManagement(DeviceId pDeviceId, int powerState);

    public int CmdResetWimaxDevice(DeviceId pDeviceId);

    public int CmdResetToFactorySettings(DeviceId pDeviceId);

    public int GetErrorString(DeviceId pDeviceId, int errorCode, char[] buffer, int[] pLength);

    public int SetServiceProviderUnLock(DeviceId pDeviceId, char[] lockCode);

    public int GetServiceProviderLockStatus(DeviceId pDeviceId, int[] pLockStatus, char[] NSPName);

    public int GetNetworkList(DeviceId pDeviceId, NSPInfo[] pNSPInfo, int[] pArrayLength);

    public int CmdConnectToNetwork(DeviceId pDeviceId, WString nspName, int profileId, char[] password);

    public int CmdDisconnectFromNetwork(DeviceId pDeviceId);

    public int CmdNetworkSearchWideScan(DeviceId pDeviceId);

    public int GetIPInterfaceIndex(DeviceId pDeviceId, InterfaceInfo[] pInterfaceInfo);

    public int GetSelectProfileList(DeviceId pDeviceId, ProfileInfo[] pProfileList, int[] pListSize);

    public int GetLinkStatus(DeviceId pDeviceId, LinkStatusInfo[] pLinkStatus);

    public int GetDeviceInformation(DeviceId pDeviceId, DeviceInfo[] pDeviceInfo);

    public int GetDeviceStatus(DeviceId[] pDeviceId, int[] pDeviceStatus, int[] pConnectionProgressInfo);

    public int GetConnectedNSP(DeviceId pDeviceId, ConnectedNspInfo[] pConnectedNSP);

    public int SetRoamingMode(DeviceId pDeviceId, int roamingMode);

    public int GetRoamingMode(DeviceId pDeviceId, int[] pRoamingMode);

    public int GetStatistics(DeviceId pDeviceId, ConnectionStatistics[] pStatistics);

    public int GetProvisioningStatus(DeviceId pDeviceId, char nspName, boolean[] pProvisoningStatus);

    public int GetContactInformation(DeviceId pDeviceId, byte[] nspName, ContactInfo[] pContactInfo, int[] pSizeOfContactList);

    public int GetPackageInformation(DeviceId pDeviceId, PackageInfo[] pPackageInfo);

    public int SetPackageUpdateState(DeviceId pDeviceId, int packageUpdateState);

    public int SubscribeDeviceStatusChange(DeviceId pDeviceId, Callback pCallbackFnc);

    public int SubscribeDeviceInsertRemove(DeviceId pDeviceId, Callback pCallbackFnc);

    public int SubscribeControlPowerManagement(DeviceId pDeviceId, Callback pCallbackFnc);

    public int SubscribeConnectToNetwork(DeviceId pDeviceId, Callback pCallbackFnc);

    public int SubscribeDisconnectToNetwork(DeviceId pDeviceId, Callback pCallbackFnc);

    public int SubscribeNetworkSearchWideScan(DeviceId pDeviceId, Callback pCallbackFnc);

    public int SubscribeProvisioningOperation(DeviceId pDeviceId, Callback pCallbackFnc);

    public int SubscribePackageUpdate(DeviceId pDeviceId, Callback pCallbackFnc);

    public int UnsubscribeDeviceStatusChange(DeviceId pDeviceId);

    public int UnsubscribeDeviceInsertRemove(DeviceId pDeviceId);

    public int UnsubscribeControlPowerManagement(DeviceId pDeviceId);

    public int UnsubscribeConnectToNetwork(DeviceId pDeviceId);

    public int UnsubscribeDisconnectToNetwork(DeviceId pDeviceId);

    public int UnsubscribeNetworkSearchWideScan(DeviceId pDeviceId);

    public int UnsubscribeProvisioningOperation(DeviceId pDeviceId);

    public int UnsubscribePackageUpdate(DeviceId pDeviceId);
}
