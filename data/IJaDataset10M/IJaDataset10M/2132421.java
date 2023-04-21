package org.kwantu.m2.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kwantu.m2.KwantuFaultException;
import org.kwantu.persistence.AbstractPersistentObject;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/** Define a relationship between two KwantuClasses.
 * eg Class "Project" might have the relationship "deliverables" to class "Deliverable".
 *
 * When defining a relationship you also need to specify a name for the inverse relationship because
 * the relationship and the inverse relationship are always defined together.
 *
 * The target class ("Deliverable" in the example) might have the inverse relationship
 * name "Project".
 *
 * When defining a relationship pair the first is set as the "primary" relationship.  The primary
 * relationship may be "Owning" which means that the items it refers to should cease to exist
 * when the relationship is ended.  The primary relationship may also have Cardinality "MANY"
 * instead of "ONE".  The inverse relationship may only have cardinality "ONE".
 *
 */
@Entity
public class KwantuRelationship extends AbstractPersistentObject {

    /** Logging. */
    public static final Log LOG = LogFactory.getLog(KwantuRelationship.class);

    /** to define if the relationship is one-to-one or one-to-many. */
    public enum Cardinality {

        ONE, MANY
    }

    private KwantuClass owningKwantuClass;

    private String name;

    private KwantuClass relationshipToKwantuClass;

    private Cardinality cardinality;

    private KwantuRelationship inverseKwantuRelationship;

    private boolean owner = false;

    private boolean primaryRelationship;

    private boolean showInExpertModeOnly;

    public KwantuRelationship() {
    }

    public KwantuRelationship(final KwantuClass owningKwantuClass, final String name, final KwantuClass relationshipToKwantuClass, final Cardinality cardinality, final boolean owner) {
        this.owningKwantuClass = owningKwantuClass;
        this.name = name;
        this.relationshipToKwantuClass = relationshipToKwantuClass;
        this.cardinality = cardinality;
        this.owner = owner;
    }

    @ManyToOne
    public KwantuClass getOwningKwantuClass() {
        return owningKwantuClass;
    }

