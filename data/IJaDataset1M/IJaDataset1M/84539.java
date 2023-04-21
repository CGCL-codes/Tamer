package part_2;

import java.io.*;
import java.util.*;

public class ReadFile {

    BufferedReader br;

    StringTokenizer st;

    public ReadFile(String fileName) throws FileNotFoundException, IOException {
        boolean inputOk = false;
        br = new BufferedReader(new FileReader(fileName));
        while (!inputOk) {
            try {
                st = new StringTokenizer(br.readLine());
                inputOk = true;
            } catch (NoSuchElementException e) {
                inputOk = false;
            } catch (IOException e) {
                throw e;
            }
        }
    }

    public double readDouble() throws IOException {
        double d = 0;
        boolean inputOk = false;
        String str;
        while (!inputOk) {
            try {
                if (st.hasMoreTokens()) {
                    d = Double.parseDouble(st.nextToken());
                    inputOk = true;
                } else {
                    str = br.readLine();
                    if (str != null) {
                        st = new StringTokenizer(str);
                        inputOk = false;
                    } else {
                        throw new EOFException();
                    }
                }
            } catch (NoSuchElementException e) {
                inputOk = false;
            }
        }
        return d;
    }

    public int readInt() throws IOException {
        int i = 0;
        boolean inputOk = false;
        String str;
        while (!inputOk) {
            try {
                if (st.hasMoreTokens()) {
                    i = Integer.parseInt(st.nextToken());
                    inputOk = true;
                } else {
                    str = br.readLine();
                    if (str != null) {
                        st = new StringTokenizer(str);
                        inputOk = false;
                    } else {
                        throw new EOFException();
                    }
                }
            } catch (NoSuchElementException e) {
                inputOk = false;
            }
        }
        return i;
    }

    public void close() throws IOException {
        br.close();
    }
}
