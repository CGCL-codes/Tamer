package com.sitescape.team.module.definition.index;

import java.util.Set;
import java.util.Map;
import org.apache.lucene.document.Field;
import com.sitescape.team.search.BasicIndexUtils;

/**
 *
 * @author Jong Kim
 */
public class FieldBuilderCheck extends AbstractFieldBuilder {

    public String makeFieldName(String dataElemName) {
        return dataElemName;
    }

    protected Field[] build(String dataElemName, Set dataElemValue, Map args) {
        Boolean val = (Boolean) getFirstElement(dataElemValue);
        if (val == null) {
            return new Field[0];
        } else {
            Field field = new Field(makeFieldName(dataElemName), val.toString(), Field.Store.NO, Field.Index.UN_TOKENIZED);
            return new Field[] { field };
        }
    }
}
