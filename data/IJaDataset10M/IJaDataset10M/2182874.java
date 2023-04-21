package monstersim;

import java.lang.Process;
import java.util.Vector;
import java.lang.Runtime;
import java.io.File;
import java.io.*;
import java.util.Date;

public class MonsterSim {

    private static int currTasks;

    static xyStruct[] sizes;

    static String[] trafficPatterns = { "linear", "lin-fi", "lin-fo", "diamond", "fi", "fo", "tree-fi-deg2", "tree-fo-deg2", "tree-fi-deg3", "tree-fo-deg3", "mesh", "stree-fi-deg2-rows", "stree-fo-deg2-rows" };

    static String[] policies = { "Odd-even", "Pred-Agg-Feedback", "Pred-Feedback" };

    static int totalSims = 0;

    public static String[] args;

    static String nocsimpath;

    static String savePath;

    static String currTraffic;

    public static void main(String[] args) {
        initXYStruct();
        nocsimpath = args[2];
        savePath = args[3];
        PrintStream data = null;
        try {
            data = new PrintStream(new File(savePath + "DATA.csv"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        MonsterSim.args = args;
        boolean first = true;
        for (int i = Integer.parseInt(args[4]); i < sizes.length; i++) {
            for (int j = Integer.parseInt(args[0]); j <= Integer.parseInt(args[1]); j++) {
                if (i == Integer.parseInt(args[4]) && first) {
                    j = Integer.parseInt(args[5]);
                    first = false;
                }
                String template = makeTemplateFile(i, j);
                Vector oddEvenSims25 = new Vector();
                Vector oddEvenSims500 = new Vector();
                int currPeriod;
                for (currPeriod = 500; currPeriod <= 10000; currPeriod += 200) {
                    NocSimStat oddEven = runNocSim(currPeriod, template, i, 0, 25);
                    oddEvenSims25.add(oddEven);
                    NocSimStat paf = runNocSim(currPeriod, template, i, 1, 25);
                    System.out.println("Ratio: " + paf.avgExecTime / oddEven.avgExecTime);
                    System.out.println("");
                    if (paf.avgExecTime / oddEven.avgExecTime >= 0.95) {
                        break;
                    }
                }
                int endPeriod = currPeriod;
                int startPeriod = currPeriod - 450;
                data.println("Size\tTraffic\tPeriod\tTasks\tOEExec\tOEdec\tPAFexec\tPAFdec");
                for (int k = startPeriod; k <= endPeriod; k += 50) {
                    NocSimStat oddEven = runNocSim(k, template, i, 0, 500);
                    oddEvenSims500.add(oddEven);
                    NocSimStat paf = runNocSim(k, template, i, 1, 500);
                    data.println(sizes[i].x + "x" + sizes[i].y + "\t" + currTraffic + "\t" + k + "\t" + currTasks + "\t" + oddEven.avgExecTime + "\t" + oddEven.avgNumChoices + "\t" + paf.avgExecTime + "\t" + paf.avgNumChoices);
                }
                data.println("");
                for (currPeriod = 500; currPeriod <= 10000; currPeriod += 200) {
                    NocSimStat oddEven = searchForPeriod(currPeriod, oddEvenSims25);
                    if (oddEven == null) {
                        oddEven = runNocSim(currPeriod, template, i, 0, 25);
                        oddEvenSims25.add(oddEven);
                    }
                    NocSimStat pf = runNocSim(currPeriod, template, i, 2, 25);
                    if (pf.avgExecTime / oddEven.avgExecTime >= 0.95) {
                        break;
                    }
                }
                endPeriod = currPeriod;
                startPeriod = currPeriod - 450;
                data.println("Size\tTraffic\tPeriod\tTasks\tOEExec\tOEdec\tPFexec\tPFdec");
                for (int k = startPeriod; k <= endPeriod; k += 50) {
                    NocSimStat oddEven = searchForPeriod(k, oddEvenSims500);
                    if (oddEven == null) {
                        oddEven = runNocSim(k, template, i, 0, 500);
                        oddEvenSims500.add(oddEven);
                    }
                    NocSimStat pf = runNocSim(k, template, i, 2, 500);
                    data.println(sizes[i].x + "x" + sizes[i].y + "\t" + currTraffic + "\t" + k + "\t" + currTasks + "\t" + oddEven.avgExecTime + "\t" + oddEven.avgNumChoices + "\t" + pf.avgExecTime + "\t" + pf.avgNumChoices);
                }
                data.println("");
                data.println("");
            }
        }
        data.close();
        System.out.println("WE'RE DONE!");
    }

    private static NocSimStat searchForPeriod(int period, Vector sims) {
        for (int i = 0; i < sims.size(); i++) {
            if (((NocSimStat) (sims.elementAt(i))).period == period) return (NocSimStat) sims.elementAt(i);
        }
        return null;
    }

    private static NocSimStat runNocSim(int period, String template, int sizeSub, int policySub, int gcc) {
        totalSims++;
        Date date = new Date();
        System.out.println("Running NocSim simulation #" + totalSims + " gcc " + gcc + " period " + period + " policy " + policies[policySub] + " on " + date);
        String cmdLine = "java NocSim -buildgt " + savePath + " -tg 4 -delay 2000 -size 20 -period " + period + " " + template;
        String buildgtSaveFile = savePath + "_task4_period" + period + "_delay2000_pl20.gtb";
        System.out.println(cmdLine);
        try {
            Process p = Runtime.getRuntime().exec(cmdLine, null, new File(args[2]));
            redirectOutput(p);
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cmdLine = "java NocSim -buildr " + savePath + " -sizeX " + sizes[sizeSub].x + " -sizeY " + sizes[sizeSub].y + " -policy " + policies[policySub];
        System.out.println(cmdLine);
        String buildrouterSaveFile = savePath + "_Mesh2D_Bidirectional_" + sizes[sizeSub].x + "x" + sizes[sizeSub].y + "_" + policies[policySub].toLowerCase() + ".rb";
        try {
            Process p = Runtime.getRuntime().exec(cmdLine, null, new File(args[2]));
            redirectOutput(p);
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cmdLine = "java NocSim -link " + savePath + " " + buildgtSaveFile + " " + buildrouterSaveFile + " -mapping random -seed 220";
        String gtsub = buildgtSaveFile.substring(buildgtSaveFile.indexOf("_"), buildgtSaveFile.indexOf("."));
        String rtsub = buildrouterSaveFile.substring(buildrouterSaveFile.indexOf("_"), buildrouterSaveFile.indexOf("."));
        String linkSave = savePath + gtsub + rtsub + ".lef";
        System.out.println(cmdLine);
        try {
            Process p = Runtime.getRuntime().exec(cmdLine, null, new File(args[2]));
            redirectOutput(p);
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cmdLine = "java -Xmx256m NocSim -exec " + linkSave + " -sgcc " + gcc + " -solgc -restartp -oi -log " + savePath + " -gcl test.csv";
        System.out.println(cmdLine);
        try {
            Process p = Runtime.getRuntime().exec(cmdLine, null, new File(args[2]));
            redirectOutput(p);
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cmdLine = "grep ^\\\"Avg\\\" test.csv";
        String line2 = null;
        String line = null;
        try {
            Process p = Runtime.getRuntime().exec(cmdLine, null, new File(savePath));
            try {
                InputStream stdin = p.getInputStream();
                InputStreamReader isr = new InputStreamReader(stdin);
                BufferedReader br = new BufferedReader(isr);
                while ((line = br.readLine()) != null) {
                    line2 = line;
                }
                br.close();
                isr.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        line = line2;
        int start = line.lastIndexOf(",") + 2;
        int end = line.lastIndexOf("\"");
        int avg = Integer.parseInt(line.substring(start, end));
        System.out.println("Average execution time per graph: " + avg);
        cmdLine = "cat policystat.dat";
        line2 = null;
        line = null;
        try {
            Process p = Runtime.getRuntime().exec(cmdLine, null, new File(savePath));
            try {
                InputStream stdin = p.getInputStream();
                InputStreamReader isr = new InputStreamReader(stdin);
                BufferedReader br = new BufferedReader(isr);
                while ((line = br.readLine()) != null) {
                    line2 = line;
                }
                br.close();
                isr.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        line = line2;
        start = 0;
        end = line.lastIndexOf("!");
        double decavg = Double.parseDouble(line.substring(start, end));
        System.out.println("Average number of choices per routing cycle: " + ((double) Math.round(decavg * 10000)) / 10000);
        File dir = new File(savePath);
        System.out.println("");
        NocSimStat returnVal = new NocSimStat();
        returnVal.avgExecTime = (double) avg;
        returnVal.avgNumChoices = ((double) Math.round(decavg * 10000)) / 10000;
        returnVal.period = period;
        return returnVal;
    }

    private static String makeTemplateFile(int i, int j) {
        String traffic = trafficPatterns[j];
        int numtasks = sizes[i].x * sizes[i].y / 4;
        if (j == 12 || j == 11) {
            int numRows = (int) (Math.round(solveQuadratic(0.5, 0.5, -1 * (double) (numtasks), true)));
            traffic += "" + numRows;
            numtasks = numRows * (numRows + 1) / 2;
        }
        if (j == 6 || j == 7) {
            numtasks--;
        }
        if (j == 8 || j == 9) {
            int exponent = (int) (Math.round(Math.log(2 * numtasks + 1) / Math.log(3)));
            numtasks = (int) ((Math.pow(3, exponent) - 1) / 2);
        }
        if (j == 10) {
            traffic += "" + sizes[i].x / 2 + "x" + sizes[i].y / 2;
        }
        currTraffic = traffic;
        currTasks = numtasks;
        String cmdLine = "java util.templateGen.TemplateGen -g 4 -t " + numtasks + " -tm " + traffic + " -sp " + args[3] + "monster";
        String filename = args[3] + "monster" + "_tg4_task" + numtasks + "_" + traffic + ".tpl";
        System.out.println("Creating " + filename);
        try {
            Process p = Runtime.getRuntime().exec(cmdLine, null, new File(args[2]));
            redirectOutput(p);
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
    }

    private static int redirectOutput(Process p) {
        int exitVal = -1;
        try {
            InputStream stdin = p.getInputStream();
            InputStream stderr = p.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            InputStreamReader esr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            BufferedReader br2 = new BufferedReader(esr);
            String line = null;
            String line2 = null;
            while ((line = br.readLine()) != null || ((line2 = br2.readLine()) != null)) {
                if (line != null) System.out.println(line);
                if (line2 != null) {
                    System.err.println(line2);
                    System.exit(1);
                }
            }
            exitVal = p.waitFor();
            br.close();
            br2.close();
            isr.close();
            esr.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return exitVal;
    }

    private static double solveQuadratic(double a, double b, double c, boolean greatest_solution) {
        double multiplier = -1;
        if (greatest_solution) {
            multiplier = 1;
        }
        return (-1 * b / (2 * a) + multiplier * Math.sqrt(b * b - 4 * a * c) / (2 * a));
    }

    private static void initXYStruct() {
        sizes = new xyStruct[7];
        sizes[0] = new xyStruct(4, 4);
        sizes[1] = new xyStruct(4, 8);
        sizes[2] = new xyStruct(8, 8);
        sizes[3] = new xyStruct(8, 16);
        sizes[4] = new xyStruct(16, 16);
        sizes[5] = new xyStruct(16, 32);
        sizes[6] = new xyStruct(32, 32);
    }
}
