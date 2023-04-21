package org.vgr.module.javascript;

import java.util.Collections;
import java.util.List;
import org.alfresco.repo.dictionary.constraint.ListOfValuesConstraint;
import org.alfresco.repo.processor.BaseProcessorExtension;
import org.alfresco.service.cmr.dictionary.ConstraintDefinition;
import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.cmr.dictionary.PropertyDefinition;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ListConstraintScript extends BaseProcessorExtension {

    private static final Log logger = LogFactory.getLog(TypeScript.class);

    private DictionaryService dictionaryService;

    public ListConstraintScript() {
        if (logger.isDebugEnabled()) {
            logger.debug("ListConstraintScript Constructor Called");
        }
    }

    protected PropertyDefinition getProperty(String propertyName) {
        QName name = QName.createQName(propertyName);
        PropertyDefinition propertyDefinition = this.dictionaryService.getProperty(name);
        return propertyDefinition;
    }

    public List<String> getListConstraintValues(String propertyName) {
        PropertyDefinition propertyDefinition = this.getProperty(propertyName);
        List<ConstraintDefinition> constraints = propertyDefinition.getConstraints();
        List<String> allowedValues = null;
        for (ConstraintDefinition c : constraints) {
            if (c.getConstraint() instanceof ListOfValuesConstraint) {
                ListOfValuesConstraint lovConstraint = (ListOfValuesConstraint) c.getConstraint();
                allowedValues = lovConstraint.getAllowedValues();
            }
        }
        return allowedValues;
    }

    public void setDictionaryService(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }
}
