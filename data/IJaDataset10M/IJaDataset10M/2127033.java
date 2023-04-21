package eu.annocultor.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import eu.annocultor.TestEnvironment;
import eu.annocultor.api.Factory;
import eu.annocultor.context.Namespaces;
import eu.annocultor.objects.TestRulesSetup;
import eu.annocultor.path.Path;
import eu.annocultor.rules.ObjectRuleImpl;
import eu.annocultor.triple.LiteralValue;

public class ConverterHandlerDataObjectsTest extends TestRulesSetup {

    final String fieldId = "ID";

    final String fieldName = "Name";

    List<String> dataObjects = new ArrayList<String>();

    public void testDO() throws Exception {
        final Path recordSeparatingPath = new Path("");
        task = Factory.makeTask("bibliopolis", "terms", "Bibliopolis, KB", Namespaces.DC, new TestEnvironment());
        objectRule = ObjectRuleImpl.makeObjectRule(task, recordSeparatingPath, new Path(fieldId), new Path(""), null, true);
        assertEquals(1, task.getRuleForSourcePath(recordSeparatingPath).size());
        ConverterHandler defaultHandler = new ConverterHandler(task) {

            @Override
            protected void processDataObject() throws Exception {
                ListOfValues values = getValues(new Path(fieldName));
                Collections.sort(values);
                dataObjects.add(StringUtils.join(values, ","));
            }
        };
        ConverterHandlerDataObjects handler = new ConverterHandlerDataObjects(defaultHandler, recordSeparatingPath);
        handler.setAggregate(false);
        imitateDataInsertionSequence(handler);
        assertEquals(4, dataObjects.size());
        assertEquals("2A,2B", dataObjects.get(1));
        assertEquals("1A", dataObjects.get(2));
        handler.setAggregate(true);
        imitateDataInsertionSequence(handler);
        assertEquals(3, dataObjects.size());
        assertEquals("1A,2A,2B", dataObjects.get(1));
        assertEquals("3A", dataObjects.get(2));
    }

    private void imitateDataInsertionSequence(ConverterHandlerDataObjects handler) throws Exception {
        dataObjects.clear();
        handler.startDocument();
        makeObject(handler, "1", "1A", "1B");
        makeObject(handler, "2", "2A", "2B");
        makeObject(handler, "2", "1A");
        makeObject(handler, "3", "3A");
        handler.endDocument();
    }

    private void makeObject(ConverterHandlerDataObjects handler, String id, String... names) throws Exception {
        handler.attemptDataObjectChange(id);
        handler.addField(fieldId, new LiteralValue(id));
        for (String name : names) {
            handler.addField(fieldName, new LiteralValue(name));
        }
    }
}
