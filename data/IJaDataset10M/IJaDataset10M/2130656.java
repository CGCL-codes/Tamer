package com.iv.flash.util;

import com.iv.flash.api.FlashFile;
import com.iv.flash.url.BufferedUrl;
import com.iv.flash.url.IVUrl;
import com.iv.flash.xml.XMLDataSource;
import com.iv.flash.xml.XMLHelper;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Helper class for reading any kind of datasources.
 *
 * @author Dmitry Skavish
 * @see XMLDataSource
 * @see DataSource
 */
public class DataSourceHelper {

    /**
     * Reads datasource from given url, detects it's type: xml or text
     * and creates either LineReader (text) or else (xml)<P>
     * Datasources can be specified by url or inline. If url starts
     * with '#' then this is inline datasource which is completely given
     * in the url string.
     *
     * @param surl      url or inline datasource
     * @param flashFile current flash file from which this datasource is requested
     * @return either LineReader (plain datasource) or IVUrl
     * @exception IOException
     * @exception IVException
     */
    public static Object readContextData(String surl, FlashFile flashFile) throws IVException, IOException {
        if (surl == null || surl.length() == 0) {
            throw new IOException("null datasource");
        }
        Object dsrc;
        if (surl.charAt(0) == '#') {
            if (surl.charAt(1) == '<') {
                byte[] bytes = flashFile.getEncoding() != null ? surl.substring(1).getBytes(flashFile.getEncoding()) : PropertyManager.defaultEncoding != null ? surl.substring(1).getBytes(PropertyManager.defaultEncoding) : surl.substring(1).getBytes();
                dsrc = new BufferedUrl(new FlashBuffer(bytes));
            } else {
                dsrc = new NativeLineReader(surl);
            }
        } else {
            int idx = surl.indexOf(';');
            if (idx == -1) {
                dsrc = new BufferedUrl(surl, flashFile);
            } else {
                StringTokenizer st = new StringTokenizer(surl, ";");
                IVVector urls = new IVVector();
                while (st.hasMoreTokens()) {
                    IVUrl url = IVUrl.newUrl(st.nextToken(), flashFile);
                    urls.addElement(url);
                }
                dsrc = new MultipleUrlsReader(urls, flashFile);
            }
        }
        if (dsrc instanceof BufferedUrl) {
            BufferedUrl burl = (BufferedUrl) dsrc;
            byte[] buf = burl.getFlashBuffer().getBuf();
            if (!(buf.length > 3 && (buf[0] == '<' || (buf[0] == -1 && buf[1] == -2 && buf[2] == '<')))) {
                dsrc = Util.getUrlReader(flashFile, burl);
            }
        }
        return dsrc;
    }

    /**
     * Return context data.<p>
     * Reads datasource from given url, detects it's type: xml or text
     * and creates either xml Node or array of strings.<P>
     * Datasources can be specified by url or inline. If url starts
     * with '#' then this is inline datasource which is completely given
     * in the utl string.
     *
     * @param surl      url or inline datasource
     * @param flashFile current flash file from which this datasource is requested
     * @return either Node or String[][]
     * @exception IVException
     * @exception IOException
     */
    public static Object getContextData(String surl, FlashFile flashFile) throws IVException, IOException {
        Object dsrc = readContextData(surl, flashFile);
        if (dsrc instanceof LineReader) {
            DataSource ds = new DataSource((LineReader) dsrc);
            String[][] data = ds.getData();
            return data;
        } else {
            try {
                return XMLHelper.getNode((IVUrl) dsrc);
            } catch (Exception e) {
                if (e instanceof IOException) throw (IOException) e;
                throw new IVException(e);
            }
        }
    }
}
