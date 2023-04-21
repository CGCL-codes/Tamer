package vavi.net.im.protocol.oscar.flap;

/**
 * @author mikem
 */
public class FlapConstants {

    public static byte FLAP_PREAMBLE = 0x2a;

    public static byte FLAP_CHANNEL_CONNECT = 0x01;

    public static byte FLAP_CHANNEL_SNAC = 0x02;

    public static byte FLAP_CHANNEL_ERROR = 0x03;

    public static byte FLAP_CHANNEL_DISCONNECT = 0x04;

    public static byte FLAP_CHANNEL_KEEP_ALIVE = 0x05;

    public static int FLAP_MAX_DATA_LENGTH = 0xffff;
}
