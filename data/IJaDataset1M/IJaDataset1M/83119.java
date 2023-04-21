package xbird.servlet.webdav;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public abstract class DavMethod {

    public DavMethod() {
    }

    public abstract void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
