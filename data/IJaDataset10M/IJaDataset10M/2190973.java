package edu.rabbit.kernel.btree;

import static edu.rabbit.kernel.DbUtility.get2byte;
import static edu.rabbit.kernel.DbUtility.get4byte;
import static edu.rabbit.kernel.DbUtility.getUnsignedByte;
import static edu.rabbit.kernel.DbUtility.getVarint;
import static edu.rabbit.kernel.DbUtility.getVarint32;
import static edu.rabbit.kernel.DbUtility.memcpy;
import static edu.rabbit.kernel.DbUtility.memset;
import static edu.rabbit.kernel.DbUtility.movePtr;
import static edu.rabbit.kernel.DbUtility.pointer;
import static edu.rabbit.kernel.DbUtility.put2byte;
import static edu.rabbit.kernel.DbUtility.put4byte;
import static edu.rabbit.kernel.DbUtility.putUnsignedByte;
import static edu.rabbit.kernel.DbUtility.putVarint;
import static edu.rabbit.kernel.btree.Btree.TRACE;
import edu.rabbit.DbErrorCode;
import edu.rabbit.DbException;
import edu.rabbit.kernel.IConfig;
import edu.rabbit.kernel.ILimits;
import edu.rabbit.kernel.DbCloneable;
import edu.rabbit.kernel.DbUtility;
import edu.rabbit.kernel.memory.IMemoryPointer;
import edu.rabbit.kernel.pager.IPage;

/**
 * As each page of the file is loaded into memory, an instance of the following
 * structure is appended and initialized to zero. This structure stores
 * information about the page that is decoded from the raw file page.
 * 
 * The pParent field points back to the parent page. This allows us to walk up
 * the BTree from any leaf to the root. Care must be taken to unref() the parent
 * page pointer when this page is no longer referenced. The pageDestructor()
 * routine handles that chore.
 * 
 * Access to all fields of this structure is controlled by the mutex stored in
 * MemPage.pBt->mutex.
 * 
 * @author Yuanyan<yanyan.cao@gmail.com>
 * 
 * 
 */
public class MemoryPage extends DbCloneable {

    /**
     * Page type flags. An ORed combination of these flags appear as the first
     * byte of on-disk image of every BTree page.
     */
    public static final byte PTF_INTKEY = 0x01;

    public static final byte PTF_ZERODATA = 0x02;

    public static final byte PTF_LEAFDATA = 0x04;

    public static final byte PTF_LEAF = 0x08;

    /** True if previously initialized. MUST BE FIRST! */
    boolean isInit;

    /** Number of overflow cell bodies in aCell[] */
    int nOverflow;

    /** True if intkey flag is set */
    boolean intKey;

    /** True if leaf flag is set */
    boolean leaf;

    /** True if this page stores data */
    boolean hasData;

    /** 100 for page 1. 0 otherwise */
    byte hdrOffset;

    /** 0 if leaf==1. 4 if leaf==0 */
    byte childPtrSize;

    /** Copy of BtShared.maxLocal or BtShared.maxLeaf */
    int maxLocal;

    /** Copy of BtShared.minLocal or BtShared.minLeaf */
    int minLocal;

    /** Index in aData of first cell pointer */
    int cellOffset;

    /** Number of free bytes on the page */
    int nFree;

    /** Number of cells on this page, local and ovfl */
    int nCell;

    /** Mask for page offset */
    int maskPage;

    static class _OvflCell extends DbCloneable {

        /** Pointers to the body of the overflow cell */
        IMemoryPointer pCell;

        /** Insert this cell before idx-th non-overflow cell */
        int idx;
    }

    /** Cells that will not fit on aData[] */
    _OvflCell[] aOvfl = new _OvflCell[] { new _OvflCell(), new _OvflCell(), new _OvflCell(), new _OvflCell(), new _OvflCell() };

    /** Pointer to BtShared that this page is part of */
    BtreeShared pBt;

    /** Pointer to disk image of the page data */
    IMemoryPointer aData;

    /** Pager page handle */
    IPage pDbPage;

    /** Page number for this page */
    int pgno;

    /**
     * The ISAUTOVACUUM macro is used within balance_nonroot() to determine if
     * the database supports auto-vacuum or not. Because it is used within an
     * expression that is an argument to another macro (sqliteMallocRaw), it is
     * not possible to use conditional compilation. So, this macro is defined
     * instead.
     * 
     * @return
     */
    private boolean ISAUTOVACUUM() {
        return pBt.autoVacuum;
    }

    /**
     * Decode the flags byte (the first byte of the header) for a page and
     * initialize fields of the MemPage structure accordingly.
     * 
     * Only the following combinations are supported. Anything different
     * indicates a corrupt database files:
     * 
     * <p>
     * PTF_ZERODATA
     * </p>
     * <p>
     * PTF_ZERODATA | PTF_LEAF
     * </p>
     * <p>
     * PTF_LEAFDATA | PTF_INTKEY
     * </p>
     * <p>
     * PTF_LEAFDATA | PTF_INTKEY | PTF_LEAF
     * </p>
     */
    public void decodeFlags(int flagByte) throws DbException {
        assert (hdrOffset == (pgno == 1 ? 100 : 0));
        assert (pBt.iMutex.held());
        leaf = (flagByte >> 3) > 0;
        assert (PTF_LEAF == 1 << 3);
        flagByte &= ~PTF_LEAF;
        childPtrSize = (byte) (4 - 4 * (leaf ? 1 : 0));
        if (flagByte == (PTF_LEAFDATA | PTF_INTKEY)) {
            intKey = true;
            hasData = leaf;
            maxLocal = pBt.maxLeaf;
            minLocal = pBt.minLeaf;
        } else if (flagByte == PTF_ZERODATA) {
            intKey = false;
            hasData = false;
            maxLocal = pBt.maxLocal;
            minLocal = pBt.minLocal;
        } else {
            throw new DbException(DbErrorCode.CORRUPT);
        }
        return;
    }

