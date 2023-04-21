package sf2.vm.impl.xen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import sf2.core.Config;
import sf2.core.ConfigException;
import sf2.vm.VMMonitor;

public class XenMonitor implements VMMonitor, XenCommon {

    protected static boolean useSudo;

    protected static String xentopCmd;

    static {
        try {
            Config config = Config.search();
            useSudo = config.getBoolean(PROP_USE_SUDO, DEFAULT_USE_SUDO);
            xentopCmd = config.get(PROP_XENTOP_CMD, DEFAULT_XENTOP_CMD);
        } catch (ConfigException e) {
            e.printStackTrace();
        }
    }

    protected Thread worker;

    protected boolean running;

    public void start() {
        running = true;
        worker = new Thread(new MonitorWorker());
        worker.start();
    }

    public void shutdown() {
        running = false;
    }

    class MonitorWorker implements Runnable {

        public void run() {
            try {
                while (running) {
                    update();
                    Thread.sleep(1000);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    protected void update() throws IOException {
        ProcessBuilder builder = useSudo ? new ProcessBuilder("sudo", xentopCmd, "-b", "-i", "1") : new ProcessBuilder(xentopCmd, "-b", "-i", "1");
        builder.redirectErrorStream(false);
        Process process = builder.start();
        process.getErrorStream().close();
        process.getOutputStream().close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null;
        while ((line = reader.readLine()) != null) {
            StringTokenizer tokenizer = new StringTokenizer(line);
            String name = tokenizer.nextToken();
            if (name.equals("NAME")) continue;
            String state = tokenizer.nextToken();
            long cpu = Long.parseLong(tokenizer.nextToken());
            double cpuRatio = Double.parseDouble(tokenizer.nextToken());
            int mem = Integer.parseInt(tokenizer.nextToken());
            double memRatio = Double.parseDouble(tokenizer.nextToken());
            long maxmem;
            String mmem = tokenizer.nextToken();
            if (mmem.equals("no")) {
                tokenizer.nextToken();
                maxmem = -1;
            } else {
                maxmem = Long.parseLong(mmem);
            }
            double maxmemRatio;
            String mmemRatio = tokenizer.nextToken();
            if (mmemRatio.equals("n/a")) {
                maxmemRatio = -1.0;
            } else {
                maxmemRatio = Double.parseDouble(mmemRatio);
            }
            int vcpus = Integer.parseInt(tokenizer.nextToken());
            int nets = Integer.parseInt(tokenizer.nextToken());
            long nettx = Long.parseLong(tokenizer.nextToken());
            long netrx = Long.parseLong(tokenizer.nextToken());
            long vbds = Long.parseLong(tokenizer.nextToken());
            long vbdoo = Long.parseLong(tokenizer.nextToken());
            long vbdrd = Long.parseLong(tokenizer.nextToken());
            long vbdwr = Long.parseLong(tokenizer.nextToken());
            long ssid = Long.parseLong(tokenizer.nextToken());
            System.out.println("name=" + name + " ssid" + ssid);
        }
        reader.close();
    }
}
