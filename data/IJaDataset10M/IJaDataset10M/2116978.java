package robot.crosstiger;

import static java.lang.Math.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.UnknownHostException;
import robot.crosstiger.monitor.SerialStream;
import josx.platform.gattaca.Action;
import josx.platform.gattaca.EventBoard;

public class CrossTigerDriver {

    static String client = "CrossTigerDriver";

    private static boolean debugflag = false;

    private static double f1 = 1;

    private static double f2 = 20;

    private static String autostart = "";

    private EventBoard bb;

    private SerialStream serialLine1;

    private SerialStream serialLine2;

    private OutputStream outputStream1;

    private OutputStream outputStream2;

    private BufferedReader inputStream1;

    private BufferedReader inputStream2;

    private boolean first = true;

    private String gate;

    public CrossTigerDriver(String connectString, String clientName) throws UnknownHostException, IOException {
        bb = new EventBoard(connectString, clientName);
        if (bb == null) return;
        bb.GetMessage();
    }

    /**
	 * @param args
	 * @throws IOException 
	 */
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        String port = "50501";
        String serialPort = "COM17";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-h")) {
                host = args[++i];
            } else if (args[i].equals("-p")) {
                port = args[++i];
            } else if (args[i].equals("-s")) {
                serialPort = args[++i];
            } else if (args[i].equals("-f1")) {
                f1 = Double.parseDouble(args[++i]);
            } else if (args[i].equals("-f2")) {
                f2 = Double.parseDouble(args[++i]);
            } else if (args[i].equals("-f2")) {
                f2 = Double.parseDouble(args[++i]);
            } else if (args[i].equals("-c")) {
                autostart += " " + args[++i];
            } else if (args[i].equals("-debug")) {
                debugflag = true;
            }
            ;
        }
        ;
        System.out.println(client + " Client started.");
        CrossTigerDriver ad = new CrossTigerDriver(host + ":" + port, client);
        ad.start(serialPort);
    }

    public void start(final String name) throws IOException {
        openSerialLine(name, "COM18");
        bb.SendMessage("CrossTigerPrimitiv:");
        bb.SendMessage("CrossTigerDriver:");
        bb.SendMessage("CamObjects2D:");
        bb.onMessage("CrossTigerDriver", new Action() {

            @Override
            public void action(String... args) {
                if (args.length > 1) {
                    if (args[1].equals("connect")) {
                        if (serialLine1 == null) {
                            try {
                                openSerialLine(name, "COM18");
                                bb.SendMessage("CrossTigerPrimitiv: " + autostart);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (args[1].equals("disconnect")) {
                        try {
                            closeSerialLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        bb.onMessage("CrossTigerPrimitiv", new Action() {

            @Override
            public void action(String... args) {
                for (int i = 1; i < args.length; i++) {
                    if (args[i].indexOf('=') > 0) {
                        String[] list = args[i].split("=");
                        set(list[0], Integer.parseInt(list[1]));
                    } else {
                        cmd(args[i]);
                    }
                }
            }
        });
        bb.onMessage("CamObjects2D", new Action() {

            long t1;

            @Override
            public void action(String... args) {
                if (args.length < 6) return;
                int n = Integer.parseInt(args[1]);
                if (n > 0) {
                    int arg = 2;
                    double mx = 0;
                    double my = 0;
                    int otype = Integer.parseInt(args[arg++]);
                    double confi = Double.parseDouble(args[arg++]);
                    double x = Double.parseDouble(args[arg++]);
                    double y = Double.parseDouble(args[arg++]);
                    int id = Integer.parseInt(args[9]);
                    if (id == 1 && (otype == 1 || otype == 2)) {
                        mx = x;
                        my = max(0, y - 200);
                    }
                    if (my > 0) {
                        double a = f2 * (atan2(my, mx) - PI / 2);
                        set("L", otype);
                        set("M", a);
                        t1 = System.currentTimeMillis();
                        my = 0;
                    }
                } else {
                    set("L", -1);
                    set("M", 3);
                }
            }
        });
        bb.onMessage("terror", new Action() {

            long t1;

            @Override
            public void action(String... args) {
                int seq = Integer.parseInt(args[1]);
                long mytime = 0xffffffffL & Long.parseLong(args[2]);
                int lw = 0xffff & (int) mytime;
                int hw = 0xffff & (int) (mytime >> 16);
                bb.SendMessage("stream: 90 " + seq + " " + mytime);
            }
        });
        Thread serialReadChannel1 = new Thread("readFromCrossTigerChannel1") {

            public void run() {
                try {
                    while (true) {
                        String line;
                        try {
                            line = inputStream1.readLine();
                            if (debugflag) System.out.println(line);
                            bb.SendMessage("stream: " + line);
                            if (line.startsWith("61\t")) {
                                setGateInfo(line);
                            }
                        } catch (IOException e1) {
                            sleep(1000);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        serialReadChannel1.setDaemon(true);
        serialReadChannel1.start();
        Thread serialWriteChannel2 = new Thread("writeToCrossTigerChannel2") {

            public void run() {
                try {
                    while (true) {
                        try {
                            if (outputStream2 == null) {
                            } else {
                                String line = getGateInfo();
                                if (line != null) {
                                    outputStream2.write(line.getBytes("ASCII"));
                                    outputStream2.flush();
                                } else {
                                    sleep(100);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        serialWriteChannel2.setDaemon(true);
        serialWriteChannel2.start();
        Thread serialReadChannel2 = new Thread("readFromCrossTigerChannel2") {

            public void run() {
                try {
                    while (true) {
                        String line;
                        try {
                            if (serialLine2 != null) {
                                line = inputStream2.readLine();
                                if (debugflag) System.out.println(line);
                                bb.SendMessage("stream: " + line);
                            }
                        } catch (IOException e1) {
                            sleep(1000);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        serialReadChannel2.setDaemon(true);
        serialReadChannel2.start();
        bb.SendMessage("CrossTigerPrimitiv: " + autostart);
        bb.startMessageLoop();
    }

    private synchronized void setGateInfo(String line) {
        gate = line + "\n";
    }

    private synchronized String getGateInfo() {
        String line = gate;
        gate = null;
        return line;
    }

    private void openSerialLine(final String name1, String name2) throws IOException {
        serialLine1 = new SerialStream(name1);
        inputStream1 = new BufferedReader(new InputStreamReader(serialLine1.getInputStream()));
        outputStream1 = serialLine1.getOutputStream();
        try {
            serialLine2 = new SerialStream(name2);
            inputStream2 = new BufferedReader(new InputStreamReader(serialLine2.getInputStream()));
            outputStream2 = serialLine2.getOutputStream();
        } catch (Exception e) {
            serialLine2 = null;
            e.printStackTrace();
        }
    }

    private void closeSerialLine() throws IOException {
        if (serialLine1 != null) {
            serialLine1.close();
            inputStream1.close();
            outputStream1.close();
            serialLine1 = null;
        }
        if (serialLine2 != null) {
            inputStream2.close();
            outputStream2.close();
            serialLine2 = null;
        }
    }

    private void send(String string) {
        try {
            if (first) Thread.sleep(2000);
            first = false;
            if (outputStream1 == null) {
                System.out.print(string);
            } else {
                outputStream1.write((string).getBytes("ASCII"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendln(String string) {
        send(string + "\n");
    }

    public void set(String reg, double a) {
        if (a >= 0) {
            int i = (int) (a + 0.5);
            sendln("(" + i + reg + "<)");
        } else {
            int i = (int) (a - 0.5);
            sendln("(" + (-i) + "n" + reg + "<)");
        }
    }

    public void cmd(String cmd) {
        sendln("(" + cmd + ")");
    }
}
