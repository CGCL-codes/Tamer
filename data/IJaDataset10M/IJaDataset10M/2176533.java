package edu.univalle.lingweb.persistence;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AbstractToPublication entity provides the base persistence definition of the
 * ToPublication entity.
 * 
 * @author LingWeb
 */
@MappedSuperclass
public abstract class AbstractToPublication implements java.io.Serializable {

    private Long publicationId;

    private MaUser maUser;

    private CoUnit coUnit;

    private ToThemes toThemes;

    private String title;

    private String description;

    private Date createDate;

    private Set<CoMaterial> coMaterials = new HashSet<CoMaterial>(0);

    private Set<ToComment> toComments = new HashSet<ToComment>(0);

    /** default constructor */
    public AbstractToPublication() {
    }

    /** minimal constructor */
    public AbstractToPublication(Long publicationId, MaUser maUser, String title, String description, Date createDate) {
        this.publicationId = publicationId;
        this.maUser = maUser;
        this.title = title;
        this.description = description;
        this.createDate = createDate;
    }

    /** full constructor */
    public AbstractToPublication(Long publicationId, MaUser maUser, CoUnit coUnit, ToThemes toThemes, String title, String description, Date createDate, Set<CoMaterial> coMaterials, Set<ToComment> toComments) {
        this.publicationId = publicationId;
        this.maUser = maUser;
        this.coUnit = coUnit;
        this.toThemes = toThemes;
        this.title = title;
        this.description = description;
        this.createDate = createDate;
        this.coMaterials = coMaterials;
        this.toComments = toComments;
    }

    @Id
    @Column(name = "publication_id", unique = true, nullable = false, insertable = true, updatable = true, precision = 15, scale = 0)
    public Long getPublicationId() {
        return this.publicationId;
    }

    public void setPublicationId(Long publicationId) {
        this.publicationId = publicationId;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = false, nullable = false, insertable = true, updatable = true)
    public MaUser getMaUser() {
        return this.maUser;
    }

    public void setMaUser(MaUser maUser) {
        this.maUser = maUser;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", unique = false, nullable = true, insertable = true, updatable = true)
    public CoUnit getCoUnit() {
        return this.coUnit;
    }

    public void setCoUnit(CoUnit coUnit) {
        this.coUnit = coUnit;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id", unique = false, nullable = true, insertable = true, updatable = true)
    public ToThemes getToThemes() {
        return this.toThemes;
    }

    public void setToThemes(ToThemes toThemes) {
        this.toThemes = toThemes;
    }

    @Column(name = "title", unique = false, nullable = false, insertable = true, updatable = true, length = 60)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "description", unique = false, nullable = false, insertable = true, updatable = true, length = 1000)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "create_date", unique = false, nullable = false, insertable = true, updatable = true, length = 29)
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(name = "to_publication_material", schema = "public", joinColumns = { @JoinColumn(name = "publication_id", unique = false, nullable = false, insertable = true, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "material_id", unique = false, nullable = false, insertable = true, updatable = false) })
    public Set<CoMaterial> getCoMaterials() {
        return this.coMaterials;
    }

    public void setCoMaterials(Set<CoMaterial> coMaterials) {
        this.coMaterials = coMaterials;
    }

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(name = "to_publication_comment", schema = "public", joinColumns = { @JoinColumn(name = "publication_id", unique = false, nullable = false, insertable = true, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "comment_id", unique = false, nullable = false, insertable = true, updatable = false) })
    public Set<ToComment> getToComments() {
        return this.toComments;
    }

    public void setToComments(Set<ToComment> toComments) {
        this.toComments = toComments;
    }
}
