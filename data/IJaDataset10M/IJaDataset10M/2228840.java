package net.jxta.impl.xindice.core.filer;

import net.jxta.impl.xindice.core.DBException;
import net.jxta.impl.xindice.core.FaultCodes;
import net.jxta.impl.xindice.core.data.Key;
import net.jxta.impl.xindice.core.data.Value;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Paged is a paged file implementation that is foundation for both the
 * BTree class and the HashFiler. It provides flexible paged I/O and
 * page caching functionality.
 * <p/>
 * Page has folowing configuration attributes:
 * <ul>
 * <li><strong>pagesize</strong>: Size of the page used by the paged file.
 * Default page size is 4096 bytes. This parameter can be set only
 * before paged file is created. Once it is created, this parameter
 * can not be changed.</li>
 * <li><strong>maxkeysize</strong>: Maximum allowed size of the key.
 * Default maximum key size is 256 bytes.</li>
 * <li><strong>max-descriptors</strong>: Defines maximum amount of
 * simultaneously opened file descriptors this paged file can have.
 * Several descriptors are needed to provide multithreaded access
 * to the underlying file. Too large number will limit amount of
 * collections you can open. Default value is 16
 * (DEFAULT_DESCRIPTORS_MAX).</li>
 * </ul>
 * <p/>
 * <br>FIXME: Currently it seems that maxkeysize is not used anywhere.
 * <br>TODO: Introduce Paged interface, implementations.
 */
public abstract class Paged {

    /**
     * Logger
     */
    private static final Logger LOG = Logger.getLogger(Paged.class.getName());

    /**
     * The maximum number of pages that will be held in the dirty cache.
     * Once number reaches the limit, pages are flushed to disk.
     */
    private static final int MAX_DIRTY_SIZE = 128;

    private static final int DEFAULT_DESCRIPTORS_MAX = 16;

    /**
     * Unused page status
     */
    protected static final byte UNUSED = 0;

    /**
     * Overflow page status
     */
    protected static final byte OVERFLOW = 126;

    /**
     * Deleted page status
     */
    protected static final byte DELETED = 127;

    /**
     * Page ID of non-existent page
     */
    protected static final int NO_PAGE = -1;

    /**
     * flag whether to sync DB on every write or not.
     */
    protected boolean sync = true;

    /**
     * Cache of recently read pages.
     * <p/>
     * Cache contains weak references to the Page objects, keys are page numbers (Long objects).
     * Access synchronized by this map itself.
     */
    private final Map<Long, WeakReference<Page>> pages = new WeakHashMap<Long, WeakReference<Page>>();

    /**
     * Cache of modified pages waiting to be written out.
     * Access is synchronized by the {@link #dirtyLock}.
     */
    private Map<Long, Page> dirty = new HashMap<Long, Page>();

    /**
     * Lock for synchronizing access to the {@link #dirty} map.
     */
    private final Object dirtyLock = new Object();

    /**
     * Random access file descriptors cache.
     * Access to it and to {@link #descriptorsCount} is synchronized by itself.
     */
    private final Stack<RandomAccessFile> descriptors = new Stack<RandomAccessFile>();

    /**
     * The number of random access file objects that exist, either in the
     * cache {@link #descriptors}, or currently in use.
     */
    private int descriptorsCount;

    /**
     * The maximum number of random access file objects that can be opened
     * by this paged instance.
     */
    private int descriptorsMax;

    /**
     * Whether the file is opened or not.
     */
    private boolean opened;

    /**
     * The underlying file where the Paged object stores its pages.
     */
    private File file;

    /**
     * Header of this Paged
     */
    private final FileHeader fileHeader;

    public Paged() {
        descriptorsMax = DEFAULT_DESCRIPTORS_MAX;
        fileHeader = createFileHeader();
    }

    public Paged(File file) {
        this();
        setFile(file);
    }

    /**
     * setFile sets the file object for this Paged.
     *
     * @param file The File
     */
    protected final void setFile(final File file) {
        this.file = file;
    }

    /**
     * getFile returns the file object for this Paged.
     *
     * @return The File
     */
    protected final File getFile() {
        return file;
    }

