package uk.ac.ebi.pride.tools.converter.report.validator.objectrules;

import psidev.psi.tools.ontology_manager.OntologyManager;
import psidev.psi.tools.validator.Context;
import psidev.psi.tools.validator.MessageLevel;
import psidev.psi.tools.validator.ValidatorException;
import psidev.psi.tools.validator.ValidatorMessage;
import psidev.psi.tools.validator.rules.codedrule.ObjectRule;
import uk.ac.ebi.pride.tools.converter.report.model.DatabaseMapping;
import java.util.ArrayList;
import java.util.Collection;

/**
 * ObjectRule to check if a PTM annotation has been provided.
 * <p/>
 * NOTE: there are also cv-mapping rules to check these elements, but they may not
 * result in an error if the rule could not be applied because of missing elements.
 *
 * @author Florian Reisinger
 * @since 0.1
 */
public class DatabaseMappingCheck<ReportObject> extends ObjectRule<ReportObject> {

    private static final Context context = new Context("PTM element");

    public DatabaseMappingCheck(OntologyManager ontologyManager) {
        super(ontologyManager);
        setName("DatabaseMappingCheck");
    }

    /**
     * @return a String that should uniquely identify the rule
     */
    @Override
    public String getId() {
        return "DBMAP";
    }

    /**
     * This rule can only check objects of the following types (as they wrap
     * the instrument element):
     * - Description
     * - Instrument
     * <p/>
     * NOTE: higher level objects (like MzData or Experiment) are not taken into account
     * as these are usually too big and the validator is supposed to break them into parts.
     *
     * @param t the object to check.
     * @return true if this rule can check the provided object.
     */
    public boolean canCheck(Object t) {
        return (t instanceof DatabaseMapping);
    }

    public Collection<ValidatorMessage> check(ReportObject reportObject) throws ValidatorException {
        Collection<ValidatorMessage> msgs = new ArrayList<ValidatorMessage>();
        if (reportObject instanceof DatabaseMapping) {
            DatabaseMapping mapping = (DatabaseMapping) reportObject;
            if (mapping == null) {
                msgs.add(new ValidatorMessage("DatabaseMapping must not be null!", MessageLevel.ERROR, context, this));
            } else {
                if (mapping.getCuratedDatabaseName() == null || "".equals(mapping.getCuratedDatabaseName())) {
                    msgs.add(new ValidatorMessage("Curated database name must be set for this search database: " + mapping.getSearchEngineDatabaseName() + ". ", MessageLevel.ERROR, context, this));
                }
                if (mapping.getCuratedDatabaseVersion() == null || "".equals(mapping.getCuratedDatabaseVersion())) {
                    msgs.add(new ValidatorMessage("Curated database version should be set for this search database: " + mapping.getSearchEngineDatabaseName() + ". Please provide a the version of the search database you used in your experiment (for example release-2012_02 for UniprotKB) or the date you generated or downloaded the search database.", MessageLevel.WARN, context, this));
                }
            }
        } else {
            msgs.add(new ValidatorMessage("DatabaseMappingCheck: Could not check the presented object '" + reportObject.getClass() + "', as it is not of a supported type (DatabaseMapping)!", MessageLevel.ERROR, context, this));
        }
        return msgs;
    }
}
