package org.xmlcml.cml.tools;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class TestString {

    /**
	 * @param args
	 * @throws IOException 
	 */
    public static void main(String[] args) throws IOException {
        byte[] parsable = FileUtils.readFileToByteArray(new File("C:\\temp\\encodingissues\\parsable"));
        String parsableS = new String(parsable);
        byte[] unparsable = FileUtils.readFileToByteArray(new File("C:\\temp\\encodingissues\\unparsable"));
        String unparsableS = new String(unparsable);
        int diff = unparsableS.compareTo(parsableS);
        System.out.println(diff + "\n" + unparsableS.substring(0, diff + 2) + "\n" + parsableS.substring(0, diff + 2));
        System.out.println(diff + "/" + unparsableS.charAt(diff) + "/" + parsableS.charAt(diff));
    }
}
