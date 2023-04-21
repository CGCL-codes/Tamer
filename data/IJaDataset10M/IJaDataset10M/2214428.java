package com.googlecode.legendtv.test.drivers.faults;

public class CrashThread extends Thread {

    private static final String CLASS_NAME = "org.legendtv.test.drivers.faults.LibCrash";

    public void run() {
        LC lc = null;
        try {
            NativeLoader cLoader = new NativeLoader(CLASS_NAME);
            Class<?> lcClass = cLoader.loadClass(CLASS_NAME);
            lc = ((LC) lcClass.newInstance());
            lc.noCrash();
            lc.asyncCrash();
            lc.noCrash();
            lc.crash();
            while (true) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
