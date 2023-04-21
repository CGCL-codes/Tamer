package com.finalist.jag.template;

import java.util.*;
import com.finalist.jag.*;

/**
 * Class TemplateHeaderCollection
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class TemplateHeaderCollection {

    /** Field headerLines           */
    private HeaderLine[] headerLines = null;

    /**
    * Constructor TemplateHeaderCollection
    *
    *
    * @param headerLines
    *
    */
    public TemplateHeaderCollection(HeaderLine[] headerLines) {
        this.headerLines = headerLines;
    }

    /**
    * Method getHeaderUrl
    *
    *
    * @param s
    *
    * @return
    *
    * @throws JagException
    *
    */
    public UrlHeaderLine getHeaderUrl(String s) throws JagException {
        UrlHeaderLine line = findHeaderUrl(s);
        if (line == null) {
            throw new JagException("Missing header definition for the taglibrary " + s);
        }
        return line;
    }

    /**
    * Method findHeaderUrl
    *
    *
    * @param s
    *
    * @return
    *
    */
    public UrlHeaderLine findHeaderUrl(String s) {
        for (int i = 0; i < headerLines.length; i++) {
            HeaderLine headerLine = headerLines[i];
            if (!(headerLine instanceof UrlHeaderLine)) {
                continue;
            }
            UrlHeaderLine urlHeader = (UrlHeaderLine) headerLine;
            if (s.equals(urlHeader.getPrefix())) {
                return urlHeader;
            }
        }
        return null;
    }
}

;
