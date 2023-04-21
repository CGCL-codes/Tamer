package GEMpire;

import java.io.*;
import java.util.Vector;

public class DelimitedLineReader {

    String line;

    StringBuffer sb = new StringBuffer();

    char sepChar;

    int nextPos = 0;

    public DelimitedLineReader(char sepChar1) {
        sepChar = sepChar1;
    }

    /** Count the number of columns in the file with delimit character c. */
    public static int countColumns(String filename, char c) throws IOException {
        DelimitedLineReader dlr = new DelimitedLineReader(c);
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String line = in.readLine();
        boolean done = false;
        while (!done) {
            if (line != null) {
                if (line.trim().equals("")) line = in.readLine(); else done = true;
            } else return 0;
        }
        dlr.setLine(line);
        int i = 0;
        while (dlr.getNextField() != null) i++;
        in.close();
        return i;
    }

    /** sets the current line for subsequent calls to the various getFields() methods. */
    public void setLine(String line1) throws IOException {
        if (line1 == null) throw new IOException("null pointer passed in");
        line = line1;
        sb.setLength(0);
        nextPos = 0;
    }

    /** returns an Vector of all fields in target line */
    public Vector getFields() throws IOException {
        Vector v = new Vector();
        String field = getNextField();
        while (field != null) {
            v.addElement(field);
            field = getNextField();
        }
        return v;
    }

    /** returns an array of all fields in target line */
    public String[] getFieldsAsArray() throws IOException {
        Vector v = new Vector();
        String field = getNextField();
        while (field != null) {
            v.addElement(field);
            field = getNextField();
        }
        int n = v.size();
        String tmp[] = new String[n];
        for (int i = 0; i < n; i++) {
            tmp[i] = (String) (v.elementAt(i));
        }
        return tmp;
    }

    /** returns an array of String with all fields from columns 0..(numCols-1) */
    public String[] getFields(int numCols) throws IOException {
        String fields[] = new String[numCols];
        for (int i = 0; i < numCols; i++) {
            fields[i] = getNextField();
        }
        return fields;
    }

    /** Returns selected columns from the line (set by setLine), specified by fields[].
     */
    public String[] getFields(int fields[]) throws IOException {
        String rv[] = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            int inc;
            if (i == 0) inc = fields[0] + 1; else inc = fields[i] - fields[i - 1];
            if (inc <= 0) throw new IOException("array passed in was non-increasing");
            String tmp = null;
            for (int j = 0; j < inc; j++) tmp = getNextField();
            if (tmp == null) {
                System.out.println("DelimitedLineReader.getFields(): this shouldn't happen!");
                System.exit(1);
            }
            rv[i] = tmp;
        }
        return rv;
    }

    public String getNthField(int n) throws IOException {
        String field = "";
        for (int i = 0; i < n; i++) {
            field = getNextField();
            if (field == null) throw new IOException("expected more fields");
        }
        return field;
    }

    public int getNthFieldAsInt(int n) throws IOException {
        return Integer.parseInt(getNthField(n).trim());
    }

    public int getNextInt() throws NumberFormatException {
        String field = getNextField();
        if (field == null) throw new NumberFormatException("integer field expected");
        if (field.trim().equals("")) return (0);
        return (Integer.parseInt(field.trim()));
    }

    public int[] getNextInt2() throws NumberFormatException {
        int x[] = new int[1];
        x[0] = 0;
        String field = getNextField();
        if (field == null) return null;
        if (field.trim().equals("")) return (x);
        x[0] = Integer.parseInt(field.trim());
        return (x);
    }

    public double getNextDouble() throws NumberFormatException {
        String field = getNextField();
        if (field == null) throw new NumberFormatException("Double field expected");
        if (field.trim().equals("")) return (0.0);
        return (new Double(field.trim()).doubleValue());
    }

    public int parseInt(String field) throws NumberFormatException {
        if (field.trim().equals("")) return (0);
        return (Integer.parseInt(field.trim()));
    }

    public double parseDouble(String field) throws NumberFormatException {
        String tmp = field.trim();
        if (tmp.equals("")) return 0.0;
        return new Double(tmp).doubleValue();
    }

    public String getNextNonBlankField() {
        String field = getNextField();
        while (field != null && field.trim().equals("")) field = getNextField();
        return field;
    }

    public String getNextField() {
        String tmp;
        if (nextPos == line.length()) {
            nextPos++;
            return ("");
        } else if (nextPos > line.length()) {
            return (null);
        }
        while (nextPos < line.length()) {
            char c = line.charAt(nextPos++);
            if (c == sepChar) {
                tmp = sb.toString();
                sb.setLength(0);
                return tmp;
            }
            sb.append(c);
        }
        nextPos++;
        tmp = sb.toString();
        return tmp;
    }

    public String[] parseToArray(int numColumns) {
        String[] result = new String[numColumns];
        String field = getNextField();
        int i = 0;
        while (field != null && i < numColumns) {
            result[i] = field;
            field = getNextField();
            i++;
        }
        return result;
    }

    public static void test3() throws Exception {
        DelimitedLineReader dlr = new DelimitedLineReader(';');
        dlr.setLine("1  ;1;1  ;3;381;1  ;2;384");
        int i = 1;
        while (true) {
            System.out.println((i++) + ": " + dlr.getNextInt());
        }
    }

    public static void test2() throws Exception {
        DelimitedLineReader dlr = new DelimitedLineReader(';');
        dlr.setLine("1.1;1;5");
        System.out.println(dlr.getNextInt());
        System.out.println(dlr.getNextInt());
        System.out.println(dlr.getNextInt());
        System.out.println(dlr.getNextInt());
    }

    public static void test1() throws Exception {
        DataInputStream din = new DataInputStream(System.in);
        DelimitedLineReader tslr = new DelimitedLineReader(';');
        String line = din.readLine();
        while (line != null) {
            String field;
            tslr.setLine(line);
            field = tslr.getNextField();
            while (field != null) {
                System.out.println("field: <" + field + ">");
                field = tslr.getNextField();
            }
            line = din.readLine();
        }
    }

    public static void test4() throws Exception {
        DataInputStream din = new DataInputStream(System.in);
        DelimitedLineReader tslr = new DelimitedLineReader(';');
        String line = din.readLine();
        while (line != null) {
            String field;
            tslr.setLine(line);
            field = tslr.getNextField();
            while (field != null) {
                try {
                    System.out.println("double field: <" + tslr.parseDouble(field) + ">");
                } catch (NumberFormatException e) {
                    System.out.println("field: <" + field + ">");
                }
                field = tslr.getNextField();
            }
            line = din.readLine();
        }
    }

    public static void main(String args[]) throws Exception {
        test1();
    }
}
