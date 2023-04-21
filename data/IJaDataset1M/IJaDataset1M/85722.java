package condorAPI;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.TimerTask;
import java.util.Timer;
import condor.classad.ClassAdParser;
import condor.classad.RecordExpr;

/** 
 * Log monitor for xml formatted condor log
 */
class LogMonitor {

    static Timer timer = new Timer("LogMonitor", true);

    Condor condor;

    String filename;

    LineNumberReader lnr;

    ClassAdParser parser = new ClassAdParser(ClassAdParser.XML);

    private TimerTask task = null;

    int counter = 0;

    int maxError = 10;

    double sleepSec = 5;

    LogMonitor(Condor condor, String filename, double sleepSec) {
        this.condor = condor;
        this.filename = filename;
        this.sleepSec = sleepSec;
    }

    synchronized void setInterval(double sleepSec) {
        this.sleepSec = sleepSec;
        if (task == null) return;
        task.cancel();
        start();
    }

    void openLnr() {
        if (lnr == null) {
            try {
                FileReader reader = new FileReader(filename);
                lnr = new LineNumberReader(reader);
            } catch (IOException e) {
                System.err.println("failed to open " + filename + " , retry.");
                counter++;
                return;
            }
        }
    }

    StringBuffer sb = new StringBuffer();

    RecordExpr readExpr() throws IOException {
        String tmp;
        while (true) {
            tmp = lnr.readLine();
            if (tmp == null) return null;
            sb.append(tmp);
            if (tmp.trim().equals("</c>")) break;
        }
        String adString = sb.toString();
        sb = new StringBuffer();
        parser = new ClassAdParser(ClassAdParser.XML);
        parser.reset(adString);
        return (RecordExpr) (parser.parse());
    }

    void readLog() {
        try {
            RecordExpr expr;
            while ((expr = readExpr()) != null) {
                Event event = Event.getEvent(expr);
                if (condor != null) condor.informEvent(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
            counter++;
        }
    }

    public synchronized void start() {
        task = new TimerTask() {

            public void run() {
                LogMonitor.this.run();
            }
        };
        timer.schedule(task, 0, (long) (sleepSec * 1000));
    }

    public synchronized void stop() {
        if (task == null) return;
        task.cancel();
        task = null;
    }

    public void run() {
        if (counter < maxError) {
            openLnr();
            if (lnr != null) {
                readLog();
            }
        } else {
            stop();
            System.err.println("LogMonitor for " + filename + " exits: too many errors");
        }
    }

    /**
	 * just for test
	 * @throws InterruptedException 
	 */
    public static void main(String[] args) throws InterruptedException {
        String filename = args[0];
        LogMonitor monitor = new LogMonitor(null, filename, 10);
        monitor.start();
        Thread.sleep(100000);
    }
}
