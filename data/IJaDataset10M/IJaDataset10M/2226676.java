package com.cjssolutions.plex.service.preferences;

import java.util.Vector;
import java.util.prefs.Preferences;
import ObRun.ObFunction.ObFunction;
import ObRun.ObFunction.ObVariableGroup;
import ObRun.ObFunction.ObVariableGroupX;
import ObRun.ObFunction.ObVariableX;
import ObRun.ObRTTypes.ObCharFld;
import ObRun.ObRTTypes.ObDblFld;
import ObRun.ObRTTypes.ObField;
import ObRun.ObRTTypes.ObIntFld;
import ObRun.ObRTTypes.ObLongDblFld;
import ObRun.ObRTTypes.ObLongFld;
import ObRun.ObRTTypes.ObObjFld;

public final class CJ11F_ObFnc extends ObFunction {

    final class CJ11F_ObDat extends ObVariableGroupX {

        public CJ11F_ObDat() {
            super(ObVariableGroup.VT_LOCAL, null);
            addVariable("CJ11F_PreferenceStoreL", initvarCJ11F_CJ11F_PreferenceStoreL());
            addVariable("CJ11F_PreferenceDefaultL", initvarCJ11F_CJ11F_PreferenceDefaultL());
            addVariable("CJ11F_Environment", initvarCJ11F_CJ11F_Environment());
            addVariable("CJ11F_View", initvarCJ11F_CJ11F_View());
        }

        public CJ11F_ObDat(ObFunction fnc) {
            super(ObVariableGroup.VT_LOCAL, fnc);
            addVariable("CJ11F_PreferenceStoreL", initvarCJ11F_CJ11F_PreferenceStoreL());
            addVariable("CJ11F_PreferenceDefaultL", initvarCJ11F_CJ11F_PreferenceDefaultL());
            addVariable("CJ11F_Environment", initvarCJ11F_CJ11F_Environment());
            addVariable("CJ11F_View", initvarCJ11F_CJ11F_View());
        }

        public ObVariableX initvarCJ11F_CJ11F_Environment() {
            ObVariableX var = new ObVariableX(this, "varCJ11F_CJ11F_Environment");
            var.addField("Sqmx1ac", new ObCharFld(ObField.ENVIRONMENT, 1, 'c', "Sqmx1ac", false, false, false, ""));
            var.addField("Sqmx1bm", new ObCharFld(ObField.ENVIRONMENT, 32, 'c', "Sqmx1bm", false, false, false, ""));
            var.addField("Sqmx1bo", new ObCharFld(ObField.ENVIRONMENT, 32, 'c', "Sqmx1bo", false, false, false, ""));
            var.addField("Sqmx1c9", new ObCharFld(ObField.ENVIRONMENT, 512, 'c', "Sqmx1c9", false, false, false, ""));
            var.addField("Sqmx1e9", new ObCharFld(ObField.ENVIRONMENT, 7, 'c', "Sqmx1e9", false, false, false, ""));
            var.addField("Sqmx1ed", new ObCharFld(ObField.ENVIRONMENT, 256, 'c', "Sqmx1ed", false, true, false, ""));
            var.addField("Sqmx1eq", new ObCharFld(ObField.ENVIRONMENT, 1, 'c', "Sqmx1eq", false, false, false, ""));
            var.addField("Sqmx1eu", new ObCharFld(ObField.ENVIRONMENT, 128, 'c', "Sqmx1eu", false, true, false, ""));
            var.addField("Sqmx1g5", new ObCharFld(ObField.ENVIRONMENT, 32, 'c', "Sqmx1g5", false, false, false, ""));
            var.addField("Sqmx0r4", new ObCharFld(ObField.ENVIRONMENT, 7, 'c', "Sqmx0r4", false, false, false, ""));
            var.addField("Sqmx0r6", new ObCharFld(ObField.ENVIRONMENT, 7, 'c', "Sqmx0r6", false, false, false, ""));
            var.addField("Sqmx0r8", new ObCharFld(ObField.ENVIRONMENT, 7, 'c', "Sqmx0r8", false, false, false, ""));
            var.addField("Sqmx0ra", new ObCharFld(ObField.ENVIRONMENT, 7, 'c', "Sqmx0ra", false, false, false, ""));
            var.addField("Sqmx0rc", new ObCharFld(ObField.ENVIRONMENT, 7, 'c', "Sqmx0rc", false, false, false, ""));
            var.addField("Sqmx0re", new ObCharFld(ObField.ENVIRONMENT, 7, 'c', "Sqmx0re", false, false, false, ""));
            var.addField("Sqmx0rg", new ObCharFld(ObField.ENVIRONMENT, 7, 'c', "Sqmx0rg", false, false, false, ""));
            var.addField("Sqmx0s3", new ObCharFld(ObField.ENVIRONMENT, 1, 'c', "Sqmx0s3", false, false, false, ""));
            var.addField("Sqmx0xh", new ObCharFld(ObField.ENVIRONMENT, 32, 'c', "Sqmx0xh", false, false, false, ""));
            return var;
        }

