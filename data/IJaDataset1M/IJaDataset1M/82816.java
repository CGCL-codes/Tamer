package neembuu.http;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import jpfm.DirectoryStream;
import jpfm.FileFlags;
import jpfm.annotations.NonBlocking;
import jpfm.operations.RequestHandlingApproach;
import jpfm.operations.readwrite.ReadRequest;
import jpfm.volume.BasicCascadableAbstractFile;
import neembuu.common.ListenableRangeArray;
import neembuu.common.RangeArray;
import neembuu.common.RangeArrayElement;
import neembuu.common.RangeArrayElementFactory;
import neembuu.directdownloader.DirectDownloadLinkGenerator;
import neembuu.util.logging.LoggerUtil;
import neembuu.vfs.readmanager.NewConnectionProvider;
import neembuu.vfs.readmanager.RegionHandler;
import neembuu.vfs.readmanager.ReadQueueManager;
import neembuu.vfs.readmanager.sampleImpl.DownloadManager;
import neembuu.vfs.test.MonitoredHttpFile;
import net.jcip.annotations.NotThreadSafe;
import org.apache.http.cookie.Cookie;

/**
 *
 * @author Shashank Tulsyan
 */
@NotThreadSafe
public class SeekableHttpFile extends BasicCascadableAbstractFile implements RangeArrayElementFactory<SeekableHttpChannel> {

    protected String url;

    protected String store;

    private boolean urlOkay = false;

    private final NewReadHandlerProviderImpl readHandlerProvider = new NewReadHandlerProviderImpl(this);

    private final ReadQueueManager readManager;

    private NewConnectionProvider newConnectionProvider;

    /**
     * the channel over who's gui the user 's mouse is located, and therefore, the channel's who's speed and
     * other attributes will be drawn on the gui
     */
    public volatile SeekableHttpChannel channelOfInterest = null;

    private static final Logger LOGGER = LoggerUtil.getLogger();

    private volatile double totalDownloadSpeed = 0;

    private volatile double totalRequestSpeed = 0;

    private volatile boolean speedThreadCanRun = true;

    private final SpeedUpdateThread speedUpdateThread = new SpeedUpdateThread();

    private final RangeArray.UnsynchronizedAccess handlersUnsync;

    private final SpeedUpdateThread sut = new SpeedUpdateThread();

    private final List<Cookie> cookies;

    public SeekableHttpFile(String url, String name, long size, String storePath, DirectoryStream parent) throws IOException {
        this(null, url, name, size, storePath, parent);
        urlOkay = true;
    }

    public SeekableHttpFile(Class<? extends DirectDownloadLinkGenerator> linkGenerator, String url, String name, long size, String storePathBase, DirectoryStream parent) throws IOException {
        this(linkGenerator, url, name, size, storePathBase, parent, new DownloadManager(url));
    }

    public SeekableHttpFile(Class<? extends DirectDownloadLinkGenerator> linkGenerator, String url, String name, long size, String storePathBase, DirectoryStream parent, NewConnectionProvider connectionProvider) throws IOException {
        this(linkGenerator, url, name, size, storePathBase, parent, connectionProvider, null);
    }

