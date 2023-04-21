package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import flanagan.math.Matrix;

/**
 * Provide the import matrix routine.
 * 
 * @author Luca Petraglio
 * @author Michael Mattes
 */
public class ImportMatrix {

    /**
	 * Empty constructor.
	 */
    public ImportMatrix() {
    }

    /**
	 * Get the matrix from a file.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the matrix
	 */
    public Matrix getMatrix(final String fileName) {
        boolean ok = true;
        Matrix m = null;
        BufferedReader br = null;
        ArrayList<String> list = new ArrayList<String>();
        double[][] dualArray;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String t;
            while ((t = br.readLine()) != null) {
                try {
                    final StringTokenizer st = new StringTokenizer(t);
                    if (st.countTokens() == 0) {
                        continue;
                    }
                    while (st.hasMoreTokens()) {
                        String s = st.nextToken();
                        if (s.equalsIgnoreCase("x")) {
                            s = "0.0";
                        }
                        new BigDecimal(s);
                    }
                } catch (final NumberFormatException e) {
                    ok = false;
                    list = null;
                    br.close();
                    break;
                } catch (final NoSuchElementException e) {
                    list = null;
                    br.close();
                    break;
                }
                list.add(t);
            }
            br.close();
        } catch (final IOException e) {
            JOptionPane.showMessageDialog(null, "Invalid file: " + fileName, "Warning", JOptionPane.WARNING_MESSAGE);
            ok = false;
        }
        if (ok) {
            dualArray = new double[list.size()][list.size()];
            StringTokenizer st;
            for (int i = 0; i < list.size(); i++) {
                st = new StringTokenizer(list.get(i));
                for (int j = 0; j < list.size(); j++) {
                    if (st.hasMoreTokens()) {
                        String s = st.nextToken();
                        if (s.equalsIgnoreCase("x")) {
                            s = "0.0";
                        }
                        dualArray[i][j] = new BigDecimal(s).doubleValue();
                    }
                }
            }
            m = new Matrix(dualArray);
        }
        return m;
    }

    /**
	 * set all the file's line into an array list.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the array list
	 */
    public ArrayList<String> getLinesAsList(final String fileName) {
        BufferedReader br = null;
        ArrayList<String> list = new ArrayList<String>();
        int tokens = 0;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String t;
            while ((t = br.readLine()) != null) {
                try {
                    final StringTokenizer st = new StringTokenizer(t);
                    if (st.countTokens() == 0) {
                        continue;
                    }
                    tokens = st.countTokens();
                    while (st.hasMoreTokens()) {
                        String s = st.nextToken();
                        if (s.equalsIgnoreCase("x")) {
                            s = "0.0";
                        }
                        new BigDecimal(s);
                    }
                } catch (final NumberFormatException e) {
                    list = null;
                    br.close();
                    break;
                } catch (final NoSuchElementException e) {
                    list = null;
                    br.close();
                    break;
                }
                list.add(t);
            }
            br.close();
        } catch (final IOException e) {
            list = null;
        }
        if (list != null) {
            if (tokens != list.size()) {
                list.clear();
                list.add("not square");
            }
        }
        return list;
    }
}
