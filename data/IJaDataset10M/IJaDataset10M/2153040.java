package net.effigent.jdownman.util;

import java.util.concurrent.ThreadFactory;
import sun.tools.jconsole.Worker;
import net.effigent.jdownman.Download;
import net.effigent.jdownman.Download.ChunkDownload;

/**
 * 
 * 
 * @author vipul
 *
 */
public class DownloadNamedThreadFactory implements ThreadFactory {

    /**
	 * Creates a new thread without a name if the runnable is NOT a Download or
	 * ChunkDownload instance
	 * If Download :  name is the Download ID
	 * 	e.g. DN-124ASF234AGT
	 * If ChunkDownload : name is the DownloadID appended by the range
	 *  e.g. DN-124ASF234AGT:0-2000
	 * 
	 */
    public Thread newThread(Runnable runnable) {
        System.out.println("Creating thread in DownlaodNamedThreadFactory " + runnable);
        Thread thread = null;
        Runnable innerRunnable = runnable;
        if (runnable instanceof ChunkDownload) {
            ChunkDownload chunk = (ChunkDownload) runnable;
            thread = new Thread(runnable, "DN-" + chunk.getParentDownload().getID() + ':' + chunk.getBeginRange() + "-" + chunk.getEndRange());
        } else if (runnable instanceof Download) {
            Download download = (Download) runnable;
            thread = new Thread(runnable, download.getID());
        } else {
            thread = new Thread(runnable);
        }
        return thread;
    }
}
