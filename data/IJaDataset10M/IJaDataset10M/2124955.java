package net.community.apps.tools.adm.config;

import javax.swing.SwingUtilities;
import net.community.apps.common.BaseMain;
import net.community.chest.CoVariantReturn;

/**
 * <P>Copyright 2009 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Oct 14, 2009 12:05:21 PM
 */
public final class Main extends BaseMain {

    private Main(String... args) {
        super(args);
    }

    @Override
    @CoVariantReturn
    protected MainFrame createMainFrameInstance() throws Exception {
        return new MainFrame(getMainArguments());
    }

    private static final boolean updateDBValue(final String pName, final String colName, final String colVal) {
        final MainFrame f = (MainFrame) getMainFrameInstance();
        if (null == f) return false;
        return f.updateDBValue(pName, colName, colVal);
    }

    public static final String PARAM_VALUE_COL = "paramvalue";

    private static final boolean updateParamValue(final String pName, final String pValue) {
        return updateDBValue(pName, PARAM_VALUE_COL, pValue);
    }

    public static final boolean updateParamValue(final ValueTableEntry vte, final String sVal) {
        if (null == vte) return false;
        final boolean res = updateParamValue(vte.getKey(), sVal);
        if (res) vte.setValue(sVal); else vte.setValue(vte.getOriginalValue());
        return res;
    }

    public static final String PARAM_NAME_COL = "paramname";

    private static final boolean updateParamName(final String pName, final String pValue) {
        return updateDBValue(pName, PARAM_NAME_COL, pValue);
    }

    public static final boolean updateParamName(final ValueTableEntry vte, final String sVal) {
        if (null == vte) return false;
        final boolean res = updateParamName(vte.getKey(), sVal);
        if (res) vte.setKey(sVal);
        return res;
    }

    public static final boolean createNewEntry(final ValueTableEntry vte) {
        final MainFrame f = (MainFrame) getMainFrameInstance();
        if (null == f) return false;
        return f.insertDBConfigValue(vte);
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Main(args));
    }
}
