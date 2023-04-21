package knet.net.inet;

import java.io.*;
import java.net.*;
import knet.net.*;

public class Address implements KAddress {

    InetAddress address;

    public Address(InetAddress anAddress) {
        address = anAddress;
    }

    public KInterface getInterface() {
        return null;
    }

    public InetAddress getAddress() {
        return address;
    }

    public String toString() {
        return address.toString();
    }
}
