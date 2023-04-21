package net.sf.reqbook.services.format;

/**
 * $Id: PDFFormat.java,v 1.3 2005/12/09 15:05:25 pavel_sher Exp $
 *
 * @author Pavel Sher
 */
public class PDFFormat extends OutputFormat {

    public boolean equals(Object obj) {
        return obj instanceof PDFFormat;
    }
}
