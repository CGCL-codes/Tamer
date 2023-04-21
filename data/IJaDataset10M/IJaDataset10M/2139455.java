package evolaris.platform.smssvc.web.form;

import java.util.Locale;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import evolaris.framework.async.datamodel.InteractionList;
import evolaris.framework.sys.web.form.EnterEditDuplicateForm;

/**
 * Base class for forms of InteractionList subclasses
 * @author richard.hable
 *
 */
@SuppressWarnings("serial")
public abstract class InteractionListEnterEditDuplicateForm<IL extends InteractionList> extends EnterEditDuplicateForm<IL> {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(InteractionListEnterEditDuplicateForm.class);

    private String description;

    @Override
    public void initialize(IL interactionList, Locale locale, Session session, MessageResources resources) {
        this.setId(interactionList.getId());
        this.setGroupId(interactionList.getGroup().getId());
        this.setDescription(interactionList.getDescription());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
