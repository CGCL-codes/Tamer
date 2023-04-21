package tcg.scada.iec;

import java.util.Calendar;
import java.util.Date;

public class IecTimeTagHelper {

    enum TimeTagType {

        CP16Time2a, CP24Time2a, CP32Time2a, CP56Time2a
    }

    ;

    /**
	 * Build raw byte array of CP16Time2a (2 bytes) for socket transmission
	 * CP16 {msec}
	 */
    public static byte[] build_cp16timetag(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        byte[] buffer = new byte[2];
        int tempVal = (cal.get(Calendar.SECOND) * 1000) + cal.get(Calendar.MILLISECOND);
        buffer[0] = (byte) (tempVal & 0x00ff);
        buffer[1] = (byte) ((tempVal & 0xff00) >> 8);
        return buffer;
    }

    /**
	 * Build raw byte array of Timetag (3 bytes) for socket transmission
	 * CP24 {msec, min, RES1, invalid}
	 */
    public static byte[] build_cp24timetag(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        byte[] buffer = new byte[3];
        int tempVal = (cal.get(Calendar.SECOND) * 1000) + cal.get(Calendar.MILLISECOND);
        buffer[0] = (byte) (tempVal & 0x00ff);
        buffer[1] = (byte) ((tempVal & 0xff00) >> 8);
        buffer[2] = (byte) (cal.get(Calendar.MINUTE) & 0x003f);
        return buffer;
    }

    /**
	 * Build raw byte array of Timetag (4 bytes) for socket transmission
	 * CP32 {msec, min, RES1, invalid, hour, RES2, summer time}
	 */
    public static byte[] build_cp32timetag(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        byte[] buffer = new byte[4];
        int tempVal = (cal.get(Calendar.SECOND) * 1000) + cal.get(Calendar.MILLISECOND);
        buffer[0] = (byte) (tempVal & 0x00ff);
        buffer[1] = (byte) ((tempVal & 0xff00) >> 8);
        buffer[2] = (byte) (cal.get(Calendar.MINUTE) & 0x003f);
        buffer[3] = (byte) (cal.get(Calendar.HOUR_OF_DAY) & 0x001f);
        return buffer;
    }

    /**
	 * Build raw byte array of CP56Time2a (7 bytes) for socket transmission
	 * CP56 {msec, min, RES1, invalid, hour, RES2, summer time, date, day, mon, RES3, year, RES4}
	 */
    public static byte[] build_cp56timetag(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        byte[] buffer = new byte[7];
        int tempVal = (cal.get(Calendar.SECOND) * 1000) + cal.get(Calendar.MILLISECOND);
        buffer[0] = (byte) (tempVal & 0x00ff);
        buffer[1] = (byte) ((tempVal & 0xff00) >> 8);
        buffer[2] = (byte) (cal.get(Calendar.MINUTE) & 0x003f);
        buffer[3] = (byte) (cal.get(Calendar.HOUR_OF_DAY) & 0x001f);
        buffer[4] = (byte) (cal.get(Calendar.DAY_OF_MONTH) & 0x001f);
        buffer[5] = (byte) (cal.get(Calendar.MONTH) & 0x000f);
        tempVal = cal.get(Calendar.YEAR) - 2000;
        buffer[6] = (byte) (tempVal & 0x007f);
        return buffer;
    }

    /**
	 * Parse received raw byte array from socket
	 * It will assume the timetag type based on the length of the byte array.
	 */
    public static Date parse(byte[] message) {
        if (message.length < 2) return null;
        int tempVal = 0, msec = 0, sec = 0, min = 0, hour_of_day = 0, day_of_mon = 0, mon = 0, year = 0;
        boolean invalid = false, summer_time = false;
        Calendar cal = Calendar.getInstance();
        tempVal = ((message[1] & 0x00ff) * 256) + (message[0] & 0x00ff);
        sec = tempVal / 1000;
        msec = tempVal % 1000;
        cal.set(Calendar.MILLISECOND, msec);
        cal.set(Calendar.SECOND, sec);
        if (message.length > 2) {
            min = message[2] & 0x003f;
            invalid = ((message[2] & 0x0080) != 0);
            cal.set(Calendar.MINUTE, min);
        }
        if (invalid) return null;
        if (message.length > 3) {
            hour_of_day = message[3] & 0x001f;
            summer_time = ((message[3] & 0x0080) != 0);
            cal.set(Calendar.HOUR_OF_DAY, hour_of_day);
        }
        if (summer_time) {
        }
        if (message.length >= 7) {
            day_of_mon = message[4] & 0x001f;
            mon = message[5] & 0x000f;
            year = (message[6] & 0x007f) + 2000;
            cal.set(Calendar.DAY_OF_MONTH, day_of_mon);
            cal.set(Calendar.MONTH, mon);
            cal.set(Calendar.YEAR, year);
        }
        return cal.getTime();
    }
}
