package com.rbnb.plugins;

import java.util.Hashtable;
import com.rbnb.plugins.SimplePlugIn;
import com.rbnb.sapi.*;

public class InfiniteDirPlugIn implements SimplePlugIn.PlugInCallback {

    /**
	  * Passes generic options to the plugin handler.
	  */
    public void setOptions(Hashtable options) {
    }

    /**
	  * Handles the request defined by the provided <code>PlugInChannelMap.
	  *  </code>
	  */
    public void processRequest(PlugInChannelMap picm) throws SAPIException {
        String[] chans = picm.GetChannelList();
        boolean registration = "registration".equals(picm.GetRequestReference());
        System.err.println(picm.GetRequestReference());
        picm.Clear();
        picm.PutTimeAuto("timeofday");
        for (int ii = 0; ii < chans.length; ++ii) {
            String cname = chans[ii];
            if (cname.endsWith("*") || cname.endsWith("...")) {
                int lslash = cname.lastIndexOf('/'), index;
                if (lslash != -1) {
                    cname = cname.substring(0, lslash);
                    index = picm.Add(cname + "/x");
                    if (!registration) picm.PutDataAsString(index, cname + "/x"); else picm.PutDataAsByteArray(index, new byte[1]);
                    index = picm.Add(cname + "/T/x");
                    if (!registration) picm.PutDataAsString(index, cname + "/T/x"); else picm.PutDataAsByteArray(index, new byte[1]);
                } else {
                    index = picm.Add("x");
                    if (!registration) picm.PutDataAsString(index, "x"); else picm.PutDataAsByteArray(index, new byte[1]);
                    index = picm.Add("T/x");
                    if (!registration) picm.PutDataAsString(index, "T/x"); else picm.PutDataAsByteArray(index, new byte[1]);
                }
            } else if (cname.endsWith("x")) {
                int index = picm.Add(cname);
                if (!registration) picm.PutDataAsString(index, cname); else {
                    picm.PutDataAsString(index, "<?xml version=\"1.0\" " + "encoding=\"ISO-8859-1\"?>\n<!DOCTYPE rbnb>\n<rbnb>" + "<size>" + cname.length() + "</size>\n</rbnb>\n");
                    picm.PutMime(index, "text/xml");
                }
            } else if (cname.endsWith("T")) {
                int index = picm.Add(cname + "/.");
                picm.PutDataAsByteArray(index, new byte[1]);
            }
        }
        System.err.println(picm);
    }

    /**
	  * Prepares this object for reuse.  Return false if the object should
	  *  be discarded.  The object will also be discarded if this method
	  *  throws a run-time exception.
	  */
    public boolean recycle() {
        return true;
    }

    public static void main(String args[]) {
        System.err.println("Starting again again again!!");
        SimplePlugIn spi = new SimplePlugIn();
        spi.setHandlerClass(InfiniteDirPlugIn.class);
        spi.setPlugInName("InfiniteDirPlugIn");
        spi.run();
    }
}
