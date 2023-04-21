package org.jdmp.jetty.html.pages;

import javax.servlet.http.HttpServletRequest;
import org.jdmp.jetty.html.Page;
import org.jdmp.jetty.html.tags.H1Tag;
import org.ujmp.core.Matrix;

public class DefaultMatrixPage extends Page {

    private static final long serialVersionUID = -1585763405757847068L;

    public DefaultMatrixPage(HttpServletRequest request, String path, Matrix matrix, Object... parameters) {
        super();
        if (matrix == null) {
            setTitle("JDMP Search: not found");
            add(new H1Tag("sample not found"));
        } else {
            setTitle("JDMP Search: " + matrix.getLabel());
            add(new H1Tag(matrix.getLabel()));
        }
    }
}
