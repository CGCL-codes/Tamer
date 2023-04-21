package signitserver.application.utils;

import java.io.IOException;
import java.util.LinkedList;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Sachin Sudheendra
 */
public class Formatter {

    public static String listToString(LinkedList list, char delimiter) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            builder.append(delimiter);
        }
        return builder.toString();
    }

    public static LinkedList<Double> stringToList(String listAsString, char delimiter) {
        LinkedList<Double> list = new LinkedList<Double>();
        String[] split = listAsString.split(delimiter + "");
        for (int i = 0; i < split.length; i++) {
            list.add(Double.parseDouble(split[i]));
        }
        return list;
    }

    public static byte[] stringToByteArray(String str) throws IOException {
        return new BASE64Decoder().decodeBuffer(str);
    }

    public static String byteArrayToString(byte[] byteArray) {
        return new BASE64Encoder().encode(byteArray);
    }
}
