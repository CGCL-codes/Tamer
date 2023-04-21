package rj.tools.jcsc;

/**
 * <code>MethodMetric</code> - a metric for a method
 *
 * @author Ralph Jocham
 * @version __0.98.2__
 */
public class MethodMetric {

    private String mName;

    private int mNCSSCount;

    private int mCCNCount;

    private int mLine;

    private int mColumn;

    /**
    * Creates a new <code>MethodMetric</code> instance.
    *
    * @param line an <code>int</code> value - line of method
    * @param column an <code>int</code> value - column of method
    * @param name a <code>String</code> value - name of method
    * @param ncssCount an <code>int</code> value - ncssCount of method
    * @param ccnCount an <code>int</code> value - ccnCount of method
    */
    public MethodMetric(int line, int column, String name, int ncssCount, int ccnCount) {
        setLine(line);
        setColumn(column);
        setMethodName(name);
        setNCSSCount(ncssCount);
        setCCNCount(ccnCount);
    }

    /**
    * <code>setMethodName</code>
    *
    * @param name a <code>String</code> value
    */
    public void setMethodName(String name) {
        mName = name;
    }

    /**
    * <code>getMethodName</code>
    *
    * @return a <code>String</code> value
    */
    public String getMethodName() {
        return mName;
    }

    /**
    * <code>setNCSSCount</code>
    *
    * @param count an <code>int</code> value
    */
    public void setNCSSCount(int count) {
        mNCSSCount = count;
    }

    /**
    * <code>getNCSSCount</code>
    *
    * @return an <code>int</code> value
    */
    public int getNCSSCount() {
        return mNCSSCount;
    }

    /**
    * <code>setCCNCount</code>
    *
    * @param count an <code>int</code> value
    */
    public void setCCNCount(int count) {
        mCCNCount = count;
    }

    /**
    * <code>getCCNCount</code>
    *
    * @return an <code>int</code> value
    */
    public int getCCNCount() {
        return mCCNCount;
    }

    /**
    * <code>setLine</code>
    *
    * @param line an <code>int</code> value
    */
    public void setLine(int line) {
        mLine = line;
    }

    /**
    * <code>getLine</code>
    *
    * @return an <code>int</code> value
    */
    public int getLine() {
        return mLine;
    }

    /**
    * <code>setColumn</code>
    *
    * @param column an <code>int</code> value
    */
    public void setColumn(int column) {
        mColumn = column;
    }

    /**
    * <code>getColumn</code>
    *
    * @return an <code>int</code> value
    */
    public int getColumn() {
        return mColumn;
    }
}