    /**
     * Obtain RandomAccessFile ('descriptor') object out of the pool.
     * If no descriptors available, and maximum amount already allocated,
     * the call will block.
     * @return the file
     * @throws java.io.IOException if an io error occurs
     */
    protected final RandomAccessFile getDescriptor() throws IOException {
        synchronized (descriptors) {
            if (!descriptors.empty()) {
                return descriptors.pop();
            }
            if (descriptorsCount < descriptorsMax) {
                descriptorsCount++;
                return new RandomAccessFile(file, "rw");
            }
            while (true) {
                try {
                    descriptors.wait();
                    return descriptors.pop();
                } catch (InterruptedException e) {
                } catch (EmptyStackException e) {
                }
            }
        }
    }

    /**
     * Puts a RandomAccessFile ('descriptor') back into the descriptor pool.
     * @param raf the file to add
     */
    protected final void putDescriptor(RandomAccessFile raf) {
        if (raf != null) {
            synchronized (descriptors) {
                descriptors.push(raf);
                descriptors.notify();
            }
        }
    }

    /**
     * Closes a RandomAccessFile ('descriptor') and removes it from the pool.
     * @param raf the file to close
     */
    protected final void closeDescriptor(RandomAccessFile raf) {
        if (raf != null) {
            try {
                raf.close();
            } catch (IOException e) {
            }
            synchronized (descriptors) {
                descriptorsCount--;
            }
        }
    }

    /**
     * getPage returns the page specified by pageNum.
     *
     * @param pageNum The Page number
     * @return The requested Page
     * @throws IOException if an Exception occurs
     */
    protected final Page getPage(long pageNum) throws IOException {
        final Long lp = pageNum;
        Page page;
        synchronized (this) {
            page = dirty.get(lp);
            if (page == null) {
                WeakReference<Page> ref = pages.get(lp);
                if (ref != null) {
                    page = ref.get();
                }
            }
            if (page == null) {
                page = new Page(lp);
                pages.put(page.pageNum, new WeakReference<Page>(page));
            }
        }
        page.read();
        return page;
    }

    /**
     * readValue reads the multi-Paged Value starting at the specified
     * Page.
     *
     * @param page The starting Page
     * @return The Value
     * @throws IOException if an Exception occurs
     */
    protected final Value readValue(Page page) throws IOException {
        final PageHeader sph = page.getPageHeader();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(sph.getRecordLen());
        Page p = page;
        while (true) {
            PageHeader ph = p.getPageHeader();
            p.streamTo(bos);
            long nextPage = ph.getNextPage();
            if (nextPage == NO_PAGE) {
                break;
            }
            p = getPage(nextPage);
        }
        return new Value(bos.toByteArray());
    }

    /**
     * readValue reads the multi-Paged Value starting at the specified
     * page number.
     *
     * @param page The starting page number
     * @return The Value
     * @throws IOException if an Exception occurs
     */
    protected final Value readValue(long page) throws IOException {
        return readValue(getPage(page));
    }

    /**
     * writeValue writes the multi-Paged Value starting at the specified
     * Page.
     *
     * @param page  The starting Page
     * @param value The Value to write
     * @throws IOException if an Exception occurs
     */
    protected final void writeValue(Page page, Value value) throws IOException {
        if (value == null) {
            throw new IOException("Can't write a null value");
        }
        InputStream is = value.getInputStream();
        PageHeader hdr = page.getPageHeader();
        hdr.setRecordLen(value.getLength());
        page.streamFrom(is);
        while (is.available() > 0) {
            Page lpage = page;
            PageHeader lhdr = hdr;
            long np = lhdr.getNextPage();
            if (np != NO_PAGE) {
                page = getPage(np);
            } else {
                page = getFreePage();
                lhdr.setNextPage(page.getPageNum());
            }
            hdr = page.getPageHeader();
            hdr.setStatus(OVERFLOW);
            page.streamFrom(is);
            lpage.write();
        }
        long np = hdr.getNextPage();
        if (np != NO_PAGE) {
            unlinkPages(np);
        }
        hdr.setNextPage(NO_PAGE);
        page.write();
    }