    public void setOwningKwantuClass(final KwantuClass owningKwantuClass) {
        this.owningKwantuClass = owningKwantuClass;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @OneToOne
    public KwantuClass getRelationshipToKwantuClass() {
        return relationshipToKwantuClass;
    }

    public void setRelationshipToKwantuClass(final KwantuClass relationshipToKwantuClass) {
        this.relationshipToKwantuClass = relationshipToKwantuClass;
    }

    public Cardinality getCardinality() {
        return cardinality;
    }

    public void setCardinality(final Cardinality cardinality) {
        this.cardinality = cardinality;
    }

    @OneToOne
    public KwantuRelationship getInverseKwantuRelationship() {
        return inverseKwantuRelationship;
    }

    public void setInverseKwantuRelationship(final KwantuRelationship inverseKwantuRelationship) {
        this.inverseKwantuRelationship = inverseKwantuRelationship;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(final boolean owner) {
        this.owner = owner;
    }

    public boolean isPrimaryRelationship() {
        return primaryRelationship;
    }

    public void setPrimaryRelationship(boolean primaryRelationship) {
        this.primaryRelationship = primaryRelationship;
    }

    public boolean isShowInExpertModeOnly() {
        return showInExpertModeOnly;
    }

    public void setShowInExpertModeOnly(final boolean showInExpertModeOnly) {
        this.showInExpertModeOnly = showInExpertModeOnly;
    }

    /** Generate java code to maintain this relationship.
     *  eg if relationship is "project hasDeliverables" (which is one to many)
     *  we require a getter and setter to add a single deliverable
     *  and a getter and setter to set or return the entire list of deliverables.
     *
     * @return getters and setters to maintain a list of related objects.
     */
    @Transient
    public String getJavaCodeGetter() {
        String className = relationshipToKwantuClass.getName();
        String nameUpshifted = StringUtil.upshiftLeadingLetter(name);
        String deleteOrphans;
        String getter;
        if (owner) {
            deleteOrphans = "    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)\n";
        } else {
            deleteOrphans = "";
        }
        if (cardinality == Cardinality.ONE) {
            if (owner) {
                getter = "    @OneToOne(mappedBy = \"" + getInverseKwantuRelationship().getName() + "\", cascade = CascadeType.ALL)\n";
            } else {
                getter = "    @OneToOne\n";
            }
            getter = getter + "    public " + className + " get" + nameUpshifted + "() {\n" + "        return " + name + ";\n" + "    }\n\n";
        } else if (cardinality == Cardinality.MANY) {
            getter = "    @OneToMany(mappedBy = \"" + getInverseKwantuRelationship().getName() + "\", cascade = CascadeType.ALL)\n" + deleteOrphans + "    public Set<" + className + "> get" + nameUpshifted + "() {\n" + "        if (" + name + " == null) {\n" + "            " + name + " = new HashSet<" + className + ">();\n" + "        }\n" + "        return " + name + ";\n" + "    }\n";
        } else {
            throw new KwantuFaultException("Unknown Relationship Cardinality (not one or many)");
        }
        return getter;
    }

    @Transient
    public String getJavaCodeSetter() {
        String className = relationshipToKwantuClass.getName();
        String nameUpshifted = StringUtil.upshiftLeadingLetter(name);
        String setter;
        if (cardinality == Cardinality.ONE) {
            setter = "    public void set" + nameUpshifted + "(" + className + " " + name + ") {\n" + "        this." + name + " = " + name + ";\n" + "    }\n";
        } else if (cardinality == Cardinality.MANY) {
            setter = "    public void set" + nameUpshifted + "(final Set<" + className + "> " + name + ") {\n" + "        this." + name + " = " + name + ";\n" + "    }\n";
        } else {
            throw new KwantuFaultException("Unknown Relationship Cardinality (not one or many)");
        }
        return setter;
    }

    @Transient
    public String getJavaCodeDefinition() {
        String className = relationshipToKwantuClass.getName();
        String s;
        if (cardinality == Cardinality.ONE) {
            s = "    private " + className + " " + name + ";\n";
        } else if (cardinality == Cardinality.MANY) {
            s = "    private Set<" + className + "> " + name + ";\n";
        } else {
            throw new KwantuFaultException("Unknown Relationship Cardinality (not one or many)");
        }
        return s;
    }

    /**
     *Generate utility methods for maintaining relationships (which are not owning relationships).
     *
     * Add/Remove methods for maintaining one to many relationships
     * and Assign/Clear for maintaining one-to-one relationships.
     *
     * @return generated java source code as a string.
     */
    @Transient
    public void getJavaHelperMethods(StringBuilder sb) {
        String relNameUpshifted = StringUtil.upshiftLeadingLetter(getName());
        String inverseRelNameUpshifted = StringUtil.upshiftLeadingLetter(getInverseKwantuRelationship().getName());
        String toClassName = getRelationshipToKwantuClass().getName();
        if (cardinality == Cardinality.MANY) {
            sb.append("    public void add" + relNameUpshifted + "(" + toClassName + "... items) {\n");
            sb.append("        for (" + toClassName + " item : items) {\n");
            sb.append("            item.set" + inverseRelNameUpshifted + "(this);\n");
            sb.append("            get" + relNameUpshifted + "().add(item);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
            sb.append("    public void remove" + relNameUpshifted + "(" + toClassName + "... items) {\n");
            sb.append("        for (" + toClassName + " item : items) {\n");
            sb.append("            item.set" + inverseRelNameUpshifted + "(null);\n");
            sb.append("            get" + relNameUpshifted + "().remove(item);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        } else if (cardinality == Cardinality.ONE) {
            sb.append("    public void assign" + relNameUpshifted + "(" + toClassName + " item) {\n");
            sb.append("        item.set" + inverseRelNameUpshifted + "(this);\n");
            sb.append("        set" + relNameUpshifted + "(item);\n");
            sb.append("    }\n\n");
            sb.append("    public void clear" + relNameUpshifted + "(" + toClassName + " item) {\n");
            sb.append("        item.set" + inverseRelNameUpshifted + "(null);\n");
            sb.append("        set" + relNameUpshifted + "(null);\n");
            sb.append("    }\n\n");
        } else {
            assert false;
        }
    }
}
