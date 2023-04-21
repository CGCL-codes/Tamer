package jpfm.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import jpfm.AccessLevel;
import jpfm.DirectoryStream;
import jpfm.FileDescriptor;
import jpfm.FileFlags;
import jpfm.FileId;
import jpfm.JPfmError;
import jpfm.JPfmReadable;
import jpfm.annotations.Blocking;
import jpfm.annotations.MightBeBlocking;
import jpfm.annotations.NonBlocking;
import jpfm.annotations.PartiallyCompleting;
import jpfm.fs.ReadOnlyRawFileData;
import jpfm.operations.RequestHandlingApproach;
import jpfm.operations.readwrite.ReadRequest;
import jpfm.operations.readwrite.SimpleReadRequest;
import jpfm.volume.BasicAbstractFile;
import jpfm.volume.RealFile;

/**
 * The correct usage of this requires that {@link #open() } and {@link #close() }
 * be called when the file is opened and closed.
 * When a read request is made on the file,
 * {@link #read(jpfm.operations.readwrite.ReadRequest) } should be called.
 * Each request should be send only once, as the implementation is {@link NonBlocking}
 * for the simple reason that requests are dispatched in another thread.
 * <br/><br/>
 * @author Shashank Tulsyan
 */
public final class DispatchReadRequestsInOtherThread {

    /**
     * This is used so that
     * {@link JPfmReadable#read(jpfm.operations.readwrite.ReadRequest)  }
     * and  {@link ReadImplementor#readImpl(jpfm.operations.readwrite.ReadRequest)}
     * can reside in same class
     */
    public interface ReadImplementor {

        /**
         * The name of this method is different so
         * that {@link JPfmReadable#read(jpfm.operations.readwrite.ReadRequest)  }
         * and  {@link ReadImplementor#readImpl(jpfm.operations.readwrite.ReadRequest)}
         * can reside in same class.
         * Blocking or PartiallyCompleting or NonBlocking
         */
        @Blocking
        @PartiallyCompleting
        @NonBlocking
        public void readImpl(ReadRequest read) throws Exception;

        public boolean isOpen();

        /**
         * Used for naming the read thread
         * for making it easy to identify during debugging
         * @return name of the read thread
         */
        public String getThreadName();
    }

    private final ConcurrentLinkedQueue<ReadRequest> pendingReadRequests = new ConcurrentLinkedQueue<ReadRequest>();

    private final Object lock = new Object();

    private ReadThread readThread = null;

    private final ReadImplementor readImplementor;

    private final RequestHandlingApproach approach;

    private static final Logger LOGGER = Logger.getLogger(DispatchReadRequestsInOtherThread.class.getName());

    public DispatchReadRequestsInOtherThread(ReadImplementor readImplementor, RequestHandlingApproach approach) {
        if (readImplementor == null) throw new IllegalArgumentException("ReadImplementor passed was null");
        if (approach == null) throw new IllegalArgumentException("RequestHandlingApproach passed was null.");
        this.approach = approach;
        this.readImplementor = readImplementor;
        AtomicInteger handlerRequestHandlingApproachChecked = new AtomicInteger(-1);
        try {
            Method m = readImplementor.getClass().getDeclaredMethod("readImpl", ReadRequest.class);
            Annotation[] annotations = m.getAnnotations();
            FOR: for (Annotation annotation : annotations) {
                int result = RequestHandlingApproach.isEqual(approach, annotation);
                SW: switch(result) {
                    case 1:
                        LOGGER.log(Level.WARNING, "One of the annotations indicate that the ReadImplementor RequestHandlingApproach annotation does not match with expected approach. Expected={0}, Found={1} ", new Object[] { approach.getAsAnnotation().annotationType().getSimpleName(), annotation.annotationType().getSimpleName() });
                        handlerRequestHandlingApproachChecked.compareAndSet(-1, 1);
                        break SW;
                    case 0:
                        LOGGER.log(Level.INFO, "ReadImplementor approach is consistent with what was defined during runtime. Expected=Found={0}", approach.getAsAnnotation().annotationType().getSimpleName());
                        handlerRequestHandlingApproachChecked.set(0);
                        break SW;
                    case 2:
                        String reason = (String) MightBeBlocking.class.getDeclaredMethod("reason").invoke(annotation);
                        LOGGER.log(Level.WARNING, "The implementation might be blocking in nature. Reason = {0}", reason);
                        break SW;
                    default:
                        continue FOR;
                }
            }
            if (handlerRequestHandlingApproachChecked.get() == -1) {
                LOGGER.log(Level.INFO, "The method \n{0}\n of\n {1}\n was not annotated. Please annotated it as @{2}", new Object[] { m, readImplementor, approach.getAsAnnotation().annotationType().getSimpleName() });
            }
        } catch (Exception any) {
            LOGGER.log(Level.INFO, " ", any);
        }
    }