        public ObVariableX initvarCJ11F_CJ11F_PreferenceDefaultL() {
            ObVariableX var = new ObVariableX(this, "varCJ11F_CJ11F_PreferenceDefaultL");
            var.addField("CJuA", new ObCharFld(ObField.LOCAL, 1, 'c', "CJuA", false, false, false, ""));
            var.addField("CJvA", new ObLongDblFld(ObField.LOCAL, 19, 9, 'p', "CJvA", ""));
            var.addField("CJwA", new ObLongDblFld(ObField.LOCAL, 19, 7, 'p', "CJwA", ""));
            var.addField("CJxA", new ObIntFld(ObField.LOCAL, 5, 0, 'p', "CJxA", ""));
            var.addField("CJyA", new ObLongFld(ObField.LOCAL, 10, 0, 'p', "CJyA", ""));
            var.addField("CJzA", new ObCharFld(ObField.LOCAL, 1024, 'c', "CJzA", true, true, false, ""));
            return var;
        }

        public ObVariableX initvarCJ11F_CJ11F_PreferenceStoreL() {
            ObVariableX var = new ObVariableX(this, "varCJ11F_CJ11F_PreferenceStoreL");
            var.addField("CJeA", new ObObjFld(ObField.LOCAL, 0, 0, 'j', "CJeA", ""));
            var.addField("CJaA", new ObCharFld(ObField.LOCAL, 32, 'c', "CJaA", false, true, false, ""));
            var.addField("CJ9A", new ObCharFld(ObField.LOCAL, 80, 'c', "CJ9A", true, true, false, ""));
            var.addField("S5trhs2", new ObCharFld(ObField.LOCAL, 1024, 'c', "S5trhs2", true, true, false, ""));
            return var;
        }

        public ObVariableX initvarCJ11F_CJ11F_View() {
            ObVariableX var = new ObVariableX(this, "varCJ11F_CJ11F_View");
            var.addField("CJeA", new ObObjFld(ObField.VIEW_KEY, 0, 0, 'j', "CJeA", ""));
            var.addField("CJ9A", new ObCharFld(ObField.VIEW_KEY, 80, 'c', "CJ9A", true, true, false, ""));
            var.addField("CJaA", new ObCharFld(ObField.VIEW_COL, 32, 'c', "CJaA", false, true, false, ""));
            var.addField("CJbA", new ObCharFld(ObField.VIEW_COL, 1024, 'c', "CJbA", true, true, false, ""));
            var.addField("CJiA", new ObCharFld(ObField.VIEW_COL, 1, 'c', "CJiA", false, false, false, ""));
            var.addField("CJlA", new ObLongDblFld(ObField.VIEW_COL, 19, 9, 'p', "CJlA", ""));
            var.addField("CJmA", new ObLongDblFld(ObField.VIEW_COL, 19, 7, 'p', "CJmA", ""));
            var.addField("CJkA", new ObIntFld(ObField.VIEW_COL, 5, 0, 'p', "CJkA", ""));
            var.addField("CJjA", new ObLongFld(ObField.VIEW_COL, 10, 0, 'p', "CJjA", ""));
            return var;
        }
    }

