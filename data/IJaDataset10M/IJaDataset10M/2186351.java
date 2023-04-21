package org.webstrips.core;

import org.webstrips.WebStrips;
import org.webstrips.core.data.Cache;
import org.webstrips.core.data.CacheProvider;
import org.webstrips.core.data.Transfer;
import org.webstrips.core.data.TransferListener;

/**
 * Asyncronous operation owned by a comic
 * 
 * @author luka
 * @see AsynchronousOperation
 * @since WebStrips 0.3.1
 */
abstract class ComicDriverAsynchronousOperation extends AsynchronousOperation {

    private ComicDriver c;

    private UserInterface ui;

    private Cache<byte[]> cache;

    private class ContextAwareThread extends Thread implements TransferListener, CacheProvider<byte[]> {

        public ContextAwareThread(Runnable r) {
            super(r);
        }

        public void transferConnecting(Transfer d) {
            if (ui != null) ui.setProgress(c, 1000);
        }

        public void transferStarted(Transfer d) {
            if (ui != null) ui.setProgress(c, 0);
        }

        public void transferProgress(Transfer d, int progress) {
            if (ui != null) ui.setProgress(c, (progress == -1) ? 1000 : progress);
        }

        public void transferEnded(Transfer d) {
            if (ui != null) ui.setProgress(c, -1);
        }

        public void transferError(Transfer d, int error, String errorMessage) {
            WebStrips.getApplicationLogger().report(WebStrips.TRANSFER, "Transfer error %s (%d)", errorMessage, error);
            if (ui != null) ui.setStatusMessage(c, UserInterface.StatusMessageType.ERROR, errorMessage);
            if (ui != null) ui.setProgress(c, -1);
        }

        public Cache<byte[]> getCache() {
            return cache;
        }
    }

    /**
	 * Consturcts new operation
	 * 
	 * @param c comic owner
	 */
    public ComicDriverAsynchronousOperation(ComicDriver c, UserInterface ui, Cache<byte[]> cache) {
        super();
        this.c = c;
        this.ui = ui;
        this.cache = cache;
    }

    /**
	 * Gets owner comic
	 * 
	 * @return owner comic
	 */
    public ComicDriver getComicDriver() {
        return c;
    }

    public UserInterface getUserInterface() {
        return ui;
    }

    @Override
    protected Thread createThread(Runnable r) {
        return new ContextAwareThread(r);
    }
}
