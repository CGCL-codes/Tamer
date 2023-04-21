package net.sf.zorobot.util;

import java.util.ArrayList;

public class SourceSegment {

    public int start, end;

    public String segment;

    public ArrayList<String> pairs;

    public SourceSegment(int a, int b, String seg, ArrayList<String> al) {
        start = a;
        end = b;
        segment = seg;
        pairs = al;
    }
}
