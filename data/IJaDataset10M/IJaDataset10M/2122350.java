package edu.ucdavis.cs.dblp.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAttribute;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import de.unitrier.dblp.Article;
import de.unitrier.dblp.Author;
import de.unitrier.dblp.Book;
import de.unitrier.dblp.Cite;
import de.unitrier.dblp.Incollection;
import de.unitrier.dblp.Inproceedings;
import de.unitrier.dblp.Journal;
import de.unitrier.dblp.Mastersthesis;
import de.unitrier.dblp.Phdthesis;
import de.unitrier.dblp.Proceedings;
import de.unitrier.dblp.Publisher;
import de.unitrier.dblp.School;
import de.unitrier.dblp.Www;

@Entity
@NamedQueries({ @NamedQuery(name = "Publication.all", query = "FROM Publication p"), @NamedQuery(name = "Publication.byId", query = "FROM Publication p WHERE p.key = :key"), @NamedQuery(name = "Publication.byAuthorName", query = "SELECT p FROM Publication p, IN(p.author) a WHERE a.content=:name ORDER BY p.year DESC NULLS LAST"), @NamedQuery(name = "Publication.byCategory", query = "SELECT p FROM Publication p, IN(p.content.categories) cats WHERE cats.key=:catKey ORDER BY p.year DESC NULLS LAST"), @NamedQuery(name = "smeTest", query = "SELECT NEW edu.ucdavis.cs.dblp.data.SmeDTO(auths, COUNT(*)) FROM Publication p, IN(p.author) auths, IN(p.content.categories) cats WHERE cats.id=:catId GROUP BY auths ORDER BY COUNT(*) DESC"), @NamedQuery(name = "allSmes", query = "SELECT NEW edu.ucdavis.cs.dblp.data.SmeDTO(cats.key, auths, COUNT(*)) FROM Publication p, IN(p.author) auths, IN(p.content.categories) cats GROUP BY cats, auths HAVING COUNT(*) > 1 ORDER BY COUNT(*) DESC") })
public class Publication implements Serializable {

    public static final Logger logger = Logger.getLogger(Publication.class);

    public static final long serialVersionUID = -708123659231597900l;