    ObCharFld m_obvalfld_10 = new ObCharFld("1", 1);

    ObCharFld m_obvalfld_12 = new ObCharFld("java.lang.Long", 32);

    ObCharFld m_obvalfld_4 = new ObCharFld("0", 1);

    ObIntFld m_obvalfld_6 = new ObIntFld(0);

    ObCharFld m_obvalfld_2 = new ObCharFld("ERR", 7);

    ObCharFld m_obvalfld_1 = new ObCharFld("INF", 7);

    ObCharFld m_obvalfld_14 = new ObCharFld("java.lang.Float", 32);

    ObCharFld m_obvalfld_9 = new ObCharFld("java.lang.Boolean", 32);

    ObLongFld m_obvalfld_7 = new ObLongFld(0);

    ObCharFld m_obvalfld_8 = new ObCharFld(" ", 1024);

    ObLongDblFld m_obvalfld_5 = new ObLongDblFld(0);

    ObCharFld m_obvalfld_0 = new ObCharFld(" ", 7);

    ObCharFld m_obvalfld_13 = new ObCharFld("java.lang.Double", 32);

    ObCharFld m_obvalfld_11 = new ObCharFld("java.lang.Integer", 32);

    ObCharFld m_obvalfld_3 = new ObCharFld("java.lang.String", 32);

    public final String m_strFldSqmx0r8 = "Sqmx0r8";

    public final String m_strVarCJ11F_PreferenceStoreL = "CJ11F_PreferenceStoreL";

    public final String m_strVarS5trhs2 = "S5trhs2";

    public final String m_strVarCJ11F_Environment = "CJ11F_Environment";

    public final String m_strVarCJmA = "CJmA";

    public final String m_strVarCJlA = "CJlA";

    public final String m_strVarCJ9A = "CJ9A";

    public final String m_strVarCJkA = "CJkA";

    public final String m_strVarCJjA = "CJjA";

    public final String m_strVarCJiA = "CJiA";

    public final String m_strVarCJ11F_View = "CJ11F_View";

    public final String m_strVarCJzA = "CJzA";

    public final String m_strVarCJyA = "CJyA";

    public final String m_strVarCJxA = "CJxA";

    public final String m_strVarCJwA = "CJwA";

    public final String m_strVarSqmx0r4 = "Sqmx0r4";

    public final String m_strVarCJeA = "CJeA";

    public final String m_strVarCJvA = "CJvA";

    public final String m_strVarCJ11F_PreferenceDefaultL = "CJ11F_PreferenceDefaultL";

    public final String m_strVarCJuA = "CJuA";

    public final String m_strFldSqmx0rg = "Sqmx0rg";

    public final String m_strVarSqmx0r6 = "Sqmx0r6";

    public final String m_strVarCJbA = "CJbA";

    public final String m_strFldSqmx0r6 = "Sqmx0r6";

    public final String m_strVarCJ11F_CheckKey = "CJ11F_CheckKey";

    public final String m_strVarCJaA = "CJaA";

    public final String m_strVarSqmx0r8 = "Sqmx0r8";

    public CJ11F_ObFnc() {
        CJ11F_ObDat varObDat = new CJ11F_ObDat(this);
        super.setLocalVariable(varObDat);
        setFunctionName("JavaPreferences.Data.PreferenceStore.Fetch.CheckRow");
        setDBConnectionName("", false);
        setVioKeys();
    }

    public ObVariableGroup getDatVariable() {
        if (m_locVariable == null) {
            m_locVariable = new CJ11F_ObDat(this);
        }
        return m_locVariable;
    }

