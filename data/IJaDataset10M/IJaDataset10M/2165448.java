package er.extensions;

import com.webobjects.foundation.*;
import com.webobjects.eocontrol.*;
import com.webobjects.eoaccess.*;
import org.apache.log4j.Category;

public class ERXValidationException extends NSValidation.ValidationException implements NSKeyValueCoding {

    public static final Category cat = Category.getInstance("er.validation.ERXValidationException");

    public static final String NullPropertyException = "NullPropertyException";

    public static final String InvalidNumberException = "InvalidNumberException";

    public static final String MandatoryRelationshipException = "MandatoryRelationshipException";

    public static final String ObjectRemovalException = "ObjectRemovalException";

    public static final String CustomMethodException = "CustomMethodException";

    public static final String ValidationTypeUserInfoKey = "ValidationTypeUserInfoKey";

    public static final String ValidatedValueUserInfoKey = "ValidatedValueUserInfoKey";

    public static final String ValidatedMethodUserInfoKey = "ValidatedMethodUserInfoKey";

    public static final String TargetLanguageUserInfoKey = "TargetLanguageUserInfoKey";

    public ERXValidationException(String type, NSDictionary userInfo) {
        super(type, userInfo);
        setType(type);
    }

    public ERXValidationException(EOEnterpriseObject object, String method) {
        super(CustomMethodException);
        customExceptionForMethod(object, method);
    }

    public ERXValidationException(EOEnterpriseObject object, String property, Object value, String type) {
        super(type);
        exceptionForObject(object, property, value, type);
    }

    protected String _message;

    public String getMessage() {
        if (_message == null) {
            _message = ERXValidationFactory.defaultFactory().messageForException(this);
        }
        return _message;
    }

    public void customExceptionForMethod(EOEnterpriseObject object, String method) {
        setEoObject(object);
        setMethod(method);
        setType(CustomMethodException);
    }

    public void exceptionForObject(EOEnterpriseObject object, String property, Object value, String type) {
        if (object != null) setEoObject(object);
        if (property != null) setPropertyKey(property);
        setValue((value != null ? value : NSKeyValueCoding.NullValue));
        setType(type);
    }

    public boolean isCustomMethodException() {
        return type() == CustomMethodException;
    }

    protected NSMutableDictionary _userInfo() {
        return (NSMutableDictionary) super.userInfo();
    }

    public Object valueForKey(String key) {
        Object value = null;
        if (key.equals("object")) value = object(); else if (key.equals("propertyKey")) value = propertyKey(); else if (key.equals("method")) value = method(); else if (key.equals("type")) value = type(); else if (key.equals("value")) value = value();
        return value != null ? value : objectForKey(key);
    }

    public void takeValueForKey(Object value, String key) {
        setObjectForKey(value, key);
    }

    public Object objectForKey(String aKey) {
        return _userInfo().objectForKey(aKey);
    }

    public void setObjectForKey(Object anObject, String aKey) {
        _userInfo().setObjectForKey(anObject, aKey);
    }

    public String method() {
        return (String) objectForKey(ValidatedMethodUserInfoKey);
    }

    public void setMethod(String aMethod) {
        setObjectForKey(aMethod, ValidatedMethodUserInfoKey);
    }

    public EOEnterpriseObject eoObject() {
        return (EOEnterpriseObject) objectForKey(ValidatedObjectUserInfoKey);
    }

    public void setEoObject(EOEnterpriseObject anObject) {
        setObjectForKey(anObject, ValidatedObjectUserInfoKey);
    }

    public String propertyKey() {
        return (String) objectForKey(ValidatedKeyUserInfoKey);
    }

    public void setPropertyKey(String aProperty) {
        setObjectForKey(aProperty, ValidatedKeyUserInfoKey);
    }

    public String type() {
        return (String) objectForKey(ValidationTypeUserInfoKey);
    }

    public void setType(String aType) {
        setObjectForKey(aType, ValidationTypeUserInfoKey);
    }

    public Object value() {
        return objectForKey(ValidatedValueUserInfoKey);
    }

    public void setValue(Object aValue) {
        setObjectForKey(aValue, ValidatedValueUserInfoKey);
    }

    public String targetLanguage() {
        return (String) objectForKey(TargetLanguageUserInfoKey);
    }

    public void setTargetLanguage(String aValue) {
        setObjectForKey(aValue, TargetLanguageUserInfoKey);
    }

    public NSMutableArray additionalExceptionsMutable() {
        return (NSMutableArray) objectForKey(AdditionalExceptionsKey);
    }

    private volatile Object _delegate;

    public Object delegate() {
        return _delegate != null ? _delegate : ERXValidationFactory.defaultDelegate();
    }

    public void setDelegate(Object obj) {
        _delegate = obj;
    }
}
