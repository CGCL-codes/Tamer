package analysis;

import org.tc33.jheatchart.HeatChart;
import java.io.*;
import java.lang.String;
import java.awt.Color;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.WritableByteArrayComparable;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.util.Bytes;
import analysis.AnalysisMappings;

public class AvgDrawHeatChart {

    private static Configuration conf = null;

    private static double[][] cpudata = new double[6][14], memdata = new double[6][14];

    private static double[][] power = new double[6][14], temp = new double[6][14], ps = new double[6][4];

    private static double[][] cpucnt = new double[6][14], memcnt = new double[6][14], pscnt = new double[6][4];

    private static int[][] num_active = new int[6][2];

    private static boolean[][] active = new boolean[6][14];

    private static String[] inactive = { "ws282", "ws309", "ws310", "ws311", "ws312", "ws313", "ws314", "ws315", "ws316", "ws317", "ws318", "ws319", "ws320", "ws321", "ws322", "ws336" };

    public static void initDrawHeatChart() {
        int bc, b;
        for (bc = 0; bc < 6; bc++) {
            for (b = 0; b < 14; b++) {
                active[bc][b] = true;
                cpudata[bc][b] = 0;
                memdata[bc][b] = 0;
                power[bc][b] = 0;
                temp[bc][b] = 0;
            }
            num_active[bc][0] = num_active[bc][1] = 0;
        }
        for (String node : inactive) {
            String host = node + ".cercs.int";
            bc = AnalysisMappings.getBladeCenterNumber(host) - 1;
            b = AnalysisMappings.getBladeNumber(host) - 1;
            active[bc][b] = false;
        }
        for (bc = 0; bc < 6; bc++) {
            for (b = 0; b < 6; b++) {
                if (active[bc][b]) {
                    num_active[bc][0]++;
                }
            }
            for (b = 6; b < 14; b++) {
                if (active[bc][b]) {
                    num_active[bc][1]++;
                }
            }
        }
        System.out.println("active = " + active);
    }