    /**
     * writeValue writes the multi-Paged Value starting at the specified
     * page number.
     *
     * @param page  The starting page number
     * @param value The Value to write
     * @throws IOException if an Exception occurs
     */
    protected final void writeValue(long page, Value value) throws IOException {
        writeValue(getPage(page), value);
    }

    /**
     * unlinkPages unlinks a set of pages starting at the specified Page.
     *
     * @param page The starting Page to unlink
     * @throws IOException if an Exception occurs
     */
    protected final void unlinkPages(Page page) throws IOException {
        if (page.pageNum < fileHeader.pageCount) {
            long nextPage = page.header.nextPage;
            page.header.setStatus(DELETED);
            page.header.setNextPage(NO_PAGE);
            page.write();
            if (nextPage == NO_PAGE) {
                page = null;
            } else {
                page = getPage(nextPage);
            }
        }
        if (page != null) {
            long firstPage = page.pageNum;
            while (page.header.nextPage != NO_PAGE) {
                page = getPage(page.header.nextPage);
            }
            long lastPage = page.pageNum;
            synchronized (fileHeader) {
                if (fileHeader.lastFreePage != NO_PAGE) {
                    Page p = getPage(fileHeader.lastFreePage);
                    p.header.setNextPage(firstPage);
                    p.write();
                }
                if (fileHeader.firstFreePage == NO_PAGE) {
                    fileHeader.setFirstFreePage(firstPage);
                }
                fileHeader.setLastFreePage(lastPage);
            }
        }
    }

    /**
     * unlinkPages unlinks a set of pages starting at the specified
     * page number.
     *
     * @param pageNum The starting page number to unlink
     * @throws IOException if an Exception occurs
     */
    protected final void unlinkPages(long pageNum) throws IOException {
        unlinkPages(getPage(pageNum));
    }

    /**
     * getFreePage returns the first free Page from secondary storage.
     * If no Pages are available, the file is grown as appropriate.
     *
     * @return The next free Page
     * @throws IOException if an Exception occurs
     */
    protected final Page getFreePage() throws IOException {
        Page p = null;
        synchronized (fileHeader) {
            if (fileHeader.firstFreePage != NO_PAGE) {
                p = getPage(fileHeader.firstFreePage);
                fileHeader.setFirstFreePage(p.getPageHeader().nextPage);
                if (fileHeader.firstFreePage == NO_PAGE) {
                    fileHeader.setLastFreePage(NO_PAGE);
                }
            }
        }
        if (p == null) {
            p = getPage(fileHeader.incTotalCount());
        }
        p.header.setNextPage(NO_PAGE);
        p.header.setStatus(UNUSED);
        return p;
    }

    /**
     * @throws DBException COL_COLLECTION_CLOSED if paged file is closed
     */
    protected final void checkOpened() throws DBException {
        if (!opened) {
            throw new FilerException(FaultCodes.COL_COLLECTION_CLOSED, "Filer is closed");
        }
    }

    /**
     * getFileHeader returns the FileHeader
     *
     * @return The FileHeader
     */
    public FileHeader getFileHeader() {
        return fileHeader;
    }

    public boolean exists() {
        return file.exists();
    }

    public boolean create() throws DBException {
        try {
            createFile();
            fileHeader.write();
            flush();
            return true;
        } catch (Exception e) {
            throw new FilerException(FaultCodes.GEN_CRITICAL_ERROR, "Error creating " + file.getName(), e);
        }
    }

    private void createFile() throws IOException {
        RandomAccessFile raf = null;
        try {
            raf = getDescriptor();
            long o = fileHeader.headerSize + (fileHeader.pageCount + 1) * fileHeader.pageSize - 1;
            raf.seek(o);
            raf.write(0);
        } finally {
            putDescriptor(raf);
        }
    }

    public boolean open() throws DBException {
        RandomAccessFile raf = null;
        try {
            if (exists()) {
                raf = getDescriptor();
                fileHeader.read();
                opened = true;
            } else {
                opened = false;
            }
            return opened;
        } catch (Exception e) {
            throw new FilerException(FaultCodes.GEN_CRITICAL_ERROR, "Error opening " + file.getName(), e);
        } finally {
            putDescriptor(raf);
        }
    }

