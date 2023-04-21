package net.sf.ruleminer.common.data.serializer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.sf.ruleminer.common.data.IAttribute;
import net.sf.ruleminer.common.data.ICategory;
import net.sf.ruleminer.common.data.IDataSet;
import net.sf.ruleminer.common.data.IRecord;
import net.sf.ruleminer.common.data.IValue;
import net.sf.ruleminer.common.data.parser.ParsingConstants;
import net.sf.ruleminer.util.ContractChecker;
import net.sf.ruleminer.util.xml.sax.ManualContentHandlerHelper;
import net.sf.ruleminer.util.xml.sax.RichAttributesImpl;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * @author Michal Burda
 * 
 */
public class Serializer {

    /**
	 * 
	 */
    private ManualContentHandlerHelper handler;

    /**
	 * @param handler
	 */
    public Serializer(ContentHandler handler) {
        super();
        ContractChecker.mustNotBeNull(handler, "handler");
        this.handler = new ManualContentHandlerHelper(handler);
    }

    /**
	 * @param attributes
	 * @throws SAXException
	 */
    public void serializeMeta(List<IAttribute> attributes) throws SAXException {
        this.handler.startElement(ParsingConstants.META_ELEMENT);
        serializeAttributes(attributes);
        this.handler.endElement();
    }

    /**
	 * @param attributes
	 * @throws SAXException
	 */
    public void serializeAttributes(List<IAttribute> attributes) throws SAXException {
        this.handler.startElement(ParsingConstants.ATTRIBUTES_ELEMENT);
        for (IAttribute attribute : attributes) {
            serializeAttribute(attribute);
        }
        this.handler.endElement();
    }

    /**
	 * @param attribute
	 * @throws SAXException
	 */
    public void serializeAttribute(IAttribute attribute) throws SAXException {
        assert attribute != null;
        RichAttributesImpl attrs = new RichAttributesImpl();
        attrs.addAttribute(ParsingConstants.ID_ATTRIBUTE, Integer.toString(attribute.getId()));
        this.handler.startElement(ParsingConstants.ATTRIBUTE_ELEMENT, attrs);
        this.handler.putElement(ParsingConstants.NAME_ELEMENT, attribute.getName());
        this.handler.putElement(ParsingConstants.TYPE_ELEMENT, attribute.getType().toString().toLowerCase());
        this.handler.putElement(ParsingConstants.NULL_ELEMENT, (attribute.getNull() ? "true" : "false"));
        serializeCategories(attribute.getCategories());
        this.handler.endElement();
    }

    /**
	 * @param categories
	 * @throws SAXException
	 */
    public void serializeCategories(List<ICategory> categories) throws SAXException {
        assert categories != null;
        if (categories.size() > 0) {
            this.handler.startElement(ParsingConstants.CATEGORIES_ELEMENT);
            for (ICategory category : categories) {
                serializeCategory(category);
            }
            this.handler.endElement();
        }
    }

    /**
	 * @param category
	 * @throws SAXException
	 */
    public void serializeCategory(ICategory category) throws SAXException {
        assert category != null;
        RichAttributesImpl attrs = new RichAttributesImpl();
        attrs.addAttribute(ParsingConstants.ID_ATTRIBUTE, Integer.toString(category.getId()));
        this.handler.putElement(ParsingConstants.CATEGORY_ELEMENT, attrs, category.getName());
    }

    /**
	 * @param dataSet
	 * @throws SAXException
	 */
    public void serializeDataSet(IDataSet dataSet) throws SAXException {
        this.handler.startDocument();
        this.handler.startElementWithNsDeclaration(ParsingConstants.DATASET_ELEMENT);
        serializeMeta(dataSet.getAttributes());
        serializeRecords(dataSet.getRecords());
        this.handler.endElement();
        this.handler.endDocument();
    }

    /**
	 * @param records
	 * @throws SAXException
	 */
    private void serializeRecords(Collection<IRecord> records) throws SAXException {
        this.handler.startElement(ParsingConstants.RECORDS_ELEMENT);
        for (IRecord record : records) {
            serializeRecord(record);
        }
        this.handler.endElement();
    }

    /**
	 * @param record
	 * @throws SAXException
	 */
    private void serializeRecord(IRecord record) throws SAXException {
        this.handler.startElement(ParsingConstants.RECORD_ELEMENT);
        for (Map.Entry<IAttribute, IValue> entry : record.getValues().entrySet()) {
            RichAttributesImpl attrs = new RichAttributesImpl();
            attrs.addAttribute(ParsingConstants.ATTRIBUTE_ATTRIBUTE, Integer.toString(entry.getKey().getId()));
            if (entry.getValue() != null) {
                this.handler.putElement(ParsingConstants.VALUE_ELEMENT, attrs, entry.getValue().getStringValue());
            }
        }
        this.handler.endElement();
    }
}