    /**
     * Initialize the auxiliary information for a disk block.
     * 
     * Return SQLITE_OK on success. If we see that the page does not contain a
     * well-formed database page, then return SQLITE_CORRUPT. Note that a return
     * of SQLITE_OK does not guarantee that the page is well-formed. It only
     * shows that we failed to detect any corruption.
     */
    public void initPage() throws DbException {
        assert (pBt != null);
        assert (pBt.iMutex.held());
        assert (pgno == pDbPage.getPageNumber());
        assert (this == pDbPage.getExtra());
        assert (aData.getBuffer() == pDbPage.getData().getBuffer());
        if (!isInit) {
            int pc;
            byte hdr;
            int usableSize;
            int cellOffset;
            int nFree;
            int top;
            hdr = hdrOffset;
            decodeFlags(DbUtility.getUnsignedByte(aData, hdr));
            assert (pBt.pageSize >= 512 && pBt.pageSize <= 32768);
            maskPage = pBt.pageSize - 1;
            nOverflow = 0;
            usableSize = pBt.usableSize;
            this.cellOffset = cellOffset = hdr + 12 - 4 * (leaf ? 1 : 0);
            top = get2byte(aData, hdr + 5);
            nCell = get2byte(aData, hdr + 3);
            if (nCell > pBt.MX_CELL()) {
                throw new DbException(DbErrorCode.CORRUPT);
            }
            pc = get2byte(aData, hdr + 1);
            nFree = DbUtility.getUnsignedByte(aData, hdr + 7) + top - (cellOffset + 2 * nCell);
            while (pc > 0) {
                int next, size;
                if (pc > usableSize - 4) {
                    throw new DbException(DbErrorCode.CORRUPT);
                }
                next = get2byte(aData, pc);
                size = get2byte(aData, pc + 2);
                if (next > 0 && next <= pc + size + 3) {
                    throw new DbException(DbErrorCode.CORRUPT);
                }
                nFree += size;
                pc = next;
            }
            this.nFree = nFree;
            if (nFree >= usableSize) {
                throw new DbException(DbErrorCode.CORRUPT);
            }
            isInit = true;
        }
    }

    /**
     * Release a MemPage. This should be called once for each prior call to
     * sqlite3BtreeGetPage.
     * 
     * @throws DbException
     */
    public static void releasePage(MemoryPage pPage) throws DbException {
        if (pPage != null) {
            assert (pPage.nOverflow == 0 || pPage.pDbPage.getRefCount() > 1);
            assert (pPage.aData != null);
            assert (pPage.pBt != null);
            assert (pPage.pDbPage.getExtra() == pPage);
            assert (pPage.pDbPage.getData().getBuffer() == pPage.aData.getBuffer());
            assert (pPage.pBt.iMutex.held());
            pPage.pDbPage.unref();
        }
    }

    /**
     * Set the pointer-map entries for all children of page pPage. Also, if
     * pPage contains cells that point to overflow pages, set the pointer map
     * entries for the overflow pages as well.
     * 
     * @throws DbException
     */
    public void setChildPtrmaps() throws DbException {
        int i;
        int nCell;
        boolean isInitOrig = isInit;
        assert (pBt.iMutex.held());
        try {
            initPage();
            nCell = this.nCell;
            for (i = 0; i < nCell; i++) {
                IMemoryPointer pCell = findCell(i);
                ptrmapPutOvflPtr(pCell);
                if (!leaf) {
                    int childPgno = get4byte(pCell);
                    pBt.ptrmapPut(childPgno, BtreeShared.PTRMAP_BTREE, pgno);
                }
            }
            if (!leaf) {
                int childPgno = get4byte(aData, hdrOffset + 8);
                pBt.ptrmapPut(childPgno, BtreeShared.PTRMAP_BTREE, pgno);
            }
        } catch (DbException e) {
            isInit = isInitOrig;
            throw e;
        }
    }

