package org.openofficesearch.io.openoffice;

import org.openofficesearch.io.ReaderFileFilter;

/**
 * A filter that only accepts OpenOffice documents<br />
 * Created: 2005
 * @author Connor Garvey
 * @version 0.1.1
 * @since 0.0.1
 */
public class OpenOfficeReaderFileFilter extends ReaderFileFilter {

    /**
   * Creates a new instance of this class
   */
    public OpenOfficeReaderFileFilter() {
        super();
        super.addReader(new OpenOfficeWriterReader());
    }
}