    public SeekableHttpFile(Class<? extends DirectDownloadLinkGenerator> linkGenerator, String url, String name, long size, String storePathBase, DirectoryStream parent, NewConnectionProvider connectionProvider, List<Cookie> cookies) throws IOException {
        super(name, size, parent);
        this.url = url;
        this.cookies = cookies;
        store = storePathBase;
        LOGGER.log(Level.INFO, "quick url={0} for file={1}", new Object[] { url, name });
        this.newConnectionProvider = connectionProvider;
        if (size <= 0) {
            long size_obt = FileSizeFinder.getFileSizeFinder().getSize(url);
            if (size_obt == -1) {
                throw new IllegalStateException("could not determine size");
            }
            this.fileSize = size_obt;
        } else {
            LOGGER.log(Level.INFO, "Filesize={0}", fileSize);
        }
        super.name = super.name.replace(java.io.File.separatorChar, '_');
        try {
            store = store + File.separator + super.name + "_neembuu_download_data";
        } catch (Exception any) {
            LOGGER.log(Level.INFO, "", any);
            store = store + File.separator + "temporary_neembuu_download_data";
        }
        if (new File(store).exists()) {
            if (new java.io.File(store).isFile()) {
                throw new IOException("Storage location should be a directory. Given = " + store);
            }
        } else new File(store).mkdir();
        if (System.getProperty("neembuu.vfs.test.MoniorFrame.resumepolicy").equals("emptyDirectory")) {
            LOGGER.log(Level.CONFIG, "emptyDirectory mode{0}", this);
            emptyDirectory();
            readManager = new ReadQueueManager(readHandlerProvider, RequestHandlingApproach.PARTIALLY_COMPLETING);
            handlersUnsync = readManager.getHandlers().getUnSynchronizedArrayAccess();
        } else if (System.getProperty("neembuu.vfs.test.MoniorFrame.resumepolicy").equals("resumeFromPreviousState")) {
            LOGGER.log(Level.CONFIG, "resumeFromPreviousState mode{0}", this);
            List<RegionHandler> l = resumeFromPreviousState();
            readManager = new ReadQueueManager(l, readHandlerProvider, RequestHandlingApproach.PARTIALLY_COMPLETING);
            handlersUnsync = readManager.getHandlers().getUnSynchronizedArrayAccess();
        } else {
            LOGGER.log(Level.CONFIG, "emptyDirectory mode{0}", this);
            LOGGER.severe("policy was not set correctly, assumed emptyDirectory as default");
            emptyDirectory();
            readManager = new ReadQueueManager(readHandlerProvider, RequestHandlingApproach.PARTIALLY_COMPLETING);
            handlersUnsync = readManager.getHandlers().getUnSynchronizedArrayAccess();
        }
        readManager.getHandlers().setFileSize(fileSize);
        Thread t = new Thread(speedUpdateThread, "SpeedUpdateThread");
        t.setDaemon(true);
        t.start();
    }

    final NewConnectionProvider getNewConnectionProvider() {
        return newConnectionProvider;
    }

    public final List<Cookie> getCookies() {
        return cookies;
    }

    public final ListenableRangeArray<RegionHandler> getConnections() {
        return readManager.getHandlers();
    }

    protected ReadQueueManager getReadQueueManager() {
        return readManager;
    }

    private final Object speedUpdateLock = new Object();

    final void notifySpeedChange() {
        synchronized (speedUpdateLock) {
            speedUpdateLock.notifyAll();
        }
    }

    private final class SpeedUpdateThread implements Runnable {

        SpeedUpdateThread() {
        }

