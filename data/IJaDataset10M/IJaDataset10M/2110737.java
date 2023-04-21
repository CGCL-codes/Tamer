package study.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

public class A {

    public static void main(String[] args) throws IOException {
        File fileIn = new File("D:\\work\\study\\src\\study\\io\\A.java");
        File fileOut = new File("D:\\work\\study\\src\\study\\io2\\Aout.java");
        File fileOutMid = new File("D:\\work\\study\\src\\study\\io2\\AoutMid.java");
        OutputStream foutMid = new FileOutputStream(fileOutMid);
        OutputStream fout = new FileOutputStream(fileOut);
        BufferedOutputStream bfout = new BufferedOutputStream(fout);
        BufferedOutputStream bfoutMid = new BufferedOutputStream(foutMid);
        InputStream fin = new FileInputStream(fileIn);
        PipedInputStream pin = new PipedInputStream();
        PipedOutputStream pout = new PipedOutputStream();
        pout.connect(pin);
        A a = new A();
        IO_Switch IOS1 = a.new IO_Switch();
        IOS1.setIn(fin);
        IOS1.addOut(bfout);
        IOS1.addOut(pout);
        IOS1.start();
        IoRead ior = a.new IoRead(pin, bfoutMid);
        System.out.println("========== 1 OK !");
        ior.start();
        System.out.print("\n");
    }

    public class IoRead extends Thread {

        PipedInputStream pin;

        BufferedOutputStream bfout;

        public IoRead(PipedInputStream pin, BufferedOutputStream bfout) {
            this.pin = pin;
            this.bfout = bfout;
        }

        public void run() {
            System.out.println("========== 2 START !");
            A a = new A();
            IO_Switch IOS2 = a.new IO_Switch();
            IOS2.setIn(pin);
            IOS2.addOut(bfout);
            try {
                IOS2.start();
                bfout.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("========== 2 OK !");
        }
    }

    public class IO_Switch extends Thread {

        protected List<OutputStream> lout;

        InputStream in;

        public void run() {
            int c;
            OutputStream[] outS = new OutputStream[lout.size()];
            outS = lout.toArray(outS);
            try {
                while ((c = in.read()) != -1) {
                    for (int i = 0; i < outS.length; ++i) {
                        System.out.print((char) c);
                        if (outS[i] != null) outS[i].write(c);
                    }
                }
                for (int i = 0; i < outS.length; ++i) {
                    if (outS[i] != null) outS[i].close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public List<OutputStream> getLout() {
            return lout;
        }

        public void setLout(List<OutputStream> lout) {
            this.lout = lout;
        }

        public void addOut(OutputStream out) {
            if (this.lout == null) lout = new ArrayList<OutputStream>();
            lout.add(out);
        }

        public InputStream getIn() {
            return in;
        }

        public void setIn(InputStream inBin) {
            this.in = inBin;
        }
    }
}
