package net.tourbook.device.hac4pro;

import java.io.IOException;
import java.io.RandomAccessFile;

public class HAC4ProDeviceData {

    public void readFromFile(RandomAccessFile fileRawData) {
        try {
            fileRawData.seek(0x0 + HAC4ProDeviceDataReader.OFFSET_RAWDATA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
