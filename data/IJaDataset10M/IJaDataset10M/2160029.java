package se.slackers.locality.media.queue;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;
import org.apache.log4j.Logger;
import se.slackers.locality.exception.EncapsuledExceptionRuntimException;
import se.slackers.locality.media.Frame;
import se.slackers.locality.media.reader.MediaReader;
import se.slackers.locality.media.reader.MediaReaderFactory;
import se.slackers.locality.media.reader.SilentMediaReader;
import se.slackers.locality.model.Media;
import se.slackers.locality.model.Metadata;

public abstract class AbstractMediaQueueProcessor implements MediaQueueProcessor, Runnable {

    private static final Logger log = Logger.getLogger(AbstractMediaQueueProcessor.class);

    private MediaQueue mediaQueue;

    private MediaReader mediaReader = null;

    private MediaReader silentReader = new SilentMediaReader();

    private MediaReaderFactory mediaReaderFactory = null;

    private List<WeakReference<MediaQueueProcessorListener>> mediaQueueProcessorListeners = new ArrayList<WeakReference<MediaQueueProcessorListener>>();

    private boolean stopProcessing;

    private int activeClients = 0;

    private Object stopProcessingMonitor = new Object();

    private Semaphore initDeinit = new Semaphore(1);

    /**
	 * {@inheritDoc}
	 */
    public synchronized void init() {
        log.info("Acquire permit");
        initDeinit.acquireUninterruptibly();
        activeClients = 0;
        stopProcessing = false;
        checkMediaReader();
    }

    /**
	 * {@inheritDoc}
	 */
    public synchronized void deinit() {
        mediaQueue.getFrameStorage().clear();
        if (mediaReader != null) {
            try {
                mediaReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mediaReader = null;
        log.info("Releasing permit");
        initDeinit.release();
    }

    /**
	 * 
	 */
    public void run() {
        Frame frame = new Frame(3000);
        try {
            while (stopProcessing == false) {
                if (mediaReader == null && mediaQueue.size() == 0) {
                    break;
                }
                checkMediaReader();
                if (mediaReader != null) {
                    synchronized (this) {
                        readData(mediaReader, frame);
                    }
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (stopProcessing) {
            synchronized (stopProcessingMonitor) {
                stopProcessingMonitor.notifyAll();
            }
        }
    }

    /**
	 * Makes sure that there is a valid {@link MediaReader} instantiated as long as there are more entries in the queue.
	 */
    private void checkMediaReader() {
        if (mediaReader == null && mediaQueue.size() > 0) {
            mediaReader = mediaReaderFactory.getMediaReader(mediaQueue.get(0));
            try {
                mediaReader.open(mediaQueue.get(0));
                fireNextMediaEvent(mediaQueue.get(0), mediaReader.getMetadata());
            } catch (IOException e) {
                log.error("Can't open [" + mediaQueue.get(0) + "], skipping file");
                mediaQueue.remove(0);
            }
        }
        if (mediaReader != null && mediaReader.eof()) {
            try {
                mediaReader.close();
                mediaReader = null;
                mediaQueue.remove(0);
                if (mediaQueue.size() > 0) {
                    mediaReader = mediaReaderFactory.getMediaReader(mediaQueue.get(0));
                    mediaReader.open(mediaQueue.get(0));
                    fireNextMediaEvent(mediaQueue.get(0), mediaReader.getMetadata());
                } else {
                    mediaReader = silentReader;
                }
            } catch (IOException e) {
                log.error("Can't open [" + mediaQueue.get(0) + "], skipping file");
                mediaQueue.remove(0);
            }
        }
    }

    /**
	 * Reads a frame from the media and stores it in the FrameStorage.
	 * 
	 * @param reader
	 * @param frame
	 * @return
	 */
    protected abstract void readData(MediaReader reader, Frame frame);

    /**
	 * Stops the processing and waits until it really has stopped.
	 */
    public void stopProcessing() {
        this.stopProcessing = true;
        try {
            synchronized (stopProcessingMonitor) {
                stopProcessingMonitor.wait();
            }
        } catch (InterruptedException e) {
            throw new EncapsuledExceptionRuntimException(e);
        }
        deinit();
        mediaQueue.stopProcessor();
    }

    /**
	 * Returns the mediaQueue that is used by the processor
	 */
    public MediaQueue getMediaQueue() {
        return mediaQueue;
    }

    /**
	 * Sets the media queue to be used by the processor. This method also ensures that the reverse dependency is
	 * correct.
	 */
    public void setMediaQueue(MediaQueue mediaQueue) {
        this.mediaQueue = mediaQueue;
        if (this.mediaQueue.getMediaQueueProcessor() != this) {
            this.mediaQueue.setMediaQueueProcessor(this);
        }
    }

    public MediaReaderFactory getMediaReaderFactory() {
        return mediaReaderFactory;
    }

    public void setMediaReaderFactory(MediaReaderFactory mediaReaderFactory) {
        this.mediaReaderFactory = mediaReaderFactory;
    }

    public void clientStartStreaming() {
        activeClients++;
        log.debug("Client connected, client=" + activeClients);
    }

    public void clientStopsStreaming() {
        activeClients--;
        log.debug("Client disconnected, client=" + activeClients);
        if (activeClients == 0) {
            stopProcessing();
        }
    }

    public void addMediaQueueProcessorListener(MediaQueueProcessorListener listener) {
        log.debug("New listener added by thread " + Thread.currentThread());
        mediaQueueProcessorListeners.add(new WeakReference<MediaQueueProcessorListener>(listener));
    }

    public void removeMediaQueueProcessorListener(MediaQueueProcessorListener listener) {
        mediaQueueProcessorListeners.remove(listener);
    }

    /**
	 * Calls the nextMedia method in all registered MediaQueueListeners
	 * @param media
	 */
    protected void fireNextMediaEvent(Media media, Metadata metadata) {
        Iterator<WeakReference<MediaQueueProcessorListener>> iterator = mediaQueueProcessorListeners.iterator();
        while (iterator.hasNext()) {
            WeakReference<MediaQueueProcessorListener> ref = iterator.next();
            if (null == ref.get()) {
                iterator.remove();
            } else {
                ref.get().nextMedia(media, metadata);
            }
        }
    }

    public Metadata getCurrentMetadata() {
        if (mediaReader == null) {
            return Metadata.create("Nothing playing", null, null);
        }
        return mediaReader.getMetadata();
    }
}
