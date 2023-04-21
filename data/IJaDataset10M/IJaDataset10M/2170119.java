package com.fluidops.tools.vmfs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import com.fluidops.base.Version;
import com.fluidops.util.HexDump;
import com.fluidops.util.StringUtil;
import com.fluidops.util.logging.Debug;

/**
 * The VMware VMFS driver.
 * Provides directory and file access to VMFS volumes.
 * 
 * @author Uli
 */
public class VMFSDriver {

    public static class UUID {

        public byte[] uuid__16;

        String twoDigitHex(byte b) {
            String s = Integer.toHexString((b & 0xff));
            if (s.length() < 2) s = "0" + s;
            return s;
        }

        public String toString() {
            if (uuid__16 == null) return "(null)";
            String s = "";
            for (int p = 3; p >= 0; p--) s += twoDigitHex(uuid__16[p]);
            s += "-";
            for (int p = 3; p >= 0; p--) s += twoDigitHex(uuid__16[p + 4]);
            s += "-";
            for (int p = 1; p >= 0; p--) s += twoDigitHex(uuid__16[p + 8]);
            s += "-";
            for (int p = 0; p <= 5; p++) s += twoDigitHex(uuid__16[p + 10]);
            return s;
        }
    }

    public static class VolumeInfo {

        public int magic;

        public int version;

        public String name__28_0x12;

        public UUID uuid__0x82;

        public long unknown__0x92;

        public long unknown__0x9a;
    }

    public static class LVMInfo {

        public long size;

        public long blocks;

        public int unknown;

        public String uuidString__35;

        byte[] fill__0x1d;

        public UUID uuid__0x54;

        public int unknown2;

        public long ctime;

        public int unknown3;

        public int numberOfSegments;

        public int firstSegment;

        public int unknown4;

        public int lastSegment;

        public int unknown5;

        public long mtime;

        public int numberOfExtents;
    }

    public static class FSInfo {

        public int magic;

        public int volumeVersion;

        public byte version;

        public UUID uuid;

        public int unknown2;

        public String label__128;

        public long blocksize__0xa1;

        public int timestamp;

        public int unknownFlag;

        public UUID volumeUuid__0xb1;
    }

    public static final int VMFS_HB_MAGIC_OFF = 0xabcdef01;

    public static final int VMFS_HB_MAGIC_ON = 0xabcdef02;

    public static final long VMFS_HB_BASE = 0x0300000L;

    public static final int VMFS_HB_SIZE = 0x200;

    public static final int VMFS_HB_NUM = 2048;

    public static class HeartbeatRecord {

        public int magic;

        public long pos;

        public long seq;

        public long uptime;

        public UUID uuid;

        public int journalBlock;

        public int volVersion;

        public int version;
    }

    public static final int TYPE_FOLDER = 2;

    public static final int TYPE_FILE = 3;

    public static final int TYPE_SYMLINK = 4;

    public static final int TYPE_META = 5;

    public static final int TYPE_RDM = 6;

    public static final int DIR_ENTRY_SIZE = 140;

    public static class FileRecord {

        public int type;

        public int blockId;

        public int recordId;

        public String name__128;

        public boolean isFolder() {
            return type == TYPE_FOLDER || type == TYPE_SYMLINK;
        }

        public boolean equals(Object other) {
            return other instanceof FileRecord && ((FileRecord) other).blockId == blockId && ((FileRecord) other).recordId == recordId && ((FileRecord) other).name__128.equals(name__128);
        }

        public int hashCode() {
            return name__128.hashCode();
        }

        public String toString() {
            return name__128 + " (" + (isFolder() ? "folder" : "file") + ")";
        }
    }

    public static final int FILE_RECORD_MAGIC = 0x10c00001;

    public static class FileMetaHeader {

        public int magic;

        public long position;

        public long hbPos, hbSeq;

        public long objSeq;

        public int hbLock;

        public UUID hbUuid;

        public long mtime;
    }

    public static class FileMetaRecord {

        public int id;

        public int id2;

        public int nlink, type, flags;

        public long size;

        public long blockSize, blockCount;

        public int timeStamp1__0x2c;

        public int timeStamp2;

        public int timeStamp3;

        public int uid, gid, mode;

        public int zla;

        public int tbz;

        public int cow;
    }

    public static class RDMMetaRecord {

        public int unknownid;

        public short unknownid2;

        public String lunType__28;

        public int blocks;

        public String lunUuid__17;
    }

    public static class BlockID {

        public int id;

        public int subgroup;

        public int type;

        public int number;

        public BlockID(int id) {
            this.id = id;
            type = id & 7;
            toString();
        }

        public String toString() {
            String s = null;
            switch(type) {
                case 0:
                    s = "NULL";
                    if (id != 0) s = "NULL(INVALID)";
                    break;
                case 1:
                    s = "FullBlock";
                    subgroup = (id & 32) != 0 ? 1 : 0;
                    number = (id & 0xffffffff) >> 6;
                    break;
                case 2:
                    s = "SubBlock";
                    subgroup = (id >> 28) & 0xf;
                    number = (id & 0xfffffff) >> 6;
                    break;
                case 3:
                    s = "PointerBlock";
                    subgroup = (id >> 28) & 0xf;
                    number = (id & 0xfffffff) >> 6;
                    break;
                case 4:
                    s = "FileDescriptor";
                    subgroup = (id >> 6) & 0x7fff;
                    number = (id >> 22) & 0x3ff;
                    break;
                default:
                    s = "UNKNOWN";
                    break;
            }
            return "Block " + s + "(" + Long.toHexString(id & 0xffffffffL) + ") type=" + type + " number=" + number + " subgroup=" + subgroup;
        }
    }

    public static class BitmapMetaHeader {

        public int blocks;

        public int count;

