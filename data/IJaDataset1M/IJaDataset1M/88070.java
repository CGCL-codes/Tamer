package examples.services.relation;

/**
 * @author <a href="mailto:shadow12@users.sourceforge.net">Bronwen Cassidy</a>
 * @version $Revision: 1.1 $
 */
interface SimpleBooksMBean {

    public void setBook(String bookName);

    public String getBook();
}

public class SimpleBooks implements SimpleBooksMBean {

    private String m_name = null;

    public SimpleBooks(String bookName) {
        m_name = bookName;
    }

    public void setBook(String bookName) {
        m_name = bookName;
    }

    public String getBook() {
        return m_name;
    }
}
