package com.abra.j2xb.axis2;

import com.abra.j2xb.beans.xmlModel.MoXmlSchema;
import com.abra.j2xb.beans.xmlModel.MoXmlBindingModel;
import com.abra.j2xb.beans.model.*;
import com.abra.j2xb.beans.exceptions.MOBeansException;
import com.abra.j2xb.annotations.xmlAnnotations.MOValidationString;
import com.abra.j2xb.annotations.xmlAnnotations.MOValidationCollection;
import com.abra.j2xb.axis2.xmlModel.J2xbWebMethodElement;
import com.abra.j2xb.axis2.xmlModel.J2xbParam;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: yoava
 * Date: 11/06/2008
 * Time: 19:36:40
 * To change this template use File | Settings | File Templates.
 */
public class J2xbWebException implements MOJavaValueDescriptor, MOAnnotationLocation {

    private J2xbWebService webService;

    private Class<?> exceptionClass;

    private List<MOPropertySimpleTypeDescriptor> simpleTypeDescriptors = new ArrayList<MOPropertySimpleTypeDescriptor>();

    private MOPersistentBeanDescriptor exceptionBeanDescriptor;

    private J2xbWebExceptionElement webExceptionElement;

    public J2xbWebException(J2xbWebService webService, Class<?> clazz) throws MOBeansException {
        this.webService = webService;
        this.exceptionClass = clazz;
        getSimpleTypeDescriptors().add(new MOPropertySimpleTypeDescriptor(this, getValueType(), null, null, null, webService.getXmlBindingModel().getPropertyEditorRegistry()));
        MoXmlBindingModel xmlBindingModel = webService.getXmlBindingModel();
        if (xmlBindingModel.hasBean(exceptionClass)) exceptionBeanDescriptor = xmlBindingModel.getBeanDescriptor(exceptionClass); else {
            exceptionBeanDescriptor = xmlBindingModel.addBean(exceptionClass);
        }
    }

    public void createXmlElement(MoXmlSchema xmlServiceSchema) {
        webExceptionElement = new J2xbWebExceptionElement(this);
        xmlServiceSchema.addElement(webExceptionElement);
    }

    public String getXmlName() {
        return exceptionClass.getSimpleName();
    }

    public String getDefaultValueAsString() {
        return null;
    }

    public boolean isXmlAttribute() {
        return false;
    }

    public boolean isXmlOptional() {
        return false;
    }

    public String[] getEnumSetDefaults() {
        return new String[0];
    }

    public MOValidationString getValidationString() {
        return null;
    }

    public MOValidationCollection getValidationCollection() {
        return null;
    }

    public Class<?> getCollectionItemClass() {
        return null;
    }

    public boolean isXmlListStyle() {
        return false;
    }

    public boolean isXmlValueUnion() {
        return false;
    }

    public MOBeanDescriptor getBeanValueBeanDescriptor() {
        return exceptionBeanDescriptor;
    }

    public String getLocation() {
        return String.format("Exception [%s]", exceptionClass.getName());
    }

    public Class<?> getValueType() {
        return exceptionClass;
    }

    public List<MOPropertySimpleTypeDescriptor> getSimpleTypeDescriptors() {
        return simpleTypeDescriptors;
    }

    public Object getDefaultValue() {
        return null;
    }

    public boolean isXmlGlobalBeanRef() {
        return false;
    }

    public MOPersistentBeanDescriptor getExceptionBeanDescriptor() {
        return exceptionBeanDescriptor;
    }

    public J2xbWebService getWebService() {
        return webService;
    }

    public J2xbWebExceptionElement getWebExceptionElement() {
        return webExceptionElement;
    }
}
