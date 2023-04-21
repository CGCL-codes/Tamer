package org.formproc.validation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import com.anthonyeden.lib.config.Configuration;
import com.anthonyeden.lib.util.ClassUtilities;
import org.formproc.FormData;
import org.formproc.FormElement;
import org.formproc.FormUtilities;

/**
 * A FormValidator which uses Rule objects to test for validity.
 *
 * @author Anthony Eden
 */
public class RuleValidator extends Validator {

    private ArrayList rules = new ArrayList();

    /**
     * Construct a RuleValidator.
     */
    public RuleValidator() {
    }

    /**
     * Configure the validator using the given configuration object
     *
     * @param configuration The configuration object
     * @throws Exception
     */
    public void configureInternal(Configuration configuration) throws Exception {
        Iterator ruleElements = configuration.getChildren("rule").iterator();
        while (ruleElements.hasNext()) {
            Configuration ruleElement = (Configuration) ruleElements.next();
            String ruleClassName = ruleElement.getValue().trim();
            Rule rule = (Rule) ClassUtilities.loadClass(ruleClassName).newInstance();
            rule.configure(ruleElement);
            rules.add(rule);
        }
    }

    /**
     * Validate the given FormData array.
     *
     * @param formElements An array of FormElement objects
     * @param formData An array of FormData objects
     * @param locale The Locale
     * @throws Exception
     */
    public ValidationResult validate(FormElement[] formElements, FormData[] formData, Locale locale) throws Exception {
        Iterator iter = rules.iterator();
        while (iter.hasNext()) {
            Rule rule = (Rule) iter.next();
            for (int i = 0; i < formElements.length; i++) {
                FormData currentFormData = FormUtilities.findFormData(formElements[i], formData);
                if (!rule.validate(currentFormData.getValue(), locale).isValid()) {
                    return new ValidationResult(formElements, formData, locale, this);
                }
            }
        }
        return new ValidationResult(formElements, formData, locale);
    }
}
