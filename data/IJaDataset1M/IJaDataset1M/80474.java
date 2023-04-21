package org.exist.http.webdav;

import org.exist.http.webdav.methods.Copy;
import org.exist.http.webdav.methods.Delete;
import org.exist.http.webdav.methods.Get;
import org.exist.http.webdav.methods.Head;
import org.exist.http.webdav.methods.Mkcol;
import org.exist.http.webdav.methods.Move;
import org.exist.http.webdav.methods.Options;
import org.exist.http.webdav.methods.Propfind;
import org.exist.http.webdav.methods.Put;
import org.exist.storage.BrokerPool;

/**
 * Create a {@link WebDAVMethod} for the method specified in the
 * HTTP request.
 * 
 * @author wolf
 */
public class WebDAVMethodFactory {

    public static final WebDAVMethod create(String method, BrokerPool pool) {
        if (method.equals("OPTIONS")) return new Options(); else if (method.equals("GET")) return new Get(pool); else if (method.equals("HEAD")) return new Head(pool); else if (method.equals("PUT")) return new Put(pool); else if (method.equals("DELETE")) return new Delete(pool); else if (method.equals("MKCOL")) return new Mkcol(pool); else if (method.equals("PROPFIND")) return new Propfind(pool); else if (method.equals("MOVE")) return new Move(pool); else if (method.equals("COPY")) return new Copy(pool); else return null;
    }
}
