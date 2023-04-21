package net.narusas.si.auction.converters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import net.narusas.util.lang.NFile;

/**
 * The Class 건물현황_구조Converter.
 */
public class 건물현황_구조Converter {

    /** The props. */
    private static Properties props;

    static {
        props = new Properties();
        try {
            String text = NFile.getText(new File("cfg/건물현황_구조.cfg"), "euc-kr");
            String[] lines = text.split("\n");
            for (String line : lines) {
                if ("".equals(line.trim())) {
                    continue;
                }
                String[] tokens = line.split("=");
                props.put(tokens[0], tokens[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Convert.
	 * 
	 * @param str the str
	 * 
	 * @return the string
	 */
    public static String convert(String str) {
        if (str == null) {
            return "";
        }
        if (props.containsKey(str)) {
            return props.getProperty(str);
        }
        str = str.replaceAll("및", ",");
        String temp;
        if (str.endsWith("조") && str.endsWith("구조") == false) {
            temp = str.substring(0, str.length() - 1);
            return temp + "구조";
        }
        if (str.endsWith("구조") == false) {
            return str + "구조";
        }
        return str;
    }
}