    public synchronized boolean close() throws DBException {
        if (isOpened()) {
            try {
                opened = false;
                flush();
                synchronized (descriptors) {
                    final int total = descriptorsCount;
                    while (!descriptors.empty()) {
                        closeDescriptor(descriptors.pop());
                    }
                    int n = descriptorsCount;
                    while (descriptorsCount > 0 && n > 0) {
                        try {
                            descriptors.wait(500);
                        } catch (InterruptedException woken) {
                            Thread.interrupted();
                        }
                        if (descriptors.isEmpty()) {
                            n--;
                        } else {
                            closeDescriptor(descriptors.pop());
                        }
                    }
                    if (descriptorsCount > 0) {
                        LOG.fine(descriptorsCount + " out of " + total + " files were not closed during close.");
                    }
                }
            } catch (Exception e) {
                opened = true;
                throw new FilerException(FaultCodes.GEN_CRITICAL_ERROR, "Error closing " + file.getName(), e);
            }
        }
        return true;
    }

    public boolean isOpened() {
        return opened;
    }

    public boolean drop() throws DBException {
        try {
            close();
            return !exists() || getFile().delete();
        } catch (Exception e) {
            throw new FilerException(FaultCodes.COL_CANNOT_DROP, "Can't drop " + file.getName(), e);
        }
    }

    void addDirty(Page page) throws IOException {
        synchronized (dirtyLock) {
            dirty.put(page.pageNum, page);
            if (dirty.size() > MAX_DIRTY_SIZE) {
                try {
                    flush();
                } catch (Exception e) {
                    throw new IOException(e.getMessage());
                }
            }
        }
    }

    public void flush() throws DBException {
        int error = 0;
        Collection<Page> pages;
        synchronized (dirtyLock) {
            pages = dirty.values();
            dirty = new HashMap<Long, Page>();
        }
        for (Object page : pages) {
            Page p = (Page) page;
            try {
                p.flush();
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Exception while flushing page", e);
                error++;
            }
        }
        if (fileHeader.dirty) {
            try {
                fileHeader.write();
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Exception while flushing file header", e);
                error++;
            }
        }
        if (error != 0) {
            throw new FilerException(FaultCodes.GEN_CRITICAL_ERROR, "Error performing flush! Failed to flush " + error + " pages!");
        }
    }

    /**
     * createFileHeader must be implemented by a Paged implementation
     * in order to create an appropriate subclass instance of a FileHeader.
     *
     * @return a new FileHeader
     */
    public abstract FileHeader createFileHeader();

    /**
     * createFileHeader must be implemented by a Paged implementation
     * in order to create an appropriate subclass instance of a FileHeader.
     *
     * @param read If true, reads the FileHeader from disk
     * @return a new FileHeader
     * @throws IOException if an exception occurs
     */
    public abstract FileHeader createFileHeader(boolean read) throws IOException;

    /**
     * createFileHeader must be implemented by a Paged implementation
     * in order to create an appropriate subclass instance of a FileHeader.
     *
     * @param pageCount The number of pages to allocate for primary storage
     * @return a new FileHeader
     */
    public abstract FileHeader createFileHeader(long pageCount);

    /**
     * createFileHeader must be implemented by a Paged implementation
     * in order to create an appropriate subclass instance of a FileHeader.
     *
     * @param pageCount The number of pages to allocate for primary storage
     * @param pageSize  The size of a Page (should be a multiple of a FS block)
     * @return a new FileHeader
     */
    public abstract FileHeader createFileHeader(long pageCount, int pageSize);

    /**
     * createPageHeader must be implemented by a Paged implementation
     * in order to create an appropriate subclass instance of a PageHeader.
     *
     * @return a new PageHeader
     */
    public abstract PageHeader createPageHeader();

    public static Value[] insertArrayValue(Value[] vals, Value val, int idx) {
        Value[] newVals = new Value[vals.length + 1];
        if (idx > 0) {
            System.arraycopy(vals, 0, newVals, 0, idx);
        }
        newVals[idx] = val;
        if (idx < vals.length) {
            System.arraycopy(vals, idx, newVals, idx + 1, vals.length - idx);
        }
        return newVals;
    }

