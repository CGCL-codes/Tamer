package eu.coaxion.gnumedj.postgre.v14;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.orm.PersistentSession;
import org.orm.criteria.*;

public class AllergyDetachedCriteria extends AbstractORMDetachedCriteria {

    public final IntegerExpression pk_audit;

    public final IntegerExpression row_version;

    public final TimestampExpression modified_when;

    public final StringExpression modified_by;

    public final IntegerExpression pk_item;

    public final TimestampExpression clin_when;

    public final StringExpression narrative;

    public final StringExpression soap_cat;

    public final IntegerExpression pk;

    public final StringExpression substance;

    public final StringExpression substance_code;

    public final StringExpression generics;

    public final StringExpression allergene;

    public final StringExpression atc_code;

    public final BooleanExpression generic_specific;

    public final BooleanExpression definite;

    public AllergyDetachedCriteria() {
        super(eu.coaxion.gnumedj.postgre.v14.Allergy.class, eu.coaxion.gnumedj.postgre.v14.AllergyCriteria.class);
        pk_audit = new IntegerExpression("pk_audit", this.getDetachedCriteria());
        row_version = new IntegerExpression("row_version", this.getDetachedCriteria());
        modified_when = new TimestampExpression("modified_when", this.getDetachedCriteria());
        modified_by = new StringExpression("modified_by", this.getDetachedCriteria());
        pk_item = new IntegerExpression("pk_item", this.getDetachedCriteria());
        clin_when = new TimestampExpression("clin_when", this.getDetachedCriteria());
        narrative = new StringExpression("narrative", this.getDetachedCriteria());
        soap_cat = new StringExpression("soap_cat", this.getDetachedCriteria());
        pk = new IntegerExpression("pk", this.getDetachedCriteria());
        substance = new StringExpression("substance", this.getDetachedCriteria());
        substance_code = new StringExpression("substance_code", this.getDetachedCriteria());
        generics = new StringExpression("generics", this.getDetachedCriteria());
        allergene = new StringExpression("allergene", this.getDetachedCriteria());
        atc_code = new StringExpression("atc_code", this.getDetachedCriteria());
        generic_specific = new BooleanExpression("generic_specific", this.getDetachedCriteria());
        definite = new BooleanExpression("definite", this.getDetachedCriteria());
    }

    public AllergyDetachedCriteria(DetachedCriteria aDetachedCriteria) {
        super(aDetachedCriteria, eu.coaxion.gnumedj.postgre.v14.AllergyCriteria.class);
        pk_audit = new IntegerExpression("pk_audit", this.getDetachedCriteria());
        row_version = new IntegerExpression("row_version", this.getDetachedCriteria());
        modified_when = new TimestampExpression("modified_when", this.getDetachedCriteria());
        modified_by = new StringExpression("modified_by", this.getDetachedCriteria());
        pk_item = new IntegerExpression("pk_item", this.getDetachedCriteria());
        clin_when = new TimestampExpression("clin_when", this.getDetachedCriteria());
        narrative = new StringExpression("narrative", this.getDetachedCriteria());
        soap_cat = new StringExpression("soap_cat", this.getDetachedCriteria());
        pk = new IntegerExpression("pk", this.getDetachedCriteria());
        substance = new StringExpression("substance", this.getDetachedCriteria());
        substance_code = new StringExpression("substance_code", this.getDetachedCriteria());
        generics = new StringExpression("generics", this.getDetachedCriteria());
        allergene = new StringExpression("allergene", this.getDetachedCriteria());
        atc_code = new StringExpression("atc_code", this.getDetachedCriteria());
        generic_specific = new BooleanExpression("generic_specific", this.getDetachedCriteria());
        definite = new BooleanExpression("definite", this.getDetachedCriteria());
    }

    public EncounterDetachedCriteria createFk_encounterCriteria() {
        return new EncounterDetachedCriteria(createCriteria("fk_encounter"));
    }

    public EpisodeDetachedCriteria createFk_episodeCriteria() {
        return new EpisodeDetachedCriteria(createCriteria("fk_episode"));
    }

    public _enum_allergy_typeDetachedCriteria createFk_typeCriteria() {
        return new _enum_allergy_typeDetachedCriteria(createCriteria("fk_type"));
    }

    public Allergy uniqueAllergy(PersistentSession session) {
        return (Allergy) super.createExecutableCriteria(session).uniqueResult();
    }

    public Allergy[] listAllergy(PersistentSession session) {
        List list = super.createExecutableCriteria(session).list();
        return (Allergy[]) list.toArray(new Allergy[list.size()]);
    }
}
