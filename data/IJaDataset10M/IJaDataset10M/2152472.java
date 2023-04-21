package nts.command;

import nts.base.BoolPar;
import nts.io.Loggable;
import nts.io.MaxLoggable;

public class ScanToksChecker extends BaseToksChecker {

    protected final String desc;

    protected final MaxLoggable list;

    protected final Token insTok;

    public ScanToksChecker(String tokErr, String eofErr, String desc, MaxLoggable list, Loggable src, Token ins) {
        super(tokErr, eofErr, src);
        this.desc = desc;
        this.list = list;
        insTok = ins;
    }

    public ScanToksChecker(String tokErr, String eofErr, String desc, MaxLoggable list, Loggable src) {
        super(tokErr, eofErr, src);
        this.desc = desc;
        this.list = list;
        insTok = RightBraceToken.TOKEN;
    }

    protected void tryToFix() {
        Command.insertTokenWithoutCleaning(insTok);
    }

    protected void reportRunAway() {
        Command.runAway(desc, list);
    }
}
