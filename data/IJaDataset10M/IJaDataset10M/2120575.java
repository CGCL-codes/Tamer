package at.langegger.xlwrap.map.expr;

/**
 * @author dorgon
 *
 */
public abstract class XLExpr2 extends XLExprBase {

    protected XLExpr arg1, arg2;

    /**
	 * constructor
	 */
    public XLExpr2(XLExpr arg1, XLExpr arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    /**
	 * @param e
	 */
    public void setArg1(XLExpr e) {
        this.arg1 = e;
    }

    /**
	 * @param e
	 */
    public void setArg2(XLExpr e) {
        this.arg2 = e;
    }

    /**
	 * @return the e1
	 */
    public XLExpr getArg1() {
        return arg1;
    }

    /**
	 * @return the e2
	 */
    public XLExpr getArg2() {
        return arg2;
    }

    @Override
    public abstract String toString();
}
