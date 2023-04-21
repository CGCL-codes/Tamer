package com.google.code.ihtika.IhtikaClient.Lucene;

import com.google.code.ihtika.IhtikaClient.InternalFunctions;
import com.google.code.ihtika.IhtikaClient.Vars.Ini;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import net.htmlparser.jericho.Source;
import org.apache.lucene.index.Term;

/**
 *
 * @author Arthur Khusnutdinov
 */
public class SourceLocal {

    InternalFunctions InternalFunctions_local;

    public Source get_source(String pageURL, Boolean checkInBase, InternalFunctions InternalFunctions_local) {
        URL url;
        URLConnection conn;
        Reader inReader;
        Source source = null;
        Term term;
        String LastModified = "";
        Boolean updateData = false;
        try {
            url = new URL(pageURL);
            conn = url.openConnection();
            conn.setRequestProperty("Accept-Charset", "windows-1251");
            if (checkInBase) {
                for (int i = 0; ; i++) {
                    String name = conn.getHeaderFieldKey(i);
                    String value = conn.getHeaderField(i);
                    if (name == null && value == null) {
                        break;
                    }
                    if ("Last-Modified".equals(name)) {
                        LastModified = value;
                    }
                }
                Ini.rs = Ini.stmt.executeQuery("select count(1) as qwe from " + " PUBLIC.PAGES " + "where url = '" + pageURL + "';");
                Ini.rs.next();
                if (Ini.rs.getInt("qwe") == 0) {
                    Ini.stmt.executeUpdate("insert into PUBLIC.PAGES(url, lastUpdateDate) " + " values('" + pageURL + "', " + "'" + LastModified + "'" + ");");
                } else {
                    Ini.rs = Ini.stmt.executeQuery("select lastUpdateDate from " + " PUBLIC.PAGES " + "where url = '" + pageURL + "';");
                    Ini.rs.next();
                    if (!Ini.rs.getString("lastUpdateDate").equals(LastModified)) {
                        updateData = true;
                    } else {
                        return null;
                    }
                }
            }
            inReader = new InputStreamReader(conn.getInputStream(), "windows-1251");
            source = new Source(inReader);
            source.setLogger(null);
            source.fullSequentialParse();
            if (updateData) {
                Ini.stmt.executeUpdate("delete from PUBLIC.LINKDATA " + "where id in (" + "select id from PUBLIC.PAGES " + "where url = '" + pageURL + "'" + ")");
                Ini.stmt.executeUpdate("delete from PUBLIC.PAGES " + "where url = '" + pageURL + "';");
                Ini.stmt.executeUpdate("insert into PUBLIC.PAGES " + " values('" + pageURL + "', " + "'" + LastModified + "'" + ");");
                term = new Term("link", pageURL);
                InternalFunctions_local.writerForLucene.deleteDocuments(term);
            }
        } catch (Exception ex) {
            Ini.logger.fatal("Error: ", ex);
        }
        return source;
    }
}