        public int headerSize;

        public int dataSize;

        public int areaSize;

        public int itemCount;

        public int areaCount;
    }

    public static class BitmapHeader {

        public int magic;

        public int id;

        public int flag__0x1c;

        public int bitmapId__0x200;

        public int total;

        public int free;

        public int ffree;

        public byte[] bitmap__0x100;
    }

    public static class MBRPartitionEntry {

        public byte bootable;

        public byte c, h, s;

        public byte partitionType;

        public byte c2, h2, s2;

        public int lbaStart;

        public int lbaSize;
    }

    IOAccess rf;

    long vmfsBase;

    int blockSize;

    VolumeInfo vi;

    LVMInfo lvm;

    FSInfo fs;

    boolean hyperverbose;

    /**
     * Reads a block from specified location.
     * @param pos
     * @param size
     * @return
     * @throws IOException
     */
    byte[] readBlock(long pos, int size) throws IOException {
        return rf.read(pos, size);
    }

    /**
     * This class encapsulates the VMFS allocation bitmaps.
     * Virtually all meta data is managed using these allocation tables.
     * 
     * @author Uli
     */
    class BitmappedBlockAllocation {

        IOAccess io;

        long basePos;

        long firstBitmapPos;

        BitmapMetaHeader bmp;

        public static final int VMFS_BITMAP_ENTRY_SIZE = 0x400;

        BitmappedBlockAllocation(IOAccess io) throws Exception {
            this(io, 0);
        }

        BitmappedBlockAllocation(IOAccess io, long basePos) throws Exception {
            this.io = io;
            this.basePos = basePos;
            readBitmapInfo();
        }

        long getDataBlockAreaSize() {
            return bmp.areaSize;
        }

        long getDataBlockSize() {
            return bmp.dataSize;
        }

        long getDataBlockAddress(long block) {
            long itemsPerArea = (bmp.count * bmp.blocks);
            long area = block / itemsPerArea;
            long mod = block % itemsPerArea;
            long areaAddr = getDataBlockAreaAddress(area);
            long res = areaAddr + mod * getDataBlockSize();
            if (hyperverbose) Debug.out.println("blockAddr(" + block + ")=" + res);
            return res;
        }

        long getDataBlockAreaAddress(long area) {
            long bmpMetaSize = VMFS_BITMAP_ENTRY_SIZE * bmp.count;
            long areaAddress = firstBitmapPos + area * bmp.areaSize;
            long res = basePos + areaAddress + bmpMetaSize;
            if (hyperverbose) Debug.out.println("areaAddress(" + area + ")=" + Long.toHexString(res));
            return res;
        }

        /**
         * Reads bitmap info.
         * @throws Exception
         */
        void readBitmapInfo() throws Exception {
            long pos = basePos;
            Debug.out.println("Reading block allocation bitmap from " + io);
            Debug.out.println("Bitmap @" + Long.toHexString(pos));
            byte[] header = io.read(pos, 0x800);
            bmp = new BitmapMetaHeader();
            NativeStruct.fromNative(bmp, header, 0);
            Debug.out.println("Blocks per bitmap = " + bmp.blocks);
            Debug.out.println("Managed items = " + bmp.itemCount);
            Debug.out.println("Count = " + bmp.count);
            Debug.out.println("Data size = " + bmp.dataSize);
            Debug.out.println("Area size = " + bmp.areaSize);
            Debug.out.println("Header size = " + bmp.headerSize);
            firstBitmapPos = bmp.headerSize;
            pos += firstBitmapPos;
        }

        void scanAllBitmaps() throws Exception {
            long pos = basePos + firstBitmapPos;
            int totalItems = bmp.itemCount;
            int expectedBitmapId = 0;
            int dataBlockCount = 0;
            int magic = 0;
            while (totalItems > 0) {
                io.setPosition(pos);
                Debug.out.println("Bitmap headers @" + Long.toHexString(io.getPosition()));
                byte[] bmpBuf = new byte[VMFS_BITMAP_ENTRY_SIZE];
                for (int bitmapCount = 0; bitmapCount < bmp.count; bitmapCount++) {
                    io.read(bmpBuf, 0, bmpBuf.length);
                    BitmapHeader bmpHdr = new BitmapHeader();
                    NativeStruct.fromNative(bmpHdr, bmpBuf, 0);
                    if (magic == 0) {
                        magic = bmpHdr.magic;
                    } else {
                        if (magic != bmpHdr.magic) throw new IOException("Wrong magic in bitmap: " + magic + " expected was " + bmpHdr.magic);
                    }
                    if (expectedBitmapId != bmpHdr.bitmapId__0x200) throw new IOException("Wrong ID in bitmap: " + bmpHdr.bitmapId__0x200 + " expected was " + expectedBitmapId);
                    expectedBitmapId++;
                    totalItems -= bmpHdr.total;
                    if (totalItems <= 0) break;
                }
                long dataAddr = getDataBlockAreaAddress(dataBlockCount);
                if (dataAddr != io.getPosition()) Debug.err.println("POsition of block " + dataBlockCount + ": " + dataAddr + " != " + io.getPosition());
                dataBlockCount++;
                pos = io.getPosition() + bmp.areaSize - (bmp.count * VMFS_BITMAP_ENTRY_SIZE);
                Debug.out.println("XXX@" + Long.toHexString(io.getPosition()) + " itemsLeft=" + totalItems);
            }
        }
    }

