package net.sf.dictccfe.and;

import java.io.File;
import java.io.FileInputStream;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

/** Exact searching for keyword descriptions.
  * @author Daniel Stoinski
  * @version $Revision$
  */
public final class SearchExact extends Search {

    /** Implementation of the exact search for keyword descriptions.
    * @param aDb           the database in which to search for keyword
    *                      descriptions.
    * @param aKey          the keyword, for which to search for keyword
    *                      descriptions.
    *                      Never null.
    * @param aSearchResult interface for result callbacks. May be null.
    * @throws Exception various IOException, UnsupportedEncodingException
    */
    protected final void dosearch(SQLiteDatabase aDb, CharSequence aKey, ISearchResult aSearchResult) throws Exception {
        byte[] buf;
        Cursor c;
        File dictfile;
        FileInputStream fis;
        Search.DictConfig cfg;
        String enc, res;
        int idx, num, lastidx, beg, begold, len, ln;
        c = null;
        fis = null;
        try {
            if (aDb != null && aKey != null && aSearchResult != null && (cfg = this.getDictConfig()) != null && (enc = cfg.getDictEnc()) != null && (dictfile = cfg.getDictFile()) != null) {
                idx = -1;
                num = 0;
                c = aDb.rawQuery("SELECT idx, num FROM keys WHERE key = ?", new String[] { aKey.toString().toUpperCase() });
                if (c.moveToNext() && this.getGoon()) {
                    idx = c.getInt(0);
                    num = c.getInt(1);
                }
                c.close();
                c = null;
                if (idx >= 0 && num > 0 && this.getGoon()) {
                    lastidx = idx + num;
                    c = aDb.rawQuery("SELECT beg, len FROM refs WHERE idx >= ? AND idx < ? ORDER BY beg LIMIT 20", new String[] { Integer.toString(idx), Integer.toString(lastidx) });
                    fis = new FileInputStream(dictfile);
                    begold = 0;
                    buf = new byte[256];
                    while (c.moveToNext() && this.getGoon()) {
                        beg = c.getInt(0);
                        len = c.getInt(1);
                        if (buf.length < len) buf = new byte[len];
                        fis.skip(beg - begold);
                        ln = fis.read(buf, 0, len);
                        res = new String(buf, 0, ln, enc);
                        aSearchResult.addResult(res);
                        begold = beg + len;
                    }
                }
            }
        } finally {
            if (c != null) try {
                c.close();
            } catch (Exception excp) {
            }
            if (fis != null) try {
                fis.close();
            } catch (Exception excp) {
            }
        }
    }
}
