package net.tourbook.device.hac5;

import java.io.IOException;
import java.io.RandomAccessFile;
import net.tourbook.device.DeviceReaderTools;

public class HAC5DeviceData {

    private static final int OFFSET_DEVICE_DATA = 0x380;

    private long bike1DrivingTime;

    private long bike1Distance;

    private long bike1AltitudeUp;

    private long bike1AltitudeDown;

    public void readFromFile(RandomAccessFile fileRawData) {
        byte[] recordBuffer = new byte[0x10];
        try {
            fileRawData.seek(HAC5DeviceDataReader.OFFSET_RAWDATA + OFFSET_DEVICE_DATA + 4);
            fileRawData.read(recordBuffer);
            bike1DrivingTime = DeviceReaderTools.get4ByteData(recordBuffer, 0);
            bike1Distance = DeviceReaderTools.get4ByteData(recordBuffer, 4);
            bike1AltitudeUp = DeviceReaderTools.get4ByteData(recordBuffer, 8);
            bike1AltitudeDown = DeviceReaderTools.get4ByteData(recordBuffer, 12);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