    public ObVariableGroup getInVariable() {
        if (m_in == null) {
            m_in = new CJ11F_ObIn(this);
        }
        return m_in;
    }

    public ObVariableGroup getOutVariable() {
        if (m_out == null) {
            m_out = new CJ11F_ObOut(this);
        }
        return m_out;
    }

    public ObCharFld getReturningStatus() {
        return (ObCharFld) (((CJ11F_ObDat) getDatVariable()).getVariable(m_strVarCJ11F_Environment).getField(m_strFldSqmx0r8));
    }

    public void ObRun() {
        CJ11F_ObDat v;
        CJ11F_ObIn in;
        CJ11F_ObOut out;
        ObFunction fnc = this;
        v = (CJ11F_ObDat) fnc.getDatVariable();
        in = (CJ11F_ObIn) fnc.getInVariable();
        out = (CJ11F_ObOut) fnc.getOutVariable();
        String sqlString;
        String whereString;
        Vector keyFields;
        {
            {
            }
            {
            }
            {
            }
            {
                {
                }
                {
                }
                {
                }
            }
        }
        ObSbr_CJ11F1();
        if (hasReturned()) {
            return;
        }
        {
            ObSbr_CJ11F9();
            if (hasReturned()) {
                return;
            }
        }
        {
        }
        {
            ObSbr_CJ11F2();
            if (hasReturned()) {
                return;
            }
        }
        {
        }
        {
        }
        {
        }
    }