    /**
     * Somewhere on pPage, which is guarenteed to be a btree page, not an
     * overflow page, is a pointer to page iFrom. Modify this pointer so that it
     * points to iTo. Parameter eType describes the type of pointer to be
     * modified, as follows:
     * 
     * PTRMAP_BTREE: pPage is a btree-page. The pointer points at a child page
     * of pPage.
     * 
     * PTRMAP_OVERFLOW1: pPage is a btree-page. The pointer points at an
     * overflow page pointed to by one of the cells on pPage.
     * 
     * PTRMAP_OVERFLOW2: pPage is an overflow-page. The pointer points at the
     * next overflow page in the list.
     * 
     * @throws ExceptionRemove
     */
    public void modifyPagePointer(int iFrom, int iTo, short s) throws DbException {
        assert (pBt.iMutex.held());
        if (s == BtreeShared.PTRMAP_OVERFLOW2) {
            if (get4byte(aData) != iFrom) {
                throw new DbException(DbErrorCode.CORRUPT);
            }
            put4byte(aData, iTo);
        } else {
            boolean isInitOrig = this.isInit;
            int i;
            int nCell;
            initPage();
            nCell = this.nCell;
            for (i = 0; i < nCell; i++) {
                IMemoryPointer pCell = findCell(i);
                if (s == BtreeShared.PTRMAP_OVERFLOW1) {
                    BtreeCellInfo info;
                    info = parseCellPtr(pCell);
                    if (info.iOverflow > 0) {
                        if (iFrom == get4byte(pCell, info.iOverflow)) {
                            put4byte(pCell, info.iOverflow, iTo);
                            break;
                        }
                    }
                } else {
                    if (get4byte(pCell) == iFrom) {
                        put4byte(pCell, iTo);
                        break;
                    }
                }
            }
            if (i == nCell) {
                if (s != BtreeShared.PTRMAP_BTREE || get4byte(aData, hdrOffset + 8) != iFrom) {
                    throw new DbException(DbErrorCode.CORRUPT);
                }
                put4byte(aData, hdrOffset + 8, iTo);
            }
            this.isInit = isInitOrig;
        }
    }

    /**
     * Given a btree page and a cell index (0 means the first cell on the page,
     * 1 means the second cell, and so forth) return a pointer to the cell
     * content.
     * 
     * This routine works only for pages that do not contain overflow cells.
     */
    public IMemoryPointer findCell(int i) {
        return pointer(aData, maskPage & get2byte(aData, cellOffset + 2 * i));
    }

    /**
     * If the cell pCell, part of page pPage contains a pointer to an overflow
     * page, insert an entry into the pointer-map for the overflow page.
     * 
     * @throws DbException
     */
    private void ptrmapPutOvflPtr(IMemoryPointer pCell) throws DbException {
        assert (pCell != null);
        BtreeCellInfo info = parseCellPtr(pCell);
        assert ((info.nData + (intKey ? 0 : info.nKey)) == info.nPayload);
        if ((info.nData + (intKey ? 0 : info.nKey)) > info.nLocal) {
            int ovfl = get4byte(pCell, info.iOverflow);
            pBt.ptrmapPut(ovfl, BtreeShared.PTRMAP_OVERFLOW1, pgno);
        }
    }

    /**
     * Parse a cell content block and fill in the CellInfo structure. There are
     * two versions of this function. sqlite3BtreeParseCell() takes a cell index
     * as the second argument and sqlite3BtreeParseCellPtr() takes a pointer to
     * the body of the cell as its second argument.
     * 
     * @param pCell
     *            Pointer to the cell text.
     * @return
     */
    BtreeCellInfo parseCellPtr(IMemoryPointer pCell) {
        int n;
        int[] nPayload = new int[1];
        assert (pBt.iMutex.held());
        BtreeCellInfo pInfo = new BtreeCellInfo();
        pInfo.pCell = pCell;
        n = childPtrSize;
        assert (n == 4 - 4 * (leaf ? 1 : 0));
        if (intKey) {
            if (hasData) {
                n += getVarint32(pCell, n, nPayload);
            } else {
                nPayload[0] = 0;
            }
            long[] pInfo_nKey = new long[1];
            n += getVarint(pCell, n, pInfo_nKey);
            pInfo.nKey = pInfo_nKey[0];
            pInfo.nData = nPayload[0];
        } else {
            pInfo.nData = 0;
            n += getVarint32(pCell, n, nPayload);
            pInfo.nKey = nPayload[0];
        }
        pInfo.nPayload = nPayload[0];
        pInfo.nHeader = n;
        if (nPayload[0] <= this.maxLocal) {
            int nSize;
            nSize = nPayload[0] + n;
            pInfo.nLocal = nPayload[0];
            pInfo.iOverflow = 0;
            if ((nSize & ~3) == 0) {
                nSize = 4;
            }
            pInfo.nSize = nSize;
        } else {
            int minLocal;
            int maxLocal;
            int surplus;
            minLocal = this.minLocal;
            maxLocal = this.maxLocal;
            surplus = minLocal + (nPayload[0] - minLocal) % (pBt.usableSize - 4);
            if (surplus <= maxLocal) {
                pInfo.nLocal = surplus;
            } else {
                pInfo.nLocal = minLocal;
            }
            pInfo.iOverflow = pInfo.nLocal + n;
            pInfo.nSize = pInfo.iOverflow + 4;
        }
        return pInfo;
    }

    /**
     * @param iCell
     *            The cell index. First cell is 0
     * @return
     */
    public BtreeCellInfo parseCell(int iCell) {
        return parseCellPtr(findCell(iCell));
    }