    public static Value[] deleteArrayValue(Value[] vals, int idx) {
        Value[] newVals = new Value[vals.length - 1];
        if (idx > 0) {
            System.arraycopy(vals, 0, newVals, 0, idx);
        }
        if (idx < newVals.length) {
            System.arraycopy(vals, idx + 1, newVals, idx, newVals.length - idx);
        }
        return newVals;
    }

    public static long[] insertArrayLong(long[] vals, long val, int idx) {
        long[] newVals = new long[vals.length + 1];
        if (idx > 0) {
            System.arraycopy(vals, 0, newVals, 0, idx);
        }
        newVals[idx] = val;
        if (idx < vals.length) {
            System.arraycopy(vals, idx, newVals, idx + 1, vals.length - idx);
        }
        return newVals;
    }

    public static long[] deleteArrayLong(long[] vals, int idx) {
        long[] newVals = new long[vals.length - 1];
        if (idx > 0) {
            System.arraycopy(vals, 0, newVals, 0, idx);
        }
        if (idx < newVals.length) {
            System.arraycopy(vals, idx + 1, newVals, idx, newVals.length - idx);
        }
        return newVals;
    }

    public static int[] insertArrayInt(int[] vals, int val, int idx) {
        int[] newVals = new int[vals.length + 1];
        if (idx > 0) {
            System.arraycopy(vals, 0, newVals, 0, idx);
        }
        newVals[idx] = val;
        if (idx < vals.length) {
            System.arraycopy(vals, idx, newVals, idx + 1, vals.length - idx);
        }
        return newVals;
    }

    public static int[] deleteArrayInt(int[] vals, int idx) {
        int[] newVals = new int[vals.length - 1];
        if (idx > 0) {
            System.arraycopy(vals, 0, newVals, 0, idx);
        }
        if (idx < newVals.length) {
            System.arraycopy(vals, idx + 1, newVals, idx, newVals.length - idx);
        }
        return newVals;
    }

    public static short[] insertArrayShort(short[] vals, short val, int idx) {
        short[] newVals = new short[vals.length + 1];
        if (idx > 0) {
            System.arraycopy(vals, 0, newVals, 0, idx);
        }
        newVals[idx] = val;
        if (idx < vals.length) {
            System.arraycopy(vals, idx, newVals, idx + 1, vals.length - idx);
        }
        return newVals;
    }

    public static short[] deleteArrayShort(short[] vals, int idx) {
        short[] newVals = new short[vals.length - 1];
        if (idx > 0) {
            System.arraycopy(vals, 0, newVals, 0, idx);
        }
        if (idx < newVals.length) {
            System.arraycopy(vals, idx + 1, newVals, idx, newVals.length - idx);
        }
        return newVals;
    }

    /**
     * Paged file's header
     */
    public abstract class FileHeader {

        private boolean dirty = false;

        private int workSize;

        private short headerSize;

        private int pageSize;

        private long pageCount;

        private long totalCount;

        private long firstFreePage = -1;

        private long lastFreePage = -1;

        private byte pageHeaderSize = 64;

        private short maxKeySize = 256;

        private long recordCount;

        public FileHeader() {
            this(1024);
        }

        public FileHeader(long pageCount) {
            this(pageCount, 4096);
        }

        public FileHeader(long pageCount, int pageSize) {
            this.pageSize = pageSize;
            this.pageCount = pageCount;
            totalCount = pageCount;
            headerSize = (short) 4096;
            calculateWorkSize();
        }

        public FileHeader(boolean read) throws IOException {
            if (read) {
                read();
            }
        }

        public final synchronized void read() throws IOException {
            RandomAccessFile raf = null;
            try {
                raf = getDescriptor();
                raf.seek(0);
                read(raf);
                calculateWorkSize();
            } finally {
                putDescriptor(raf);
            }
        }

        public synchronized void read(RandomAccessFile raf) throws IOException {
            headerSize = raf.readShort();
            pageSize = raf.readInt();
            pageCount = raf.readLong();
            totalCount = raf.readLong();
            firstFreePage = raf.readLong();
            lastFreePage = raf.readLong();
            pageHeaderSize = raf.readByte();
            maxKeySize = raf.readShort();
            recordCount = raf.readLong();
        }

