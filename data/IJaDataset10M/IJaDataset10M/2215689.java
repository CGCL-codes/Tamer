package cn.poco.util.xmlparse;

import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;

public class FoodNameXmlparse {

    private static ArrayList<String> aList = null;

    public static ArrayList<String> getXml(InputStream inSteam) throws Exception {
        XmlPullParser xmpparse = Xml.newPullParser();
        xmpparse.setInput(inSteam, "UTF-8");
        int code = xmpparse.getEventType();
        while (code != XmlPullParser.END_DOCUMENT) {
            switch(code) {
                case XmlPullParser.START_TAG:
                    if ("dish".equals(xmpparse.getName())) {
                        aList = new ArrayList<String>();
                    }
                    if (aList != null) {
                        if ("item".equals(xmpparse.getName())) {
                            String key = xmpparse.nextText();
                            aList.add(key);
                        }
                    }
                    break;
            }
            code = xmpparse.next();
        }
        inSteam.close();
        return aList;
    }
}
