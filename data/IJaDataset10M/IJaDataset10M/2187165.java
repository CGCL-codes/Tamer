package com.sibyl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import android.database.Cursor;

/**
 * works with amazon for now
 * @author sibyl
 *
 */
public class CoverDownloader {

    private static final String TAG = "CoverDownloader";

    private static final String QUERY_AMAZON = "http://webservices.amazon.com/onca/xml?Service=AWSECommerceService" + "&AWSAccessKeyId=0RX30HTGPKA2ZDK3RWG2" + "&Operation=ItemSearch" + "&SearchIndex=Music" + "&ResponseGroup=Images,ItemAttributes,Small";

    /**
     * download the cover for the albumId given and save it into the database
     * 
     * @param mdb database connection
     * @param albumId   album id
     * @return true if cover found, false otherwise
     */
    public static boolean retrieveCover(MusicDB mdb, int albumId) {
        Cursor c = mdb.getAlbumInfo(albumId);
        while (c.moveToNext()) {
            try {
                String q = QUERY_AMAZON + "&Title=" + URLEncoder.encode(c.getString(c.getColumnIndex(Music.ALBUM.NAME)), "UTF-8");
                int col = c.getColumnIndex(Music.ARTIST.ID);
                if (col >= 0 && c.getInt(col) > 1) {
                    q += "&Artist=" + URLEncoder.encode(c.getString(c.getColumnIndex(Music.ARTIST.NAME)), "UTF-8");
                }
                AmazonParser ap = new AmazonParser();
                SAXParserFactory.newInstance().newSAXParser().parse(new URL(q).openStream(), ap);
                String answer = ap.getResult();
                if (answer != null) {
                    File f = new File(Music.COVER_DIR);
                    if (!f.isDirectory()) {
                        if (!f.mkdir()) {
                            throw new IOException("can't create cover folder");
                        }
                    }
                    InputStream in = new URL(answer).openStream();
                    String filename = Music.COVER_DIR + answer.substring(answer.lastIndexOf('/') + 1);
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filename), 8192);
                    byte[] buffer = new byte[1024];
                    int numRead;
                    while ((numRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, numRead);
                    }
                    out.close();
                    in.close();
                    mdb.setCover(albumId, filename);
                    c.close();
                    return true;
                }
            } catch (UnsupportedEncodingException uee) {
            } catch (SAXException saxe) {
            } catch (ParserConfigurationException pce) {
            } catch (IOException ioe) {
            }
        }
        c.close();
        return false;
    }
}
