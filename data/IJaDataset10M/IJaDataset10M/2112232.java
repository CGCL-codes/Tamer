package org.jxstar.control;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 
 *
 * @author TonyTan
 * @version 1.0, 2011-3-4
 */
public class CharacterFilterTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        CharacterFilterTest test = new CharacterFilterTest();
        String value = test.expireVal(30);
        System.out.println("=========value=" + value);
        String uri = "uri=/jxstar/public/ext/resources/css/ext-all.css";
        String cachetype = "1j1s,css,jpg,png,gif,ico";
        System.out.println("=========uri.indexOf=" + uri.indexOf(".js"));
        uri = "d:\\aa_cojsde\\abc.jpg";
        System.out.println("=========uri.indexOf=" + uri.indexOf(".js"));
        boolean bret = test.isCacheType(uri, cachetype);
        System.out.println("=========bret=" + bret);
    }

    /**
	 * 取一个月后的时间值
	 * @return
	 */
    private String expireVal(int day) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:ss 'GMT'", Locale.US);
        return sdf.format(cal.getTime());
    }

    private boolean isCacheType(String uri, String cachetype) {
        if (uri == null || uri.length() == 0 || cachetype == null || cachetype.length() == 0) {
            return false;
        }
        String[] fs = uri.split("\\.");
        if (fs.length < 2) return false;
        String ext = fs[1].toLowerCase();
        String[] ct = cachetype.toLowerCase().split(",");
        for (int i = 0; i < ct.length; i++) {
            if (ct[i].equals(ext)) return true;
        }
        return false;
    }
}
