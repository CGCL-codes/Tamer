package com.ikaad.mathnotepad.engine.visitor;

public class TypCheckResult {

    public static final int PASS = 0;

    public static final int ERR_UNKNOWN_COMMAND = 1001;

    public static final int ERR_RESERV_WORD_REASSIGN = 2;

    public static final int ERR_UNKNOWN_FUNCTION = 3;

    public static final int ERR_FUNC_ARITY_MISMATCH = 4;

    public static final int ERR_UNKNOWN_ID = 5;

    public static final int ERR_FUNC_DEF = 5;

    public static final int ERR_CMD_PARAM = 1006;

    public static final int ERR_CMD_NOTIMPL = 9999;

    public static final TypCheckResult CHECKED = new TypCheckResult();

    public String str = "";

    private int errnum;

    private TypCheckResult() {
        errnum = PASS;
    }

    public TypCheckResult(int errnum) {
        this.errnum = errnum;
    }

    public TypCheckResult(int errnum, String str) {
        this.errnum = errnum;
        this.str = str;
    }

    public boolean isChecked() {
        return errnum == PASS;
    }

    public int getErrCode() {
        return errnum;
    }

    public String msg() {
        return str;
    }
}