    /**
     * Set up a raw page so that it looks like a database page holding no
     * entries.
     * 
     * @param sqlJetBtree
     * @param flags
     * @throws DbException
     */
    void zeroPage(int flags) throws DbException {
        IMemoryPointer data = aData;
        byte hdr = hdrOffset;
        int first;
        assert (pDbPage.getPageNumber() == pgno);
        assert (pDbPage.getExtra() == this);
        assert (pDbPage.getData().getBuffer() == data.getBuffer());
        assert (pBt.iMutex.held());
        DbUtility.putUnsignedByte(data, hdr, (short) flags);
        first = hdr + 8 + 4 * ((flags & MemoryPage.PTF_LEAF) == 0 ? 1 : 0);
        DbUtility.put4byte(data, hdr + 1, 0);
        DbUtility.putUnsignedByte(data, hdr + 7, (short) 0);
        DbUtility.put2byte(data, hdr + 5, pBt.usableSize);
        nFree = pBt.usableSize - first;
        decodeFlags(flags);
        hdrOffset = hdr;
        cellOffset = first;
        nOverflow = 0;
        assert (pBt.pageSize >= 512 && pBt.pageSize <= 32768);
        maskPage = pBt.pageSize - 1;
        nCell = 0;
        isInit = true;
    }

    /**
     * Add a page of the database file to the freelist. unref() is NOT called
     * for pPage.
     */
    public void freePage() throws DbException {
        MemoryPage pPage1 = pBt.pPage1;
        int n, k;
        assert (pBt.iMutex.held());
        assert (this.pgno > 1);
        this.isInit = false;
        pPage1.pDbPage.write();
        n = get4byte(pPage1.aData, 36);
        put4byte(pPage1.aData, 36, n + 1);
        if (IConfig.SECURE_DELETE) {
            pDbPage.write();
            memset(aData, (byte) 0, pBt.pageSize);
        }
        if (ISAUTOVACUUM()) {
            pBt.ptrmapPut(pgno, BtreeShared.PTRMAP_FREEPAGE, 0);
        }
        if (n == 0) {
            pDbPage.write();
            memset(aData, (byte) 0, 8);
            put4byte(pPage1.aData, 32, pgno);
            TRACE("FREE-PAGE: %d first\n", this.pgno);
        } else {
            MemoryPage pTrunk;
            pTrunk = pBt.getPage(get4byte(pPage1.aData, 32), false);
            k = get4byte(pTrunk.aData, 4);
            if (k >= pBt.usableSize / 4 - 8) {
                pDbPage.write();
                put4byte(aData, pTrunk.pgno);
                put4byte(aData, 4, 0);
                put4byte(aData, 32, pgno);
                TRACE("FREE-PAGE: %d new trunk page replacing %d\n", this.pgno, pTrunk.pgno);
            } else if (k < 0) {
                throw new DbException(DbErrorCode.CORRUPT);
            } else {
                pTrunk.pDbPage.write();
                put4byte(pTrunk.aData, 4, k + 1);
                put4byte(pTrunk.aData, 8 + k * 4, pgno);
                if (IConfig.SECURE_DELETE) {
                    pDbPage.dontWrite();
                }
                TRACE("FREE-PAGE: %d leaf on trunk page %d\n", this.pgno, pTrunk.pgno);
            }
            releasePage(pTrunk);
        }
    }

    /**
     ** Free any overflow pages associated with the given Cell.
     */
    public void clearCell(IMemoryPointer pCell) throws DbException {
        BtreeCellInfo info;
        int[] ovflPgno = new int[1];
        int nOvfl;
        int ovflPageSize;
        assert (pBt.iMutex.held());
        info = parseCellPtr(pCell);
        if (info.iOverflow == 0) {
            return;
        }
        ovflPgno[0] = get4byte(pCell, info.iOverflow);
        ovflPageSize = pBt.usableSize - 4;
        nOvfl = (info.nPayload - info.nLocal + ovflPageSize - 1) / ovflPageSize;
        assert (ovflPgno[0] == 0 || nOvfl > 0);
        while (nOvfl-- != 0) {
            MemoryPage[] pOvfl = new MemoryPage[1];
            if (ovflPgno[0] < 2 || ovflPgno[0] > pBt.pPager.getPageCount()) {
                throw new DbException(DbErrorCode.CORRUPT);
            }
            pBt.getOverflowPage(ovflPgno[0], pOvfl, (nOvfl == 0) ? null : ovflPgno);
            pOvfl[0].freePage();
            pOvfl[0].pDbPage.unref();
        }
    }

    /**
     ** Compute the total number of bytes that a Cell needs in the cell data area
     * of the btree-page. The return number includes the cell data header and
     * the local payload, but not any overflow page or the space used by the
     * cell pointer.
     */
    int cellSize(int iCell) {
        BtreeCellInfo info = parseCell(iCell);
        return info.nSize;
    }

    int cellSizePtr(IMemoryPointer pCell) {
        BtreeCellInfo info = parseCellPtr(pCell);
        return info.nSize;
    }

    /**
     * Remove the i-th cell from pPage. This routine effects pPage only. The
     * cell content is not freed or deallocated. It is assumed that the cell
     * content has been copied someplace else. This routine just removes the
     * reference to the cell from pPage.
     * 
     * "sz" must be the number of bytes in the cell.
     * 
     * @param idx
     * @param sz
     * @throws DbException
     */
    public void dropCell(int idx, int sz) throws DbException {
        final MemoryPage pPage = this;
        int i;
        int pc;
        IMemoryPointer data;
        IMemoryPointer ptr;
        assert (idx >= 0 && idx < pPage.nCell);
        assert (sz == pPage.cellSize(idx));
        assert (pPage.pBt.iMutex.held());
        data = pPage.aData;
        ptr = pointer(data, pPage.cellOffset + 2 * idx);
        pc = get2byte(ptr);
        if ((pc < pPage.hdrOffset + 6 + (pPage.leaf ? 0 : 4)) || (pc + sz > pPage.pBt.usableSize)) {
            throw new DbException(DbErrorCode.CORRUPT);
        }
        pPage.freeSpace(pc, sz);
        for (i = idx + 1; i < pPage.nCell; i++, movePtr(ptr, 2)) {
            putUnsignedByte(ptr, 0, getUnsignedByte(ptr, 2));
            putUnsignedByte(ptr, 1, getUnsignedByte(ptr, 3));
        }
        pPage.nCell--;
        put2byte(data, pPage.hdrOffset + 3, pPage.nCell);
        pPage.nFree += 2;
    }

