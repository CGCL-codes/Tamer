package com.bss.impactreplacer.thread;

import com.bss.impactreplacer.lib.ImpactReplacer;
import com.bss.impactreplacer.lib.ImpactReplacerHandler;

/**
 * To start ImpactReplacer in a new thread
 * @author bearocean
 *
 */
public class ImpactReplacerThread extends Thread {

    private ImpactReplacer ir = null;

    public ImpactReplacerThread(String filePath, String filePattern, String pattern, String replacement, ImpactReplacerHandler infoListener) {
        this.ir = new ImpactReplacer(filePath, filePattern, pattern, replacement);
        this.ir.setInfoListener(infoListener);
    }

    public void run() {
        try {
            this.ir.replaceAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
