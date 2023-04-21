package name.ddns.green.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.TimeZone;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStore;
import com.nokia.mid.ui.DeviceControl;
import name.ddns.green.mobile.FATJava.FATJava;
import name.ddns.green.mobile.FATJava.RMSAccessor;
import name.ddns.green.mobile.FATJava.FATJava.FileData;

/** A test harness for MIDP phones.  Although this looks extravagant, it is not
 * part of the library
 * 
 * @author paulg
 */
public class mobile_harness extends MIDlet {

    private runner r = null;

    private class runner extends Thread {

        boolean nokia_platform = true;

        PrintStream out = null;

        InputStream in = null;

        int[] blockMap = new int[512];

        RecordStore r;

        Display display = null;

        boolean closing = false;

        public runner(Display d) {
            display = d;
        }

        public void kill() {
            closing = true;
            if (nokia_platform) {
                DeviceControl.startVibra(50, 1000);
                for (int cntr = 0; cntr < 10000000; cntr++) ;
            }
            FATJava.kill();
        }

        public void run() {
            display.setCurrent(new ui());
            try {
                try {
                    Class.forName("com.nokia.mid.ui.DeviceControl");
                    StreamConnection cc = (StreamConnection) Connector.open("btspp://00198600167A:14");
                    out = new PrintStream(new DosStream(cc.openOutputStream()));
                    in = cc.openInputStream();
                    in.read();
                    DeviceControl.setLights(0, 100);
                } catch (Exception e1) {
                    nokia_platform = false;
                    out = new PrintStream(new ConsoleStream());
                    FileConnection fconn = (FileConnection) Connector.open("file:///E/input.txt");
                    in = fconn.openInputStream();
                }
                out.println("[2J[HStarting application.");
                out.print("Do you want to refresh the RMS with a new image? (y/n) ?");
                int input;
                do {
                    input = in.read();
                    input = input & 0xdf;
                } while ((input != 'Y') && (input != 'N'));
                out.println((char) input);
                RMSAccessor f = new RMSAccessor("FAT", 360);
                if (input == 'Y') {
                    out.println("Refreshing the RMS");
                    InputStream i = getClass().getResourceAsStream("/resources/180K");
                    out.print("Beginning refresh....");
                    FATJava.import_data(f, i, 512);
                    i.close();
                    out.println("done");
                }
                FATJava fat = FATJava.getInstance();
                fat.mount(f, "ROOT");
                out.println("Filesystem roots;");
                String[] x = fat.listRoots();
                for (int cntr = 0; cntr < x.length; cntr++) out.println("-->" + x[cntr]);
                out.println("Scanning dir...");
                Calendar c = Calendar.getInstance();
                FileData[] list = fat.getFileList("ROOT:");
                for (int cntr = 0; cntr < list.length; cntr++) {
                    FileData fd = list[cntr];
                    String dout = ((fd.fileAttrs & FATJava.FAT_SUBDIR) != 0 ? "d" : "-") + ((fd.fileAttrs & FATJava.FAT_RO) != 0 ? "r" : "-") + ((fd.fileAttrs & FATJava.FAT_HIDDEN) != 0 ? "h" : "-") + ((fd.fileAttrs & FATJava.FAT_SYSTEM) != 0 ? "s" : "-") + " " + fd.filename;
                    for (int cntr2 = fd.filename.length(); cntr2 < 8; cntr2++) dout = dout + " ";
                    dout = dout + " " + fd.extension;
                    for (int cntr2 = fd.extension.length(); cntr2 < 4; cntr2++) dout = dout + " ";
                    String len = "";
                    if ((fd.fileAttrs & FATJava.FAT_SUBDIR) == 0) len = String.valueOf(fd.length);
                    while (len.length() < 9) len = " " + len;
                    dout = dout + len + " ";
                    c.setTime(fd.lastModified);
                    dout = dout + c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
                    out.println(dout);
                }
                out.println("Showing file data");
                int fd1 = fat.open("ROOT:/dir/abc", FATJava.READ_ONLY);
                while (true) {
                    byte[] data = fat.read(fd1, 16);
                    if (data.length == 0) break;
                    out.println(new String(data, 0, data.length));
                }
                fat.close(fd1);
                out.println("\nProgram exit - press any key");
                in.read();
                destroyApp(true);
                notifyDestroyed();
            } catch (Exception e) {
                out.println("Exit exception: " + e.getClass().getName() + "," + e.getMessage());
            }
        }
    }

    private class ui extends Canvas {

        Image disk = null;

        public ui() {
            try {
                disk = Image.createImage("/resources/disk.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            setFullScreenMode(true);
        }

        protected void paint(Graphics arg0) {
            arg0.setColor(0, 0, 0);
            arg0.fillRect(0, 0, 240, 320);
            arg0.drawImage(disk, 120 - (disk.getWidth() / 2), 0, Graphics.TOP + Graphics.LEFT);
        }
    }

    private class ConsoleStream extends OutputStream {

        public void write(byte[] b, int off, int len) throws IOException {
            System.out.write(b, off, len);
            System.out.flush();
        }

        public void write(byte[] b) throws IOException {
            System.out.write(b);
            System.out.flush();
        }

        public void write(int b) throws IOException {
            System.out.write(b);
            System.out.flush();
        }
    }

    private class DosStream extends OutputStream {

        private OutputStream destination = null;

        public DosStream(OutputStream source) {
            destination = source;
        }

        public void write(byte[] b, int off, int len) throws IOException {
            for (int cntr = 0; cntr < b.length; cntr++) if (b[cntr] == 10) b[cntr] = 13;
            destination.write(b, off, len);
            destination.flush();
        }

        public void write(byte[] b) throws IOException {
            for (int cntr = 0; cntr < b.length; cntr++) if (b[cntr] == 10) b[cntr] = 13;
            destination.write(b);
            destination.flush();
        }

        public void write(int b) throws IOException {
            destination.write(b);
            destination.flush();
        }
    }

    private class PrintStream extends java.io.PrintStream {

        public PrintStream(OutputStream O) {
            super(O);
        }

        public void print(boolean b) {
            super.print(b);
        }

        public void print(char c) {
            super.print(c);
        }

        public void print(char[] s) {
            super.print(s);
        }

        public void print(double d) {
            super.print(d);
        }

        public void print(float f) {
            super.print(f);
        }

        public void print(int i) {
            super.print(i);
        }

        public void print(long l) {
            super.print(l);
        }

        public void print(Object obj) {
            super.print(obj);
        }

        public void print(String s) {
            super.print(s);
        }

        public void println() {
            super.println();
        }

        public void println(boolean x) {
            super.println(x);
        }

        public void println(char x) {
            super.println(x);
        }

        public void println(char[] x) {
            super.println(x);
        }

        public void println(double x) {
            super.println(x);
        }

        public void println(float x) {
            super.println(x);
        }

        public void println(int x) {
            super.println(x);
        }

        public void println(long x) {
            super.println(x);
        }

        public void println(Object x) {
            super.println(x);
        }

        public void println(String x) {
            super.println(x);
        }

        protected void setError() {
            super.setError();
        }

        public void write(byte[] buf, int off, int len) {
            super.write(buf, off, len);
        }

        public void write(int b) {
            super.write(b);
        }
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        r.kill();
        r.interrupt();
    }

    protected void pauseApp() {
    }

    protected void startApp() throws MIDletStateChangeException {
        Display d = Display.getDisplay(this);
        Form f = new Form("");
        d.setCurrent(f);
        r = new runner(d);
        r.start();
    }
}