    /**
     * Reads bitmap info.
     * @throws Exception
     */
    void readBitmapInfo(IOAccess io, long pos) throws Exception {
        Debug.out.println("Reading block allocation bitmap from " + io);
        Debug.out.println("Bitmap @" + Long.toHexString(pos));
        byte[] header = io.read(pos, 0x800);
        BitmapMetaHeader bmp = new BitmapMetaHeader();
        NativeStruct.fromNative(bmp, header, 0);
        Debug.out.println("Blocks per bitmap = " + bmp.blocks);
        Debug.out.println("Managed items = " + bmp.itemCount);
        Debug.out.println("Count = " + bmp.count);
        long dataBlockSize = (long) bmp.areaSize;
        Debug.out.println("Data item size = " + Long.toHexString(dataBlockSize));
        pos += 0x10000;
        int totalItems = bmp.itemCount;
        int expectedBitmapId = 0;
        int magic = 0;
        while (totalItems > 0) {
            io.setPosition(pos);
            Debug.out.println("Bitmap headers @" + Long.toHexString(io.getPosition()));
            byte[] bmpBuf = new byte[0x400];
            for (int bitmapCount = 0; bitmapCount < bmp.count; bitmapCount++) {
                io.read(bmpBuf, 0, bmpBuf.length);
                BitmapHeader bmpHdr = new BitmapHeader();
                NativeStruct.fromNative(bmpHdr, bmpBuf, 0);
                if (magic == 0) {
                    magic = bmpHdr.magic;
                } else {
                    if (magic != bmpHdr.magic) throw new IOException("Wrong magic in bitmap: " + magic + " expected was " + bmpHdr.magic);
                }
                if (expectedBitmapId != bmpHdr.bitmapId__0x200) throw new IOException("Wrong ID in bitmap: " + bmpHdr.bitmapId__0x200 + " expected was " + expectedBitmapId);
                expectedBitmapId++;
                totalItems -= bmpHdr.total;
                if (totalItems <= 0) break;
            }
            pos = io.getPosition() + dataBlockSize;
            Debug.out.println("XXX@" + Long.toHexString(io.getPosition()) + " itemsLeft=" + totalItems);
        }
    }

    List<ExtentInfo> extents;

    public static final long LVM_SEGMENT_SIZE = 256L * 1024L * 1024L;

    /**
     * IOAccess that spans multiple VMFS LVM segments.
     * 
     * @author Uli
     */
    public class ExtentIOAccess extends IOAccess {

        long pos = 0;

        long size;

        public ExtentIOAccess() {
            size = extents.get(0).lvm.size;
        }

        ExtentInfo getExtentForOffset(long ofs) {
            long seg = ofs / LVM_SEGMENT_SIZE;
            for (ExtentInfo ex : extents) if (seg >= ex.lvm.firstSegment && seg <= ex.lvm.lastSegment) return ex;
            return null;
        }

        @Override
        public void close() {
            for (ExtentInfo ex : extents) ex.rf.close();
        }

        @Override
        public long getPosition() {
            return pos;
        }

        @Override
        public long getSize() {
            return size;
        }

        @Override
        public int read(byte[] buffer, int offset, int size) throws IOException {
            ExtentInfo ex = getExtentForOffset(pos);
            if (ex == null) throw new IOException("Extent for pos=" + pos + " not available");
            long posInExtent = pos - ex.lvm.firstSegment * LVM_SEGMENT_SIZE;
            if ((posInExtent + size) > ex.lvm.numberOfSegments * LVM_SEGMENT_SIZE) throw new IOException("IO operation exceeds LVM segment");
            ex.rf.setPosition(posInExtent + ex.vmfsBase + 0x1000000L);
            return ex.rf.read(buffer, offset, size);
        }

        @Override
        public void setPosition(long pos) {
            this.pos = pos;
        }

        @Override
        public void setSize(long newSize) {
            throw new RuntimeException("setSize not supported");
        }

        @Override
        public void write(byte[] buffer, int offset, int size) throws IOException {
            throw new IOException("Readonly mode");
        }
    }

    /**
     * This class encapsulates all info required for a
     * VMFS extent, which in turn spans multiple segments.
     * Each segment has size 256MB.
     * 
     * @author Uli
     */
    public static class ExtentInfo {

        IOAccess rf;

        long offset;

        long vmfsBase;

        VolumeInfo vi;

        LVMInfo lvm;

        /**
    	 * Opens extent on the given device.
    	 * 
    	 * If the VMFS is not stored RAW on the device,
    	 * it parses the MBR partition table for the 1st
    	 * VMware partition.
    	 * 
    	 * Throws exception if no VMFS extent was detected.
    	 * 
    	 * @param io
    	 * @throws IOException
    	 */
        public ExtentInfo(IOAccess io) throws IOException {
            this.rf = io;
            try {
                readVmfsLvmInfo();
            } catch (Exception ex) {
                if (ex instanceof IOException) throw (IOException) ex;
                throw new IOException(ex);
            }
        }

        /**
    	 * Reads/detects VMFS extent info.
    	 * 
    	 * @throws Exception
    	 */
        void readVmfsLvmInfo() throws Exception {
            try {
                readVmfsLvmInfo(0);
                offset = 0;
            } catch (Exception ex) {
                long start = getVMFSPartitionOffset();
                if (start > 0) {
                    Debug.out.println("Detected VMFS partition @" + start);
                    readVmfsLvmInfo(start);
                    offset = start;
                } else throw ex;
            }
        }