        public final synchronized void write() throws IOException {
            if (!dirty) {
                return;
            }
            RandomAccessFile raf = null;
            try {
                raf = getDescriptor();
                raf.seek(0);
                write(raf);
                dirty = false;
            } finally {
                putDescriptor(raf);
            }
        }

        public synchronized void write(RandomAccessFile raf) throws IOException {
            raf.writeShort(headerSize);
            raf.writeInt(pageSize);
            raf.writeLong(pageCount);
            raf.writeLong(totalCount);
            raf.writeLong(firstFreePage);
            raf.writeLong(lastFreePage);
            raf.writeByte(pageHeaderSize);
            raf.writeShort(maxKeySize);
            raf.writeLong(recordCount);
        }

        public final synchronized void setDirty() {
            dirty = true;
        }

        public final synchronized boolean isDirty() {
            return dirty;
        }

        /**
         * The size of the FileHeader. Usually 1 OS Page.
         * This method should be called only while initializing Paged, not during normal processing.
         * @param headerSize the new header size
         */
        public final synchronized void setHeaderSize(short headerSize) {
            this.headerSize = headerSize;
            dirty = true;
        }

        /**
         * The size of the FileHeader.  Usually 1 OS Page
         * @return the header size
         */
        public final synchronized short getHeaderSize() {
            return headerSize;
        }

        /**
         * The size of a page. Usually a multiple of a FS block.
         * This method should be called only while initializing Paged, not during normal processing.
         * @param pageSize the new page size
         */
        public final synchronized void setPageSize(int pageSize) {
            this.pageSize = pageSize;
            calculateWorkSize();
            dirty = true;
        }

        /**
         * The size of a page.  Usually a multiple of a FS block
         * @return the page size
         */
        public final synchronized int getPageSize() {
            return pageSize;
        }

        /**
         * The number of pages in primary storage.
         * This method should be called only while initializing Paged, not during normal processing.
         * @param pageCount the new page count
         */
        public final synchronized void setPageCount(long pageCount) {
            this.pageCount = pageCount;
            dirty = true;
        }

        /**
         * The number of pages in primary storage
         * @return the page count
         */
        public final synchronized long getPageCount() {
            return pageCount;
        }

        /**
         * The number of total pages in the file.
         * This method should be called only while initializing Paged, not during normal processing.
         * @param totalCount the new total count
         */
        public final synchronized void setTotalCount(long totalCount) {
            this.totalCount = totalCount;
            dirty = true;
        }

        public final synchronized long incTotalCount() {
            dirty = true;
            return this.totalCount++;
        }

        /**
         * The number of total pages in the file
         * @return the total count
         */
        public final synchronized long getTotalCount() {
            return totalCount;
        }

        /**
         * The first free page in unused secondary space
         * @param firstFreePage the new first free page
         */
        public final synchronized void setFirstFreePage(long firstFreePage) {
            this.firstFreePage = firstFreePage;
            dirty = true;
        }

        /**
         * The first free page in unused secondary space
         * @return the first free page
         */
        public final synchronized long getFirstFreePage() {
            return firstFreePage;
        }

        /**
         * The last free page in unused secondary space
         * @param lastFreePage sets the last free page
         */
        public final synchronized void setLastFreePage(long lastFreePage) {
            this.lastFreePage = lastFreePage;
            dirty = true;
        }

        /**
         * The last free page in unused secondary space
         * @return the last free page
         */
        public final synchronized long getLastFreePage() {
            return lastFreePage;
        }

        /**
         * Set the size of a page header.
         * <p/>
         * Normally, 64 is sufficient.
         * @param pageHeaderSize the new page header size
         */
        public final synchronized void setPageHeaderSize(byte pageHeaderSize) {
            this.pageHeaderSize = pageHeaderSize;
            calculateWorkSize();
            dirty = true;
        }

        /**
         * Get the size of a page header.
         * <p/>
         * Normally, 64 is sufficient
         * @return the page header size
         */
        public final synchronized byte getPageHeaderSize() {
            return pageHeaderSize;
        }

