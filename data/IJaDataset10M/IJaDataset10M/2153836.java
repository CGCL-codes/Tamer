package org.ramadda.repository;

import org.w3c.dom.*;
import java.io.InputStream;

/**
 */
public abstract class ImportHandler {

    /**
     * _more_
     *
     * @param request _more_
     * @param repository _more_
     * @param uploadedFile _more_
     * @param parentEntry _more_
     *
     * @return _more_
     *
     * @throws Exception _more_
     */
    public Result handleRequest(Request request, Repository repository, String uploadedFile, Entry parentEntry) throws Exception {
        return null;
    }

    /**
     * _more_
     *
     * @param fileName _more_
     * @param stream _more_
     *
     * @return _more_
     *
     * @throws Exception _more_
     */
    public InputStream getStream(String fileName, InputStream stream) throws Exception {
        return null;
    }

    /**
     * _more_
     *
     * @param root _more_
     *
     * @return _more_
     *
     * @throws Exception _more_
     */
    public Element getDOM(Element root) throws Exception {
        return null;
    }
}
