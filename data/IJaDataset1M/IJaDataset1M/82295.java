package de.amenthes.queue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;
import org.pockit.communication.GiStreamConnection;

/**
 * Our Downloadmanager. Since all downloads should be in some kind of queue,
 * we only want one Download manager. This is assured by the Singleton-Pattern. 
 * 
 * @author Claudius Coenen <amenthes@pock-it.org>
 * @link http://java.sun.com/developer/JDCTechTips/2006/tt0113.html
 */
public class QueueManager implements Runnable {

    /**
	 * states that a new item should be on top of the queue (i.e. processed as the very next item)
	 */
    public static final boolean PRIORITY_TOP = true;

    /**
	 * states that a new item should be at the bottom of the queue (i.e. processed as the last item)
	 */
    public static final boolean PRIORITY_BOTTOM = false;

    /**
	 * this contains the Instance
	 */
    private static final QueueManager INSTANCE = new QueueManager();

    /**
	 * contains the queueItems.
	 */
    private static volatile Vector queue;

    /**
	 * Thread, in which downloads are fetched
	 */
    private Thread thread;

    /**
	 * used for the QueueItem Identifier.
	 */
    private static int nextIdentifier = 0;

    /**
	 * reference to the currently downloading item. Will be null when nothing is downloading.
	 */
    private static QueueItem currentlyDownloading = null;

    /**
	 * this is the emergency brake that will cause the current transfer to shut down
	 * true: everything stops
	 * false: everything's fine!
	 */
    private static boolean emergencyBrake = false;

    private QueueManager() {
        queue = new Vector();
    }

    /**
	 * Returns the QueueManager instance. There is only one Instance of
	 * QueueManager (Singleton Pattern)
	 * 
	 * @return The QueueManager Instance
	 */
    public static final QueueManager getInstance() {
        return INSTANCE;
    }

    /**
	 * queues a new item, starts the downloading process, if neccessary
	 */
    public synchronized QueueItem transfer(String url, String data, QueueListener listener, boolean priority) {
        int id = nextIdentifier++;
        QueueItem queueItem = new QueueItem(id, url, data, listener);
        if (priority == PRIORITY_TOP) {
            queue.insertElementAt(queueItem, 0);
        } else {
            queue.addElement(queueItem);
        }
        if (thread == null) {
            thread = new Thread(INSTANCE);
            thread.start();
        }
        return queueItem;
    }

    public QueueItem transfer(String url, QueueListener listener) {
        return transfer(url, null, listener);
    }

    public QueueItem transfer(String url, String data, QueueListener listener) {
        return transfer(url, data, listener, PRIORITY_BOTTOM);
    }

    /** 
	 * In case you wish to cancel the current queue
	 * please note that the listeners will not be notified about this change of plan.
	 */
    public synchronized void cancelQueue() {
        int count = 0;
        System.out.print("canceling the queue ");
        count += queue.size();
        queue.removeAllElements();
        System.out.print("3 ");
        emergencyBrake = true;
        System.out.print("2 ");
        try {
            thread.interrupt();
            thread.join();
        } catch (InterruptedException unhandled) {
            unhandled.printStackTrace();
        } catch (NullPointerException handled) {
            System.out.print("no thread to speak of. Nothing to cancel.");
        }
        System.out.print("1 ");
        if (currentlyDownloading != null) {
            currentlyDownloading = null;
            count++;
        }
        emergencyBrake = false;
        System.out.println(" done! Cancelled " + count + " QueueItems");
    }

    /**
	 * Used internally to start the next item in the queue.
	 * @throws QueueEmptyException when the queue is empty. Indicating that all
	 * work is done. 
	 */
    private synchronized void startNextItem() throws QueueEmptyException {
        if (queue.size() == 0) {
            throw new QueueEmptyException("No items left in queue");
        }
        if (currentlyDownloading == null) {
            currentlyDownloading = (QueueItem) queue.elementAt(0);
            queue.removeElement(currentlyDownloading);
        } else {
            throw new RuntimeException("starting a new queueitem is not allowed " + "when another one is still downloading. See if you have " + "unset the \"currentlyDownloading\" variable");
        }
    }

    /**
	 * @return the number of items currently in the queue
	 */
    public synchronized int size() {
        return queue.size();
    }