        /**
         * Set the maximum number of bytes a key can be.
         * <p/>
         * Normally, 256 is good
         * @param maxKeySize the new max key size
         */
        public final synchronized void setMaxKeySize(short maxKeySize) {
            this.maxKeySize = maxKeySize;
            dirty = true;
        }

        /**
         * Get the maximum number of bytes.
         * <p/>
         * Normally, 256 is good.
         * @return max key size
         */
        public final synchronized short getMaxKeySize() {
            return maxKeySize;
        }

        /**
         * Increment the number of records being managed by the file
         */
        public final synchronized void incRecordCount() {
            recordCount++;
            dirty = true;
        }

        /**
         * Decrement the number of records being managed by the file
         */
        public final synchronized void decRecordCount() {
            recordCount--;
            dirty = true;
        }

        /**
         * The number of records being managed by the file (not pages)
         * @return the record count
         */
        public final synchronized long getRecordCount() {
            return recordCount;
        }

        private synchronized void calculateWorkSize() {
            workSize = pageSize - pageHeaderSize;
        }

        public final synchronized int getWorkSize() {
            return workSize;
        }
    }

    /**
     * PageHeader
     */
    public abstract class PageHeader implements Streamable {

        private boolean dirty = false;

        private byte status = UNUSED;

        private int keyLen = 0;

        private int keyHash = 0;

        private int dataLen = 0;

        private int recordLen = 0;

        private long nextPage = -1;

        public PageHeader() {
        }

        public PageHeader(DataInputStream dis) throws IOException {
            read(dis);
        }

        public synchronized void read(DataInputStream dis) throws IOException {
            status = dis.readByte();
            dirty = false;
            if (status == UNUSED) {
                return;
            }
            keyLen = dis.readInt();
            if (keyLen < 0) {
                keyLen = 0;
            }
            keyHash = dis.readInt();
            dataLen = dis.readInt();
            recordLen = dis.readInt();
            nextPage = dis.readLong();
        }

        public synchronized void write(DataOutputStream dos) throws IOException {
            dirty = false;
            dos.writeByte(status);
            dos.writeInt(keyLen);
            dos.writeInt(keyHash);
            dos.writeInt(dataLen);
            dos.writeInt(recordLen);
            dos.writeLong(nextPage);
        }

        public final synchronized boolean isDirty() {
            return dirty;
        }

        public final synchronized void setDirty() {
            dirty = true;
        }

        /**
         * The status of this page (UNUSED, RECORD, DELETED, etc...)
         * @param status the new status
         */
        public final synchronized void setStatus(byte status) {
            this.status = status;
            dirty = true;
        }

        /**
         * The status of this page (UNUSED, RECORD, DELETED, etc...)
         * @return the status
         */
        public final synchronized byte getStatus() {
            return status;
        }

        public final synchronized void setKey(Key key) {
            setRecordLen(0);
            dataLen = 0;
            keyHash = key.getHash();
            keyLen = key.getLength();
            dirty = true;
        }

        /**
         * The length of the Key
         * @param keyLen the new key length
         */
        public final synchronized void setKeyLen(int keyLen) {
            this.keyLen = keyLen;
            dirty = true;
        }

        /**
         * The length of the Key
         * @return the key length
         */
        public final synchronized int getKeyLen() {
            return keyLen;
        }

        /**
         * The hashed value of the Key for quick comparisons
         * @param keyHash sets the key hash
         */
        public final synchronized void setKeyHash(int keyHash) {
            this.keyHash = keyHash;
            dirty = true;
        }

        /**
         * The hashed value of the Key for quick comparisons
         * @return the key hash
         */
        public final synchronized int getKeyHash() {
            return keyHash;
        }

        /**
         * The length of the Data
         * @param dataLen sets the data length
         */
        public final synchronized void setDataLen(int dataLen) {
            this.dataLen = dataLen;
            dirty = true;
        }

        /**
         * The length of the Data
         * @return the data length
         */
        public final synchronized int getDataLen() {
            return dataLen;
        }

        /**
         * The length of the Record's value
         * @param recordLen sets the record length
         */
        public synchronized void setRecordLen(int recordLen) {
            this.recordLen = recordLen;
            dirty = true;
        }

        /**
         * The length of the Record's value
         * @return record length
         */
        public final synchronized int getRecordLen() {
            return recordLen;
        }