    public static void getAllRecords(String tableName, String rackname, long starttime) {
        try {
            HTable table = new HTable(conf, tableName);
            Long TSchosen = new Long(0);
            Integer bcnum = 0, bnum = 0;
            FilterList filterList = new FilterList();
            Scan scan = new Scan();
            String strstart = new Long(starttime).toString();
            filterList.addFilter(new FamilyFilter(CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("powertag"))));
            filterList.addFilter(new RowFilter(CompareOp.GREATER_OR_EQUAL, new BinaryComparator(Bytes.toBytes(strstart))));
            scan.setFilter(filterList);
            ResultScanner ss = table.getScanner(scan);
            String family = "", host = "", val = "";
            try {
                for (Result r : ss) {
                    for (KeyValue kv : r.raw()) {
                        family = new String(kv.getFamily());
                        host = new String(kv.getQualifier());
                        val = new String(kv.getValue());
                        if (val.equals("Not Readable!") || val.equals("null")) {
                            System.out.println("NOT READABLE: family = " + family + " qualifier = " + host + " ts = " + kv.getTimestamp());
                            continue;
                        }
                        System.out.println("TSchosen = " + TSchosen + " kvt=" + kv.getTimestamp() + " st=" + starttime);
                        if ((TSchosen == 0) && (kv.getTimestamp() >= starttime)) {
                            TSchosen = kv.getTimestamp();
                            System.out.println("TS selected = " + TSchosen);
                        }
                        if (family.equals("powertag") && ((kv.getTimestamp() >= TSchosen) || (kv.getTimestamp() < (TSchosen + 61000)))) {
                            int dash1 = host.indexOf('-');
                            if (dash1 != 2) continue;
                            String rack = host.substring(0, dash1);
                            if (rackname.equals(rack)) {
                                System.out.println("kv TS = " + kv.getTimestamp());
                                if (host.contains("CPUTemp1") || host.contains("CPUTemp2")) {
                                    System.out.println("rack matches - temp qual=" + host);
                                    int dash2 = host.indexOf('-', dash1 + 1);
                                    bcnum = Integer.valueOf(host.substring(dash1 + 3, dash2));
                                    int dash3 = host.indexOf('-', dash2 + 1);
                                    bnum = Integer.valueOf(host.substring(dash2 + 6, dash3));
                                    bcnum--;
                                    bnum--;
                                    System.out.println("OUT: host = " + host + "[" + bcnum + "][" + bnum + "]  : temp = " + val);
                                    if (new Double(val) != 0) temp[bcnum][bnum] = new Double(val);
                                } else if (host.contains("OutletPower1") || host.contains("OutletPower8")) {
                                    System.out.println("rack matches - power qual=" + host + ", val = " + val);
                                    if (host.contains("PhaseXY") && host.endsWith("1")) bcnum = 6;
                                    if (host.contains("PhaseXY") && host.endsWith("8")) bcnum = 5;
                                    if (host.contains("PhaseYZ") && host.endsWith("1")) bcnum = 4;
                                    if (host.contains("PhaseYZ") && host.endsWith("8")) bcnum = 3;
                                    if (host.contains("PhaseXZ") && host.endsWith("1")) bcnum = 2;
                                    if (host.contains("PhaseXZ") && host.endsWith("8")) bcnum = 1;
                                    bcnum--;
                                    Double pwr = new Double(val);
                                    if (pwr == 0) continue;
                                    if (host.contains("-PS1_")) {
                                        ps[bcnum][0] += pwr;
                                        pscnt[bcnum][0]++;
                                    } else if (host.contains("-PS2_")) {
                                        ps[bcnum][1] += pwr;
                                        pscnt[bcnum][1]++;
                                    } else if (host.contains("-PS3_")) {
                                        ps[bcnum][2] += pwr;
                                        pscnt[bcnum][2]++;
                                    } else if (host.contains("-PS4_")) {
                                        ps[bcnum][3] += pwr;
                                        pscnt[bcnum][3]++;
                                    }
                                }
                            }
                        }
                    }
                }
                for (int bc = 0; bc < 6; bc++) {
                    for (int p = 0; p < 4; p++) {
                        System.out.println("BC" + (bc + 1) + " PS" + (p + 1) + " : power = " + ps[bc][p]);
                    }
                    Double pwr = new Double(0);
                    for (int i = 0; i < 6; i++) {
                        num_active[i][0] = 6;
                        num_active[i][1] = 8;
                    }
                    if (num_active[bc][0] != 0) {
                        double avg = (ps[bc][0] / pscnt[bc][0]) + (ps[bc][1] / pscnt[bc][1]);
                        pwr = (avg - 242) / num_active[bc][0];
                        for (bnum = 0; bnum < 6; bnum++) {
                            power[bc][bnum] = pwr;
                        }
                    }
                    if (num_active[bc][1] != 0) {
                        double avg = (ps[bc][0] / pscnt[bc][0]) + (ps[bc][1] / pscnt[bc][1]);
                        pwr = avg / num_active[bc][1];
                        for (bnum = 6; bnum < 14; bnum++) {
                            power[bc][bnum] = pwr;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ss.close();
            }
            TSchosen = TSchosen / 1000;
            String start = TSchosen.toString();
            String end = (new Long(TSchosen + 60)).toString();
            System.out.println("VC: TSchosen=" + TSchosen + " - start = " + start + "  end = " + end);
            filterList = new FilterList();
            filterList.addFilter(new FamilyFilter(CompareOp.NOT_EQUAL, new BinaryComparator(Bytes.toBytes("powertag"))));
            filterList.addFilter(new RowFilter(CompareOp.GREATER_OR_EQUAL, new BinaryComparator(Bytes.toBytes(start))));
            filterList.addFilter(new RowFilter(CompareOp.LESS, new BinaryComparator(Bytes.toBytes(end))));
            scan.setFilter(filterList);
            ss = table.getScanner(scan);
            try {
                for (Result r : ss) {
                    for (KeyValue kv : r.raw()) {
                        family = new String(kv.getFamily());
                        host = new String(kv.getQualifier());
                        val = new String(kv.getValue());
                        if (family.equals("vcmachine")) {
                            bcnum = AnalysisMappings.getBladeCenterNumber(host) - 1;
                            bnum = AnalysisMappings.getBladeNumber(host) - 1;
                            String splits[] = val.split(", ");
                            Integer cpu = Integer.parseInt(splits[0]);
                            Integer mem = Integer.parseInt(splits[1]);
                            System.out.println("OUT: host = " + host + "[" + bcnum + "][" + bnum + "]  : " + "cpu = " + cpu + " mem = " + mem + " ts = " + kv.getTimestamp());
                            if (cpu != 0) {
                                cpudata[bcnum][bnum] += cpu;
                                cpucnt[bcnum][bnum]++;
                            }
                            if (mem != 0) {
                                memdata[bcnum][bnum] += mem;
                                memcnt[bcnum][bnum]++;
                            }
                        }
                        if (family.equals("guest")) {
                            System.out.println("OUT: guest = " + host + " usage = " + val + " ts = " + kv.getTimestamp());
                        }
                        if (family.equals("tothostusage")) {
                            System.out.println("OUT: tothostusage = " + val + " at row = " + Bytes.toString(kv.getRow()) + " ts = " + kv.getTimestamp());
                        }
                        if (family.equals("totguestusage")) {
                            System.out.println("OUT: totguestusage = " + val + " at row = " + Bytes.toString(kv.getRow()) + " ts = " + kv.getTimestamp());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ss.close();
            }
            for (bcnum = 0; bcnum < 6; bcnum++) {
                for (bnum = 0; bnum < 14; bnum++) {
                    cpudata[bcnum][bnum] /= cpucnt[bcnum][bnum];
                    memdata[bcnum][bnum] /= memcnt[bcnum][bnum];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printChartData() {
        double totcpu, totmem, totpower, tottemp;
        System.out.println("\n## BC, Blade:	CPU, Memory, Power, Temperature ##");
        System.out.println("##");
        for (int bc = 0; bc < 6; bc++) {
            System.out.println("## BC" + (bc + 1) + ":");
            totcpu = totmem = totpower = tottemp = 0;
            for (int b = 0; b < 14; b++) {
                System.out.println("## Blade" + (b + 1) + " : " + cpudata[bc][b] + ", " + memdata[bc][b] + ", " + power[bc][b] + ", " + temp[bc][b]);
                totcpu += cpudata[bc][b];
                totmem += memdata[bc][b];
                totpower += power[bc][b];
                tottemp += temp[bc][b];
            }
            System.out.println("## Total CPU = " + totcpu + " MHz" + " (Average = " + totcpu / 14.0 + ")");
            System.out.println("## Total Memory = " + totmem + " MB" + " (Average = " + totmem / 14.0 + ")");
            System.out.println("## Total Power = " + totpower + " (Average = " + totpower / 14.0 + ")");
            System.out.println("## Average Temperature = " + tottemp / 14.0);
            System.out.println("##");
        }
    }

    public static void main(String args[]) {
        try {
            conf = HBaseConfiguration.create();
            HBaseAdmin admin = new HBaseAdmin(conf);
            if (admin.tableExists("metrics")) {
                System.out.println("metrics table exists!");
            } else {
                System.out.println("metrics table does not exist!");
                System.exit(1);
            }
            initDrawHeatChart();
            String bnames[] = new String[14];
            for (int i = 0; i < bnames.length; i++) {
                bnames[i] = "Blade" + (i + 1);
            }
            String bcnames[] = new String[6];
            for (int i = 0; i < bcnames.length; i++) {
                bcnames[i] = "BC" + (i + 1);
            }
            double[][] dummy = new double[][] { { 0 } };
            HeatChart chart = new HeatChart(dummy);
            chart.setXAxisLabel("Blades");
            chart.setYAxisLabel("Blade centers");
            chart.setXValues(bnames);
            chart.setYValues(bcnames);
            chart.setLowValueColour(new Color(220, 220, 220));
            String rackname = new String(args[0]);
            long start = new Long(args[1]) * 1000;
            long interval = new Long(args[2]) * 1000;
            for (int i = 0; i < Integer.parseInt(args[3]); i++) {
                getAllRecords("metrics", rackname, start);
                printChartData();
                chart.setZValues(cpudata);
                chart.setTitle("CPU usage map");
                chart.saveToFile(new File("analysis/" + start + "-cpu-chart.png"));
                chart.setZValues(memdata);
                chart.setTitle("Memory usage map");
                chart.saveToFile(new File("analysis/" + start + "-mem-chart.png"));
                chart.setZValues(power);
                chart.setTitle("Power usage map");
                chart.saveToFile(new File("analysis/" + start + "-power-chart.png"));
                chart.setZValues(temp);
                chart.setTitle("Thermal map");
                chart.saveToFile(new File("analysis/" + start + "-temp-chart.png"));
                System.out.println("Done plotting charts. Waiting for " + interval + " msec...");
                Thread.sleep(5);
                start += interval;
            }
        } catch (Exception e) {
            System.out.println("Exception!!");
            e.printStackTrace();
        }
        return;
    }
}