    private void freeSpace(int start, int size) throws DbException {
        MemoryPage pPage = this;
        int addr, pbegin, hdr;
        IMemoryPointer data = pPage.aData;
        assert (pPage.pBt != null);
        assert (start >= pPage.hdrOffset + 6 + (pPage.leaf ? 0 : 4));
        assert ((start + size) <= pPage.pBt.usableSize);
        assert (pPage.pBt.iMutex.held());
        assert (size >= 0);
        if (IConfig.SECURE_DELETE) {
            memset(data, start, (byte) 0, size);
        }
        hdr = pPage.hdrOffset;
        addr = hdr + 1;
        while ((pbegin = get2byte(data, addr)) < start && pbegin > 0) {
            assert (pbegin <= pPage.pBt.usableSize - 4);
            if (pbegin <= addr) {
                throw new DbException(DbErrorCode.CORRUPT);
            }
            addr = pbegin;
        }
        if (pbegin > pPage.pBt.usableSize - 4) {
            throw new DbException(DbErrorCode.CORRUPT);
        }
        assert (pbegin > addr || pbegin == 0);
        put2byte(data, addr, start);
        put2byte(data, start, pbegin);
        put2byte(data, start + 2, size);
        pPage.nFree += size;
        addr = pPage.hdrOffset + 1;
        while ((pbegin = get2byte(data, addr)) > 0) {
            int pnext, psize, x;
            assert (pbegin > addr);
            assert (pbegin <= pPage.pBt.usableSize - 4);
            pnext = get2byte(data, pbegin);
            psize = get2byte(data, pbegin + 2);
            if (pbegin + psize + 3 >= pnext && pnext > 0) {
                int frag = pnext - (pbegin + psize);
                if ((frag < 0) || (frag > (int) DbUtility.getUnsignedByte(data, pPage.hdrOffset + 7))) {
                    throw new DbException(DbErrorCode.CORRUPT);
                }
                DbUtility.putUnsignedByte(data, pPage.hdrOffset + 7, (byte) (DbUtility.getUnsignedByte(data, pPage.hdrOffset + 7) - (byte) frag));
                x = get2byte(data, pnext);
                put2byte(data, pbegin, x);
                x = pnext + get2byte(data, pnext + 2) - pbegin;
                put2byte(data, pbegin + 2, x);
            } else {
                addr = pbegin;
            }
        }
        if (DbUtility.getUnsignedByte(data, hdr + 1) == DbUtility.getUnsignedByte(data, hdr + 5) && DbUtility.getUnsignedByte(data, hdr + 2) == DbUtility.getUnsignedByte(data, hdr + 6)) {
            int top;
            pbegin = get2byte(data, hdr + 1);
            memcpy(data, hdr + 1, data, pbegin, 2);
            top = get2byte(data, hdr + 5) + get2byte(data, pbegin + 2);
            put2byte(data, hdr + 5, top);
        }
        assert (pPage.pDbPage.isWriteable());
    }

