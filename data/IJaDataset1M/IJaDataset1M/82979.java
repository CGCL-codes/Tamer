package utilitario;

import java.io.*;
import java.util.*;

/**
 Simple input from the keyboard for all primitive types. ver 1.0 
 <pre> 
  Originial Copyright by (c) Peter van der Linden,  May 5 1997. 
         corrected error message 11/21/97 
         made all methods static and included more comments (PB, 3/1999)
  </pre>

 The creator of this software hereby gives you permission to:
 <ol>
 <li> copy the work without changing it </li>
 <li> modify the work providing you send me a copy which I can
    use in any way I want, including incorporating into this work.</li> 
 <li> distribute copies of the work to the public by sale, lease, 
    rental, or lending </li>
 <li> perform the work</li>
 <li> display the work</li>
 <li> fold the work into a funny hat and wear it on your head.</li>
 </ol>

 This is not thread safe, not high performance, and doesn't tell EOF.
 It's intended for low-volume easy keyboard input. <p>

 An example of use is: 
   <pre>

import de.berlios.StochasticSimulation.EasyIn;

   int     i = EasyIn.readInt();       // reads an int from System.in
   float   f = EasyIn.readFloat();     // reads a float from System.in 
   boolean b = EasyIn.readBoolean();   // reads a boolean from System.in
   </pre>
   etc.
*/
public abstract class EasyIn {

    static InputStreamReader is = new InputStreamReader(System.in);

    static BufferedReader br = new BufferedReader(is);

    private static StringTokenizer st;

    private static StringTokenizer getToken() throws IOException {
        String s = br.readLine();
        return new StringTokenizer(s);
    }

    /** read a boolean from stdin */
    public static boolean readBoolean() {
        try {
            st = getToken();
            return new Boolean(st.nextToken()).booleanValue();
        } catch (IOException ioe) {
            System.err.println("IO Exception in EasyIn.readBoolean");
            return false;
        }
    }

    /** read a byte from stdin */
    public static byte readByte() {
        try {
            st = getToken();
            return Byte.parseByte(st.nextToken());
        } catch (IOException ioe) {
            System.err.println("IO Exception in EasyIn.readByte");
            return 0;
        }
    }

    /** read a short from stdin */
    public static short readShort() {
        try {
            st = getToken();
            return Short.parseShort(st.nextToken());
        } catch (IOException ioe) {
            System.err.println("IO Exception in EasyIn.readShort");
            return 0;
        }
    }

    /** read an integer from stdin */
    public static int readInt() {
        try {
            st = getToken();
            return Integer.parseInt(st.nextToken());
        } catch (IOException ioe) {
            System.err.println("IO Exception in EasyIn.readInt");
            return 0;
        }
    }

    /** read a long from stdin */
    public static long readLong() {
        try {
            st = getToken();
            return Long.parseLong(st.nextToken());
        } catch (IOException ioe) {
            System.err.println("IO Exception in EasyIn.readLong");
            return 0L;
        }
    }

    /** read a float from stdin */
    public static float readFloat() {
        try {
            st = getToken();
            return new Float(st.nextToken()).floatValue();
        } catch (IOException ioe) {
            System.err.println("IO Exception in EasyIn.readFloat");
            return 0.0F;
        }
    }

    /** read a double from stdin */
    public static double readDouble() {
        try {
            st = getToken();
            return new Double(st.nextToken()).doubleValue();
        } catch (IOException ioe) {
            System.err.println("IO Exception in EasyIn.readDouble");
            return 0.0;
        }
    }

    /** read a char from stdin */
    public static char readChar() {
        try {
            String s = br.readLine();
            return s.charAt(0);
        } catch (IOException ioe) {
            System.err.println("IO Exception in EasyIn.readChar");
            return 0;
        }
    }

    /** read a string from stdin */
    public static String readString() {
        try {
            return br.readLine();
        } catch (IOException ioe) {
            System.err.println("IO Exception in EasyIn.readString");
            return "";
        }
    }
}