        /**
         * Detects/reads VMFS info structs.
         * @throws Exception
         */
        void readVmfsLvmInfo(long ofs) throws Exception {
            vi = null;
            long pos = 0x100000;
            byte[] header = new byte[0x800];
            while (pos < 0x100001) {
                rf.setPosition(ofs + pos);
                rf.read(header, 0, header.length);
                vi = new VolumeInfo();
                NativeStruct.fromNative(vi, header, 0);
                if (vi.magic == (int) 0xc001d00d) break;
                pos += 0x10000;
            }
            if (pos >= 0x100001) throw new Exception("No VMware File System detected");
            vmfsBase = ofs + pos;
            Debug.out.println("VMFS base @" + Long.toHexString(vmfsBase));
            lvm = new LVMInfo();
            NativeStruct.fromNative(lvm, header, 0x200);
            Debug.out.println("VMFS Volume at " + Long.toHexString(pos));
            Debug.out.println("Volume type = " + vi.name__28_0x12);
            Debug.out.println("Volume UUID = " + vi.uuid__0x82);
            Debug.out.println("LVM first,last,segments = " + lvm.firstSegment + "," + lvm.lastSegment + "," + lvm.numberOfSegments);
            Debug.out.println("LVM extents = " + lvm.numberOfExtents);
            Debug.out.println("Size = " + StringUtil.displaySizeInBytes(lvm.size));
            Debug.out.println("Blocks = " + lvm.blocks);
            Debug.out.println("LVM UUID       = " + lvm.uuid__0x54);
        }

        /**
         * Reads the device's MBR and searches for the VMFS
         * partition
         * 
         * @return Offset (in blocks) of the partition, or -1 if not found
         * @throws IOException
         * @throws Exception
         */
        long getVMFSPartitionOffset() throws IOException, Exception {
            long start = -1;
            byte[] mbr = rf.read(0, 512);
            if (mbr[510] == 0x55 && mbr[511] == (byte) 0xaa) {
                MBRPartitionEntry entry = new MBRPartitionEntry();
                for (int p = 0; p <= 3; p++) {
                    NativeStruct.fromNative(entry, mbr, 0x1be + p * 0x10);
                    if (entry.partitionType == (byte) 0xfb) {
                        start = 512L * (entry.lbaStart & 0xFFFFFFFFL);
                        break;
                    }
                }
            }
            return start;
        }
    }

    /**
     * Reads the VMFS info from the extent(s).
     * Validates some extent conditions.
     * 
     * @throws Exception
     */
    void readVmfsInfo() throws Exception {
        for (ExtentInfo ex : extents) if (ex.lvm.firstSegment == 0) {
            vi = ex.vi;
            lvm = ex.lvm;
        }
        if (vi == null) throw new IOException("First extent of VMFS missing, cannot open");
        if (lvm.numberOfExtents != extents.size()) System.err.println("Warning: only " + extents.size() + " of " + lvm.numberOfExtents + " VMFS extents present. Read errors on unavailable extents might occur");
        byte[] header = new byte[0x800];
        vmfsBase = 0;
        long pos = vmfsBase + 0x200000;
        Debug.out.println("@" + Long.toHexString(pos));
        rf.setPosition(pos);
        rf.read(header, 0, header.length);
        fs = new FSInfo();
        NativeStruct.fromNative(fs, header, 0);
        if (fs.magic != 0x2fabf15e) throw new Exception("VMFS FSInfo block not found");
        Debug.out.println("VMFS version = 3." + fs.version);
        Debug.out.println("VMFS label = " + fs.label__128);
        Debug.out.println("VMFS creation date = " + new java.util.Date(1000L * fs.timestamp));
        Debug.out.println("VMFS UUID = " + fs.uuid);
        Debug.out.println("VMFS block size = " + fs.blocksize__0xa1 + " / " + Long.toHexString(fs.blocksize__0xa1));
        Debug.out.println("VMFS volume version = " + fs.volumeVersion);
        blockSize = (int) fs.blocksize__0xa1;
        openHeartbeats();
    }

    /**
     * "Opens" the heartbeat section by verifying that the first
     * and the last entry have the correct magic number.
     * 
     * @throws Exception
     */
    void openHeartbeats() throws Exception {
        readHeartbeat(0);
        readHeartbeat(VMFS_HB_NUM - 1);
        Debug.out.println("Heartbeat records present");
    }

    /**
     * Reads the specified heartbeat record.
     * @param num
     * @return
     * @throws Exception
     */
    public HeartbeatRecord readHeartbeat(int num) throws Exception {
        if (num < 0 || num > VMFS_HB_NUM) throw new IllegalArgumentException("Heartbeat # outside range: " + num);
        long pos = vmfsBase + VMFS_HB_BASE + num * VMFS_HB_SIZE;
        rf.setPosition(pos);
        Debug.out.println("@" + Long.toHexString(pos));
        byte[] header = new byte[VMFS_HB_SIZE];
        rf.read(header, 0, header.length);
        HeartbeatRecord r = new HeartbeatRecord();
        NativeStruct.fromNative(r, header, 0);
        if (r.magic != VMFS_HB_MAGIC_ON && r.magic != VMFS_HB_MAGIC_OFF) throw new Exception("VMFS heartbeat record " + num + " @" + rf.getPosition() + " not found - wrong magic number");
        return r;
    }

    /**
     * Reads all heartbeat records, returns only active if onlyActive=true.
     * 
     * @param onlyActive
     * @return
     * @throws Exception
     */
    public List<HeartbeatRecord> readHeartbeats(boolean onlyActive) throws Exception {
        List<HeartbeatRecord> res = new ArrayList<HeartbeatRecord>();
        for (int hb = 0; hb < VMFS_HB_NUM; hb++) {
            HeartbeatRecord r = readHeartbeat(hb);
            if (!onlyActive || (onlyActive && r.magic == VMFS_HB_MAGIC_ON)) {
                res.add(r);
            }
        }
        return res;
    }

    List<FileRecord> frs;

    List<FileMetaInfo> fmis;

    /**
     * Searches the file record for the given file name.
     * @param fileName
     * @return Null if record not found
     */
    FileRecord getFileRecord(List<FileRecord> frs, String fileName) {
        for (FileRecord fr : frs) {
            if (fr.name__128.equals(fileName)) {
                return fr;
            }
        }
        return null;
    }

