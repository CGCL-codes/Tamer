package abstractService.serviceManagement;

import converter.IConverter;

/**
 * The interface for the service wrapper.
 * 
 * @author Michael Sch�fer
 *
 */
public interface IServiceWrapper {

    public abstract boolean translateMethodRequest(String abstractServiceMethodIdentifier, Object[] abstractServiceMethodParameters) throws ServiceWrapperException;

    public abstract String getTranslatedMethodName();

    public abstract String getTranslatedMethodNamespace();

    public abstract Object[] getTranslatedMethodParameters();

    public abstract String[] getTranslatedMethodParameterNames();

    public abstract boolean getTranslatedMethodOneWay();

    public abstract boolean getTranslatedMethodComplexType();

    public abstract String[] getTranslatedMethodTypeNames();

    public abstract String[] getTranslatedMethodTypeNamespaces();

    public abstract IConverter getTranslatedMethodReturnValueConverter();

    public abstract boolean getTranslatedMethodReturnValueComplexType();

    public abstract String getTranslatedMethodReturnValueTypeName();

    public abstract String getTranslatedMethodReturnValueTypeNamespace();

    public abstract String[] getTranslatedMethodParameterNestedComplexTypeNames();

    public abstract String[] getTranslatedMethodParameterNestedComplexTypeNamespaces();

    public abstract String[] getTranslatedMethodReturnValueNestedComplexTypeNames();

    public abstract String[] getTranslatedMethodReturnValueNestedComplexTypeNamespaces();
}