        @Override
        public final void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException a) {
            }
            while (speedThreadCanRun) {
                try {
                    synchronized (speedUpdateLock) {
                        speedUpdateLock.wait(10000);
                    }
                } catch (InterruptedException ie) {
                }
                RangeArrayElement[] connections = null;
                try {
                    connections = SeekableHttpFile.this.handlersUnsync.tryToGetUnsynchronizedCopy(null);
                } catch (NullPointerException npe) {
                    continue;
                }
                if (connections == null) continue;
                if (connections.length == 0) continue;
                double totalDownloadSpeed = 0, totalRequestSpeed = 0;
                for (int i = 0; i < connections.length; i++) {
                    SeekableHttpChannel channel = (SeekableHttpChannel) connections[i];
                    double ith_download_speed = channel.getDownloadSpeed() / 1024;
                    double ith_request_speed = channel.getRequestSpeed() / 1024;
                    if (ith_download_speed < 0) {
                        SeekableHttpChannel.LOGGER.severe("Incorrect implementation of get download speed, value returned is less than zero");
                    }
                    if (ith_request_speed < 0) {
                        SeekableHttpChannel.LOGGER.severe("Incorrect implementation of get request speed, value returned is less than zero");
                    }
                    totalDownloadSpeed += ith_download_speed;
                    totalRequestSpeed += ith_request_speed;
                }
                SeekableHttpFile.this.totalDownloadSpeed = totalDownloadSpeed;
                SeekableHttpFile.this.totalRequestSpeed = totalRequestSpeed;
                if (SeekableHttpFile.this instanceof MonitoredHttpFile) {
                    MonitoredHttpFile self = (MonitoredHttpFile) SeekableHttpFile.this;
                    self.addRequestSpeedObservation(totalRequestSpeed);
                    self.addSupplySpeedObservation(totalDownloadSpeed);
                }
            }
        }
    }

    private void emptyDirectory() {
        try {
            File[] f = new File(store).listFiles();
            for (int i = 0; i < f.length; i++) {
                f[i].delete();
            }
        } catch (Exception any) {
            LOGGER.log(Level.INFO, "", any);
        }
    }

    private List<RegionHandler> resumeFromPreviousState() {
        this.isOpen();
        List<RegionHandler> handlers = new LinkedList<RegionHandler>();
        try {
            File[] fs = new File(store).listFiles();
            Arrays.sort(fs, new Comparator<File>() {

                @Override
                public int compare(File o1, File o2) {
                    String f1name = o1.getName();
                    f1name = f1name.substring(f1name.indexOf("_0x") + 3, f1name.lastIndexOf("."));
                    String f2name = o2.getName();
                    f2name = f2name.substring(f2name.indexOf("_0x") + 3, f2name.lastIndexOf("."));
                    return (int) (-Long.parseLong(f1name, 16) + Long.parseLong(f2name, 16));
                }
            });
            long lastIndex = Long.MAX_VALUE;
            for (int i = 0; i < fs.length; i++) {
                File nextFile = fs[i];
                String nm = nextFile.getName();
                long startingOffset = Long.parseLong(nm.substring(nm.indexOf("_0x") + 3, nm.lastIndexOf('.')), 16);
                long endingOffset = startingOffset + nextFile.length() - 1;
                RangeArrayElement element = new RangeArrayElement(startingOffset, lastIndex > endingOffset ? endingOffset : lastIndex);
                lastIndex = element.starting() - 1;
                SeekableHttpChannel channel = new SeekableHttpChannel(this, element, nextFile.toString());
                handlers.add(channel);
            }
        } catch (Exception any) {
            LOGGER.log(Level.INFO, "", any);
            return null;
        }
        return handlers;
    }

    public final boolean hasPendingRequests() {
        RangeArrayElement[] elements = readManager.getHandlers().getUnSynchronizedArrayAccess().tryToGetUnsynchronizedCopy(readManager.getHandlers());
        for (int i = 0; i < elements.length; i++) {
            SeekableHttpChannel channel = (SeekableHttpChannel) elements[i];
            if (channel.requestsPresentAlongThisConnection()) return true;
        }
        return false;
    }

    protected void setFileSize(long fileSize) {
        super.fileSize = fileSize;
    }

    public String getUrl() {
        return url;
    }

    public final RangeArray getDownloadedRegion() {
        return readManager.getHandlers();
    }

    @Override
    @NonBlocking
    public void read(ReadRequest read) throws Exception {
        readManager.read(read);
    }

    @Override
    public long getFileSize() {
        return super.getFileSize();
    }

    @Override
    public final void close() {
        readManager.close();
    }

    @Override
    public SeekableHttpChannel newInstance() {
        return new SeekableHttpChannel();
    }

    @Override
    public SeekableHttpChannel newInstance(long start, long end) {
        return new SeekableHttpChannel(start, end);
    }

    @Override
    public boolean entriesNeverDissolve() {
        return true;
    }

    @Override
    public FileFlags getFileFlags() {
        return new FileFlags.Builder().setReadOnly().setNoIndex().setOffline().build();
    }

    @Override
    public final void open() {
        readManager.open();
    }
}
