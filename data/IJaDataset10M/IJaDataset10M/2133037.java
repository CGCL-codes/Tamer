package jsp2.examples.simpletag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import jsp2.examples.BookBean;

/**
 * SimpleTag handler that pretends to search for a book, and stores
 * the result in a scoped variable.
 */
public class FindBookSimpleTag extends SimpleTagSupport {

    private String var;

    private static final String BOOK_TITLE = "The Lord of the Rings";

    private static final String BOOK_AUTHOR = "J. R. R. Tolkein";

    private static final String BOOK_ISBN = "0618002251";

    @Override
    public void doTag() throws JspException {
        BookBean book = new BookBean(BOOK_TITLE, BOOK_AUTHOR, BOOK_ISBN);
        getJspContext().setAttribute(this.var, book);
    }

    public void setVar(String var) {
        this.var = var;
    }
}