    /**
     * Insert a new cell on pPage at cell index "i". pCell points to the content
     * of the cell.
     * 
     * If the cell content will fit on the page, then put it there. If it will
     * not fit, then make a copy of the cell content into pTemp if pTemp is not
     * null. Regardless of pTemp, allocate a new entry in pPage->aOvfl[] and
     * make it point to the cell content (either in pTemp or the original pCell)
     * and also record its index. Allocating a new entry in pPage->aCell[]
     * implies that pPage->nOverflow is incremented.
     * 
     * If nSkip is non-zero, then do not copy the first nSkip bytes of the cell.
     * The caller will overwrite them after this function returns. If nSkip is
     * non-zero, then pCell may not point to an invalid memory location (but
     * pCell+nSkip is always valid).
     * 
     * @param i
     *            New cell becomes the i-th cell of the page
     * @param pCell
     *            Content of the new cell
     * @param sz
     *            Bytes of content in pCell
     * @param pTemp
     *            Temp storage space for pCell, if needed
     * @param nSkip
     *            Do not write the first nSkip bytes of the cell
     * 
     * @throws DbException
     */
    public void insertCell(int i, IMemoryPointer pCell, int sz, IMemoryPointer pTemp, byte nSkip) throws DbException {
        final MemoryPage pPage = this;
        int idx;
        int j;
        int top;
        int end;
        int ins;
        int hdr;
        int cellOffset;
        IMemoryPointer data;
        assert (i >= 0 && i <= pPage.nCell + pPage.nOverflow);
        assert (pPage.nCell <= pPage.pBt.MX_CELL() && pPage.pBt.MX_CELL() <= 5460);
        assert (pPage.nOverflow <= pPage.aOvfl.length);
        assert (sz == pPage.cellSizePtr(pCell));
        assert (pPage.pBt.iMutex.held());
        if (pPage.nOverflow != 0 || sz + 2 > pPage.nFree) {
            if (pTemp != null) {
                memcpy(pTemp, nSkip, pCell, nSkip, sz - nSkip);
                pCell = pTemp;
            }
            j = pPage.nOverflow++;
            pPage.aOvfl[j].pCell = pCell;
            pPage.aOvfl[j].idx = i;
            pPage.nFree = 0;
        } else {
            pPage.pDbPage.write();
            assert (pPage.pDbPage.isWriteable());
            data = pPage.aData;
            hdr = pPage.hdrOffset;
            top = get2byte(data, hdr + 5);
            cellOffset = pPage.cellOffset;
            end = cellOffset + 2 * pPage.nCell + 2;
            ins = cellOffset + 2 * i;
            if (end > top - sz) {
                pPage.defragmentPage();
                top = get2byte(data, hdr + 5);
                assert (end + sz <= top);
            }
            idx = pPage.allocateSpace(sz);
            assert (idx > 0);
            assert (end <= get2byte(data, hdr + 5));
            if (idx + sz > pPage.pBt.usableSize) {
                throw new DbException(DbErrorCode.CORRUPT);
            }
            pPage.nCell++;
            pPage.nFree -= 2;
            memcpy(data, idx + nSkip, pCell, nSkip, sz - nSkip);
            for (j = end - 2; j > ins; j -= 2) {
                DbUtility.putUnsignedByte(data, j, DbUtility.getUnsignedByte(data, j - 2));
                DbUtility.putUnsignedByte(data, j + 1, DbUtility.getUnsignedByte(data, j - 1));
            }
            put2byte(data, ins, idx);
            put2byte(data, hdr + 3, pPage.nCell);
            if (pPage.pBt.autoVacuum) {
                BtreeCellInfo info = pPage.parseCellPtr(pCell);
                assert ((info.nData + (pPage.intKey ? 0 : info.nKey)) == info.nPayload);
                if ((info.nData + (pPage.intKey ? 0 : info.nKey)) > info.nLocal) {
                    int pgnoOvfl = get4byte(pCell, info.iOverflow);
                    pPage.pBt.ptrmapPut(pgnoOvfl, BtreeShared.PTRMAP_OVERFLOW1, pPage.pgno);
                }
            }
        }
    }

    /**
     * Allocate nByte bytes of space on a page.
     * 
     * Return the index into pPage->aData[] of the first byte of the new
     * allocation. The caller guarantees that there is enough space. This
     * routine will never fail.
     * 
     * If the page contains nBytes of free space but does not contain nBytes of
     * contiguous free space, then this routine automatically calls
     * defragementPage() to consolidate all free space before allocating the new
     * chunk.
     * 
     * @param nByte
     * @return
     * 
     * @throws DbException
     */
    private int allocateSpace(int nByte) throws DbException {
        final MemoryPage pPage = this;
        int addr, pc, hdr;
        int size;
        int nFrag;
        int top;
        int nCell;
        int cellOffset;
        IMemoryPointer data;
        data = pPage.aData;
        assert (pPage.pDbPage.isWriteable());
        assert (pPage.pBt != null);
        assert (pPage.pBt.iMutex.held());
        assert (nByte >= 0);
        assert (pPage.nFree >= nByte);
        assert (pPage.nOverflow == 0);
        pPage.nFree -= nByte;
        hdr = pPage.hdrOffset;
        nFrag = DbUtility.getUnsignedByte(data, hdr + 7);
        if (nFrag < 60) {
            addr = hdr + 1;
            while ((pc = get2byte(data, addr)) > 0) {
                size = get2byte(data, pc + 2);
                if (size >= nByte) {
                    int x = size - nByte;
                    if (size < nByte + 4) {
                        memcpy(data, addr, data, pc, 2);
                        DbUtility.putUnsignedByte(data, hdr + 7, (byte) (nFrag + x));
                        return pc;
                    } else {
                        put2byte(data, pc + 2, x);
                        return pc + x;
                    }
                }
                addr = pc;
            }
        }
        top = get2byte(data, hdr + 5);
        nCell = get2byte(data, hdr + 3);
        cellOffset = pPage.cellOffset;
        if (nFrag >= 60 || cellOffset + 2 * nCell > top - nByte) {
            defragmentPage();
            top = get2byte(data, hdr + 5);
        }
        top -= nByte;
        assert (cellOffset + 2 * nCell <= top);
        put2byte(data, hdr + 5, top);
        assert (pPage.pDbPage.isWriteable());
        return top;
    }

