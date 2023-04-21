package org.onlytime.rssgen;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDbPool;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;
import org.rrd4j.graph.RrdGraph;
import org.rrd4j.graph.RrdGraphConstants;
import org.rrd4j.graph.RrdGraphDef;

public class MetricsCollector extends TimerTask {

    private static final long period = 1000;

    private static final long totalTime = 120000;

    private static MBeanServer server;

    private RrdDbPool pool = RrdDbPool.getInstance();

    public static void main(String[] args) throws Exception {
        ArrayList servers = MBeanServerFactory.findMBeanServer(null);
        server = (MBeanServer) servers.get(0);
        System.out.println(servers);
        ObjectName operationSystem = new ObjectName("java.lang:type=OperatingSystem");
        Long maxMemory = (Long) server.getAttribute(operationSystem, "TotalPhysicalMemorySize");
        RrdDef rrdDef = new RrdDef("./freememory.rrd", period / 1000);
        rrdDef.addDatasource("data", DsType.GAUGE, period / 1000, Double.NaN, maxMemory.doubleValue());
        rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 1, (int) (totalTime / period) + 5);
        RrdDb rrdDb = new RrdDb(rrdDef);
        rrdDb.close();
        String startTime = getCurTime();
        Timer t = new Timer();
        t.schedule(new MetricsCollector(), period, period);
        Thread.sleep(totalTime);
        String endTime = getCurTime();
        RrdGraphDef graphDef = new RrdGraphDef();
        graphDef.setTimeSpan(Long.valueOf(startTime).longValue(), Long.valueOf(endTime).longValue());
        graphDef.datasource("data", "./freememory.rrd", "data", ConsolFun.AVERAGE);
        graphDef.datasource("shading10", "data,0.90,*");
        graphDef.datasource("shading15", "data,0.85,*");
        graphDef.datasource("shading20", "data,0.80,*");
        graphDef.datasource("shading25", "data,0.75,*");
        graphDef.datasource("shading30", "data,0.70,*");
        graphDef.datasource("shading35", "data,0.65,*");
        graphDef.datasource("shading40", "data,0.60,*");
        graphDef.datasource("shading45", "data,0.55,*");
        graphDef.datasource("shading50", "data,0.50,*");
        graphDef.datasource("shading55", "data,0.45,*");
        graphDef.datasource("shading60", "data,0.40,*");
        graphDef.datasource("shading65", "data,0.35,*");
        graphDef.datasource("shading70", "data,0.30,*");
        graphDef.datasource("shading75", "data,0.25,*");
        graphDef.datasource("shading80", "data,0.20,*");
        graphDef.datasource("shading85", "data,0.15,*");
        graphDef.setAntiAliasing(true);
        graphDef.setNoMinorGrid(true);
        graphDef.setImageFormat("png");
        graphDef.setShowSignature(false);
        graphDef.area("data", new Color(0xFF, 0, 0), null);
        graphDef.area("shading10", new Color(0xE1, 0, 0), null);
        graphDef.area("shading15", new Color(0xD2, 0, 0), null);
        graphDef.area("shading20", new Color(0xC3, 0, 0), null);
        graphDef.area("shading25", new Color(0xB4, 0, 0), null);
        graphDef.area("shading30", new Color(0xA5, 0, 0), null);
        graphDef.area("shading35", new Color(0x96, 0, 0), null);
        graphDef.area("shading40", new Color(0x87, 0, 0), null);
        graphDef.area("shading45", new Color(0x78, 0, 0), null);
        graphDef.area("shading50", new Color(0x69, 0, 0), null);
        graphDef.area("shading55", new Color(0x5A, 0, 0), null);
        graphDef.area("shading60", new Color(0x4B, 0, 0), null);
        graphDef.area("shading65", new Color(0x3C, 0, 0), null);
        graphDef.area("shading70", new Color(0x2D, 0, 0), null);
        graphDef.area("shading75", new Color(0x18, 0, 0), null);
        graphDef.area("shading80", new Color(0x0F, 0, 0), null);
        graphDef.area("shading85", new Color(0x00, 0, 0), null);
        graphDef.setColor(RrdGraphConstants.COLOR_SHADEA, new Color(0, 0, 0));
        graphDef.setColor(RrdGraphConstants.COLOR_SHADEB, new Color(0, 0, 0));
        graphDef.setColor(RrdGraphConstants.COLOR_BACK, new Color(0, 0, 0));
        graphDef.setColor(RrdGraphConstants.COLOR_FONT, new Color(0xDD, 0xDD, 0xDD));
        graphDef.setColor(RrdGraphConstants.COLOR_CANVAS, new Color(0x20, 0x20, 0x20));
        graphDef.setColor(RrdGraphConstants.COLOR_GRID, new Color(0x66, 0x66, 0x66));
        graphDef.setColor(RrdGraphConstants.COLOR_MGRID, new Color(0xAA, 0xAA, 0xAA));
        graphDef.setColor(RrdGraphConstants.COLOR_FRAME, new Color(0x20, 0x20, 0x20));
        graphDef.setColor(RrdGraphConstants.COLOR_ARROW, new Color(0xFF, 0xFF, 0xFF));
        graphDef.setFilename("./memory" + System.currentTimeMillis() + ".png");
        RrdGraph graph = new RrdGraph(graphDef);
        BufferedImage bi = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        graph.render(bi.getGraphics());
        rrdDb = new RrdDb("./freememory.rrd");
        System.out.println(rrdDb.dump());
        rrdDb.close();
        t.cancel();
    }

    public void run() {
        try {
            ObjectName operationSystem = new ObjectName("java.lang:type=OperatingSystem");
            Long freePhysicalMemory = (Long) server.getAttribute(operationSystem, "FreePhysicalMemorySize");
            ObjectName memory = new ObjectName("java.lang:type=Memory");
            CompositeData heap = (CompositeData) server.getAttribute(memory, "HeapMemoryUsage");
            System.out.println("used heap: " + heap.get("used"));
            RrdDb rrd = pool.requestRrdDb("./freememory.rrd");
            String curMem = getCurTime();
            Sample sample = rrd.createSample(Long.valueOf(curMem).longValue());
            sample.setValue("data", ((Long) heap.get("used")).doubleValue());
            sample.update();
            pool.release(rrd);
            System.out.println("time: " + curMem);
            System.out.println("mem: " + freePhysicalMemory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getCurTime() {
        String curMem = String.valueOf(System.currentTimeMillis());
        curMem = curMem.substring(0, curMem.length() - 3);
        return curMem;
    }
}