        /**
         * The next page for this Record (if overflowed)
         * @param nextPage next page
         */
        public final synchronized void setNextPage(long nextPage) {
            this.nextPage = nextPage;
            dirty = true;
        }

        /**
         * The next page for this Record (if overflowed)
         * @return next page
         */
        public final synchronized long getNextPage() {
            return nextPage;
        }
    }

    /**
     * Paged file's page
     */
    public final class Page implements Comparable<Page> {

        /**
         * This page number
         */
        private final Long pageNum;

        /**
         * The Header for this Page
         */
        private final PageHeader header;

        /**
         * The offset into the file that this page starts
         */
        private final long offset;

        /**
         * The data for this page. Null if page is not loaded.
         */
        private byte[] data;

        /**
         * The position (relative) of the Key in the data array
         */
        private int keyPos;

        /**
         * The position (relative) of the Data in the data array
         */
        private int dataPos;

        public Page(Long pageNum) {
            this.header = createPageHeader();
            this.pageNum = pageNum;
            this.offset = fileHeader.headerSize + (pageNum * fileHeader.pageSize);
        }

        /**
         * Reads a page into the memory, once. Subsequent calls are ignored.
         * @throws java.io.IOException if an io error occurs
         */
        public synchronized void read() throws IOException {
            if (data == null) {
                RandomAccessFile raf = null;
                try {
                    byte[] data = new byte[fileHeader.pageSize];
                    raf = getDescriptor();
                    raf.seek(this.offset);
                    raf.read(data);
                    ByteArrayInputStream bis = new ByteArrayInputStream(data);
                    this.header.read(new DataInputStream(bis));
                    this.keyPos = fileHeader.pageHeaderSize;
                    this.dataPos = this.keyPos + this.header.keyLen;
                    this.data = data;
                } finally {
                    putDescriptor(raf);
                }
            }
        }

        /**
         * Writes out the header into the this.data, and adds a page to the set of
         * dirty pages.
         * @throws java.io.IOException if an io error occurs
         */
        public void write() throws IOException {
            synchronized (this) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream(fileHeader.getPageHeaderSize());
                header.write(new DataOutputStream(bos));
                byte[] b = bos.toByteArray();
                System.arraycopy(b, 0, data, 0, b.length);
            }
            Paged.this.addDirty(this);
        }

        /**
         * Flushes content of the dirty page into the file
         * @throws java.io.IOException if an io error occurs
         */
        public synchronized void flush() throws IOException {
            RandomAccessFile raf = null;
            try {
                raf = getDescriptor();
                if (this.offset >= raf.length()) {
                    long o = (fileHeader.headerSize + ((fileHeader.totalCount * 3) / 2) * fileHeader.pageSize) + (fileHeader.pageSize - 1);
                    raf.seek(o);
                    raf.writeByte(0);
                }
                raf.seek(this.offset);
                raf.write(this.data);
                if (sync) {
                    raf.getFD().sync();
                }
            } finally {
                putDescriptor(raf);
            }
        }

        public Long getPageNum() {
            return this.pageNum;
        }

        public PageHeader getPageHeader() {
            return this.header;
        }

        public synchronized void setKey(Key key) {
            header.setKey(key);
            key.copyTo(this.data, this.keyPos);
            this.dataPos = this.keyPos + header.keyLen;
        }

        public synchronized Key getKey() {
            if (header.keyLen > 0) {
                return new Key(this.data, this.keyPos, header.keyLen);
            } else {
                return null;
            }
        }

        public synchronized void streamTo(OutputStream os) throws IOException {
            if (header.dataLen > 0) {
                os.write(this.data, this.dataPos, header.dataLen);
            }
        }

        public synchronized void streamFrom(InputStream is) throws IOException {
            int avail = is.available();
            header.dataLen = fileHeader.workSize - header.keyLen;
            if (avail < header.dataLen) {
                header.dataLen = avail;
            }
            if (header.dataLen > 0) {
                is.read(this.data, this.keyPos + header.keyLen, header.dataLen);
            }
        }

        public int compareTo(Page o) {
            return (int) (this.pageNum - o.pageNum);
        }
    }
}
