package edu.ucsd.rbnb.esper.executable;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.rbnb.sapi.SAPIException;
import edu.ucsd.rbnb.esper.Monitor;
import edu.ucsd.rbnb.esper.event.Measurement;
import edu.ucsd.rbnb.esper.event.Metadata;
import edu.ucsd.rbnb.esper.io.GenericDataSource;
import edu.ucsd.rbnb.esper.io.RBNBsink;

public class EsperTest_FromRBNB {

    private static Monitor monitor;

    private static GenericDataSource src;

    public static void main(String[] args) throws Exception {
        monitor = new Monitor();
        src = new GenericDataSource("ESPER");
        splitStreams();
        monitorBleaching();
        loadSampleData();
    }

    private static void splitStreams() {
        monitor.query("insert into Temperature select * " + "from Measurement where meta.type = 'temp' ");
        monitor.query("insert into Solar select * " + "from Measurement where meta.type = 'solar' ");
    }

    private static void monitorBleaching() throws SAPIException {
        src.addChannel("avgSolar");
        src.addChannel("avgTemp");
        monitor.query("insert into AverageSolar " + "select avg(value) as value, meta " + "from	Solar.win:time(5 hours)");
        monitor.query("insert into AverageTemp " + "select avg(value) as value, meta " + "from	Temperature.win:time(5 hours)");
        monitor.query("insert into LongTermAverageTemp " + "select avg(value) as value, meta " + "from	Temperature.win:time(20 days)");
        monitor.query("select rT.value as temp, " + "rT.value as longTemp, " + "s.value as solar, " + "rT.meta as tempMeta, " + "s.meta as solarMeta " + "from AverageSolar.std:lastevent() as s, " + "AverageTemp.std:lastevent() as rT," + "LongTermAverageTemp.std:lastevent() lT " + "where rT.value>lT.value and s.value > 600").addListener(new UpdateListener() {

            public void update(EventBean[] newEvents, EventBean[] oldEvents) {
                EventBean event = newEvents[0];
                printBleeching(event);
            }
        });
    }

    static Metadata M_TEMP = new Metadata("iguassu.sdsc.edu:3333", "Thailand/HOBO/kohracha/temp", "HOBO", "#1172881", "temp");

    static Metadata M_SOLAR = new Metadata("iguassu.sdsc.edu:3333", "Thailand/HOBO/kohracha/solar", "HOBO", "#1172881", "solar");

    static Metadata[] meta = { M_TEMP, M_SOLAR };

    static void loadSampleData() throws IOException, ParseException, SAPIException {
        RBNBsink sink = new RBNBsink(meta);
        while (true) {
            Measurement[] pts = sink.getData();
            if (pts == null) break;
            long timestamp = pts[0].getTimestamp() * 1000;
            monitor.setTime(timestamp);
            System.out.print("[Added] ");
            System.out.print(time(timestamp) + " ");
            System.out.print("Temp: " + pts[1].getValue() + "\t");
            System.out.print("Solar: " + pts[0].getValue() + "\t");
            System.out.println();
            monitor.loadData(new Measurement(pts[1].getValue(), M_TEMP));
            monitor.loadData(new Measurement(pts[0].getValue(), M_SOLAR));
        }
    }

    private static String time(long timestamp) {
        return (new SimpleDateFormat("MM/dd/yyyy HH:mm")).format(timestamp);
    }

    private static void printBleeching(EventBean event) {
        System.out.println("!!! Bleaching EVENT !!!");
        System.out.format("#  avg.temp (rec>long): %.3f > %.3f", event.get("temp"), event.get("longTemp"));
        System.out.println();
        System.out.print("#  |-from  ");
        System.out.print(event.get("tempMeta.server"));
        System.out.print(event.get("tempMeta.source"));
        System.out.print("  " + event.get("tempMeta.device"));
        System.out.print(" " + event.get("tempMeta.model"));
        System.out.println();
        System.out.format("#  avg.solar %.3f", event.get("solar"));
        System.out.println();
        System.out.print("#  |-from  ");
        System.out.print(event.get("solarMeta.server"));
        System.out.print(event.get("solarMeta.source"));
        System.out.print("  " + event.get("solarMeta.device"));
        System.out.print(" " + event.get("solarMeta.model"));
        System.out.println();
    }
}
