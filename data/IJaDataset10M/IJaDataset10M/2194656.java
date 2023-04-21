package pogvue.datamodel;

import java.util.Vector;

public class Range {

    public String name;

    public int start;

    public int end;

    public String type;

    public double score;

    public Range(int start, int end) {
        this(null, null, start, end, 0);
    }

    public Range(String type, int start, int end) {
        this(null, null, start, end, 0);
    }

    public Range(String name, String type, int start, int end, double score) {
        this.name = name;
        this.type = type;
        this.start = start;
        this.end = end;
        this.score = score;
    }
}
