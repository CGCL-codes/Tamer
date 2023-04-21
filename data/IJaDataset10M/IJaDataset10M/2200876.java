package org.docsfree.legacy.jsp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * A helper class to manage meme attachments.
 Handles Internationalization 

 */
public class Internationalization {

    private String localeSource = "";

    /** ------------------------------------------------------------
	 * 
	 * @author john [ Oct 20, 2009 ]
	 * @return Returns the localeSource.
	 */
    public String getLocaleSource() {
        return localeSource;
    }

    /** ------------------------------------------------------------
	 * 
	 * @author john [ Oct 20, 2009 ]
	 * @param localeSource The localeSource to set.
	 */
    public void setLocaleSource(String localeSource) {
        this.localeSource = localeSource;
    }

    public static void main(String[] args) {
        String locale_name = "jp";
        String locale_source = "C:/eclipse/workspace/Sheetster_2-0/common/system/il8n/";
        getLocaleStrings(locale_name, locale_source);
    }

    /**
	 * load the locale strings from file or database
	 * ------------------------------------------------------------
	 * 
	 * @author john [ Oct 20, 2009 ]
	 * @param locale_name
	 * @return map of locale strings
	 */
    public static Map getLocaleStrings(String locale_name, String locale_src) {
        Map locale_strings = new HashMap();
        String fnz = locale_src + "translation_" + locale_name + ".txt";
        try {
            java.io.File fxa = new java.io.File(fnz);
            List fileTxt = new Vector();
            try {
                BufferedReader d = new BufferedReader(new FileReader(fxa));
                while (d.ready()) fileTxt.add(d.readLine());
                d.close();
            } catch (Exception e) {
                System.out.print("Error in JSPProcessor reading file: " + fnz + ":" + e.toString());
            }
            Map ret = new HashMap();
            Iterator its = fileTxt.iterator();
            while (its.hasNext()) {
                try {
                    String nm = new String(its.next().toString());
                    int pos = nm.indexOf("||");
                    if (pos > -1) {
                        String val = nm.substring(pos + 2);
                        nm = nm.substring(0, pos);
                        locale_strings.put(nm, val);
                    }
                } catch (Exception ex) {
                    ;
                }
            }
        } catch (Exception e) {
        }
        return locale_strings;
    }
}
