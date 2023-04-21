package dscript.intl;

import dscript.preprocess.LineSplit;
import dscript.Ask;
import dscript.DApplet;
import java.io.*;
import java.util.HashMap;

public class LanguageConverter {

    private static HashMap lhash = new HashMap();

    public static String FS = "";

    public static String LS = "";

    static {
        if (!DApplet.using_as_applet) {
            FS = System.getProperty("file.separator");
            LS = System.getProperty("line.separator");
        } else {
            FS = "/";
            LS = "\n";
        }
    }

    public LanguageConverter() {
    }

    public String convert_to_english(String src) {
        return src;
    }

    public String convert_from_english(String src) {
        return src;
    }

    public static String convertToEnglish(String src) {
        if (src.indexOf("/*LANG:") < 0) {
            return src;
        }
        int i = src.indexOf("/*LANG:") + 7;
        String lang = src.substring(i, src.indexOf("*/", i));
        try {
            StringBuffer sb = new StringBuffer(100);
            BufferedReader br = new BufferedReader(new FileReader(new File("languages.txt")));
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s).append(LS);
            }
            br.close();
            LineSplit ls = new LineSplit();
            String dat = ls.ssplit(lang, sb.toString());
            if (dat.equals("")) {
                return src;
            }
            String clss = ls.ssplit("CLASS", dat);
            Class c = null;
            if (lhash.containsKey(clss)) {
                c = (Class) lhash.get(clss);
            } else {
                c = Class.forName(clss);
                lhash.put(clss, c);
            }
            Object o = c.newInstance();
            LanguageConverter lc = (LanguageConverter) o;
            return lc.convert_to_english(src);
        } catch (Exception e) {
        }
        return src;
    }

    public static void main(String[] args) {
        System.out.println("Please enter the name of the language you are registering:");
        String lname = Ask.ask();
        StringBuffer langdata = new StringBuffer(100);
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("languages.txt")));
            String s;
            while ((s = br.readLine()) != null) {
                langdata.append(s).append(LS);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Failed at reading languages file");
            System.exit(0);
        }
        if (langdata.toString().indexOf("<" + lname + ">") > -1) {
            System.out.println("The language '" + lname + "' is already registered");
            System.exit(0);
        }
        System.out.println("What is the fully qualified classname (ie. mypackage.mylanguageclass)?:");
        String clname = Ask.ask();
        System.out.println("Testing..");
        try {
            LanguageConverter lc = (LanguageConverter) (Class.forName(clname).newInstance());
        } catch (Exception ez) {
            System.out.println("This language class seems to be corrupted, or did not subclass dscript.intl.LanguageConverter\n");
            System.exit(0);
        }
        langdata.append('<').append(lname).append('>').append("<CLASS>").append(clname).append("</CLASS><").append('/').append(lname).append('>').append(LS);
        try {
            FileWriter fw = new FileWriter(new File("languages.txt"));
            fw.write(langdata.toString());
            fw.close();
            System.out.println("succeeded at writing file.");
            System.exit(0);
        } catch (Exception ez) {
        }
        System.out.println("Failed at writing file...");
    }
}
