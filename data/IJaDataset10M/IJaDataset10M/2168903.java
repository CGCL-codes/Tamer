package ru.net.romikk.keepass;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: romikk
 * Date: Mar 22, 2008
 * Time: 5:54:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    public static Date unpackDate(byte[] d) {
        int year = (d[0] << 6) | ((d[1] >> 2) & 0x0000003F);
        int month = ((d[1] & 0x00000003) << 2) | ((d[2] >> 6) & 0x00000003);
        int day = (d[2] << 1) & 0x0000001F;
        int hour = ((d[2] & 0x00000001) << 4) | ((d[3] >> 4) & 0x0000000F);
        int minute = ((d[3] & 0x0000000F) << 2) | ((d[4] >> 6) & 0x00000003);
        int second = d[4] & 0x0000003F;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, second);
        return calendar.getTime();
    }

    public static String hexEncode(byte[] aInput) {
        StringBuffer result = new StringBuffer();
        char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        for (int idx = 0; idx < aInput.length; ++idx) {
            byte b = aInput[idx];
            result.append(digits[(b & 0xf0) >> 4]);
            result.append(digits[b & 0x0f]);
        }
        return result.toString();
    }

    public static byte[] fromHexString(String str) {
        int length = str.length() / 2;
        byte[] toReturn = new byte[length];
        for (int i = 0; i < length; i++) {
            toReturn[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
        }
        return toReturn;
    }
}
