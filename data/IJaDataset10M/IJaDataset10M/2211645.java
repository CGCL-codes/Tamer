package org.proclos.etlcore.source.processor;

import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.proclos.etlcore.node.Row;
import org.proclos.etlcore.node.Tree;
import org.proclos.etlcore.source.ITreeSource;

public class ParentChildWeightAttributesView extends ParentChildWeightView {

    private AttributesUtil attributes;

    private static final Log log = LogFactory.getLog(ParentChildWeightAttributesView.class);

    public Row setupRow(ITreeSource source, boolean hasRoot) {
        Row row = super.setupRow(source, hasRoot);
        attributes = new AttributesUtil(source.getTreeManager().getAttributes());
        row.addColumns(attributes.getAliases());
        row.addColumns(attributes.getAttributes());
        return row;
    }

    public Row fillRow(int current) {
        Row arow = getSourceRows().get(current);
        for (int i = 0; i < attributes.getAliases().size(); i++) {
            attributes.fillAlias((Tree) arow.getColumn(1), i);
        }
        for (int i = 0; i < attributes.getAttributes().size(); i++) {
            attributes.fillAttribute((Tree) arow.getColumn(1), i);
        }
        return super.fillRow(current);
    }

    public String[] getColumn(int pos) {
        if (pos < 3) return super.getColumn(pos);
        ArrayList<String> result = new ArrayList<String>();
        int attributeStart = attributes.getAliases().size() + 3;
        if ((pos > 2) && (pos < attributeStart)) {
            for (int i = 0; i < size(); i++) {
                Row arow = getSourceRows().get(i);
                result.add(attributes.fillAlias((Tree) arow.getColumn(1), pos - 3).getValueAsString());
            }
        } else if ((pos >= attributeStart) && (pos < getTargetRow().size())) {
            for (int i = 0; i < size(); i++) {
                Row arow = getSourceRows().get(i);
                result.add(attributes.fillAttribute((Tree) arow.getColumn(1), pos - attributeStart).getValueAsString());
            }
        } else log.error("Column position " + pos + " out of range: " + size());
        return result.toArray(new String[result.size()]);
    }
}
