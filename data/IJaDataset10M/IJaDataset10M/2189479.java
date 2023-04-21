package jmxm.gui.monitor;

import java.util.Date;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;

public class XTimeSeries extends TimeSeries {

    public XTimeSeries(String name) {
        super(name, Second.class);
    }

    public void addNow(Integer temp) {
        addOrUpdate(new Second(new Date()), temp);
    }

    public void addNow(Long temp) {
        addOrUpdate(new Second(new Date()), temp);
    }

    public void addNow(Double temp) {
        addOrUpdate(new Second(new Date()), temp);
    }

    public void addNow(String temp) {
        try {
            addNow(Long.parseLong(temp));
        } catch (NumberFormatException e) {
            try {
                addNow(Double.parseDouble(temp));
            } catch (NumberFormatException e2) {
                System.err.println("Unable to parse String in XTimeSeries.addNow(" + temp + ")");
            }
        }
    }
}
