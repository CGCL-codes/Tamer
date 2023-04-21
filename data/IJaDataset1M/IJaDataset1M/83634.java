package bookshelf.book;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import bookshelf.book.element.Book;
import bookshelf.book.element.Page;
import bookshelf.font.Font;
import bookshelf.font.FontWriter;

/**
 * @author Anton Krasovsky <ak1394@mail.ru>
 *  
 */
public class ChunkedBookPartWriter {

    private static final int DEFAULT_BLOCK_SIZE = 12288;

    private static final int BLOCK_ENTRY_INDEX_SIZE = 2;

    private static final int PER_BLOCK_OVERHEAD = 128;

    private static final String PREFIX = "book";

    private FontWriter fontWriter;

    private int maxBlockSize = DEFAULT_BLOCK_SIZE;

    private long maxPartSize = -1;

    private JarOutput output;

    private Book book;

    private List pages;

    private List registers;

    private ArrayList fontsWritten;

    private int currentPage;

    public ChunkedBookPartWriter(FontWriter fontWriter) throws Exception {
        this.fontWriter = fontWriter;
    }

    public void setOutput(JarOutput output) {
        this.output = output;
        fontsWritten = new ArrayList();
    }

    public void setBook(Book book) throws Exception {
        this.book = book;
        this.pages = book.getPages();
        this.currentPage = 0;
        SpaceOptimizer optimizer = new SpaceOptimizer();
        registers = optimizer.optimizeSpaces(book);
    }

    public void setMaxPartSize(long partSize) {
        this.maxPartSize = partSize;
    }

    public long getMaxPartSize() {
        return maxPartSize;
    }

    public int getMaxBlockSize() {
        return maxBlockSize;
    }

    public void setMaxBlockSize(int chunkSize) {
        this.maxBlockSize = chunkSize;
    }

    public boolean hasMorePages() {
        return pages != null && pages.size() > currentPage ? true : false;
    }

    public Book writePart(int bookId) throws Exception {
        writeFonts();
        Stack index = new Stack();
        int oldCurrentPage = currentPage;
        int blockNumber = 0;
        while (pages.size() > currentPage && output.getWrittenCompressed() < maxPartSize) {
            long spaceLeft = maxPartSize - output.getWrittenCompressed() - (blockNumber * PER_BLOCK_OVERHEAD);
            long blockSize = maxBlockSize < spaceLeft ? maxBlockSize : spaceLeft;
            int pagesInBlock = writeBlock(bookId, blockNumber, blockSize);
            if (pagesInBlock == 0) {
                break;
            }
            index.push(new Integer(currentPage + pagesInBlock - oldCurrentPage));
            blockNumber++;
            currentPage = currentPage + pagesInBlock;
        }
        if (!index.empty()) {
            writeIndex(bookId, index, oldCurrentPage);
            return book;
        } else {
            return null;
        }
    }

    private int writeBlock(int bookId, int blockNumber, long blockSize) throws Exception {
        ByteArrayOutputStream blockBytes = new ByteArrayOutputStream();
        DataOutputStream block = new DataOutputStream(blockBytes);
        ByteArrayOutputStream blockIndexBytes = new ByteArrayOutputStream();
        DataOutputStream blockIndex = new DataOutputStream(blockIndexBytes);
        int pagesWritten = 0;
        while (pages.size() > currentPage + pagesWritten) {
            Page page = (Page) pages.get(currentPage + pagesWritten);
            byte[] pageBytes = getPageBytes(page);
            if (block.size() + pageBytes.length + ((pagesWritten + 1) * BLOCK_ENTRY_INDEX_SIZE) > blockSize) {
                break;
            }
            block.write(pageBytes);
            blockIndex.writeChar((char) block.size());
            pagesWritten++;
        }
        block.flush();
        blockIndex.flush();
        output.putNextEntry(PREFIX + bookId + "/" + Integer.toString(blockNumber));
        output.write(blockIndexBytes.toByteArray());
        output.write(blockBytes.toByteArray());
        output.flush();
        return pagesWritten;
    }

    private void writeFonts() throws Exception {
        for (Iterator iterator = book.getFonts().iterator(); iterator.hasNext(); ) {
            Font font = (Font) iterator.next();
            if (!fontsWritten.contains(font.getId())) {
                fontWriter.writeFont(output, "fonts/", font);
                fontsWritten.add(font.getId());
            }
        }
        output.flush();
    }

    private void writeIndex(int bookId, Stack index, int startPage) throws Exception {
        output.putNextEntry(PREFIX + bookId + "/info");
        DataOutputStream dos = new DataOutputStream(output);
        dos.writeInt((int) book.getPageSize().getWidth());
        dos.writeInt((int) book.getPageSize().getHeight());
        dos.writeInt((int) book.getPageSize().getWidth());
        dos.writeInt((int) book.getPageSize().getHeight());
        dos.writeInt(book.getFonts().size());
        for (Iterator iterator = book.getFonts().iterator(); iterator.hasNext(); ) {
            Font font = (Font) iterator.next();
            dos.writeUTF(font.getId());
        }
        dos.writeInt(((Font) book.getFonts().get(0)).getBackground().getRGB());
        int preferredLineHeight = ((Font) book.getFonts().get(0)).getHeight() + book.getInterlineSpacing();
        dos.writeInt(preferredLineHeight);
        dos.writeInt(registers.size());
        for (Iterator iterator = registers.iterator(); iterator.hasNext(); ) {
            Integer r = (Integer) iterator.next();
            dos.writeInt(r.intValue());
        }
        dos.writeInt(startPage);
        dos.writeInt(index.size());
        for (Iterator iterator = index.iterator(); iterator.hasNext(); ) {
            Integer indexElement = (Integer) iterator.next();
            dos.writeChar((char) indexElement.intValue());
        }
        dos.flush();
    }

    private byte[] getPageBytes(Page page) throws Exception {
        ByteArrayOutputStream pageBAOS = new ByteArrayOutputStream();
        DataOutputStream pageDOS = new DataOutputStream(pageBAOS);
        page.write(pageDOS);
        pageDOS.flush();
        return pageBAOS.toByteArray();
    }
}
