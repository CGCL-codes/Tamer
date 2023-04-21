package org.maverickdbms.test.basic;

import org.maverickdbms.basic.mvBasicCode;
import org.maverickdbms.basic.mvProgramInfo;
import org.maverickdbms.basic.mvNumber;
import org.maverickdbms.basic.mvString;

public class testALPHA extends mvBasicCode {

    /**
    * Test the alpha function in the mvString class
    * <PRE>
    * </PRE>
    */
    public mvBasicCode run(mvProgramInfo pinfo) {
        String[] test = { "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "abcdefghijklmnopqrstuvwxyz", "01234567890", "abcdef1234567890", "Robert", "J", "Colquhoun", " ", ",", ".", "-", "" };
        mvString ZERO = pinfo.getVar("ZERO");
        ZERO.set(0);
        mvString ONE = pinfo.getVar("ONE");
        ONE.set(1);
        mvString[] result = { ONE, ONE, ZERO, ZERO, ONE, ONE, ONE, ZERO, ZERO, ZERO, ZERO, ZERO };
        mvString A = pinfo.getVar("A");
        mvString B = pinfo.getVar("B");
        for (int i = 0; i < test.length; i++) {
            A.set(test[i]);
            A.ALPHA(B);
            if (B.NE(result[i])) {
                mvString err = pinfo.getVar("ERROR_STRING");
                err.set("Error calculating ALPHA(" + test[i] + ")\n");
                return pinfo.getCode("ERROR_HANDLER");
            }
        }
        return null;
    }
}

;
