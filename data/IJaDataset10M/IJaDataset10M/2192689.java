package javasean.GBXML;

import java.io.*;
import java.util.*;

public class GXBioSourceSubtype {

    private Vector m_GXBioSourceSubSourceList;

    public GXBioSourceSubtype() {
    }

    public int getGXBioSourceSubSourceListSize() {
        if (m_GXBioSourceSubSourceList != null) {
            return m_GXBioSourceSubSourceList.size();
        } else {
            return 0;
        }
    }

    public void addGXBioSourceSubSource(GXBioSourceSubSource the_GXBioSourceSubSource) {
        if (m_GXBioSourceSubSourceList == null) {
            m_GXBioSourceSubSourceList = new Vector();
        }
        m_GXBioSourceSubSourceList.add(the_GXBioSourceSubSource);
    }

    public GXBioSourceSubSource getGXBioSourceSubSource(int the_index) {
        if ((the_index >= 0) && (the_index < m_GXBioSourceSubSourceList.size())) {
            return (GXBioSourceSubSource) m_GXBioSourceSubSourceList.get(the_index);
        }
        return null;
    }

    public void setGXBioSourceSubSourceList(Vector the_GXBioSourceSubSourceList) {
        m_GXBioSourceSubSourceList = the_GXBioSourceSubSourceList;
    }

    public Vector getGXBioSourceSubSourceList() {
        return m_GXBioSourceSubSourceList;
    }

    public void removeAllGXBioSourceSubSource() {
        m_GXBioSourceSubSourceList = new Vector();
    }

    public void Display(int the_depth) {
        String spacer = "";
        for (int d = 0; d < the_depth; d++) {
            spacer += "\t";
        }
        System.out.println(spacer + "GXBioSourceSubtype");
        for (int i = 0; i < getGXBioSourceSubSourceListSize(); i++) {
            getGXBioSourceSubSource(i).Display(the_depth + 1);
        }
    }
}
