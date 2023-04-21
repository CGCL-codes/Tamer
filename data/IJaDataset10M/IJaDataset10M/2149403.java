package jp.ac.nitech.matlab.najm.net;

/**
 * <[  بسم الله الرحمان الرحيم  ]
 * @author k-hon
 *
 */
public class Packet {

    public static final int CTRL = 16;

    public static final int DATA4 = 4096;

    public static final int DATA8 = 8192;

    public static final int DATA16 = 16384;

    public static final int DATA32 = 32768;

    public static final int DATA64 = 65536;

    public byte[] buffer;

    private int type = -1;

    public boolean isData() {
        return (type != CTRL) ? true : false;
    }

    public Packet(int type) {
        this.type = type;
        buffer = new byte[type];
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public String getControl() {
        return new String(buffer);
    }

    public String getCtrlTarget() {
        return getControl().substring(7);
    }

    public String getCtrlCmd() {
        return getControl().substring(0, 3);
    }

    public void setControl(String cmd, String code, String target) {
        buffer = (cmd + code + target).getBytes();
    }

    class DataPacket extends Packet {

        public DataPacket() {
            super(Packet.DATA4);
        }
    }
}
