package org.databene.platform.dbunit;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import org.databene.benerator.engine.BeneratorContext;
import org.databene.model.data.ComplexTypeDescriptor;
import org.databene.model.data.Entity;
import org.databene.script.ScriptUtil;
import org.databene.webdecs.DataContainer;

/**
 * Parses a flat DbUnit dataset file.<br/><br/>
 * Created: 20.09.2011 07:53:15
 * @since 0.7.2
 * @author Volker Bergmann
 */
public class FlatDbUnitEntityIterator extends AbstractDbUnitEntityIterator {

    public FlatDbUnitEntityIterator(String uri, BeneratorContext context) {
        super(uri, context);
        DbUnitUtil.skipRootElement(reader);
    }

    public DataContainer<Entity> next(DataContainer<Entity> container) {
        DbUnitUtil.skipNonStartTags(reader);
        if (reader.getEventType() == XMLStreamConstants.END_DOCUMENT) return null;
        QName name = reader.getName();
        Row row = parseDataset(name.getLocalPart());
        Entity result = mapToEntity(row);
        return container.setData(result);
    }

    private Row parseDataset(String tableName) {
        int columnCount = reader.getAttributeCount();
        String[] columnNames = new String[columnCount];
        String[] cellValues = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = reader.getAttributeLocalName(i);
            cellValues[i] = reader.getAttributeValue(i);
        }
        Row row = new Row(tableName, columnNames, cellValues);
        logger.debug("parsed row {}", row);
        return row;
    }

    protected Entity mapToEntity(Row row) {
        String[] cells = row.getValues();
        ComplexTypeDescriptor descriptor = getType(row);
        Entity result = new Entity(descriptor);
        for (int i = 0; i < cells.length; i++) {
            String rowValue = String.valueOf(ScriptUtil.evaluate(cells[i], context));
            result.setComponent(row.getColumnName(i), rowValue);
        }
        return result;
    }
}
