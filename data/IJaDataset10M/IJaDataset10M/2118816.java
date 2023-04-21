package nacaLib.CESM;

import nacaLib.base.*;
import nacaLib.varEx.Var;

public class CESMReadQueue extends CJMapObject {

    protected boolean m_bTransient = false;

    protected String m_Name = "";

    protected CESMQueueManager m_Manager = null;

    protected int m_nRecordPosition = 0;

    public CESMReadQueue(boolean bTransient, String name, CESMQueueManager manager) {
        m_bTransient = bTransient;
        m_Name = name;
        m_Manager = manager;
    }

    public CESMReadQueue nextInto(Var tsZone, Var tsLong) {
        m_Manager.readNextTempQueue(m_Name, tsZone);
        return this;
    }

    public CESMReadQueue nextInto(Var tsZone) {
        m_Manager.readNextTempQueue(m_Name, tsZone);
        return this;
    }

    public CESMReadQueue itemInto(int nIndex, Var varItem) {
        m_Manager.readIndexedTempQueue(m_Name, nIndex, varItem, null);
        return this;
    }

    public CESMReadQueue itemInto(Var varIndex, Var varItem) {
        int nIndex = varIndex.getInt();
        m_Manager.readIndexedTempQueue(m_Name, nIndex, varItem, null);
        return this;
    }

    public CESMReadQueue itemInto(Var varIndex, Var varItem, Var varLength) {
        int nIndex = varIndex.getInt();
        m_Manager.readIndexedTempQueue(m_Name, nIndex, varItem, varLength);
        return this;
    }

    public CESMReadQueue numItem(Var varNbItems) {
        m_Manager.getNbItems(m_Name, varNbItems);
        return this;
    }
}
