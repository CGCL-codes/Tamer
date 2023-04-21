package reconcile.weka.core;

import java.io.Serializable;

/** 
 * Class representing a single cardinal number. The number is set by a 
 * string representation such as: <P>
 *
 * <code>
 *   first
 *   last
 *   1
 *   3
 * </code> <P>
 * The number is internally converted from 1-based to 0-based (so methods that 
 * set or get numbers not in string format should use 0-based numbers).
 *
 * @author Eibe Frank (eibe@cs.waikato.ac.nz)
 * @version $Revision: 1.1 $
 */
public class SingleIndex implements Serializable {

    /** Record the string representation of the number */
    protected String m_IndexString = "";

    /** The selected index */
    protected int m_SelectedIndex = -1;

    /** Store the maximum value permitted. -1 indicates that no upper
      value has been set */
    protected int m_Upper = -1;

    public SingleIndex() {
    }

    public SingleIndex(String index) {
        setSingleIndex(index);
    }

    public void setUpper(int newUpper) {
        if (newUpper >= 0) {
            m_Upper = newUpper;
            setValue();
        }
    }

    public String getSingleIndex() {
        return m_IndexString;
    }

    public void setSingleIndex(String index) {
        m_IndexString = index;
        m_SelectedIndex = -1;
    }

    public String toString() {
        if (m_IndexString.equals("")) {
            return "No index set";
        }
        if (m_Upper == -1) {
            throw new RuntimeException("Upper limit has not been specified");
        }
        return m_IndexString;
    }

    public int getIndex() {
        if (m_IndexString.equals("")) {
            throw new RuntimeException("No index set");
        }
        if (m_Upper == -1) {
            throw new RuntimeException("No upper limit has been specified for index");
        }
        return m_SelectedIndex;
    }

    public static String indexToString(int index) {
        return "" + (index + 1);
    }

    protected void setValue() {
        if (m_IndexString.equals("")) {
            throw new RuntimeException("No index set");
        }
        if (m_IndexString.toLowerCase().equals("first")) {
            m_SelectedIndex = 0;
        } else if (m_IndexString.toLowerCase().equals("last")) {
            m_SelectedIndex = m_Upper;
        } else {
            m_SelectedIndex = Integer.parseInt(m_IndexString) - 1;
            if (m_SelectedIndex < 0) {
                m_IndexString = "";
                throw new IllegalArgumentException("Index must be greater than zero");
            }
            if (m_SelectedIndex > m_Upper) {
                m_IndexString = "";
                throw new IllegalArgumentException("Index is too large");
            }
        }
    }

    public static void main(String[] argv) {
        try {
            if (argv.length == 0) {
                throw new Exception("Usage: SingleIndex <indexspec>");
            }
            SingleIndex singleIndex = new SingleIndex();
            singleIndex.setSingleIndex(argv[0]);
            singleIndex.setUpper(9);
            System.out.println("Input: " + argv[0] + "\n" + singleIndex.toString());
            int selectedIndex = singleIndex.getIndex();
            System.out.println(selectedIndex + "");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }
}
