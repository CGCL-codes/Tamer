package jmud;

import jmud.util.log.Log;
import jmud.util.StrUtil;

class RandAttr {

    static final int T_ALL = 0;

    static final int T_ATTR = 1;

    static final int T_ROW = 2;

    static final int T_COL = 3;

    static final String targetNames[] = { "todos", "atributo", "linha", "coluna" };

    static final int M_CHANGE = 0;

    static final int M_ADD = 1;

    static final int M_SUB = 2;

    static final String modeNames[] = { "mudar", "somar", "subtrair" };

    private int target;

    private int which;

    private int value;

    private int mode;

    RandAttr(int t, int w, int v, int m) {
        target = t;
        which = w;
        value = v;
        mode = m;
    }

    int getTarget() {
        return target;
    }

    int getWhich() {
        return which;
    }

    int getValue() {
        return value;
    }

    int getMode() {
        return mode;
    }

    String getSheet() {
        return StrUtil.rightPad(targetNames[target], 8) + " " + StrUtil.formatNumber(which, 3) + " " + StrUtil.rightPad(modeNames[mode], 8) + " " + StrUtil.formatNumber(value, 3);
    }
}
