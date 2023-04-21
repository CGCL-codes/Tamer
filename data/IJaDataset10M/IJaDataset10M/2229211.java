package net.sf.istcontract.wsimport.tools.wsdl.document.jaxws;

import net.sf.istcontract.wsimport.tools.wsdl.framework.ExtensionImpl;
import org.w3c.dom.Element;
import org.xml.sax.Locator;
import javax.xml.namespace.QName;
import java.util.*;

/**
 * @author Vivek Pandey
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class JAXWSBinding extends ExtensionImpl {

    /**
     *
     */
    public JAXWSBinding(Locator locator) {
        super(locator);
        jaxbBindings = new HashSet<Element>();
    }

    public void validateThis() {
    }

    public QName getElementName() {
        return JAXWSBindingsConstants.JAXWS_BINDINGS;
    }

    public QName getWSDLElementName() {
        return getElementName();
    }

    public void addExtension(ExtensionImpl e) {
    }

    public Iterable<ExtensionImpl> extensions() {
        return null;
    }

    /**
     * @return Returns the enableAsyncMapping.
     */
    public Boolean isEnableAsyncMapping() {
        return enableAsyncMapping;
    }

    /**
     * @param enableAsyncMapping The enableAsyncMapping to set.
     */
    public void setEnableAsyncMapping(Boolean enableAsyncMapping) {
        this.enableAsyncMapping = enableAsyncMapping;
    }

    /**
     * @return Returns the enableMimeContentMapping.
     */
    public Boolean isEnableMimeContentMapping() {
        return enableMimeContentMapping;
    }

    /**
     * @param enableMimeContentMapping The enableMimeContentMapping to set.
     */
    public void setEnableMimeContentMapping(Boolean enableMimeContentMapping) {
        this.enableMimeContentMapping = enableMimeContentMapping;
    }

    /**
     * @return Returns the enableWrapperStyle.
     */
    public Boolean isEnableWrapperStyle() {
        return enableWrapperStyle;
    }

    /**
     * @param enableWrapperStyle The enableWrapperStyle to set.
     */
    public void setEnableWrapperStyle(Boolean enableWrapperStyle) {
        this.enableWrapperStyle = enableWrapperStyle;
    }

    /**
     * @return Returns the jaxwsPackage.
     */
    public CustomName getJaxwsPackage() {
        return jaxwsPackage;
    }

    /**
     * @param jaxwsPackage The jaxwsPackage to set.
     */
    public void setJaxwsPackage(CustomName jaxwsPackage) {
        this.jaxwsPackage = jaxwsPackage;
    }

    /**
     * @return Returns the node.
     */
    public String getNode() {
        return node;
    }

    /**
     * @param node The node to set.
     */
    public void setNode(String node) {
        this.node = node;
    }

    /**
     * @return Returns the version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version The version to set.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return Returns the wsdlLocation.
     */
    public String getWsdlLocation() {
        return wsdlLocation;
    }

    /**
     * @param wsdlLocation The wsdlLocation to set.
     */
    public void setWsdlLocation(String wsdlLocation) {
        this.wsdlLocation = wsdlLocation;
    }

    /**
     * @return Returns the wsdlNamespace.
     */
    public String getWsdlNamespace() {
        return wsdlNamespace;
    }

    /**
     * @param wsdlNamespace The wsdlNamespace to set.
     */
    public void setWsdlNamespace(String wsdlNamespace) {
        this.wsdlNamespace = wsdlNamespace;
    }

    /**
     * @return Returns the jaxbBindings.
     */
    public Set<Element> getJaxbBindings() {
        return jaxbBindings;
    }

    /**
     * @param jaxbBinding The jaxbBindings to set.
     */
    public void addJaxbBindings(Element jaxbBinding) {
        if (jaxbBindings == null) return;
        this.jaxbBindings.add(jaxbBinding);
    }

    /**
     * @return the isProvider.
     */
    public Boolean isProvider() {
        return isProvider;
    }

    /**
     * @param isProvider The isProvider to set.
     */
    public void setProvider(Boolean isProvider) {
        this.isProvider = isProvider;
    }

    /**
     * @return Returns the methodName.
     */
    public CustomName getMethodName() {
        return methodName;
    }

    /**
     * @param methodName The methodName to set.
     */
    public void setMethodName(CustomName methodName) {
        this.methodName = methodName;
    }

    /**
     * @return Returns the parameter.
     */
    public Iterator<Parameter> parameters() {
        return parameters.iterator();
    }

    /**
     * @param parameter The parameter to set.
     */
    public void addParameter(Parameter parameter) {
        if (parameters == null) parameters = new ArrayList<Parameter>();
        parameters.add(parameter);
    }

    public String getParameterName(String msgName, String wsdlPartName, QName element, boolean wrapperStyle) {
        if (msgName == null || wsdlPartName == null || element == null || parameters == null) return null;
        for (Parameter param : parameters) {
            if (param.getMessageName().equals(msgName) && param.getPart().equals(wsdlPartName)) {
                if (wrapperStyle && (param.getElement() != null)) {
                    if (param.getElement().equals(element)) return param.getName();
                } else if (!wrapperStyle) {
                    return param.getName();
                }
            }
        }
        return null;
    }

    /**
     * @return Returns the className.
     */
    public CustomName getClassName() {
        return className;
    }

    /**
     * @param className The className to set.
     */
    public void setClassName(CustomName className) {
        this.className = className;
    }

    /**
     * @return Returns the exception.
     */
    public Exception getException() {
        return exception;
    }

    /**
     * @param exception The exception to set.
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }

    private String wsdlNamespace;

    private String wsdlLocation;

    private String node;

    private String version;

    private CustomName jaxwsPackage;

    private List<Parameter> parameters;

    private Boolean enableWrapperStyle;

    private Boolean enableAsyncMapping;

    private Boolean enableMimeContentMapping;

    private Boolean isProvider;

    private Exception exception;

    private Set<Element> jaxbBindings;

    private CustomName className;

    private CustomName methodName;
}
