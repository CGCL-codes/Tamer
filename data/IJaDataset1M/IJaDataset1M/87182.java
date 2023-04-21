package adamb.ogg;

import java.io.*;

/**Converts a {@link PageStream} into a {@link LogicalPageStream}
 by returning only pages with the correct serial number*/
public class StreamSerialFilter implements LogicalPageStream {

    private PageStream ps;

    private boolean haveSerialNumber;

    private int correctSerialNumber;

    private boolean throwUponForeignPages;

    public StreamSerialFilter(PageStream ps, boolean throwUponForeignPages) {
        this(ps, throwUponForeignPages, null);
    }

    public StreamSerialFilter(PageStream ps, boolean throwUponForeignPages, Integer streamSerialNumber) {
        this.ps = ps;
        if (streamSerialNumber != null) {
            haveSerialNumber = true;
            correctSerialNumber = streamSerialNumber.intValue();
        } else haveSerialNumber = false;
        this.throwUponForeignPages = throwUponForeignPages;
    }

    public Page next() throws ForeignPageException, IOException {
        while (true) {
            Page page = ps.next();
            if (page != null) {
                if (haveSerialNumber) {
                    if (page.streamSerialNumber == correctSerialNumber) return page; else if (throwUponForeignPages) throw new ForeignPageException("Found Ogg page from a different stream, #" + page.streamSerialNumber + "; expected #" + correctSerialNumber + "!");
                } else {
                    correctSerialNumber = page.streamSerialNumber;
                    haveSerialNumber = true;
                    return page;
                }
            } else return null;
        }
    }
}
