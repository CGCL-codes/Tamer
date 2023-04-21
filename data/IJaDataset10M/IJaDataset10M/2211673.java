package com.idna.dm.service.execution.xml.extractor.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import com.idna.dm.service.execution.xml.extractor.domain.IdentifierList;
import com.idna.dm.service.execution.xml.extractor.domain.ParsedData;

public class AttributeParserDelegate extends UniqueInputRecordsParserDelegate {

    @Override
    public ParsedData parse(InputStream inputXml, IdentifierList requiredElements) {
        XMLStreamReader reader = readerProvider.getFilteredXMLStreamReader(inputXml);
        Map<String, String> uniqueMapOfElements = new HashMap<String, String>();
        try {
            while (reader.hasNext()) {
                if (reader.isStartElement()) {
                    String key = elementPathProvider.getKeyPath(reader, elementType);
                    if (!requiredElements.contains(key)) {
                        reader.next();
                        continue;
                    }
                    String value = elementPathProvider.getValueText(reader, elementType);
                    uniqueMapOfElements.put(key, value);
                } else throw new IllegalStateException(String.format("Element of type [%s] should have been a start element.", reader.getEventType()));
                reader.next();
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(String.format("[%s]: Error when parsing with the XMLStreamReader.", this.getClass()), e);
        } finally {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                throw new RuntimeException(String.format("[%s]: Error when closing the XMLStreamReader.", this.getClass()), e);
            }
        }
        Map<String, String> convertedData = elementMappingsHelper.convertValuesToIDs(uniqueMapOfElements);
        return getParsedDataFactory().createDecisionInputDataWithUniqueRecords(convertedData);
    }
}
