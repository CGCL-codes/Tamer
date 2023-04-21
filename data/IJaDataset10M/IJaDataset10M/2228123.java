package org.iisc.mile.indickeyboards.windows;

import org.iisc.mile.indickeyboards.ParseXML;
import org.iisc.mile.indickeyboards.PhoneticParseXML;

public class OutputCharToActiveWindow {

    /**
	 * Native method opChars is used to output the Unicodes to the current
	 * active window.
	 * 
	 * @param opchar
	 *            the variable which holds the Unicode that is required to be
	 *            outputted onto the active widnow
	 */
    public native void opChars(int opchar);

    static OutputCharToActiveWindow ob = new OutputCharToActiveWindow();

    static {
        try {
            System.loadLibrary("indic-keyboards-opChars");
        } catch (UnsatisfiedLinkError e) {
        }
    }

    public static void getcharforop_nonPhonetic(String ucodeValue) {
        StringBuilder temp = new StringBuilder(4);
        int i;
        if (ucodeValue.length() > 4) {
            for (int j = 0; j < ucodeValue.length(); j++) {
                temp.append(ucodeValue.charAt(j));
                if (temp.length() == 4) {
                    System.out.println(temp.toString());
                    i = Integer.valueOf(temp.toString(), 16).intValue();
                    ob.opChars(i);
                    temp.delete(0, 4);
                }
            }
        } else {
            if (ParseXML.keyboardlayoutname.compareTo("tamil99.xml") == 0 && ParseXML.pattern.compareTo("a") == 0 && ParseXML.previousConsonantFlag != 0) {
            } else {
                i = Integer.valueOf(ucodeValue, 16).intValue();
                ob.opChars(i);
            }
        }
    }

    public static void getcharforop_phonetic(String ucodeValue) {
        StringBuilder temp = new StringBuilder(4);
        int i;
        if (ucodeValue.length() > 4) {
            for (int j = 0; j < ucodeValue.length(); j++) {
                temp.append(ucodeValue.charAt(j));
                if (temp.length() == 4) {
                    System.out.println(temp.toString());
                    i = Integer.valueOf(temp.toString(), 16).intValue();
                    ob.opChars(i);
                    temp.delete(0, 4);
                }
            }
        } else {
            if (PhoneticParseXML.aflag == 0) {
                i = Integer.valueOf(ucodeValue, 16).intValue();
                ob.opChars(i);
            } else PhoneticParseXML.aflag = 0;
        }
    }
}
