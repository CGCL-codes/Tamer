package org.freeworld.jmultiplug.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.freeworld.jmultiplug.config.Preloader;
import org.freeworld.jmultiplug.intl.IntlString;
import org.freeworld.jmultiplug.logging.Logger;
import org.freeworld.jmultiplug.validation.core.CheckPoint;
import org.freeworld.jmultiplug.validation.core.ValidationFactory;
import org.freeworld.jmultiplug.validation.core.ValidationNameSpace;
import org.freeworld.jmultiplug.validation.core.ValidationToken;
import org.freeworld.jmultiplug.validation.core.Validator;
import org.freeworld.jmultiplug.validation.impl.base.ValidationContext;
import org.freeworld.jmultiplug.validation.results.ValidationResultFactory;
import org.freeworld.jmultiplug.validation.tokenizer.ValidationTokenFactory;

/**
 * The Validation Singleton is the root of the validation system. It stores
 * hooks to all sub-systems used to generate validation results. It also
 * manages all the selection of appropriate validators for each query. This
 * works both for user quesries and for internal mapping of validations. 
 * 
 * Validation Types
 * When a user kicks off a validation job, it may be either Simple or
 * Aggregate. A simple validation returns a single validation response. This
 * works quire well when the user is validating something quite small and
 * simple, or when failure specifics aren't meaningful. When either job is
 * invoked, it is fed into this module to route to an appropriate validator.
 * If the validation request is simple, but the validator is aggregate, only
 * the first tag with the highest severity are returned. For instance, if the
 * validation result's were: {OK, OK, WARN, ERROR, WARN, ERROR} only the fourth
 * result would be returned to the user.
 * 
 * Validation Mapping
 * All RAW aggregators can naturally be used in Aggregate results and aggregate
 * results can include sub-aggregate results. When aggregates fetch specific
 * results, they must use this validation method to reference the RAW validation
 * implementations. This way, validations can always be reused in their most
 * primitive implementations and not be limited by their aggregate graphs.
 * 
 * @author dchemko
 */
public class Validation {

    private static final IntlString INTL_FACTORY_CLASS_NULL = new IntlString(Validation.class, "01_FACTORY_CLASS_NULL", "Cannot register validation factory \"{0}\" because it has an empty class definition.");

    private static final IntlString INTL_FACTORY_CLASS_INIT_ERROR = new IntlString(Validation.class, "02_FACTORY_CLASS_INIT_ERROR", "Cannot register validation factory \"{0}\" because \"{1}\" class couldn''t be initialized.");

    private static final String CONFIG_VALIDATORS_PREINIT_ROOT = "jmultiplug.validation.validators.impl";

    private static final String CONFIG_TOKENIZERS_PREINIT_ROOT = "jmultiplug.validation.tokenizers.impl";

    private static Validation instance = null;

    private Object nameSpaceLock = new Object();

    private List<ValidationNameSpace> nameSpaces = new ArrayList<ValidationNameSpace>();

    private Object factoryLock = new Object();

    private Map<String, ValidationFactory> factories = new HashMap<String, ValidationFactory>();

    static {
        Preloader.safePreload("jmultiplug-validatiors", CONFIG_VALIDATORS_PREINIT_ROOT);
        Preloader.safePreload("jmultiplug-tokenizers", CONFIG_TOKENIZERS_PREINIT_ROOT);
    }

    /**
    * Implemented to prevent subclassing or instantiation to confirm to
    * singleton dynamics 
    */
    private Validation() {
    }

    /**
    * Fetch an instance of this singleton
    * 
    * @return - Instance of Validation
    */
    private static Validation getInstance() {
        if (instance == null) instance = new Validation();
        return instance;
    }

    /**
    * Adds a new Validation framework namespace to the validation to the
    * validation framework. 
    *  
    * @param nameSpace - Namespace to be added
    */
    public static void addNameSpace(ValidationNameSpace nameSpace) {
        if (nameSpace == null) return;
        synchronized (getInstance().nameSpaceLock) {
            getInstance().nameSpaces.add(nameSpace);
        }
    }