    /**
     * Defragment the page given. All Cells are moved to the end of the page and
     * all free space is collected into one big FreeBlk that occurs in between
     * the header and cell pointer array and the cell content area.
     * 
     * @throws DbException
     */
    private void defragmentPage() throws DbException {
        final MemoryPage pPage = this;
        int i;
        int pc;
        int addr;
        int hdr;
        int size;
        int usableSize;
        int cellOffset;
        int cbrk;
        int nCell;
        IMemoryPointer data;
        IMemoryPointer temp;
        assert (pPage.pDbPage.isWriteable());
        assert (pPage.pBt != null);
        assert (pPage.pBt.usableSize <= ILimits.DB_MAX_PAGE_SIZE);
        assert (pPage.nOverflow == 0);
        assert (pPage.pBt.iMutex.held());
        temp = pPage.pBt.pPager.getTempSpace();
        data = pPage.aData;
        hdr = pPage.hdrOffset;
        cellOffset = pPage.cellOffset;
        nCell = pPage.nCell;
        assert (nCell == get2byte(data, hdr + 3));
        usableSize = pPage.pBt.usableSize;
        cbrk = get2byte(data, hdr + 5);
        memcpy(temp, cbrk, data, cbrk, usableSize - cbrk);
        cbrk = usableSize;
        for (i = 0; i < nCell; i++) {
            IMemoryPointer pAddr;
            pAddr = pointer(data, cellOffset + i * 2);
            pc = get2byte(pAddr);
            if (pc >= usableSize) {
                throw new DbException(DbErrorCode.CORRUPT);
            }
            size = pPage.cellSizePtr(pointer(temp, pc));
            cbrk -= size;
            if (cbrk < cellOffset + 2 * nCell || pc + size > usableSize) {
                throw new DbException(DbErrorCode.CORRUPT);
            }
            assert (cbrk + size <= usableSize && cbrk >= 0);
            memcpy(data, cbrk, temp, pc, size);
            put2byte(pAddr, cbrk);
        }
        assert (cbrk >= cellOffset + 2 * nCell);
        put2byte(data, hdr + 5, cbrk);
        DbUtility.putUnsignedByte(data, hdr + 1, (byte) 0);
        DbUtility.putUnsignedByte(data, hdr + 2, (byte) 0);
        DbUtility.putUnsignedByte(data, hdr + 7, (byte) 0);
        addr = cellOffset + 2 * nCell;
        memset(data, addr, (byte) 0, cbrk - addr);
        assert (pPage.pDbPage.isWriteable());
        if (cbrk - addr != pPage.nFree) {
            throw new DbException(DbErrorCode.CORRUPT);
        }
    }

    /**
     * This a more complex version of findCell() that works for pages that do
     * contain overflow cells. See insert
     * 
     * @param iCell
     * @return
     */
    public IMemoryPointer findOverflowCell(int iCell) {
        final MemoryPage pPage = this;
        int i;
        assert (pPage.pBt.iMutex.held());
        for (i = pPage.nOverflow - 1; i >= 0; i--) {
            int k;
            _OvflCell pOvfl = pPage.aOvfl[i];
            k = pOvfl.idx;
            if (k <= iCell) {
                if (k == iCell) {
                    return pOvfl.pCell;
                }
                iCell--;
            }
        }
        return pPage.findCell(iCell);
    }

    /**
     * Add a list of cells to a page. The page should be initially empty. The
     * cells are guaranteed to fit on the page.
     * 
     * @param nCell
     *            The number of cells to add to this page
     * @param apCell
     *            Pointers to cell bodies
     * @param aSize
     *            Sizes of the cells
     * 
     * @throws DbException
     */
    public void assemblePage(int nCell, IMemoryPointer[] apCell, int[] aSize) throws DbException {
        assemblePage(nCell, apCell, 0, aSize, 0);
    }

    public void assemblePage(int nCell, IMemoryPointer[] apCell, int apCellPos, int[] aSize, int aSizePos) throws DbException {
        final MemoryPage pPage = this;
        int i;
        int totalSize;
        int hdr;
        int cellptr;
        int cellbody;
        IMemoryPointer data;
        assert (pPage.nOverflow == 0);
        assert (pPage.pBt.iMutex.held());
        assert (nCell >= 0 && nCell <= pPage.pBt.MX_CELL() && pPage.pBt.MX_CELL() <= 5460);
        totalSize = 0;
        for (i = 0; i < nCell; i++) {
            totalSize += aSize[aSizePos + i];
        }
        assert (totalSize + 2 * nCell <= pPage.nFree);
        assert (pPage.nCell == 0);
        assert (pPage.pDbPage.isWriteable());
        cellptr = pPage.cellOffset;
        data = pPage.aData;
        hdr = pPage.hdrOffset;
        put2byte(data, hdr + 3, nCell);
        if (nCell != 0) {
            cellbody = pPage.allocateSpace(totalSize);
            assert (cellbody > 0);
            assert (pPage.nFree >= 2 * nCell);
            pPage.nFree -= 2 * nCell;
            for (i = 0; i < nCell; i++) {
                put2byte(data, cellptr, cellbody);
                memcpy(data, cellbody, apCell[apCellPos + i], 0, aSize[aSizePos + i]);
                cellptr += 2;
                cellbody += aSize[aSizePos + i];
            }
            assert (cellbody == pPage.pBt.usableSize);
        }
        pPage.nCell = nCell;
    }

    /**
     * Page pParent is an internal (non-leaf) tree page. This function asserts
     * that page number iChild is the left-child if the iIdx'th cell in page
     * pParent. Or, if iIdx is equal to the total number of cells in pParent,
     * that page number iChild is the right-child of the page.
     * 
     * @param iIdx
     * @param iChild
     */
    public void assertParentIndex(int iIdx, int iChild) {
        final MemoryPage pParent = this;
        assert (iIdx <= pParent.nCell);
        if (iIdx == pParent.nCell) {
            assert (get4byte(pParent.aData, pParent.hdrOffset + 8) == iChild);
        } else {
            assert (get4byte(pParent.findCell(iIdx)) == iChild);
        }
    }