    FileRecord getFileRecord(String path) throws Exception {
        return _getFileRecord(path, true);
    }

    FileRecord getFileOrFolderRecord(String path) throws Exception {
        return _getFileRecord(path, false);
    }

    FileRecord _getFileRecord(String path, boolean onlyFiles) throws Exception {
        List<FileRecord> rs = frs;
        StringTokenizer st = new StringTokenizer(path, "/");
        FileRecord fr = null;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            fr = getFileRecord(rs, token);
            if (fr == null) return null;
            if (fr.isFolder()) {
                rs = getFileRecordsForDirectory(fr);
            } else {
                if (st.hasMoreTokens()) throw new IOException("Illegal path: " + path);
                return fr;
            }
        }
        if (onlyFiles) throw new IOException("File not found: " + path); else return fr;
    }

    Map<FileRecord, List<FileRecord>> dirCache = new HashMap<FileRecord, List<FileRecord>>();

    List<FileRecord> getFileRecordsForDirectory(FileRecord fr) throws IOException, Exception {
        List<FileRecord> rs = dirCache != null ? dirCache.get(fr) : null;
        if (rs != null) return rs;
        IOAccess io = new FileIOAccess(getMetaInfo(fr));
        if (io.getSize() == 0) rs = new ArrayList<FileRecord>(); else rs = readFileRecords(io, 0, (int) (io.getSize() / DIR_ENTRY_SIZE));
        io.close();
        if (dirCache != null) dirCache.put(fr, rs);
        return rs;
    }

    /**
     * Follow the symlink of the file record.
     * @param fr
     * @return
     */
    public String getSymLink(FileRecord fr) {
        if (fr.type == TYPE_SYMLINK) try {
            IOAccess io = new FileIOAccess(getMetaInfo(fr));
            int sz = (int) io.getSize();
            String symLink = new String(io.read(0, sz), "UTF-8");
            io.close();
            return symLink;
        } catch (Exception ex) {
            Debug.err.println("Symlink cannot be followed - ignoring");
        }
        return null;
    }

    /**
     * Lists the given directory.
     * @param path Full path to the directory.
     * @return The list of file records in the given directory
     * @throws Exception
     */
    public List<FileMetaInfo> dir(String path) throws Exception {
        List<FileMetaInfo> fmis = new ArrayList<FileMetaInfo>();
        List<FileRecord> frs = _dir(path, null);
        List<String> names = new ArrayList<String>();
        if (frs != null) for (FileRecord fr : frs) {
            if (".".equals(fr.name__128) || "..".equals(fr.name__128)) continue;
            if (names.contains(fr.name__128)) {
                System.err.println("Omitting duplicate entry: " + fr.name__128);
                System.err.println(fr + " type=" + fr.type + " bId=" + fr.blockId + " rId=" + fr.recordId);
                continue;
            }
            names.add(fr.name__128);
            FileMetaInfo fmi = getMetaInfo(fr);
            if (fmi != null) {
                fmi.fullPath = path;
                fmis.add(fmi);
            }
        }
        Collections.sort(fmis, new Comparator<FileMetaInfo>() {

            @Override
            public int compare(FileMetaInfo o1, FileMetaInfo o2) {
                return o1.fr.name__128.compareTo(o2.fr.name__128);
            }
        });
        return fmis;
    }

    List<FileRecord> _dir(String path, List<String> followed) throws Exception {
        List<FileRecord> rs = frs;
        StringTokenizer st = new StringTokenizer(path, "/");
        String partialPath = "/";
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            FileRecord fr = getFileRecord(rs, token);
            if (fr == null) return null;
            if (fr.type == TYPE_FOLDER) {
                rs = getFileRecordsForDirectory(fr);
            } else if (fr.type == TYPE_SYMLINK) {
                token = getSymLink(fr);
                String followLink = partialPath + token;
                if (followed == null) followed = new ArrayList<String>();
                if (followed.contains(followLink)) {
                    System.err.println("Error: cycle in symlink " + followLink);
                    return null;
                }
                followed.add(followLink);
                Debug.out.println("Followed link " + fr.name__128 + " => " + followLink);
                rs = _dir(followLink, followed);
            } else {
                throw new IOException("Illegal path: " + path);
            }
            partialPath += token + "/";
        }
        return rs;
    }

    /**
     * Returns the meta info for the given file name.
     * @param fileName
     * @return Null if not found
     */
    public FileMetaInfo getMetaInfoForFile(String fileName) {
        FileRecord fr;
        try {
            fr = getFileOrFolderRecord(fileName);
            return getMetaInfo(fr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    FileMetaInfo getMetaInfo(FileRecord fr) {
        if (fr != null) {
            for (FileMetaInfo fmi : fmis) if (fmi.fmr.id == fr.blockId) {
                fmi.fr = fr;
                return fmi;
            }
            BlockID bid = new BlockID(fr.blockId);
            try {
                long addr = bid.number * 0x800 + fdcBmp.getDataBlockAddress(bid.subgroup * fdcBmp.bmp.blocks);
                FileMetaInfo fmi = readFileMetaInfo(fdc, addr);
                if (fmi.fmr.id == fr.blockId) {
                    fmi.fr = fr;
                    fmis.add(fmi);
                    return fmi;
                } else {
                    System.err.println("Illegal file record -- fmr.id=" + fmi.fmr.id + " blockId=" + fr.blockId);
                }
            } catch (Exception ex) {
                System.err.println("Cannot read file record: " + bid + " error was: " + ex);
            }
        }
        return null;
    }

    /**
     * Opens the VMFS
     * @throws Exception
     */
    public void openVmfs() throws Exception {
        Debug.out.println("Opening VMFS on IOAccess=" + rf);
        readVmfsInfo();
        long ofs = 0;
        for (; ; ) {
            try {
                BitmappedBlockAllocation tempFdcBmp = new BitmappedBlockAllocation(rf, vmfsBase + 0x400000 + ofs);
                long fdcBaseZero = tempFdcBmp.getDataBlockAddress(0);
                Debug.out.println("FDC base zero = " + Long.toHexString(fdcBaseZero));
                frs = readFileRecords(rf, vmfsBase + 0x400000 + ofs + blockSize, 1);
                fmis = readFileMetaInfos(rf, fdcBaseZero, 1);
                long rootDirSize = fmis.get(0).fmr.size;
                int rootDirEntries = (int) (rootDirSize / DIR_ENTRY_SIZE);
                frs = readFileRecords(rf, vmfsBase + 0x400000 + ofs + blockSize, rootDirEntries);
                fmis = readFileMetaInfos(rf, fdcBaseZero, 6);
                if (!frs.isEmpty() && !fmis.isEmpty()) break;
            } catch (Exception ex) {
                if (Debug.debug) {
                    Debug.out.println("Ignored error: " + ex);
                    ex.printStackTrace();
                }
            }
            ofs += 0x100000;
            if (ofs >= 0x1000000) throw new IOException("VMFS FDC base not found");
        }
        readMetaFiles();
    }

    /**
     * IO access of a file hosted on VMFS
     * @author Uli
     */
    class FileIOAccess extends IOAccess {

        FileMetaInfo fmi;

        long pos;

        FileIOAccess(FileMetaInfo fmi) throws IOException {
            this.fmi = fmi;
            fmi.resolvePointerBlocks();
        }

        public String toString() {
            return "FileIOAccess " + fmi + " Position=" + getPosition();
        }

        @Override
        public void close() {
        }

        @Override
        public long getPosition() {
            return pos;
        }

        @Override
        public long getSize() {
            return fmi.fmr.size;
        }

        @Override
        public int read(byte[] buffer, int offset, int size) throws IOException {
            int done = 0;
            long amountLeft = getSize() - getPosition();
            if (amountLeft <= 0) return 0;
            if (size > amountLeft) size = (int) amountLeft;
            while (size > 0) {
                int bn = (int) (pos / blockSize);
                if (fmi.blockTab == null) throw new IOException("This file has no allocation table (RDM)");
                BlockID bid = bn < fmi.blockTab.length ? new BlockID(fmi.blockTab[bn]) : new BlockID(0);
                int posInBlock = (int) (pos % blockSize);
                int maxAmountInBlock = (int) (blockSize - posInBlock);
                IOAccess io = rf;
                long base = vmfsBase;
                long blockAddr = (long) bid.number * blockSize;
                long posInDevice = base + blockAddr + posInBlock;
                switch(bid.type) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        {
                            io = sbc;
                            posInBlock = (int) (pos % sbcBmp.getDataBlockSize());
                            maxAmountInBlock = (int) sbcBmp.getDataBlockSize() - posInBlock;
                            posInDevice = sbcBmp.getDataBlockAddress(bid.number * 16 + bid.subgroup) + posInBlock;
                            break;
                        }
                    case 3:
                        throw new IOException("Internal error: PointerBlock should already be resolved");
                    default:
                        throw new IOException("Internal error: block type " + bid.type + " unexpected");
                }
                int thisTime = Math.min(size, maxAmountInBlock);
                int res;
                if (bid.type == 0) {
                    if (buffer != null) Arrays.fill(buffer, offset, offset + thisTime, (byte) 0);
                    res = thisTime;
                } else res = io.read(posInDevice, buffer, offset, thisTime);
                done += res;
                pos += thisTime;
                offset += thisTime;
                size -= thisTime;
                if (res < thisTime) break;
            }
            return done;
        }

        /**
         * Returns the allocated logical extents of the file.
         * Usefule for sparse ("thin provisioned") files to iterate over
         * allocated data.
         * 
         * @return
         */
        public List<Long> getAllocatedExtents() {
            List<Long> alloc = new ArrayList<Long>();
            long p = 0, sz = 0;
            for (int n = 0; n < fmi.blockTab.length; n++) {
                int bid = fmi.blockTab[n];
                if (bid == 0) {
                    if (sz > 0) {
                        alloc.add(p);
                        alloc.add(sz);
                    }
                    p = (long) blockSize * (n + 1);
                    sz = 0;
                } else sz += blockSize;
            }
            if (sz > 0) {
                alloc.add(p);
                alloc.add(sz);
            }
            return alloc;
        }

        @Override
        public void setPosition(long pos) {
            this.pos = pos;
        }

        @Override
        public void setSize(long newSize) {
            throw new IllegalArgumentException("setSize not supported");
        }

        @Override
        public void write(byte[] buffer, int offset, int size) throws IOException {
            throw new IOException("Write not supported");
        }
    }

    /**
     * Opens the file with the given name.
     * @param name Full path to the file.
     * @return IOAccess handle of the file
     * @throws IOException
     */
    public IOAccess openFile(String name) throws IOException {
        FileMetaInfo fmi = getMetaInfoForFile(name);
        if (fmi == null) throw new FileNotFoundException(name);
        return new FileIOAccess(fmi);
    }

    IOAccess fbb, fdc, pbc, sbc, vh;

    BitmappedBlockAllocation fbbBmp, fdcBmp, pbcBmp, sbcBmp;

    /**
     * Reads the VMFS meta files.
     * @throws Exception
     */
    void readMetaFiles() throws Exception {
        fbb = openFile(".fbb.sf");
        fdc = openFile(".fdc.sf");
        pbc = openFile(".pbc.sf");
        sbc = openFile(".sbc.sf");
        vh = openFile(".vh.sf");
        fbbBmp = new BitmappedBlockAllocation(fbb);
        fdcBmp = new BitmappedBlockAllocation(fdc);
        pbcBmp = new BitmappedBlockAllocation(pbc);
        sbcBmp = new BitmappedBlockAllocation(sbc);
    }

    /**
     * Reads the file record at the specified location.
     * @param pos
     * @return
     * @throws Exception
     */
    FileRecord readFileRecord(IOAccess io, long pos) throws Exception {
        Debug.out.println("@" + Long.toHexString(pos));
        NativeStruct frn = new NativeStruct(FileRecord.class);
        int recordSize = frn.getSize();
        byte[] frBuffer = io.read(pos, recordSize);
        FileRecord fr = new FileRecord();
        if (frBuffer == null || frBuffer.length < frn.size) return fr;
        frn.decode(fr, frBuffer, 0);
        if (hyperverbose) {
            if (fr.isFolder()) Debug.out.println("Folder: " + fr.name__128 + " Block=" + new BlockID(fr.blockId) + " ID=" + Long.toHexString(fr.recordId)); else Debug.out.println("File: " + fr.name__128 + " Block=" + new BlockID(fr.blockId) + " ID=" + Long.toHexString(fr.recordId));
        }
        return fr;
    }

    /**
     * Reads file records from the specific location, stops when type==0 is detected.
     * @param pos
     * @return
     * @throws Exception
     */
    List<FileRecord> readFileRecords(IOAccess io, long pos, int count) throws Exception {
        List<FileRecord> res = new ArrayList<FileRecord>();
        FileRecord fr = null;
        int sz = new NativeStruct(FileRecord.class).getSize();
        for (int i = 0; i < count; i++) {
            fr = readFileRecord(io, pos);
            pos += sz;
            if (fr.type != 0) res.add(fr);
        }
        return res;
    }

    /**
     * Class that holds the file meta structs. 
     * @author Uli
     */
    public class FileMetaInfo {

        public FileRecord fr;

        public FileMetaHeader fmh;

        public FileMetaRecord fmr;

        public RDMMetaRecord rdm;

        public int[] blockTab;

        public boolean resolvedPointers;

        public String fullPath = "?";

        /**
         * Checks if this file meta data has pointer blocks,
         * and resolves them.
         */
        synchronized void resolvePointerBlocks() throws IOException {
            if (resolvedPointers) return;
            resolvedPointers = true;
            if (blockTab == null) return;
            List<Integer> res = new ArrayList<Integer>();
            for (int b : blockTab) {
                BlockID bid = new BlockID(b);
                if (bid.type == 3) {
                    Debug.out.println("Resolving " + this + " => " + bid);
                    long addr = pbcBmp.getDataBlockAddress(bid.number * 16 + bid.subgroup);
                    byte[] p = pbc.read(addr, (int) pbcBmp.getDataBlockSize());
                    for (int i = 0; i < p.length / 4; i++) {
                        int block = (p[4 * i] & 0xff) + ((p[4 * i + 1] & 0xff) << 8) + ((p[4 * i + 2] & 0xff) << 16) + ((p[4 * i + 3] & 0xff) << 24);
                        res.add(block);
                    }
                } else res.add(b);
            }
            if (res.size() != blockTab.length) {
                blockTab = new int[res.size()];
                for (int i = 0; i < blockTab.length; i++) blockTab[i] = res.get(i);
            }
        }

        /**
         * Pretty toString useful for listing files / directories etc.
         * 
         * @return
         */
        public String toPrettyString() {
            String p = fullPath;
            if (p.endsWith("/")) p = p.substring(0, p.length() - 1);
            Date d = new java.util.Date(fmr.timeStamp1__0x2c * 1000L);
            String res = "[unknown object type: " + this.fmr.type + " name=" + fr.name__128 + "]";
            switch(this.fmr.type) {
                case TYPE_META:
                case TYPE_FILE:
                    res = String.format("%1$tD %1$tT %2$,15d %3$s", d, fmr.size, p + "/" + fr.name__128);
                    break;
                case TYPE_FOLDER:
                    res = String.format("%1$tD %1$tT %2$15s %3$s", d, "(dir)", p + "/" + fr.name__128);
                    break;
                case TYPE_SYMLINK:
                    res = String.format("%1$tD %1$tT %2$15s %3$s => %4$s", d, "(symlink)", p + "/" + fr.name__128, getSymLink(fr));
                    break;
                case TYPE_RDM:
                    res = String.format("%1$tD %1$tT %2$15s %3$s => %4$s", d, "(rdm " + StringUtil.displaySizeInBytes(fmr.size) + ")", p + "/" + fr.name__128, rdm.lunType__28 + " " + rdm.lunUuid__17);
                    break;
            }
            return res;
        }

        public String toString() {
            String p = fullPath;
            if (p.endsWith("/")) p = p.substring(0, p.length() - 1);
            return "Name=" + p + "/" + fr.name__128 + " Size=" + fmr.size + " Date=" + new java.util.Date(fmr.timeStamp1__0x2c * 1000L);
        }
    }

    /**
     * Reads file meta info at the specified location.
     * @param pos
     * @return
     * @throws Exception
     */
    FileMetaInfo readFileMetaInfo(IOAccess io, long pos) throws Exception {
        FileMetaInfo res = new FileMetaInfo();
        Debug.out.println("@" + Long.toHexString(pos));
        if ((io.getSize() > 0) && (pos + 0x800 > io.getSize())) {
            throw new IOException("File meta info after EOF @Pos=" + pos + " : " + io);
        }
        byte[] header = io.read(pos, 0x800);
        FileMetaHeader fmh = new FileMetaHeader();
        FileMetaRecord fmr = new FileMetaRecord();
        res.fmh = fmh;
        res.fmr = fmr;
        NativeStruct.fromNative(fmh, header, 0);
        if (fmh.magic != FILE_RECORD_MAGIC) throw new Exception("Illegal FileMetaRecord @" + pos);
        NativeStruct.fromNative(fmr, header, 0x200);
        Debug.out.println("Type=" + fmr.type + " id=" + new BlockID(fmr.id) + " size=0x" + Long.toHexString(fmr.size) + " / " + fmr.size + " date=" + new java.util.Date(1000L * fmr.timeStamp1__0x2c));
        if (fmr.type == 6) {
            Debug.out.println("RDM mapped file");
            RDMMetaRecord rdmRec = new RDMMetaRecord();
            res.rdm = rdmRec;
            NativeStruct.fromNative(rdmRec, header, 0x400);
            Debug.out.println("RDM type = " + rdmRec.lunType__28);
            Debug.out.println("RDM UUID = " + rdmRec.lunUuid__17);
            Debug.out.println("RDM blocks = " + rdmRec.blocks);
        } else {
            int blocks = (int) ((fmr.size + blockSize - 1) / blockSize);
            Debug.out.println("Blocks = " + blocks);
            if (blocks > 256) {
                blocks = (blocks + 1023) / 1024;
                if (hyperverbose) Debug.out.println("Blocks to pointers = " + blocks);
            }
            if (blocks < 256) {
                int[] blockTab = new int[blocks];
                res.blockTab = blockTab;
                for (int i = 0; i < blocks; i++) {
                    blockTab[i] = (header[4 * i + 0x400] & 0xff) + ((header[4 * i + 0x401] & 0xff) << 8) + ((header[4 * i + 0x402] & 0xff) << 16) + ((header[4 * i + 0x403] & 0xff) << 24);
                    BlockID b = new BlockID(blockTab[i]);
                    if (hyperverbose) Debug.out.println("Block" + i + " = " + b);
                }
            }
        }
        pos += 0x800;
        return res;
    }

    /**
     * Reads file meta infos from the given location.
     * @param pos Position to start reading from
     * @param amount Amount of meta infos to read
     * @return
     * @throws Exception
     */
    List<FileMetaInfo> readFileMetaInfos(IOAccess io, long pos, int amount) throws Exception {
        List<FileMetaInfo> res = new ArrayList<FileMetaInfo>();
        FileMetaInfo fmi;
        for (int i = 0; i < amount; i++) {
            fmi = readFileMetaInfo(io, pos);
            if (fmi.fmr.id != 0) res.add(fmi);
            pos += 0x800;
        }
        return res;
    }

    /**
     * Closes the volume.
     * @throws IOException
     */
    public void closeVolume() throws IOException {
        if (rf != null) rf.close();
        extents = null;
    }

    /**
     * Opens the specified volume.
     * @param file
     * @throws IOException
     * @throws URISyntaxException 
     */
    public void openVolume(String file) throws IOException, URISyntaxException {
        closeVolume();
        extents = new ArrayList<ExtentInfo>();
        String[] volumes = file.split(",");
        for (String vol : volumes) openSingleVolume(vol);
    }

    public void openSingleVolume(String file) throws IOException, URISyntaxException {
        if (file.startsWith("ssh://")) setVolumeIOAccess(new RemoteSSHIOAccess(file, false)); else if (file.startsWith("http://")) setVolumeIOAccess(providerIOAccess(file)); else if (file.startsWith("file://")) setVolumeIOAccess(new RandomIOAccess(new File(new URI(file)).getAbsolutePath(), "r")); else setVolumeIOAccess(new RandomIOAccess(file, "r"));
    }

    private IOAccess providerIOAccess(String file) throws IOException, URISyntaxException {
        URI uri = new URI(file);
        String[] pairs = uri.getQuery().split("&");
        Properties props = new Properties();
        for (String pair : pairs) {
            String[] kv = pair.split("=");
            props.setProperty(kv[0], kv[1]);
        }
        try {
            IOAccess rf = (IOAccess) Class.forName(props.getProperty("provider")).newInstance();
            String[] uInfo = uri.getUserInfo().split(":");
            props.setProperty("filer", "http://" + uri.getHost());
            props.setProperty("user", uInfo[0]);
            props.setProperty("password", uInfo[1]);
            rf.open(uri.getPath(), props);
            return rf;
        } catch (Exception e) {
            throw new IOException("Provider class " + props.getProperty("provider") + " not found", e);
        }
    }

    /**
     * Set the volume IO access.
     * @param io
     */
    public void setVolumeIOAccess(IOAccess io) throws IOException {
        ExtentInfo ex = new ExtentInfo(io);
        if (!extents.isEmpty() && !extents.get(0).lvm.uuid__0x54.toString().equals(ex.lvm.uuid__0x54.toString())) throw new IOException("LVM UUID does not match. Expected: " + extents.get(0).lvm.uuid__0x54 + " got: " + ex.lvm.uuid__0x54 + " device: " + io);
        for (ExtentInfo exx : extents) if (exx.lvm.firstSegment == ex.lvm.firstSegment) throw new IOException("LVM extent already opened: " + io);
        extents.add(ex);
        if (rf == null) rf = new ExtentIOAccess();
    }

    /**
     * Analyzes the given VMFS volume and prints to dbgout.
     * Internal debug function.
     * @param args
     */
    public static void main(String[] args) throws Throwable {
        System.out.println("VMFSInfo (C) by fluid Operations (" + Version.getBuildNumber() + " / " + Version.getBuildDate() + ")");
        System.out.println("http://www.fluidops.com");
        System.out.println();
        String f = args != null && args.length == 1 ? args[0] : null;
        VMFSDriver vmfsInfo = new VMFSDriver();
        vmfsInfo.openVolume(f);
        vmfsInfo.openVmfs();
        vmfsInfo.closeVolume();
    }
}
