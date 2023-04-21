package org.kablink.teaming.module.definition.index;

import java.util.Map;
import java.util.Set;
import org.apache.lucene.document.Field;
import org.dom4j.Element;
import org.kablink.teaming.domain.DefinableEntity;
import org.kablink.teaming.module.definition.DefinitionModule;
import org.kablink.teaming.search.BasicIndexUtils;

public class FieldBuilderCheckbox extends AbstractFieldBuilder {

    public Field[] buildField(DefinableEntity entity, String dataElemName, Map args) {
        Set dataElemValue = getEntryElementValue(entity, dataElemName);
        Element entryElement = (Element) args.get(DefinitionModule.DEFINITION_ELEMENT);
        String caption = getEntryElementCaption(entity, dataElemName, entryElement);
        args.put(DefinitionModule.INDEX_CAPTION, caption);
        if (dataElemValue != null) return build(dataElemName, dataElemValue, args); else return null;
    }

    protected Field[] build(String dataElemName, Set dataElemValue, Map args) {
        String caption = (String) args.get(DefinitionModule.INDEX_CAPTION);
        Boolean val = (Boolean) getFirstElement(dataElemValue);
        if (val == null) {
            return new Field[0];
        }
        Field field = new Field(getSearchFieldName(dataElemName), val.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS);
        if (!isFieldsOnly(args)) {
            Field allTextField = BasicIndexUtils.allTextField(caption);
            return new Field[] { allTextField, field };
        } else {
            return new Field[] { field };
        }
    }

    @Override
    public String getSearchFieldName(String dataElemName) {
        return dataElemName;
    }

    @Override
    public String getSortFieldName(String dataElemName) {
        return getSearchFieldName(dataElemName);
    }

    @Override
    public Field.Index getFieldIndex() {
        return Field.Index.NOT_ANALYZED_NO_NORMS;
    }

    @Override
    public Field.Store getFieldStore() {
        return Field.Store.YES;
    }
}
