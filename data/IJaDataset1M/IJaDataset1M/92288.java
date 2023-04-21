package org.dcm4chee.usr.ui.validator;

import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.dcm4chee.usr.model.AETGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @author Robert David <robert.david@agfa.com>
* @version $Revision$ $Date$
* @since Apr. 20, 2011
*/
public class AETGroupValidator extends AbstractValidator<String> {

    private static final long serialVersionUID = 1L;

    private static Logger log = LoggerFactory.getLogger(AETGroupValidator.class);

    private ListModel<AETGroup> allAETGroups;

    private String ignoreName;

    public AETGroupValidator(ListModel<AETGroup> allAETGroups, String ignoreName) {
        this.allAETGroups = allAETGroups;
        this.ignoreName = ignoreName;
    }

    @Override
    protected void onValidate(IValidatable<String> validatable) {
        try {
            for (AETGroup anAETGroup : this.allAETGroups.getObject()) {
                if (anAETGroup.getGroupname().equalsIgnoreCase(validatable.getValue())) if (!validatable.getValue().equalsIgnoreCase(ignoreName)) error(validatable);
            }
        } catch (Exception e) {
            log.error(this.getClass().toString() + ": " + "onValidate: " + e.getMessage());
            log.debug("Exception: ", e);
        }
    }
}
