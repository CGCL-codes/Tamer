package com.tiani.prnscp.print;

import java.awt.print.Pageable;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.print.attribute.standard.Chromaticity;

/**
 *  <description>
 *
 * @author     <a href="mailto:gunter@tiani.com">gunter zeilinger</a>
 * @since      March 30, 2003
 * @version    $Revision: 6066 $
 */
class PrintImageJob implements Pageable {

    private final PrintableFilmBox filmBox;

    private final String name;

    /**
     *Constructor for the PrintImageJob object
     *
     * @param  service          Description of the Parameter
     * @param  f                Description of the Parameter
     * @param  config           Description of the Parameter
     * @exception  IOException  Description of the Exception
     */
    public PrintImageJob(PrinterService service, File f, String config, Chromaticity chromaticity) throws IOException {
        if (!f.isFile()) {
            throw new FileNotFoundException("Could not find " + f);
        }
        this.name = f.getName() + "[" + chromaticity + "/" + (config.length() == 0 ? "not calibrated" : config) + "]";
        this.filmBox = new PrintableFilmBox(service, f, config, chromaticity, name);
    }

    /**
     *  Gets the name attribute of the PrintImageJob object
     *
     * @return    The name value
     */
    public String getName() {
        return name;
    }

    /**
     *  Gets the numberOfPages attribute of the PrintImageJob object
     *
     * @return    The numberOfPages value
     */
    public int getNumberOfPages() {
        return 1;
    }

    /**
     *  Gets the pageFormat attribute of the PrintImageJob object
     *
     * @param  pageIndex                      Description of the Parameter
     * @return                                The pageFormat value
     * @exception  IndexOutOfBoundsException  Description of the Exception
     */
    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        if (pageIndex != 0) {
            throw new IndexOutOfBoundsException("pageIndex: " + pageIndex);
        }
        return filmBox.getPageFormat();
    }

    /**
     *  Gets the printable attribute of the PrintImageJob object
     *
     * @param  pageIndex                      Description of the Parameter
     * @return                                The printable value
     * @exception  IndexOutOfBoundsException  Description of the Exception
     */
    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
        if (pageIndex != 0) {
            throw new IndexOutOfBoundsException("pageIndex: " + pageIndex);
        }
        return filmBox;
    }
}
