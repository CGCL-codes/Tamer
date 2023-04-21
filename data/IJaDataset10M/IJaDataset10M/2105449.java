package com.test.util;

public class VouPage {

    public static boolean vouPage(String startNo, String endNo, String pageNo) {
        int istartNo = 0;
        int iendNo = 0;
        int ipageNo = 0;
        try {
            istartNo = Integer.parseInt(startNo);
            iendNo = Integer.parseInt(endNo);
            ipageNo = Integer.parseInt(pageNo);
        } catch (Exception exp) {
            return false;
        }
        if (istartNo > iendNo) {
            return false;
        }
        if ((istartNo % ipageNo != 0) && (istartNo % ipageNo != 1)) {
            return false;
        }
        if ((iendNo % ipageNo != 0) && (iendNo % ipageNo != 1)) {
            return false;
        }
        if ((iendNo - istartNo + 1) % ipageNo != 0) {
            return false;
        }
        return true;
    }
}
