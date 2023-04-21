package org.javasock.application.dhcpoptions;

/**
  * Duration for which the leased IP address is valid.
  */
public class IPLeaseTimeValue extends TimeValue {

    public static int CODE = 51;

    public IPLeaseTimeValue(long time) {
        super(CODE, time);
    }

    public IPLeaseTimeValue(java.nio.ByteBuffer bb) {
        super(bb);
    }
}
