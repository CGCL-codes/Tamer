package tinybase.pf.test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import tinybase.basic.BytePointer;
import tinybase.basic.IntegerWrapper;
import tinybase.basic.RC;
import tinybase.pf.Errors;
import tinybase.pf.PF_FileHandle;
import tinybase.pf.PF_Internal;
import tinybase.pf.PF_Manager;
import tinybase.pf.PF_PageHandle;
import tinybase.pf.PageNum;
import tinybase.pf.StatisticsMgr;

public class TestPf1 {

    StatisticsMgr pStatisticsMgr;

    private static String FILE1 = "files/file1";

    private static String FILE2 = "files/file2";

    /**
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
        RC rc = RC.PF_SUCCESS;
        System.err.flush();
        System.out.flush();
        System.out.println("Starting PF layer test.\n");
        System.out.flush();
        File F1 = new File(FILE1);
        File F2 = new File(FILE2);
        F1.delete();
        F2.delete();
        TestPf1 t1 = new TestPf1();
        if ((rc = t1.TestPF()) != RC.PF_SUCCESS) Errors.printErrors(rc);
        System.out.println("Ending PF layer test.\n\n");
    }

    /***
	 * 
	 * @param pfm: Manager, the file system manager
	 * @param fname: String, name of the file
	 * @return
	 * @throws IOException
	 */
    RC WriteFile(PF_Manager pfm, String fname) throws IOException {
        PF_FileHandle fh = new PF_FileHandle();
        PF_PageHandle ph = new PF_PageHandle();
        RC rc = RC.PF_SUCCESS;
        BytePointer refArray = new BytePointer();
        int pageNum = 0;
        PageNum refPageNum = new PageNum();
        int i = 0;
        System.out.println("Opening file: " + fname + "\n");
        if ((rc = pfm.openFile(fname, fh)) != RC.PF_SUCCESS) return (rc);
        for (i = 0; i < PF_Internal.PF_BUFFER_SIZE; i++) {
            if ((rc = fh.allocatePage(ph)) != RC.PF_SUCCESS || (rc = ph.getData(refArray)) != RC.PF_SUCCESS || (rc = ph.getPageNum(refPageNum)) != RC.PF_SUCCESS) return (rc);
            pageNum = refPageNum.getPageNum();
            if (i != pageNum) {
                System.out.println("Page number incorrect: " + pageNum + " " + i);
                System.exit(1);
            }
            ByteBuffer.wrap(refArray.getArray()).putInt(0, pageNum);
            System.out.println("Page allocated: " + pageNum);
        }
        if ((rc = fh.allocatePage(ph)) != RC.PF_NOBUF) {
            System.out.println("Pin too many pages should fail: ");
            return (rc);
        }
        System.out.println("Unpinning pages and closing the file\n");
        for (i = 0; i < PF_Internal.PF_BUFFER_SIZE; i++) {
            if ((rc = fh.unpinPage(i)) != RC.PF_SUCCESS) return (rc);
        }
        if ((rc = pfm.closeFile(fh)) != RC.PF_SUCCESS) return (rc);
        return RC.PF_SUCCESS;
    }

    /***
	 * Read file represented by fh, print page numbers we have writen
	 * @param fh: FileHandle, a handle representing an existing open file
	 * @return
	 */
    RC PrintFile(PF_FileHandle fh) {
        PF_PageHandle ph = new PF_PageHandle();
        RC rc = RC.PF_SUCCESS;
        BytePointer refArray = new BytePointer();
        int pageNum = 0, temp = 0;
        PageNum refPageNum = new PageNum();
        System.out.println("Reading file\n");
        if ((rc = fh.getFirstPage(ph)) != RC.PF_SUCCESS) return (rc);
        do {
            if ((rc = ph.getData(refArray)) != RC.PF_SUCCESS || (rc = ph.getPageNum(refPageNum)) != RC.PF_SUCCESS) return (rc);
            pageNum = refPageNum.getPageNum();
            temp = ByteBuffer.wrap(refArray.getArray()).getInt(0);
            System.out.println("Got page: " + pageNum + " " + temp);
            if ((rc = fh.unpinPage(pageNum)) != RC.PF_SUCCESS) return (rc);
        } while ((rc = fh.getNextPage(pageNum, ph)) != RC.PF_SUCCESS);
        if (rc != RC.PF_EOF) return (rc);
        System.out.println("EOF reached\n");
        return RC.PF_SUCCESS;
    }

