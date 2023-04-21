package com.msimone.mobreader.text;

import com.msimone.j2me.DataStorer;
import com.msimone.j2me.Waiter;
import java.io.InputStreamReader;

public class SimpleFileTextProvider extends Thread implements TextProvider {

    private static final String path = "/books/";

    private static final String extension = ".txt";

    private final String fileName;

    private String fileContent;

    private final Paginator paginator;

    private int pageSize = TextProvider.DEFAULT_PAGE_SIZE;

    private int position = 0;

    private int currentPage = 0;

    private int bookmark = 1;

    private static final String SFTP_STORER = "mobreader.bookmarks";

    private final DataStorer dataStorer;

    private Waiter caller;

    public SimpleFileTextProvider(String file, Waiter menu) {
        this.fileName = file;
        paginator = new Paginator();
        caller = menu;
        dataStorer = new DataStorer(SFTP_STORER);
    }

    public void run() {
        InputStreamReader input = new InputStreamReader(getClass().getResourceAsStream(path + fileName + extension));
        StringBuffer line = new StringBuffer();
        int c;
        try {
            while ((c = (input.read())) != -1) {
                line.append((char) c);
            }
        } catch (Exception e) {
            line.append("ERROR: " + e.getMessage());
        }
        fileContent = line.toString();
        paginator.paginate(fileContent, pageSize);
        recoverBookmark();
        caller.ready();
    }

    private void recoverBookmark() {
        String stringBookmark = dataStorer.getRecord(1);
        if (stringBookmark != null) {
            bookmark = Integer.parseInt(stringBookmark);
        }
    }

    private void persistBookmark() {
        dataStorer.writeRecord(1, Integer.toString(bookmark));
    }

    public String getNextLine() {
        int begin = position;
        int end = fileContent.indexOf('\n', begin);
        if (end >= 0) {
            position = end + 1;
            refreshPageNumber();
            return fileContent.substring(begin, end);
        }
        position = fileContent.length();
        refreshPageNumber();
        return fileContent.substring(begin);
    }

    public String getNextPage() {
        if (currentPage < paginator.numberOfPages()) currentPage++;
        return getCurrentPage();
    }

    private void refreshPageNumber() {
        currentPage = paginator.pageOf(position);
    }

    public int pageNumber() {
        return currentPage;
    }

    public String getPreviousLine() {
        return null;
    }

    public String getPreviousPage() {
        if (currentPage > 1) currentPage--;
        return getCurrentPage();
    }

    public String getCurrentPage() {
        int begin = paginator.beginOfPage(currentPage);
        int end = paginator.endOfPage(currentPage);
        position = end;
        return fileContent.substring(begin, end);
    }

    public void mark() {
        bookmark = currentPage;
        persistBookmark();
    }

    public void reset() {
        currentPage = bookmark;
        position = paginator.beginOfPage(currentPage);
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getMark() {
        return bookmark;
    }

    public boolean hasMore() {
        return (position < fileContent.length());
    }

    public int numberOfPages() {
        return paginator.numberOfPages();
    }
}
