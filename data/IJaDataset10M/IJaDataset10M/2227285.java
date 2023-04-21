package lv.accenture.ex04;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class HilbertTest {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: HibertTest order:int filepath");
            System.exit(1);
        }
        int order = Integer.parseInt(args[0]);
        String fileName = args[1];
        Hilbert hilbert = new Hilbert();
        FileOutputStream fos = new FileOutputStream(fileName);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(hilbert.hilbertSVG(order));
        bw.close();
        osw.close();
        fos.close();
    }
}
