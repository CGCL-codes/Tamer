package net.sourceforge.ecm.validator;

import net.sourceforge.ecm.exceptions.ObjectNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: May 18, 2009
 * Time: 9:57:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class ValidatorClass {

    private static final Log LOG = LogFactory.getLog(ValidatorClass.class);

    public ValidationResult validate(String objpid) throws ObjectNotFoundException {
        LOG.trace("Entering validate(), objpid='" + objpid + "'");
        try {
            LOG.trace("doms user token made");
            CompoundContentModel compoundContentModel = null;
            compoundContentModel = CompoundContentModel.loadFromDataObject(objpid);
            LOG.trace("Compound content model created");
            Validator[] validators = { new OntologyValidator(), new DatastreamValidator() };
            LOG.trace("Validators created");
            ValidationResult global_result = new ValidationResult();
            for (Validator validator : validators) {
                ValidationResult result = validator.validate(objpid, compoundContentModel);
                LOG.trace("validator validated");
                global_result = global_result.combine(result);
            }
            LOG.trace("All validators run, returning");
            return global_result;
        } catch (Throwable e) {
            LOG.error("Caught exception", e);
            throw new Error(e);
        }
    }

    public ValidationResult validateAgainst(String objpid, String cmpid) {
        LOG.trace("Entering validateAgainst(), objpid='" + objpid + "', cmpid='" + cmpid + "'");
        try {
            LOG.trace("doms user token made");
            CompoundContentModel compoundContentModel = null;
            compoundContentModel = CompoundContentModel.loadFromContentModel(cmpid);
            LOG.trace("Compound content model created");
            Validator[] validators = { new OntologyValidator(), new DatastreamValidator() };
            LOG.trace("Validators created");
            ValidationResult global_result = new ValidationResult();
            for (Validator validator : validators) {
                ValidationResult result = validator.validate(objpid, compoundContentModel);
                LOG.trace("validator validated");
                global_result = global_result.combine(result);
            }
            LOG.trace("All validators run, returning");
            return global_result;
        } catch (Throwable e) {
            LOG.error("Caught exception", e);
            throw new Error(e);
        }
    }
}
