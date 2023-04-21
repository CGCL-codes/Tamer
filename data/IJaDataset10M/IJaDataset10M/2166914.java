package corner.demo.model.xfire;

/**
 * @author <a href=mailto:Ghostbb@bjmaxinfo.com>Ghostbb</a>
 * @version $Revision$
 * @since 0.0.1
 */
public class Book {

    private String author;

    private String title;

    private String isbn;

    /**
	 * @return Returns the author.
	 */
    public String getAuthor() {
        return author;
    }

    /**
	 * @param author The author to set.
	 */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
	 * @return Returns the title.
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * @param title The title to set.
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * @return Returns the isbn.
	 */
    public String getIsbn() {
        return isbn;
    }

    /**
	 * @param isbn The isbn to set.
	 */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
