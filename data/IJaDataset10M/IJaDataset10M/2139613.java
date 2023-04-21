package org.springframework.web.servlet.mvc.composition.providers;

import org.springframework.dao.services.RetrievalService;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.composition.FormBackerProvider;
import org.springframework.web.servlet.mvc.composition.ModelProvider;
import org.springframework.web.servlet.mvc.composition.ReferenceDataProvider;
import org.springframework.web.servlet.mvc.composition.RequestObjectProvider;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * Polymorphic implementation that uses a RetrievalService to fetch objects
 * given an identifier provided by a single parameter in the request.
 * <ul>
 * Implemented interfaces
 * <li>FormBackerProvider : the retrieved object becomes the form backing
 * object</li>
 * <li>RequestObjectProvider : returns the retrieved object</li>
 * <li>ReferenceDataProvider : adds the retrieved object to the reference data
 * map using page context key</li>
 * <li>ModelProvider : adds the retrieved object to the ModelAndView using page
 * context key</li>
 * </ul>
 * <ul>
 * Injected parameters:
 * <li>retrievalService : responsible for retrieving the object for the given
 * identifier</li>
 * <li>requestParameter : name of request parameter that must contain the
 * identifier</li>
 * <li>pageContextKey : the retrieved object can be placed in the model using
 * this key</li>
 * <li>parameterType : the expected request parameter value type, defaults to
 * String</li>
 * </ul>
 * <p/>
 * </p>
 * <p/>
 * 
 * @author Hector Rovira
 * @author Sam Alston
 */
public class RetrieveBySingleParameterProvider<T> implements FormBackerProvider<T>, RequestObjectProvider<T>, ReferenceDataProvider<T>, ModelProvider {

    private RetrievalService<T, Serializable> retrievalService;

    private RetrieveByParameterType parameterType = RetrieveByParameterType.String;

    private String requestParameter;

    private String pageContextKey;

    public RetrievalService<T, Serializable> getRetrievalService() {
        return retrievalService;
    }

    public void setRetrievalService(RetrievalService<T, Serializable> retrievalService) {
        this.retrievalService = retrievalService;
    }

    public String getPageContextKey() {
        return pageContextKey;
    }

    public void setPageContextKey(String pageContextKey) {
        this.pageContextKey = pageContextKey;
    }

    public String getRequestParameter() {
        return requestParameter;
    }

    public void setRequestParameter(String requestParameter) {
        this.requestParameter = requestParameter;
    }

    public RetrieveByParameterType getParameterType() {
        return parameterType;
    }

    public void setParameterType(RetrieveByParameterType parameterType) {
        this.parameterType = parameterType;
    }

    public T provideBacker(HttpServletRequest request, T command) throws Exception {
        return getRequestObject(request);
    }

    @SuppressWarnings("unchecked")
    public void provideReferences(HttpServletRequest request, T command, Map referenceData, Errors errors) throws Exception {
        referenceData.put(getPageContextKey(), getRequestObject(request));
    }

    public void provideModel(ModelAndView modelAndView, HttpServletRequest request) throws Exception {
        modelAndView.addObject(getPageContextKey(), getRequestObject(request));
    }

    public T getRequestObject(HttpServletRequest request) throws Exception {
        return getRetrievalService().retrieve(getIdentifier(request));
    }

    private Serializable getIdentifier(HttpServletRequest request) throws Exception {
        switch(getParameterType()) {
            case Integer:
                return ServletRequestUtils.getRequiredIntParameter(request, getRequestParameter());
            case Double:
                return ServletRequestUtils.getRequiredDoubleParameter(request, getRequestParameter());
            case Float:
                return ServletRequestUtils.getRequiredFloatParameter(request, getRequestParameter());
            case Long:
                return ServletRequestUtils.getRequiredLongParameter(request, getRequestParameter());
            default:
                return ServletRequestUtils.getRequiredStringParameter(request, getRequestParameter());
        }
    }
}
