package com.myc.findbugs.plugin;

import edu.umd.cs.findbugs.BugReporter;
import java.util.Locale;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantString;

/**
 * �Ӽ��Actio,Form,Service���Ƿ���SQL�ĵ���.
 *
 * @author Ma Yichao
 */
public class FindServiceSQL extends AbstractFindbugsPlugin {

    /** SQL���ؼ���. ������ĸ����Сд.*/
    private static final String[] SQL_KEY = { "select", "from", "where", "like" };

    public FindServiceSQL(BugReporter br) {
        super(br);
    }

    /** ����Ƿ���Service�� .
     * ���ڼ���Action,form������ж�.
     */
    private boolean isService() {
        String pkname = getPackageName();
        int layerType = FindBugsPluginUtils.getLayerByPackageName(pkname);
        switch(layerType) {
            case FindBugsPluginUtils.FW_LAYER_SERVICE:
            case FindBugsPluginUtils.FW_LAYER_SERVICE_IMPL:
            case FindBugsPluginUtils.FW_LAYER_ACTION:
            case FindBugsPluginUtils.FW_LAYER_FORM:
                return true;
            default:
                return false;
        }
    }

    /** ����Ƿ���SQL���. */
    private boolean isSql(String s) {
        boolean is = false;
        if (s != null) {
            for (String key : SQL_KEY) {
                if (s.toLowerCase(Locale.getDefault()).contains(key)) {
                    is = true;
                    break;
                }
            }
        }
        return is;
    }

    @Override
    public void sawOpcode(int seen) {
        if (isService() && seen == LDC) {
            Constant c = getConstantRefOperand();
            if (c instanceof ConstantString) {
                String s = getStringConstantOperand();
                if (isSql(s)) {
                    reportBug("MYCSOFT_SERVICE_SQL", HIGH_PRIORITY);
                }
            }
        }
    }
}
