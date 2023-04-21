package cn.jsprun.utils;

public class CronsLocker {

    private static CronsLocker cronsLocker = new CronsLocker();

    private CronsLocker() {
    }

    static CronsLocker getInstance() {
        return cronsLocker;
    }
}
