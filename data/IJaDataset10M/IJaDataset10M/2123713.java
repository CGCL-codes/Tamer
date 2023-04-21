package net.taylor.validation.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.rules.RuleRuntime;
import net.taylor.rules.util.Jsr94Util;
import net.taylor.util.MessageSource;
import net.taylor.validation.Context;
import net.taylor.validation.ValidationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Managers the validation framework by setting up the context, invoking the
 * RuleManager and determining when to throw a Validation Exception.
 * <p>
 * 
 * @author jgilbert
 * @version $Id: ValidationManagerBean.java,v 1.1 2006/01/08 20:09:17 jgilbert01 Exp $
 */
@Stateless
public class ValidationManagerBean implements ValidationManager {

    private static final Log logger = LogFactory.getLog(ValidationManagerBean.class);

    private String basename = "validation-messages";

    @Resource(name = "net.taylor.ValidationRuleRuntime")
    private RuleRuntime ruleRuntime;

    public void validate(final String rulesetName, final Object[] objects, final Locale locale) throws ValidationException {
        logger.debug(this);
        MessageSource messageSource = new MessageSource(basename);
        Context context = new Context(rulesetName, messageSource, locale);
        Map<String, Context> properties = new HashMap<String, Context>();
        properties.put("context", context);
        Jsr94Util.execute(ruleRuntime, rulesetName, Arrays.asList(objects), properties);
        if (context.hasErrors()) {
            throw new ValidationException(context.getMessages());
        }
    }

    public void doTest(final boolean messageExpected, final int messageCount, final String rulesetName, final Object[] args) {
        logger.debug(this);
        boolean ok = !messageExpected;
        try {
            validate(rulesetName, args, Locale.getDefault());
        } catch (ValidationException e) {
            ok = messageExpected && e.getMessages().size() == messageCount;
            logger.info("Message Count == " + e.getMessages().size());
            logger.info(e.getMessages());
            if (!ok) {
                throw new RuntimeException("Message Count != " + messageCount);
            }
        }
    }
}
