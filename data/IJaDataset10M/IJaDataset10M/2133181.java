package network;

import java.lang.*;
import java.io.*;
import java.util.*;
import java.net.*;

/** The Network Alalyser client works along with the network analyser server 
 * Finds the network stats
 */
public class networkAnalyzerClient extends networkAnalyzer {

    private static long histime;

    private static long mytime;

    private static long mytimeD;

    private static long mytimeX;

    public networkAnalyzerClient(DataInputStream in, DataOutputStream out, int numberofSends) throws Exception {
        int count = 0;
        mytime = 0;
        histime = 0;
        mytimeD = 0;
        out.writeInt(numberofSends);
        mytimeD = System.currentTimeMillis();
        while (count < numberofSends + 1) {
            mytime = System.currentTimeMillis();
            out.writeLong(mytime);
            histime = in.readLong();
            mytimeD = System.currentTimeMillis();
            addTime(mytime, mytimeD, histime);
            count++;
        }
        analyse();
    }

    public static void main(String[] args) throws Exception {
        Socket s = new Socket(args[0], 8909);
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        DataInputStream in = new DataInputStream(s.getInputStream());
        networkAnalyzerClient a = new networkAnalyzerClient(in, out, 30);
        s.close();
    }
}
