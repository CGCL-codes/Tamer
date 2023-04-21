package org.pojonode.codegen.config;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.pojonode.PojonodeException;
import org.pojonode.aop.annotations.*;
import org.pojonode.model.metamodel.PojonodeModel;
import org.pojonode.model.metamodel.attributes.AlfrescoPropertyType;
import org.pojonode.model.metamodel.attributes.AssociationInfo;
import org.pojonode.model.metamodel.attributes.PropertyInfo;
import org.pojonode.model.metamodel.types.AspectInfo;
import org.pojonode.model.metamodel.types.EntityInfo;
import org.pojonode.model.metamodel.types.TypeInfo;
import org.pojonode.util.PojonodeConstants;
import org.pojonode.util.Utils;
import org.pojonode.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author Cosmin Marginean, Nov 2, 2010
 */
public class AlfrescoModelXmlGenerator extends AbstractAlfrescoConfigGenerator {

    private static final Logger log = Logger.getLogger(AlfrescoModelXmlGenerator.class);

    @Override
    public void generateConfig(PojonodeModel pojonodeModel, OutputStream outputStream) throws PojonodeException {
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document document = docBuilder.newDocument();
            Element rootElement = document.createElement("model");
            rootElement.setAttribute("name", pojonodeModel.getName());
            rootElement.setAttribute("xmlns", "http://www.alfresco.org/model/dictionary/1.0");
            document.appendChild(rootElement);
            XmlUtils.appendTextChild(rootElement, "description", pojonodeModel.getDescription());
            XmlUtils.appendTextChild(rootElement, "author", pojonodeModel.getAuthor());
            XmlUtils.appendTextChild(rootElement, "version", pojonodeModel.getVersion());
            Element importsElement = document.createElement("imports");
            rootElement.appendChild(importsElement);
            XmlUtils.addNamespaceMapping(importsElement, "d", "http://www.alfresco.org/model/dictionary/1.0");
            XmlUtils.addNamespaceMapping(importsElement, "cm", "http://www.alfresco.org/model/content/1.0");
            Element nameSpacesElement = document.createElement("namespaces");
            rootElement.appendChild(nameSpacesElement);
            for (String namespacePrefix : getNamespacePrefixResolver().getPrefixes()) {
                if (getNamespacePrefixResolver().isPojonodeNamespace(namespacePrefix)) {
                    XmlUtils.addNamespaceElement(nameSpacesElement, namespacePrefix, getNamespacePrefixResolver().getNamespaceURI(namespacePrefix));
                }
            }
            Element constraintsElement = document.createElement("constraints");
            rootElement.appendChild(constraintsElement);
            Element typesElement = document.createElement("types");
            rootElement.appendChild(typesElement);
            Element aspectsElement = document.createElement("aspects");
            rootElement.appendChild(aspectsElement);
            for (TypeInfo typeInfo : pojonodeModel.getTypes()) {
                handleEntity(pojonodeModel, document, typesElement, typeInfo, constraintsElement, "type");
            }
            for (AspectInfo aspectInfo : pojonodeModel.getAspects()) {
                handleEntity(pojonodeModel, document, aspectsElement, aspectInfo, constraintsElement, "aspect");
            }
            XmlUtils.serializeDocument(document, outputStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new PojonodeException(e.getMessage(), e);
        }
    }

    @Override
    public String getMimeType() {
        return PojonodeConstants.MIMETYPE_XML;
    }

    private void handleEntity(PojonodeModel pojonodeModel, Document document, Element typesElement, EntityInfo entityInfo, Element constraintsElement, String entityName) throws PojonodeException {
        if (PojonodeConstants.isPojoCoreType(entityInfo.getPojoClass())) {
            return;
        }
        Element crtTypeElement = document.createElement(entityName);
        crtTypeElement.setAttribute("name", entityInfo.getType().getPrefixString());
        String parentType = entityInfo.getParentPrefixString();
        if (parentType != null) {
            XmlUtils.appendTextChild(crtTypeElement, "parent", parentType);
        }
        typesElement.appendChild(crtTypeElement);
        Element propertiesElement = document.createElement("properties");
        Element associationsElement = document.createElement("associations");
        crtTypeElement.appendChild(propertiesElement);
        crtTypeElement.appendChild(associationsElement);
        for (PropertyInfo propertyInfo : entityInfo.getProperties()) {
            if (!propertyInfo.isInherited()) {
                handleProperty(pojonodeModel, propertyInfo, propertiesElement, constraintsElement);
            }
        }
        for (AssociationInfo assocInfo : entityInfo.getAssociations()) {
            if (!assocInfo.isInherited()) {
                handleAssociation(pojonodeModel, assocInfo, associationsElement);
            }
        }
        if (entityInfo instanceof TypeInfo) {
            Element mandatoryAspectsElement = document.createElement("mandatory-aspects");
            crtTypeElement.appendChild(mandatoryAspectsElement);
            Set<String> aspects = ((TypeInfo) entityInfo).getMandatoryAspectClassNames();
            if (aspects != null) {
                for (String aspectClassName : aspects) {
                    AspectInfo aspectInfo = pojonodeModel.getAspect(aspectClassName);
                    XmlUtils.appendTextChild(mandatoryAspectsElement, "aspect", aspectInfo.getType().getPrefixString());
                }
            }
        }
    }

