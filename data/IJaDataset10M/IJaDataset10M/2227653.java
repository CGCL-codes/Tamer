package sgf.value;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValueSize implements Value {

    private static final Pattern p0 = Pattern.compile("\\s*(\\d+)\\s*");

    private static final Pattern p1 = Pattern.compile("\\s*(\\d+)\\s*:\\s*(\\d+)\\s*");

    private int size;

    public ValueSize() {
        size = 0;
    }

    public ValueSize(int boardSize) {
        size = boardSize;
    }

    public void setSgfString(String str) {
        Matcher m0 = p0.matcher(str);
        Matcher m1 = p1.matcher(str);
        if (m0.matches()) {
            size = Integer.parseInt(m0.group(1));
        } else if (m1.matches()) {
            size = Integer.parseInt(m0.group(1));
        } else {
            System.err.println("error:ValueSize:" + str + ":");
        }
    }

    public String getSgfString() {
        return Integer.toString(size);
    }

    public String getValueString() {
        StringBuilder str = new StringBuilder();
        str.append("[");
        str.append(getSgfString());
        str.append("]");
        return str.toString();
    }

    public int getSize() {
        return size;
    }
}
