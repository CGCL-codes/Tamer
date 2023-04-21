package tinybase.pf;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;
import tinybase.basic.BytePointer;
import tinybase.basic.IntegerWrapper;
import tinybase.basic.RedBase;
import tinybase.basic.RC;

public class PF_BufferMgr {

    public static final int INVALID_SLOT = -1;

    public static final FileDescriptor MEMORY_FD = null;

    public static final int PAGEHDR_SIZE = 4;

    public StatisticsMgr pStatisticsMgr;

    private PF_BufPageDesc[] bufTable;

    private HashTable hashTable;

    public void setHashTable(HashTable hashTable) {
        this.hashTable = hashTable;
    }

    private int numPages;

    private int pageSize;

    private int first;

    private int last;

    private int free;

    static File fLog;

    private RC insertFree(int slot) {
        bufTable[slot].next = free;
        free = slot;
        return RC.PF_SUCCESS;
    }

    private RC linkHead(int slot) {
        bufTable[slot].next = first;
        bufTable[slot].prev = INVALID_SLOT;
        if (first != INVALID_SLOT) bufTable[first].prev = slot;
        first = slot;
        if (last == INVALID_SLOT) last = first;
        return RC.PF_SUCCESS;
    }

    private RC unlink(int slot) {
        if (first == slot) first = bufTable[slot].next;
        if (last == slot) last = bufTable[slot].prev;
        if (bufTable[slot].next != INVALID_SLOT) bufTable[bufTable[slot].next].prev = bufTable[slot].prev;
        if (bufTable[slot].prev != INVALID_SLOT) bufTable[bufTable[slot].prev].next = bufTable[slot].next;
        bufTable[slot].prev = INVALID_SLOT;
        bufTable[slot].next = INVALID_SLOT;
        return RC.PF_SUCCESS;
    }

    private RC internalAlloc(Slot slot) {
        RC rc;
        int i = slot.getSlotValue();
        if (free != INVALID_SLOT) {
            slot.setSlotValue(free);
            free = bufTable[slot.getSlotValue()].next;
        } else {
            for (i = last; i != INVALID_SLOT; i = bufTable[i].prev) {
                if (bufTable[i].pinCount == 0) break;
            }
            slot.setSlotValue(i);
            if (i == INVALID_SLOT) return RC.PF_NOBUF;
            if (bufTable[i].bDirty) {
                if ((rc = writePage(bufTable[i].fd, bufTable[i].pageNum, bufTable[i].data)) != RC.PF_SUCCESS) return rc;
                bufTable[i].bDirty = false;
            }
            if ((rc = hashTable.delete(bufTable[i].fd, bufTable[i].pageNum)) != RC.PF_SUCCESS || (rc = unlink(i)) != RC.PF_SUCCESS) return rc;
            slot.setSlotValue(i);
        }
        if ((rc = linkHead(slot.getSlotValue())) != RC.PF_SUCCESS) return rc;
        return RC.PF_SUCCESS;
    }