    /**
	 * Thread in which the work is done. Runs as long as there are items in the queue.
	 */
    public void run() {
        while (!emergencyBrake) {
            System.out.println("Thread läuft");
            try {
                startNextItem();
            } catch (QueueEmptyException e) {
                break;
            }
            HttpConnection connection = null;
            try {
                connection = (HttpConnection) Connector.open(currentlyDownloading.url);
                System.out.println("Connection opened to " + connection.getURL());
                connection.setRequestProperty("Accept", "text/xml");
                GiStreamConnection.setDefaultHeaders(connection);
                if (currentlyDownloading.sendData != null || currentlyDownloading.sendFile != null) {
                    handleUploadPart(connection);
                }
                handleDownloadPart(connection);
                System.out.println("fertig");
                currentlyDownloading.queueItemFinished();
                currentlyDownloading = null;
            } catch (SecurityException e) {
            } catch (IOException e) {
                System.err.println("General transfer-error");
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        thread = null;
    }

    /**
	 * handles the upload-part of a transfer. Do not call directly, gets used by run()
	 * @param connection
	 * @return void
	 * @throws IOException catches and rethrows any exception that might
	 */
    private void handleUploadPart(HttpConnection connection) throws IOException {
        OutputStream out = null;
        InputStream source = null;
        int sizeOut = Integer.MAX_VALUE;
        try {
            if (currentlyDownloading.sendData != null) {
                connection.setRequestMethod(HttpConnection.POST);
                sizeOut = currentlyDownloading.sendData.length;
                connection.setRequestProperty("Content-Length", Integer.toString(sizeOut));
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                out = connection.openOutputStream();
                source = new ByteArrayInputStream(currentlyDownloading.sendData);
            } else if (currentlyDownloading.sendFile != null) {
                connection.setRequestMethod(HttpConnection.POST);
                connection.setRequestProperty("Content-Type", "image/jpeg");
                FileConnection fileConnection = (FileConnection) Connector.open(currentlyDownloading.sendFile, Connector.READ);
                InputStream fileContent = fileConnection.openInputStream();
                sizeOut = (int) fileConnection.fileSize();
                String fileNameOnly = currentlyDownloading.sendFile.substring(currentlyDownloading.sendFile.lastIndexOf('/') + 1);
                connection.setRequestProperty("Content-Length", Integer.toString(sizeOut));
                connection.setRequestProperty("Filename", fileNameOnly);
                out = connection.openOutputStream();
                source = fileContent;
            }
            int updateChunks = calculateOptimumUpdateChunks(sizeOut);
            int read = 0, total = 0;
            byte[] chopper = new byte[updateChunks];
            while ((read = source.read(chopper)) != -1) {
                if (emergencyBrake) {
                    out.close();
                    break;
                }
                out.write(chopper, 0, read);
                total += read;
                currentlyDownloading.queueItemStatus((int) 100 * total / sizeOut);
                System.out.print(".");
            }
        } catch (IOException e) {
            System.err.println("Error while sending data from the Queue");
            throw e;
        } catch (SecurityException e) {
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException unhandled) {
                }
            }
        }
    }

    /**
	 * handles the download-part of a file transfer. Do not call directly, 
	 * it's designed to be used by run()
	 * @param connection
	 * @throws IOException
	 */
    private void handleDownloadPart(HttpConnection connection) throws IOException {
        InputStream in = null;
        try {
            in = connection.openInputStream();
            int sizeIn = (int) connection.getLength();
            int updateChunks = calculateOptimumUpdateChunks(sizeIn);
            currentlyDownloading.statuscode = connection.getResponseCode();
            currentlyDownloading.receiveData = new byte[sizeIn];
            for (int i = 0; i < sizeIn; i++) {
                if (emergencyBrake) {
                    in.close();
                    break;
                }
                currentlyDownloading.receiveData[i] = (byte) in.read();
                if ((i % updateChunks) == 0) {
                    currentlyDownloading.queueItemStatus((int) 100 * i / sizeIn);
                    System.out.print(".");
                }
            }
        } catch (IOException e) {
            System.err.println("Error while downloading data in the queue");
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
	 * returns a reasonable size for update-chunks. Usually divides into ~100 chunks of
	 * equal size. The number is capped at 5000 and will at least be 1.
	 * @param totalSize
	 * @return integer [1..5000]
	 */
    private int calculateOptimumUpdateChunks(int totalSize) {
        int updateChunks;
        updateChunks = (int) totalSize / 100;
        if (updateChunks > 5000) updateChunks = 5000;
        if (updateChunks < 1) updateChunks = 1;
        return updateChunks;
    }
}
