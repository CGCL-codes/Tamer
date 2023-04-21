package kr.ac.kaist.swrc.corenet.data.vo;

import kr.ac.kaist.swrc.corenet.data.Arg;
import kr.ac.kaist.swrc.corenet.data.ChiCaseFrame;
import kr.ac.kaist.swrc.corenet.data.Word;
import kr.ac.kaist.swrc.corenet.data.WordSense;

/**
  * <p>ChiVerbFrame.java</p>
  * <p>Provides case frame information for Chinese verbs. The Argument slot in  the Case frame is limited to 4 arguments (N1, N2, N3, N4), 
  * and, basically, user can get information about argument from one argument object.  
  * ChiVerbFrame also provides auxiliary information.
  * <p>Copyright: Copyright (c) 2011</p>
  * <p>Company: SWRC, KAIST., Ltd</p>
  * 
  * @author <a href="mailto:his.barnabas@kaist.ac.kr">Kim, Kyoungryol</a>, <a href="mailto:dhjin@world.kaist.ac.kr">Jin, Duhyeon</a>
  * @version v 1.0 2011. 1. 13.
  */
public class ChiVerbFrame extends ChiCaseFrame {

    int idx;

    Arg arg1;

    Arg arg2;

    Arg arg3;

    Arg arg4;

    String aux;

    String pa;

    ChiWord chi;

    /**
	 * A constructor which creates Chinese verb case frame object
	 * @param idx an index of a case frame in the database
	 * @param chi ChiWord entry of the predicate in the case frame
	 * @param pa the String of the ending auxiliary of the predicate
	 * @param aux the String of an auxiliary of the predicate
	 * @param arg1 a first argument of the case frame
	 * @param arg2 a second argument of the case frame
	 * @param arg3 a third argument of the case frame
	 * @param arg4 a fourth argument of the case frame
	 */
    public ChiVerbFrame(int idx, ChiWord chi, String pa, String aux, Arg arg1, Arg arg2, Arg arg3, Arg arg4) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
        this.aux = aux;
        this.pa = pa;
        this.chi = chi;
        this.idx = idx;
    }

    public int getIdx() {
        return idx;
    }

    public String getAux() {
        return aux;
    }

    public String getPa() {
        return pa;
    }

    @Override
    public Word getPredicate() {
        return chi;
    }

    @Override
    public Arg getArg1() {
        return arg1;
    }

    @Override
    public Arg getArg2() {
        return arg2;
    }

    @Override
    public Arg getArg3() {
        return arg3;
    }

    @Override
    public Arg getArg4() {
        return arg4;
    }
}
