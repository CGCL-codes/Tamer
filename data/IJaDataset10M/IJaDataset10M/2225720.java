package com.dotmarketing.quartz.job;

import com.dotmarketing.portlets.contentlet.business.ReindexContentletFactoryImpl;
import com.dotmarketing.util.Logger;

public class ShutdownHookThread extends Thread {

    public void run() {
        Logger.info(this, "Running dotCMS shutdown cleanup sequence.");
        ReindexContentletFactoryImpl.shuttingDown = true;
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            Logger.error(this, "A error ocurred trying to close the lucene writer, maybe be the lucene index would be corrupted at the next startup.");
        }
    }
}