    private void handleAssociation(PojonodeModel pojonodeModel, AssociationInfo assocInfo, Element associationsElement) throws PojonodeException {
        Element assocElement = associationsElement.getOwnerDocument().createElement(assocInfo.isChildAssociation() ? "child-association" : "association");
        assocElement.setAttribute("name", assocInfo.getQname().getPrefixString());
        XmlUtils.appendTextChild(assocElement, "title", assocInfo.getTitle());
        Element sourceElement = associationsElement.getOwnerDocument().createElement("source");
        XmlUtils.appendTextChild(sourceElement, "mandatory", assocInfo.isSourceMandatory() + "");
        Class<? extends Annotation> annotationClass = assocInfo.getAnnotationClass();
        XmlUtils.appendTextChild(sourceElement, "many", (ManyToMany.class.equals(annotationClass) || ManyToOne.class.equals(annotationClass)) + "");
        assocElement.appendChild(sourceElement);
        Element targetElement = associationsElement.getOwnerDocument().createElement("target");
        Class targetType = assocInfo.getTargetClass();
        EntityInfo type = pojonodeModel.getType(targetType);
        if (null == type) {
            type = pojonodeModel.getAspect(targetType);
        }
        XmlUtils.appendTextChild(targetElement, "class", type.getType().getPrefixString());
        XmlUtils.appendTextChild(targetElement, "mandatory", assocInfo.isTargetMandatory() + "");
        XmlUtils.appendTextChild(targetElement, "many", (OneToMany.class.equals(annotationClass) || ManyToMany.class.equals(annotationClass)) + "");
        assocElement.appendChild(targetElement);
        associationsElement.appendChild(assocElement);
    }

    private void handleProperty(PojonodeModel pojonodeModel, PropertyInfo propertyInfo, Element propertiesElement, Element constraintsElement) throws PojonodeException {
        Element propertyElement = propertiesElement.getOwnerDocument().createElement("property");
        propertyElement.setAttribute("name", propertyInfo.getQname().getPrefixString());
        XmlUtils.appendTextChild(propertyElement, "title", propertyInfo.getTitle());
        XmlUtils.appendTextChild(propertyElement, "type", AlfrescoPropertyType.getAlfrescoPropertyType(propertyInfo.getTargetClass()));
        if (propertyInfo.isMandatory()) {
            XmlUtils.appendTextChild(propertyElement, "mandatory", "true");
        }
        if (!StringUtils.isBlank(propertyInfo.getDefaultValue())) {
            XmlUtils.appendTextChild(propertyElement, "default", propertyInfo.getDefaultValue());
        }
        if (propertyInfo.isMultiple()) {
            XmlUtils.appendTextChild(propertyElement, "multiple", "true");
        }
        propertiesElement.appendChild(propertyElement);
        if (propertyInfo.getTargetClass().isEnum()) {
            handleEnumField(pojonodeModel, propertyInfo, constraintsElement);
        }
    }

    private void handleEnumField(PojonodeModel pojonodeModel, PropertyInfo propertyInfo, Element constraintsElement) throws PojonodeException {
        String constraintName = null;
        Class<?> fieldType = propertyInfo.getTargetClass();
        ListConstraint propConstraintAnnotation = fieldType.getAnnotation(ListConstraint.class);
        if (propConstraintAnnotation != null) {
            constraintName = propConstraintAnnotation.name();
        } else {
            constraintName = Utils.getPrefix(propertyInfo.getQname().getPrefixString()) + ":" + fieldType.getSimpleName();
        }
        if (!pojonodeModel.constraintExists(constraintName)) {
            Field[] enumFields = fieldType.getFields();
            Document doc = constraintsElement.getOwnerDocument();
            Element constraintElement = doc.createElement("constraint");
            constraintElement.setAttribute("name", constraintName);
            constraintElement.setAttribute("type", "LIST");
            constraintsElement.appendChild(constraintElement);
            Element paramElement = doc.createElement("parameter");
            paramElement.setAttribute("name", "allowedValues");
            constraintElement.appendChild(paramElement);
            Element valuesListElement = doc.createElement("list");
            paramElement.appendChild(valuesListElement);
            for (Field enumField : enumFields) {
                if (enumField.getType().getCanonicalName().equals(fieldType.getCanonicalName())) {
                    ListConstraintValue constraintValueAnnotation = enumField.getAnnotation(ListConstraintValue.class);
                    XmlUtils.appendTextChild(valuesListElement, "value", constraintValueAnnotation != null ? constraintValueAnnotation.value() : enumField.getName());
                }
            }
            pojonodeModel.addConstraint(constraintName);
        }
    }
}
