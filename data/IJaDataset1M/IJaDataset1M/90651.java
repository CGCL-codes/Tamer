package org.br.xvalidator.handler.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Form;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.ValidatorResult;
import org.apache.commons.validator.ValidatorResults;
import org.br.xvalidator.handler.IValidatorHandler;
import org.xml.sax.SAXException;

public final class ValidatorSiasgnetHandler implements IValidatorHandler {

    private static final String VALIDATOR_RULES_BASE = "/META-INF/validator/validator-rules-base.xml";

    private static final String MSG_PREFIX = "Validator.";

    private ArrayList<URL> resourceList;

    private Locale locale = new Locale("pt", "BR");

    private ValidatorResources resources;

    private Validator validator;

    private final boolean useDefaults;

    private ResourceBundle resourceBundle;

    /**
	 * Constructor indicates if we should use own validator rules. Should use the configure(URL providerValidatorRulesXML) method ({@link #configure(URL providerValidatorRulesXML)}).<br>
	 */
    public ValidatorSiasgnetHandler() {
        this.useDefaults = Boolean.FALSE;
    }

    /**
	 * @param useDefaults
	 *            Indicates if we should use default validator rules, located in the same package as this class, and named <code>validator-rules.xml</code>.
	 */
    public ValidatorSiasgnetHandler(boolean useDefaults) {
        this(null, useDefaults);
    }

    /**
	 * @param locale
	 *            the Locale
	 * @param useDefaults
	 *            indicates if this tool should use the default rule set
	 */
    public ValidatorSiasgnetHandler(Locale locale, boolean useDefaults) {
        this.locale = locale;
        this.useDefaults = useDefaults;
    }

    private void initResouce() {
        URL url = ValidatorSiasgnetHandler.class.getResource(ValidatorSiasgnetHandler.VALIDATOR_RULES_BASE);
        getResourceList().add(url);
    }

    private void initResouce(URL providerValidatorRulesXML) {
        getResourceList().add(providerValidatorRulesXML);
    }

    /**
	 * @return Returns the locale.
	 */
    public Locale getLocale() {
        return this.locale;
    }

    /**
	 * @param locale
	 *            The locale to set.
	 */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
	 * Performs the validation. As no form is specified, the bean class name is used. The message key retrieving works as follows:
	 * <ul>
	 * <li>If the validating field has a message, its key is returned</li>
	 * <li>Else, the validator action message is returned</li>
	 * <li>Otherwise, a default message like "validator.(form).(propertyname)" is returned</li>
	 * </ul>
	 * 
	 * @param bean
	 *            the bean to be validated
	 * @return an array of messages or null if the validation gets passed
	 */
    public HashMap<String, String> perform(Object bean) {
        String packageName = bean.getClass().getPackage().getName();
        String className = bean.getClass().getName();
        String form = null;
        if (packageName.length() > 0) {
            form = className.substring(packageName.length() + 1);
        } else {
            form = className;
        }
        return this.perform(bean, form);
    }

    /**
	 * Performs the validation against the specified validation form
	 * 
	 * @param bean
	 *            the bean to validate
	 * @param form
	 *            the form containing the ruleset
	 * @return an array of messages or null if the validation gets passed
	 */
    @Override
    public HashMap<String, String> perform(Object bean, String form) {
        return this.perform(bean, form, 0);
    }

    /**
	 * Performs the validation against the specified validation form
	 * 
	 * @param bean
	 *            the bean to validate
	 * @param form
	 *            the form containing the ruleset
	 * @param page
	 *            the initial validation page (a zero value means to validate all pages)
	 * @return an array of messages or null if the validation gets passed
	 */
    public HashMap<String, String> perform(Object bean, String form, int page) {
        this.validator = new Validator(this.resources, form);
        this.validator.setParameter(Validator.BEAN_PARAM, bean);
        this.validator.setOnlyReturnErrors(true);
        this.validator.setPage(page);
        ValidatorResults results = null;
        HashMap<String, String> retorno = null;
        try {
            results = this.validator.validate();
            retorno = printResults(bean, results, form);
        } catch (ValidatorException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return retorno;
    }

    /**
	 * Calculates the messages to be returned
	 * 
	 * @param bean
	 *            the bean validated
	 * @param results
	 *            the validator results
	 * @param res
	 *            the validator resources
	 * @param formName
	 *            the form name
	 * @return the messages array
	 */
    private HashMap<String, String> printResults(Object bean, ValidatorResults results, String formName) {
        HashMap<String, String> messages = new HashMap<String, String>();
        Form form = this.resources.getForm(Locale.getDefault(), formName);
        @SuppressWarnings("rawtypes") Iterator propertyNames = results.getPropertyNames().iterator();
        while (propertyNames.hasNext()) {
            String propertyName = (String) propertyNames.next();
            Field field = form.getField(propertyName);
            ValidatorResult result = results.getValidatorResult(propertyName);
            @SuppressWarnings("rawtypes") Iterator keys = result.getActions();
            while (keys.hasNext()) {
                String actName = (String) keys.next();
                if (!result.isValid(actName)) {
                    String msgKey = null;
                    msgKey = field.getMsg(actName);
                    if (msgKey == null) {
                        msgKey = MSG_PREFIX + form.getName() + "." + propertyName + "." + actName;
                    }
                    String message = this.getResourceBundle().getString(msgKey);
                    String varArg = field.getVarValue(actName);
                    Object[] args;
                    if (varArg != null) {
                        args = new Object[] { form.getName() + "." + propertyName, field.getVar(actName).getValue() };
                    } else {
                        args = new Object[] { form.getName() + "." + propertyName };
                    }
                    StringBuilder origemFormatada = new StringBuilder(msgKey);
                    origemFormatada.append("-");
                    origemFormatada.append(form.getName());
                    origemFormatada.append(".");
                    origemFormatada.append(propertyName);
                    messages.put(origemFormatada.toString(), MessageFormat.format(message, args));
                }
            }
        }
        return messages;
    }

    /**
	 * @return the resourceList
	 */
    public ArrayList<URL> getResourceList() {
        if (this.resourceList == null) {
            this.resourceList = new ArrayList<URL>(0);
        }
        return this.resourceList;
    }

    /**
	 * @param resourceList
	 *            the resourceList to set
	 */
    public void setResourceList(ArrayList<URL> resourceList) {
        this.resourceList = resourceList;
    }

    @Override
    public void configure(URL providerValidatorRulesXML) {
        if (!this.useDefaults) {
            initResouce(providerValidatorRulesXML);
        }
    }

    /**
	 * Create the ValidatorResources object
	 */
    @Override
    public void configure() {
        initResouce();
        if (this.locale == null) {
            this.locale = Locale.getDefault();
        }
        InputStream[] ins = new InputStream[getResourceList().size()];
        try {
            int i = 0;
            for (URL url : getResourceList()) {
                ins[i++] = url.openStream();
            }
            this.resources = new ValidatorResources(ins);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (SAXException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public final ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public final void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
}
