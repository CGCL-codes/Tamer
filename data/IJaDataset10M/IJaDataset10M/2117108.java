package de.fau.cs.dosis.drug.model;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import de.fau.cs.dosis.account.model.User;

@Entity
@Table(name = "active_ingredient_revision")
@NamedQueries({ @NamedQuery(name = ActiveIngredientRevision.QRY_SET_REVIEWED, query = "UPDATE ActiveIngredientRevision air " + "SET air.dateReviewed = :reviewed " + "WHERE air.id = :airId"), @NamedQuery(name = ActiveIngredientRevision.QRY_MAX_REVISION_NUMBER, query = "SELECT max(aih.revisionNumber) " + " FROM ActiveIngredientRevision aih " + " WHERE aih.activeIngredient.guid = :guid"), @NamedQuery(name = ActiveIngredientRevision.QRY_ALL_UNREVIEWED, query = "SELECT air FROM ActiveIngredientRevision air WHERE air.dateReviewed IS NULL AND " + ActiveIngredientRevision.COLUMN_STATUS + " = :airStatus"), @NamedQuery(name = ActiveIngredientRevision.QRY_ALL_REVIEWED, query = "SELECT air FROM ActiveIngredientRevision air WHERE air.dateReviewed IS NOT NULL AND " + ActiveIngredientRevision.COLUMN_STATUS + " = :airStatus"), @NamedQuery(name = ActiveIngredientRevision.QRY_GET_BY_ID, query = "SELECT air FROM ActiveIngredientRevision air WHERE air.id = :airId"), @NamedQuery(name = ActiveIngredientRevision.QRY_GET_BY_GUID, query = "SELECT air FROM ActiveIngredientRevision air WHERE air.guid = :guid"), @NamedQuery(name = ActiveIngredientRevision.QRY_COUNT, query = "SELECT count(air) FROM ActiveIngredientRevision air") })
public class ActiveIngredientRevision implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String QRY_MAX_REVISION_NUMBER = "ActiveIngredientRevision.getMaxRevisionNumber";

    public static final String QRY_GET_BY_ID = "ActiveIngredientRevision.getById";

    public static final String QRY_GET_BY_GUID = "ActiveIngredientRevision.getByGuid";

    public static final String QRY_SET_REVIEWED = "ActiveIngredientRevision.setReviewed";

    public static final String QRY_ALL_UNREVIEWED = "ActiveIngredientRevision.getAllUnreviewed";

    public static final String QRY_ALL_REVIEWED = "ActiveIngredientRevision.getAllReviewed";

    public static final String QRY_COUNT = "ActiveIngredientRevision.count";

    protected static final String COLUMN_BRANDNAME = "brand_name";

    protected static final String COLUMN_REVIEW = "date_reviewed";

    protected static final String COLUMN_STATUS = "status";

    protected static final String COLUMN_ACTIVE_INGREDIENT = "name";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "guid", nullable = false, unique = true, updatable = false)
    private String guid;

    @Column(name = "dosage")
    @Lob
    private String dosage;

    @Column(name = "application")
    @Lob
    private String application;

    @Column(name = "effect_spectrum")
    @Lob
    private String effectSpectrum;

    @Column(name = "side_effects")
    @Lob
    private String sideEffects;

    @Column(name = "interaction_effects")
    @Lob
    private String interactionEffects;

    @Column(name = "contra_indications")
    @Lob
    private String contraIndications;

    @Column(name = "pharmaco_dynamics")
    @Lob
    private String pharmacoDynamics;

    @Column(name = "comment")
    @Lob
    private String comment;

    @Column(name = "literature_references")
    @Lob
    private String references;

    @ManyToOne(optional = false)
    @JoinColumn(name = "active_ingredient", updatable = false)
    private ActiveIngredient activeIngredient;

    @Basic(fetch = FetchType.EAGER)
    @ManyToOne(optional = true)
    private User owner;

    @Column(name = "owner_guid")
    private String ownerGuid;

    @Column(name = "owner_name")
    private String ownerName;

    @Basic(fetch = FetchType.EAGER)
    @ManyToOne
    private User userReviewed;

    @Column(name = "previous_revision_number")
    private Integer previousRevisionNumber;

    @Column(name = "revision_number", nullable = false)
    private Integer revisionNumber;

    @Column(name = COLUMN_STATUS, nullable = false)
    @Enumerated(EnumType.STRING)
    private RevisionStatus status = RevisionStatus.AWAITING;

    @Column(name = "date_created", nullable = false)
    private GregorianCalendar dateCreated;

    @Column(name = ActiveIngredientRevision.COLUMN_REVIEW)
    private Date dateReviewed = null;

    public ActiveIngredientRevision() {
    }

    @Override
    public String toString() {
        return "ActiveIngredientRevision: " + getId() + ":" + getRevisionNumber();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getEffectSpectrum() {
        return effectSpectrum;
    }

    public void setEffectSpectrum(String effectSpectrum) {
        this.effectSpectrum = effectSpectrum;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public String getInteractionEffects() {
        return interactionEffects;
    }

    public void setInteractionEffects(String interactionEffects) {
        this.interactionEffects = interactionEffects;
    }

    public String getContraIndications() {
        return contraIndications;
    }

    public void setContraIndications(String contraIndications) {
        this.contraIndications = contraIndications;
    }

    public String getPharmacoDynamics() {
        return pharmacoDynamics;
    }

    public void setPharmacoDynamics(String pharmacoDynamics) {
        this.pharmacoDynamics = pharmacoDynamics;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public ActiveIngredient getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(ActiveIngredient activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getUserReviewed() {
        return userReviewed;
    }

    public void setUserReviewed(User userReviewed) {
        this.userReviewed = userReviewed;
    }

    public Integer getPreviousRevisionNumber() {
        return previousRevisionNumber;
    }

    public void setPreviousRevisionNumber(Integer previousRevisionNumber) {
        this.previousRevisionNumber = previousRevisionNumber;
    }

    public Integer getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(Integer revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public RevisionStatus getStatus() {
        return status;
    }

    public void setStatus(RevisionStatus status) {
        this.status = status;
    }

    public GregorianCalendar getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(GregorianCalendar dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateReviewed() {
        return dateReviewed;
    }

    public void setDateReviewed(Date dateReviewed) {
        this.dateReviewed = dateReviewed;
    }

    public boolean isReviewed2() {
        return getDateReviewed() != null;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getGuid() {
        return guid;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerGuid(String ownerGuid) {
        this.ownerGuid = ownerGuid;
    }

    public String getOwnerGuid() {
        return ownerGuid;
    }
}