    /**
     * Create the byte sequence used to represent a cell on page pPage and write
     * that byte sequence into pCell[]. Overflow pages are allocated and filled
     * in as necessary. The calling procedure is responsible for making sure
     * sufficient space has been allocated for pCell[].
     * 
     * Note that pCell does not necessary need to point to the pPage->aData
     * area. pCell might point to some temporary storage. The cell will be
     * constructed in this temporary area then copied into pPage->aData later.
     * 
     * @param pCell
     *            Complete text of the cell
     * @param pKey
     *            The key
     * @param nKey
     *            The key
     * @param pData
     *            The data
     * @param nData
     *            The data
     * @param nZero
     *            Extra zero bytes to append to pData
     * 
     * @return cell size
     * 
     * @throws DbException
     */
    public int fillInCell(IMemoryPointer pCell, IMemoryPointer pKey, long nKey, IMemoryPointer pData, int nData, int nZero) throws DbException {
        final MemoryPage pPage = this;
        int pnSize = 0;
        int nPayload;
        IMemoryPointer pSrc;
        int nSrc, n;
        int spaceLeft;
        MemoryPage pOvfl = null;
        MemoryPage pToRelease = null;
        IMemoryPointer pPrior;
        IMemoryPointer pPayload;
        BtreeShared pBt = pPage.pBt;
        int[] pgnoOvfl = { 0 };
        int nHeader;
        BtreeCellInfo info;
        assert (pPage.pBt.iMutex.held());
        assert (pCell.getBuffer() != pPage.aData.getBuffer() || pPage.pDbPage.isWriteable());
        nHeader = 0;
        if (!pPage.leaf) {
            nHeader += 4;
        }
        if (pPage.hasData) {
            nHeader += putVarint(pointer(pCell, nHeader), nData + nZero);
        } else {
            nData = nZero = 0;
        }
        nHeader += putVarint(pointer(pCell, nHeader), nKey);
        info = pPage.parseCellPtr(pCell);
        assert (info.nHeader == nHeader);
        assert (info.nKey == nKey);
        assert (info.nData == nData + nZero);
        nPayload = nData + nZero;
        if (pPage.intKey) {
            pSrc = pData;
            nSrc = nData;
            nData = 0;
        } else {
            nPayload += (int) nKey;
            pSrc = pKey;
            nSrc = (int) nKey;
        }
        pnSize = info.nSize;
        spaceLeft = info.nLocal;
        pPayload = pointer(pCell, nHeader);
        pPrior = pointer(pCell, info.iOverflow);
        while (nPayload > 0) {
            if (spaceLeft == 0) {
                int pgnoPtrmap = pgnoOvfl[0];
                if (pBt.autoVacuum) {
                    do {
                        pgnoOvfl[0]++;
                    } while (pBt.PTRMAP_ISPAGE(pgnoOvfl[0]) || pgnoOvfl[0] == pBt.PENDING_BYTE_PAGE());
                }
                try {
                    pOvfl = pBt.allocatePage(pgnoOvfl, pgnoOvfl[0], false);
                    if (pBt.autoVacuum) {
                        byte eType = (pgnoPtrmap != 0 ? BtreeShared.PTRMAP_OVERFLOW2 : BtreeShared.PTRMAP_OVERFLOW1);
                        try {
                            pBt.ptrmapPut(pgnoOvfl[0], eType, pgnoPtrmap);
                        } catch (DbException e) {
                            releasePage(pOvfl);
                        }
                    }
                } catch (DbException e) {
                    releasePage(pToRelease);
                    throw e;
                }
                assert (pToRelease == null || pToRelease.pDbPage.isWriteable());
                assert (pPrior.getBuffer() != pPage.aData.getBuffer() || pPage.pDbPage.isWriteable());
                put4byte(pPrior, pgnoOvfl[0]);
                releasePage(pToRelease);
                pToRelease = pOvfl;
                pPrior = pOvfl.aData;
                put4byte(pPrior, 0);
                pPayload = pointer(pOvfl.aData, 4);
                spaceLeft = pBt.usableSize - 4;
            }
            n = nPayload;
            if (n > spaceLeft) n = spaceLeft;
            assert (pToRelease == null || pToRelease.pDbPage.isWriteable());
            assert (pPayload.getBuffer() != pPage.aData.getBuffer() || pPage.pDbPage.isWriteable());
            if (nSrc > 0) {
                if (n > nSrc) n = nSrc;
                assert (pSrc != null);
                memcpy(pPayload, pSrc, n);
            } else {
                memset(pPayload, (byte) 0, n);
            }
            nPayload -= n;
            movePtr(pPayload, n);
            pSrc = pointer(pSrc, n);
            nSrc -= n;
            spaceLeft -= n;
            if (nSrc == 0) {
                nSrc = nData;
                pSrc = pData;
            }
        }
        releasePage(pToRelease);
        return pnSize;
    }

    /**
     * If the cell with index iCell on page pPage contains a pointer to an
     * overflow page, insert an entry into the pointer-map for the overflow
     * page.
     * 
     * @param iCell
     * 
     * @throws DbException
     */
    public void ptrmapPutOvfl(int iCell) throws DbException {
        MemoryPage pPage = this;
        IMemoryPointer pCell;
        assert (pPage.pBt.iMutex.held());
        pCell = pPage.findOverflowCell(iCell);
        pPage.ptrmapPutOvflPtr(pCell);
    }
}
