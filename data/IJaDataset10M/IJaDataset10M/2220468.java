package javasean.GBFF;

import java.lang.*;
import java.util.*;

public class CDS {

    private String m_name;

    private ArrayList m_SegmentList;

    public CDS() {
        m_SegmentList = new ArrayList(5);
    }

    public CDS(String the_name) {
        m_name = the_name;
        m_SegmentList = new ArrayList(5);
    }

    public void putname(String the_name) {
        m_name = the_name;
    }

    public String getname() {
        return m_name;
    }

    public int getSegmentCount() {
        return m_SegmentList.size();
    }

    public Segment getSegment(int the_index) {
        if ((the_index >= 0) && (the_index < m_SegmentList.size())) {
            return (Segment) m_SegmentList.get(the_index);
        } else {
            return null;
        }
    }

    public void addSegment(Segment the_Segment) {
        m_SegmentList.add(the_Segment);
    }

    public ArrayList getSegmentList() {
        return m_SegmentList;
    }

    public void Display() {
        System.out.println("\nCDS**********");
        System.out.println(" name\t<" + m_name + ">");
        if (m_SegmentList.size() > 0) {
            System.out.println(" Segments:");
            for (int i = 0; i < m_SegmentList.size(); i++) {
                ((Segment) getSegment(i)).Display();
            }
        }
    }
}