    @Enumerated
    private PublicationType type;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "PUB_AUTHORS", joinColumns = @JoinColumn(name = "PUB_ID", referencedColumnName = "DBLP_KEY"), inverseJoinColumns = @JoinColumn(name = "AUTHOR_ID", referencedColumnName = "AUTHOR_NAME"))
    protected Set<Author> author;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    protected PublicationContent content;

    protected String editor;

    @Column(length = 4000)
    protected String title;

    protected String booktitle;

    protected String pages;

    @Column(name = "PUB_YEAR")
    protected String year;

    protected String address;

    @ManyToOne(cascade = CascadeType.ALL)
    protected Journal journal;

    protected String volume;

    protected String number;

    protected String month;

    protected String url;

    @Column(name = "ELECTRONIC_EDITION")
    protected String ee;

    @Column(name = "CDROM_PATH")
    protected String cdrom;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "PUB_CITES", joinColumns = @JoinColumn(name = "PUB_ID", referencedColumnName = "DBLP_KEY"), inverseJoinColumns = @JoinColumn(name = "CITE_ID", referencedColumnName = "CITE_VALUE"))
    protected Set<Cite> cite;

    @ManyToOne(cascade = CascadeType.ALL)
    protected Publisher publisher;

    protected String note;

    protected String crossref;

    protected String isbn;

    protected String series;

    @ManyToOne(cascade = CascadeType.ALL)
    protected School school;

    protected String chapter;

    @Id
    @XmlAttribute(required = true)
    @Column(name = "DBLP_KEY")
    protected String key;

    @XmlAttribute
    @Column(name = "MOD_DATE")
    protected String mdate;

    public Publication() {
        author = new HashSet<Author>();
        cite = new HashSet<Cite>();
    }

    public static final Publication convert(Object obj) {
        Publication pub = new Publication();
        pub.type = PublicationType.typeOf(obj);
        if (obj instanceof Article) {
            Article pubToConvert = (Article) obj;
            pub.author = new HashSet<Author>(pubToConvert.getAuthor());
            pub.editor = pubToConvert.getEditor() != null ? pubToConvert.getEditor().getContent() : null;
            if (null != pubToConvert.getTitle() && pubToConvert.getTitle().getContent().size() > 0) {
                pub.title = StringUtils.join(pubToConvert.getTitle().getContent(), ' ');
                logger.debug("title=" + pub.title);
            }
            pub.booktitle = pubToConvert.getBooktitle() != null ? pubToConvert.getBooktitle().getContent() : null;
            pub.pages = pubToConvert.getPages() != null ? pubToConvert.getPages().getContent() : null;
            pub.year = pubToConvert.getYear() != null ? pubToConvert.getYear().getContent() : null;
            pub.address = pubToConvert.getAddress() != null ? pubToConvert.getAddress().getContent() : null;
            pub.journal = pubToConvert.getJournal();
            pub.volume = pubToConvert.getVolume() != null ? pubToConvert.getVolume().getContent() : null;
            pub.number = pubToConvert.getNumber() != null ? pubToConvert.getNumber().getContent() : null;
            pub.month = pubToConvert.getMonth() != null ? pubToConvert.getMonth().getContent() : null;
            pub.url = pubToConvert.getUrl() != null ? pubToConvert.getUrl().getContent() : null;
            pub.ee = pubToConvert.getEe() != null ? pubToConvert.getEe().getContent() : null;
            pub.cdrom = pubToConvert.getCdrom() != null ? pubToConvert.getCdrom().getContent() : null;
            pub.cite = new HashSet<Cite>(pubToConvert.getCite());
            pub.publisher = pubToConvert.getPublisher();
            pub.note = pubToConvert.getNote() != null ? pubToConvert.getNote().getContent() : null;
            pub.crossref = pubToConvert.getCrossref() != null ? pubToConvert.getCrossref().getContent() : null;
            pub.isbn = pubToConvert.getIsbn() != null ? pubToConvert.getIsbn().getContent() : null;
            pub.series = pubToConvert.getSeries() != null ? pubToConvert.getSeries().getContent() : null;
            pub.school = pubToConvert.getSchool();
            pub.chapter = pubToConvert.getChapter() != null ? pubToConvert.getChapter().getContent() : null;
            pub.key = pubToConvert.getKey();
            pub.mdate = pubToConvert.getMdate();
        } else if (obj instanceof Inproceedings) {
            Inproceedings pubToConvert = (Inproceedings) obj;
            pub.author = new HashSet<Author>(pubToConvert.getAuthor());
            pub.editor = pubToConvert.getEditor() != null ? pubToConvert.getEditor().getContent() : null;
            if (null != pubToConvert.getTitle() && pubToConvert.getTitle().getContent().size() > 0) {
                pub.title = StringUtils.join(pubToConvert.getTitle().getContent(), ' ');
                logger.debug("title=" + pub.title);
            }
            pub.booktitle = pubToConvert.getBooktitle() != null ? pubToConvert.getBooktitle().getContent() : null;
            pub.pages = pubToConvert.getPages() != null ? pubToConvert.getPages().getContent() : null;
            pub.year = pubToConvert.getYear() != null ? pubToConvert.getYear().getContent() : null;
            pub.address = pubToConvert.getAddress() != null ? pubToConvert.getAddress().getContent() : null;
            pub.journal = pubToConvert.getJournal();
            pub.volume = pubToConvert.getVolume() != null ? pubToConvert.getVolume().getContent() : null;
            pub.number = pubToConvert.getNumber() != null ? pubToConvert.getNumber().getContent() : null;
            pub.month = pubToConvert.getMonth() != null ? pubToConvert.getMonth().getContent() : null;
            pub.url = pubToConvert.getUrl() != null ? pubToConvert.getUrl().getContent() : null;
            pub.ee = pubToConvert.getEe() != null ? pubToConvert.getEe().getContent() : null;
            pub.cdrom = pubToConvert.getCdrom() != null ? pubToConvert.getCdrom().getContent() : null;
            pub.cite = new HashSet<Cite>(pubToConvert.getCite());
            pub.publisher = pubToConvert.getPublisher();
            pub.note = pubToConvert.getNote() != null ? pubToConvert.getNote().getContent() : null;
            pub.crossref = pubToConvert.getCrossref() != null ? pubToConvert.getCrossref().getContent() : null;
            pub.isbn = pubToConvert.getIsbn() != null ? pubToConvert.getIsbn().getContent() : null;
            pub.series = pubToConvert.getSeries() != null ? pubToConvert.getSeries().getContent() : null;
            pub.school = pubToConvert.getSchool();
            pub.chapter = pubToConvert.getChapter() != null ? pubToConvert.getChapter().getContent() : null;
            pub.key = pubToConvert.getKey();
            pub.mdate = pubToConvert.getMdate();
        } else if (obj instanceof Proceedings) {
            Proceedings pubToConvert = (Proceedings) obj;
            pub.author = new HashSet<Author>(pubToConvert.getAuthor());
            pub.editor = pubToConvert.getEditor() != null ? pubToConvert.getEditor().getContent() : null;
            if (null != pubToConvert.getTitle() && pubToConvert.getTitle().getContent().size() > 0) {
                pub.title = StringUtils.join(pubToConvert.getTitle().getContent(), ' ');
                logger.debug("title=" + pub.title);
            }
            pub.booktitle = pubToConvert.getBooktitle() != null ? pubToConvert.getBooktitle().getContent() : null;
            pub.pages = pubToConvert.getPages() != null ? pubToConvert.getPages().getContent() : null;
            pub.year = pubToConvert.getYear() != null ? pubToConvert.getYear().getContent() : null;
            pub.address = pubToConvert.getAddress() != null ? pubToConvert.getAddress().getContent() : null;
            pub.journal = pubToConvert.getJournal();
            pub.volume = pubToConvert.getVolume() != null ? pubToConvert.getVolume().getContent() : null;
            pub.number = pubToConvert.getNumber() != null ? pubToConvert.getNumber().getContent() : null;
            pub.month = pubToConvert.getMonth() != null ? pubToConvert.getMonth().getContent() : null;
            pub.url = pubToConvert.getUrl() != null ? pubToConvert.getUrl().getContent() : null;
            pub.ee = pubToConvert.getEe() != null ? pubToConvert.getEe().getContent() : null;
            pub.cdrom = pubToConvert.getCdrom() != null ? pubToConvert.getCdrom().getContent() : null;
            pub.cite = new HashSet<Cite>(pubToConvert.getCite());
            pub.publisher = pubToConvert.getPublisher();
            pub.note = pubToConvert.getNote() != null ? pubToConvert.getNote().getContent() : null;
            pub.crossref = pubToConvert.getCrossref() != null ? pubToConvert.getCrossref().getContent() : null;
            pub.isbn = pubToConvert.getIsbn() != null ? pubToConvert.getIsbn().getContent() : null;
            pub.series = pubToConvert.getSeries() != null ? pubToConvert.getSeries().getContent() : null;
            pub.school = pubToConvert.getSchool();
            pub.chapter = pubToConvert.getChapter() != null ? pubToConvert.getChapter().getContent() : null;
            pub.key = pubToConvert.getKey();
            pub.mdate = pubToConvert.getMdate();
        } else if (obj instanceof Book) {
            Book pubToConvert = (Book) obj;
            pub.author = new HashSet<Author>(pubToConvert.getAuthor());
            pub.editor = pubToConvert.getEditor() != null ? pubToConvert.getEditor().getContent() : null;
            if (null != pubToConvert.getTitle() && pubToConvert.getTitle().getContent().size() > 0) {
                pub.title = StringUtils.join(pubToConvert.getTitle().getContent(), ' ');
                logger.debug("title=" + pub.title);
            }
            pub.booktitle = pubToConvert.getBooktitle() != null ? pubToConvert.getBooktitle().getContent() : null;
            pub.pages = pubToConvert.getPages() != null ? pubToConvert.getPages().getContent() : null;
            pub.year = pubToConvert.getYear() != null ? pubToConvert.getYear().getContent() : null;
            pub.address = pubToConvert.getAddress() != null ? pubToConvert.getAddress().getContent() : null;
            pub.journal = pubToConvert.getJournal();
            pub.volume = pubToConvert.getVolume() != null ? pubToConvert.getVolume().getContent() : null;
            pub.number = pubToConvert.getNumber() != null ? pubToConvert.getNumber().getContent() : null;
            pub.month = pubToConvert.getMonth() != null ? pubToConvert.getMonth().getContent() : null;
            pub.url = pubToConvert.getUrl() != null ? pubToConvert.getUrl().getContent() : null;
            pub.ee = pubToConvert.getEe() != null ? pubToConvert.getEe().getContent() : null;
            pub.cdrom = pubToConvert.getCdrom() != null ? pubToConvert.getCdrom().getContent() : null;
            pub.cite = new HashSet<Cite>(pubToConvert.getCite());
            pub.publisher = pubToConvert.getPublisher();
            pub.note = pubToConvert.getNote() != null ? pubToConvert.getNote().getContent() : null;
            pub.crossref = pubToConvert.getCrossref() != null ? pubToConvert.getCrossref().getContent() : null;
            pub.isbn = pubToConvert.getIsbn() != null ? pubToConvert.getIsbn().getContent() : null;
            pub.series = pubToConvert.getSeries() != null ? pubToConvert.getSeries().getContent() : null;
            pub.school = pubToConvert.getSchool();
            pub.chapter = pubToConvert.getChapter() != null ? pubToConvert.getChapter().getContent() : null;
            pub.key = pubToConvert.getKey();
            pub.mdate = pubToConvert.getMdate();
        } else if (obj instanceof Incollection) {
            Incollection pubToConvert = (Incollection) obj;
            pub.author = new HashSet<Author>(pubToConvert.getAuthor());
            pub.editor = pubToConvert.getEditor() != null ? pubToConvert.getEditor().getContent() : null;
            if (null != pubToConvert.getTitle() && pubToConvert.getTitle().getContent().size() > 0) {
                pub.title = StringUtils.join(pubToConvert.getTitle().getContent(), ' ');
                logger.debug("title=" + pub.title);
            }
            pub.booktitle = pubToConvert.getBooktitle() != null ? pubToConvert.getBooktitle().getContent() : null;
            pub.pages = pubToConvert.getPages() != null ? pubToConvert.getPages().getContent() : null;
            pub.year = pubToConvert.getYear() != null ? pubToConvert.getYear().getContent() : null;
            pub.address = pubToConvert.getAddress() != null ? pubToConvert.getAddress().getContent() : null;
            pub.journal = pubToConvert.getJournal();
            pub.volume = pubToConvert.getVolume() != null ? pubToConvert.getVolume().getContent() : null;
            pub.number = pubToConvert.getNumber() != null ? pubToConvert.getNumber().getContent() : null;
            pub.month = pubToConvert.getMonth() != null ? pubToConvert.getMonth().getContent() : null;
            pub.url = pubToConvert.getUrl() != null ? pubToConvert.getUrl().getContent() : null;
            pub.ee = pubToConvert.getEe() != null ? pubToConvert.getEe().getContent() : null;
            pub.cdrom = pubToConvert.getCdrom() != null ? pubToConvert.getCdrom().getContent() : null;
            pub.cite = new HashSet<Cite>(pubToConvert.getCite());
            pub.publisher = pubToConvert.getPublisher();
            pub.note = pubToConvert.getNote() != null ? pubToConvert.getNote().getContent() : null;
            pub.crossref = pubToConvert.getCrossref() != null ? pubToConvert.getCrossref().getContent() : null;
            pub.isbn = pubToConvert.getIsbn() != null ? pubToConvert.getIsbn().getContent() : null;
            pub.series = pubToConvert.getSeries() != null ? pubToConvert.getSeries().getContent() : null;
            pub.school = pubToConvert.getSchool();
            pub.chapter = pubToConvert.getChapter() != null ? pubToConvert.getChapter().getContent() : null;
            pub.key = pubToConvert.getKey();
            pub.mdate = pubToConvert.getMdate();
        } else if (obj instanceof Phdthesis) {
            Phdthesis pubToConvert = (Phdthesis) obj;
            pub.author = new HashSet<Author>(pubToConvert.getAuthor());
            pub.editor = pubToConvert.getEditor() != null ? pubToConvert.getEditor().getContent() : null;
            if (null != pubToConvert.getTitle() && pubToConvert.getTitle().getContent().size() > 0) {
                pub.title = StringUtils.join(pubToConvert.getTitle().getContent(), ' ');
                logger.debug("title=" + pub.title);
            }
            pub.booktitle = pubToConvert.getBooktitle() != null ? pubToConvert.getBooktitle().getContent() : null;
            pub.pages = pubToConvert.getPages() != null ? pubToConvert.getPages().getContent() : null;
            pub.year = pubToConvert.getYear() != null ? pubToConvert.getYear().getContent() : null;
            pub.address = pubToConvert.getAddress() != null ? pubToConvert.getAddress().getContent() : null;
            pub.journal = pubToConvert.getJournal();
            pub.volume = pubToConvert.getVolume() != null ? pubToConvert.getVolume().getContent() : null;
            pub.number = pubToConvert.getNumber() != null ? pubToConvert.getNumber().getContent() : null;
            pub.month = pubToConvert.getMonth() != null ? pubToConvert.getMonth().getContent() : null;
            pub.url = pubToConvert.getUrl() != null ? pubToConvert.getUrl().getContent() : null;
            pub.ee = pubToConvert.getEe() != null ? pubToConvert.getEe().getContent() : null;
            pub.cdrom = pubToConvert.getCdrom() != null ? pubToConvert.getCdrom().getContent() : null;
            pub.cite = new HashSet<Cite>(pubToConvert.getCite());
            pub.publisher = pubToConvert.getPublisher();
            pub.note = pubToConvert.getNote() != null ? pubToConvert.getNote().getContent() : null;
            pub.crossref = pubToConvert.getCrossref() != null ? pubToConvert.getCrossref().getContent() : null;
            pub.isbn = pubToConvert.getIsbn() != null ? pubToConvert.getIsbn().getContent() : null;
            pub.series = pubToConvert.getSeries() != null ? pubToConvert.getSeries().getContent() : null;
            pub.school = pubToConvert.getSchool();
            pub.chapter = pubToConvert.getChapter() != null ? pubToConvert.getChapter().getContent() : null;
            pub.key = pubToConvert.getKey();
            pub.mdate = pubToConvert.getMdate();
        } else if (obj instanceof Mastersthesis) {
            Mastersthesis pubToConvert = (Mastersthesis) obj;
            pub.author = new HashSet<Author>(pubToConvert.getAuthor());
            pub.editor = pubToConvert.getEditor() != null ? pubToConvert.getEditor().getContent() : null;
            if (null != pubToConvert.getTitle() && pubToConvert.getTitle().getContent().size() > 0) {
                pub.title = StringUtils.join(pubToConvert.getTitle().getContent(), ' ');
                logger.debug("title=" + pub.title);
            }
            pub.booktitle = pubToConvert.getBooktitle() != null ? pubToConvert.getBooktitle().getContent() : null;
            pub.pages = pubToConvert.getPages() != null ? pubToConvert.getPages().getContent() : null;
            pub.year = pubToConvert.getYear() != null ? pubToConvert.getYear().getContent() : null;
            pub.address = pubToConvert.getAddress() != null ? pubToConvert.getAddress().getContent() : null;
            pub.journal = pubToConvert.getJournal();
            pub.volume = pubToConvert.getVolume() != null ? pubToConvert.getVolume().getContent() : null;
            pub.number = pubToConvert.getNumber() != null ? pubToConvert.getNumber().getContent() : null;
            pub.month = pubToConvert.getMonth() != null ? pubToConvert.getMonth().getContent() : null;
            pub.url = pubToConvert.getUrl() != null ? pubToConvert.getUrl().getContent() : null;
            pub.ee = pubToConvert.getEe() != null ? pubToConvert.getEe().getContent() : null;
            pub.cdrom = pubToConvert.getCdrom() != null ? pubToConvert.getCdrom().getContent() : null;
            pub.cite = new HashSet<Cite>(pubToConvert.getCite());
            pub.publisher = pubToConvert.getPublisher();
            pub.note = pubToConvert.getNote() != null ? pubToConvert.getNote().getContent() : null;
            pub.crossref = pubToConvert.getCrossref() != null ? pubToConvert.getCrossref().getContent() : null;
            pub.isbn = pubToConvert.getIsbn() != null ? pubToConvert.getIsbn().getContent() : null;
            pub.series = pubToConvert.getSeries() != null ? pubToConvert.getSeries().getContent() : null;
            pub.school = pubToConvert.getSchool();
            pub.chapter = pubToConvert.getChapter() != null ? pubToConvert.getChapter().getContent() : null;
            pub.key = pubToConvert.getKey();
            pub.mdate = pubToConvert.getMdate();
        } else if (obj instanceof Www) {
            Www pubToConvert = (Www) obj;
            pub.author = new HashSet<Author>(pubToConvert.getAuthor());
            pub.editor = pubToConvert.getEditor() != null ? pubToConvert.getEditor().getContent() : null;
            if (null != pubToConvert.getTitle() && pubToConvert.getTitle().getContent().size() > 0) {
                pub.title = StringUtils.join(pubToConvert.getTitle().getContent(), ' ');
                logger.debug("title=" + pub.title);
            }
            pub.booktitle = pubToConvert.getBooktitle() != null ? pubToConvert.getBooktitle().getContent() : null;
            pub.pages = pubToConvert.getPages() != null ? pubToConvert.getPages().getContent() : null;
            pub.year = pubToConvert.getYear() != null ? pubToConvert.getYear().getContent() : null;
            pub.address = pubToConvert.getAddress() != null ? pubToConvert.getAddress().getContent() : null;
            pub.journal = pubToConvert.getJournal();
            pub.volume = pubToConvert.getVolume() != null ? pubToConvert.getVolume().getContent() : null;
            pub.number = pubToConvert.getNumber() != null ? pubToConvert.getNumber().getContent() : null;
            pub.month = pubToConvert.getMonth() != null ? pubToConvert.getMonth().getContent() : null;
            pub.url = pubToConvert.getUrl() != null ? pubToConvert.getUrl().getContent() : null;
            pub.ee = pubToConvert.getEe() != null ? pubToConvert.getEe().getContent() : null;
            pub.cdrom = pubToConvert.getCdrom() != null ? pubToConvert.getCdrom().getContent() : null;
            pub.cite = new HashSet<Cite>(pubToConvert.getCite());
            pub.publisher = pubToConvert.getPublisher();
            pub.note = pubToConvert.getNote() != null ? pubToConvert.getNote().getContent() : null;
            pub.crossref = pubToConvert.getCrossref() != null ? pubToConvert.getCrossref().getContent() : null;
            pub.isbn = pubToConvert.getIsbn() != null ? pubToConvert.getIsbn().getContent() : null;
            pub.series = pubToConvert.getSeries() != null ? pubToConvert.getSeries().getContent() : null;
            pub.school = pubToConvert.getSchool();
            pub.chapter = pubToConvert.getChapter() != null ? pubToConvert.getChapter().getContent() : null;
            pub.key = pubToConvert.getKey();
            pub.mdate = pubToConvert.getMdate();
        } else {
            throw new IllegalArgumentException("invalid Object type to convert to " + "a Publication: " + obj);
        }
        return pub;
    }

    /**
	 * @return the type
	 */
    public PublicationType getType() {
        return type;
    }

    /**
	 * @param type the type to set
	 */
    public void setType(PublicationType type) {
        this.type = type;
    }

    /**
	 * @return the author
	 */
    public Set<Author> getAuthor() {
        return author;
    }

    /**
	 * @param author the author to set
	 */
    public void setAuthor(Set<Author> author) {
        this.author = author;
    }

    /**
	 * @return the editor
	 */
    public String getEditor() {
        return editor;
    }

    /**
	 * @param editor the editor to set
	 */
    public void setEditor(String editor) {
        this.editor = editor;
    }

    /**
	 * @return the title
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * @param title the title to set
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * @return the booktitle
	 */
    public String getBooktitle() {
        return booktitle;
    }

    /**
	 * @param booktitle the booktitle to set
	 */
    public void setBooktitle(String booktitle) {
        this.booktitle = booktitle;
    }

    /**
	 * @return the pages
	 */
    public String getPages() {
        return pages;
    }

    /**
	 * @param pages the pages to set
	 */
    public void setPages(String pages) {
        this.pages = pages;
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

    /**
	 * @return the address
	 */
    public String getAddress() {
        return address;
    }

    /**
	 * @param address the address to set
	 */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
	 * @return the journal
	 */
    public Journal getJournal() {
        return journal;
    }

    /**
	 * @param journal the journal to set
	 */
    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    /**
	 * @return the volume
	 */
    public String getVolume() {
        return volume;
    }

    /**
	 * @param volume the volume to set
	 */
    public void setVolume(String volume) {
        this.volume = volume;
    }

    /**
	 * @return the number
	 */
    public String getNumber() {
        return number;
    }

    /**
	 * @param number the number to set
	 */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
	 * @return the month
	 */
    public String getMonth() {
        return month;
    }

    /**
	 * @param month the month to set
	 */
    public void setMonth(String month) {
        this.month = month;
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
	 * @return the cdrom
	 */
    public String getCdrom() {
        return cdrom;
    }

    /**
	 * @param cdrom the cdrom to set
	 */
    public void setCdrom(String cdrom) {
        this.cdrom = cdrom;
    }

    /**
	 * @return the cite
	 */
    public Set<Cite> getCite() {
        return cite;
    }

    /**
	 * @param cite the cite to set
	 */
    public void setCite(Set<Cite> cite) {
        this.cite = cite;
    }

    /**
	 * @return the publisher
	 */
    public Publisher getPublisher() {
        return publisher;
    }

    /**
	 * @param publisher the publisher to set
	 */
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    /**
	 * @return the note
	 */
    public String getNote() {
        return note;
    }

    /**
	 * @param note the note to set
	 */
    public void setNote(String note) {
        this.note = note;
    }

    /**
	 * @return the crossref
	 */
    public String getCrossref() {
        return crossref;
    }

    /**
	 * @param crossref the crossref to set
	 */
    public void setCrossref(String crossref) {
        this.crossref = crossref;
    }

    /**
	 * @return the isbn
	 */
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
	 * @return the series
	 */
    public String getSeries() {
        return series;
    }

    /**
	 * @param series the series to set
	 */
    public void setSeries(String series) {
        this.series = series;
    }

    /**
	 * @return the school
	 */
    public School getSchool() {
        return school;
    }

    /**
	 * @param school the school to set
	 */
    public void setSchool(School school) {
        this.school = school;
    }

    /**
	 * @return the chapter
	 */
    public String getChapter() {
        return chapter;
    }

    /**
	 * @param chapter the chapter to set
	 */
    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    /**
	 * @return the key
	 */
    public String getKey() {
        return key;
    }

    /**
	 * @param key the key to set
	 */
    public void setKey(String key) {
        this.key = key;
    }

    /**
	 * @return the mdate
	 */
    public String getMdate() {
        return mdate;
    }

    /**
	 * @param mdate the mdate to set
	 */
    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    /**
	 * @return the content
	 */
    public PublicationContent getContent() {
        return content;
    }

    /**
	 * @param content the content to set
	 */
    public void setContent(PublicationContent content) {
        this.content = content;
    }

    public String getCitationString() {
        StringBuilder str = new StringBuilder();
        assert this.getType() != null : "type must be initialized for a Publication to be valid";
        switch(this.getType()) {
            case ARTICLE:
                if (this.getJournal() != null) {
                    str.append(this.getJournal().getContent());
                }
                if (this.getVolume() != null) {
                    str.append(' ' + this.getVolume());
                }
                if (this.getNumber() != null) {
                    str.append('(' + this.getNumber() + ')');
                }
                if (this.getPages() != null) {
                    str.append(':' + this.getPages());
                }
                if (this.getYear() != null) {
                    str.append(" (" + this.getYear() + ')');
                }
                break;
            case INPROCEEDINGS:
                if (this.getBooktitle() != null) {
                    str.append(this.getBooktitle());
                }
                if (this.getYear() != null) {
                    str.append(' ' + this.getYear());
                }
                if (this.getPages() != null) {
                    str.append(':' + this.getPages());
                }
                break;
            case BOOK:
                if (this.getPublisher() != null) {
                    str.append(' ' + this.getPublisher().getContent());
                }
                if (this.getYear() != null) {
                    str.append(' ' + this.getYear());
                }
                break;
        }
        return str.toString();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("Title", this.title).append("PubYear", this.year).append("Authors", this.author).append("PubContent", this.content).toString();
    }

    public static final Function<Publication, String> FN_PUB_KEY = new Function<Publication, String>() {

        @Override
        public String apply(Publication pub) {
            return pub.getKey();
        }
    };

    public static final Function<Publication, String> FN_PUB_YEAR = new Function<Publication, String>() {

        @Override
        public String apply(Publication pub) {
            return pub.getYear();
        }
    };

    public static final Function<Publication, Iterable<String>> FN_PUB_KEYWORDS = new Function<Publication, Iterable<String>>() {

        @Override
        public Iterable<String> apply(Publication pub) {
            return Iterables.transform(pub.getContent().getKeywords(), new Function<Keyword, String>() {

                @Override
                public String apply(Keyword keyword) {
                    return keyword.getKeyword();
                }
            });
        }
    };

    public static final Function<Publication, Iterable<Keyword>> FN_PUB_KEYWORDOBJS = new Function<Publication, Iterable<Keyword>>() {

        @Override
        public Iterable<Keyword> apply(Publication pub) {
            return pub.getContent().getKeywords();
        }
    };

    public static final Predicate<Publication> PRED_HAS_CONTENT = new Predicate<Publication>() {

        @Override
        public boolean apply(Publication pub) {
            return pub.getContent() != null;
        }
    };

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Publication other = (Publication) obj;
        if (key == null) {
            if (other.key != null) return false;
        } else if (!key.equals(other.key)) return false;
        return true;
    }
}