    /***
	 * Open file, read it, print it to System.out
	 * @param pfm: Manager, file system manager
	 * @param fname: String, file name
	 * @return
	 * @throws IOException
	 */
    RC ReadFile(PF_Manager pfm, String fname) throws IOException {
        System.out.println("run read!");
        PF_FileHandle fh = new PF_FileHandle();
        RC rc = RC.PF_SUCCESS;
        System.out.println("Opening: " + fname + "\n");
        if ((rc = pfm.openFile(fname, fh)) != RC.PF_SUCCESS || (rc = PrintFile(fh)) != RC.PF_SUCCESS || (rc = pfm.closeFile(fh)) != RC.PF_SUCCESS) return (rc); else return RC.PF_SUCCESS;
    }

    RC TestPF() throws IOException {
        PF_Manager pfm = new PF_Manager();
        PF_FileHandle fh1 = new PF_FileHandle();
        PF_FileHandle fh2 = new PF_FileHandle();
        PF_PageHandle ph = new PF_PageHandle();
        RC rc = RC.PF_SUCCESS;
        BytePointer refArray = new BytePointer();
        int pageNum, temp;
        PageNum refPageNum = new PageNum();
        IntegerWrapper refInteger = new IntegerWrapper();
        int len = 0;
        pfm.getBlockSize(refInteger);
        len = refInteger.getValue();
        System.out.println("get block size returned " + len);
        System.out.println("Creating and opening two files\n");
        if ((rc = pfm.createFile(FILE1)) != RC.PF_SUCCESS || (rc = pfm.createFile(FILE2)) != RC.PF_SUCCESS || (rc = WriteFile(pfm, FILE1)) != RC.PF_SUCCESS || (rc = ReadFile(pfm, FILE1)) != RC.PF_SUCCESS || (rc = WriteFile(pfm, FILE2)) != RC.PF_SUCCESS || (rc = ReadFile(pfm, FILE2)) != RC.PF_SUCCESS || (rc = pfm.openFile(FILE1, fh1)) != RC.PF_SUCCESS || (rc = pfm.openFile(FILE2, fh2)) != RC.PF_SUCCESS) return (rc);
        System.out.println("Disposing of alternate pages\n");
        for (int i = 0; i < PF_Internal.PF_BUFFER_SIZE; i++) {
            if (i % 2 == 1) {
                if ((rc = fh1.disposePage(i)) != RC.PF_SUCCESS) return (rc);
            } else {
                if ((rc = fh2.disposePage(i)) != RC.PF_SUCCESS) return (rc);
            }
        }
        System.out.println("Closing and destroying both files\n");
        if ((rc = fh1.flushPages()) != RC.PF_SUCCESS || (rc = fh2.flushPages()) != RC.PF_SUCCESS || (rc = pfm.closeFile(fh1)) != RC.PF_SUCCESS || (rc = pfm.closeFile(fh2)) != RC.PF_SUCCESS || (rc = ReadFile(pfm, FILE1)) != RC.PF_SUCCESS || (rc = ReadFile(pfm, FILE2)) != RC.PF_SUCCESS || (rc = pfm.destroyFile(FILE1)) != RC.PF_SUCCESS || (rc = pfm.destroyFile(FILE2)) != RC.PF_SUCCESS) return (rc);
        System.out.println("Creating and opening files again\n");
        if ((rc = pfm.createFile(FILE1)) != RC.PF_SUCCESS || (rc = pfm.createFile(FILE2)) != RC.PF_SUCCESS || (rc = WriteFile(pfm, FILE1)) != RC.PF_SUCCESS || (rc = WriteFile(pfm, FILE2)) != RC.PF_SUCCESS || (rc = pfm.openFile(FILE1, fh1)) != RC.PF_SUCCESS || (rc = pfm.openFile(FILE2, fh2)) != RC.PF_SUCCESS) return (rc);
        System.out.println("Allocating additional pages in both files\n");
        for (int i = PF_Internal.PF_BUFFER_SIZE; i < PF_Internal.PF_BUFFER_SIZE * 2; i++) {
            if ((rc = fh2.allocatePage(ph)) != RC.PF_SUCCESS || (rc = ph.getData(refArray)) != RC.PF_SUCCESS || (rc = ph.getPageNum(refPageNum)) != RC.PF_SUCCESS) return (rc);
            pageNum = refPageNum.getPageNum();
            if (i != pageNum) {
                System.out.println("Page number is incorrect:" + pageNum + " " + i + "\n");
                System.exit(1);
            }
            ByteBuffer.wrap(refArray.getArray()).putInt(0, pageNum);
            if ((rc = fh2.markDirty(pageNum)) != RC.PF_SUCCESS || (rc = fh2.unpinPage(pageNum)) != RC.PF_SUCCESS) {
                return rc;
            }
            if ((rc = fh1.allocatePage(ph)) != RC.PF_SUCCESS || (rc = ph.getData(refArray)) != RC.PF_SUCCESS || (rc = ph.getPageNum(refPageNum)) != RC.PF_SUCCESS) {
                return rc;
            }
            pageNum = refPageNum.getPageNum();
            if (i != pageNum) {
                System.out.println("Page number is incorrect:" + pageNum + " ");
                System.exit(1);
            }
            ByteBuffer.wrap(refArray.getArray()).putInt(0, pageNum);
            if ((rc = fh1.markDirty(pageNum)) != RC.PF_SUCCESS || (rc = fh1.unpinPage(pageNum)) != RC.PF_SUCCESS) {
                return rc;
            }
        }
        System.out.println("Disposing of alternate additional pages\n");
        for (int i = PF_Internal.PF_BUFFER_SIZE; i < PF_Internal.PF_BUFFER_SIZE * 2; i++) {
            if (i % 2 == 1) {
                if ((rc = fh1.disposePage(i)) != RC.PF_SUCCESS) return rc;
            } else {
                if ((rc = fh2.disposePage(i)) != RC.PF_SUCCESS) return rc;
            }
        }
        System.out.println("Getting file 2 remaining additional pages\n");
        for (int i = PF_Internal.PF_BUFFER_SIZE; i < PF_Internal.PF_BUFFER_SIZE * 2; i++) {
            if (i % 2 == 1) {
                if ((rc = fh2.getThisPage(i, ph)) != RC.PF_SUCCESS || (rc = ph.getData(refArray)) != RC.PF_SUCCESS || (rc = ph.getPageNum(refPageNum)) != RC.PF_SUCCESS) {
                    return rc;
                }
                temp = ByteBuffer.wrap(refArray.getArray()).getInt(0);
                pageNum = refPageNum.getPageNum();
                System.out.println("Page: " + pageNum + " " + temp);
                if ((rc = fh2.unpinPage(i)) != RC.PF_SUCCESS) return rc;
            }
        }
        System.out.println("Getting file 1 remaining additional pages\n");
        for (int i = PF_Internal.PF_BUFFER_SIZE; i < PF_Internal.PF_BUFFER_SIZE * 2; i++) {
            if (i % 2 == 0) {
                if ((rc = fh1.getThisPage(i, ph)) != RC.PF_SUCCESS || (rc = ph.getData(refArray)) != RC.PF_SUCCESS || (rc = ph.getPageNum(refPageNum)) != RC.PF_SUCCESS) {
                    return rc;
                }
                temp = ByteBuffer.wrap(refArray.getArray()).getInt(0);
                pageNum = refPageNum.getPageNum();
                System.out.println("Page: " + pageNum + " " + temp);
                if ((rc = fh1.unpinPage(i)) != RC.PF_SUCCESS) return rc;
            }
        }
        System.out.println("Printing file 2, then file 1\n");
        if ((rc = PrintFile(fh2)) != RC.PF_SUCCESS || (rc = PrintFile(fh1)) != RC.PF_SUCCESS) return (rc);
        System.out.println("Putting stuff into the holes of file 1\n");
        for (int i = 0; i < PF_Internal.PF_BUFFER_SIZE / 2; i++) {
            if ((rc = fh1.allocatePage(ph)) != RC.PF_SUCCESS || (rc = ph.getData(refArray)) != RC.PF_SUCCESS || (rc = ph.getPageNum(refPageNum)) != RC.PF_SUCCESS) return (rc);
            pageNum = refPageNum.getPageNum();
            ByteBuffer.wrap(refArray.getArray()).putInt(0, pageNum);
            if ((rc = fh1.markDirty(pageNum)) != RC.PF_SUCCESS || (rc = fh1.unpinPage(pageNum)) != RC.PF_SUCCESS) return (rc);
        }
        System.out.println("Print file 1 and then close both files\n");
        if ((rc = PrintFile(fh1)) != RC.PF_SUCCESS || (rc = pfm.closeFile(fh1)) != RC.PF_SUCCESS || (rc = pfm.closeFile(fh2)) != RC.PF_SUCCESS) return (rc);
        System.out.println("Reopen file 1 and test some error conditions\n");
        if ((rc = pfm.openFile(FILE1, fh1)) != RC.PF_SUCCESS) return (rc);
        if ((rc = fh1.disposePage(100)) != RC.PF_INVALIDPAGE) {
            System.out.println("Dispose invalid page should fail: ");
            return (rc);
        }
        if ((rc = fh1.getThisPage(1, ph)) != RC.PF_SUCCESS) return (rc);
        if ((rc = fh1.disposePage(1)) != RC.PF_PAGEPINNED) {
            System.out.println("Dispose pinned page should fail: ");
            return (rc);
        }
        if ((rc = ph.getData(refArray)) != RC.PF_SUCCESS || (rc = ph.getPageNum(refPageNum)) != RC.PF_SUCCESS) return (rc);
        pageNum = refPageNum.getPageNum();
        temp = ByteBuffer.wrap(refArray.getArray()).getInt(0);
        if (temp != 1 || pageNum != -1) {
            System.out.println("Asked for page 1, got: " + pageNum + " " + temp);
            System.exit(1);
        }
        if ((rc = fh1.unpinPage(pageNum)) != RC.PF_SUCCESS) return (rc);
        if ((rc = fh1.unpinPage(pageNum)) != RC.PF_PAGEUNPINNED) {
            System.out.println("Unpin unpinned page should fail: ");
            return (rc);
        }
        System.out.println("Opening file 1 twice, printing out both copies\n");
        if ((rc = pfm.openFile(FILE1, fh2)) != RC.PF_SUCCESS) return (rc);
        if ((rc = PrintFile(fh1)) != RC.PF_SUCCESS || (rc = PrintFile(fh2)) != RC.PF_SUCCESS) return (rc);
        System.out.println("Closing and destroying both files\n");
        if ((rc = pfm.closeFile(fh1)) != RC.PF_SUCCESS || (rc = pfm.closeFile(fh2)) != RC.PF_SUCCESS || (rc = pfm.destroyFile(FILE1)) != RC.PF_SUCCESS || (rc = pfm.destroyFile(FILE2)) != RC.PF_SUCCESS) return (rc);
        return RC.PF_SUCCESS;
    }
}
