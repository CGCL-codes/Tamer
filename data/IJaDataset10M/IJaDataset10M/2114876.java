package org.kablink.teaming.module.definition.index;

import java.util.Set;
import java.util.Map;
import org.apache.lucene.document.Field;
import org.kablink.teaming.search.BasicIndexUtils;
import org.kablink.util.search.Constants;

public abstract class FieldBuilderGeneric extends AbstractFieldBuilder {

    protected Field[] build(String dataElemName, Set dataElemValue, Map args) {
        String strToIndex = getStringToIndex(dataElemValue);
        if (strToIndex != null && !strToIndex.equals("")) {
            Field field = new Field(getSearchFieldName(dataElemName), strToIndex, getFieldStore(), getFieldIndex());
            Field sortField = null;
            if (!getSearchFieldName(dataElemName).equals(getSortFieldName(dataElemName))) {
                sortField = new Field(getSortFieldName(dataElemName), strToIndex.toLowerCase(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS);
            }
            Field allTextField = null;
            if (!isFieldsOnly(args)) allTextField = BasicIndexUtils.allTextField(strToIndex);
            if (sortField != null) {
                if (allTextField != null) return new Field[] { field, sortField, allTextField }; else return new Field[] { field, sortField };
            } else {
                if (allTextField != null) return new Field[] { field, allTextField }; else return new Field[] { field };
            }
        } else {
            return new Field[0];
        }
    }

    protected String getStringToIndex(Set dataElemValue) {
        String result = null;
        Object val = getFirstElement(dataElemValue);
        if (val instanceof String) {
            String sVal = (String) val;
            sVal = sVal.trim();
            if (sVal.length() > 0) result = sVal;
        }
        return result;
    }

    public Field.Store getFieldStore() {
        return Field.Store.YES;
    }

    public Field.Index getFieldIndex() {
        return Field.Index.ANALYZED;
    }

    @Override
    public String getSearchFieldName(String dataElemName) {
        return dataElemName;
    }

    @Override
    public String getSortFieldName(String dataElemName) {
        return Constants.SORT_FIELD_PREFIX + getSearchFieldName(dataElemName);
    }
}