    /**
    * Removes a namespace from the active validation framework
    * 
    * @param name - Name of the namespace to be removed
    */
    public static void removeNameSpace(String name) {
        if (name == null) return;
        synchronized (getInstance().nameSpaceLock) {
            ValidationNameSpace removal = null;
            for (int i = 0; i < getInstance().nameSpaces.size(); i++) if (getInstance().nameSpaces.get(i).getNameSpaceName().equalsIgnoreCase(name)) removal = getInstance().nameSpaces.get(i);
            if (removal != null) getInstance().nameSpaces.remove(name);
        }
    }

    /**
    * Registers a validation implementation into the validation root singleton.
    * Once registered, the validation factory can be used to produce validation
    * requests for the requesters if the factory matches the requesting
    * validation. 
    * 
    * @param type - Type of factory to register. There are RAW (Single result)
    * and Aggregate (Multiple Results) validators.  
    * @param clsRef - Class referred to
    * @param factoryName - Friendly name of the validator
    */
    public static void registerFactory(Class<?> clsRef, String factoryName) {
        if (clsRef == null) {
            Logger.error(INTL_FACTORY_CLASS_NULL.setArgs(factoryName));
            return;
        }
        ValidationFactory ref = null;
        try {
            ref = (ValidationFactory) clsRef.newInstance();
        } catch (Exception e) {
            Logger.error(INTL_FACTORY_CLASS_INIT_ERROR.setArgs(factoryName, clsRef.getName()));
            return;
        }
        registerFactory(ref, factoryName);
    }

    /**
    * Registers a validator into the validation framework.  
    * 
    * @param type - The type of factory being added
    * @param ref - Object reference to the factory
    * @param factoryName - Friendly name of the factory
    */
    public static void registerFactory(ValidationFactory ref, String factoryName) {
        if (ref == null || factoryName == null) return;
        synchronized (getInstance().factoryLock) {
            getInstance().factories.put(factoryName, ref);
        }
    }

    /**
    * Fetch a list of all enumerable validators in all active factories
    * 
    * @return - List of validations that can be run
    */
    public static List<String> getAllValidatorNames() {
        List<String> allValidators = new ArrayList<String>();
        synchronized (getInstance().factoryLock) {
            Iterator<Entry<String, ValidationFactory>> iter = getInstance().factories.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, ValidationFactory> entry = iter.next();
                allValidators.addAll(entry.getValue().getValidatorNames());
            }
        }
        return allValidators;
    }

    /**
    * Reads in an object to be validated and performs a series of validations
    * on the object. The validation actions are decided upon by backend
    * processes. 
    * 
    * @param validationObject - Object to be tested
    * @return ValidationResult - Result of the first and most sever problem found
    */
    public static ValidationResult validateItem(CheckPoint cp, Object validationObject) {
        return null;
    }

    public static ValidationResult validateItem(CheckPoint cp, ValidationNameSpace ns, Object validationObject) {
        return null;
    }

    /**
    * Reads in an object to be validated and performs a series of validations
    * on the object. The validation actions are decided upon by backend
    * processes. 
    * 
    * @param validationObject
    * @return List<ValidationResult> - Result of all validation operations
    * performed on the object. Since a single object can have many differently
    * assigned validations, this list is a combination of them all. The list
    * data is not ordered in any meaningful way. It is up to the caller to
    * decide the best way of presenting the information returned.    
    */
    public static List<ValidationResult> validate(CheckPoint cp, Object validationObject) {
        List<ValidationToken> tokens = ValidationTokenFactory.getTokens(validationObject);
        Validator validator = getInstance().getValidator(cp.getName());
        if (validator == null) return ValidationResultFactory.createCheckPointNoValidatorError(cp);
        ValidationContext context = new ValidationContext();
        context.setCheckPoint(cp);
        validator.validate(context, tokens);
        return context.getValidationResults();
    }

    public static List<ValidationResult> validate(CheckPoint cp, ValidationNameSpace ns, Object validationObject) {
        return null;
    }

    protected Validator getValidator(String name) {
        List<String> names = null;
        synchronized (factoryLock) {
            Iterator<Entry<String, ValidationFactory>> it = factories.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, ValidationFactory> entry = it.next();
                names = entry.getValue().getValidatorNames();
                if (names.contains(name)) return entry.getValue().getValidator(name);
            }
        }
        return null;
    }
}
