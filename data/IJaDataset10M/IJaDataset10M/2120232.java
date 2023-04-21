package resultviewer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class GenerateBat {

    private static int port = 4445;

    private static long period = 30;

    private static int fontSize = 20;

    private static boolean antiAliasing = true;

    private static String className = "";

    private static String classTitle = "";

    private static int eventRaceId = 1;

    private static int timeWindowDelay = 200;

    private static String url = "resultviewer/xml/files/SOFTRES.XML";

    private static String newBackgroundColor = "255,255,0";

    private static boolean useMainResult = false;

    /**
         * @param args
         */
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-fontSize")) {
                fontSize = Integer.parseInt(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("-period")) {
                period = Long.parseLong(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("-antialiasing")) {
                antiAliasing = Boolean.parseBoolean(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("-port")) {
                port = Integer.parseInt(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("-classname")) {
                className = args[i + 1];
            } else if (args[i].equalsIgnoreCase("-classtitle")) {
                classTitle = args[i + 1];
            } else if (args[i].equalsIgnoreCase("-eventRaceId")) {
                eventRaceId = Integer.parseInt(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("-timeWindowDelay")) {
                timeWindowDelay = Integer.parseInt(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("-url")) {
                url = args[i + 1];
            } else if (args[i].equalsIgnoreCase("-newBackgroundColor))")) {
                newBackgroundColor = args[i + 1];
            } else if (args[i].equalsIgnoreCase("-useMainResult))")) {
                useMainResult = Boolean.parseBoolean(args[i + 1]);
            }
        }
        Writer writer = null;
        try {
            File file = new File(classTitle + "-" + eventRaceId + ".bat");
            writer = new BufferedWriter(new FileWriter(file));
            writer.write("java resultviewer/ResultViewer -displaymode 1680x1050x24 -fontcolor 0,0,0 ");
            writer.write("-port " + port + " ");
            writer.write("-layout TwoSwitchingPortrait ");
            writer.write("-fontsize " + fontSize + " ");
            writer.write("-antiAliasing " + antiAliasing + " ");
            writer.write("-period " + period + " ");
            writer.write("-layoutparams ");
            writer.write("\"[" + className + ";" + classTitle + ";" + url + ";" + timeWindowDelay + ";10;false;true;false;5;150;1;true;" + eventRaceId + ";0,0,0;-;" + newBackgroundColor + ";true;" + useMainResult + "]");
            writer.write("[-;;logooringen2009.gif;6;0;false;false;false;0;0;0;false;0;0,0,0;-;255,255,255;true;true]");
            writer.write("[" + className + ";" + ";" + url + ";" + timeWindowDelay + ";10;false;true;false;5;150;1;true;" + eventRaceId + ";0,0,0;-;" + newBackgroundColor + ";true;" + useMainResult + "]");
            writer.write("[-;;logooringen2009.gif;6;0;false;false;false;0;0;0;false;0;0,0,0;-;255,255,255;true;false]\"");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
