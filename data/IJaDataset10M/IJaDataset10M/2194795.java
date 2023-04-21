package com.intel.bluetooth;

import android.bluetooth.BluetoothSocket;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;

/**
 *
 * @author Mina Shokry
 */
public class AndroidServiceRecord extends ServiceRecordImpl {

    private BluetoothSocket socket;

    private boolean obex;

    private UUID uuid;

    public AndroidServiceRecord(BluetoothStack bluetoothStack, RemoteDevice device, BluetoothSocket socket, UUID uuid, boolean obex) {
        super(bluetoothStack, device, 0);
        this.socket = socket;
        this.obex = obex;
        this.uuid = uuid;
    }

    @Override
    public String getConnectionURL(int requiredSecurity, boolean mustBeMaster) {
        StringBuilder buf = new StringBuilder();
        buf.append(obex ? BluetoothConsts.PROTOCOL_SCHEME_BT_OBEX : BluetoothConsts.PROTOCOL_SCHEME_RFCOMM);
        buf.append("://");
        buf.append(socket.getRemoteDevice().getAddress().replace(":", ""));
        buf.append(":");
        buf.append(uuid.toString());
        switch(requiredSecurity) {
            case NOAUTHENTICATE_NOENCRYPT:
                buf.append(";authenticate=false;encrypt=false");
                break;
            case AUTHENTICATE_NOENCRYPT:
                buf.append(";authenticate=true;encrypt=false");
                break;
            case AUTHENTICATE_ENCRYPT:
                buf.append(";authenticate=true;encrypt=true");
                break;
            default:
                throw new IllegalArgumentException();
        }
        if (mustBeMaster) {
            buf.append(";master=true");
        } else {
            buf.append(";master=false");
        }
        buf.append(";android=true");
        return buf.toString();
    }
}