    void ObSbr_CJ11F1() {
        CJ11F_ObDat v;
        CJ11F_ObIn in;
        CJ11F_ObOut out;
        ObFunction fnc = this;
        v = (CJ11F_ObDat) fnc.getDatVariable();
        in = (CJ11F_ObIn) fnc.getInVariable();
        out = (CJ11F_ObOut) fnc.getOutVariable();
        String sqlString;
        String whereString;
        Vector keyFields;
        {
            v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r8).assign(m_obvalfld_0);
        }
        {
        }
        {
        }
        {
        }
        {
        }
        {
        }
        {
        }
    }

    void ObSbr_CJ11F2() {
        CJ11F_ObDat v;
        CJ11F_ObIn in;
        CJ11F_ObOut out;
        ObFunction fnc = this;
        v = (CJ11F_ObDat) fnc.getDatVariable();
        in = (CJ11F_ObIn) fnc.getInVariable();
        out = (CJ11F_ObOut) fnc.getOutVariable();
        String sqlString;
        String whereString;
        Vector keyFields;
        {
        }
        {
        }
        {
        }
        setReturn();
        if (true) {
            return;
        }
    }

    void ObSbr_CJ11F3() {
        CJ11F_ObDat v;
        CJ11F_ObIn in;
        CJ11F_ObOut out;
        ObFunction fnc = this;
        v = (CJ11F_ObDat) fnc.getDatVariable();
        in = (CJ11F_ObIn) fnc.getInVariable();
        out = (CJ11F_ObOut) fnc.getOutVariable();
        String sqlString;
        String whereString;
        Vector keyFields;
        {
        }
        {
        }
        {
        }
        {
        }
        {
        }
        {
        }
    }

    void ObSbr_CJ11F4() {
        CJ11F_ObDat v;
        CJ11F_ObIn in;
        CJ11F_ObOut out;
        ObFunction fnc = this;
        v = (CJ11F_ObDat) fnc.getDatVariable();
        in = (CJ11F_ObIn) fnc.getInVariable();
        out = (CJ11F_ObOut) fnc.getOutVariable();
        String sqlString;
        String whereString;
        Vector keyFields;
        {
        }
        {
        }
        {
        }
        {
            v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_0);
            try {
                Class pclass = Key.getPrefType(v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJ9A).getValue());
                if (pclass == null) {
                    v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_1);
                } else {
                    v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJaA).assign(new ObCharFld(pclass.getName()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_2);
            }
        }
        if (v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).isEqual(m_obvalfld_1)) {
            v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJaA).assign(m_obvalfld_3);
        }
        {
        }
        {
        }
        {
        }
    }

    void ObSbr_CJ11F5() {
        CJ11F_ObDat v;
        CJ11F_ObIn in;
        CJ11F_ObOut out;
        ObFunction fnc = this;
        v = (CJ11F_ObDat) fnc.getDatVariable();
        in = (CJ11F_ObIn) fnc.getInVariable();
        out = (CJ11F_ObOut) fnc.getOutVariable();
        String sqlString;
        String whereString;
        Vector keyFields;
        {
        }
        {
        }
        {
        }
        v.getVariable(m_strVarCJ11F_PreferenceDefaultL).getAsObCharFldField(m_strVarCJuA).assign(m_obvalfld_4);
        v.getVariable(m_strVarCJ11F_PreferenceDefaultL).getAsObLongDblFldField(m_strVarCJvA).assign(m_obvalfld_5);
        v.getVariable(m_strVarCJ11F_PreferenceDefaultL).getAsObLongDblFldField(m_strVarCJwA).assign(m_obvalfld_5);
        v.getVariable(m_strVarCJ11F_PreferenceDefaultL).getAsObIntFldField(m_strVarCJxA).assign(m_obvalfld_6);
        v.getVariable(m_strVarCJ11F_PreferenceDefaultL).getAsObLongFldField(m_strVarCJyA).assign(m_obvalfld_7);
        v.getVariable(m_strVarCJ11F_PreferenceDefaultL).getAsObCharFldField(m_strVarCJzA).assign(m_obvalfld_8);
        {
        }
        {
        }
        {
        }
    }

    void ObSbr_CJ11F6() {
        CJ11F_ObDat v;
        CJ11F_ObIn in;
        CJ11F_ObOut out;
        ObFunction fnc = this;
        v = (CJ11F_ObDat) fnc.getDatVariable();
        in = (CJ11F_ObIn) fnc.getInVariable();
        out = (CJ11F_ObOut) fnc.getOutVariable();
        String sqlString;
        String whereString;
        Vector keyFields;
        {
        }
        {
        }
        {
        }
        ObSbr_CJ11F4();
        if (hasReturned()) {
            return;
        }
        {
            if (v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJaA).isEqual(m_obvalfld_9)) {
                {
                    v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_0);
                    try {
                        Preferences pref = (Preferences) v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObObjFldField(m_strVarCJeA).getValue();
                        String key = v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJ9A).getValue();
                        boolean dft = v.getVariable(m_strVarCJ11F_PreferenceDefaultL).getAsObCharFldField(m_strVarCJuA).getValue().equals(m_obvalfld_10.getValue());
                        boolean value = pref.getBoolean(key, dft);
                        if (value) {
                            v.getVariable(m_strVarCJ11F_View).getAsObCharFldField(m_strVarCJiA).assign(m_obvalfld_10);
                        } else {
                            v.getVariable(m_strVarCJ11F_View).getAsObCharFldField(m_strVarCJiA).assign(m_obvalfld_4);
                        }
                        v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarS5trhs2).assign(new ObCharFld(Boolean.toString(value)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_2);
                    }
                }
            } else if (v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJaA).isEqual(m_obvalfld_11)) {
                {
                    v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_0);
                    try {
                        Preferences pref = (Preferences) v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObObjFldField(m_strVarCJeA).getValue();
                        String key = v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJ9A).getValue();
                        int dft = v.getVariable(m_strVarCJ11F_PreferenceDefaultL).getAsObIntFldField(m_strVarCJxA).getValue();
                        int value = pref.getInt(key, dft);
                        v.getVariable(m_strVarCJ11F_View).getAsObIntFldField(m_strVarCJkA).assign(new ObIntFld(value));
                        v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarS5trhs2).assign(new ObCharFld(Integer.toString(value)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_2);
                    }
                }
            } else if (v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJaA).isEqual(m_obvalfld_12)) {
                {
                    v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_0);
                    try {
                        Preferences pref = (Preferences) v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObObjFldField(m_strVarCJeA).getValue();
                        String key = v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJ9A).getValue();
                        long dft = v.getVariable(m_strVarCJ11F_PreferenceDefaultL).getAsObLongFldField(m_strVarCJyA).getValue();
                        long value = pref.getLong(key, dft);
                        v.getVariable(m_strVarCJ11F_View).getAsObLongFldField(m_strVarCJjA).assign(new ObLongFld(value));
                        v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarS5trhs2).assign(new ObCharFld(Long.toString(value)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_2);
                    }
                }
            } else if (v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJaA).isEqual(m_obvalfld_13)) {
                {
                    v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_0);
                    try {
                        Preferences pref = (Preferences) v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObObjFldField(m_strVarCJeA).getValue();
                        String key = v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJ9A).getValue();
                        double dft = v.getVariable(m_strVarCJ11F_PreferenceDefaultL).getAsObLongDblFldField(m_strVarCJvA).getValue();
                        double value = pref.getDouble(key, dft);
                        v.getVariable(m_strVarCJ11F_View).getAsObLongDblFldField(m_strVarCJlA).assign(new ObDblFld(value));
                        v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarS5trhs2).assign(new ObCharFld(Double.toString(value)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_2);
                    }
                }
            } else if (v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJaA).isEqual(m_obvalfld_14)) {
                {
                    v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_0);
                    try {
                        Preferences pref = (Preferences) v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObObjFldField(m_strVarCJeA).getValue();
                        String key = v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJ9A).getValue();
                        float dft = (float) v.getVariable(m_strVarCJ11F_PreferenceDefaultL).getAsObLongDblFldField(m_strVarCJwA).getValue();
                        float value = pref.getFloat(key, dft);
                        v.getVariable(m_strVarCJ11F_View).getAsObLongDblFldField(m_strVarCJmA).assign(new ObDblFld(value));
                        v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarS5trhs2).assign(new ObCharFld(Float.toString(value)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_2);
                    }
                }
            } else {
                {
                    v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_0);
                    try {
                        Preferences pref = (Preferences) v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObObjFldField(m_strVarCJeA).getValue();
                        String key = v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJ9A).getValue();
                        String dft = v.getVariable(m_strVarCJ11F_PreferenceDefaultL).getAsObCharFldField(m_strVarCJzA).getValue();
                        String value = pref.get(key, dft);
                        v.getVariable(m_strVarCJ11F_View).getAsObCharFldField(m_strVarCJbA).assign(new ObCharFld(value));
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_2);
                    }
                }
            }
        }
        if (v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).isEqual(m_obvalfld_0)) {
            v.getVariable(m_strVarCJ11F_View).getAsObObjFldField(m_strVarCJeA).assign(v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObObjFldField(m_strVarCJeA));
            v.getVariable(m_strVarCJ11F_View).getAsObCharFldField(m_strVarCJ9A).assign(v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJ9A));
            v.getVariable(m_strVarCJ11F_View).getAsObCharFldField(m_strVarCJaA).assign(v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJaA));
            if (v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJaA).isNotEqual(m_obvalfld_3)) {
                v.getVariable(m_strVarCJ11F_View).getAsObCharFldField(m_strVarCJbA).assign(v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarS5trhs2));
            }
        }
        {
        }
        {
        }
        {
        }
    }

    void ObSbr_CJ11F7() {
        CJ11F_ObDat v;
        CJ11F_ObIn in;
        CJ11F_ObOut out;
        ObFunction fnc = this;
        v = (CJ11F_ObDat) fnc.getDatVariable();
        in = (CJ11F_ObIn) fnc.getInVariable();
        out = (CJ11F_ObOut) fnc.getOutVariable();
        String sqlString;
        String whereString;
        Vector keyFields;
        {
        }
        {
        }
        {
        }
        v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJ9A).assign(v.getVariable(m_strVarCJ11F_View).getAsObCharFldField(m_strVarCJ9A));
        ObSbr_CJ11F4();
        if (hasReturned()) {
            return;
        }
        {
            if (v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJaA).isEqual(m_obvalfld_9)) {
                {
                    v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_0);
                    try {
                        Preferences pref = (Preferences) v.getVariable(m_strVarCJ11F_View).getAsObObjFldField(m_strVarCJeA).getValue();
                        String _key = v.getVariable(m_strVarCJ11F_View).getAsObCharFldField(m_strVarCJ9A).getValue();
                        String _boolean = v.getVariable(m_strVarCJ11F_View).getAsObCharFldField(m_strVarCJiA).getValue();
                        String _true = m_obvalfld_10.getValue();
                        boolean _value = _boolean.equals(_true);
                        pref.putBoolean(_key, _value);
                        v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarS5trhs2).assign(new ObCharFld(Boolean.toString(_value)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_2);
                    }
                }
            } else if (v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJaA).isEqual(m_obvalfld_11)) {
                {
                    v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_0);
                    try {
                        Preferences pref = (Preferences) v.getVariable(m_strVarCJ11F_View).getAsObObjFldField(m_strVarCJeA).getValue();
                        String _key = v.getVariable(m_strVarCJ11F_View).getAsObCharFldField(m_strVarCJ9A).getValue();
                        int _value = v.getVariable(m_strVarCJ11F_View).getAsObIntFldField(m_strVarCJkA).getValue();
                        pref.putInt(_key, _value);
                        v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarS5trhs2).assign(new ObCharFld(Integer.toString(_value)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_2);
                    }
                }
            } else if (v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJaA).isEqual(m_obvalfld_12)) {
                {
                    v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_0);
                    try {
                        Preferences pref = (Preferences) v.getVariable(m_strVarCJ11F_View).getAsObObjFldField(m_strVarCJeA).getValue();
                        String _key = v.getVariable(m_strVarCJ11F_View).getAsObCharFldField(m_strVarCJ9A).getValue();
                        long _value = v.getVariable(m_strVarCJ11F_View).getAsObLongFldField(m_strVarCJjA).getValue();
                        pref.putLong(_key, _value);
                        v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarS5trhs2).assign(new ObCharFld(Long.toString(_value)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_2);
                    }
                }
            } else if (v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJaA).isEqual(m_obvalfld_13)) {
                {
                    v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_0);
                    try {
                        Preferences pref = (Preferences) v.getVariable(m_strVarCJ11F_View).getAsObObjFldField(m_strVarCJeA).getValue();
                        String _key = v.getVariable(m_strVarCJ11F_View).getAsObCharFldField(m_strVarCJ9A).getValue();
                        double _value = v.getVariable(m_strVarCJ11F_View).getAsObLongDblFldField(m_strVarCJlA).getValue();
                        pref.putDouble(_key, _value);
                        v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarS5trhs2).assign(new ObCharFld(Double.toString(_value)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_2);
                    }
                }
            } else if (v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJaA).isEqual(m_obvalfld_14)) {
                {
                    v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_0);
                    try {
                        Preferences pref = (Preferences) v.getVariable(m_strVarCJ11F_View).getAsObObjFldField(m_strVarCJeA).getValue();
                        String _key = v.getVariable(m_strVarCJ11F_View).getAsObCharFldField(m_strVarCJ9A).getValue();
                        float _value = (float) v.getVariable(m_strVarCJ11F_View).getAsObLongDblFldField(m_strVarCJmA).getValue();
                        pref.putFloat(_key, _value);
                        v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarS5trhs2).assign(new ObCharFld(Float.toString(_value)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_2);
                    }
                }
            } else {
                {
                    v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_0);
                    try {
                        Preferences pref = (Preferences) v.getVariable(m_strVarCJ11F_View).getAsObObjFldField(m_strVarCJeA).getValue();
                        String _key = v.getVariable(m_strVarCJ11F_View).getAsObCharFldField(m_strVarCJ9A).getValue();
                        String _value = v.getVariable(m_strVarCJ11F_View).getAsObCharFldField(m_strVarCJbA).getValue();
                        pref.put(_key, _value);
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r6).assign(m_obvalfld_2);
                    }
                }
            }
        }
        {
        }
        {
        }
        {
        }
    }

    void ObSbr_CJ11F8() {
        CJ11F_ObDat v;
        CJ11F_ObIn in;
        CJ11F_ObOut out;
        ObFunction fnc = this;
        v = (CJ11F_ObDat) fnc.getDatVariable();
        in = (CJ11F_ObIn) fnc.getInVariable();
        out = (CJ11F_ObOut) fnc.getOutVariable();
        String sqlString;
        String whereString;
        Vector keyFields;
        {
        }
        {
        }
        {
        }
        {
            v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r4).assign(m_obvalfld_1);
            try {
                Preferences pref = (Preferences) v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObObjFldField(m_strVarCJeA).getValue();
                String _key = v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJ9A).getValue();
                String[] list = pref.keys();
                for (int i = 0; i < list.length; i++) {
                    if (_key.equals(list[i])) {
                        v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r4).assign(m_obvalfld_0);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r4).assign(m_obvalfld_2);
            }
        }
        {
        }
        {
        }
        {
        }
    }

    void ObSbr_CJ11F9() {
        CJ11F_ObDat v;
        CJ11F_ObIn in;
        CJ11F_ObOut out;
        ObFunction fnc = this;
        v = (CJ11F_ObDat) fnc.getDatVariable();
        in = (CJ11F_ObIn) fnc.getInVariable();
        out = (CJ11F_ObOut) fnc.getOutVariable();
        String sqlString;
        String whereString;
        Vector keyFields;
        {
        }
        {
        }
        {
            v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObObjFldField(m_strVarCJeA).assign(in.getVariable(m_strVarCJ11F_CheckKey).getAsObObjFldField(m_strVarCJeA));
            v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJ9A).assign(in.getVariable(m_strVarCJ11F_CheckKey).getAsObCharFldField(m_strVarCJ9A));
            {
                v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r4).assign(m_obvalfld_1);
                try {
                    Preferences pref = (Preferences) v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObObjFldField(m_strVarCJeA).getValue();
                    String _key = v.getVariable(m_strVarCJ11F_PreferenceStoreL).getAsObCharFldField(m_strVarCJ9A).getValue();
                    String[] list = pref.keys();
                    for (int i = 0; i < list.length; i++) {
                        if (_key.equals(list[i])) {
                            v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r4).assign(m_obvalfld_0);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r4).assign(m_obvalfld_2);
                }
            }
            v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r8).assign(v.getVariable(m_strVarCJ11F_Environment).getAsObCharFldField(m_strVarSqmx0r4));
        }
        {
        }
        {
        }
        {
        }
    }

    public void setCallStatus(ObCharFld status) {
        ((CJ11F_ObDat) getDatVariable()).getVariable(m_strVarCJ11F_Environment).getField(m_strFldSqmx0rg).assign(status);
    }

    public void setReturnedStatus(ObCharFld returned) {
        ((CJ11F_ObDat) getDatVariable()).getVariable(m_strVarCJ11F_Environment).getField(m_strFldSqmx0r6).assign(returned);
    }

    public void setReturningStatus(ObCharFld returning) {
        ((CJ11F_ObDat) getDatVariable()).getVariable(m_strVarCJ11F_Environment).getField(m_strFldSqmx0r8).assign(returning);
    }

    public void setVioKeys() {
        Vector keySequence = new Vector();
        keySequence.addElement("CJeA");
        keySequence.addElement("CJ9A");
        setKeySequencex(keySequence);
    }
}
