package mfb2.tools.obclipse.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @version $Id: $
 */
public class GenerateObfuscationDictionary {

    static String reserved_names[] = { "con", "prn", "nul", "aux", "lpt1", "lpt2", "lpt3", "lpt4", "com1", "com2", "com3", "com4" };

    /**
   * @param args
   */
    public static void main(String[] args) {
        GenerateObfuscationDictionary generateObfuscationDictionary = new GenerateObfuscationDictionary();
        generateObfuscationDictionary.generate();
    }

    public void generate() {
        Set<String> reserved = new HashSet<String>(Arrays.asList(reserved_names));
        ArrayList<String> names = new ArrayList<String>();
        for (char x = 97; x <= 122; x++) {
            names.add(String.valueOf(x));
        }
        ArrayList<String> tempList = new ArrayList<String>(names);
        for (int i = 0; i < 3; i++) {
            ArrayList<String> tempList2 = new ArrayList<String>();
            for (String name : tempList) {
                for (char x = 97; x <= 122; x++) {
                    String nextName = name + String.valueOf(x);
                    tempList2.add(nextName);
                    names.add(nextName);
                }
            }
            tempList = tempList2;
        }
        System.out.println(names);
        try {
            FileWriter fileWriter = new FileWriter(new File("dict.txt"));
            for (String name : names) {
                if (!reserved.contains(name)) {
                    fileWriter.write(name + "\n");
                } else {
                    System.out.println(name);
                }
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("finished!");
    }
}
