package tinlizard.model;

import tinlizard.dao.jpa.JpaDao;
import tinlizard.dao.jpa.Persistable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

/**
 * General collection of Codelines.
 */
@Entity(name = "View")
@Table(name = "TL_VIEW", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }) })
@NamedQueries({ @NamedQuery(name = QueryNames.VIEW_BY_NAME, query = "select o from View o where o.name = ?" + View.ORDER_BY) })
@Indexed
public final class View implements Persistable {

    static final String ORDER_BY = " order by o.name";

    private static final Class<View> CLASS = View.class;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME", nullable = false)
    @Field(index = Index.TOKENIZED, store = Store.NO)
    private String name;

    @Column(name = "CREATED", nullable = false)
    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @DateBridge(resolution = Resolution.SECOND)
    private Date created;

    @Column(name = "CREATED_BY", nullable = false)
    @Field(index = Index.TOKENIZED, store = Store.NO)
    private String createdBy;

    @Version
    @Column(name = "LAST_MODIFIED")
    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @DateBridge(resolution = Resolution.SECOND)
    private Date lastModified;

    @Column(name = "LAST_MODIFIED_BY")
    @Field(index = Index.TOKENIZED, store = Store.NO)
    private String lastModifiedBy;

    @Column(name = "DESCRIPTION")
    @Field(index = Index.TOKENIZED, store = Store.NO)
    private String description;

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Date getCreated() {
        return this.created;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Date getLastModified() {
        return this.lastModified;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setCreated(final Date created) {
        this.created = created;
    }

    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    public void setLastModified(final Date lastModified) {
        this.lastModified = lastModified;
    }

    public void setLastModifiedBy(final String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public View() {
    }

    public View(final String name, final String description) {
        setName(name);
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Collection<Codeline> getCodelines() {
        Object[] params = { this };
        return JpaDao.getInstance().findByNamedQuery(Codeline.class, QueryNames.CODELINES_ALL_FOR_VIEW, params);
    }

    public Collection<Codeline> getActiveCodelines() {
        Object[] params = { this };
        return JpaDao.getInstance().findByNamedQuery(Codeline.class, QueryNames.CODELINES_ACTIVE_FOR_VIEW, params);
    }

    public void add() {
        JpaDao.getInstance().add(this);
    }

    public void update() {
        JpaDao.getInstance().update(this);
    }

    public void index() {
        JpaDao.getInstance().index(this);
    }

    public void delete() {
        TinLizard.getInstance().getScmDao().releaseFiles(getCodelines());
        JpaDao.getInstance().delete(CLASS, getId());
    }

    public static Collection<View> findAll() {
        return JpaDao.getInstance().findAll(CLASS);
    }

    public static View findByName(final String name) {
        Object[] params = { name };
        return JpaDao.getInstance().findSingleByNamedQuery(CLASS, QueryNames.VIEW_BY_NAME, params);
    }

    public static View getDefaultView() {
        View v = View.findByName("Default");
        if (v == null) {
            v = new View("Default", "Default View.");
            v.add();
        }
        return v;
    }

    public static Collection<View> search(final String query) {
        String[] fields = { "name", "created", "createdBy", "lastModified", "lastModifiedBy", "description" };
        return JpaDao.getInstance().findByTextSearch(CLASS, query, fields);
    }

    public static void indexAll() {
        JpaDao.getInstance().indexAll(CLASS);
    }
}
