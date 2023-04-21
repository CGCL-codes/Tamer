package syndicus.domain;

import org.springframework.rules.Rules;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.support.DefaultRulesSource;
import syndicus.gui.end.DataHolder;

public class SyndicusValidationRulesSource extends DefaultRulesSource {

    /**
	 * Zipcode validator, allows NNNNN or NNNNN-NNNN
	 */
    final Constraint ZIPCODE_CONSTRAINT = all(new Constraint[] { required(), minLength(5), maxLength(10), regexp("[0-9]{5}(-[0-9]{4})?", "zipcodeConstraint") });

    /**
	 * Email validator, simply tests for x@y, wrap in ()? so it is optional
	 */
    final Constraint EMAIL_CONSTRAINT = all(new Constraint[] { regexp("([-a-zA-Z0-9.]+@[-a-zA-Z0-9.]+)?", "emailConstraint") });

    /**
	 * Phone number validator, must be 123-456-7890, wrap in ()? so it is
	 * optional
	 */
    final Constraint PHONE_CONSTRAINT = all(new Constraint[] { regexp("([0-9]{3}-[0-9]{3}-[0-9]{4})?", "phoneConstraint") });

    private final Constraint ALFANUMERIC_CONTRAINT = all(new Constraint[] { required(), minLength(2), regexp("[-'.a-zA-Z0-9_+/ ]*", "alphanumericConstraint") });

    /**
	 * Basic name validator. Note that the "alphabeticConstraint" argument is a
	 * message key used to locate the message to display when this validator
	 * fails.
	 */
    private final Constraint NAME_CONSTRAINT = all(new Constraint[] { required(), minLength(2), regexp("[-'.a-zA-Z ]*", "alphabeticConstraint") });

    /**
	 * Construct the rules source. Just add all the rules for each class that
	 * will be validated.
	 */
    public SyndicusValidationRulesSource() {
        super();
        addRules(createCostRules());
        addRules(createCostCatalogRules());
        addRules(createResidentialRules());
        addRules(createOwerRules());
        addRules(createPropertyRules());
        addRules(createCloseYearDatHolderRules());
    }

    private Rules createCloseYearDatHolderRules() {
        return new Rules(DataHolder.class) {

            @Override
            protected void initRules() {
                add(required("year"));
                add(required("residential"));
            }
        };
    }

    private Rules createCostCatalogRules() {
        return new Rules(CostCatalog.class) {

            @Override
            protected void initRules() {
                add("code", ALFANUMERIC_CONTRAINT);
                add("description", NAME_CONSTRAINT);
            }
        };
    }

    private Rules createCostRules() {
        return new Rules(Cost.class) {

            @Override
            protected void initRules() {
                add("insertDate", required());
                add("residential", required());
                add("costCatalog", required());
            }
        };
    }

    private Rules createOwerRules() {
        return new Rules(Owner.class) {

            @Override
            protected void initRules() {
                add("name", ALFANUMERIC_CONTRAINT);
                add("address", ALFANUMERIC_CONTRAINT);
                add("city", required());
            }
        };
    }

    private Rules createPropertyRules() {
        return new Rules(Propertie.class) {

            @Override
            protected void initRules() {
                add("residentie", required());
                add("description", ALFANUMERIC_CONTRAINT);
                add("propertieType", required());
            }
        };
    }

    private Rules createResidentialRules() {
        return new Rules(Residential.class) {

            @Override
            protected void initRules() {
                add("name", ALFANUMERIC_CONTRAINT);
                add("address", ALFANUMERIC_CONTRAINT);
            }
        };
    }
}
