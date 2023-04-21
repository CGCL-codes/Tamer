package knet.chat;

import knet.net.*;

public class ChatHandle {

    String handle;

    KAddress address;

    public ChatHandle(String aHandle) {
        handle = aHandle;
        address = null;
    }

    public ChatHandle(String aHandle, KAddress anAddress) {
        handle = aHandle;
        address = anAddress;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String aHandle) {
        handle = aHandle;
    }

    public KAddress getAddress() {
        return address;
    }

    public void setAddress(KAddress anAddress) {
        address = anAddress;
    }
}