    public synchronized void open() {
        readThread = new ReadThread(readImplementor.getThreadName());
        this.readThread.start();
    }

    @NonBlocking(usesOneThreadPerRequest = true)
    public void read(ReadRequest readRequest) {
        if (!readImplementor.isOpen()) {
            throw new IllegalStateException("Cannot handle read requests : Looks like the open function of " + ReadImplementor.class.getName() + " was not called.");
        }
        if (this.readThread == null) throw new IllegalStateException("Cannot handle read requests : Looks like the open function of " + DispatchReadRequestsInOtherThread.class.getName() + " was not called.");
        if (!this.readThread.isAlive()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
            }
            if (!this.readThread.isAlive()) throw new IllegalStateException("Cannot handle read requests : Looks like the open function of " + DispatchReadRequestsInOtherThread.class.getName() + " was not called.");
        }
        pendingReadRequests.add(readRequest);
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public final RequestHandlingApproach getApproach() {
        return approach;
    }

    public synchronized void close() {
        if (this.readThread != null) {
            while (this.readThread.isAlive()) {
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
        }
    }

    public final class BlockingReadThread extends Thread {

        private final ReadRequest readRequest;

        public BlockingReadThread(ReadRequest readRequest) {
            this.readRequest = readRequest;
        }

        @Override
        public void run() {
            try {
                readImplementor.readImpl(readRequest);
            } catch (Exception anE) {
                LOGGER.log(Level.INFO, "Exception occured in readImplementor readImpl", anE);
            }
            if (!readRequest.isCompleted()) {
                readRequest.complete(JPfmError.SUCCESS, readRequest.getByteBuffer().capacity(), null);
            }
        }

        @Override
        public String toString() {
            return "BlockingReadThread{ReadRequest=" + readRequest + "}";
        }
    }

    private final class ReadThread extends Thread {

        public ReadThread(String filename) {
            super("ReadDispatcher@" + filename);
        }

        @Override
        public void run() {
            for (; readImplementor.isOpen(); ) {
                Iterator<ReadRequest> it = pendingReadRequests.iterator();
                WHILE: while (it.hasNext()) {
                    ReadRequest read = it.next();
                    if (read.isCompleted()) {
                        it.remove();
                        continue;
                    }
                    try {
                        SWITCH: switch(approach) {
                            case BLOCKING:
                                new BlockingReadThread(read).start();
                                it.remove();
                                continue WHILE;
                            case NON_BLOCKING:
                                readImplementor.readImpl(read);
                                it.remove();
                                continue WHILE;
                            case PARTIALLY_COMPLETING:
                                readImplementor.readImpl(read);
                                if (read.isCompleted()) it.remove();
                                continue WHILE;
                        }
                    } catch (Exception any) {
                        LOGGER.log(Level.INFO, "Exception occured in readImplementor readImpl", any);
                        if (!read.isCompleted()) read.handleUnexpectedCompletion(any);
                    }
                }
                synchronized (lock) {
                    try {
                        while (pendingReadRequests.isEmpty() && readImplementor.isOpen()) lock.wait();
                    } catch (InterruptedException exception) {
                    }
                }
            }
        }
    }

    /**
     * Use this to wrap files so that read requests are dispatched in a thread
     * other than jpfm thread, so that the underlying blocking nature of the file 
     * does not affect service of other files.
     */
    public static final class Wrapper extends BasicAbstractFile implements ReadImplementor, RealFile {

        private final RealFile abstractFile;

        private final DispatchReadRequestsInOtherThread drriot;

        public Wrapper(final RealFile abstractFile, final RequestHandlingApproach approach, final DirectoryStream parent) {
            this(abstractFile, approach, parent, abstractFile.getName());
        }

        public Wrapper(final RealFile abstractFile, final RequestHandlingApproach approach, final DirectoryStream parent, final String fileName) {
            super(fileName, abstractFile.getFileSize(), parent);
            if (abstractFile.getParent() != null) {
                throw new IllegalArgumentException("Parent of the file wrapped must be null, otherwise 2 files might appear in the volume");
            }
            this.abstractFile = abstractFile;
            drriot = new DispatchReadRequestsInOtherThread(this, approach);
        }

        @Blocking
        @Override
        public void readImpl(ReadRequest read) throws Exception {
            abstractFile.read(read);
        }

        @Override
        public String getThreadName() {
            return abstractFile.getName();
        }

        @Override
        public DirectoryStream getParent() {
            return super.getParent();
        }

        @Override
        public void open() {
            abstractFile.open();
            drriot.open();
        }

        @Override
        public void close() {
            abstractFile.close();
            drriot.close();
        }

        @Override
        public void read(ReadRequest readRequest) throws Exception {
            drriot.read(readRequest);
        }

        @Override
        public long getFileSize() {
            return abstractFile.getFileSize();
        }

        @Override
        public long getCreateTime() {
            return abstractFile.getCreateTime();
        }

        @Override
        public long getAccessTime() {
            return abstractFile.getAccessTime();
        }

        @Override
        public long getWriteTime() {
            return abstractFile.getWriteTime();
        }

        @Override
        public long getChangeTime() {
            return abstractFile.getChangeTime();
        }

        @Override
        public FileDescriptor getParentFileDescriptor() {
            return abstractFile.getParentFileDescriptor();
        }

        @Override
        public FileFlags getFileFlags() {
            return abstractFile.getFileFlags();
        }

        private final AtomicInteger cascadeReferenceCount = new AtomicInteger(0);

        @Override
        public ReadOnlyRawFileData getReference(FileId fileId, AccessLevel level) {
            cascadeReferenceCount.incrementAndGet();
            return new ROFileData(this, 10);
        }

        @Override
        public String getSourceFile() {
            return abstractFile.getSourceFile();
        }

        private static final class ROFileData implements ReadOnlyRawFileData {

            private final Wrapper wrappedbasicRealFile;

            private final int suggestedDataGlimpseSize;

            public ROFileData(Wrapper basicRealFile, int suggestedDataGlimpseSize) {
                this.wrappedbasicRealFile = basicRealFile;
                this.suggestedDataGlimpseSize = suggestedDataGlimpseSize;
                basicRealFile.open();
            }

            @Override
            public ByteBuffer getDataGlimpse() {
                ByteBuffer buffer;
                if (suggestedDataGlimpseSize == 0) buffer = ByteBuffer.allocate(10); else buffer = ByteBuffer.allocate(suggestedDataGlimpseSize);
                SimpleReadRequest srr = new SimpleReadRequest(buffer, 0);
                try {
                    read(srr);
                } catch (Exception any) {
                    return buffer;
                }
                while (!srr.isCompleted()) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception any) {
                    }
                }
                return buffer;
            }

            @Override
            public String getName() {
                return wrappedbasicRealFile.getName();
            }

            @Override
            public void read(ReadRequest readRequest) throws Exception {
                wrappedbasicRealFile.read(readRequest);
            }

            @Override
            public long getFileSize() {
                return wrappedbasicRealFile.getFileSize();
            }

            @Override
            public boolean isOpen() {
                return wrappedbasicRealFile.isOpen();
            }

            @Override
            public void close() {
                wrappedbasicRealFile.cascadeReferenceCount.decrementAndGet();
            }
        }

        @Override
        public boolean isOpenByCascading() {
            return cascadeReferenceCount.get() > 0;
        }

        private boolean cannotClose = false;

        @Override
        public synchronized void setCannotClose(boolean cannotClose) {
            this.cannotClose = cannotClose;
        }
    }

    public static void main(String[] args) {
        DispatchReadRequestsInOtherThread drriot = new DispatchReadRequestsInOtherThread(new ReadImplementor() {

            @PartiallyCompleting
            @MightBeBlocking(reason = "A fool made this")
            @Blocking
            @Override
            public void readImpl(ReadRequest read) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isOpen() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getThreadName() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }, RequestHandlingApproach.BLOCKING);
    }
}
