package com.sun.jsr082.bluetooth;

public class GenericBluetoothStack extends BluetoothStack {

    private final int MAX_HCI_PACKET_SIZE = 257;

    private final int HCI_INQUIRY_COMPLETE = 0x01;

    private final int HCI_INQUIRY_RESULT = 0x02;

    private final int HCI_AUTH_COMPLETE = 0x06;

    private final int HCI_NAME_COMPLETE = 0x07;

    private final int HCI_ENCRYPT_CHANGE = 0x08;

    private byte[] buffer = new byte[MAX_HCI_PACKET_SIZE];

    protected String extractAddress(int offset) {
        byte[] addr = new byte[BluetoothUtils.BTADDR_SIZE];
        System.arraycopy(buffer, offset, addr, 0, BluetoothUtils.BTADDR_SIZE);
        return BluetoothUtils.getAddressString(addr);
    }

    protected String extractString(int offset) {
        int length = 0;
        while (offset + length < MAX_HCI_PACKET_SIZE && buffer[offset + length] != 0) {
            length++;
        }
        try {
            return new String(buffer, offset, length, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    protected int extractShort(int offset) {
        return ((int) buffer[offset] & 0xff) | (((int) buffer[offset + 1] & 0xff) << 8);
    }

    protected BluetoothEvent retrieveEvent() {
        readData(buffer);
        int code = buffer[0];
        int len = buffer[1];
        switch(code) {
            case HCI_INQUIRY_COMPLETE:
                return new InquiryCompleteEvent(buffer[2] == 0);
            case HCI_INQUIRY_RESULT:
                int num = buffer[2];
                int offset = 3;
                InquiryResult[] results = new InquiryResult[num];
                for (int i = 0; i < num; i++) {
                    String addr = extractAddress(offset);
                    int cod = (buffer[offset + 9] & 0xFF) + ((buffer[offset + 10] & 0xFF) << 8) + ((buffer[offset + 11] & 0xFF) << 16);
                    results[i] = new InquiryResult(addr, cod);
                    offset += 14;
                }
                return new InquiryResultEvent(results);
            case HCI_AUTH_COMPLETE:
                return new AuthenticationCompleteEvent(extractShort(3), buffer[2] == 0);
            case HCI_NAME_COMPLETE:
                return new NameResultEvent(extractAddress(3), extractString(9));
            case HCI_ENCRYPT_CHANGE:
                return new EncryptionChangeEvent(extractShort(3), buffer[2] == 0, buffer[5] == 1);
            default:
                return null;
        }
    }
}
