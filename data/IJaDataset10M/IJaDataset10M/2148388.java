package org.fao.geonet.lib.wms.dimensions.time;

import java.util.*;
import org.jdom.*;

public class TimeExtent {

    private Vector dates = new Vector();

    public static void main(String args[]) {
        TimeExtent te = new TimeExtent(new Element("asdfasdf"));
        te.getIntervalDates("2003-01-01/2003-12-01/P1M");
    }

    public TimeExtent(Element extent) {
        this(extent.getText());
    }

    public TimeExtent(String value) {
        StringTokenizer s = new StringTokenizer(value, ",");
        while (s.hasMoreTokens()) {
            String token = s.nextToken();
            if (token.indexOf("/") == -1) {
                dates.add(token);
            } else getIntervalDates(token);
        }
    }

    /**
	 * Returns a Jdom Element containig all the dates
	 *
	 * @return   an Element
	 *
	 */
    public Element getJdom() {
        Element elDates = new Element("extent").setAttribute("name", "time");
        for (Iterator i = dates.iterator(); i.hasNext(); ) {
            String date = (String) i.next();
            elDates.addContent(new Element("value").setText(date));
        }
        return elDates;
    }

    /**
	 * Returns all the dates contained in the given interval
	 *
	 * @param    interval            a  String
	 *
	 */
    private void getIntervalDates(String interval) {
        System.out.println("interval: " + interval);
        StringTokenizer st = new StringTokenizer(interval, "/");
        WmsTime start = new WmsTime(st.nextToken());
        WmsTime stop = new WmsTime(st.nextToken());
        start.setPeriod(st.nextToken());
        getDates(start, stop);
    }

    private void getDates(WmsTime beginDate, WmsTime endDate) {
        for (; !beginDate.after(endDate); beginDate.increment()) {
            dates.add(beginDate.toString());
        }
    }
}
