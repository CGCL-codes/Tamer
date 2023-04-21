package org.formproc.form;

import java.util.ArrayList;
import com.anthonyeden.lib.config.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.formproc.FormManager;

/**
 * Default implementation of the FormElementGroup interface.
 *
 * @author Anthony Eden
 */
public class DefaultFormElementGroup extends AbstractFormElementGroup {

    private static final Log log = LogFactory.getLog(DefaultFormElementGroup.class);

    protected FormManager formManager;

    /**
     * Construct an empty FormElementGroup.
     */
    public DefaultFormElementGroup(FormManager formManager) {
        super(new ArrayList());
        this.formManager = formManager;
    }

    /**
     * Configure the FormElementGroup.
     *
     * @param configuration The configuration object
     */
    public void configure(Configuration configuration) throws Exception {
        setName(configuration.getAttribute("name"));
        Configuration validatorElement = configuration.getChild("validator");
        if (validatorElement == null) {
            log.debug("Validator element is null");
        } else {
            log.debug("Loading validator");
            validator = formManager.getValidator(validatorElement);
            if (log.isDebugEnabled()) log.debug("Validator loaded:" + validator);
        }
    }
}
