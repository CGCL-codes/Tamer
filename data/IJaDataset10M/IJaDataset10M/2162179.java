package uk.ac.ebi.intact.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.persistence.*;

/**
 * TODO comment this!
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: CvObjectXref.java 11706 2008-07-01 09:01:16Z baranda $
 * @since <pre>21-Jul-2006</pre>
 */
@Entity
@Table(name = "ia_controlledvocab_xref", uniqueConstraints = { @UniqueConstraint(columnNames = { "parent_ac", "qualifier_ac", "database_ac", "primaryid" }) })
public class CvObjectXref extends Xref {

    private static final Log log = LogFactory.getLog(CvObjectXref.class);

    public CvObjectXref() {
    }

    @PostLoad
    public void postLoad() {
        CvObject parent = (CvObject) getParent();
        if (parent != null && parent.getIdentifier() != null) {
            prepareParentMi();
        }
    }

    @PrePersist
    @PreUpdate
    public void prepareParentMi() {
        CvObject parent = (CvObject) getParent();
        if (parent == null) {
            throw new IllegalStateException("Trying to persist or update an cv xref without parent: " + this);
        }
        if (CvXrefQualifier.IDENTITY_MI_REF.equals(getCvXrefQualifier().getIdentifier())) {
            if (CvDatabase.PSI_MI_MI_REF.equals(getCvDatabase().getIdentifier())) {
                parent.setIdentifier(getPrimaryId());
            } else {
                if (parent.getIdentifier() == null) {
                    parent.setIdentifier(getPrimaryId());
                }
            }
        }
    }

    public CvObjectXref(Institution anOwner, CvDatabase aDatabase, String aPrimaryId, String aSecondaryId, String aDatabaseRelease, CvXrefQualifier aCvXrefQualifier) {
        super(anOwner, aDatabase, aPrimaryId, aSecondaryId, aDatabaseRelease, aCvXrefQualifier);
    }

    public CvObjectXref(Institution anOwner, CvDatabase aDatabase, String aPrimaryId, CvXrefQualifier aCvXrefQualifier) {
        super(anOwner, aDatabase, aPrimaryId, aCvXrefQualifier);
    }

    @ManyToOne(targetEntity = CvObject.class)
    @JoinColumn(name = "parent_ac")
    public AnnotatedObject getParent() {
        return super.getParent();
    }

    @Column(name = "parent_ac", insertable = false, updatable = false)
    public String getParentAc() {
        return super.getParentAc();
    }
}
