package dblp.social.hibernate.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.hibernate.validator.Length;

/**
 * A DBLP Publication.
 * Does not exist in the original data, but it useful as all the publications (articles, book chapters and conference papers) share
 * the same structure.
 * 
 * @author ghezzi
 * 
 */
@MappedSuperclass
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NamedQuery(name = "findPublicationById", query = "from DblpPublication d where d.id = :publicationId")
public abstract class DblpPublication {

    /**
	* Unique ID, used by Hibernate.
	*/
    private String id;

    /**
	 * Title of the publication
	 */
    @Length(max = 600)
    private String title = "";

    /**
	 * The page range of the publication.
	 */
    private String pageRange = "";

    private List<DblpPublication> cites = new ArrayList<DblpPublication>();

    private List<Person> authors = new ArrayList<Person>();

    private String dblpKey = "";

    private String year = "";

    private String url = "";

    private String ee = "";

    private String isbn = "";

    private String publisher = "";

    public DblpPublication() {
    }

    public DblpPublication(String title) {
        this.title = title;
    }

    public String getDblpKey() {
        return dblpKey;
    }

    public void setDblpKey(String dblpKey) {
        this.dblpKey = dblpKey;
    }

    /**
	* @return Returns the id.
	*/
    @Id
    @Column(name = "publicationId")
    public String getId() {
        return id;
    }

    /**
	* @param id The id to set.
	*/
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @return the isbn
	 */
    @Transient
    public String getIsbn() {
        return isbn;
    }

    /**
	 * @param isbn the isbn to set
	 */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
	 * @return the ee
	 */
    public String getEe() {
        return ee;
    }

    /**
	 * @param ee the ee to set
	 */
    public void setEe(String ee) {
        this.ee = ee;
    }

    /**
	 * @return the publisher
	 */
    @Transient
    public String getPublisher() {
        return publisher;
    }

    /**
	 * @param publisher the publisher to set
	 */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
	 * @return the url
	 */
    public String getUrl() {
        return url;
    }

    /**
	 * @param url the url to set
	 */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
	 * @return the year
	 */
    public String getYear() {
        return year;
    }

    /**
	 * @param year the year to set
	 */
    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPageRange() {
        return pageRange;
    }

    public void setPageRange(String pageRange) {
        this.pageRange = pageRange;
    }

    @OneToMany
    public List<DblpPublication> getCites() {
        return cites;
    }

    public void setCites(List<DblpPublication> cites) {
        this.cites = cites;
    }

    public void setAuthors(List<Person> authors) {
        this.authors = authors;
    }

    @ManyToMany
    @JoinTable(name = "DblpPublication_Authors", joinColumns = { @JoinColumn(name = "publicationId") }, inverseJoinColumns = { @JoinColumn(name = "personId") })
    public List<Person> getAuthors() {
        return authors;
    }

    public void addAuthor(Person author) {
        this.authors.add(author);
    }
}
