package xboxphreak.system;

import java.util.*;
import jpcap.*;
import java.net.InetAddress;

public class Network {

    List<Proxy> remoteHosts;

    List<Device> localHosts;

    NetworkInterface netInterface;

    public Network() {
        remoteHosts = new ArrayList<Proxy>();
        localHosts = new ArrayList<Device>();
    }

    public void addRemoteHost(Proxy p) {
        remoteHosts.add(p);
    }

    public NetworkInterface getNetInterface() {
        return netInterface;
    }

    public void setNetInterface(NetworkInterface netInterface) {
        this.netInterface = netInterface;
    }

    public InetAddress localAddress() {
        return netInterface.addresses[0].address;
    }

    public byte[] localMacAddress() {
        return netInterface.mac_address;
    }

    public String toString() {
        String result = "Network:\n";
        result += "-Local Devices\n";
        for (Device device : localHosts) {
            result += "--" + device.toString() + "\n";
        }
        result += "-Remote Proxies\n";
        for (Proxy proxy : remoteHosts) {
            result += "--" + proxy.toString() + "\n";
        }
        return result;
    }

    public InetAddress findDestination(InetAddress remoteDeviceAddress) {
        for (Proxy proxy : remoteHosts) {
            for (Device device : proxy.getDevices()) {
                if (device.getAddress().equals(remoteDeviceAddress)) {
                    return proxy.getAddress();
                }
            }
        }
        return null;
    }

    public byte[] findDestinationMAC(InetAddress remoteDeviceAddress) {
        for (Proxy proxy : remoteHosts) {
            for (Device device : proxy.getDevices()) {
                if (device.getAddress().equals(remoteDeviceAddress)) {
                    return proxy.getReturnAddress();
                }
            }
        }
        return null;
    }

    public boolean isLocalAddress(InetAddress addy) {
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();
        for (NetworkInterface device : devices) {
            for (NetworkInterfaceAddress address : device.addresses) {
                if (address.address.equals(addy)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isProxyAddress(InetAddress addy) {
        for (Proxy proxy : remoteHosts) {
            if (proxy.getAddress().equals(addy)) {
                return true;
            }
        }
        return false;
    }

    public boolean isRemoteDeviceAddress(InetAddress addy) {
        for (Proxy proxy : remoteHosts) {
            for (Device device : proxy.getDevices()) {
                if (device.getAddress().equals(addy)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Proxy> getRemoteHosts() {
        return remoteHosts;
    }

    public void syncLocalDevice(InetAddress ip, byte[] mac) {
        for (Device device : localHosts) {
            if (device.getAddress().equals(ip)) {
                device.setMac(mac);
                return;
            }
        }
        Device newDevice = new Device(ip);
        newDevice.setMac(mac);
        localHosts.add(newDevice);
    }

    public void syncRemoteDevice(InetAddress proxyip, InetAddress ip, byte[] mac) {
        for (Proxy proxy : remoteHosts) {
            for (Device device : proxy.getDevices()) {
                if (device.getAddress().equals(ip)) {
                    device.setMac(mac);
                    return;
                }
            }
        }
        Device newDevice = new Device(ip);
        newDevice.setMac(mac);
        for (Proxy proxy : remoteHosts) {
            if (proxy.getAddress().equals(proxyip)) {
                proxy.addDevice(newDevice);
                return;
            }
        }
    }

    public void syncRemoteProxy(InetAddress ip, byte[] mac) {
        for (Proxy proxy : remoteHosts) {
            if (proxy.getAddress().equals(ip)) {
                proxy.setReturnAddress(mac);
                return;
            }
        }
    }

    public static String mac2str(byte[] mac) {
        String result = "[";
        for (byte b : mac) {
            result += (new Integer(b)) + ",";
        }
        result += "]";
        return result;
    }
}
