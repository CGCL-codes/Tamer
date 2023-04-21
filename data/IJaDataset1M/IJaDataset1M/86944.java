package csa.jportal.script;

import javax.swing.JOptionPane;
import java.util.*;

public class ScriptDataPool {

    public static final String DEFAULT_XML_NAME = new String("ScriptData.xml");

    private String mFileName = DEFAULT_XML_NAME;

    private HashMap<String, ScriptData> mScriptData = new HashMap<String, ScriptData>();

    private HashMap<String, String> mKlassenMap = new HashMap<String, String>();

    public ScriptDataPool(String name) {
        mFileName = name;
        init();
    }

    public ScriptDataPool() {
        init();
    }

    public void setFilename(String n) {
        mFileName = n;
    }

    private boolean init() {
        try {
            return load();
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Load Error ScriptData...", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
    }

    public boolean load() {
        java.io.File f = new java.io.File(csa.Global.mBaseDir + mFileName);
        if (!f.exists()) return false;
        mScriptData = ScriptData.getHashMapFromXML(mFileName);
        return true;
    }

    public void save() {
        ScriptData.saveCollectionAsXML(mFileName, mScriptData.values());
        buildKlassenMap();
    }

    public void remove(ScriptData st) {
        mScriptData.remove(st.mName);
    }

    public void put(ScriptData st) {
        mScriptData.remove(st.mName);
        mScriptData.put(st.mName, st);
    }

    public void putAsNew(ScriptData st) {
        mScriptData.put(st.mName, st);
    }

    public ScriptData get(String key) {
        return mScriptData.get(key);
    }

    public HashMap<String, ScriptData> getHashMap() {
        return mScriptData;
    }

    private void buildKlassenMap() {
        mKlassenMap = new HashMap<String, String>();
        Set entries = mScriptData.entrySet();
        Iterator it = entries.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            ScriptData value = (ScriptData) entry.getValue();
            mKlassenMap.put(value.mClass, value.mClass);
        }
    }

    public HashMap<String, String> getKlassenHashMap() {
        buildKlassenMap();
        return mKlassenMap;
    }

    public HashMap<String, ScriptData> getMapForKlasse(String klasse) {
        HashMap<String, ScriptData> ret = new HashMap<String, ScriptData>();
        Set entries = mScriptData.entrySet();
        Iterator it = entries.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            ScriptData value = (ScriptData) entry.getValue();
            if (value.mClass.equalsIgnoreCase(klasse)) {
                ret.put(value.mName, value);
            }
        }
        return ret;
    }
}
