package org.homedns.krolain.MochaJournal.LJData;

import java.lang.String;
import java.util.ArrayList;
import java.lang.Integer;

public class LJMoods extends java.util.Vector implements java.io.Serializable {

    public static class MoodInfo extends java.lang.Object implements java.lang.Comparable, java.io.Serializable {

        public String m_szMoodName = null;

        public int m_iMoodID;

        public int m_iParentID;

        /** Creates a new instance of MoodInfo */
        public MoodInfo() {
            m_szMoodName = new String();
            m_iMoodID = -1;
            m_iParentID = -1;
        }

        public MoodInfo(int iID, String szName) {
            m_szMoodName = new String(szName);
            m_iMoodID = iID;
        }

        public int compareTo(Object obj) {
            if (obj == null) throw (new java.lang.NullPointerException());
            if (obj instanceof MoodInfo) {
                MoodInfo tmp = (MoodInfo) obj;
                int iVal = m_szMoodName.compareTo(tmp.m_szMoodName);
                return iVal;
            } else if (obj instanceof String) {
                String szKey = (String) obj;
                return m_szMoodName.compareTo(szKey);
            } else throw (new java.lang.ClassCastException());
        }

        public String toString() {
            return m_szMoodName;
        }

        public String getMoodName() {
            return m_szMoodName;
        }

        public void setMoodName(String s) {
            m_szMoodName = s;
        }

        public int getMoodID() {
            return m_iMoodID;
        }

        public void setMoodID(int i) {
            m_iMoodID = i;
        }

        public int getParentID() {
            return m_iParentID;
        }

        public void setParentID(int i) {
            m_iParentID = i;
        }
    }

    /** Creates a new instance of LJMoods */
    int m_iMaxMoodID = -1;

    public int getMaxMood() {
        return m_iMaxMoodID;
    }

    public void setMaxMood(int var) {
        m_iMaxMoodID = var;
    }

    public LJMoods() {
        m_iMaxMoodID = 0;
    }

    public void addMood(int iID, String name, int iParent) {
        if (getMoodID(iID) != null) return;
        MoodInfo mood = new MoodInfo();
        mood.m_iMoodID = iID;
        mood.m_iParentID = iParent;
        mood.m_szMoodName = name;
        add(mood);
        if (iID > m_iMaxMoodID) m_iMaxMoodID = iID;
    }

    public void sort() {
        Object[] obj = toArray();
        java.util.Arrays.sort(obj);
        removeAllElements();
        addAll(java.util.Arrays.asList(obj));
    }

    public MoodInfo[] getMoodList() {
        MoodInfo[] info = new MoodInfo[elementCount];
        for (int i = 0; i < elementCount; i++) info[i] = (MoodInfo) get(i);
        return info;
    }

    public int getMaxMoodID() {
        return m_iMaxMoodID;
    }

    public MoodInfo getMoodID(int iMoodID) {
        int iSize = size();
        for (int i = 0; i < iSize; i++) {
            if (((MoodInfo) get(i)).m_iMoodID == iMoodID) return (MoodInfo) get(i);
        }
        return null;
    }
}
