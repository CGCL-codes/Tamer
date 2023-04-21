package preprocessing.methods.Import.FileLoader;

import java.util.Vector;

/**
 *
 * @author pv
 */
public abstract class DataLoader<T> {

    public String relation;

    public Vector<DataAttribute> attributes;

    public String errorMsg;

    public Integer error;

    public int lineIndex = 0;

    public int commentLines = 0;

    public int attributesFound = 0;

    private String[] spacecols;

    public int dataStartLine = 0;

    public int dataLines = 0;

    public String BoundaryString = "";

    public String dataValueSeparator = ",";

    public Vector<Vector<T>> data;

    public int unknownValues = 0;

    public int sparseLines = 0;

    public abstract void load(String path) throws Exception;

    public String RawDataReport() {
        StringBuilder result = new StringBuilder();
        if (data != null) {
            int i = 0;
            for (i = 0; i < data.size(); i++) {
                String dataline = "#" + i + ": ";
                for (Object item : data.get(i)) {
                    dataline += item + " | ";
                }
                dataline += "\n";
                result.append(dataline);
            }
        }
        return result.toString();
    }
}