    private RC readPage(FileDescriptor fd, PageNum pageNum, byte[] dest) {
        String pfMessage = "Reading (" + fd + ", " + pageNum.getPageNum() + "). \n";
        writeLog(pfMessage);
        pStatisticsMgr.register(Statistic.PF_READPAGE, Stat_Operation.STAT_ADDONE, null);
        long offset = pageNum.getPageNum() * (long) pageSize + PF_Internal.PF_FILE_HDR_SIZE;
        FileInputStream fis = new FileInputStream(fd);
        FileChannel fc = fis.getChannel();
        ByteBuffer bb = ByteBuffer.wrap(dest);
        long num = 0;
        try {
            if ((fc = fc.position(offset)) == null) return RC.PF_UNIX;
            num = fc.read(bb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (num < 0) return RC.PF_UNIX; else if (num != pageSize) return RC.PF_INCOMPLETEREAD; else return RC.PF_SUCCESS;
    }

    private RC writePage(FileDescriptor fd, PageNum pageNum, byte[] source) {
        String psMessage = "Reading (" + fd + ", " + pageNum.getPageNum() + "). \n";
        writeLog(psMessage);
        pStatisticsMgr.register(Statistic.PF_READPAGE, Stat_Operation.STAT_ADDONE, null);
        long offset = pageNum.getPageNum() * (long) pageSize + PF_Internal.PF_FILE_HDR_SIZE;
        FileOutputStream fos = new FileOutputStream(fd);
        FileChannel fc = fos.getChannel();
        ByteBuffer bb = ByteBuffer.wrap(source);
        long num = 0;
        try {
            if ((fc = fc.position(offset)) == null) return RC.PF_UNIX;
            num = fc.write(bb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (num < 0) return RC.PF_UNIX; else if (num != pageSize) return RC.PF_INCOMPLETEWRITE; else return RC.PF_SUCCESS;
    }

    private RC initPageDesc(FileDescriptor fd, PageNum pageNum, Slot slot) {
        bufTable[slot.getSlotValue()].fd = fd;
        bufTable[slot.getSlotValue()].pageNum = pageNum;
        bufTable[slot.getSlotValue()].bDirty = false;
        bufTable[slot.getSlotValue()].pinCount = 1;
        return RC.PF_SUCCESS;
    }

    public PF_BufferMgr(int numPages) {
        this.numPages = numPages;
        pageSize = Pf.PF_PAGE_SIZE + PAGEHDR_SIZE;
        hashTable = new HashTable(PF_Internal.PF_HASH_TBL_SIZE);
        pStatisticsMgr = new StatisticsMgr();
        String pfMessage = "Creating buffer manager. " + numPages + " pages of size " + pageSize;
        writeLog(pfMessage);
        bufTable = new PF_BufPageDesc[numPages];
        for (int i = 0; i < numPages; i++) {
            bufTable[i] = new PF_BufPageDesc();
            byte[] temp = new byte[pageSize];
            if (temp == null) {
                System.err.println("Not enough memory for buffer");
                System.exit(1);
            } else {
                bufTable[i].data = temp;
            }
            for (int j = 0; j < pageSize; j++) bufTable[i].data[i] = '0';
            bufTable[i].prev = i - 1;
            bufTable[i].next = i + 1;
        }
        bufTable[0].prev = INVALID_SLOT;
        bufTable[numPages - 1].next = INVALID_SLOT;
        free = 0;
        first = last = INVALID_SLOT;
        writeLog("Succesfully created the buffer manager.\n");
    }

    public RC getPage(FileDescriptor fd, PageNum pageNum, BytePointer ppBuffer, boolean bMultiplePins) {
        RC rc;
        Slot slot = new Slot(0);
        String psMessage = "Looking for (" + fd.toString() + "," + pageNum.getPageNum() + ").\n";
        writeLog(psMessage);
        pStatisticsMgr.register(Statistic.PF_GETPAGE, Stat_Operation.STAT_ADDONE, null);
        if ((rc = hashTable.find(fd, pageNum, slot)) != RC.PF_SUCCESS && (rc != RC.PF_HASHNOTFOUND)) return rc;
        if (rc == RC.PF_HASHNOTFOUND) {
            pStatisticsMgr.register(Statistic.PF_PAGEFOUND, Stat_Operation.STAT_ADDONE, null);
            if ((rc = internalAlloc(slot)) != RC.PF_SUCCESS) return rc;
            if ((rc = readPage(fd, pageNum, bufTable[slot.getSlotValue()].data)) != RC.PF_SUCCESS || (rc = hashTable.insert(fd, pageNum, slot)) != RC.PF_SUCCESS || (rc = initPageDesc(fd, pageNum, slot)) != RC.PF_SUCCESS) {
                unlink(slot.getSlotValue());
                insertFree(slot.getSlotValue());
                return rc;
            }
            writeLog("Page not found in buffer. Loaded.\n");
        } else {
            pStatisticsMgr.register(Statistic.PF_PAGEFOUND, Stat_Operation.STAT_ADDONE, null);
            if (bMultiplePins == false && bufTable[slot.getSlotValue()].pinCount > 0) return RC.PF_PAGEPINNED;
            bufTable[slot.getSlotValue()].pinCount++;
            psMessage = "Page found in buffer.  " + bufTable[slot.getSlotValue()].pinCount + " pin count.\n";
            writeLog(psMessage);
            if ((rc = unlink(slot.getSlotValue())) != RC.PF_SUCCESS || (rc = linkHead(slot.getSlotValue())) != RC.PF_SUCCESS) return rc;
        }
        ppBuffer.setArray(bufTable[slot.getSlotValue()].data);
        return RC.PF_SUCCESS;
    }

    public RC allocatePage(FileDescriptor fd, PageNum pageNum, BytePointer ppBuffer) {
        RC rc;
        Slot slot = new Slot(0);
        String psMessage = "Allocating a page for (" + fd.toString() + "," + pageNum.getPageNum() + ")....";
        System.out.println(psMessage);
        writeLog(psMessage);
        if ((rc = hashTable.find(fd, pageNum, slot)) == RC.PF_SUCCESS) return RC.PF_PAGEINBUF; else if (rc != RC.PF_HASHNOTFOUND) return rc;
        if ((rc = internalAlloc(slot)) != RC.PF_SUCCESS) return rc;
        if ((rc = hashTable.insert(fd, pageNum, slot)) != RC.PF_SUCCESS || (rc = initPageDesc(fd, pageNum, slot)) != RC.PF_SUCCESS) {
            unlink(slot.getSlotValue());
            insertFree(slot.getSlotValue());
            return rc;
        }
        System.out.println("Successufully allocated page.\n");
        writeLog("Successufully allocated page.\n");
        ppBuffer.setArray(bufTable[slot.getSlotValue()].data);
        return RC.PF_SUCCESS;
    }

    RC markDirty(FileDescriptor fd, PageNum pageNum) {
        RC rc;
        Slot slot = new Slot(0);
        String psMessage = "Marking dirty (" + fd.toString() + "," + pageNum + ").\n";
        writeLog(psMessage);
        if ((rc = hashTable.find(fd, pageNum, slot)) != RC.PF_SUCCESS) if (rc == RC.PF_HASHNOTFOUND) return RC.PF_PAGENOTINBUF; else return rc;
        if (bufTable[slot.getSlotValue()].pinCount == 0) return RC.PF_PAGEUNPINNED;
        bufTable[slot.getSlotValue()].bDirty = true;
        if ((rc = unlink(slot.getSlotValue())) != RC.PF_SUCCESS || (rc = linkHead(slot.getSlotValue())) != RC.PF_SUCCESS) return rc;
        return RC.PF_SUCCESS;
    }

    public RC unpinPage(FileDescriptor fd, PageNum pageNum) {
        RC rc;
        Slot slot = new Slot(0);
        if ((rc = hashTable.find(fd, pageNum, slot)) != RC.PF_SUCCESS) if (rc == RC.PF_HASHNOTFOUND) return RC.PF_PAGENOTINBUF; else return rc;
        if (bufTable[slot.getSlotValue()].pinCount == 0) return RC.PF_PAGEUNPINNED;
        String psMessage = null;
        if (fd != null) psMessage = "Unpinning (" + fd.toString() + "," + pageNum + "). " + (bufTable[slot.getSlotValue()].pinCount - 1) + "Pin count\n"; else psMessage = "Unpinning (" + fd + "," + pageNum + "). " + (bufTable[slot.getSlotValue()].pinCount - 1) + "Pin count\n";
        writeLog(psMessage);
        if (--(bufTable[slot.getSlotValue()].pinCount) == 0) {
            if ((rc = unlink(slot.getSlotValue())) != RC.PF_SUCCESS || (rc = linkHead(slot.getSlotValue())) != RC.PF_SUCCESS) return rc;
        }
        return RC.PF_SUCCESS;
    }

    public RC flushPages(FileDescriptor fd) {
        RC rc, rcWarn = RC.PF_SUCCESS;
        String psMessage = "Flushing all pages for (" + fd.toString() + ").\n";
        writeLog(psMessage);
        pStatisticsMgr.register(Statistic.PF_FLUSHPAGES, Stat_Operation.STAT_ADDONE, null);
        Slot slot = new Slot(0);
        slot.setSlotValue(first);
        while (slot.getSlotValue() != INVALID_SLOT) {
            int next = bufTable[slot.getSlotValue()].next;
            if (bufTable[slot.getSlotValue()].fd == fd) {
                psMessage = "Page (" + bufTable[slot.getSlotValue()].pageNum + ") is in buffer manager.\n";
                writeLog(psMessage);
                if (bufTable[slot.getSlotValue()].pinCount != 0) {
                    rcWarn = RC.PF_PAGEPINNED;
                } else {
                    if (bufTable[slot.getSlotValue()].bDirty) {
                        psMessage = "Page (" + bufTable[slot.getSlotValue()].pageNum + ") is dirty\n";
                        writeLog(psMessage);
                        if ((rc = writePage(fd, bufTable[slot.getSlotValue()].pageNum, bufTable[slot.getSlotValue()].data)) != RC.PF_SUCCESS) return rc;
                        bufTable[slot.getSlotValue()].bDirty = false;
                    }
                    if ((rc = hashTable.delete(fd, bufTable[slot.getSlotValue()].pageNum)) != RC.PF_SUCCESS || (rc = unlink(slot.getSlotValue())) != RC.PF_SUCCESS || (rc = insertFree(slot.getSlotValue())) != RC.PF_SUCCESS) return rc;
                }
            }
            slot.setSlotValue(next);
        }
        writeLog("All necessary pages flushed.\n");
        return rcWarn;
    }

    public RC forcePages(FileDescriptor fd, PageNum pageNum) {
        RC rc;
        String psMessage = "Forcing page " + fd.toString() + " for (" + pageNum + ").\n";
        writeLog(psMessage);
        Slot slot = new Slot(0);
        slot.setSlotValue(first);
        while (slot.getSlotValue() != INVALID_SLOT) {
            int next = bufTable[slot.getSlotValue()].next;
            if (bufTable[slot.getSlotValue()].fd == fd && (pageNum.getPageNum() == RedBase.ALL_PAGES || bufTable[slot.getSlotValue()].pageNum == pageNum)) {
                psMessage = "Page (" + bufTable[slot.getSlotValue()].pageNum + ") is in buffer pool.\n";
                writeLog(psMessage);
                if (bufTable[slot.getSlotValue()].bDirty) {
                    psMessage = "Page (" + bufTable[slot.getSlotValue()].pageNum + ") is dirty.\n";
                    writeLog(psMessage);
                    if ((rc = writePage(fd, bufTable[slot.getSlotValue()].pageNum, bufTable[slot.getSlotValue()].data)) != RC.PF_SUCCESS) return rc;
                    bufTable[slot.getSlotValue()].bDirty = false;
                }
            }
            slot.setSlotValue(next);
        }
        return RC.PF_SUCCESS;
    }

    public RC printBuffer() {
        System.out.println("buffer contains " + numPages + " pages of size " + pageSize);
        System.out.println("Contents in order from most recently used to " + "least recently used");
        Slot slot = new Slot(0);
        int next;
        while (slot.getSlotValue() != INVALID_SLOT) {
            next = bufTable[slot.getSlotValue()].next;
            System.out.println(slot.getSlotValue() + " :: ");
            System.out.println("  fd= " + bufTable[slot.getSlotValue()].fd.toString());
            System.out.println("  pageNum= " + bufTable[slot.getSlotValue()].pageNum);
            System.out.println("  bDirty= " + bufTable[slot.getSlotValue()].bDirty);
            System.out.println("  pinCount= " + bufTable[slot.getSlotValue()].pinCount);
            slot.setSlotValue(next);
        }
        if (first == INVALID_SLOT) System.out.println("Buffer is empty!"); else System.out.println("All remaining slots are free.");
        return RC.PF_SUCCESS;
    }

    public RC clearBuffer() {
        RC rc;
        Slot slot = new Slot(0);
        int next;
        while (slot.getSlotValue() != INVALID_SLOT) {
            next = bufTable[slot.getSlotValue()].next;
            if (bufTable[slot.getSlotValue()].pinCount == 0) if ((rc = hashTable.delete(bufTable[slot.getSlotValue()].fd, bufTable[slot.getSlotValue()].pageNum)) != RC.PF_SUCCESS || (rc = unlink(slot.getSlotValue())) != RC.PF_SUCCESS || (rc = insertFree(slot.getSlotValue())) != RC.PF_SUCCESS) return rc;
            slot.setSlotValue(next);
        }
        return RC.PF_SUCCESS;
    }

    public RC resizeBuffer(int iNewSize) {
        int i;
        RC rc;
        this.clearBuffer();
        PF_BufPageDesc[] pNewBufTable = new PF_BufPageDesc[iNewSize];
        for (i = 0; i < iNewSize; i++) {
            if ((pNewBufTable[i].data = new byte[pageSize]) == null) {
                System.out.print("Not enough memory for buffer\n");
                break;
            }
            for (i = 0; i < pageSize; i++) {
                pNewBufTable[i].data = null;
            }
            pNewBufTable[i].prev = i - 1;
            pNewBufTable[i].next = i + 1;
        }
        pNewBufTable[0].prev = pNewBufTable[iNewSize - 1].next = INVALID_SLOT;
        int oldFirst = first;
        PF_BufPageDesc[] pOldBufTable = bufTable;
        numPages = iNewSize;
        first = last = INVALID_SLOT;
        free = 0;
        bufTable = pNewBufTable;
        int slot, next, newSlot = 0;
        slot = oldFirst;
        while (slot != INVALID_SLOT) {
            next = pOldBufTable[slot].next;
            if ((rc = hashTable.delete(pOldBufTable[slot].fd, pOldBufTable[slot].pageNum)) != RC.PF_SUCCESS) return rc;
            slot = next;
        }
        slot = oldFirst;
        while (slot != INVALID_SLOT) {
            next = pOldBufTable[slot].next;
            if ((rc = internalAlloc(new Slot(newSlot))) != RC.PF_SUCCESS) return (rc);
            if ((rc = hashTable.insert(pOldBufTable[slot].fd, pOldBufTable[slot].pageNum, new Slot(newSlot))) != RC.PF_SUCCESS || (rc = initPageDesc(pOldBufTable[slot].fd, pOldBufTable[slot].pageNum, new Slot(newSlot))) != RC.PF_SUCCESS) return (rc);
            unlink(newSlot);
            insertFree(newSlot);
            slot = next;
        }
        return RC.PF_SUCCESS;
    }

    public RC getBlockSize(IntegerWrapper length) {
        length.setValue(pageSize);
        return RC.PF_SUCCESS;
    }

    public RC allocateBlock(BytePointer buffer) {
        RC rc = RC.PF_SUCCESS;
        Slot slot = new Slot(0);
        if ((rc = internalAlloc(slot)) != RC.PF_SUCCESS) return rc;
        PageNum pageNum = new PageNum(bufTable[slot.getSlotValue()].data.hashCode());
        if ((rc = hashTable.insert(MEMORY_FD, pageNum, slot)) != RC.PF_SUCCESS || (rc = initPageDesc(MEMORY_FD, pageNum, slot)) != RC.PF_SUCCESS) {
            unlink(slot.getSlotValue());
            insertFree(slot.getSlotValue());
            return rc;
        }
        buffer.setArray(bufTable[slot.getSlotValue()].data);
        return RC.PF_SUCCESS;
    }

    public RC disposeBlock(byte[] buffer) {
        return unpinPage(MEMORY_FD, new PageNum(buffer.hashCode()));
    }

    public void writeLog(String psMessage) {
        FileWriter fw = null;
        String psFileName = null;
        if (fLog == null) {
            int iLogNum = -1;
            boolean bFound = false;
            while (iLogNum < 999 && bFound == false) {
                iLogNum++;
                psFileName = "log/PF_LOG." + iLogNum;
                try {
                    fLog = new File(psFileName);
                    if (fLog.createNewFile()) {
                        bFound = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!bFound) {
                System.err.println("Cannot create a new log file!");
                System.exit(1);
            }
        }
        try {
            fw = new FileWriter(fLog, true);
            fw.write(psMessage + "\n");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
